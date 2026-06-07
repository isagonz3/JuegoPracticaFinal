package jueguito.juegopracticafinal.TADs;

public interface InterfazGrafo<N,A extends InterfazArista<N>> {
    void addNodo(N nodo);
    boolean addArista(A arista);
    boolean existeArista(N origen,N destino);
    Lista<N> getNodos();
    Lista<A> getAristas();
    Lista<N> getAdyacentes(N nodo);
    Lista<A> getAristasDesde(N nodo);
    A getArista(N origen,N destino);
    int getIndex(N nodo);
    int size();
    boolean isEmpty();
}
