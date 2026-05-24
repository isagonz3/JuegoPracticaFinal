package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.Lista;

public class PruebaLista {

    public static void main(String[] args) {

        System.out.println("PRUEBA LISTA");

        Lista<Integer> lista = new Lista<>();

        // add
        lista.add(1);
        lista.add(2);
        lista.add(3);

        System.out.println("Lista despues de add:");
        System.out.println(lista);

        // addFirst
        lista.addFirst(0);

        System.out.println("Lista despues de addFirst:");
        System.out.println(lista);

        // get
        System.out.println("Elemento posicion 2:");
        System.out.println(lista.get(2));

        // set
        lista.set(1, 99);

        System.out.println("Lista despues de set:");
        System.out.println(lista);

        // contains
        System.out.println("Contiene 99?");
        System.out.println(lista.contains(99));

        // delete
        lista.delete(2);

        System.out.println("Lista despues de delete:");
        System.out.println(lista);

        // getFirst
        System.out.println("Primer elemento:");
        System.out.println(lista.getFirst());

        // deleteLast
        lista.deleteLast();

        System.out.println("Lista despues de deleteLast:");
        System.out.println(lista);

        // size
        System.out.println("Tamano:");
        System.out.println(lista.getSize());

        // isEmpty
        System.out.println("Esta vacia?");
        System.out.println(lista.isEmpty());
    }
}