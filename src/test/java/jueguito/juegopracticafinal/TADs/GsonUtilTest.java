package jueguito.juegopracticafinal.TADs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GsonUtilTest {

    @TempDir
    File tempDir;

    static class TestData {
        String name;
        int value;
        TestData() {}
        TestData(String name, int value) { this.name = name; this.value = value; }
    }

    @Test
    void guardarYCargarObjetoRoundTrip() {
        File f = new File(tempDir, "obj.json");
        TestData obj = new TestData("hello", 42);

        GsonUtil.guardarObjetoEnArchivo(f.getAbsolutePath(), obj);
        TestData loaded = GsonUtil.cargarObjetoDesdeArchivo(f.getAbsolutePath(), TestData.class);

        assertNotNull(loaded);
        assertEquals("hello", loaded.name);
        assertEquals(42, loaded.value);
    }

    @Test
    void guardarYCargarObjetoNullFields() {
        File f = new File(tempDir, "null.json");
        GsonUtil.guardarObjetoEnArchivo(f.getAbsolutePath(), new TestData(null, 0));
        TestData loaded = GsonUtil.cargarObjetoDesdeArchivo(f.getAbsolutePath(), TestData.class);

        assertNotNull(loaded);
        assertNull(loaded.name);
    }

    @Test
    void guardarYCargarArrayRoundTrip() {
        File f = new File(tempDir, "arr.json");
        TestData[] arr = {new TestData("a", 1), new TestData("b", 2)};

        GsonUtil.guardarArray(f.getAbsolutePath(), arr);
        TestData[] loaded = GsonUtil.cargarArray(f.getAbsolutePath(), TestData[].class);

        assertNotNull(loaded);
        assertEquals(2, loaded.length);
        assertEquals("a", loaded[0].name);
        assertEquals(2, loaded[1].value);
    }

    @Test
    void guardarYCargarArrayVacio() {
        File f = new File(tempDir, "empty.json");
        TestData[] arr = {};

        GsonUtil.guardarArray(f.getAbsolutePath(), arr);
        TestData[] loaded = GsonUtil.cargarArray(f.getAbsolutePath(), TestData[].class);

        assertNotNull(loaded);
        assertEquals(0, loaded.length);
    }

    @Test
    void cargarObjetoArchivoInexistenteLanzaExcepcion() {
        assertThrows(RuntimeException.class,
                () -> GsonUtil.cargarObjetoDesdeArchivo("/ruta/inexistente.json", TestData.class));
    }

    @Test
    void cargarArrayArchivoInexistenteLanzaExcepcion() {
        assertThrows(RuntimeException.class,
                () -> GsonUtil.cargarArray("/ruta/inexistente.json", TestData[].class));
    }

    @Test
    void existeArchivoDevuelveTrueSiExiste() {
        File f = new File(tempDir, "exists.json");
        GsonUtil.guardarObjetoEnArchivo(f.getAbsolutePath(), new TestData("x", 1));

        assertTrue(GsonUtil.existeArchivo(f.getAbsolutePath()));
    }

    @Test
    void existeArchivoDevuelveFalseSiNoExiste() {
        assertFalse(GsonUtil.existeArchivo("/ruta/inexistente.json"));
    }

    @Test
    void eliminarFicheroBorraYExisteDevuelveFalse() {
        File f = new File(tempDir, "del.json");
        GsonUtil.guardarObjetoEnArchivo(f.getAbsolutePath(), new TestData("x", 1));
        assertTrue(f.exists());

        boolean deleted = GsonUtil.eliminarFichero(f.getAbsolutePath());
        assertTrue(deleted);
        assertFalse(f.exists());
    }
}
