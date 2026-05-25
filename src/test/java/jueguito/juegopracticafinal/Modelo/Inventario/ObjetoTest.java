package jueguito.juegopracticafinal.Modelo.Inventario;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ObjetoTest {

    @Test void constructorVacio() {
        Objeto o = new Objeto();
        assertEquals("Objeto", o.getNombre());
        assertEquals(TipoObjeto.CONSUMIBLE, o.getTipo());
        assertEquals(1, o.getUsosMax());
        assertEquals(1, o.getUsosRestantes());
    }

    @Test void constructorNombreTipo() {
        Objeto o = new Objeto("Pocion", TipoObjeto.CONSUMIBLE);
        assertEquals("Pocion", o.getNombre());
        assertEquals(TipoObjeto.CONSUMIBLE, o.getTipo());
    }

    @Test void constructorCompleto() {
        Objeto o = new Objeto("Espada", TipoObjeto.EQUIPABLE, 5, 2, 0, 1, 99, "Una espada");
        assertEquals("Espada", o.getNombre());
        assertEquals(TipoObjeto.EQUIPABLE, o.getTipo());
        assertEquals(5, o.getAtaqueBonus());
        assertEquals(2, o.getDefensaBonus());
        assertEquals(0, o.getVidaBonus());
        assertEquals(1, o.getRangoBonus());
        assertEquals(99, o.getUsosMax());
        assertEquals("Una espada", o.getDescripcion());
    }

    @Test void constructorPlaceholder() {
        Objeto o = new Objeto("Espada");
        assertNotNull(o);
    }

    @Test void gettersSetters() {
        Objeto o = new Objeto("X", TipoObjeto.CONSUMIBLE);
        o.setNombre("Y"); assertEquals("Y", o.getNombre());
        o.setTipo(TipoObjeto.EQUIPABLE); assertEquals(TipoObjeto.EQUIPABLE, o.getTipo());
        o.setSlot(SlotEquipable.ARMA); assertEquals(SlotEquipable.ARMA, o.getSlot());
    }

    @Test void isConsumible() {
        assertTrue(new Objeto("Pocion", TipoObjeto.CONSUMIBLE).isConsumible());
        assertTrue(new Objeto("Llave", TipoObjeto.LLAVE).isConsumible());
        assertFalse(new Objeto("Espada", TipoObjeto.EQUIPABLE).isConsumible());
        assertFalse(new Objeto("Mapa", TipoObjeto.MAPA).isConsumible());
        assertFalse(new Objeto("Usable", TipoObjeto.USABLE).isConsumible());
    }

    @Test void usarObjeto() {
        Objeto o = new Objeto("Pocion", TipoObjeto.CONSUMIBLE, 0, 0, 5, 0, 3, "");
        assertTrue(o.usarObjeto());
        assertEquals(2, o.getUsosRestantes());
        assertTrue(o.usarObjeto());
        assertTrue(o.usarObjeto());
        assertFalse(o.usarObjeto());
        assertEquals(0, o.getUsosRestantes());
    }

    @Test void objetoGastado() {
        Objeto o = new Objeto("Pocion", TipoObjeto.CONSUMIBLE, 0, 0, 5, 0, 1, "");
        assertFalse(o.objetoGastado());
        o.usarObjeto();
        assertTrue(o.objetoGastado());
    }

    @Test void objetoGastadoEquipableNoSeGasta() {
        Objeto o = new Objeto("Espada", TipoObjeto.EQUIPABLE, 3, 0, 0, 0, 99, "");
        assertFalse(o.objetoGastado());
        o.usarObjeto();
        assertFalse(o.objetoGastado());
    }

    @Test void copiar() {
        Objeto o = new Objeto("Espada", TipoObjeto.EQUIPABLE, 5, 2, 0, 1, 99, "Copia");
        o.setSlot(SlotEquipable.ARMA);
        Objeto copia = o.copiar();
        assertEquals(o.getNombre(), copia.getNombre());
        assertEquals(o.getAtaqueBonus(), copia.getAtaqueBonus());
        assertEquals(SlotEquipable.ARMA, copia.getSlot());
        copia.usarObjeto();
        assertEquals(99, o.getUsosRestantes());
    }

    @Test void toStringDevuelveNombre() {
        Objeto o = new Objeto("PocionMagica", TipoObjeto.CONSUMIBLE);
        assertEquals("PocionMagica", o.toString());
    }

    @Test void rangoBonus() {
        Objeto o = new Objeto("Botas", TipoObjeto.EQUIPABLE, 0, 0, 0, 3, 99, "");
        assertEquals(3, o.getRangoBonus());
    }

    @Test void vidaBonus() {
        Objeto o = new Objeto("PocionV", TipoObjeto.CONSUMIBLE, 0, 0, 20, 0, 1, "");
        assertEquals(20, o.getVidaBonus());
    }

    @Test void getDescripcion() {
        Objeto o = new Objeto("X", TipoObjeto.CONSUMIBLE, 0, 0, 0, 0, 1, "Descripcion");
        assertEquals("Descripcion", o.getDescripcion());
    }

    @Test void getUsosMax() {
        Objeto o = new Objeto("X", TipoObjeto.CONSUMIBLE, 0, 0, 0, 0, 5, "");
        assertEquals(5, o.getUsosMax());
    }
}
