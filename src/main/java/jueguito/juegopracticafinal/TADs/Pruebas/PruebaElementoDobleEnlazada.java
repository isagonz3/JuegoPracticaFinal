package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.ElementoDobleEnlazada;

public class PruebaElementoDobleEnlazada {

    public static void main(String[] args) {

        System.out.println("PRUEBA ELEMENTO DOBLE ENLAZADA");

        ElementoDobleEnlazada<String> nodo =
                new ElementoDobleEnlazada<>("Hola");

        System.out.println("Nodo creado correctamente");
    }
}