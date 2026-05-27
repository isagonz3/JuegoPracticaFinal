package jueguito.juegopracticafinal.TADs;


public class ElementoDobleEnlazada<T> {
    protected T elemento;
    protected ElementoDobleEnlazada<T> next;
    protected ElementoDobleEnlazada<T> before;

    public ElementoDobleEnlazada(T data) {
        this.elemento = data;
        this.next = null;
        this.before = null;
    }

    public T getElemento() {
        return elemento;
    }

    public void setElemento(T elemento) {
        this.elemento = elemento;
    }

    public ElementoDobleEnlazada<T> getNext() {
        return next;
    }

    public void setNext(ElementoDobleEnlazada<T> next) {
        this.next = next;
    }

    public ElementoDobleEnlazada<T> getBefore() {
        return before;
    }

    public void setBefore(ElementoDobleEnlazada<T> before) {
        this.before = before;
    }
}


