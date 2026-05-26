package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartidaObjetosYCombateTest {

    private Partida partida;
    private PartidaObjetosYCombate combate;

    @BeforeEach
    void setUp() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z = new Zona(0, "Test", 5, 5);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                z.setCelda(i, j, new Celda(TipoCelda.SUELO));
        grafo.addZona(z);
        grafo.getZona(0).setSpawnJugador(new Posicion(1, 1));
        partida = new Partida(grafo);
        partida.iniciar();
        combate = new PartidaObjetosYCombate(partida);
    }

    @Test void calcularHit_devuelveNumeroPositivo() {
        int hit = PartidaObjetosYCombate.calcularHit(10, 5);
        assertTrue(hit >= 0);
    }

    @Test void calcularHit_defensaSuperiorAtaque() {
        int hit = PartidaObjetosYCombate.calcularHit(3, 20);
        assertEquals(0, hit);
    }

    @Test void calcularHit_rangoEstadistico() {
        for (int i = 0; i < 100; i++) {
            int hit = PartidaObjetosYCombate.calcularHit(10, 3);
            assertTrue(hit >= 0, "Daño no negativo");
            assertTrue(hit <= 17, "Daño max " + hit);
        }
    }

    @Test void atacarDireccion_conEnemigoAdyacente() {
        Posicion pj = partida.getJugador().getPosicion();
        Enemigo e = new Enemigo("E", new Estadisticas(10, 1, 1, 1));
        e.setPosicion(new Posicion(pj.getRow(), pj.getCol() + 1));
        partida.getZonaActual().getCelda(pj.getRow(), pj.getCol() + 1).setEntidad(e);
        assertTrue(combate.atacarDireccion(0, 1));
    }

    @Test void atacarDireccion_sinEnemigo_noHaceNada() {
        assertFalse(combate.atacarDireccion(0, 1));
    }

    @Test void atacarDireccion_cuandoEstadoNoEnCurso_noAtaca() {
        partida.setEstadoActual(EstadoJuego.DERROTA);
        assertFalse(combate.atacarDireccion(0, 1));
    }

    @Test void atacarDireccion_bloqueaAccionRepetida() {
        partida.iniciarTurno();
        Posicion pj = partida.getJugador().getPosicion();
        Enemigo e = new Enemigo("E", new Estadisticas(10, 1, 1, 1));
        e.setPosicion(new Posicion(pj.getRow(), pj.getCol() + 1));
        partida.getZonaActual().getCelda(pj.getRow(), pj.getCol() + 1).setEntidad(e);
        assertTrue(combate.atacarDireccion(0, 1));
        assertFalse(combate.atacarDireccion(0, 1));
    }

    @Test void atacarJugador_sinEnemigosAdyacentes_noFalla() {
        combate.atacarJugador();
    }

    @Test void recogerObjeto() {
        Celda c = partida.getZonaActual().getCelda(1, 0);
        c.setObjeto(new Objeto("Gema", TipoObjeto.NPC));
        assertTrue(combate.recogerObjeto(c));
        assertFalse(c.tieneObjeto());
    }

    @Test void recogerObjetoSinObjeto() {
        Celda c = partida.getZonaActual().getCelda(1, 0);
        assertFalse(combate.recogerObjeto(c));
    }

    @Test void usarObjeto_curaVida() {
        Objeto pocion = new Objeto("Pocion", TipoObjeto.USABLE, 0, 0, 10, 0, 1, "");
        partida.getJugador().getEstadisticas().recibirAtaque(8);
        partida.getJugador().getInventario().addObjeto(pocion);
        assertTrue(combate.usarObjeto(pocion));
    }

    @Test void equiparObjeto_arma() {
        Objeto espada = new Objeto("Espada", TipoObjeto.EQUIPABLE, 3, 0, 0, 0, 99, "");
        espada.setSlot(SlotEquipable.ARMA);
        partida.getJugador().getInventario().addObjeto(espada);
        assertTrue(combate.equiparObjeto(espada, SlotEquipable.ARMA));
    }

}