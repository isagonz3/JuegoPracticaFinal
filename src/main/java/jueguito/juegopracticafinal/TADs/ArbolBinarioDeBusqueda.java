package jueguito.juegopracticafinal.TADs;

import jueguito.juegopracticafinal.Modelo.Excepciones.ExcepcionTADs;

public class ArbolBinarioDeBusqueda<T extends Comparable<T>> implements InterfazArbol<T>{

    private NodoArbol<T> raiz;
    private int size;

    @Override
    public void add(T elemento) {
        if(elemento == null){
            throw new ExcepcionTADs("El elemento no puede ser nulo.");
        }
        raiz = addRecur(raiz, elemento);
    }

    private NodoArbol<T> addRecur(NodoArbol<T> nodo, T elemento) {
        if(nodo == null){
            size++;
            return new NodoArbol<>(elemento);
        }

        int cmp = elemento.compareTo(nodo.elemento);
        if(cmp < 0){
            nodo.izq = addRecur(nodo.izq, elemento);
        }

        else if(cmp > 0){
            nodo.der = addRecur(nodo.der, elemento);
        }

        return nodo;
    }

    @Override
    public boolean delete(T elemento) {
        if(elemento == null || !contains(elemento)){
            return false;
        }

        raiz = deleteRecur(raiz, elemento);
        size--;
        return true;
    }

    private NodoArbol<T> deleteRecur(NodoArbol<T> nodo, T elemento) {
        if(nodo == null){
            return null;
        }
        int cmp = elemento.compareTo(nodo.elemento);
        if(cmp < 0){
            nodo.izq = deleteRecur(nodo.izq, elemento);
        }
        else if(cmp > 0){
            nodo.der = deleteRecur(nodo.der, elemento);
        }

        else{
            if(nodo.izq == null){
                return nodo.der;
            }
            if(nodo.der == null){
                return nodo.izq;
            }

            NodoArbol<T> aux = getMinimo(nodo.der);
            nodo.elemento = aux.elemento;
            nodo.der =  deleteRecur(nodo.der, aux.elemento);

        }
        return nodo;
    }

    @Override
    public boolean contains(T elemento) {
        return buscar(elemento) != null;
    }

    public NodoArbol<T> buscar(T elemento) {
        return buscarRecur(raiz, elemento);
    }

    private NodoArbol<T> buscarRecur(NodoArbol<T> nodo, T elemento) {
        if(nodo == null){
            return null;
        }
        int cmp = elemento.compareTo(nodo.elemento);
        if(cmp == 0){
            return nodo;
        }
        if(cmp < 0){
            return buscarRecur(nodo.izq, elemento);
        }
        return buscarRecur(nodo.der, elemento);
    }


    public NodoArbol<T> getMinimo(NodoArbol<T> nodo) {
        while (nodo.izq != null){
            nodo = nodo.izq;
        }
        return nodo;
    }


    @Override
    public Lista<T> inOrden() {
        Lista<T> lista = new Lista<>();
        inOrdenRecur(raiz, lista);
        return lista;
    }

    private void inOrdenRecur(NodoArbol<T> nodo, Lista<T> lista) {
        if(nodo == null){
            return;
        }
        inOrdenRecur(nodo.izq, lista);
        lista.add(nodo.elemento);
        inOrdenRecur(nodo.der, lista);
    }

    @Override
    public Lista<T> preOrden() {
        Lista<T> lista = new Lista<>();
        preOrdenRecur(raiz, lista);
        return lista;
    }

    private void preOrdenRecur(NodoArbol<T> nodo, Lista<T> lista) {
        if(nodo == null){
            return;
        }
        lista.add(nodo.elemento);
        preOrdenRecur(nodo.izq, lista);
        preOrdenRecur(nodo.der, lista);
    }

    @Override
    public Lista<T> postOrden() {
        Lista<T> lista = new Lista<>();
        postOrdenRecur(raiz, lista);
        return lista;
    }

    private void postOrdenRecur(NodoArbol<T> nodo, Lista<T> lista) {
        if(nodo == null){
            return;
        }
        postOrdenRecur(nodo.izq, lista);
        postOrdenRecur(nodo.der, lista);
        lista.add(nodo.elemento);
    }

    @Override
    public int getAltura() {
        return getAlturaRecur(raiz);
    }

    private int getAlturaRecur(NodoArbol<T> nodo) {
        if(nodo == null){
            return 0;
        }
        return 1 + Math.max(getAlturaRecur(nodo.izq), getAlturaRecur(nodo.der));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return raiz == null;
    }

    @Override
    public void clear() {
        raiz = null;
        size = 0;
    }

    @Override
    public String toString() {
        Lista<T> lista = inOrden();
        StringBuilder sb = new StringBuilder("[");
        InterfazIterador<T> it = lista.iterador();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
