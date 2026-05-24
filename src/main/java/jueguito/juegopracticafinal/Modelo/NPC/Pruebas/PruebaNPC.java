package jueguito.juegopracticafinal.Modelo.NPC.Pruebas;

import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;
import jueguito.juegopracticafinal.Modelo.NPC.Tradeo;
import jueguito.juegopracticafinal.TADs.Lista;

public class PruebaNPC {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS NPC ===");

        // Crear NPC
        NPC npc = new NPC("Carlos", TipoNPC.COMERCIANTE);

        System.out.println("Nombre: " + npc.getNombre());
        System.out.println("Tipo: " + npc.getTipo());

        // Probar diálogos
        npc.addDialogo("Hola viajero");
        npc.addDialogo("¿Qué necesitas?");
        npc.addDialogo("Hasta luego");

        System.out.println("\n=== DIÁLOGOS ===");
        System.out.println(npc.hablar());
        System.out.println(npc.hablar());
        System.out.println(npc.hablar());
        System.out.println(npc.hablar()); // debería devolver "..."

        // Reset diálogo
        npc.restartDialogo();
        System.out.println("Después de reset: " + npc.hablar());

        // Probar info gato
        System.out.println("\n=== INFO GATO ===");
        System.out.println("¿Tiene info?: " + npc.tieneInfoGato());

        npc.setInfoGato("Los gatos dominan el mundo");

        System.out.println("Info: " + npc.getInfoGato());
        System.out.println("¿Tiene info?: " + npc.tieneInfoGato());

        // Probar comercio
        System.out.println("\n=== COMERCIO ===");

        System.out.println("¿Puede comerciar?: " + npc.comerciar());

        Tradeo t1 = new Tradeo();
        Tradeo t2 = new Tradeo();

        npc.addTradeo(t1);
        npc.addTradeo(t2);

        System.out.println("Tradeos disponibles: " + npc.getTradeos().getSize());

        System.out.println("¿Puede comerciar ahora?: " + npc.comerciar());

        // Mostrar tradeos
        Lista<Tradeo> lista = npc.getTradeos();

        System.out.println("\nLista de tradeos:");
        for (int i = 0; i < lista.getSize(); i++) {
            System.out.println("- tradeo " + i + ": " + lista.get(i));
        }

        System.out.println("\n=== FIN PRUEBAS ===");
    }
}