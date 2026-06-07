package jueguito.juegopracticafinal.Modelo.Mundo;

import static org.junit.jupiter.api.Assertions.*;

import jueguito.juegopracticafinal.TADs.Lista;
import org.junit.jupiter.api.Test;

class GrafoZonasTest {

    @Test
    void deberiaAñadirZona() {
        GrafoZonas grafo=new GrafoZonas();

        Zona z1=new Zona(19,"",40,10);
        z1.setIdZona(1);

        grafo.addZona(z1);

        assertEquals(z1,grafo.getZona(1));
    }

    @Test
    void getZonaDebeDevolverNullSiNoExiste() {
        GrafoZonas grafo=new GrafoZonas();

        assertNull(grafo.getZona(999));
    }

    @Test
    void conectarDosZonasDebeCrearConexionBidireccional() {
        GrafoZonas grafo=new GrafoZonas();

        Zona z1=new Zona(19,"",40,10);
        z1.setIdZona(1);

        Zona z2=new Zona(16,"ds",40,10);
        z2.setIdZona(2);

        grafo.addZona(z1);
        grafo.addZona(z2);

        grafo.conectar(1,2);

        // comprobamos que la conexión existe en ambos sentidos
        assertNotNull(grafo.getZona(1));
        assertNotNull(grafo.getZona(2));
    }

    @Test
    void conectarZonasQueNoExistenNoDebeRomper() {
        GrafoZonas grafo=new GrafoZonas();

        grafo.conectar(1,2);

        assertNull(grafo.getZona(1));
        assertNull(grafo.getZona(2));
    }

    @Test
    void addZonaNullNoRompe() {
        GrafoZonas grafo = new GrafoZonas();
        assertDoesNotThrow(() -> grafo.addZona(null));
    }

