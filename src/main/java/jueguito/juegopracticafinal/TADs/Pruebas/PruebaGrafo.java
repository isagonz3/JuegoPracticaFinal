package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.Grafo;

public class PruebaGrafo {

    public static void main(String[] args) {

        System.out.println("PRUEBA GRAFO");

        Grafo grafo = new Grafo();

        // addZona
        grafo.addZona(0, "Bosque", 5, 5);
        grafo.addZona(1, "Cueva", 4, 4);
        grafo.addZona(2, "Rio", 6, 6);

        // conectarZona
        grafo.conectarZona(0, 1);
        grafo.conectarZona(1, 2);

        System.out.println("Zonas conectadas con zona 1:");
        System.out.println(grafo.getZonasConectadas(1));

        // BFS
        System.out.println("Camino de 0 a 2:");
        System.out.println(grafo.BFS(0, 2));
    }
}