package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.ElementoSimpleEnlazada;

public class PruebaElementoSimpleEnlazada {

    public static void main(String[] args) {

        System.out.println("PRUEBA ELEMENTO SIMPLE ENLAZADA");

        ElementoSimpleEnlazada<Integer> nodo1 =
                new ElementoSimpleEnlazada<>(10);

        ElementoSimpleEnlazada<Integer> nodo2 =
                new ElementoSimpleEnlazada<>(20);

        nodo1.setNext(nodo2);

        System.out.println("Dato nodo1: " + nodo1.getData());
        System.out.println("Dato siguiente nodo: "
                + nodo1.getNext().getData());
    }
}