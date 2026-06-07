package jueguito.juegopracticafinal.Modelo.Core;

import com.google.gson.Gson;
import jueguito.juegopracticafinal.Modelo.Mundo.PuertaData;
import jueguito.juegopracticafinal.Modelo.Mundo.ZonaData;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.TADs.InterfazIterador;
import jueguito.juegopracticafinal.TADs.Lista;

import java.io.IOException;
import java.io.InputStreamReader;

//Gestiona la carga del mapa del juego, incluyendo zonas, puertas y sus conexiones
public class MapaLoader {
    private MapaLoader() {
        /* This utility class should not be instantiated */
    }


    //MÉTODOS

    //Carga todas las zonas del juego desde archivos JSON y las convierte en objetos Zona
    public static Lista<Zona> cargarZonas(){

        ZonaData[] datos = null;
        try (var is = MapaLoader.class.getResourceAsStream("/datosJSON/habitaciones.json")) {
            if (is != null) {
                datos = new Gson().fromJson(new InputStreamReader(is), ZonaData[].class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar habitaciones.json", e);
        }

        Lista<Zona> zonas = new Lista<>();

        if(datos != null){

            for(ZonaData d : datos){
                Zona z = crearDesdeTexto(d.id, d.mapa);
                zonas.add(z);
            }
        }

        return zonas;
    }

    //Carga todas las puertas del juego desde JSON y las convierte en objetos Puerta
    public static Lista<Puerta> cargarPuertas(){

        PuertaData[] datos = null;
        try (var is = MapaLoader.class.getResourceAsStream("/datosJSON/puertas.json")) {
            if (is != null) {
                datos = new Gson().fromJson(new InputStreamReader(is), PuertaData[].class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar puertas.json", e);
        }

        Lista<Puerta> puertas = new Lista<>();

        if(datos != null){

            for(PuertaData d : datos){
                puertas.add(
                        new Puerta(
                                d.zonaOrigen,
                                d.zonaDestino,
                                d.xOrigen,
                                d.yOrigen,
                                d.xDestino,
                                d.yDestino
                        )
                );
            }
        }

        return puertas;
    }

    //Convierte una representación en texto del mapa en una estructura de celdas y crea una Zona
    private static Zona crearDesdeTexto(int id ,String[] mapa){

        Celda[][] celdas = new Celda[mapa.length][];

        for(int y = 0; y < mapa.length; y++){

            celdas[y] = new Celda[mapa[y].length()];

            for(int x = 0; x < mapa[y].length(); x++){

                celdas[y][x] = switch (mapa[y].charAt(x)) {

                    case '#' -> new Celda(TipoCelda.PARED);
                    case '_' -> new Celda(TipoCelda.SUELO);
                    case 'P' -> new Celda(TipoCelda.PUERTA);
                    case '?' -> new Celda(TipoCelda.INTERACTUABLE);
                    case '~' -> new Celda(TipoCelda.AGUA);
                    case '0' -> new Celda(TipoCelda.VACIO);
                    case 'S' -> new Celda(TipoCelda.SALIDA);

                    default -> throw new IllegalArgumentException(
                            "Símbolo desconocido: " + mapa[y].charAt(x)
                    );
                };
            }
        }
        return new Zona(id, " ", celdas);
    }

    //Conecta las puertas entre zonas y actualiza el grafo de zonas con sus relaciones
    public static void conectarPuertas(GrafoZonas grafo, Lista<Puerta> puertas){

        if(puertas == null){
            return;
        }

        InterfazIterador<Puerta> it = puertas.iterador();
        while (it.hasNext()) {
            Puerta puerta = it.next();

            if(puerta == null){
                continue;
            }

            int zonaOrigen = puerta.getZonaOrigen();
            int zonaDestino = puerta.getZonaDestino();

            int xOrigen = puerta.getXOrigen();
            int yOrigen = puerta.getYOrigen();

            int xDestino = puerta.getXDestino();
            int yDestino = puerta.getYDestino();

            Zona origen = grafo.getZona(zonaOrigen);

            if(origen != null && origen.esValida(yOrigen, xOrigen)){

                Celda celdaOrigen = origen.getCelda(yOrigen, xOrigen);

                celdaOrigen.setTipoCelda(TipoCelda.PUERTA);
                celdaOrigen.setPuerta(puerta);

                origen.addPuerta(celdaOrigen);
            }

            Zona destino = grafo.getZona(zonaDestino);

            if(destino != null && destino.esValida(yDestino, xDestino)){

                Celda celdaDestino = destino.getCelda(yDestino, xDestino);

                celdaDestino.setTipoCelda(TipoCelda.PUERTA);
                celdaDestino.setPuerta(puerta);

                destino.addPuerta(celdaDestino);
            }

            grafo.conectar(zonaOrigen, zonaDestino);
        }
    }
}