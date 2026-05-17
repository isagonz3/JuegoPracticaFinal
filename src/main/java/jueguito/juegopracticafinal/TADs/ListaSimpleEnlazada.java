package jueguito.juegopracticafinal.TADs;


public class ListaSimpleEnlazada<T> {

    //Atributos
    protected ElementoSimpleEnlazada<T> first;
    protected int size;

    //Constructor de lista vacía
    public ListaSimpleEnlazada() {
        first = null;
        size = 0;
    }

    //Buscar un elemento de la lista según su valor, recorre la lista de principio a fin
    public T get(T data){
        ElementoSimpleEnlazada<T> actual = first;
        while(actual!=null){
            if(actual.getData().equals(data)){
                return actual.getData();
            }
            actual = actual.getNext();
        }
        return null;
    }

    //Elimina un elemento de la lista segun su valor
    public T delete(T data){
        if(first == null){
            return null;
        }

        if(first.getData().equals(data)){
            T deleted = first.getData();
            first = first.getNext();
            size--;
            return deleted;
        }

        ElementoSimpleEnlazada<T> actual = first;
        while(actual.getNext() != null && !actual.getNext().getData().equals(data)){
            actual =  actual.getNext();
        }
        if(actual.getNext() == null){
            return null;
        }
        T deleted = actual.getNext().getData();
        actual.setNext(actual.getNext().getNext());
        size--;
        return deleted;
    }

    //Comprueba si la lista está vacía
    public boolean isEmpty(){
        return first == null;
    }

    public void addLast(T data){
        ElementoSimpleEnlazada<T> nuevo = new ElementoSimpleEnlazada<>(data);

        if(isEmpty()){
            first = nuevo;
        }
        else{
            ElementoSimpleEnlazada<T> actual = first;
            while(actual.getNext() != null){
                actual = actual.getNext();
            }
            actual.setNext(nuevo);
        }
        size++;
    }

    public int getSize(){
        return size;
    }

    //Vaciar la lista
    public void clear(){
        first = null;
        size = 0;
    }

    //Dato en una posición concreta en la lista
    public T get(int index){
        if(index < 0 || index >= size){
            return null;
        }
        ElementoSimpleEnlazada<T> actual = first;
        for(int i = 0; i < index; i++){
            actual = actual.getNext();
        }
        return actual.getData();
    }

    public int indexOf(T data){
        ElementoSimpleEnlazada<T> actual = first;
        int index = 0;

        while(actual != null){
            if(actual.getData().equals(data)){
                return index;
            }
            actual = actual.getNext();
            index++;
        }
        return -1;
    }

    public boolean contains(T data){
        ElementoSimpleEnlazada<T> actual = first;
        while(actual != null){
            if(actual.getData().equals(data)){
                return true;
            }
            actual = actual.getNext();
        }
        return false;
    }

    public void set(T data, int index){
        if(index < 0 || index >= size){
            return;
        }
        ElementoSimpleEnlazada<T> actual = first;
        for(int i = 0; i < index; i++){
            actual = actual.getNext();
        }
        actual.setData(data);
    }

    public ElementoSimpleEnlazada<T> getFirst(){
        return first;
    }

    public int size(){
        int contador = 0;
        ElementoSimpleEnlazada<T> actual = first;

        while(actual != null){
            contador++;
            actual = actual.getNext();
        }
        return contador;
    }

    public Iterador<T> getIterador(){
        return new IteradorSE<>(first);
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder("[");

        Iterador<T> it = this.getIterador();

        while (it.hasNext()) {
            resultado.append(it.next());

            if (it.hasNext()) {
                resultado.append(", ");
            }
        }

        resultado.append("]");
        return resultado.toString();
    }

}
