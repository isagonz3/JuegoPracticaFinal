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
import jueguito.juegopracticafinal.TADs.InterfazIterador;
import jueguito.juegopracticafinal.TADs.Lista;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Gestiona la serialización y deserialización de partidas en formato JSON (guardar y cargar estado del juego)
public class LoadJSON {

    private static final Gson GSON = new Gson();
    private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

    //Estructura interna de datos utilizada para guardar y reconstruir una partida
    private static class PartidaData {
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
        int[] equipAtqBonus;
        int[] equipDefBonus;
        int[] equipVidaBonus;
        int[] equipRangoBonus;
        int[] equipUsos;
        String[] equipSlot;
    }

    //Guarda el estado actual de la partida en un archivo JSON
    public static void guardarPartida(Partida partida, String ruta) {

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

            if (o.getSlot() != null) {
                partidaData.objetosSlot[i] = o.getSlot().name();
            } else {
                partidaData.objetosSlot[i] = "";
            }
        }

        //Guardar objetos en equipamiento
        partidaData.objetosEquipamiento = new String[2];
        partidaData.equipAtqBonus = new int[2];
        partidaData.equipDefBonus = new int[2];
        partidaData.equipVidaBonus = new int[2];
        partidaData.equipRangoBonus = new int[2];
        partidaData.equipUsos = new int[2];
        partidaData.equipSlot = new String[2];

