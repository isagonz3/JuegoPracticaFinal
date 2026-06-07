package jueguito.juegopracticafinal.Modelo.Entidades;

import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Log.LogMovimiento;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JugadorTest {

    @Test
    void constructorDebeAsignarNombreEstadisticasYPosicion() {
        Estadisticas stats=new Estadisticas(10,5,3,2);
        Posicion pos=new Posicion(1,1);

        Jugador jugador=new Jugador("Heroe",stats,pos);

        assertEquals("Heroe",jugador.getNombre());
        assertEquals(stats,jugador.getEstadisticas());
        assertEquals(pos,jugador.getPosicion());
    }

    @Test
    void constructorDebeCrearInventarioYEquipamiento() {
        Jugador jugador=new Jugador("Heroe",
                new Estadisticas(1,1,1,1),
                new Posicion(0,0));

        assertNotNull(jugador.getInventario());
        assertNotNull(jugador.getEquipamiento());
        assertEquals(2,jugador.getEquipamiento().length);
    }

    @Test
    void constructorVacioNoDebeSerNull() {
        Jugador jugador=new Jugador();

        assertNotNull(jugador);
    }

    @Test
    void getTipoEntidadDebeDevolverJugador() {
        Jugador jugador=new Jugador("Heroe",new Estadisticas(1,1,1,1),new Posicion(0,0));

        assertEquals("JUGADOR",jugador.getTipoEntidad());
    }

    @Test
    void equiparDebeFuncionarCorrectamente() {
        Jugador jugador=new Jugador("jugador",new Estadisticas(10,5,2,1),new Posicion(0,0));

        Objeto espada=new Objeto("Espada",TipoObjeto.EQUIPABLE);
        espada.setSlot(SlotEquipable.ARMA);

        jugador.getInventario().addObjeto(espada);

        assertTrue(jugador.equipar(espada,SlotEquipable.ARMA));
        assertEquals(espada,jugador.getEquipamiento()[SlotEquipable.ARMA.ordinal()]);
    }

    @Test
    void equiparDebeFallarSiNoEsEquipable() {
        Jugador jugador=new Jugador("jugador",new Estadisticas(10,5,2,1), new Posicion(0,0));

        Objeto pocion=new Objeto("Pocion",TipoObjeto.USABLE);

        jugador.getInventario().addObjeto(pocion);

        assertFalse(jugador.equipar(pocion,SlotEquipable.ARMA));
    }

    @Test
    void equiparDebeFallarSiObjetoNoEstaEnInventario() {
        Jugador jugador=new Jugador("Heroe",new Estadisticas(10,5,2,1),new Posicion(0,0));

        Objeto espada=new Objeto("Espada",TipoObjeto.EQUIPABLE);

        assertFalse(jugador.equipar(espada,SlotEquipable.ARMA));
    }

    @Test
    void getAtaqueTotalDebeSumarBonusEquipamiento() {
        Estadisticas stats=new Estadisticas(10,5,2,1);

        Jugador jugador=new Jugador("jugador",stats,new Posicion(0,0));

        Objeto espada=new Objeto("Espada",TipoObjeto.EQUIPABLE,5,0,0,0,1,"");

        espada.setSlot(SlotEquipable.ARMA);

        jugador.getInventario().addObjeto(espada);

        jugador.equipar(espada,espada.getSlot());

        assertTrue(jugador.getAtaqueTotal()>=10);
    }

    @Test
    void getDefensaTotalDebeSumarBonusEquipamiento() {
        Estadisticas stats=new Estadisticas(10,5,2,1);

        Jugador jugador=new Jugador("jugador",stats,new Posicion(0,0));

        Objeto escudo=new Objeto("Escudo",TipoObjeto.EQUIPABLE,0,3,0,0,1,"");

        escudo.setSlot(SlotEquipable.ESCUDO);

        jugador.getInventario().addObjeto(escudo);

        jugador.equipar(escudo,escudo.getSlot());

        assertTrue(jugador.getDefensaTotal()>=5);
    }

    @Test
    void getRangoTotalDebeSumarBonusEquipamiento() {
        Estadisticas stats=new Estadisticas(10,5,2,1);

        Jugador jugador=new Jugador("jufador",stats,new Posicion(0,0));

        Objeto botas=new Objeto("Botas",TipoObjeto.EQUIPABLE,0,0,0,2,1,"");

        botas.setSlot(SlotEquipable.ARMA);

        jugador.getInventario().addObjeto(botas);

        jugador.equipar(botas,botas.getSlot());

        assertTrue(jugador.getRangoTotal()>=1);
    }

    @Test
    void setYGetLogDebeFuncionar() {
        Jugador jugador=new Jugador("jugador",new Estadisticas(1,1,1,1),new Posicion(0,0));

        LogMovimiento log=new LogMovimiento();

        jugador.setLog(log);

        assertEquals(log,jugador.getLog());
    }

    @Test
    void moverADebeCambiarPosicion() {
        Jugador jugador=new Jugador("heroe",new Estadisticas(10,5,3,2),new Posicion(1,1));

        jugador.moverA(new Posicion(3,4));

        assertEquals(3,jugador.getPosicion().getRow());
        assertEquals(4,jugador.getPosicion().getCol());
    }
}