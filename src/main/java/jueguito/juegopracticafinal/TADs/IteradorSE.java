package jueguito.juegopracticafinal.TADs;


public class IteradorSE<T> implements Iterador<T> {

    //Atributos
    private ElementoSimpleEnlazada<T> actual;

    public IteradorSE(ElementoSimpleEnlazada<T> first){
        this.actual = first;
    }

    @Override
    public boolean hasNext(){
        return actual != null;
    }

    @Override
    public T next(){
        T data = actual.getData();
        actual = actual.getNext();
        return data;
    }
}
