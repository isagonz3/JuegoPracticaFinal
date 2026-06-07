package jueguito.juegopracticafinal.Modelo.Inventario;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ObjetoTest {

    @Test
    void constructorVacioDebeInicializarValoresPorDefecto(){
        Objeto o=new Objeto();
        assertEquals("Objeto",o.getNombre());
        assertEquals(TipoObjeto.USABLE,o.getTipo());
        assertNull(o.getSlot());
        assertEquals(1,o.getUsosMax());
        assertEquals(1,o.getUsosRestantes());
        assertEquals("",o.getDescripcion());
    }

    @Test
    void constructorSimpleDebeAsignarNombreYTipo(){
        Objeto o=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        assertEquals("Espada",o.getNombre());
        assertEquals(TipoObjeto.EQUIPABLE,o.getTipo());
    }

    @Test
    void constructorCompletoDebeAsignarTodoCorrectamente(){
        Objeto o=new Objeto("Espada",TipoObjeto.EQUIPABLE,10,5,3,2,4,"estoy hasta las narices de los test");
        assertEquals("Espada",o.getNombre());
        assertEquals(TipoObjeto.EQUIPABLE,o.getTipo());
        assertEquals(10,o.getAtaqueBonus());
        assertEquals(5,o.getDefensaBonus());
        assertEquals(3,o.getVidaBonus());
        assertEquals(2,o.getRangoBonus());
        assertEquals(4,o.getUsosMax());
        assertEquals(4,o.getUsosRestantes());
        assertEquals("estoy hasta las narices de los test",o.getDescripcion());
        assertTrue(o.getId()>=0);
    }

    @Test
    void setSlotDebeFuncionar(){
        Objeto o=new Objeto();
        o.setSlot(SlotEquipable.ARMA);
        assertEquals(SlotEquipable.ARMA,o.getSlot());
    }

    @Test
    void turnosDebeIncrementarYResetear(){
        Objeto o=new Objeto();
        o.incrementarTurno();
        o.incrementarTurno();
        assertEquals(2,o.getTurnosActivos());
        o.resetTurnos();
        assertEquals(0,o.getTurnosActivos());
    }

    @Test
    void duracionTurnosDebeFuncionar(){
        Objeto o=new Objeto();
        o.setDuracionTurnos(5);
        assertEquals(5,o.getDuracionTurnos());
    }

    @Test
    void isConsumibleDebeSerTrueParaUsableYLlave(){
        Objeto o1=new Objeto("Pocion",TipoObjeto.USABLE);
        Objeto o2=new Objeto("Llave",TipoObjeto.LLAVE);
        assertTrue(o1.isConsumible());
        assertTrue(o2.isConsumible());
    }

    @Test
    void isConsumibleDebeSerFalseParaEquipable(){
        Objeto o=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        assertFalse(o.isConsumible());
    }

    @Test
    void usarObjetoDebeReducirUsos(){
        Objeto o=new Objeto("Pocion",TipoObjeto.USABLE,0,0,0,0,2,"me quedo sin ideas");
        assertTrue(o.usarObjeto());
        assertEquals(1,o.getUsosRestantes());
        assertTrue(o.usarObjeto());
        assertEquals(0,o.getUsosRestantes());
    }

    @Test
    void usarObjetoSinUsosDebeDevolverFalse(){
        Objeto o=new Objeto("Pocion",TipoObjeto.USABLE,0,0,0,0,1,"pfhghf");
        o.usarObjeto();
        assertFalse(o.usarObjeto());
    }

    @Test
    void toStringDebeMostrarNombre(){
        Objeto o=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        assertEquals("Espada",o.toString());
    }

    @Test
    void compareToMismoNombreDevuelveCero(){
        Objeto a=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        Objeto b=new Objeto("Espada",TipoObjeto.USABLE);
        assertEquals(0,a.compareTo(b));
    }

    @Test
    void compareToDistintoNombreNoDevuelveCero(){
        Objeto a=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        Objeto b=new Objeto("Arco",TipoObjeto.EQUIPABLE);
        assertNotEquals(0,a.compareTo(b));
    }
}