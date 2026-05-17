package jueguito.juegopracticafinal.TADs;

public class Lista<T> {
    protected ElementoDobleEnlazada<T> first;
    protected ElementoDobleEnlazada<T> last;
    int size = 0;

    public void add(T elemento) {
        ElementoDobleEnlazada<T> nuevo  = new ElementoDobleEnlazada<>(elemento);
        if (first == null) {
            first = last = nuevo;
        }
        else{
            last.next = nuevo;
            nuevo.before = last;
            last = nuevo;
        }
        size++;
    }

    public void addFirst(T elemento) {
        ElementoDobleEnlazada<T> nuevo  = new ElementoDobleEnlazada<>(elemento);

        if (first == null) {
            first = last = nuevo;
        }
        else{
            nuevo.next = first;
            first.before = nuevo;
            first = nuevo;
        }
        size++;
    }

    public void addAll(Lista<T> lista){
        if (lista == null || lista.first == null) {
            return;
        }
        ElementoDobleEnlazada<T> aux = lista.first;
        while (aux != null){
            this.add(aux.elemento);
            aux = aux.next;
        }
    }

    public T get(int index) {
        if (index < 0 || index >= size){
            return null;
        }
        ElementoDobleEnlazada<T> aux = first;
        for (int i = 0; i < index; i++){
            aux = aux.next;
        }
        return aux.elemento;
    }

    public void set(int index, T elemento) {
        if (index < 0 || index >= size){
            return;
        }
        Lista<T> lista = new Lista<>();
        for (int i = 0; i < index; i++){
            lista.add(elemento);
        }
    }

    public T delete(T elemento) {
        ElementoDobleEnlazada<T> actual = first;

        while (actual != null){
            if (actual.elemento.equals(elemento)) {
                if(actual.before == null) {
                    first = actual.next;
                }
                else{
                    actual.before.next = actual.next;
                }

                if(actual.next == null){
                    last = actual.before;
                }
                else{
                    actual.next.before = actual.before;
                }
                size--;
                return actual.elemento;
            }
            actual = actual.next;
        }
        return null;
    }

    public boolean isEmpty(){
        return first == null;
    }

    public int getSize(){
        return size;
    }

    public boolean contains(T elemento) {
        ElementoDobleEnlazada<T> aux = first;
        while (aux != null){
            if (aux.elemento.equals(elemento)){
                return true;
            }
            aux = aux.next;
        }
        return false;
    }

    public T getFirst() {
        if(first == null){
            return null;
        }
        return first.elemento;
    }

    public T deleteLast(){
        if(last == null){
            return null;
        }
        T aux = last.elemento;
        last = last.before;

        if (last != null){
            last.next = null;
        }
        else{
            first = null;
        }
        size--;
        return aux;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < this.getSize(); i++) {
            sb.append(this.get(i));
            if (i < this.getSize() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

}


