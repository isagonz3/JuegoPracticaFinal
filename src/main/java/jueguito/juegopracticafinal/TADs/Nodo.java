package jueguito.juegopracticafinal.TADs;

public class Nodo {

    private final int id;
    private final String nombre;
    private final int rows;
    private final int cols;
    private final Lista<Integer> adyacentes;

    public Nodo(int id, String nombre, int rows, int cols) {
        this.id = id;
        this.nombre = nombre;
        this.rows = rows;
        this.cols = cols;
        this.adyacentes = new Lista<>();
    }

    public void addAdyacente(int idMatrix) {
        if(!adyacentes.contains(idMatrix)) {
            adyacentes.add(idMatrix);
        }
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Lista<Integer> getAdyacentes() {
        return adyacentes;
    }

}
