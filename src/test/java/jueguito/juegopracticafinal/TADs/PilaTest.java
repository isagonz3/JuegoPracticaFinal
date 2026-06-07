package jueguito.juegopracticafinal.TADs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PilaTest {

    @Test
    void pushAnyadeElemento() {
        Pila<String> pila = new Pila<>();
        pila.push("A");
        assertEquals(1, pila.getSize());
        assertFalse(pila.isEmpty());
    }

    @Test
    void popDevuelveEnOrdenLIFO() {
        Pila<String> pila = new Pila<>();
        pila.push("A");
        pila.push("B");
        pila.push("C");
        assertEquals("C", pila.pop());
        assertEquals("B", pila.pop());
        assertEquals("A", pila.pop());
    }

    @Test
    void popReduceSize() {
        Pila<String> pila = new Pila<>();
        pila.push("A");
        pila.push("B");
        pila.pop();
        assertEquals(1, pila.getSize());
    }

    @Test
    void popPilaVaciaDevuelveNull() {
        Pila<String> pila = new Pila<>();
        assertNull(pila.pop());
    }

    @Test
    void popHastaVaciaYLuegoNull() {
        Pila<String> pila = new Pila<>();
        pila.push("X");
        pila.pop();
        assertNull(pila.pop());
        assertTrue(pila.isEmpty());
    }

    @Test
    void peekDevuelveTopeSinEliminar() {
        Pila<String> pila = new Pila<>();
        pila.push("A");
        pila.push("B");
        assertEquals("B", pila.peek());
        assertEquals(2, pila.getSize());
        assertEquals("B", pila.pop());
    }

    @Test
    void peekPilaVaciaDevuelveNull() {
        Pila<String> pila = new Pila<>();
        assertNull(pila.peek());
    }

    @Test
    void isEmptyPilaVaciaDevuelveTrue() {
        Pila<String> pila = new Pila<>();
        assertTrue(pila.isEmpty());
    }

    @Test
    void isEmptyPilaConElementosDevuelveFalse() {
        Pila<String> pila = new Pila<>();
        pila.push("A");
        assertFalse(pila.isEmpty());
    }

    @Test
    void getSizePilaVacia() {
        Pila<String> pila = new Pila<>();
        assertEquals(0, pila.getSize());
    }

    @Test
    void getSizeTrasMultiplePushYPop() {
        Pila<String> pila = new Pila<>();
        pila.push("A");
        pila.push("B");
        pila.pop();
        pila.push("C");
        assertEquals(2, pila.getSize());
    }

    @Test
    void clearVaciaLaPila() {
        Pila<String> pila = new Pila<>();
        pila.push("A");
        pila.push("B");
        pila.clear();
        assertTrue(pila.isEmpty());
        assertEquals(0, pila.getSize());
        assertNull(pila.pop());
    }

    @Test
    void clearPilaYaVaciaNoRompe() {
        Pila<String> pila = new Pila<>();
        pila.clear();
        assertTrue(pila.isEmpty());
    }

    @Test
    void pushDespuesDeClearFunciona() {
        Pila<String> pila = new Pila<>();
        pila.push("A");
        pila.clear();
        pila.push("B");
        assertEquals("B", pila.pop());
    }

    @Test
    void pushNullEsValido() {
        Pila<String> pila = new Pila<>();
        pila.push(null);
        assertEquals(1, pila.getSize());
        assertNull(pila.pop());
    }
}
