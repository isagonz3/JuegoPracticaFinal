package jueguito.juegopracticafinal.TADs;


public class IteradorLista<T> implements Iterador<T> {

    //Atributos
    private ElementoDobleEnlazada<T> actual;

    public IteradorLista(ElementoDobleEnlazada<T> first){
        this.actual = first;
    }

    @Override
    public boolean hasNext(){
        return actual != null;
    }

    @Override
    public T next(){
        T data = actual.getElemento();
        actual = actual.getNext();
        return data;
    }

    public T previous() {
        if (actual == null || actual.getBefore() == null) return null;
        actual = actual.getBefore();
        return actual.getElemento();
    }
}
