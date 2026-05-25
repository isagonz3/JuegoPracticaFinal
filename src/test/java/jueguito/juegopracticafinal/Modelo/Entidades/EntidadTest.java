package jueguito.juegopracticafinal.Modelo.Entidades;

import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EntidadTest {

    @Test void enemigo() {
        Enemigo e = new Enemigo("Orco", new Estadisticas(50, 8, 4, 3));
        assertEquals("Orco", e.getNombre());
        assertEquals("ENEMIGO", e.getTipoEntidad());
        assertEquals(8, e.getAtaqueTotal());
        assertEquals(4, e.getDefensaTotal());
        assertTrue(e.estarVivo());
    }

    @Test void gato() {
        Gato g = new Gato("Gatiko", new Estadisticas(10, 0, 0, 4));
        assertEquals("Gatiko", g.getNombre());
        assertEquals("GATO", g.getTipoEntidad());
    }

    @Test void enemigoMuerto() {
        Enemigo e = new Enemigo("Slime", new Estadisticas(1, 1, 0, 1));
        e.recibirAtaque(5);
        assertFalse(e.estarVivo());
    }

    @Test void enemigoConPosicion() {
        Posicion p = new Posicion(3, 4);
        Enemigo e = new Enemigo("Boss", new Estadisticas(100, 20, 15, 6));
        e.setPosicion(p);
        assertTrue(e.moverA(new Posicion(5, 6)));
        assertEquals(5, e.getPosicion().getRow());
    }

    @Test void gettersSettersEntidad() {
        Enemigo e = new Enemigo("Test", new Estadisticas(50, 5, 3, 2));
        e.setNombre("Renombrado"); assertEquals("Renombrado", e.getNombre());
        Estadisticas nuevas = new Estadisticas(80, 10, 8, 4);
        e.setEstadisticas(nuevas); assertEquals(nuevas, e.getEstadisticas());
        Posicion p = new Posicion(1, 1);
        e.setPosicion(p); assertEquals(p, e.getPosicion());
    }

    @Test void getAtaqueTotalEntidad() {
        Entidad e = new Enemigo("Test", new Estadisticas(100, 10, 5, 3));
        assertEquals(10, e.getAtaqueTotal());
    }

    @Test void getDefensaTotalEntidad() {
        Entidad e = new Enemigo("Test", new Estadisticas(100, 10, 5, 3));
        assertEquals(5, e.getDefensaTotal());
    }

    @Test void getAtaqueTotalSinStats() {
        Enemigo e = new Enemigo("Null", null);
        assertEquals(0, e.getAtaqueTotal());
    }

    @Test void getDefensaTotalSinStats() {
        Enemigo e = new Enemigo("Null", null);
        assertEquals(0, e.getDefensaTotal());
    }

    @Test void recibirAtaqueEntidadSinStats() {
        Enemigo e = new Enemigo("NullE", null);
        e.recibirAtaque(10);
    }

    @Test void estarVivoSinStats() {
        Enemigo e = new Enemigo("Null", null);
        assertFalse(e.estarVivo());
    }

    @Test void toStringEntidad() {
        Enemigo e = new Enemigo("Goblin", new Estadisticas(30, 5, 2, 3));
        assertNotNull(e.toString());
        assertTrue(e.toString().contains("Goblin"));
    }

    @Test void toStringEntidadSinStats() {
        Enemigo e = new Enemigo("Nada", null);
        assertEquals(" ", e.toString());
    }

    @Test void moverAFallaConNull() {
        Enemigo e = new Enemigo("Test", new Estadisticas(10, 1, 1, 1));
        assertFalse(e.moverA(null));
    }
}
