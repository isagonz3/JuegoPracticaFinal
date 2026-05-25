package jueguito.juegopracticafinal.Modelo.NPC;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NPCTest {

    private NPC npc;

    @BeforeEach
    void setUp() {
        npc = new NPC("Aldeano", TipoNPC.ALDEANO);
    }

    @Test void constructorBasico() {
        assertEquals("Aldeano", npc.getNombre());
        assertEquals(TipoNPC.ALDEANO, npc.getTipo());
        assertFalse(npc.tieneInfoGato());
        assertFalse(npc.comerciar());
    }

    @Test void getTradeos() {
        assertNotNull(npc.getTradeos());
        assertTrue(npc.getTradeos().isEmpty());
    }

    @Test void infoGato() {
        npc.setInfoGato("Lo vi en el bosque");
        assertTrue(npc.tieneInfoGato());
        assertEquals("Lo vi en el bosque", npc.getInfoGato());
        npc.setInfoGato("");
        assertFalse(npc.tieneInfoGato());
    }

    @Test void infoGatoNull() {
        NPC n = new NPC("Test", TipoNPC.ALDEANO);
        assertFalse(n.tieneInfoGato());
    }

    @Test void addDialogo() {
        npc.addDialogo("Hola");
        npc.addDialogo("Adios");
        assertEquals("Hola", npc.hablar());
        assertEquals("Adios", npc.hablar());
    }

    @Test void addTradeo() {
        Tradeo t = new Tradeo();
        npc.addTradeo(t);
        assertEquals(1, npc.getTradeos().getSize());
        assertEquals(t, npc.getTradeos().get(0));
    }

    @Test void hablar() {
        npc.addDialogo("Primera frase");
        assertEquals("Primera frase", npc.hablar());
        assertEquals("...", npc.hablar());
    }

    @Test void hablarSinDialogos() {
        assertEquals("...", npc.hablar());
    }

    @Test void restartDialogo() {
        npc.addDialogo("Frase 1");
        npc.addDialogo("Frase 2");
        npc.hablar();
        npc.restartDialogo();
        assertEquals("Frase 1", npc.hablar());
    }

    @Test void comerciar() {
        NPC comerciante = new NPC("Vendedor", TipoNPC.COMERCIANTE);
        assertFalse(comerciante.comerciar());
        comerciante.addTradeo(new Tradeo());
        assertTrue(comerciante.comerciar());
        NPC aldeano = new NPC("Pep", TipoNPC.ALDEANO);
        assertFalse(aldeano.comerciar());
    }
}
