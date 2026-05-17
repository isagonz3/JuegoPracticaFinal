package jueguito.juegopracticafinal.TADs;


public class ElementoDobleEnlazada<T> {
    T elemento;
    ElementoDobleEnlazada<T> next;
    ElementoDobleEnlazada<T> before;

    public ElementoDobleEnlazada(T elemento) {
        this.elemento = elemento;
        this.next = null;
        this.before = null;
    }
}


