package jueguito.juegopracticafinal.Modelo.Entidades;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstadisticasTest {

    private Estadisticas e;

    @BeforeEach
    void setUp() {
        e = new Estadisticas(100, 15, 10, 5);
    }

    @Test void constructor() {
        assertEquals(100, e.getVidaMax());
        assertEquals(100, e.getVidaActual());
        assertEquals(15, e.getAtaqueBase());
        assertEquals(10, e.getDefensaBase());
        assertEquals(5, e.getRangoMov());
        assertEquals(5, e.getPuntosMovDisponibles());
    }

    @Test void gettersSetters() {
        e.setVidaMax(200); assertEquals(200, e.getVidaMax());
        e.setVidaActual(50); assertEquals(50, e.getVidaActual());
        e.setAtaqueBase(20); assertEquals(20, e.getAtaqueBase());
        e.setDefensaBase(12); assertEquals(12, e.getDefensaBase());
        e.setRangoMov(8); assertEquals(8, e.getRangoMov());
        e.setPuntosMovDisponibles(3); assertEquals(3, e.getPuntosMovDisponibles());
    }

    @Test void recibirAtaque() {
        e.recibirAtaque(30);
        assertEquals(80, e.getVidaActual());
        e.recibirAtaque(200);
        assertEquals(0, e.getVidaActual());
    }

    @Test void estaVivo() {
        assertTrue(e.estaVivo());
        e.recibirAtaque(110);
        assertFalse(e.estaVivo());
    }

    @Test void resetMovimiento() {
        e.usarMovimiento(3);
        assertEquals(2, e.getPuntosMovDisponibles());
        e.resetMovimiento();
        assertEquals(5, e.getPuntosMovDisponibles());
    }

    @Test void usarMovimiento() {
        assertTrue(e.usarMovimiento(3));
        assertEquals(2, e.getPuntosMovDisponibles());
        assertFalse(e.usarMovimiento(0));
        assertFalse(e.usarMovimiento(-1));
        assertFalse(e.usarMovimiento(10));
    }

    @Test void puedeMoverse() {
        assertTrue(e.puedeMoverse());
        e.usarMovimiento(5);
        assertFalse(e.puedeMoverse());
    }

    @Test void clonar() {
        e.usarMovimiento(2);
        e.recibirAtaque(10);
        Estadisticas clon = e.clonar();
        assertEquals(e.getVidaActual(), clon.getVidaActual());
        assertEquals(e.getVidaMax(), clon.getVidaMax());
        assertEquals(e.getPuntosMovDisponibles(), clon.getPuntosMovDisponibles());
        assertEquals(e.getAtaqueBase(), clon.getAtaqueBase());
        clon.recibirAtaque(20);
        assertNotEquals(e.getVidaActual(), clon.getVidaActual());
    }

    @Test void curarVida() {
        e.recibirAtaque(40);
        e.curarVida(15);
        assertEquals(85, e.getVidaActual());
        e.curarVida(100);
        assertEquals(100, e.getVidaActual());
    }

    @Test void aumentarAtaque() {
        e.aumentarAtaque(5);
        assertEquals(20, e.getAtaqueBase());
    }

    @Test void aumentarDefensa() {
        e.aumentarDefensa(3);
        assertEquals(13, e.getDefensaBase());
    }

    @Test void aumentarRangoMov() {
        e.aumentarRangoMov(2);
        assertEquals(7, e.getRangoMov());
    }

    @Test void toStringContieneValores() {
        String s = e.toString();
        assertTrue(s.contains("100"));
        assertTrue(s.contains("15"));
        assertTrue(s.contains("10"));
    }
}
