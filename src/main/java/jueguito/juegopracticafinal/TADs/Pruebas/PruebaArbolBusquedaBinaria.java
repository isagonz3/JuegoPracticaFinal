package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.ArbolBinarioDeBusqueda;
import jueguito.juegopracticafinal.TADs.NodoArbolBinario;

public class PruebaArbolBusquedaBinaria {

    public static void main(String[] args) {

        System.out.println("PRUEBA ARBOL BINARIO DE BUSQUEDA");

        ArbolBinarioDeBusqueda<Integer> arbol =
                new ArbolBinarioDeBusqueda<>();

        // add
        arbol.add(10);
        arbol.add(5);
        arbol.add(15);
        arbol.add(3);
        arbol.add(7);

        System.out.println("Nodo buscado:");
        System.out.println(arbol.search(7));

        // altura
        System.out.println("Altura del arbol:");
        System.out.println(arbol.getAltura());

        // grado
        System.out.println("Grado del arbol:");
        System.out.println(arbol.getGrado());

        // lista nivel
        System.out.println("Nodos nivel 1:");
        System.out.println(arbol.getListaDatosNivel(1));

        // homogeneo
        System.out.println("Es homogeneo?");
        System.out.println(arbol.isArbolHomogeneo());

        // completo
        System.out.println("Es completo?");
        System.out.println(arbol.isArbolCompleto());

        // casi completo
        System.out.println("Es casi completo?");
        System.out.println(arbol.isArbolCasiCompleto());

        // camino
        System.out.println("Camino hasta 7:");
        System.out.println(arbol.getCamino(
                new NodoArbolBinario<>(10), 7));

        // delete
        arbol.delete(5);

        System.out.println("Buscar 5 despues de borrar:");
        System.out.println(arbol.search(5));

        // recorridos
        System.out.println("Lista preorden:");
        System.out.println(arbol.getListaPreorden());

        System.out.println("Lista inorden:");
        System.out.println(arbol.getListaInorden());

        System.out.println("Lista postorden:");
        System.out.println(arbol.getListaPostorden());
    }
}