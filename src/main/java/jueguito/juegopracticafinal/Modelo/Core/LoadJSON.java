package jueguito.juegopracticafinal.Modelo.Core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Log.EntradaLog;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.GrafoZonas;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.TADs.Lista;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoadJSON {

    private static final Gson GSON = new Gson();
    private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

    private static class PartidaData{
        String nombreJugador;
        int vidaActual;
        int vidaMax;
        int ataqueBase;
        int defensaBase;
        int rangoMov;
        int posRow;
        int posCol;
        String[] objetosInventario;
        String[] objetosEquipamiento;
        String nombreGato;
        boolean gatoEncontrado;
        int idZonaActual;
        int turnoActual;
        String estadoActual;
        String[] entradasLog;
        boolean[] zonasVisitadas;
        String[] enemigosJson;
        int[] objetosTipo;
        int[] objetosAtqBonus;
        int[] objetosDefBonus;
        int[] objetosVidaBonus;
        int[] objetosRangoBonus;
        int[] objetosUsos;
        String[] objetosSlot;
    }

    public static void guardarPartida(Partida partida,String ruta){
        PartidaData partidaData = new PartidaData();
        Jugador jugador = partida.getJugador();

        partidaData.nombreJugador = jugador.getNombre();
        partidaData.vidaActual = jugador.getEstadisticas().getVidaActual();
        partidaData.vidaMax = jugador.getEstadisticas().getVidaMax();
        partidaData.ataqueBase = jugador.getEstadisticas().getAtaqueBase();
        partidaData.defensaBase = jugador.getEstadisticas().getDefensaBase();
        partidaData.rangoMov = jugador.getEstadisticas().getRangoMov();
        partidaData.posRow = jugador.getPosicion().getRow();
        partidaData.posCol = jugador.getPosicion().getCol();

        //Guardar objetos del inventario

        int numObj = jugador.getInventario().size();

        partidaData.objetosInventario = new String[numObj];
        partidaData.objetosTipo = new int[numObj];
        partidaData.objetosAtqBonus = new int[numObj];
        partidaData.objetosDefBonus = new int[numObj];
        partidaData.objetosVidaBonus = new int[numObj];
        partidaData.objetosRangoBonus = new int[numObj];
        partidaData.objetosUsos = new int[numObj];
        partidaData.objetosSlot = new String[numObj];


        for (int i = 0; i < numObj; i++) {
            Objeto o = jugador.getInventario().getObjeto(i);
            partidaData.objetosInventario[i] = o.getNombre();
            partidaData.objetosTipo[i] = o.getTipo().ordinal();
            partidaData.objetosAtqBonus[i] = o.getAtaqueBonus();
            partidaData.objetosDefBonus[i] = o.getDefensaBonus();
            partidaData.objetosVidaBonus[i] = o.getVidaBonus();
            partidaData.objetosRangoBonus[i] = o.getRangoBonus();
            partidaData.objetosUsos[i] = o.getUsosRestantes();
            if(o.getSlot() != null){
                partidaData.objetosSlot[i] = o.getSlot().name();
            }
            else{
                partidaData.objetosSlot[i] = "";
            }
        }

        //Guardar objetos en equipamiento

        partidaData.objetosEquipamiento = new String[2];
        for(int i = 0; i < 2; i++){
            if(jugador.getEquipamiento()[i] != null){
                partidaData.objetosEquipamiento[i] = jugador.getEquipamiento()[i].getNombre();
            }
        }

        Gato gato = partida.getGato();
        partidaData.nombreGato = gato.getNombre();
        partidaData.gatoEncontrado = partida.isGatoEncontrado();

        partidaData.idZonaActual = partida.getIdZonaActual();
        partidaData.turnoActual = partida.getTurnoActual();
        partidaData.estadoActual = partida.getEstadoActual().name();

        Lista<EntradaLog> logs = partida.getLog().getEntradas();
        partidaData.entradasLog = new String[logs.getSize()];
        for(int i = 0; i <  logs.getSize(); i++){
            partidaData.entradasLog[i] = logs.get(i).getMensaje();
        }

        //Guardar zonas visitadas

        int numZonas = Configuracion.get().zona.numZonas;
        partidaData.zonasVisitadas = new boolean[numZonas];
        for(int i = 0; i < numZonas; i++){
            Zona z = partida.getGrafoZonas().getZona(i);
            partidaData.zonasVisitadas[i] = (z != null && z.isVisitada());
        }

        //Guardar enemigos en zona actual

        Zona zActual = partida.getZonaActual();
        Lista<String> enemigos = new Lista<>();
        for(int r = 0; r < zActual.getRows(); r++){
            for(int c = 0; c < zActual.getCols(); c++){
                Celda celda = zActual.getCelda(r, c);
                if(celda != null && celda.getEntidad() instanceof Enemigo e && e.estarVivo()){
                    enemigos.add(
                            e.getNombre() + "-" + e.getEstadisticas().getVidaActual() + "-" + e.getEstadisticas().getAtaqueBase() + "-" + e.getEstadisticas().getDefensaBase() + "-" + r + c
                    );
                }
            }
        }

        partidaData.enemigosJson = new String[enemigos.getSize()];
        for(int i = 0; i < enemigos.getSize(); i++){
            partidaData.enemigosJson[i] = enemigos.get(i);
        }

        try (FileWriter w = new FileWriter(ruta)) {
            GSON_PRETTY.toJson(partidaData, w);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar partida: " + e.getMessage());
        }
    }

    public static Partida cargarPartida(String ruta, GrafoZonas grafo){
        PartidaData partidaData = new PartidaData();

        try(FileReader fr = new FileReader(ruta)) {
            partidaData = GSON.fromJson(fr, partidaData.getClass());
        }
        catch (IOException e) {
            return null;
        }

        if(partidaData == null){
            return null;
        }

        Zona zona = grafo.getZona(partidaData.idZonaActual);
        if(zona == null){
            return null;
        }

        Partida partida = new Partida(grafo);
        partida.setIdZonaActual(partidaData.idZonaActual);
        partida.setTurnoActual(partidaData.turnoActual);
        partida.setGatoEncontrado(partidaData.gatoEncontrado);
        partida.setEstadoActual(EstadoJuego.valueOf(partidaData.estadoActual));

        Estadisticas estadisticas = new Estadisticas(partidaData.vidaMax,partidaData.ataqueBase,partidaData.defensaBase,partidaData.rangoMov);
        estadisticas.setVidaActual(partidaData.vidaActual);

        Posicion posJugador = new Posicion(partidaData.posRow,partidaData.posCol);
        Jugador jugador = new Jugador(partidaData.nombreJugador,estadisticas,posJugador);
        partida.setJugador(jugador);
        jugador.setLog(partida.getLog());

        Celda celdaJugador = zona.getCelda(partidaData.posRow,partidaData.posCol);
        if(celdaJugador != null){
            celdaJugador.setEntidad(jugador);
        }
        for (int i = 0; i < partidaData.objetosInventario.length; i++){
            if(partidaData.objetosInventario[i] != null){
                jugador.getInventario().addObjeto(
                        new Objeto(partidaData.objetosInventario[i], TipoObjeto.USABLE)
                );
            }
        }
        for (int i = 0; i < partidaData.objetosEquipamiento.length; i++){
            if(partidaData.objetosEquipamiento[i] != null){
                Objeto equip = new Objeto(partidaData.objetosEquipamiento[i], TipoObjeto.EQUIPABLE);
                jugador.equipar(equip, SlotEquipable.values()[i]);
            }
        }

        Gato gato = new Gato(partidaData.nombreGato, new Estadisticas(10,0,0,4));
        partida.setGato(gato);

        if(partidaData.entradasLog != null){
            for(String mensaje : partidaData.entradasLog){
                if(mensaje != null){
                    partida.getLog().registrar(mensaje);
                }
            }
        }

        partida.getZonaActual().setVisitada(true);
        return partida;
    }
}
