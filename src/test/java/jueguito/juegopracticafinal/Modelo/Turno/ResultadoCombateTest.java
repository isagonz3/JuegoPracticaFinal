package jueguito.juegopracticafinal.Modelo.Turno;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ResultadoCombateTest {

    @Test void constructor() {
        ResultadoCombate r = new ResultadoCombate(15, 30, false, "Golpe crítico");
        assertEquals(15, r.getHit());
        assertEquals(30, r.getVidaRestante());
        assertFalse(r.isEnemigoKO());
        assertEquals("Golpe crítico", r.getMensaje());
    }

    @Test void enemigoKO() {
        ResultadoCombate r = new ResultadoCombate(100, 0, true, "Has derrotado al enemigo");
        assertTrue(r.isEnemigoKO());
        assertEquals(0, r.getVidaRestante());
    }

    @Test void hitCero() {
        ResultadoCombate r = new ResultadoCombate(0, 50, false, "Fallaste");
        assertEquals(0, r.getHit());
        assertFalse(r.isEnemigoKO());
    }
}
