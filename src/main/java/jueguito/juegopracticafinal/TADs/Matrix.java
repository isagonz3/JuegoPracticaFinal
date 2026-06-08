package jueguito.juegopracticafinal.TADs;

public class Matrix<T> {
    private int numRows;
    private int numCols;
    private final Lista<Lista<T>> data;

    public Matrix(int numRows, int numCols) {
        if (numRows <= 0 || numCols <= 0) {
            throw new IllegalArgumentException("Dimensiones de la matriz no validas");
        }
        this.numRows = numRows;
        this.numCols = numCols;
        this.data = new Lista<>();

        for (int i = 0; i < numRows; i++) {
            Lista<T> row = new Lista<>();
            for (int j = 0; j < numCols; j++) {
                row.add(null);
            }
            data.add(row);
        }
    }

    public T get(int r, int c) {
        if (!esValida(r, c)) {
            return null;
        }
        return data.get(r).get(c);
    }

    public void set(int r, int c, T dato) {
        if (!esValida(r, c)) {
            return;
        }
        data.get(r).set(c, dato);
    }

    public Lista<T> getRow(int r) {
        if (r < 0 || r >= numRows) return null;
        return data.get(r);
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public boolean esValida(int fila, int col) {
        return fila >= 0 && fila < numRows && col >= 0 && col < numCols;
    }
}