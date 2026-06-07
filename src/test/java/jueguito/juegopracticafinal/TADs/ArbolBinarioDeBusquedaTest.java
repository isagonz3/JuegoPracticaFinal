package jueguito.juegopracticafinal.TADs;

import static org.junit.jupiter.api.Assertions.*;

import jueguito.juegopracticafinal.Modelo.Excepciones.ExcepcionTADs;
import org.junit.jupiter.api.Test;

class ArbolBinarioDeBusquedaTest {

    @Test
    void constructorArbolVacio() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        assertTrue(arbol.isEmpty());
        assertEquals(0, arbol.size());
        assertEquals(0, arbol.getAltura());
    }

    @Test
    void addElementoIncrementaSize() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        assertEquals(1, arbol.size());
        assertFalse(arbol.isEmpty());
    }

    @Test
    void addNullLanzaExcepcion() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        assertThrows(ExcepcionTADs.class, () -> arbol.add(null));
    }

    @Test
    void addDuplicadoNoIncrementaSize() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(5);
        assertEquals(1, arbol.size());
    }

    @Test
    void addMultiplesMantieneSizeCorrecto() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        arbol.add(1);
        arbol.add(9);
        assertEquals(5, arbol.size());
    }

    @Test
    void containsElementoExistenteDevuelveTrue() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        assertTrue(arbol.contains(5));
        assertTrue(arbol.contains(3));
        assertTrue(arbol.contains(7));
    }

    @Test
    void containsElementoInexistenteDevuelveFalse() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        assertFalse(arbol.contains(10));
    }

    @Test
    void containsArbolVacioDevuelveFalse() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        assertFalse(arbol.contains(1));
    }

    @Test
    void deleteHojaReduceSize() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        assertTrue(arbol.delete(3));
        assertEquals(1, arbol.size());
        assertFalse(arbol.contains(3));
    }

    @Test
    void deleteNodoConUnHijo() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(2);
        assertTrue(arbol.delete(3));
        assertEquals(2, arbol.size());
        assertFalse(arbol.contains(3));
        assertTrue(arbol.contains(5));
        assertTrue(arbol.contains(2));
    }

    @Test
    void deleteNodoConDosHijos() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        arbol.add(2);
        arbol.add(4);
        assertTrue(arbol.delete(3));
        assertEquals(4, arbol.size());
        assertFalse(arbol.contains(3));
    }

    @Test
    void deleteElementoInexistenteDevuelveFalse() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        assertFalse(arbol.delete(10));
    }

    @Test
    void deleteNullDevuelveFalse() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        assertFalse(arbol.delete(null));
    }

    @Test
    void deleteUnicoElementoDejaArbolVacio() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        assertTrue(arbol.delete(5));
        assertTrue(arbol.isEmpty());
        assertEquals(0, arbol.size());
    }

    @Test
    void inOrdenDevuelveElementosOrdenados() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        arbol.add(1);
        arbol.add(9);
        Lista<Integer> lista = arbol.inOrden();
        assertEquals(5, lista.getSize());
        assertEquals(1, lista.get(0));
        assertEquals(3, lista.get(1));
        assertEquals(5, lista.get(2));
        assertEquals(7, lista.get(3));
        assertEquals(9, lista.get(4));
    }

    @Test
    void inOrdenArbolVacio() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        Lista<Integer> lista = arbol.inOrden();
        assertTrue(lista.isEmpty());
    }

    @Test
    void preOrdenArbolSimple() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        Lista<Integer> lista = arbol.preOrden();
        assertEquals(5, lista.get(0));
    }

    @Test
    void postOrdenArbolSimple() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        Lista<Integer> lista = arbol.postOrden();
        assertEquals(3, lista.get(0));
        assertEquals(7, lista.get(1));
        assertEquals(5, lista.get(2));
    }

    @Test
    void getAlturaArbolVacio() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        assertEquals(0, arbol.getAltura());
    }

    @Test
    void getAlturaUnNodo() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        assertEquals(1, arbol.getAltura());
    }

    @Test
    void getAlturaArbolBalanceado() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        assertEquals(2, arbol.getAltura());
    }

    @Test
    void getAlturaArbolSkew() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(1);
        arbol.add(2);
        arbol.add(3);
        arbol.add(4);
        assertEquals(4, arbol.getAltura());
    }

    @Test
    void clearVaciaElArbol() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        arbol.clear();
        assertTrue(arbol.isEmpty());
        assertEquals(0, arbol.size());
        assertEquals(0, arbol.getAltura());
        assertFalse(arbol.contains(5));
    }

    @Test
    void clearArbolVacioNoRompe() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.clear();
        assertTrue(arbol.isEmpty());
    }

    @Test
    void toStringArbolVacio() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        assertEquals("[]", arbol.toString());
    }

    @Test
    void toStringFormatoCorrecto() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(3);
        arbol.add(1);
        arbol.add(5);
        assertEquals("[1, 3, 5]", arbol.toString());
    }

    @Test
    void buscarElementoExistenteDevuelveNodo() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        NodoArbol<Integer> nodo = arbol.buscar(3);
        assertNotNull(nodo);
    }

    @Test
    void buscarElementoInexistenteDevuelveNull() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        assertNull(arbol.buscar(10));
    }

    @Test
    void getMinimoDevuelveElMenor() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        arbol.add(1);
        NodoArbol<Integer> min = arbol.getMinimo(arbol.buscar(5));
        assertEquals(1, min.elemento);
    }

    @Test
    void deleteRaizConDosHijos() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(5);
        arbol.add(3);
        arbol.add(7);
        arbol.add(2);
        arbol.add(4);
        arbol.add(6);
        arbol.add(8);
        assertTrue(arbol.delete(5));
        assertFalse(arbol.contains(5));
        assertEquals(6, arbol.size());
    }
}
