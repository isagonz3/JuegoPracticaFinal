package jueguito.juegopracticafinal.TADs;

public class Cola<T> {
    private ElementoSimpleEnlazada<T> first;
    private ElementoSimpleEnlazada<T> last;
    private int size;

    public Cola(){
        first = null;
        last = null;
        size = 0;
    }

    public void enqueue(T data){
        ElementoSimpleEnlazada<T> aux = new ElementoSimpleEnlazada<>(data);
        if(first == null){
            first = aux;
        }
        else{
            last.setNext(aux);
        }
        last = aux;
        size++;
    }

    public T dequeue(){
        if(first == null){
            return null;
        }

        T data =  first.getData();
        first = first.getNext();
        if(first == null){
            last = null;
        }
        size--;
        return data;
    }

    public T peek(){
        if(first == null){
            return null;
        }
        return first.getData();
    }

    public boolean isEmpty(){
        return first == null;
    }

    public ElementoSimpleEnlazada<T> getFirst(){
        return first;
    }

    public int getSize(){
        return size;
    }

    public void clear(){
        first = null;
        last = null;
        size = 0;
    }

}
