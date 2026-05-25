package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
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
            new Objeto("Pocion", TipoObjeto.CONSUMIBLE, 0, 0, 5, 0, 1, "Cura")
        );
        LoadJSON.guardarPartida(partida, savePath.toString());
        Partida cargada = LoadJSON.cargarPartida(savePath.toString(), grafo);
        assertNotNull(cargada);
        assertTrue(cargada.getJugador().getInventario().containsObjeto("Pocion"));
    }

    @Test void guardarConLogs(@TempDir Path tempDir) {
        Path savePath = tempDir.resolve("save3.json");
        partida.getLog().registrar("Entrada de prueba");
        partida.getLog().registrar("Otra entrada");
        LoadJSON.guardarPartida(partida, savePath.toString());
        Partida cargada = LoadJSON.cargarPartida(savePath.toString(), grafo);
        assertNotNull(cargada);
        assertEquals("Otra entrada", cargada.getLog().getUltimaEntrada());
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
}
