package jueguito.juegopracticafinal.TADs;

public class ElementoSimpleEnlazada<T> {

    //Atributos
    private T data;
    private ElementoSimpleEnlazada<T> next;

    //Constructor
    public ElementoSimpleEnlazada(T data) {
        this.data = data; //Guarda el dato
        this.next = null; //Si se crea un nuevo nodo no hay siguiente nodo
    }

    //Metodos
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ElementoSimpleEnlazada<T> getNext() {
        return next;
    }

    public void setNext(ElementoSimpleEnlazada<T> next) {
        this.next = next;
    }
}
