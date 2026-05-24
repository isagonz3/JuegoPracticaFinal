package jueguito.juegopracticafinal.Modelo.Mundo.Pruebas;

import jueguito.juegopracticafinal.Modelo.Mundo.GrafoZonas;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Lista;

public class PruebaGrafoZonas {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS GRAFOZONAS ===");

        GrafoZonas grafo = new GrafoZonas();

        for (int i = 0; i < 10; i++) {
            grafo.addZona(new Zona(i));
        }

        grafo.conectar(0,1);
        grafo.conectar(1,2);
        grafo.conectar(2,3);
        grafo.conectar(2,4);
        grafo.conectar(3,6);
        grafo.conectar(3,7);
        grafo.conectar(4,5);
        grafo.conectar(7,8);
        grafo.conectar(8,9);

        // Probar zona
        System.out.println("Zona 3: " + grafo.getZona(3));

        // Vecinas
        Lista<Integer> vecinas = grafo.getZonasVecinas(2);

        System.out.println("Vecinas de 2:");
        for (int i = 0; i < vecinas.getSize(); i++) {
            System.out.println("- " + vecinas.get(i));
        }

        // Camino existente
        System.out.println("Existe camino 0 -> 9: " + grafo.existeCamino(0, 9));

        // BFS
        Lista<Integer> camino = grafo.BFSZonas(0, 9);

        System.out.println("Camino 0 -> 9:");
        if (camino != null) {
            for (int i = 0; i < camino.getSize(); i++) {
                System.out.print(camino.get(i));
                if (i < camino.getSize() - 1) System.out.print(" -> ");
            }
        }

        System.out.println("\n=== FIN PRUEBAS ===");
    }
}