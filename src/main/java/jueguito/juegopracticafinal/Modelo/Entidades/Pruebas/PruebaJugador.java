package jueguito.juegopracticafinal.Modelo.Entidades.Pruebas;

import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;

public class PruebaJugador {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS JUGADOR ===");

        // Estadísticas (AJUSTA si tu constructor es distinto)
        Estadisticas stats = new Estadisticas(100, 10, 5, 3);

        // Posición
        Posicion pos = new Posicion(2, 3);
        pos.setIdZona(0);

        // Crear jugador
        Jugador jugador = new Jugador("Heroe", stats, pos);

        System.out.println("Nombre: " + jugador.getNombre());
        System.out.println("Tipo: " + jugador.getTipoEntidad());

        // Inventario
        Objeto espada = new Objeto("Espada", TipoObjeto.EQUIPABLE);
        Objeto escudo = new Objeto("Escudo", TipoObjeto.EQUIPABLE);
        Objeto pocion = new Objeto("Poción", TipoObjeto.USABLE);

        jugador.getInventario().addObjeto(espada);
        jugador.getInventario().addObjeto(escudo);
        jugador.getInventario().addObjeto(pocion);

        System.out.println("\n=== INVENTARIO ===");
        System.out.println("Tiene espada: " +
                jugador.getInventario().containsObjeto("Espada"));

        // Equipar objetos
        System.out.println("\n=== EQUIPAMIENTO ===");

        boolean eq1 = jugador.equipar(espada, SlotEquipable.MANO_PRINCIPAL);
        System.out.println("Equipar espada: " + eq1);

        boolean eq2 = jugador.equipar(escudo, SlotEquipable.MANO_SECUNDARIA);
        System.out.println("Equipar escudo: " + eq2);

        // Ver equipamiento
        System.out.println("\nMano principal: " +
                jugador.getEquipamiento(SlotEquipable.MANO_PRINCIPAL));

        System.out.println("Mano secundaria: " +
                jugador.getEquipamiento(SlotEquipable.MANO_SECUNDARIA));

        // Probar quitar equipamiento
        System.out.println("\n=== QUITAR EQUIPO ===");
        jugador.quitarEquipamiento(SlotEquipable.MANO_PRINCIPAL);

        System.out.println("Después de quitar:");
        System.out.println("Mano principal: " +
                jugador.getEquipamiento(SlotEquipable.MANO_PRINCIPAL));

        // Probar estadísticas modificadas
        System.out.println("\n=== ESTADÍSTICAS ===");
        System.out.println("ATK: " + stats.getAtaqueBase());
        System.out.println("DEF: " + stats.getDefensaBase());
        System.out.println("RANGO: " + stats.getRangoMov());

        System.out.println("\n=== FIN PRUEBAS ===");
    }
}