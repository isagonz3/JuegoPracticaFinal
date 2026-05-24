package jueguito.juegopracticafinal.Modelo.NPC.Pruebas;

import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.NPC.Tradeo;

public class PruebaTradeo {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS TRADEO ===");

        // Objetos
        Objeto espada = new Objeto("Espada");
        Objeto escudo = new Objeto("Escudo");
        Objeto pocion = new Objeto("Poción");

        // Crear tradeo: pide espada, ofrece escudo
        Tradeo tradeo = new Tradeo(espada, escudo);

        // Crear jugador (depende de tu constructor real)
        Jugador jugador = new Jugador();

        // Simulación de inventario (ajusta si tu inventario es distinto)
        jugador.getInventario().addObjeto(espada);

        System.out.println("Jugador tiene espada");

        // Probar oferta válida
        System.out.println("\n=== OFERTA ===");
        System.out.println("¿Oferta válida?: " + tradeo.ofertaValida(jugador));

        // Probar tradeo
        boolean resultado = tradeo.tradear(jugador);

        System.out.println("¿Tradeo realizado?: " + resultado);

        // Ver resultado en inventario
        System.out.println("\n=== INVENTARIO FINAL ===");
        System.out.println("¿Tiene espada?: " +
                jugador.getInventario().containsObjeto("Espada"));
        System.out.println("¿Tiene escudo?: " +
                jugador.getInventario().containsObjeto("Escudo"));

        // Caso inválido
        System.out.println("\n=== CASO INVÁLIDO ===");

        Tradeo tradeo2 = new Tradeo(pocion, escudo);

        System.out.println("¿Puede tradear sin poción?: " +
                tradeo2.tradear(jugador));

        System.out.println("=== FIN PRUEBAS ===");
    }
}