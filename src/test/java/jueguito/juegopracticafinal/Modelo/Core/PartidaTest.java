package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.*;
import jueguito.juegopracticafinal.Modelo.Inventario.*;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;
import jueguito.juegopracticafinal.Modelo.NPC.Tradeo;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.Modelo.Turno.ResultadoCombate;
import jueguito.juegopracticafinal.TADs.Lista;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartidaTest {

    private Partida partida;
    private GrafoZonas grafo;

    @BeforeEach
    void setUp() {
        grafo = new GrafoZonas();
        int[] ids = {0, 1, 2, 4, 6, 7, 8};
        for (int id : ids) {
            Zona z = new Zona(id, "Zona" + id, 5, 5);
            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 5; j++)
                    z.setCelda(i, j, new Celda(TipoCelda.SUELO));
            grafo.addZona(z);
        }
        grafo.getZona(0).setSpawnJugador(new Posicion(1, 1));
        grafo.getZona(0).getCelda(1, 2).setTipoCelda(TipoCelda.PUERTA);
        grafo.getZona(0).getCelda(1, 2).setPuerta(new Puerta(0, 1, 2, 1, 2, 1));
        for (int i = 0; i < ids.length - 1; i++)
            grafo.conectar(ids[i], ids[i + 1]);
        partida = new Partida(grafo);
    }

    @Test
    void constructor() {
        assertNotNull(partida.getGrafoZonas());
        assertNull(partida.getJugador());
        assertEquals(EstadoJuego.EN_CURSO, partida.getEstadoActual());
    }

    @Test
    void iniciar() {
        partida.iniciar();
        assertNotNull(partida.getJugador());
        assertNotNull(partida.getGato());
        assertEquals(0, partida.getIdZonaActual());
        assertEquals("NoLink", partida.getJugador().getNombre());
        assertEquals(0, partida.getTurnoActual());
        assertFalse(partida.isGatoEncontrado());
    }

    @Test
    void iniciarSinSpawn() {
        GrafoZonas g2 = new GrafoZonas();
        for (int id : new int[]{0, 2, 4, 6, 7, 8}) {
            Zona z = new Zona(id, "Z" + id, 3, 3);
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    z.setCelda(i, j, new Celda(TipoCelda.SUELO));
            g2.addZona(z);
        }
        Partida p2 = new Partida(g2);
        p2.iniciar();
        assertNotNull(p2.getJugador());
    }

    @Test
    void iniciarTurno() {
        partida.iniciar();
        partida.getJugador().getEstadisticas().usarMovimiento(3);
        partida.iniciarTurno();
        assertEquals(partida.getJugador().getRangoTotal(),
                partida.getJugador().getEstadisticas().getPuntosMovDisponibles());
    }

    @Test
    void iniciarTurnoCuandoDerrotaNoHaceNada() {
        partida.iniciar();
        partida.setEstadoActual(EstadoJuego.DERROTA);
        int pmAntes = partida.getJugador().getEstadisticas().getPuntosMovDisponibles();
        partida.iniciarTurno();
        assertEquals(pmAntes, partida.getJugador().getEstadisticas().getPuntosMovDisponibles());
    }

    @Test
    void terminarTurno() {
        partida.iniciar();
        int t0 = partida.getTurnoActual();
        partida.terminarTurno();
        assertEquals(t0 + 1, partida.getTurnoActual());
    }

    @Test
    void moverJugador() {
        partida.iniciar();
        assertTrue(partida.moverJugador(1, 2));
        assertEquals(1, partida.getJugador().getPosicion().getRow());
        assertEquals(2, partida.getJugador().getPosicion().getCol());
    }

    @Test
    void moverJugadorACeldaNoTransitable() {
        partida.iniciar();
        partida.getZonaActual().getCelda(1, 2).setTipoCelda(TipoCelda.PARED);
        assertFalse(partida.moverJugador(1, 2));
    }

    @Test
    void moverJugadorSinPM() {
        partida.iniciar();
        partida.getJugador().getEstadisticas().setPuntosMovDisponibles(0);
        assertFalse(partida.moverJugador(1, 2));
    }

    @Test
    void moverJugadorACeldaOcupada() {
        partida.iniciar();
        Enemigo e = new Enemigo("E", new Estadisticas(10, 1, 1, 1));
        e.setPosicion(new Posicion(1, 2));
        partida.getZonaActual().getCelda(1, 2).setEntidad(e);
        assertFalse(partida.moverJugador(1, 2));
    }

    @Test
    void moverJugadorSinCamino() {
        partida.iniciar();
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                if (r != 1 || c != 1)
                    partida.getZonaActual().getCelda(r, c).setTipoCelda(TipoCelda.PARED);
        assertFalse(partida.moverJugador(1, 2));
    }

    @Test
    void moverJugadorCuandoEstadoNoEnCurso() {
        partida.iniciar();
        partida.setEstadoActual(EstadoJuego.VICTORIA);
        assertFalse(partida.moverJugador(1, 2));
    }

    @Test
    void getCeldasAccesibles() {
        partida.iniciar();
        Lista<Celda> celdas = partida.getCeldasAccesibles();
        assertNotNull(celdas);
        assertTrue(celdas.getSize() > 0);
    }

    @Test
    void getCeldasAccesiblesSinJugador() {
        Lista<Celda> celdas = partida.getCeldasAccesibles();
        assertTrue(celdas.isEmpty());
    }

    @Test
    void getCaminoMinimo() {
        partida.iniciar();
        Lista<Celda> camino = partida.getCaminoMinimo(1, 3);
        assertNotNull(camino);
        assertTrue(camino.getSize() > 0);
    }

    @Test
    void getCaminoMinimoSinJugador() {
        Lista<Celda> camino = partida.getCaminoMinimo(0, 0);
        assertTrue(camino.isEmpty());
    }

    @Test
    void atacarEnemigo() {
        partida.iniciar();
        Enemigo e = new Enemigo("Test", new Estadisticas(20, 5, 2, 3));
        e.setPosicion(new Posicion(2, 2));
        ResultadoCombate r = partida.atacarEnemigo(e);
        assertNotNull(r);
        assertTrue(r.getHit() >= 0);
    }

    @Test
    void atacarEnemigoMuerto() {
        partida.iniciar();
        Enemigo e = new Enemigo("Muerto", new Estadisticas(1, 1, 0, 1));
        e.recibirAtaque(100);
        ResultadoCombate r = partida.atacarEnemigo(e);
        assertEquals(0, r.getHit());
        assertFalse(r.isEnemigoKO());
    }

    @Test
    void atacarDireccion() {
        partida.iniciar();
        Posicion pj = partida.getJugador().getPosicion();
        Enemigo e = new Enemigo("E", new Estadisticas(10, 1, 1, 1));
        e.setPosicion(new Posicion(pj.getRow(), pj.getCol() + 1));
        partida.getZonaActual().getCelda(pj.getRow(), pj.getCol() + 1).setEntidad(e);
        assertTrue(partida.atacarDireccion(0, 1));
    }

    @Test
    void atacarDireccionSinEnemigo() {
        partida.iniciar();
        assertFalse(partida.atacarDireccion(0, 1));
    }

    @Test
    void atacarDireccionCuandoNoEnCurso() {
        partida.iniciar();
        partida.setEstadoActual(EstadoJuego.DERROTA);
        assertFalse(partida.atacarDireccion(0, 1));
    }

    @Test
    void findGato() {
        partida.iniciar();
        Gato gato = partida.getGato();
        gato.setPosicion(new Posicion(1, 0));
        partida.getZonaActual().getCelda(1, 0).setEntidad(gato);
        assertTrue(partida.findGato());
        assertTrue(partida.isGatoEncontrado());
    }

    @Test
    void findGatoYaEncontrado() {
        partida.iniciar();
        partida.setGatoEncontrado(true);
        assertTrue(partida.findGato());
    }


    @Test
    void usarObjeto() {
        partida.iniciar();
        Objeto pocion = new Objeto("Pocion", TipoObjeto.USABLE, 0, 0, 10, 0, 1, "Cura 10");
        partida.getJugador().getEstadisticas().recibirAtaque(15);
        partida.getJugador().getInventario().addObjeto(pocion);
        assertTrue(partida.usarObjeto(pocion));
        assertTrue(partida.getJugador().getEstadisticas().getVidaActual() > 5);
    }

    @Test
    void usarObjetoGastado() {
        partida.iniciar();
        Objeto pocion = new Objeto("Pocion", TipoObjeto.USABLE, 0, 0, 5, 0, 1, "");
        pocion.usarObjeto();
        partida.getJugador().getInventario().addObjeto(pocion);
        assertFalse(partida.usarObjeto(pocion));
    }

    @Test
    void usarObjetoAumentaAtaque() {
        partida.iniciar();
        Objeto p = new Objeto("PocionAtk", TipoObjeto.USABLE, 2, 0, 0, 0, 1, "");
        partida.getJugador().getInventario().addObjeto(p);
        int atkAntes = partida.getJugador().getEstadisticas().getAtaqueBase();
        partida.usarObjeto(p);
        assertEquals(atkAntes + 2, partida.getJugador().getEstadisticas().getAtaqueBase());
    }

    @Test
    void usarObjetoAumentaDefensa() {
        partida.iniciar();
        Objeto p = new Objeto("PocionDef", TipoObjeto.USABLE, 0, 1, 0, 0, 1, "");
        partida.getJugador().getInventario().addObjeto(p);
        int defAntes = partida.getJugador().getEstadisticas().getDefensaBase();
        partida.usarObjeto(p);
        assertEquals(defAntes + 1, partida.getJugador().getEstadisticas().getDefensaBase());
    }

    @Test
    void usarObjetoAumentaRango() {
        partida.iniciar();
        Objeto p = new Objeto("PocionRango", TipoObjeto.USABLE, 0, 0, 0, 3, 1, "");
        partida.getJugador().getInventario().addObjeto(p);
        int rangoAntes = partida.getJugador().getEstadisticas().getRangoMov();
        partida.usarObjeto(p);
        assertEquals(rangoAntes + 3, partida.getJugador().getEstadisticas().getRangoMov());
    }

    @Test
    void recogerObjeto() {
        partida.iniciar();
        Celda c = partida.getZonaActual().getCelda(1, 0);
        c.setObjeto(new Objeto("Manzana", TipoObjeto.USABLE));
        assertTrue(partida.recogerObjeto(c));
        assertFalse(c.tieneObjeto());
        assertTrue(partida.getJugador().getInventario().contiene(c.getObjeto()));
    }

    @Test
    void recogerObjetoSinObjeto() {
        partida.iniciar();
        Celda c = partida.getZonaActual().getCelda(1, 0);
        assertFalse(partida.recogerObjeto(c));
    }

    @Test
    void equiparObjeto() {
        partida.iniciar();
        Objeto espada = new Objeto("Espada", TipoObjeto.EQUIPABLE, 3, 0, 0, 0, 99, "");
        espada.setSlot(SlotEquipable.ARMA);
        partida.getJugador().getInventario().addObjeto(espada);
        assertTrue(partida.equiparObjeto(espada, SlotEquipable.ARMA));
        assertEquals(3, partida.getJugador().getBonusAtaqueEq());
    }

    @Test
    void interactNPC() {
        partida.iniciar();
        NPC npc = new NPC("Aldeano", TipoNPC.ALDEANO);
        npc.addDialogo("Hola!");
        Posicion pj = partida.getJugador().getPosicion();
        partida.getZonaActual().getCelda(pj.getRow() + 1, pj.getCol()).setNpc(npc);
        assertNotNull(partida.interactNPC());
    }

    @Test
    void reset_iniciarTurno(){
        partida.iniciar();
        partida.iniciarTurno();
        assertFalse(partida.isMovimientoRealizado());
        assertFalse(partida.isAccionRealizada());
    }

    @Test
    void reset_terminarTurno(){
        partida.iniciar();
        partida.iniciarTurno();
        partida.moverJugador(1,2);
        partida.terminarTurno();
        assertFalse(partida.isMovimientoRealizado());
        assertFalse(partida.isAccionRealizada());
    }

    @Test
    void reset_AccionCambioZona() {
        partida.iniciar();
        partida.iniciarTurno();
        partida.setMovimientoRealizado(true);
        partida.setAccionRealizada(true);
        partida.accionCambioZona();
        assertFalse(partida.isMovimientoRealizado());
        assertFalse(partida.isAccionRealizada());
    }

    @Test
    void activar_moverJugador() {
        partida.iniciar();
        partida.iniciarTurno();
        assertTrue(partida.moverJugador(1,2));
        assertTrue(partida.isMovimientoRealizado());
    }

    @Test
    void activar_atacarDireccion() {
        partida.iniciar();
        partida.iniciarTurno();
        Posicion pj = partida.getJugador().getPosicion();
        Enemigo e = new Enemigo("Paco", new Estadisticas(10,1, 1, 1));
        e.setPosicion(new Posicion(pj.getRow(), pj.getCol() +1));
        partida.getZonaActual().getCelda(pj.getRow(), pj.getCol() +1).setEntidad(e);
        partida.moverJugador(0,1);
        assertTrue(partida.isAccionRealizada());
    }

}
