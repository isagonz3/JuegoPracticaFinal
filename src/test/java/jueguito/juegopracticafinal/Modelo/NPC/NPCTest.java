package jueguito.juegopracticafinal.Modelo.NPC;

import jueguito.juegopracticafinal.TADs.Lista;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NPCTest {

    @Test
    void constructorDebeAsignarNombreYTipo(){
        NPC npc=new NPC("Isabel",TipoNPC.ALDEANO);
        assertEquals("Isabel",npc.getNombre());
        assertEquals(TipoNPC.ALDEANO,npc.getTipo());
    }

    @Test
    void hablarNPCNoAldeanoSiempreDicePuntosSuspensivos(){
        NPC npc=new NPC("Carolina",TipoNPC.COMERCIANTE);
        assertEquals("...",npc.hablar());
    }

    @Test
    void hablarSirenaDebeDevolverMensaje1(){
        NPC npc=new NPC("Sirena",TipoNPC.ALDEANO);
        assertEquals("Parece que para llegar a la llave vas a necesitar una barca",npc.hablar());
    }

    @Test
    void hablarGuardiaDebeDevolverMensaje2(){
        NPC npc=new NPC("Guardia",TipoNPC.ALDEANO);
        assertEquals("Necesitas conseguir una llave para abrir el castillo",npc.hablar());
    }

    @Test
    void hablarConDialogosDebeAvanzar(){
        Lista<String> d=new Lista<>();
        d.add("hola");
        d.add("que tal");

        NPC npc=new NPC("Aldeano",TipoNPC.ALDEANO,d);

        assertEquals("hola",npc.hablar());
        assertEquals("que tal",npc.hablar());
    }

    @Test
    void hablarCuandoSeAgotanDialogosDebeSerPuntos(){
        Lista<String> d=new Lista<>();
        d.add("hola");

        NPC npc=new NPC("Aldeano",TipoNPC.ALDEANO,d);

        npc.hablar();
        assertEquals("...",npc.hablar());
    }

    @Test
    void setSpriteNoRompeEstado(){
        NPC npc=new NPC("Antonio",TipoNPC.ALDEANO);
        npc.setSprite("sprite.png");
        assertEquals("Antonio",npc.getNombre());
    }
}