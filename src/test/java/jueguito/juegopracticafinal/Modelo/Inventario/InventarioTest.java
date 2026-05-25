package jueguito.juegopracticafinal.Modelo.Inventario;

import jueguito.juegopracticafinal.TADs.Lista;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventarioTest {

    private Inventario inv;

    @BeforeEach
    void setUp() {
        inv = new Inventario(3);
    }

    @Test void constructorDefecto() {
        Inventario i = new Inventario();
        assertEquals(10, i.getSizeInventario());
        assertTrue(i.inventarioVacio());
    }

    @Test void constructorTamanio() {
        assertEquals(3, inv.getSizeInventario());
        assertTrue(inv.inventarioVacio());
    }

    @Test void addObjeto() {
        assertTrue(inv.addObjeto(new Objeto("A", TipoObjeto.CONSUMIBLE)));
        assertEquals(1, inv.size());
    }

    @Test void addObjetoInventarioLleno() {
        inv.addObjeto(new Objeto("A", TipoObjeto.CONSUMIBLE));
        inv.addObjeto(new Objeto("B", TipoObjeto.CONSUMIBLE));
        inv.addObjeto(new Objeto("C", TipoObjeto.CONSUMIBLE));
        assertFalse(inv.addObjeto(new Objeto("D", TipoObjeto.CONSUMIBLE)));
        assertEquals(3, inv.size());
    }

    @Test void removeObjeto() {
        Objeto o = new Objeto("A", TipoObjeto.CONSUMIBLE);
        inv.addObjeto(o);
        assertTrue(inv.removeObjeto(o));
        assertTrue(inv.inventarioVacio());
        assertFalse(inv.removeObjeto(o));
    }

    @Test void containsObjeto() {
        inv.addObjeto(new Objeto("Pocion", TipoObjeto.CONSUMIBLE));
        assertTrue(inv.containsObjeto("Pocion"));
        assertFalse(inv.containsObjeto("Espada"));
    }

    @Test void getObjeto() {
        Objeto o = new Objeto("A", TipoObjeto.CONSUMIBLE);
        inv.addObjeto(o);
        assertEquals(o, inv.getObjeto(0));
        assertNull(inv.getObjeto(5));
    }

    @Test void findObjeto() {
        Objeto o = new Objeto("Pocion", TipoObjeto.CONSUMIBLE);
        inv.addObjeto(o);
        assertEquals(o, inv.findObjeto("Pocion"));
        assertNull(inv.findObjeto("Inexistente"));
    }

    @Test void findObjetoVacio() {
        assertNull(inv.findObjeto("Nada"));
    }

    @Test void size() {
        assertEquals(0, inv.size());
        inv.addObjeto(new Objeto("A", TipoObjeto.CONSUMIBLE));
        assertEquals(1, inv.size());
    }

    @Test void inventarioLleno() {
        assertFalse(inv.inventarioLleno());
        inv.addObjeto(new Objeto("A", TipoObjeto.CONSUMIBLE));
        inv.addObjeto(new Objeto("B", TipoObjeto.CONSUMIBLE));
        inv.addObjeto(new Objeto("C", TipoObjeto.CONSUMIBLE));
        assertTrue(inv.inventarioLleno());
    }

    @Test void inventarioVacio() {
        assertTrue(inv.inventarioVacio());
        inv.addObjeto(new Objeto("A", TipoObjeto.CONSUMIBLE));
        assertFalse(inv.inventarioVacio());
    }

    @Test void getObjetos() {
        inv.addObjeto(new Objeto("A", TipoObjeto.CONSUMIBLE));
        Lista<Objeto> obs = inv.getObjetos();
        assertEquals(1, obs.getSize());
        assertEquals("A", obs.get(0).getNombre());
    }

    @Test void setSizeInventario() {
        inv.setSizeInventario(5);
        assertEquals(5, inv.getSizeInventario());
    }

    @Test void getSizeInventario() {
        assertEquals(3, inv.getSizeInventario());
    }
}
