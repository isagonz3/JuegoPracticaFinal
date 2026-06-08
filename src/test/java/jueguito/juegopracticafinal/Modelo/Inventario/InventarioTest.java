package jueguito.juegopracticafinal.Modelo.Inventario;

import jueguito.juegopracticafinal.TADs.Lista;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventarioTest {

    @Test
    void constructorDebeCrearInventarioConSizePorDefecto() {
        Inventario inv=new Inventario();

        assertEquals(0,inv.size());
        assertFalse(inv.inventarioLleno());
    }

    @Test
    void constructorConSizeDebeFuncionar() {
        Inventario inv=new Inventario(2);

        assertEquals(0,inv.size());
        assertFalse(inv.inventarioLleno());
    }

    @Test
    void addObjetoDebeAñadirCorrectamente() {
        Inventario inv=new Inventario(2);

        Objeto o1=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        Objeto o2=new Objeto("Escudo",TipoObjeto.EQUIPABLE);

        assertTrue(inv.addObjeto(o1));
        assertTrue(inv.addObjeto(o2));
        assertEquals(2,inv.size());
    }

    @Test
    void addObjetoDebeFallarSiEstaLleno() {
        Inventario inv=new Inventario(1);

        Objeto o1=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        Objeto o2=new Objeto("Escudo",TipoObjeto.EQUIPABLE);

        assertTrue(inv.addObjeto(o1));
        assertFalse(inv.addObjeto(o2));
    }

    @Test
    void removeObjetoDebeEliminar() {
        Inventario inv=new Inventario();

        Objeto o1=new Objeto("Espada",TipoObjeto.EQUIPABLE);

        inv.addObjeto(o1);

        assertTrue(inv.removeObjeto(o1));
        assertEquals(0,inv.size());
    }

    @Test
    void removeObjetoDebeFallarSiNoExiste() {
        Inventario inv=new Inventario();

        Objeto o1=new Objeto("Espada",TipoObjeto.EQUIPABLE);

        assertFalse(inv.removeObjeto(o1));
    }

    @Test
    void inventarioLlenoDebeDetectarLimite() {
        Inventario inv=new Inventario(1);

        Objeto o1=new Objeto("Espada",TipoObjeto.EQUIPABLE);

        assertFalse(inv.inventarioLleno());

        inv.addObjeto(o1);

        assertTrue(inv.inventarioLleno());
    }

    @Test
    void contieneDebeDetectarObjetoPorId() {
        Inventario inv=new Inventario();

        Objeto o1=new Objeto("Espada",TipoObjeto.EQUIPABLE);

        inv.addObjeto(o1);

        assertTrue(inv.contiene(o1));
    }

    @Test
    void contieneDebeFallarSiNoExiste() {
        Inventario inv=new Inventario();

        Objeto o1=new Objeto("Espada",TipoObjeto.EQUIPABLE);

        assertFalse(inv.contiene(o1));
    }

    @Test
    void equiparDebePermitirPrimerosObjetos() {
        Inventario inv=new Inventario();

        Objeto arma=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        arma.setSlot(SlotEquipable.ARMA);

        Objeto escudo=new Objeto("Escudo",TipoObjeto.EQUIPABLE);
        escudo.setSlot(SlotEquipable.ESCUDO);

        assertTrue(inv.equipar(arma));
        assertTrue(inv.equipar(escudo));
    }

    @Test
    void equiparDebeFallarPorMismoSlot() {
        Inventario inv=new Inventario();

        Objeto arma1=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        arma1.setSlot(SlotEquipable.ARMA);

        Objeto arma2=new Objeto("Hacha",TipoObjeto.EQUIPABLE);
        arma2.setSlot(SlotEquipable.ARMA);

        assertTrue(inv.equipar(arma1));
        assertFalse(inv.equipar(arma2));
    }

    @Test
    void equiparDebeFallarSiMaximoEquipados() {
        Inventario inv=new Inventario();

        Objeto o1=new Objeto("A",TipoObjeto.EQUIPABLE);
        o1.setSlot(SlotEquipable.ARMA);

        Objeto o2=new Objeto("B",TipoObjeto.EQUIPABLE);
        o2.setSlot(SlotEquipable.ESCUDO);

        Objeto o3=new Objeto("C",TipoObjeto.EQUIPABLE);
        o3.setSlot(SlotEquipable.ARMA);

        inv.equipar(o1);
        inv.equipar(o2);

        assertFalse(inv.equipar(o3));
    }

    @Test
    void getObjetoDebeDevolverCorrecto() {
        Inventario inv=new Inventario();

        Objeto o1=new Objeto("Espada",TipoObjeto.EQUIPABLE);

        inv.addObjeto(o1);

        assertEquals(o1,inv.getObjeto(0));
    }

    @Test
    void avanzarTurnoDebeIncrementarEquipados() {
        Inventario inv=new Inventario();

        Objeto equipado=new Objeto("Escudo",TipoObjeto.EQUIPABLE,0,0,0,0,1,"");
        equipado.setDuracionTurnos(1);

        inv.getObjetosEquipados().add(equipado);

        inv.avanzarTurno();

        assertTrue(equipado.getTurnosActivos()>=1);
    }

    @Test
    void inOrdenDevuelveObjetosOrdenados() {
        Inventario inv=new Inventario();

        Objeto espada=new Objeto("Zweihander",TipoObjeto.EQUIPABLE);
        Objeto arco=new Objeto("Arco",TipoObjeto.EQUIPABLE);
        Objeto escudo=new Objeto("Escudo",TipoObjeto.EQUIPABLE);

        inv.addObjeto(espada);
        inv.addObjeto(arco);
        inv.addObjeto(escudo);

        Lista<Objeto> ordenados=inv.inOrden();

        assertEquals(3,ordenados.getSize());
        assertEquals("Arco",ordenados.get(0).getNombre());
        assertEquals("Escudo",ordenados.get(1).getNombre());
        assertEquals("Zweihander",ordenados.get(2).getNombre());
    }

    @Test
    void inOrdenVacioDevuelveListaVacia() {
        Inventario inv=new Inventario();
        assertTrue(inv.inOrden().isEmpty());
    }
}