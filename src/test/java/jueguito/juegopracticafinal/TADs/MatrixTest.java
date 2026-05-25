package jueguito.juegopracticafinal.TADs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    @Test
    void get() {
        Matrix<Integer> matrix = new Matrix<>(2, 2);
        assertNull(matrix.get(0, 0));
        assertNull(matrix.get(-1, 0));
        assertNull(matrix.get(5, 5));
    }

    @Test
    void set() {
        Matrix<String> matrix = new Matrix<>(2, 2);
        matrix.set(0, 1, "X");
        assertEquals("X", matrix.get(0, 1));
        assertNull(matrix.get(1, 1));
    }

    @Test
    void getNumRows() {
        Matrix<Object> matrix = new Matrix<>(5, 7);
        assertEquals(5, matrix.getNumRows());
    }

    @Test
    void getNumCols() {
        Matrix<Object> m = new Matrix<>(5, 7);
        assertEquals(7, m.getNumCols());
    }

    @Test
    void esValida() {
        Matrix<String> matrix = new Matrix<>(3, 4);
        assertTrue(matrix.esValida(0, 0));
        assertTrue(matrix.esValida(2, 3));
        assertFalse(matrix.esValida(-1, 0));
        assertFalse(matrix.esValida(0, 4));
        assertFalse(matrix.esValida(3, 0));
    }

    @Test void constructorCreaMatriz() {
        Matrix<String> matrix = new Matrix<>(2, 3);
        assertEquals(2, matrix.getNumRows());
        assertEquals(3, matrix.getNumCols());
    }

    @Test void constructorDimensionInvalidaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(5, -1));
    }
}