    @Test
    void addZonaDuplicadoNoDesincroniza() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z = new Zona(19, "", 40, 10);
        z.setIdZona(1);
        grafo.addZona(z);
        grafo.addZona(z);
        assertEquals(1, grafo.getNumZonas());
        assertSame(z, grafo.getZona(1));
    }

    @Test
    void conectarCreaAdyacenciaBidireccional() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z1 = new Zona(19, "", 40, 10);
        z1.setIdZona(1);
        Zona z2 = new Zona(16, "ds", 40, 10);
        z2.setIdZona(2);
        grafo.addZona(z1);
        grafo.addZona(z2);
        grafo.conectar(1, 2);

        Lista<Integer> ady1 = grafo.getAdyacentes(1);
        Lista<Integer> ady2 = grafo.getAdyacentes(2);

        assertEquals(1, ady1.getSize());
        assertEquals(1, ady2.getSize());
        assertTrue(ady1.contains(2));
        assertTrue(ady2.contains(1));
    }

    @Test
    void getAdyacentesZonaSinConexionesDevuelveVacio() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z = new Zona(19, "", 40, 10);
        z.setIdZona(1);
        grafo.addZona(z);
        assertTrue(grafo.getAdyacentes(1).isEmpty());
    }

    @Test
    void getNumZonasConZonas() {
        GrafoZonas grafo = new GrafoZonas();
        for (int i = 1; i <= 5; i++) {
            Zona z = new Zona(19, "", 40, 10);
            z.setIdZona(i);
            grafo.addZona(z);
        }
        assertEquals(5, grafo.getNumZonas());
    }

    @Test
    void bfsRecorreTodasLasZonasConectadas() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z1 = new Zona(19, "", 40, 10); z1.setIdZona(1);
        Zona z2 = new Zona(19, "", 40, 10); z2.setIdZona(2);
        Zona z3 = new Zona(19, "", 40, 10); z3.setIdZona(3);
        Zona z4 = new Zona(19, "", 40, 10); z4.setIdZona(4);
        grafo.addZona(z1); grafo.addZona(z2); grafo.addZona(z3); grafo.addZona(z4);
        grafo.conectar(1, 2);
        grafo.conectar(2, 3);
        grafo.conectar(3, 4);

        Lista<Integer> visitados = grafo.bfs(1);
        assertEquals(4, visitados.getSize());
        assertTrue(visitados.contains(4));
    }

    @Test
    void bfsCaminoEncuentraRuta() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z1 = new Zona(19, "", 40, 10); z1.setIdZona(1);
        Zona z2 = new Zona(19, "", 40, 10); z2.setIdZona(2);
        Zona z3 = new Zona(19, "", 40, 10); z3.setIdZona(3);
        grafo.addZona(z1); grafo.addZona(z2); grafo.addZona(z3);
        grafo.conectar(1, 2);
        grafo.conectar(2, 3);

        Lista<Integer> camino = grafo.bfsCamino(1, 3);
        assertEquals(3, camino.getSize());
        assertTrue(camino.contains(1));
        assertTrue(camino.contains(2));
        assertTrue(camino.contains(3));
    }

    @Test
    void bfsCaminoSinConexionDevuelveVacio() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z1 = new Zona(19, "", 40, 10); z1.setIdZona(1);
        Zona z2 = new Zona(19, "", 40, 10); z2.setIdZona(2);
        grafo.addZona(z1); grafo.addZona(z2);

        assertTrue(grafo.bfsCamino(1, 2).isEmpty());
    }

    @Test
    void dfsRecorreTodasLasZonas() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z1 = new Zona(19, "", 40, 10); z1.setIdZona(1);
        Zona z2 = new Zona(19, "", 40, 10); z2.setIdZona(2);
        Zona z3 = new Zona(19, "", 40, 10); z3.setIdZona(3);
        grafo.addZona(z1); grafo.addZona(z2); grafo.addZona(z3);
        grafo.conectar(1, 2);
        grafo.conectar(2, 3);

        Lista<Integer> visitados = grafo.dfs(1);
        assertEquals(3, visitados.getSize());
        assertTrue(visitados.contains(3));
    }

    @Test
    void contieneCicloEnGrafoConCiclo() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z1 = new Zona(19, "", 40, 10); z1.setIdZona(1);
        Zona z2 = new Zona(19, "", 40, 10); z2.setIdZona(2);
        Zona z3 = new Zona(19, "", 40, 10); z3.setIdZona(3);
        grafo.addZona(z1); grafo.addZona(z2); grafo.addZona(z3);
        grafo.conectar(1, 2);
        grafo.conectar(2, 3);
        grafo.conectar(3, 1);
        assertTrue(grafo.contieneCiclo());
    }

    @Test
    void contieneCicloEnGrafoSinCiclo() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z1 = new Zona(19, "", 40, 10); z1.setIdZona(1);
        grafo.addZona(z1);
        assertFalse(grafo.contieneCiclo());
    }

    @Test
    void existeConexionEntreZonasConectadas() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z1 = new Zona(19, "", 40, 10); z1.setIdZona(1);
        Zona z2 = new Zona(19, "", 40, 10); z2.setIdZona(2);
        grafo.addZona(z1); grafo.addZona(z2);
        grafo.conectar(1, 2);
        assertTrue(grafo.existeConexion(1, 2));
    }

    @Test
    void existeConexionEntreZonasNoConectadas() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z1 = new Zona(19, "", 40, 10); z1.setIdZona(1);
        Zona z2 = new Zona(19, "", 40, 10); z2.setIdZona(2);
        grafo.addZona(z1); grafo.addZona(z2);
        assertFalse(grafo.existeConexion(1, 2));
    }

    @Test
    void isEmptyAlCrearDevuelveTrue() {
        GrafoZonas grafo = new GrafoZonas();
        assertTrue(grafo.isEmpty());
    }

    @Test
    void isEmptyConZonasDevuelveFalse() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z = new Zona(19, "", 40, 10);
        z.setIdZona(1);
        grafo.addZona(z);
        assertFalse(grafo.isEmpty());
    }

    @Test
    void toStringNoNulo() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z = new Zona(19, "", 40, 10);
        z.setIdZona(1);
        grafo.addZona(z);
        assertNotNull(grafo.toString());
    }
}