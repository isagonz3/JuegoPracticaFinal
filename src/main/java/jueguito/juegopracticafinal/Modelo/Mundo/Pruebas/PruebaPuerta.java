package jueguito.juegopracticafinal.Modelo.Mundo.Pruebas;

import jueguito.juegopracticafinal.Modelo.Mundo.Puerta;

public class PruebaPuerta {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS PUERTA ===");

        // Puertas con constructor completo
        Puerta p1 = new Puerta(1, 2, 10, 20, 30, 40);
        Puerta p2 = new Puerta(1, 2, 10, 20, 30, 40);
        Puerta p3 = new Puerta(2, 3, 5, 15, 25, 35);
        Puerta p4 = new Puerta(1, 2, 11, 20, 30, 40);

        // Probar getters
        System.out.println("Puerta 1:");
        System.out.println("Zona origen: " + p1.getZonaOrigen());
        System.out.println("Zona destino: " + p1.getZonaDestino());
        System.out.println("X origen: " + p1.getXOrigen());
        System.out.println("Y origen: " + p1.getYOrigen());
        System.out.println("X destino: " + p1.getXDestino());
        System.out.println("Y destino: " + p1.getYDestino());

        // Probar compareTo
        System.out.println("\n=== PRUEBAS COMPARETO ===");

        System.out.println("p1 vs p2 (debería ser 0): " + p1.compareTo(p2));
        System.out.println("p1 vs p3 (debería ser < 0 o > 0): " + p1.compareTo(p3));
        System.out.println("p1 vs p4 (debería comparar por xOrigen): " + p1.compareTo(p4));

        // Orden lógico manual
        System.out.println("\n=== ORDENACIÓN LÓGICA ===");

        Puerta[] puertas = {p3, p4, p1, p2};

        java.util.Arrays.sort(puertas);

        for (Puerta p : puertas) {
            System.out.println(
                    p.getZonaOrigen() + " -> " +
                            p.getZonaDestino() + " | (" +
                            p.getXOrigen() + "," +
                            p.getYOrigen() + ")"
            );
        }

        // Probar constructor vacío
        System.out.println("\n=== CONSTRUCTOR VACÍO ===");
        Puerta vacia = new Puerta();

        System.out.println("Zona origen (default): " + vacia.getZonaOrigen());
        System.out.println("Zona destino (default): " + vacia.getZonaDestino());
        System.out.println("X origen (default): " + vacia.getXOrigen());

        System.out.println("\n=== FIN PRUEBAS ===");
    }
}