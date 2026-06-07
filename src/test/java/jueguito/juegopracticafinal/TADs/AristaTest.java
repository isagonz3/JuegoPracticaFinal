package jueguito.juegopracticafinal.TADs;

import static org.junit.jupiter.api.Assertions.*;

import jueguito.juegopracticafinal.Modelo.Excepciones.ExcepcionTADs;
import org.junit.jupiter.api.Test;

class AristaTest {

    @Test
    void constructorAsignaOrigenYDestino() {
        Arista<String> a = new Arista<>("A", "B");
        assertEquals("A", a.getOrigen());
        assertEquals("B", a.getDestino());
    }

    @Test
    void constructorOrigenNullLanzaExcepcion() {
        assertThrows(ExcepcionTADs.class, () -> new Arista<>(null, "B"));
    }

    @Test
    void constructorDestinoNullLanzaExcepcion() {
        assertThrows(ExcepcionTADs.class, () -> new Arista<>("A", null));
    }

    @Test
    void equalsMismaArista() {
        Arista<String> a = new Arista<>("A", "B");
        assertEquals(a, a);
    }

    @Test
    void equalsMismosValores() {
        Arista<String> a1 = new Arista<>("A", "B");
        Arista<String> a2 = new Arista<>("A", "B");
        assertEquals(a1, a2);
    }

    @Test
    void equalsDistintoOrigen() {
        Arista<String> a1 = new Arista<>("A", "B");
        Arista<String> a2 = new Arista<>("X", "B");
        assertNotEquals(a1, a2);
    }

    @Test
    void equalsDistintoDestino() {
        Arista<String> a1 = new Arista<>("A", "B");
        Arista<String> a2 = new Arista<>("A", "Y");
        assertNotEquals(a1, a2);
    }

    @Test
    void equalsNull() {
        Arista<String> a = new Arista<>("A", "B");
        assertNotEquals(null, a);
    }

    @Test
    void equalsDistintaClase() {
        Arista<String> a = new Arista<>("A", "B");
        assertNotEquals("A -> B", a);
    }

    @Test
    void toStringFormato() {
        Arista<String> a = new Arista<>("A", "B");
        assertEquals("A -> B", a.toString());
    }
}
