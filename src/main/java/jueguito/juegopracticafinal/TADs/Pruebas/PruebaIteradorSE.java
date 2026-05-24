package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.ElementoSimpleEnlazada;
import jueguito.juegopracticafinal.TADs.IteradorSE;

public class PruebaIteradorSE {

    public static void main(String[] args) {

        System.out.println("PRUEBA ITERADOR");

        ElementoSimpleEnlazada<Integer> n1 =
                new ElementoSimpleEnlazada<>(1);

        ElementoSimpleEnlazada<Integer> n2 =
                new ElementoSimpleEnlazada<>(2);

        ElementoSimpleEnlazada<Integer> n3 =
                new ElementoSimpleEnlazada<>(3);

        n1.setNext(n2);
        n2.setNext(n3);

        IteradorSE<Integer> iterador = new IteradorSE<>(n1);

        while(iterador.hasNext()) {
            System.out.println(iterador.next());
        }
    }
}