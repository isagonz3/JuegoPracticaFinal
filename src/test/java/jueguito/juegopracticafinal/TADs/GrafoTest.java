package jueguito.juegopracticafinal.TADs;

import static org.junit.jupiter.api.Assertions.*;

import jueguito.juegopracticafinal.Modelo.Excepciones.ExcepcionTADs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GrafoTest {

    private Grafo<String, Arista<String>> grafo;

    @BeforeEach
    void setUp() {
        grafo = new Grafo<>();
    }

    @Test
    void constructorCreaGrafoVacio() {
        assertTrue(grafo.isEmpty());
        assertEquals(0, grafo.size());
    }

    @Test
    void addNodoAnyadeNodo() {
        grafo.addNodo("A");
        assertEquals(1, grafo.size());
        assertFalse(grafo.isEmpty());
        assertTrue(grafo.getNodos().contains("A"));
    }

    @Test
    void addNodoMultipleAnyadeTodos() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        assertEquals(3, grafo.size());
    }

    @Test
    void addNodoDuplicadoNoCambiaSize() {
        grafo.addNodo("A");
        grafo.addNodo("A");
        assertEquals(1, grafo.size());
    }

    @Test
    void addNodoNullLanzaExcepcion() {
        assertThrows(ExcepcionTADs.class, () -> grafo.addNodo(null));
    }

    @Test
    void addAristaConectaNodos() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        assertTrue(grafo.addArista(new Arista<>("A", "B")));
        assertTrue(grafo.existeArista("A", "B"));
        assertNotNull(grafo.getArista("A", "B"));
    }

    @Test
    void addAristaNullLanzaExcepcion() {
        assertThrows(ExcepcionTADs.class, () -> grafo.addArista(null));
    }

    @Test
    void addAristaOrigenNoExistenteLanzaExcepcion() {
        grafo.addNodo("B");
        assertThrows(ExcepcionTADs.class, () -> grafo.addArista(new Arista<>("A", "B")));
    }

    @Test
    void addAristaDestinoNoExistenteLanzaExcepcion() {
        grafo.addNodo("A");
        assertThrows(ExcepcionTADs.class, () -> grafo.addArista(new Arista<>("A", "B")));
    }

    @Test
    void addAristaDuplicadaDevuelveFalse() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        assertTrue(grafo.addArista(new Arista<>("A", "B")));
        assertFalse(grafo.addArista(new Arista<>("A", "B")));
    }

    @Test
    void getNodosDevuelveCopiaNoReferencia() {
        grafo.addNodo("A");
        Lista<String> copia = grafo.getNodos();
        copia.add("X");
        assertEquals(1, grafo.size());
        assertFalse(grafo.getNodos().contains("X"));
    }

    @Test
    void getAristasDevuelveTodas() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        grafo.addArista(new Arista<>("A", "B"));
        grafo.addArista(new Arista<>("B", "C"));
        grafo.addArista(new Arista<>("A", "C"));
        assertEquals(3, grafo.getAristas().getSize());
    }

    @Test
    void getAristasVacioSiNoHay() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        assertTrue(grafo.getAristas().isEmpty());
    }

    @Test
    void getAdyacentesDevuelveVecinos() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        grafo.addArista(new Arista<>("A", "B"));
        grafo.addArista(new Arista<>("A", "C"));
        Lista<String> ady = grafo.getAdyacentes("A");
        assertEquals(2, ady.getSize());
        assertTrue(ady.contains("B"));
        assertTrue(ady.contains("C"));
    }

    @Test
    void getAdyacentesNodoInexistenteDevuelveVacio() {
        assertTrue(grafo.getAdyacentes("X").isEmpty());
    }

    @Test
    void getAdyacentesNodoSinAristasDevuelveVacio() {
        grafo.addNodo("A");
        assertTrue(grafo.getAdyacentes("A").isEmpty());
    }

    @Test
    void getAristasDesdeDevuelveAristasSalientes() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        grafo.addArista(new Arista<>("A", "B"));
        grafo.addArista(new Arista<>("A", "C"));
        assertEquals(2, grafo.getAristasDesde("A").getSize());
        assertEquals(0, grafo.getAristasDesde("B").getSize());
    }

    @Test
    void getAristaExistenteLaDevuelve() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        Arista<String> a = new Arista<>("A", "B");
        grafo.addArista(a);
        assertEquals(a, grafo.getArista("A", "B"));
    }

    @Test
    void getAristaInexistenteDevuelveNull() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        assertNull(grafo.getArista("A", "B"));
    }

    @Test
    void existeAristaTrueSiExiste() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addArista(new Arista<>("A", "B"));
        assertTrue(grafo.existeArista("A", "B"));
    }

    @Test
    void existeAristaFalseSiNoExiste() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        assertFalse(grafo.existeArista("A", "B"));
    }

    @Test
    void getIndexDevuelveIndiceCorrecto() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        assertEquals(0, grafo.getIndex("A"));
        assertEquals(1, grafo.getIndex("B"));
    }

    @Test
    void getIndexNodoInexistenteDevuelveMenosUno() {
        assertEquals(-1, grafo.getIndex("X"));
    }

    @Test
    void bfsRecorreTodosLosNodos() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        grafo.addNodo("D");
        grafo.addArista(new Arista<>("A", "B"));
        grafo.addArista(new Arista<>("A", "C"));
        grafo.addArista(new Arista<>("B", "D"));
        Lista<String> visitados = grafo.bfs("A");
        assertEquals(4, visitados.getSize());
        assertTrue(visitados.contains("A"));
        assertTrue(visitados.contains("B"));
        assertTrue(visitados.contains("C"));
        assertTrue(visitados.contains("D"));
    }

    @Test
    void bfsOrigenNoExistenteDevuelveVacio() {
        assertTrue(grafo.bfs("X").isEmpty());
    }

    @Test
    void bfsOrigenNullDevuelveVacio() {
        assertTrue(grafo.bfs(null).isEmpty());
    }

    @Test
    void dfsRecorreTodosLosNodos() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        grafo.addArista(new Arista<>("A", "B"));
        grafo.addArista(new Arista<>("A", "C"));
        Lista<String> visitados = grafo.dfs("A");
        assertEquals(3, visitados.getSize());
        assertTrue(visitados.contains("A"));
        assertTrue(visitados.contains("B"));
        assertTrue(visitados.contains("C"));
    }

    @Test
    void dfsOrigenNoExistenteDevuelveVacio() {
        assertTrue(grafo.dfs("X").isEmpty());
    }

    @Test
    void bfsCaminoEncuentraRuta() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        grafo.addNodo("D");
        grafo.addArista(new Arista<>("A", "B"));
        grafo.addArista(new Arista<>("B", "C"));
        grafo.addArista(new Arista<>("C", "D"));
        Lista<String> camino = grafo.bfsCamino("A", "D");
        assertTrue(camino.getSize() >= 2);
        assertEquals("A", camino.get(0));
        assertEquals("D", camino.get(camino.getSize() - 1));
    }

    @Test
    void bfsCaminoOrigenIgualDestino() {
        grafo.addNodo("A");
        Lista<String> camino = grafo.bfsCamino("A", "A");
        assertEquals(1, camino.getSize());
        assertEquals("A", camino.get(0));
    }

    @Test
    void bfsCaminoSinRutaDevuelveVacio() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        assertTrue(grafo.bfsCamino("A", "B").isEmpty());
    }

    @Test
    void bfsCaminoOrigenNullDevuelveVacio() {
        grafo.addNodo("A");
        assertTrue(grafo.bfsCamino(null, "A").isEmpty());
    }

    @Test
    void bfsCaminoDestinoNullDevuelveVacio() {
        grafo.addNodo("A");
        assertTrue(grafo.bfsCamino("A", null).isEmpty());
    }

    @Test
    void contieneCicloEnGrafoConCiclo() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        grafo.addArista(new Arista<>("A", "B"));
        grafo.addArista(new Arista<>("B", "C"));
        grafo.addArista(new Arista<>("C", "A"));
        assertTrue(grafo.contieneCiclo());
    }

    @Test
    void contieneCicloEnGrafoSinCiclo() {
        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        grafo.addArista(new Arista<>("A", "B"));
        grafo.addArista(new Arista<>("B", "C"));
        assertFalse(grafo.contieneCiclo());
    }

    @Test
    void contieneCicloEnGrafoVacio() {
        assertFalse(grafo.contieneCiclo());
    }

    @Test
    void toStringNoNulo() {
        grafo.addNodo("A");
        assertNotNull(grafo.toString());
        assertTrue(grafo.toString().contains("A"));
    }
}
