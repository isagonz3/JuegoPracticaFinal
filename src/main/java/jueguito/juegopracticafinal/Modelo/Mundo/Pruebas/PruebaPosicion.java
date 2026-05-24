package jueguito.juegopracticafinal.Modelo.Mundo.Pruebas;

import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;

public class PruebaPosicion {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS DE LA CLASE POSICION ===");

        // Crear posiciones
        Posicion posicion1 = new Posicion(2, 5);
        Posicion posicion2 = new Posicion(2, 5);
        Posicion posicion3 = new Posicion(4, 1);

        // Asignar zonas
        posicion1.setIdZona(1);
        posicion2.setIdZona(1);
        posicion3.setIdZona(2);

        // Probar getters
        System.out.println("Posición 1:");
        System.out.println("Zona: " + posicion1.getIdZona());
        System.out.println("Fila: " + posicion1.getRow());
        System.out.println("Columna: " + posicion1.getCol());

        // Probar setters
        posicion3.setRow(7);
        posicion3.setCol(9);

        System.out.println("\nPosición 3 modificada:");
        System.out.println("Zona: " + posicion3.getIdZona());
        System.out.println("Fila: " + posicion3.getRow());
        System.out.println("Columna: " + posicion3.getCol());

        // Probar equals
        System.out.println("\n=== PRUEBAS EQUALS ===");

        System.out.println("¿posicion1 == posicion2?: "
                + posicion1.equals(posicion2));

        System.out.println("¿posicion1 == posicion3?: "
                + posicion1.equals(posicion3));

        // Probar equals con null
        System.out.println("¿posicion1 == null?: "
                + posicion1.equals(null));

        // Probar equals consigo misma
        System.out.println("¿posicion1 == posicion1?: "
                + posicion1.equals(posicion1));

        // Probar toString
        System.out.println("\n=== PRUEBAS TOSTRING ===");

        System.out.println("posicion1: " + posicion1.toString());
        System.out.println("posicion2: " + posicion2.toString());
        System.out.println("posicion3: " + posicion3.toString());

        System.out.println("\n=== FIN DE PRUEBAS ===");
    }
}