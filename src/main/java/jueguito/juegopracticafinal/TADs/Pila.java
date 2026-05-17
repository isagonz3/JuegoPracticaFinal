package jueguito.juegopracticafinal.TADs;

public class Pila<T> {
    private ElementoSimpleEnlazada<T> top;
    private int size;

    public Pila() {
        top = null;
        size = 0;
    }

    public void push(T data){
        ElementoSimpleEnlazada<T> aux = new ElementoSimpleEnlazada<>(data);
        aux.setNext(top);
        top = aux;
        size++;
    }

    public T pop(){
        if (top == null){
            return null;
        }

        T  data = top.getData();
        top = top.getNext();
        size--;
        return data;
    }

    public T peek(){
        if (top == null){
            return null;
        }
        return top.getData();
    }

    public int getSize(){
        return size;
    }

    public void clear(){
        top = null;
        size = 0;
    }

    public boolean isEmpty(){
        return top == null;
    }
}
