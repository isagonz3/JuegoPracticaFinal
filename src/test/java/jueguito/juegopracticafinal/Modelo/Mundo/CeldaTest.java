package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CeldaTest {

    @Test void constructor() {
        Celda c = new Celda(TipoCelda.SUELO);
        assertEquals(TipoCelda.SUELO, c.getTipoCelda());
    }

    @Test void rowCol() {
        Celda c = new Celda(TipoCelda.SUELO);
        c.setRow(2); assertEquals(2, c.getRow());
        c.setCol(5); assertEquals(5, c.getCol());
    }

    @Test void tipoCelda() {
        Celda c = new Celda(TipoCelda.PARED);
        assertEquals(TipoCelda.PARED, c.getTipoCelda());
        c.setTipoCelda(TipoCelda.SUELO);
        assertEquals(TipoCelda.SUELO, c.getTipoCelda());
    }

    @Test void isTransitable() {
        assertTrue(new Celda(TipoCelda.SUELO).isTransitable());
        assertTrue(new Celda(TipoCelda.PUERTA).isTransitable());
        assertTrue(new Celda(TipoCelda.AGUA).isTransitable());
        assertTrue(new Celda(TipoCelda.SALIDA).isTransitable());
        assertFalse(new Celda(TipoCelda.PARED).isTransitable());
        assertFalse(new Celda(TipoCelda.VACIO).isTransitable());
        assertFalse(new Celda(TipoCelda.OBJETO).isTransitable());
        assertFalse(new Celda(TipoCelda.ENEMIGO).isTransitable());
        assertFalse(new Celda(TipoCelda.NPC).isTransitable());
        assertFalse(new Celda(TipoCelda.GATO).isTransitable());
        assertFalse(new Celda(TipoCelda.INTERACTUABLE).isTransitable());
    }

    @Test void puerta() {
        Celda c = new Celda(TipoCelda.PUERTA);
        assertFalse(c.tienePuerta());
        Puerta p = new Puerta(0, 1, 0, 0, 1, 1);
        c.setPuerta(p);
        assertTrue(c.tienePuerta());
        assertEquals(p, c.getPuerta());
    }

    @Test void entidad() {
        Celda c = new Celda(TipoCelda.SUELO);
        assertFalse(c.isOcupada());
        assertFalse(c.tieneEntidad());
        Enemigo e = new Enemigo("Test", new Estadisticas(10, 1, 1, 1));
        c.setEntidad(e);
        assertTrue(c.isOcupada());
        assertTrue(c.tieneEntidad());
        assertEquals(e, c.getEntidad());
        c.setEntidad(null);
        assertFalse(c.isOcupada());
    }

    @Test void objeto() {
        Celda c = new Celda(TipoCelda.SUELO);
        assertFalse(c.tieneObjeto());
        Objeto o = new Objeto("Pocion", TipoObjeto.CONSUMIBLE);
        c.setObjeto(o);
        assertTrue(c.tieneObjeto());
        assertEquals(o, c.getObjeto());
    }

    @Test void npc() {
        Celda c = new Celda(TipoCelda.SUELO);
        assertFalse(c.tieneNPC());
        NPC npc = new NPC("Aldeano", TipoNPC.ALDEANO);
        c.setNpc(npc);
        assertTrue(c.tieneNPC());
        assertEquals(npc, c.getNpc());
    }

    @Test void esAdyacente() {
        Celda c1 = new Celda(TipoCelda.SUELO); c1.setRow(2); c1.setCol(3);
        Celda c2 = new Celda(TipoCelda.SUELO); c2.setRow(2); c2.setCol(4);
        assertTrue(c1.esAdyacente(c2));
        Celda c3 = new Celda(TipoCelda.SUELO); c3.setRow(5); c3.setCol(3);
        assertFalse(c1.esAdyacente(c3));
        assertFalse(c1.esAdyacente(null));
    }

    @Test void getDistancia() {
        Celda c1 = new Celda(TipoCelda.SUELO); c1.setRow(0); c1.setCol(0);
        Celda c2 = new Celda(TipoCelda.SUELO); c2.setRow(3); c2.setCol(4);
        assertEquals(7, c1.getDistancia(c2));
        assertEquals(Integer.MAX_VALUE, c1.getDistancia(null));
    }

    @Test void clear() {
        Celda c = new Celda(TipoCelda.SUELO);
        c.setEntidad(new Enemigo("E", new Estadisticas(1,1,1,1)));
        c.setObjeto(new Objeto("O", TipoObjeto.CONSUMIBLE));
        c.setNpc(new NPC("N", TipoNPC.ALDEANO));
        c.setPuerta(new Puerta(0,1,0,0,1,1));
        c.clear();
        assertNull(c.getEntidad());
        assertNull(c.getObjeto());
        assertNull(c.getNpc());
        assertNull(c.getPuerta());
        assertEquals(TipoCelda.SUELO, c.getTipoCelda()); // no se borra
    }
}
