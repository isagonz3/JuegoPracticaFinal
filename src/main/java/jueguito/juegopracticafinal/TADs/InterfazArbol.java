package jueguito.juegopracticafinal.TADs;

public interface InterfazArbol<T extends Comparable<T>> {
    void add(T elemento);
    boolean delete(T elemento);
    boolean contains(T elemento);
    Lista<T> inOrden();
    Lista<T> preOrden();
    Lista<T> postOrden();
    int getAltura();
    int size();
    boolean isEmpty();
    void clear();
}
