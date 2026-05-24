package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.Nodo;

public class PruebaNodo {

    public static void main(String[] args) {

        System.out.println("PRUEBA NODO");

        Nodo nodo = new Nodo(1, "Sala1", 5, 5);

        // addAdyacente
        nodo.addAdyacente(2);
        nodo.addAdyacente(3);

        System.out.println("ID:");
        System.out.println(nodo.getId());

        System.out.println("Nombre:");
        System.out.println(nodo.getNombre());

        System.out.println("Filas:");
        System.out.println(nodo.getRows());

        System.out.println("Columnas:");
        System.out.println(nodo.getCols());

        System.out.println("Adyacentes:");
        System.out.println(nodo.getAdyacentes());
    }
}