package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.Pila;

public class PruebaPila {

    public static void main(String[] args) {

        System.out.println("PRUEBA PILA");

        Pila<Integer> pila = new Pila<>();

        // push
        pila.push(1);
        pila.push(2);
        pila.push(3);

        System.out.println("Tamano despues de push:");
        System.out.println(pila.getSize());

        // peek
        System.out.println("Elemento superior:");
        System.out.println(pila.peek());

        // pop
        System.out.println("Elemento eliminado:");
        System.out.println(pila.pop());

        System.out.println("Nuevo elemento superior:");
        System.out.println(pila.peek());

        // isEmpty
        System.out.println("Esta vacia?");
        System.out.println(pila.isEmpty());

        // clear
        pila.clear();

        System.out.println("Pila vaciada");

        System.out.println("Esta vacia ahora?");
        System.out.println(pila.isEmpty());
    }
}