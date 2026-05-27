package jueguito.juegopracticafinal.Modelo.Mundo;

import static org.junit.jupiter.api.Assertions.*;

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
}