package jueguito.juegopracticafinal.TADs;

public class NodoArbol<T> {
    T elemento;
    NodoArbol<T> izq;
    NodoArbol<T> der;

    public NodoArbol(T elemento) {
        this.elemento = elemento;
    }
}
