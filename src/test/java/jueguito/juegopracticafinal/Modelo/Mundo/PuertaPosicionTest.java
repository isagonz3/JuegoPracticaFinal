package jueguito.juegopracticafinal.Modelo.Mundo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PuertaTest {

    @Test void constructorCompleto() {
        Puerta p = new Puerta(0, 1, 2, 3, 4, 5);
        assertEquals(0, p.getZonaOrigen());
        assertEquals(1, p.getZonaDestino());
        assertEquals(2, p.getXOrigen());
        assertEquals(3, p.getYOrigen());
        assertEquals(4, p.getXDestino());
        assertEquals(5, p.getYDestino());
    }

    @Test void constructorVacio() {
        Puerta p = new Puerta();
        assertEquals(0, p.getZonaOrigen());
        assertEquals(0, p.getZonaDestino());
    }

    @Test void compareToIgual() {
        Puerta p1 = new Puerta(0, 1, 0, 0, 1, 1);
        Puerta p2 = new Puerta(0, 1, 0, 0, 1, 1);
        assertEquals(0, p1.compareTo(p2));
    }

    @Test void compareToMenor() {
        Puerta p1 = new Puerta(0, 1, 0, 0, 1, 1);
        Puerta p2 = new Puerta(1, 2, 0, 0, 1, 1);
        assertTrue(p1.compareTo(p2) < 0);
    }

    @Test void compareToMayor() {
        Puerta p1 = new Puerta(1, 2, 0, 0, 1, 1);
        Puerta p2 = new Puerta(0, 1, 0, 0, 1, 1);
        assertTrue(p1.compareTo(p2) > 0);
    }

    @Test void compareToMismaZonaOrigenDiferenteDestino() {
        Puerta p1 = new Puerta(0, 1, 0, 0, 1, 1);
        Puerta p2 = new Puerta(0, 2, 0, 0, 1, 1);
        assertTrue(p1.compareTo(p2) < 0);
    }

    @Test void compareToMismaZonaDiferenteXOrigen() {
        Puerta p1 = new Puerta(0, 1, 1, 0, 1, 1);
        Puerta p2 = new Puerta(0, 1, 2, 0, 1, 1);
        assertTrue(p1.compareTo(p2) < 0);
    }

    @Test void compareToMismaZonaDiferenteYOrigen() {
        Puerta p1 = new Puerta(0, 1, 1, 0, 1, 1);
        Puerta p2 = new Puerta(0, 1, 1, 1, 1, 1);
        assertTrue(p1.compareTo(p2) < 0);
    }
}

class PosicionTest {

    @Test void constructor() {
        Posicion p = new Posicion(3, 5);
        assertEquals(3, p.getRow());
        assertEquals(5, p.getCol());
    }

    @Test void idZona() {
        Posicion p = new Posicion(0, 0);
        p.setIdZona(2);
        assertEquals(2, p.getIdZona());
    }

    @Test void setRowCol() {
        Posicion p = new Posicion(0, 0);
        p.setRow(10); assertEquals(10, p.getRow());
        p.setCol(20); assertEquals(20, p.getCol());
    }

    @Test void equalsMismo() {
        Posicion p1 = new Posicion(2, 3); p1.setIdZona(1);
        Posicion p2 = new Posicion(2, 3); p2.setIdZona(1);
        assertEquals(p1, p2);
    }

    @Test void equalsDistintoIdZona() {
        Posicion p1 = new Posicion(2, 3); p1.setIdZona(1);
        Posicion p2 = new Posicion(2, 3); p2.setIdZona(2);
        assertNotEquals(p1, p2);
    }

    @Test void equalsNull() {
        Posicion p = new Posicion(0, 0);
        assertNotEquals(p, null);
    }

    @Test void equalsOtraClase() {
        Posicion p = new Posicion(0, 0);
        assertNotEquals(p, "otro");
    }

    @Test void equalsReflexivo() {
        Posicion p = new Posicion(1, 2);
        assertEquals(p, p);
    }

    @Test void toStringContieneValores() {
        Posicion p = new Posicion(3, 5);
        p.setIdZona(2);
        String s = p.toString();
        assertTrue(s.contains("2"));
        assertTrue(s.contains("3"));
        assertTrue(s.contains("5"));
    }
}
