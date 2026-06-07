package jueguito.juegopracticafinal.TADs;


public class Iterador<T> implements InterfazIterador<T> {

    //Atributos
    private ElementoDobleEnlazada<T> actual;

    public Iterador(ElementoDobleEnlazada<T> first){
        this.actual = first;
    }

    @Override
    public boolean hasNext(){
        return actual != null;
    }

    @Override
    public T next(){
        T data = actual.elemento;
        actual = actual.next;
        return data;
    }

}
