package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.TADs.Lista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartidaMovimientoTest {
    private Partida partida;
    private PartidaMovimiento movimiento;

    @BeforeEach
    void setUp() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z = new Zona(0, "Test", 5, 5);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                z.setCelda(i, j, new Celda(TipoCelda.SUELO));
        grafo.addZona(z);
        grafo.getZona(0).setSpawnJugador(new Posicion(1, 1));
        partida = new Partida(grafo);
        partida.iniciar();
        movimiento = new PartidaMovimiento(partida);
    }


    @Test
    void moverJugador_seMueve() {
        assertTrue(movimiento.moverJugador(1, 2));
        assertEquals(1, partida.getJugador().getPosicion().getRow());
        assertEquals(2, partida.getJugador().getPosicion().getCol());
    }

    @Test
    void moverJugador_chocaPared() {
        partida.getZonaActual().getCelda(1,2).setTipoCelda(TipoCelda.PARED);
        assertFalse(movimiento.moverJugador(1,2));
    }

    @Test
    void moverJugador_sinPMnoMueve(){
        partida.getJugador().getEstadisticas().setPuntosMovDisponibles(0);
        assertFalse(movimiento.moverJugador(1,2));
    }

    @Test void moverJugador_pared_noPermite() {
        partida.getZonaActual().getCelda(1, 2).setTipoCelda(TipoCelda.PARED);
        assertFalse(movimiento.moverJugador(1, 2));
    }

    @Test void moverJugador_sinPM_noPermite() {
        partida.getJugador().getEstadisticas().setPuntosMovDisponibles(0);
        assertFalse(movimiento.moverJugador(1, 2));
    }

    @Test void moverJugador_celdaOcupada_noPermite() {
        Enemigo e = new Enemigo("E", new Estadisticas(10, 1, 1, 1));
        e.setPosicion(new Posicion(1, 2));
        partida.getZonaActual().getCelda(1, 2).setEntidad(e);
        assertFalse(movimiento.moverJugador(1, 2));
    }

    @Test void moverJugador_EstadoNoEnCurso_noPermite() {
        partida.setEstadoActual(EstadoJuego.VICTORIA);
        assertFalse(movimiento.moverJugador(1, 2));
    }

    @Test void getCeldasAccesibles_devuelveLista() {
        Lista<Celda> celdas = movimiento.getCeldasAccesibles();
        assertNotNull(celdas);
        assertTrue(celdas.getSize() > 0);
    }

    @Test void getCaminoMinimo_devuelveRuta() {
        Lista<Celda> camino = movimiento.getCaminoMinimo(1, 3);
        assertNotNull(camino);
        assertTrue(camino.getSize() > 0);
    }

    @Test void calcularDist_BFS() {
        int dist = movimiento.calcularDist(1, 1, 1, 3, partida.getZonaActual());
        assertEquals(2, dist);
    }

    @Test void calcularDist_inaccesible() {
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                if (r != 1 || c != 1)
                    partida.getZonaActual().getCelda(r, c).setTipoCelda(TipoCelda.PARED);
        int dist = movimiento.calcularDist(1, 1, 1, 3, partida.getZonaActual());
        assertEquals(Integer.MAX_VALUE, dist);
    }

    @Test void moverEnemigos_sinEnemigos_noFalla() {
        movimiento.moverEnemigos();
    }
}