package jueguito.juegopracticafinal.TADs;


public class ListaCircular<T> {
    private ElementoSimpleEnlazada<T> first;
    private ElementoSimpleEnlazada<T> last;
    private int size;

    public ListaCircular() {
        first = last = null;
        size = 0;
    }

    public void addFirst(T data) {
        ElementoSimpleEnlazada<T> nuevo = new ElementoSimpleEnlazada<>(data);
        if (first == null) {
            first = last = nuevo;
            last.setNext(first); //Cierra la lista circular
        }
        else {
            nuevo.setNext(first);
            first = nuevo;
            last.setNext(first); //Cierra la lista, apuntando al nuevo primer elemento
        }
        size++;
    }

    public void addLast(T data) {
        if (first == null) {
            addFirst(data);
        }
        else {
            ElementoSimpleEnlazada<T> nuevo = new ElementoSimpleEnlazada<>(data);
            last.setNext(nuevo);
            last = nuevo;
            last.setNext(first); //Cierra la lista circular
            size++;
        }
    }

    public T deleteFirst() {
        if(first == null) {
            return null;
        }

        T data = first.getData();
        if (first == last) {
            first = last = null;
        }
        else {
            first = first.getNext();
            last.setNext(first);
        }
        size--;
        return data;
    }

    public T deleteLast() {
        if(last == null) {
            return null;
        }

        T data = last.getData();
        if (last == first) {
            first = last = null;
        }
        else {
            ElementoSimpleEnlazada<T> aux = first;
            while (aux.getNext() != last) {
                aux = aux.getNext();
            }
            aux.setNext(first);
            last = aux; //Penultimo nodo pasa a ser el último nodo
        }
        size--;
        return data;
    }

    public boolean contains(T data) {
        if (first == null) {
            return false;
        }
        ElementoSimpleEnlazada<T> aux = first;
        for(int i = 0; i < size; i++) {
            if (aux.getData().equals(data)) {
                return true;
            }
            aux = aux.getNext();
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return first == null;
    }
}
