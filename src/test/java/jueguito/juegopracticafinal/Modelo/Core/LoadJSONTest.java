package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;

class LoadJSONTest {

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
        for (int i = 0; i < ids.length - 1; i++)
            grafo.conectar(ids[i], ids[i + 1]);
        partida = new Partida(grafo);
        partida.iniciar();
    }

    @Test void guardarYCargarPartida(@TempDir Path tempDir) {
        Path savePath = tempDir.resolve("save.json");
        String ruta = savePath.toString();

        assertDoesNotThrow(() -> LoadJSON.guardarPartida(partida, ruta));
        assertTrue(savePath.toFile().exists());

        Partida cargada = LoadJSON.cargarPartida(ruta, grafo);
        assertNotNull(cargada);
        assertEquals(partida.getJugador().getNombre(), cargada.getJugador().getNombre());
        assertEquals(partida.getIdZonaActual(), cargada.getIdZonaActual());
        assertEquals(partida.getTurnoActual(), cargada.getTurnoActual());
        assertEquals(partida.isGatoEncontrado(), cargada.isGatoEncontrado());
    }

    @Test void guardarYCargarConInventario(@TempDir Path tempDir) {
        Path savePath = tempDir.resolve("save2.json");
        partida.getJugador().getInventario().addObjeto(
            new Objeto("Pocion", TipoObjeto.USABLE, 0, 0, 5, 0, 1, "Cura")
        );
        LoadJSON.guardarPartida(partida, savePath.toString());
        Partida cargada = LoadJSON.cargarPartida(savePath.toString(), grafo);
        assertNotNull(cargada);
        assertTrue(cargada.getJugador().getInventario().size() > 0);
    }


    @Test void cargarPartidaRutaInvalida() {
        Partida p = LoadJSON.cargarPartida("ruta/inexistente.json", grafo);
        assertNull(p);
    }

    @Test void cargarPartidaGrafoSinZona(@TempDir Path tempDir) {
        GrafoZonas grafoVacio = new GrafoZonas();
        Path savePath = tempDir.resolve("save_err.json");
        LoadJSON.guardarPartida(partida, savePath.toString());
        Partida p = LoadJSON.cargarPartida(savePath.toString(), grafoVacio);
        assertNull(p);
    }

    @Test void guardarYCargar_conZonasVisitadas(@TempDir Path tempDir) {
        Path savePath = tempDir.resolve("save_visit.json");
        partida.getZonaActual().setVisitada(true);
        LoadJSON.guardarPartida(partida, savePath.toString());
        Partida cargada = LoadJSON.cargarPartida(savePath.toString(), grafo);
        assertNotNull(cargada);
        assertTrue(cargada.getZonaActual().isVisitada());
    }

    @Test void guardarYCargar_conEnemigos(@TempDir Path tempDir) {
        Path savePath = tempDir.resolve("save_enemy.json");
        Posicion pj = partida.getJugador().getPosicion();
        Enemigo e = new Enemigo("Chungo", new Estadisticas(20, 5, 2, 4));
        e.setPosicion(new Posicion(2, 2));
        partida.getZonaActual().getCelda(2, 2).setEntidad(e);
        LoadJSON.guardarPartida(partida, savePath.toString());
        Partida cargada = LoadJSON.cargarPartida(savePath.toString(), grafo);
        assertNotNull(cargada);
        Celda celda = cargada.getZonaActual().getCelda(2, 2);
        assertTrue(celda.tieneEntidad());
        assertInstanceOf(Enemigo.class, celda.getEntidad());
    }

    @Test void guardarYCargar_conObjetoConBonus(@TempDir Path tempDir) {
        Path savePath = tempDir.resolve("save_bonus.json");
        Objeto espada = new Objeto("Espada+", TipoObjeto.EQUIPABLE, 5, 2, 0, 0, 99, "");
        espada.setSlot(SlotEquipable.ARMA);
        partida.getJugador().getInventario().addObjeto(espada);
        LoadJSON.guardarPartida(partida, savePath.toString());
        Partida cargada = LoadJSON.cargarPartida(savePath.toString(), grafo);
        assertNotNull(cargada);
        Objeto cargado = cargada.getJugador().getInventario().findObjeto("Espada+");
        assertNotNull(cargado);
        assertEquals(5, cargado.getAtaqueBonus());
        assertEquals(2, cargado.getDefensaBonus());
    }
}
