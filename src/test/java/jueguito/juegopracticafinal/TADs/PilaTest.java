package jueguito.juegopracticafinal.TADs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PilaTest {

    @Test
    void push() {
        Pila<Integer> pila = new Pila<>();
        pila.push(1);
        pila.push(2);
        pila.push(3);
        assertEquals(3, pila.peek());
        assertEquals(3, pila.getSize());
    }

    @Test
    void pop() {
        Pila<Integer> pila = new Pila<>();
        pila.push(1); pila.push(2);
        assertEquals(2, pila.pop());
        assertEquals(1, pila.pop());
        assertNull(pila.pop());
    }

    @Test
    void peek() {
        Pila<String> pila = new Pila<>();
        assertNull(pila.peek());
        pila.push("X");
        assertEquals("X", pila.peek());
        pila.push("Y");
        assertEquals("Y", pila.peek());
    }

    @Test
    void getSize() {
        Pila<Integer> pila = new Pila<>();
        assertEquals(0, pila.getSize());
        pila.push(1);
        assertEquals(1, pila.getSize());
        pila.pop();
        assertEquals(0, pila.getSize());
    }

    @Test
    void clear() {
        Pila<String> pila = new Pila<>();
        pila.push("A");
        pila.push("B");
        pila.push("C");
        pila.clear();
        assertTrue(pila.isEmpty());
        assertEquals(0, pila.getSize());
        assertNull(pila.pop());
    }

    @Test
    void isEmpty() {
        Pila<Integer> pila = new Pila<>();
        assertTrue(pila.isEmpty());
        pila.push(1);
        assertFalse(pila.isEmpty());
        pila.pop();
        assertTrue(pila.isEmpty());
    }

    @Test void ordenLIFO() {
        Pila<Integer> pila = new Pila<>();
        pila.push(1);
        pila.push(2);
        pila.push(3);
        assertEquals(3, pila.pop());
        assertEquals(2, pila.pop());
        assertEquals(1, pila.pop());
    }
}