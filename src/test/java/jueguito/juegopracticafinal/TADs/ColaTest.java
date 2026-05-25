package jueguito.juegopracticafinal.TADs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColaTest {

    @Test
    void enqueue() {
        Cola<Integer> cola = new Cola<>();
        cola.enqueue(10);
        cola.enqueue(20);
        cola.enqueue(30);
        assertEquals(10, cola.peek());
        assertEquals(3, cola.getSize());
    }

    @Test
    void dequeue() {
        Cola<Integer> cola = new Cola<>();
        cola.enqueue(10);
        cola.enqueue(20);
        cola.enqueue(30);
        assertEquals(10, cola.dequeue());
        assertEquals(20, cola.dequeue());
        assertEquals(30, cola.dequeue());
        assertNull(cola.dequeue());
    }

    @Test
    void peek() {
        Cola<Integer> cola = new Cola<>();
        assertNull(cola.peek());
        cola.enqueue(10);
        assertEquals(10, cola.peek());
        cola.enqueue(20);
        assertEquals(10, cola.peek());
    }

    @Test
    void isEmpty() {
        Cola<Integer> cola = new Cola<>();
        assertTrue(cola.isEmpty());
        cola.enqueue(1);
        assertFalse(cola.isEmpty());
        cola.dequeue();
        assertTrue(cola.isEmpty());
    }

    @Test
    void getFirst() {
        Cola<Integer> cola = new Cola<>();
        assertNull(cola.getFirst());
        cola.enqueue(10);
        assertNotNull(cola.getFirst());
        assertEquals(Integer.valueOf(10), cola.getFirst().getData());
        cola.enqueue(20);
        assertEquals(Integer.valueOf(10), cola.getFirst().getData());
    }

    @Test
    void getSize() {
        Cola<Integer> cola = new Cola<>();
        assertEquals(0, cola.getSize());
        cola.enqueue(1);
        assertEquals(1, cola.getSize());
        cola.enqueue(2);
        assertEquals(2, cola.getSize());
        cola.dequeue();
        assertEquals(1, cola.getSize());
        cola.clear();
        assertEquals(0, cola.getSize());
    }

    @Test
    void clear() {
        Cola<Integer> cola = new Cola<>();
        cola.enqueue(1);
        cola.enqueue(2);
        cola.enqueue(3);
        cola.clear();
        assertTrue(cola.isEmpty());
        assertEquals(0, cola.getSize());
        assertNull(cola.dequeue());
    }

    @Test
    void ordenFIFO() {
        Cola<String> cola = new Cola<>();
        cola.enqueue("A");
        cola.enqueue("B");
        cola.enqueue("C");
        assertEquals("A", cola.dequeue());
        assertEquals("B", cola.dequeue());
        assertEquals("C", cola.dequeue());
    }

    @Test
    void dequeueDeColaVaciaDevuelveNull() {
        Cola<Integer> cola = new Cola<>();
        assertNull(cola.dequeue());
        assertNull(cola.dequeue());
    }
}