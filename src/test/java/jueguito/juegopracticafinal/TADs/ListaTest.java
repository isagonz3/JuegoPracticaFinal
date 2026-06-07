package jueguito.juegopracticafinal.TADs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ListaTest {

    @Test
    void addElementoIncrementaSize() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        assertEquals(1, lista.getSize());
    }

    @Test
    void addMultipleElementosMantieneOrden() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.add("C");
        assertEquals(3, lista.getSize());
        assertEquals("A", lista.get(0));
        assertEquals("B", lista.get(1));
        assertEquals("C", lista.get(2));
    }

    @Test
    void addFirstInsertaAlInicio() {
        Lista<String> lista = new Lista<>();
        lista.add("B");
        lista.addFirst("A");
        assertEquals("A", lista.get(0));
        assertEquals("B", lista.get(1));
        assertEquals(2, lista.getSize());
    }

    @Test
    void addFirstEnListaVacia() {
        Lista<String> lista = new Lista<>();
        lista.addFirst("X");
        assertEquals(1, lista.getSize());
        assertEquals("X", lista.get(0));
    }

    @Test
    void addAllAnyadeTodosLosElementos() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        Lista<String> otros = new Lista<>();
        otros.add("B");
        otros.add("C");
        lista.addAll(otros);
        assertEquals(3, lista.getSize());
        assertEquals("A", lista.get(0));
        assertEquals("B", lista.get(1));
        assertEquals("C", lista.get(2));
    }

    @Test
    void addAllListaNullNoCambiaNada() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.addAll(null);
        assertEquals(1, lista.getSize());
    }

    @Test
    void getIndiceValidoDevuelveElemento() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        assertEquals("A", lista.get(0));
        assertEquals("B", lista.get(1));
    }

    @Test
    void getIndiceNegativoDevuelveNull() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        assertNull(lista.get(-1));
    }

    @Test
    void getIndiceMayorQueSizeDevuelveNull() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        assertNull(lista.get(5));
    }

    @Test
    void setSobrescribeElementoExistente() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.set(1, "X");
        assertEquals("X", lista.get(1));
        assertEquals(2, lista.getSize());
    }

    @Test
    void setIndiceInvalidoNoCambiaNada() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.set(5, "X");
        assertEquals(1, lista.getSize());
        assertEquals("A", lista.get(0));
    }

    @Test
    void deleteElementoExistenteLoElimina() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.add("C");
        String r = lista.delete("B");
        assertEquals("B", r);
        assertEquals(2, lista.getSize());
        assertEquals("A", lista.get(0));
        assertEquals("C", lista.get(1));
    }

    @Test
    void deletePrimerElemento() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.delete("A");
        assertEquals(1, lista.getSize());
        assertEquals("B", lista.get(0));
    }

    @Test
    void deleteUltimoElemento() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.delete("B");
        assertEquals(1, lista.getSize());
        assertEquals("A", lista.get(0));
    }

    @Test
    void deleteElementoInexistenteDevuelveNull() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        assertNull(lista.delete("X"));
        assertEquals(1, lista.getSize());
    }

    @Test
    void deleteEnListaVaciaDevuelveNull() {
        Lista<String> lista = new Lista<>();
        assertNull(lista.delete("X"));
    }

    @Test
    void deleteLastEliminaUltimo() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        assertEquals("B", lista.deleteLast());
        assertEquals(1, lista.getSize());
        assertEquals("A", lista.getFirst());
    }

    @Test
    void deleteLastEnListaVaciaDevuelveNull() {
        Lista<String> lista = new Lista<>();
        assertNull(lista.deleteLast());
    }

    @Test
    void deleteLastUnicoElemento() {
        Lista<String> lista = new Lista<>();
        lista.add("X");
        assertEquals("X", lista.deleteLast());
        assertTrue(lista.isEmpty());
    }

    @Test
    void isEmptyListaVaciaDevuelveTrue() {
        Lista<String> lista = new Lista<>();
        assertTrue(lista.isEmpty());
    }

    @Test
    void isEmptyListaConElementosDevuelveFalse() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        assertFalse(lista.isEmpty());
    }

    @Test
    void getSizeListaVacia() {
        Lista<String> lista = new Lista<>();
        assertEquals(0, lista.getSize());
    }

    @Test
    void getSizeTrasAddYDelete() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.delete("A");
        assertEquals(1, lista.getSize());
    }

    @Test
    void containsElementoExistenteDevuelveTrue() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        assertTrue(lista.contains("B"));
    }

    @Test
    void containsElementoInexistenteDevuelveFalse() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        assertFalse(lista.contains("X"));
    }

    @Test
    void containsListaVaciaDevuelveFalse() {
        Lista<String> lista = new Lista<>();
        assertFalse(lista.contains("X"));
    }

    @Test
    void getFirstListaVaciaDevuelveNull() {
        Lista<String> lista = new Lista<>();
        assertNull(lista.getFirst());
    }

    @Test
    void getFirstListaConElementos() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        assertEquals("A", lista.getFirst());
    }

    @Test
    void getFirstTrasDeleteFirst() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.delete("A");
        assertEquals("B", lista.getFirst());
    }

    @Test
    void iteradorRecorreTodosLosElementos() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        lista.add("C");
        InterfazIterador<String> it = lista.iterador();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertTrue(it.hasNext());
        assertEquals("C", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void iteradorListaVaciaNoTieneSiguiente() {
        Lista<String> lista = new Lista<>();
        InterfazIterador<String> it = lista.iterador();
        assertFalse(it.hasNext());
    }

    @Test
    void toStringListaVacia() {
        Lista<String> lista = new Lista<>();
        assertEquals("[]", lista.toString());
    }

    @Test
    void toStringListaConElementos() {
        Lista<String> lista = new Lista<>();
        lista.add("A");
        lista.add("B");
        String result = lista.toString();
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
    }
}
