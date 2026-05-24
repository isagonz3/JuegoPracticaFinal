package jueguito.juegopracticafinal.Modelo.Entidades.Pruebas;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;

public class PruebaEnemigo {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS ENEMIGO ===");

        // Crear estadísticas (AJUSTA si tu clase es diferente)
        Estadisticas stats = new Estadisticas(100, 20, 10, 5);

        // Crear enemigo
        Enemigo enemigo = new Enemigo("Goblin", stats);

        // Probar getters heredados
        System.out.println("Nombre: " + enemigo.getNombre());

        // Probar tipo entidad
        System.out.println("Tipo: " + enemigo.getTipoEntidad());

        // Probar estadísticas
        System.out.println("HP: " + enemigo.getEstadisticas().getVidaActual());
        System.out.println("ATK: " + enemigo.getEstadisticas().getAtaqueBase());
        System.out.println("DEF: " + enemigo.getEstadisticas().getDefensaBase());

        System.out.println("\n=== FIN PRUEBAS ===");
    }
}