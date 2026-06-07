package jueguito.juegopracticafinal.TADs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ColaTest {

    @Test
    void enqueueAnyadeElemento() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("A");
        assertEquals(1, cola.getSize());
        assertFalse(cola.isEmpty());
    }

    @Test
    void dequeueDevuelveEnOrdenFIFO() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("A");
        cola.enqueue("B");
        cola.enqueue("C");
        assertEquals("A", cola.dequeue());
        assertEquals("B", cola.dequeue());
        assertEquals("C", cola.dequeue());
    }

    @Test
    void dequeueReduceSize() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("A");
        cola.enqueue("B");
        cola.dequeue();
        assertEquals(1, cola.getSize());
    }

    @Test
    void dequeueColaVaciaDevuelveNull() {
        Cola<String> cola = new Cola<>();
        assertNull(cola.dequeue());
    }

    @Test
    void dequeueHastaVaciaYLuegoNull() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("X");
        cola.dequeue();
        assertNull(cola.dequeue());
        assertTrue(cola.isEmpty());
    }

    @Test
    void peekDevuelvePrimeroSinEliminar() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("A");
        cola.enqueue("B");
        assertEquals("A", cola.peek());
        assertEquals(2, cola.getSize());
        assertEquals("A", cola.dequeue());
    }

    @Test
    void peekColaVaciaDevuelveNull() {
        Cola<String> cola = new Cola<>();
        assertNull(cola.peek());
    }

    @Test
    void isEmptyColaVaciaDevuelveTrue() {
        Cola<String> cola = new Cola<>();
        assertTrue(cola.isEmpty());
    }

    @Test
    void isEmptyColaConElementosDevuelveFalse() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("A");
        assertFalse(cola.isEmpty());
    }

    @Test
    void getSizeColaVacia() {
        Cola<String> cola = new Cola<>();
        assertEquals(0, cola.getSize());
    }

    @Test
    void getSizeTrasMultipleEnqueueYDequeue() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("A");
        cola.enqueue("B");
        cola.dequeue();
        cola.enqueue("C");
        assertEquals(2, cola.getSize());
    }

    @Test
    void clearVaciaLaCola() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("A");
        cola.enqueue("B");
        cola.clear();
        assertTrue(cola.isEmpty());
        assertEquals(0, cola.getSize());
        assertNull(cola.dequeue());
    }

    @Test
    void clearColaYaVaciaNoRompe() {
        Cola<String> cola = new Cola<>();
        cola.clear();
        assertTrue(cola.isEmpty());
    }

    @Test
    void enqueueDespuesDeClearFunciona() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("A");
        cola.clear();
        cola.enqueue("B");
        assertEquals("B", cola.dequeue());
    }

    @Test
    void enqueueNullEsValido() {
        Cola<String> cola = new Cola<>();
        cola.enqueue(null);
        assertEquals(1, cola.getSize());
        assertNull(cola.dequeue());
    }
}
