package jueguito.juegopracticafinal.Modelo.Entidades.Pruebas;

import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;

public class PruebaGato {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS GATO ===");

        // Estadísticas (ajusta si tu constructor es distinto)
        Estadisticas stats = new Estadisticas(50, 5, 2, 8);

        // Crear gato
        Gato gato = new Gato("Michi", stats);

        // Probar getters heredados
        System.out.println("Nombre: " + gato.getNombre());

        // Probar tipo entidad
        System.out.println("Tipo: " + gato.getTipoEntidad());

        // Probar estadísticas
        System.out.println("HP: " + gato.getEstadisticas().getVidaActual());

        System.out.println("\n=== FIN PRUEBAS ===");
    }
}