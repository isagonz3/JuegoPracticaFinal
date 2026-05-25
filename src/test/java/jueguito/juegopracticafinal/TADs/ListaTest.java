package jueguito.juegopracticafinal.TADs;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ListaTest {

    @Test void add() {
        Lista<String> l = new Lista<>();
        l.add("A"); l.add("B");
        assertEquals("A", l.get(0));
        assertEquals("B", l.get(1));
        assertEquals(2, l.getSize());
    }

    @Test void addFirst() {
        Lista<String> l = new Lista<>();
        l.add("B"); l.addFirst("A");
        assertEquals("A", l.get(0));
        assertEquals("B", l.get(1));
    }

    @Test void addAll() {
        Lista<String> l1 = new Lista<>(); l1.add("A"); l1.add("B");
        Lista<String> l2 = new Lista<>(); l2.add("C");
        l1.addAll(l2);
        assertEquals(3, l1.getSize());
        assertEquals("C", l1.get(2));
    }

    @Test void addAllNullNoFalla() {
        Lista<String> l = new Lista<>(); l.add("A");
        l.addAll(null);
        assertEquals(1, l.getSize());
    }

    @Test void addAllEmptyNoFalla() {
        Lista<String> l = new Lista<>(); l.add("A");
        l.addAll(new Lista<>());
        assertEquals(1, l.getSize());
    }

    @Test void get() {
        Lista<Integer> l = new Lista<>(); l.add(10); l.add(20);
        assertEquals(10, l.get(0));
        assertEquals(20, l.get(1));
        assertNull(l.get(-1));
        assertNull(l.get(5));
    }

    @Test void set() {
        Lista<String> l = new Lista<>(); l.add("A"); l.add("B");
        l.set(1, "X");
        assertEquals("X", l.get(1));
        l.set(-1, "Y"); // no-op
        assertEquals("A", l.get(0));
    }

    @Test void delete() {
        Lista<String> l = new Lista<>(); l.add("A"); l.add("B"); l.add("C");
        assertEquals("B", l.delete("B"));
        assertEquals(2, l.getSize());
        assertEquals("A", l.get(0));
        assertEquals("C", l.get(1));
        assertNull(l.delete("Z"));
    }

    @Test void deleteUnico() {
        Lista<String> l = new Lista<>(); l.add("A");
        assertEquals("A", l.delete("A"));
        assertTrue(l.isEmpty());
    }

    @Test void isEmpty() {
        Lista<String> l = new Lista<>();
        assertTrue(l.isEmpty());
        l.add("X");
        assertFalse(l.isEmpty());
    }

    @Test void getSize() {
        Lista<String> l = new Lista<>();
        assertEquals(0, l.getSize());
        l.add("A"); assertEquals(1, l.getSize());
        l.delete("A"); assertEquals(0, l.getSize());
    }

    @Test void contains() {
        Lista<Integer> l = new Lista<>(); l.add(1); l.add(2);
        assertTrue(l.contains(1));
        assertFalse(l.contains(3));
    }

    @Test void getFirst() {
        Lista<String> l = new Lista<>();
        assertNull(l.getFirst());
        l.add("A"); l.add("B");
        assertEquals("A", l.getFirst());
    }

    @Test void deleteLast() {
        Lista<String> l = new Lista<>(); l.add("A"); l.add("B");
        assertEquals("B", l.deleteLast());
        assertEquals(1, l.getSize());
        assertEquals("A", l.getFirst());
        assertEquals("A", l.deleteLast());
        assertNull(l.deleteLast()); // now empty
    }

    @Test void deleteLastUnico() {
        Lista<String> l = new Lista<>(); l.add("A");
        assertEquals("A", l.deleteLast());
        assertTrue(l.isEmpty());
    }

    @Test void toStringLista() {
        Lista<Integer> l = new Lista<>();
        assertEquals("[]", l.toString());
        l.add(1); l.add(2);
        assertEquals("[1, 2]", l.toString());
    }
}