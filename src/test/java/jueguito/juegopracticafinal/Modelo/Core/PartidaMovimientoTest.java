package jueguito.juegopracticafinal.Modelo.Core;

import static org.junit.jupiter.api.Assertions.*;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorAccesoZonaBloqueada;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorCasillaOcupada;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorMovimientoInvalido;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorMovimientoSinBarca;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.TADs.Lista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartidaMovimientoTest {

    private Partida partida;
    private PartidaMovimiento pm;
    private Zona zona;

    @BeforeEach
    void setUp() {
        GrafoZonas grafo = new GrafoZonas();
        Celda[][] celdas = new Celda[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        zona = new Zona(0, "Z0", celdas);
        grafo.addZona(zona);
        partida = new Partida(grafo);
        partida.iniciar();
        partida.setEstadoActual(jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO);
        partida.iniciarTurno();
        pm = new PartidaMovimiento(partida);
    }

    @Test
    void calcularDistMismaCeldaDevuelveCero() {
        assertEquals(0, pm.calcularDist(1, 1, 1, 1, zona));
    }

    @Test
    void calcularDistAdyacenteDevuelveUno() {
        assertEquals(1, pm.calcularDist(1, 1, 1, 2, zona));
    }

    @Test
    void calcularDistCaminoDespejado() {
        assertEquals(3, pm.calcularDist(0, 0, 0, 3, zona));
    }

    @Test
    void calcularDistCaminoBloqueadoDevuelveMaxValue() {
        Celda[][] celdas = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        celdas[0][1].setTipoCelda(TipoCelda.PARED);
        celdas[1][0].setTipoCelda(TipoCelda.PARED);
        Zona z = new Zona(1, "Bloqueada", celdas);
        assertEquals(Integer.MAX_VALUE, pm.calcularDist(0, 0, 2, 2, z));
    }

    @Test
    void moverJugadorDevuelveTrue() throws ErrorMovimientoInvalido, ErrorCasillaOcupada, ErrorMovimientoSinBarca, ErrorAccesoZonaBloqueada {
        assertTrue(pm.moverJugador(1, 1));
    }

    @Test
    void moverJugadorFueraMapaLanzaExcepcion() {
        assertThrows(ErrorMovimientoInvalido.class, () -> pm.moverJugador(10, 10));
    }

    @Test
    void moverJugadorCeldaNoTransitableLanzaExcepcion() {
        zona.getCelda(1, 1).setTipoCelda(TipoCelda.PARED);
        assertThrows(ErrorMovimientoInvalido.class, () -> pm.moverJugador(1, 1));
    }

    @Test
    void moverJugadorCeldaOcupadaLanzaExcepcion() {
        zona.getCelda(1, 1).setEntidad(new Enemigo("Orco", new Estadisticas(5, 3, 1, 1)));
        assertThrows(ErrorCasillaOcupada.class, () -> pm.moverJugador(1, 1));
    }

    @Test
    void moverJugadorActualizaPosicion() throws ErrorMovimientoInvalido, ErrorCasillaOcupada, ErrorMovimientoSinBarca, ErrorAccesoZonaBloqueada {
        pm.moverJugador(2, 2);
        Posicion esperada = new Posicion(2, 2);
        assertEquals(esperada, partida.getJugador().getPosicion());
    }

    @Test
    void moverJugadorLiberaCeldaOrigen() throws ErrorMovimientoInvalido, ErrorCasillaOcupada, ErrorMovimientoSinBarca, ErrorAccesoZonaBloqueada {
        Posicion origen = partida.getJugador().getPosicion();
        pm.moverJugador(2, 2);
        Celda celdaOrigen = zona.getCelda(origen.getRow(), origen.getCol());
        assertFalse(celdaOrigen.isOcupada());
    }

    @Test
    void calcularCaminoRutaDirecta() {
        Lista<Celda> camino = pm.calcularCamino(0, 0, 0, 2, zona);
        assertNotNull(camino);
        assertTrue(camino.getSize() >= 2);
    }

    @Test
    void calcularCaminoMismaCelda() {
        Lista<Celda> camino = pm.calcularCamino(1, 1, 1, 1, zona);
        assertNotNull(camino);
        assertEquals(1, camino.getSize());
    }

    @Test
    void calcularCaminoSinRutaDevuelveNull() {
        Celda[][] celdas = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas[i][j] = new Celda(TipoCelda.PARED);
        celdas[0][0].setTipoCelda(TipoCelda.SUELO);
        celdas[2][2].setTipoCelda(TipoCelda.SUELO);
        Zona z = new Zona(2, "Aislada", celdas);
        assertNull(pm.calcularCamino(0, 0, 2, 2, z));
    }

    @Test
    void getCeldasAccesiblesNoEsNull() {
        assertNotNull(pm.getCeldasAccesibles());
    }

    @Test
    void moverEnemigosSinEnemigosNoCambiaNada() {
        int turnoAntes = partida.getTurnoActual();
        pm.moverEnemigos();
        assertEquals(turnoAntes, partida.getTurnoActual());
    }

    private Enemigo colocarEnemigo(int row, int col, int vida) {
        Enemigo e = new Enemigo("Orco", new Estadisticas(vida, 5, 1, 1));
        e.setPosicion(new Posicion(row, col));
        zona.getCelda(row, col).setEntidad(e);
        return e;
    }

    @Test
    void moverEnemigoNoAtacadoSeAcercaAlJugador() {
        Enemigo e = colocarEnemigo(2, 2, 10);
        Posicion pj = partida.getJugador().getPosicion();
        int distAntes = Math.abs(2 - pj.getRow()) + Math.abs(2 - pj.getCol());

        pm.moverEnemigos();

        Posicion nuevaPos = e.getPosicion();
        int distDespues = Math.abs(nuevaPos.getRow() - pj.getRow()) + Math.abs(nuevaPos.getCol() - pj.getCol());
        assertTrue(distDespues < distAntes, "Enemy should move closer to player");
    }

    @Test
    void moverEnemigoNoAtacadoAdyacenteNoSeMueve() {
        Enemigo e = colocarEnemigo(0, 1, 10);
        Posicion posAntes = e.getPosicion();

        pm.moverEnemigos();

        assertEquals(posAntes, e.getPosicion());
    }

    @Test
    void moverEnemigoAtacadoAdyacentePuedeAtacar() {
        int vidaAntes = partida.getJugador().getEstadisticas().getVidaActual();
        Enemigo e = colocarEnemigo(0, 1, 10);
        e.setAtacado(true);

        pm.moverEnemigos();

        assertTrue(partida.getJugador().getEstadisticas().getVidaActual() <= vidaAntes);
    }

    @Test
    void moverEnemigoBloqueadoPorParedNoSeMueve() {
        zona.getCelda(0, 1).setTipoCelda(TipoCelda.PARED);
        zona.getCelda(2, 1).setTipoCelda(TipoCelda.PARED);
        zona.getCelda(1, 0).setTipoCelda(TipoCelda.PARED);
        zona.getCelda(1, 2).setTipoCelda(TipoCelda.PARED);
        Enemigo e = colocarEnemigo(1, 1, 10);
        Posicion posAntes = e.getPosicion();

        pm.moverEnemigos();

        assertEquals(posAntes, e.getPosicion());
    }

    @Test
    void moverEnemigoMuertoNoSeProcesa() {
        Enemigo e = colocarEnemigo(2, 2, 0);
        Posicion posAntes = e.getPosicion();

        pm.moverEnemigos();

        assertEquals(posAntes, e.getPosicion());
    }
}
