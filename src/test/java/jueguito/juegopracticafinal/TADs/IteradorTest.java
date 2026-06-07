package jueguito.juegopracticafinal.TADs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IteradorTest {

    @Test
    void hasNextListaConElementosDevuelveTrue() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        InterfazIterador<String> it = lista.iterador();
        assertTrue(it.hasNext());
    }

    @Test
    void nextDevuelveElementosEnOrden() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.add("C");
        InterfazIterador<String> it = lista.iterador();
        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertEquals("C", it.next());
    }

    @Test
    void iteradorRecorreTodosLosElementos() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.add("C");
        InterfazIterador<String> it = lista.iterador();
        int count = 0;
        while (it.hasNext()) {
            assertNotNull(it.next());
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void iteradorListaVaciaNoTieneSiguiente() {
        Lista<String> lista = new Lista<>();
        InterfazIterador<String> it = lista.iterador();
        assertFalse(it.hasNext());
    }

    @Test
    void hasNextFalseTrasRecorrerTodo() {
        Lista<String> lista = new Lista<>();
        lista.add("X");
        InterfazIterador<String> it = lista.iterador();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    void iteradorListaUnElemento() {
        Lista<String> lista = new Lista<>();
        lista.add("unico");
        InterfazIterador<String> it = lista.iterador();
        assertTrue(it.hasNext());
        assertEquals("unico", it.next());
        assertFalse(it.hasNext());
    }
}