        for (int i = 0; i < 2; i++) {
            Objeto o = jugador.getEquipamiento()[i];
            if (o != null) {
                partidaData.objetosEquipamiento[i] = o.getNombre();
                partidaData.equipAtqBonus[i] = o.getAtaqueBonus();
                partidaData.equipDefBonus[i] = o.getDefensaBonus();
                partidaData.equipVidaBonus[i] = o.getVidaBonus();
                partidaData.equipRangoBonus[i] = o.getRangoBonus();
                partidaData.equipUsos[i] = o.getUsosRestantes();
                partidaData.equipSlot[i] = o.getSlot() != null ? o.getSlot().name() : "";
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

        InterfazIterador<EntradaLog> it = logs.iterador();
        for (int i = 0; it.hasNext(); i++) {
            partidaData.entradasLog[i] = it.next().getMensaje();
        }

        //Guardar zonas visitadas
        int numZonas = Configuracion.get().zona.numZonas;
        partidaData.zonasVisitadas = new boolean[numZonas];

        for (int i = 0; i < numZonas; i++) {

            Zona z = partida.getGrafoZonas().getZona(i);

            partidaData.zonasVisitadas[i] = (z != null && z.isVisitada());
        }

        //Guardar enemigos en zona actual
        Zona zActual = partida.getZonaActual();
        Lista<String> enemigos = new Lista<>();

        for (int r = 0; r < zActual.getRows(); r++) {
            for (int c = 0; c < zActual.getCols(); c++) {

                Celda celda = zActual.getCelda(r, c);

                if (celda != null && celda.getEntidad() instanceof Enemigo e && e.estarVivo()) {

                    enemigos.add(
                            e.getNombre() + "::" +
                                    e.getEstadisticas().getVidaActual() + "::" +
                                    e.getEstadisticas().getAtaqueBase() + "::" +
                                    e.getEstadisticas().getDefensaBase() + "::" +
                                    r + "::" + c
                    );
                }
            }
        }

        partidaData.enemigosJson = new String[enemigos.getSize()];
        InterfazIterador<String> itEnem = enemigos.iterador();
        for (int i = 0; itEnem.hasNext(); i++) {
            partidaData.enemigosJson[i] = itEnem.next();
        }

        try (FileWriter w = new FileWriter(ruta)) {
            GSON_PRETTY.toJson(partidaData, w);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar partida: " + e.getMessage(), e);
        }
    }

    //Carga una partida desde un archivo JSON reconstruyendo el estado completo del juego
    public static Partida cargarPartida(String ruta, GrafoZonas grafo) {

        PartidaData partidaData;

        try (FileReader fr = new FileReader(ruta)) {
            partidaData = GSON.fromJson(fr, PartidaData.class);
        } catch (IOException e) {
            return null;
        }

        if (partidaData == null) {
            return null;
        }

        Zona zona = grafo.getZona(partidaData.idZonaActual);

        if (zona == null) {
            return null;
        }

        Partida partida = new Partida(grafo);

        partida.setIdZonaActual(partidaData.idZonaActual);
        partida.setZonaActual(zona);
        partida.setTurnoActual(partidaData.turnoActual);
        partida.setGatoEncontrado(partidaData.gatoEncontrado);
        partida.setEstadoActual(EstadoJuego.valueOf(partidaData.estadoActual));

        Estadisticas estadisticas =
                new Estadisticas(
                        partidaData.vidaMax,
                        partidaData.ataqueBase,
                        partidaData.defensaBase,
                        partidaData.rangoMov
                );

        estadisticas.setVidaActual(partidaData.vidaActual);

        Posicion posJugador =
                new Posicion(partidaData.posRow, partidaData.posCol);

        Jugador jugador =
                new Jugador(partidaData.nombreJugador, estadisticas, posJugador);

        partida.setJugador(jugador);
        jugador.setLog(partida.getLog());

        Celda celdaJugador =
                zona.getCelda(partidaData.posRow, partidaData.posCol);

        if (celdaJugador != null) {
            celdaJugador.setEntidad(jugador);
        }

        //Cargar objetos inventario
        if (partidaData.objetosInventario != null) {

            for (int i = 0; i < partidaData.objetosInventario.length; i++) {

                if (partidaData.objetosInventario[i] == null) continue;

                String nombre = partidaData.objetosInventario[i];

                TipoObjeto tipo =
                        (partidaData.objetosTipo != null && i < partidaData.objetosTipo.length)
                                ? TipoObjeto.values()[partidaData.objetosTipo[i]]
                                : TipoObjeto.USABLE;

                int atq = (partidaData.objetosAtqBonus != null)
                        ? partidaData.objetosAtqBonus[i] : 0;

                int def = (partidaData.objetosDefBonus != null)
                        ? partidaData.objetosDefBonus[i] : 0;

                int vida = (partidaData.objetosVidaBonus != null)
                        ? partidaData.objetosVidaBonus[i] : 0;

                int rango = (partidaData.objetosRangoBonus != null)
                        ? partidaData.objetosRangoBonus[i] : 0;

                int usos = (partidaData.objetosUsos != null)
                        ? partidaData.objetosUsos[i] : 1;

                String slotName =
                        (partidaData.objetosSlot != null)
                                ? partidaData.objetosSlot[i] : null;

                Objeto o =
                        new Objeto(nombre, tipo, atq, def, vida, rango, usos, "");

                if (slotName != null && !slotName.isEmpty()) {
                    o.setSlot(SlotEquipable.valueOf(slotName));
                }

                jugador.getInventario().addObjeto(o);
            }
        }

        //Cargar objetos equipados
        if (partidaData.objetosEquipamiento != null) {

            for (int i = 0; i < partidaData.objetosEquipamiento.length; i++) {

                if (partidaData.objetosEquipamiento[i] == null) continue;

                String nombre = partidaData.objetosEquipamiento[i];
                int atq = partidaData.equipAtqBonus != null ? partidaData.equipAtqBonus[i] : 0;
                int def = partidaData.equipDefBonus != null ? partidaData.equipDefBonus[i] : 0;
                int vida = partidaData.equipVidaBonus != null ? partidaData.equipVidaBonus[i] : 0;
                int rango = partidaData.equipRangoBonus != null ? partidaData.equipRangoBonus[i] : 0;
                int usos = partidaData.equipUsos != null ? partidaData.equipUsos[i] : 99;

                Objeto equip = new Objeto(nombre, TipoObjeto.EQUIPABLE, atq, def, vida, rango, usos, "");
                String slotName = partidaData.equipSlot != null ? partidaData.equipSlot[i] : "";
                if (!slotName.isEmpty()) {
                    equip.setSlot(SlotEquipable.valueOf(slotName));
                }

                jugador.getEquipamiento()[i] = equip;
                jugador.getInventario().getObjetosEquipados().add(equip);
            }
            jugador.addBonusEquipamiento();
        }

        //Cargar gato
        Gato gato =
                new Gato(partidaData.nombreGato, new Estadisticas(10, 0, 0, 4));

        partida.setGato(gato);

        Zona zonaGato = grafo.getZona(8);

        if (zonaGato != null && zonaGato.esValida(16, 14)) {

            Celda celdaGato = zonaGato.getCelda(16, 14);

            if (celdaGato != null && !celdaGato.isOcupada()) {

                gato.setPosicion(new Posicion(16, 14));
                celdaGato.setEntidad(gato);
            }
        }

        //Cargar logs
        if (partidaData.entradasLog != null) {

            for (String mensaje : partidaData.entradasLog) {
                if (mensaje != null) {
                    partida.getLog().registrar(mensaje);
                }
            }
        }

        //Cargar zonas visitadas
        if (partidaData.zonasVisitadas != null) {

            for (int i = 0; i < partidaData.zonasVisitadas.length; i++) {

                Zona z = grafo.getZona(i);

                if (z != null) {
                    z.setVisitada(partidaData.zonasVisitadas[i]);
                }
            }
        }

        //Cargar enemigos
        if (partidaData.enemigosJson != null) {

            Zona za = partida.getZonaActual();

            for (String eData : partidaData.enemigosJson) {

                if (eData == null) continue;

                String[] parts = eData.split("::");

                if (parts.length == 6) {

                    String nom = parts[0];
                    int vida = Integer.parseInt(parts[1]);
                    int atq = Integer.parseInt(parts[2]);
                    int def = Integer.parseInt(parts[3]);
                    int row = Integer.parseInt(parts[4]);
                    int col = Integer.parseInt(parts[5]);

                    if (za.esValida(row, col)) {
                        Celda celdaEnemigo = za.getCelda(row, col);
                        if (celdaEnemigo != null) {
                            Enemigo e = new Enemigo(nom, new Estadisticas(vida, atq, def, 4));
                            e.getEstadisticas().setVidaActual(vida);
                            e.setPosicion(new Posicion(row, col));
                            celdaEnemigo.setEntidad(e);
                        }
                    }
                }
            }
            partida.colocarNPCsFijos();
        }
        return partida;
    }
}