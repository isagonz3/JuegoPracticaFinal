package jueguito.juegopracticafinal.Modelo.Core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

        partidaData.objetosInventario = new String[jugador.getInventario().size()];
        for (int i = 0; i < jugador.getInventario().size(); i++) {
            partidaData.objetosInventario[i] = jugador.getInventario().getObjeto(i).getNombre();
        }

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

        try (FileWriter w = new FileWriter(ruta)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(partidaData, w);
        } catch (IOException e) {
            System.out.println("Error al guardar partida: " + e.getMessage());
        }
    }

    public static Partida cargarPartida(String ruta, GrafoZonas grafo){
        PartidaData partidaData = new PartidaData();

        try(FileReader fr = new FileReader(ruta)) {
            partidaData = new Gson().fromJson(fr, partidaData.getClass());
        }
        catch (IOException e) {
            System.out.println("Error al cargar partida: " + e.getMessage());
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
                        new Objeto(partidaData.objetosInventario[i], TipoObjeto.CONSUMIBLE)
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
