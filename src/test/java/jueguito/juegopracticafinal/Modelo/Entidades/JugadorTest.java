package jueguito.juegopracticafinal.Modelo.Entidades;

import jueguito.juegopracticafinal.Modelo.Inventario.*;
import jueguito.juegopracticafinal.Modelo.Log.LogMovimiento;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JugadorTest {

    private Jugador j;

    @BeforeEach
    void setUp() {
        j = new Jugador("Link", new Estadisticas(100, 15, 10, 5), new Posicion(2, 3));
    }

    @Test void constructor() {
        assertEquals("Link", j.getNombre());
        assertEquals(15, j.getAtaqueTotal());
        assertEquals(10, j.getDefensaTotal());
        assertEquals(5, j.getRangoTotal());
        assertEquals(0, j.getBonusAtaqueEq());
        assertEquals(0, j.getBonusDefensaEq());
        assertEquals(0, j.getBonusRangoEq());
        assertNotNull(j.getInventario());
        assertTrue(j.getInventario().inventarioVacio());
        assertEquals("JUGADOR", j.getTipoEntidad());
    }

    @Test void constructorVacio() {
        Jugador jv = new Jugador();
        assertNull(jv.getNombre());
    }

    @Test void gettersSetters() {
        j.setNombre("Zelda"); assertEquals("Zelda", j.getNombre());
        assertNotNull(j.getEstadisticas());
        j.setEstadisticas(new Estadisticas(50, 5, 3, 2));
        assertEquals(5, j.getAtaqueTotal());
        Posicion p = new Posicion(0, 0);
        j.setPosicion(p); assertEquals(p, j.getPosicion());
    }

    @Test void estarVivo() {
        assertTrue(j.estarVivo());
        j.recibirAtaque(200);
        assertFalse(j.estarVivo());
    }

    @Test void moverA() {
        Posicion nueva = new Posicion(5, 6);
        assertTrue(j.moverA(nueva));
        assertEquals(nueva, j.getPosicion());
        assertFalse(j.moverA(null));
    }

    @Test void getAtaqueTotalYDefensaTotal() {
        assertEquals(15, j.getAtaqueTotal());
        assertEquals(10, j.getDefensaTotal());
    }

    @Test void equiparSumaBonos() {
        Objeto espada = new Objeto("Espada", TipoObjeto.EQUIPABLE, 5, 0, 0, 2, 99, "");
        espada.setSlot(SlotEquipable.ARMA);
        j.getInventario().addObjeto(espada);
        assertTrue(j.equipar(espada, SlotEquipable.ARMA));
        assertEquals(20, j.getAtaqueTotal());
        assertEquals(10, j.getDefensaTotal());
        assertEquals(7, j.getRangoTotal());
        assertEquals(5, j.getBonusAtaqueEq());
        assertEquals(0, j.getBonusDefensaEq());
        assertEquals(2, j.getBonusRangoEq());
    }

    @Test void equiparReemplazaDevuelveAlInventario() {
        Objeto espada1 = new Objeto("Espada1", TipoObjeto.EQUIPABLE, 3, 0, 0, 0, 99, "");
        Objeto espada2 = new Objeto("Espada2", TipoObjeto.EQUIPABLE, 5, 0, 0, 0, 99, "");
        espada1.setSlot(SlotEquipable.ARMA);
        espada2.setSlot(SlotEquipable.ARMA);
        j.getInventario().addObjeto(espada1);
        j.equipar(espada1, SlotEquipable.ARMA);
        assertEquals(18, j.getAtaqueTotal());
        j.getInventario().addObjeto(espada2);
        j.equipar(espada2, SlotEquipable.ARMA);
        assertEquals(20, j.getAtaqueTotal());
        assertNotNull(j.getInventario().findObjeto("Espada1"));
    }

    @Test void equiparNoEquipableFalla() {
        Objeto pocion = new Objeto("Pocion", TipoObjeto.USABLE);
        j.getInventario().addObjeto(pocion);
        assertFalse(j.equipar(pocion, SlotEquipable.ARMA));
    }

    @Test void quitarEquipamiento() {
        Objeto espada = new Objeto("Espada", TipoObjeto.EQUIPABLE, 3, 0, 0, 0, 99, "");
        espada.setSlot(SlotEquipable.ARMA);
        j.getInventario().addObjeto(espada);
        j.equipar(espada, SlotEquipable.ARMA);
        assertEquals(18, j.getAtaqueTotal());
        j.quitarEquipamiento(SlotEquipable.ARMA);
        assertEquals(15, j.getAtaqueTotal());
        assertEquals(0, j.getBonusAtaqueEq());
        assertFalse(j.getInventario().inventarioVacio());
    }

    @Test void getEquipamiento() {
        Objeto[] eq = j.getEquipamiento();
        assertEquals(2, eq.length);
        assertNull(j.getEquipamiento(SlotEquipable.ARMA));
        assertNull(j.getEquipamiento(SlotEquipable.ESCUDO));
    }

    @Test void log() {
        LogMovimiento log = new LogMovimiento();
        j.setLog(log);
        assertEquals(log, j.getLog());
        log.registrar("test");
        assertEquals("test", j.getLog().getUltimaEntrada());
    }

    @Test void getTipoEntidadJugador() {
        assertEquals("JUGADOR", j.getTipoEntidad());
    }

    @Test void getInventario() {
        assertNotNull(j.getInventario());
        assertTrue(j.getInventario().inventarioVacio());
    }
}
