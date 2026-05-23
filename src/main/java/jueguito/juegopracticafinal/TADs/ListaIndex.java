package jueguito.juegopracticafinal.TADs;

public class ListaIndex<T extends Comparable<T>> {
    private ElementoDobleEnlazada<T>first;
    private ElementoDobleEnlazada<T> last;

    int size = 0;

    public T get(T elemento){
        if(first == null){
            return null;
        }
        ElementoDobleEnlazada<T> aux = first;
        while(aux != null) {
            if (aux.elemento.compareTo(elemento) == 0) {
                return aux.elemento;
            }
            aux = aux.next;
        }
        return null;
    }

    public void addFirst(T elemento) {
        ElementoDobleEnlazada<T> nuevo = new ElementoDobleEnlazada<>(elemento);
        if (first == null){
            first = last = nuevo;
            first.next = first.before = first;
        }
        else{
            nuevo.next = first;
            nuevo.before = last;
            first.before = last.next = nuevo;
            first = nuevo;
        }
        size++;
    }

    public void addLast(T elemento) {
        if (last == null){
            addFirst(elemento);
        }
        else{
            ElementoDobleEnlazada<T> nuevo = new ElementoDobleEnlazada<>(elemento);
            nuevo.next = first;
            nuevo.before = last;
            first.before = last.next = nuevo;
            last = nuevo;
            size++;
        }
    }

    public T addAt(T elemento,int position) {
        ElementoDobleEnlazada<T> actual;
        if (position < 0 || position > size ) {
            System.out.println("Operación inválida, no puedes añadir nada en esta posición.");
            return null;
        }

        if (position == 0){
            addFirst(elemento);
            return elemento;
        }

        if (position == size){
            addLast(elemento);
            return elemento;
        }

        ElementoDobleEnlazada<T> nuevo = new ElementoDobleEnlazada<>(elemento);
        actual = first;
        for (int i = 0; i < position; i++){
            actual = actual.next;
        }
        nuevo.next = actual;
        nuevo.before = actual.before;
        actual.before.next = nuevo;
        actual.before = nuevo;
        size++;

        return elemento;
    }

    public T deleteFirst() {
        if  (first == null){
            System.out.println("La lista está vacía");
            return null;
        }

        ElementoDobleEnlazada<T> deleted = first;
        if (size == 1){
            first = last = null;
        }
        else{
            first = first.next;
            first.before = last;
            last.next = first;
        }
        size--;
        return deleted.elemento;
    }

    public T deleteLast() {
        if  (first == null){
            System.out.println("La lista está vacía");
            return null;
        }

        ElementoDobleEnlazada<T> deleted = last;
        if (size == 1){
            first = last = null;
        }
        else{
            last = last.before;
            last.next = first;
            first.before = last;
        }
        size--;
        return deleted.elemento;
    }

    public T deleteAt(int position) {

        if (position < 0 || position > size ) {
            System.out.println("Operación inválida, no puedes añadir nada en esta posición.");
            return null;
        }

        if (position == 0){
            deleteFirst();
        }

        if (position == size){
            deleteLast();
        }

        ElementoDobleEnlazada<T> actual = first;
        for (int i = 0; i < position; i++){
            actual = actual.next;
        }
        actual.before.next = actual.next;
        actual.next.before = actual.before;
        size--;

        return actual.elemento;
    }
}
