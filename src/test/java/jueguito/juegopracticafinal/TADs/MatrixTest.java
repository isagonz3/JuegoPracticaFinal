package jueguito.juegopracticafinal.TADs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MatrixTest {

    @Test
    void constructorCreaMatrizConDimensionesCorrectas() {
        Matrix<String> m = new Matrix<>(3, 4);
        assertEquals(3, m.getNumRows());
        assertEquals(4, m.getNumCols());
    }

    @Test
    void constructorDimensionesInvalidasLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(5, 0));
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(-1, 5));
    }

    @Test
    void getDevuelveNullInicialmente() {
        Matrix<String> m = new Matrix<>(2, 2);
        assertNull(m.get(0, 0));
        assertNull(m.get(1, 1));
    }

    @Test
    void setYGetAlmacenanYRecuperan() {
        Matrix<String> m = new Matrix<>(2, 2);
        m.set(0, 0, "A");
        m.set(1, 1, "B");
        assertEquals("A", m.get(0, 0));
        assertEquals("B", m.get(1, 1));
        assertNull(m.get(0, 1));
    }

    @Test
    void getCoordenadasInvalidasDevuelveNull() {
        Matrix<String> m = new Matrix<>(2, 2);
        assertNull(m.get(-1, 0));
        assertNull(m.get(0, -1));
        assertNull(m.get(5, 0));
        assertNull(m.get(0, 5));
    }

    @Test
    void setCoordenadasInvalidasNoLanzaExcepcion() {
        Matrix<String> m = new Matrix<>(2, 2);
        assertDoesNotThrow(() -> m.set(-1, 0, "X"));
        assertDoesNotThrow(() -> m.set(5, 5, "X"));
    }

    @Test
    void esValidaDevuelveTrueParaCoordenadasValidas() {
        Matrix<String> m = new Matrix<>(3, 4);
        assertTrue(m.esValida(0, 0));
        assertTrue(m.esValida(2, 3));
    }

    @Test
    void esValidaDevuelveFalseParaCoordenadasInvalidas() {
        Matrix<String> m = new Matrix<>(3, 4);
        assertFalse(m.esValida(-1, 0));
        assertFalse(m.esValida(0, -1));
        assertFalse(m.esValida(3, 0));
        assertFalse(m.esValida(0, 4));
    }

    @Test
    void getRowDevuelveListaDeLaFila() {
        Matrix<String> m = new Matrix<>(2, 3);
        m.set(0, 0, "A");
        m.set(0, 1, "B");
        m.set(0, 2, "C");
        Lista<String> row = m.getRow(0);
        assertNotNull(row);
        assertEquals(3, row.getSize());
        assertEquals("A", row.get(0));
        assertEquals("B", row.get(1));
        assertEquals("C", row.get(2));
    }

    @Test
    void getRowIndiceInvalidoDevuelveNull() {
        Matrix<String> m = new Matrix<>(2, 2);
        assertNull(m.getRow(-1));
        assertNull(m.getRow(5));
    }
}
