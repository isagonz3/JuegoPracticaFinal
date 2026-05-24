package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.NodoArbolBinario;

public class PruebaNodoArbolBinario {

    public static void main(String[] args) {

        System.out.println("PRUEBA NODO ARBOL BINARIO");

        NodoArbolBinario<Integer> raiz =
                new NodoArbolBinario<>(10);

        NodoArbolBinario<Integer> izq =
                new NodoArbolBinario<>(5);

        NodoArbolBinario<Integer> der =
                new NodoArbolBinario<>(20);

        raiz.setIzq(izq);
        raiz.setDer(der);

        System.out.println("Dato raiz:");
        System.out.println(raiz.getDato());

        System.out.println("Dato izquierda:");
        System.out.println(raiz.getIzq().getDato());

        System.out.println("Dato derecha:");
        System.out.println(raiz.getDer().getDato());
    }
}