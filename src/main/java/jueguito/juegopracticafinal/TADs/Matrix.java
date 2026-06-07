package jueguito.juegopracticafinal.TADs;

public class Matrix<T> {
    private int numRows;
    private int numCols;
    private final Object[] rows;


    public Matrix(int numRows, int numCols) {
        if (numRows <= 0 || numCols <= 0) {
            throw new IllegalArgumentException("Dimensiones de la matriz no validas");
        }
        this.numRows = numRows;
        this.numCols = numCols;
        this.rows = new Object[numRows];

        for (int i = 0; i < numRows; i++) {
            Lista<T> row = new Lista<>();
            for (int j = 0; j < numCols; j++) {
                row.add(null);
            }
            rows[i] = row;
        }
    }

    public T get(int r, int c) {
        if(!esValida(r,c)){
            return null;
        }
        return ((Lista<T>) rows[r]).get(c);
    }

    public void set(int r, int c, T dato) {
        if(!esValida(r,c)){
            return;
        }
        ((Lista<T>) rows[r]).set(c, dato);
    }

    public Lista<T> getRow(int r) {
        if (r < 0 || r >= numRows) return null;
        return (Lista<T>) rows[r];
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
