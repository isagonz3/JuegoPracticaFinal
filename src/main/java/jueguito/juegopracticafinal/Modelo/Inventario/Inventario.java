package jueguito.juegopracticafinal.Modelo.Inventario;

import jueguito.juegopracticafinal.TADs.Lista;

public class Inventario {

    private Lista<Objeto> objetos;
    private int sizeInventario;

    public Inventario() {
        this.objetos = new Lista<>();
        this.sizeInventario = 10;
    }

    public Inventario(int size) {
        this.objetos = new Lista<>();
        this.sizeInventario = size;
    }

    // =========================
    // AÑADIR OBJETO (SIN STACK)
    // =========================
    public boolean addObjeto(Objeto objeto) {

        if (objetos.getSize() >= sizeInventario) {
            return false;
        }

        objetos.add(objeto);
        return true;
    }

    // =========================
    // ELIMINAR OBJETO
    // =========================
    public boolean removeObjeto(Objeto objeto) {
        return objetos.delete(objeto) != null;
    }

    // =========================
    // CONTIENE OBJETO
    // =========================
    public boolean containsObjeto(String nombre) {

        for (int i = 0; i < objetos.getSize(); i++) {

            if (objetos.get(i).getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }

        return false;
    }

    // =========================
    // GET POR ÍNDICE
    // =========================
    public Objeto getObjeto(int index) {
        return objetos.get(index);
    }

    // =========================
    // BUSCAR OBJETO
    // =========================
    public Objeto findObjeto(String nombre) {

        for (int i = 0; i < objetos.getSize(); i++) {

            Objeto o = objetos.get(i);

            if (o.getNombre().equalsIgnoreCase(nombre)) {
                return o;
            }
        }

        return null;
    }

    // =========================
    // SIZE
    // =========================
    public int size() {
        return objetos.getSize();
    }

    public boolean inventarioLleno() {
        return objetos.getSize() >= sizeInventario;
    }

    public boolean inventarioVacio() {
        return objetos.isEmpty();
    }

    public Lista<Objeto> getObjetos() {
        return objetos;
    }

    public void setSizeInventario(int size) {
        this.sizeInventario = size;
    }

    public int getSizeInventario() {
        return sizeInventario;
    }

    // =========================
    // CONTAR OBJETOS REALES
    // =========================
    public int contarObjetosReales() {
        return objetos.getSize();
    }
}
