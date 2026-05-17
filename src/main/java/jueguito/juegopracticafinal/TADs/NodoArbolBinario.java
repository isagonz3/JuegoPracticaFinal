package jueguito.juegopracticafinal.TADs;


public class NodoArbolBinario<T  extends Comparable<T>> implements Comparable<NodoArbolBinario<T>> {

    protected T dato;
    protected NodoArbolBinario<T> izq;
    protected NodoArbolBinario<T> der;

    public NodoArbolBinario(T dato) {
        this.dato = dato;
        this.izq = this.der = null;
    }

    public NodoArbolBinario(T dato,NodoArbolBinario<T> izq, NodoArbolBinario<T> der) {
        this.dato = dato;
        this.izq = izq;
        this.der = der;
    }

    public NodoArbolBinario<T> getIzq() {
        return this.izq;
    }

    public NodoArbolBinario<T> getDer() {
        return this.der;
    }

    public void setIzq(NodoArbolBinario<T> izq) {
        this.izq = izq;
    }

    public void setDer(NodoArbolBinario<T> der) {
        this.der = der;
    }

    public T getDato() {
        return this.dato;
    }
    public void setDato(T dato) {
        this.dato = dato;
    }

    public void printDato() {
        System.out.println(this.dato + "  ");
    }

    @Override
    public int compareTo(NodoArbolBinario<T> o) {
        return 0;
    }
}

