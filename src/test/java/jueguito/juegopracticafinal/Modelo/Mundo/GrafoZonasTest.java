package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.TADs.Lista;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GrafoZonasTest {

    private GrafoZonas g;

    @BeforeEach
    void setUp() {
        g = new GrafoZonas();
        g.addZona(new Zona(0, "Casa", 5, 5));
        g.addZona(new Zona(1, "Bosque", 5, 5));
        g.addZona(new Zona(2, "Castillo", 5, 5));
    }

    @Test void addZona() {
        g.addZona(new Zona(3, "Cueva", 5, 5));
        assertNotNull(g.getZona(3));
        assertEquals("Cueva", g.getZona(3).getNombreZona());
    }

    @Test void getZona() {
        Zona z = g.getZona(1);
        assertNotNull(z);
        assertEquals("Bosque", z.getNombreZona());
        assertNull(g.getZona(99));
    }

    @Test void conectar() {
        g.conectar(0, 1);
        g.conectar(1, 2);
        Lista<Integer> vecinos0 = g.getZonasVecinas(0);
        assertEquals(1, vecinos0.getSize());
        assertEquals(1, vecinos0.get(0).intValue());
        Lista<Integer> vecinos1 = g.getZonasVecinas(1);
        assertEquals(2, vecinos1.getSize());
    }

    @Test void conectarDuplicado() {
        g.conectar(0, 1);
        g.conectar(0, 1);
        Lista<Integer> vecinos0 = g.getZonasVecinas(0);
        assertEquals(1, vecinos0.getSize());
    }

    @Test void conectarZonaNoExistente() {
        g.conectar(0, 99);
        assertTrue(g.getZonasVecinas(0).isEmpty());
    }

    @Test void conectarAmbasNoExistentes() {
        g.conectar(99, 100);
    }

    @Test void zonaValida() {
        assertTrue(g.zonaValida(0));
        assertTrue(g.zonaValida(2));
        assertFalse(g.zonaValida(99));
    }

    @Test void getZonasVecinas() {
        g.conectar(0, 1);
        Lista<Integer> vecinos = g.getZonasVecinas(0);
        assertEquals(1, vecinos.getSize());
        assertEquals(1, vecinos.get(0).intValue());
        assertTrue(g.getZonasVecinas(99).isEmpty());
    }

    @Test void existeCamino() {
        g.conectar(0, 1);
        g.conectar(1, 2);
        assertTrue(g.existeCamino(0, 2));
        assertFalse(g.existeCamino(0, 99));
    }

    @Test void existeCaminoMismoNodo() {
        assertTrue(g.existeCamino(1, 1));
    }

    @Test void BFSZonas() {
        g.conectar(0, 1);
        g.conectar(1, 2);
        Lista<Integer> camino = g.BFSZonas(0, 2);
        assertNotNull(camino);
        assertEquals(0, camino.get(0).intValue());
        assertEquals(2, camino.get(camino.getSize() - 1).intValue());
    }

    @Test void BFSZonasMismoOrigenDestino() {
        Lista<Integer> camino = g.BFSZonas(1, 1);
        assertNotNull(camino);
        assertEquals(1, camino.getSize());
        assertEquals(1, camino.get(0).intValue());
    }

    @Test void BFSZonasInalcanzable() {
        assertNull(g.BFSZonas(0, 2));
    }

    @Test void BFSZonasOrigenInvalido() {
        assertNull(g.BFSZonas(99, 0));
    }

    @Test void BFSZonasDestinoInvalido() {
        assertNull(g.BFSZonas(0, 99));
    }

    @Test void grafoVacio() {
        GrafoZonas vacio = new GrafoZonas();
        assertNull(vacio.getZona(0));
        assertTrue(vacio.getZonasVecinas(0).isEmpty());
        assertFalse(vacio.zonaValida(0));
        assertNull(vacio.BFSZonas(0, 1));
    }
}
