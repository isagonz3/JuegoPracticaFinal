package jueguito.juegopracticafinal.Modelo.Entidades.Pruebas;

import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;

public class PruebaEstadisticas {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS ESTADÍSTICAS ===");

        // Crear estadísticas
        Estadisticas stats = new Estadisticas(100, 20, 10, 5);

        // Estado inicial
        System.out.println("\n--- ESTADO INICIAL ---");
        System.out.println(stats);

        // Probar daño
        System.out.println("\n--- RECIBIR ATAQUE ---");
        stats.recibirAtaque(30);
        System.out.println("Después de 30 daño: " + stats.getVidaActual());

        stats.recibirAtaque(1000); // sobrekill
        System.out.println("Después de overkill: " + stats.getVidaActual());

        System.out.println("¿Está vivo?: " + stats.estaVivo());

        // Curar vida
        System.out.println("\n--- CURAR VIDA ---");
        stats.curarVida(50);
        System.out.println("Después de curar 50: " + stats.getVidaActual());

        stats.curarVida(1000); // test límite
        System.out.println("Después de cura excesiva: " + stats.getVidaActual());

        // Movimiento
        System.out.println("\n--- MOVIMIENTO ---");
        System.out.println("Movimientos disponibles: " + stats.getPuntosMovDisponibles());

        boolean mov1 = stats.usarMovimiento(2);
        System.out.println("Usar 2 puntos: " + mov1);

        boolean mov2 = stats.usarMovimiento(999);
        System.out.println("Usar demasiado: " + mov2);

        System.out.println("Puede moverse?: " + stats.puedeMoverse());
        System.out.println("Mov restantes: " + stats.getPuntosMovDisponibles());

        stats.resetMovimiento();
        System.out.println("Después de reset: " + stats.getPuntosMovDisponibles());

        // Buffs
        System.out.println("\n--- BUFFS ---");
        stats.aumentarAtaque(5);
        stats.aumentarDefensa(3);
        stats.aumentarRangoMov(2);

        System.out.println("ATAQUE: " + stats.getAtaqueBase());
        System.out.println("DEFENSA: " + stats.getDefensaBase());
        System.out.println("RANGO: " + stats.getRangoMov());

        // Clonar
        System.out.println("\n--- CLONACIÓN ---");
        Estadisticas copia = stats.clonar();

        System.out.println("Original: " + stats);
        System.out.println("Copia:    " + copia);

        copia.recibirAtaque(10);

        System.out.println("\nDespués de modificar copia:");
        System.out.println("Original: " + stats.getVidaActual());
        System.out.println("Copia:    " + copia.getVidaActual());

        System.out.println("\n=== FIN PRUEBAS ===");
    }
}