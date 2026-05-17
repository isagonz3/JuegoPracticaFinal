package jueguito.juegopracticafinal.TADs;

public class ArbolBinarioDeBusqueda<T extends Comparable<T>> {

    private NodoArbolBinario<T> raiz;
    protected int numNodos = 0;

    public ArbolBinarioDeBusqueda() {
        this.raiz = null;
    }
    public ArbolBinarioDeBusqueda(NodoArbolBinario<T> raiz) {
        this.raiz = raiz;
    }

    protected NodoArbolBinario<T> searchNodo(NodoArbolBinario<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }
        else {
            if (dato.compareTo(nodo.dato) < 0) {
                return searchNodo(nodo.izq, dato);
            }
            else if (dato.compareTo(nodo.dato) > 0) {
                return searchNodo(nodo.der, dato);
            }
            else{
                return nodo;
            }
        }
    }

    public T search(T data){
        NodoArbolBinario<T> aux = searchNodo(raiz, data);
        if (aux == null) {
            return null;
        }
        else {
            return aux.dato;
        }
    }

    public int getGrado(){
        return gradoNodo(raiz);
    }

    private int gradoNodo(NodoArbolBinario<T> nodo){
        if(nodo == null){
            return 0;
        }
        else{
            int numGrados = 0;

            if(nodo.izq != null){
                numGrados++;
            }
            if(nodo.der != null){
                numGrados++;
            }
            int gradoIzq = gradoNodo(nodo.izq);
            int gradoDer = gradoNodo(nodo.der);
            return Math.max(numGrados, Math.max(gradoIzq,gradoDer));
        }
    }

    public int getAltura(){
        return alturaNodo(raiz);
    }

    private int alturaNodo(NodoArbolBinario<T> nodo){
        if(nodo == null) return -1;
        return Math.max(alturaNodo(nodo.izq),alturaNodo(nodo.der)) +1;
    }

    public Lista<T> getListaDatosNivel(int nivelNodo){
        Lista<T> datosNivel = new Lista<>();
        if(raiz == null ||  nivelNodo < 0){
            return datosNivel;
        }

        getNivel(raiz, nivelNodo, 0, datosNivel);
        return datosNivel;
    }

    private void getNivel(NodoArbolBinario<T> nodo, int nivelNodo, int nivelActual, Lista<T> datosNivel) {
        if (nodo == null) {
            return;
        }
        if (nivelNodo == nivelActual) {
            datosNivel.add(nodo.dato);
            return;
        }
        getNivel(nodo.izq, nivelNodo, nivelActual+1, datosNivel);
        getNivel(nodo.der, nivelNodo, nivelActual+1, datosNivel);
    }

    public boolean isArbolHomogeneo(){
        return isHomogeneo(raiz);
    }

    //Un arbol es homogeneo cuando el número de hijos de los subarboles es igual al
    //grado del arbol
    private boolean isHomogeneo(NodoArbolBinario<T> nodo){
        if(nodo == null) return true;

        boolean existeIzq = nodo.izq != null;
        boolean existeDer = nodo.der != null;

        if(existeIzq != existeDer){
            return false;
        }
        return isHomogeneo(nodo.izq) && isHomogeneo(nodo.der);
    }

    private int getProfundidad(NodoArbolBinario<T> nodo){
        if(nodo == null) return 0;
        int profIzq = getProfundidad(nodo.izq);
        int profDer = getProfundidad(nodo.der);
        return Math.max(profIzq, profDer) +1;
    }

    public boolean isArbolCompleto(){
        int prof = getProfundidad(raiz);
        return isCompleto(raiz,prof,0);
    }

    //Buscamos un BST perfecto, es decir, que los nodos internos tengan grado 2
    //y que los nodos hoja tengan grado 0, y las hojas están todas a la misma altura
    private boolean isCompleto(NodoArbolBinario<T> nodo,int prof,int nivel){
        if(nodo == null) return true;
        if(nodo.izq == null && nodo.der == null){
            return prof == nivel +1;
        }
        if(nodo.izq == null || nodo.der == null){
            return false;
        }
        return isCompleto(nodo.izq,prof,nivel+1) && isCompleto(nodo.der,prof,nivel+1);

    }

    public boolean isArbolCasiCompleto(){
        return isCasiCompleto(raiz);
    }

    //Arbol casi completo: las hojas están en dos niveles, las del nivel
    //inferior son contiguas desde la izquierda del arbol
    private boolean isCasiCompleto(NodoArbolBinario<T> nodo){
        if(nodo == null) return true;

        Cola<NodoArbolBinario<T>> cola = new Cola<>();
        cola.enqueue(nodo);
        boolean nodoNulo = false;

        while(!cola.isEmpty()){
            NodoArbolBinario<T> nodoAux = cola.dequeue();
            if(nodoAux == null){
                nodoNulo = true;
            }
            else{
                if(nodoNulo){
                    return false;
                }
                cola.enqueue(nodoAux.izq);
                cola.enqueue(nodoAux.der);
            }
        }
        return true;
    }

    public Lista<T> getCamino(NodoArbolBinario<T> nodo, T dato){
        if(nodo == null){
            return null;
        }
        if(nodo.dato.equals(dato)){
            Lista<T> camino = new Lista<>();
            camino.add(nodo.dato);
            return camino;
        }
        if(dato.compareTo(nodo.dato) < 0){
            Lista<T> caminoIzq = getCamino(nodo.izq, dato);
            if(caminoIzq != null){
                caminoIzq.addFirst(nodo.dato);
                return caminoIzq;
            }
        }

        if(dato.compareTo(nodo.dato) > 0){
            Lista<T> caminoDer = getCamino(nodo.der, dato);
            if(caminoDer != null){
                caminoDer.addFirst(nodo.dato);
                return caminoDer;
            }
        }
        return null;
    }

    //Metodo para encontrar el nodo de menor valor
    private NodoArbolBinario<T> buscarMin(NodoArbolBinario<T> nodo){
        if(nodo == null){
            throw new IllegalStateException("El árbol está vacio");
        }
        while(nodo.izq != null){
            nodo = nodo.izq;
        }
        return nodo;
    }

    public void add(T dato) {
        raiz = addNodo(raiz, dato);
    }

    /*Metodo que añade un valor al arbol binario de forma recursiva:
     * Comprueba nodo a nodo si el valor ya existe en el arbol, y coloca el
     * nuevo dato a la izq. o drcha. de los nodos*/

    private NodoArbolBinario<T> addNodo(NodoArbolBinario<T> nodo, T dato) {
        //Nodo nulo: creamos uno nuevo
        if (nodo == null) {
            numNodos++;
            nodo = new NodoArbolBinario<>(dato);
            return nodo;
        }
        //Nodo no nulo: nos movemos a la izq o drcha comparando el dato a añadir y el dato en el nodo
        else {
            if (dato.compareTo(nodo.dato) < 0) {
                nodo.izq = addNodo(nodo.izq, dato);
            } else if (dato.compareTo(nodo.dato) > 0) {
                nodo.der = addNodo(nodo.der, dato);
            }
            else{
                //El dato a añadir es igual al del nodo en el que nos encontramos
                throw new IllegalArgumentException("El árbol ya contiene un nodo con dato:" + dato);
            }
        }
        return nodo;
    }

    public void delete(T dato) {
        raiz = deleteNodo(raiz, dato);
    }

    /*Para eliminar un nodo con dos hijos, necesitamos encontrar el hijo de
     * menor valor en el sub-arbol derecho (orden central). Asignamos el valor
     * de ese hijo al nodo a eliminar, y se elimina el hijo en orden central del
     * sub-arbol derecho.*/

    private void deleteNodoGrado2(NodoArbolBinario<T> nodo) {
        NodoArbolBinario<T> aux = buscarMin(nodo.der);
        nodo.dato = aux.dato;
        nodo.der = deleteNodo(nodo.der, aux.dato);
    }

    /*Metodo que elimina un valor del arbol binario de forma recursiva:
     * Comprueba nodo a nodo si el valor ya existe en el arbol, y elimina el dato */

    public NodoArbolBinario<T> deleteNodo(NodoArbolBinario<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }

        if(dato.compareTo(nodo.dato) < 0){
            nodo.izq = deleteNodo(nodo.izq, dato);
        }
        else if(dato.compareTo(nodo.dato) > 0){
            nodo.der = deleteNodo(nodo.der, dato);
        }
        else{
            if(nodo.izq == null && nodo.der == null){ //Si es una hoja
                numNodos--;
                return null;
            }
            else if(nodo.izq == null){ //Nodo tiene un hijo derecho
                numNodos--;
                return nodo.der;
            }
            else if(nodo.der == null){ //Nodo tiene un hijo izquierdo
                numNodos--;
                return nodo.izq;
            }
            else{
                deleteNodoGrado2(nodo); //Nodo tiene dos hijos
            }
        }
        return nodo;
    }

    public ArbolBinarioDeBusqueda<T> getSubArbolIzq(T dato) {
        NodoArbolBinario<T> nodo = searchNodo(raiz, dato);
        if (nodo == null || nodo.izq == null) {
            return new ArbolBinarioDeBusqueda<>();
        }
        return new ArbolBinarioDeBusqueda<>(nodo.izq);
    }

    public ArbolBinarioDeBusqueda<T> getSubArbolDer(T dato){
        NodoArbolBinario<T> nodo = searchNodo(raiz, dato);
        if (nodo == null || nodo.der == null) {
            return new ArbolBinarioDeBusqueda<>();
        }
        return new ArbolBinarioDeBusqueda<>(nodo.der);
    }

    private void preordenArbol(NodoArbolBinario<T> nodo,Lista<NodoArbolBinario<T>> lista){
        if (nodo == null) return;
        lista.add(nodo);
        preordenArbol(nodo.izq, lista);
        preordenArbol(nodo.der, lista);
    }

    private void inordenArbol(NodoArbolBinario<T> nodo,Lista<NodoArbolBinario<T>> lista){
        if (nodo == null) return;
        inordenArbol(nodo.izq, lista);
        lista.add(nodo);
        inordenArbol(nodo.der, lista);
    }
    private void postordenArbol(NodoArbolBinario<T> nodo,Lista<NodoArbolBinario<T>> lista){
        if (nodo == null) return;
        postordenArbol(nodo.izq, lista);
        postordenArbol(nodo.der, lista);
        lista.add(nodo);
    }

    public Lista<NodoArbolBinario<T>> getListaPreorden(){
        Lista<NodoArbolBinario<T>> lista = new Lista<>();
        preordenArbol(raiz, lista);
        return lista;
    }

    public Lista<NodoArbolBinario<T>> getListaInorden(){
        Lista<NodoArbolBinario<T>> lista = new Lista<>();
        inordenArbol(raiz, lista);
        return lista;
    }

    public Lista<NodoArbolBinario<T>> getListaPostorden(){
        Lista<NodoArbolBinario<T>> lista = new Lista<>();
        postordenArbol(raiz, lista);
        return lista;
    }

}
