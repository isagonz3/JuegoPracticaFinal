package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.Cola;

public class PruebaCola {

    public static void main(String[] args) {

        System.out.println("PRUEBA COLA");

        Cola<Integer> cola = new Cola<>();

        // enqueue
        cola.enqueue(10);
        cola.enqueue(20);
        cola.enqueue(30);

        System.out.println("Tamano despues de enqueue:");
        System.out.println(cola.getSize());

        // peek
        System.out.println("Primer elemento:");
        System.out.println(cola.peek());

        // dequeue
        System.out.println("Elemento eliminado:");
        System.out.println(cola.dequeue());

        System.out.println("Nuevo primer elemento:");
        System.out.println(cola.peek());

        // isEmpty
        System.out.println("Esta vacia?");
        System.out.println(cola.isEmpty());

        // clear
        cola.clear();

        System.out.println("Cola vaciada");

        System.out.println("Esta vacia ahora?");
        System.out.println(cola.isEmpty());
    }
}