package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    @Test
    void guardarYCargarMantieneEstadoBasicoYInventario() throws Exception {
        File temp=File.createTempFile("partida_test", ".json");

        GrafoZonas grafo=new GrafoZonas();
        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)},
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)}
        });
        grafo.addZona(z);

        Partida p=new Partida(grafo);


        Jugador j=new Jugador("Test", new Estadisticas(10, 5, 5, 4), new Posicion(0, 0));
        p.setJugador(j);
        z.getCelda(0,0).setEntidad(j);


        Objeto espada=new Objeto("Espada", TipoObjeto.EQUIPABLE, 2, 1, 0, 0, 3, null);
        espada.setSlot(SlotEquipable.ARMA);
        j.getInventario().addObjeto(espada);


        Gato g=new Gato("PutoGato", new Estadisticas(10, 0, 0, 4));
        p.setGato(g);

        p.setZonaActual(z);
        p.setIdZonaActual(0);
        p.setEstadoActual(EstadoJuego.EN_CURSO);


        LoadJSON.guardarPartida(p, temp.getAbsolutePath());
        Partida cargada=LoadJSON.cargarPartida(temp.getAbsolutePath(), grafo);

        assertNotNull(cargada);
        Jugador jc=cargada.getJugador();
        assertEquals("Test", jc.getNombre());
        assertEquals(EstadoJuego.EN_CURSO, cargada.getEstadoActual());
        assertEquals(0, cargada.getIdZonaActual());
        assertTrue(jc.getInventario().size() >= 1);

        Objeto o=jc.getInventario().getObjeto(0);
        assertEquals("Espada", o.getNombre());
        assertEquals(TipoObjeto.EQUIPABLE, o.getTipo());
        assertEquals(2, o.getAtaqueBonus());
        assertEquals(3, o.getUsosRestantes());
        if (o.getSlot()!=null) {
            assertEquals(SlotEquipable.ARMA, o.getSlot());
        }
    }

    @Test
    void iniciarDebeInicializarCorrectamenteLaPartida() {
        GrafoZonas grafo=new GrafoZonas();

        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO),new Celda(TipoCelda.SUELO)},
                {new Celda(TipoCelda.SUELO),new Celda(TipoCelda.SUELO)}
        });

        grafo.addZona(z);

        Partida p=new Partida(grafo);

        p.iniciar();

        assertNotNull(p.getJugador());
        assertNotNull(p.getGato());
        assertNotNull(p.getZonaActual());

        assertEquals(0, p.getIdZonaActual());
        assertEquals(EstadoJuego.EN_CURSO,p.getEstadoActual());
        assertEquals(0, p.getTurnoActual());

        boolean jugadorEnMapa =
                z.getCelda(0, 0).getEntidad()==p.getJugador();
        assertTrue(jugadorEnMapa);
    }

    @Test
    void iniciarTurnoDebeResetearFaseJugadorYPuntosMovimiento() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)}
        });
        grafo.addZona(z);

        Partida p=new Partida(grafo);
        p.iniciar();

        p.getJugador().getEstadisticas().setPuntosMovDisponibles(0);

        p.iniciarTurno();

        assertTrue(p.isFaseJugadorActiva());
        assertEquals(p.getJugador().getRangoTotal(),p.getJugador().getEstadisticas().getPuntosMovDisponibles());
    }

    @Test
    void checkDerrotaDebeCambiarEstadoSiJugadorMuere() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO)}
        });
        grafo.addZona(z);

        Partida p = new Partida(grafo);
        p.iniciar();

        // Simulamos muerte del jugador
        p.getJugador().getEstadisticas().setVidaActual(0);

        p.terminarTurnoPublic();

        assertEquals(EstadoJuego.DERROTA, p.getEstadoActual());
    }

    @Test
    void checkDerrotaDebeCambiarEstadoSiSuperaMaxTurnos() {
        GrafoZonas grafo=new GrafoZonas();
        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO)}
        });
        grafo.addZona(z);

        Partida p=new Partida(grafo);
        p.iniciar();

        int maxTurnosSimulado=100;
        p.setTurnoActual(maxTurnosSimulado);

        p.terminarTurnoPublic();

        assertEquals(EstadoJuego.DERROTA, p.getEstadoActual());
    }
}