package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CeldaTest {

    @Test
    void getRowYSetRow() {
        Celda celda=new Celda(TipoCelda.SUELO);
        celda.setRow(2);
        assertEquals(2,celda.getRow());
    }

    @Test
    void getColYSetCol() {
        Celda celda=new Celda(TipoCelda.SUELO);
        celda.setCol(5);
        assertEquals(5,celda.getCol());
    }

    @Test
    void getYSetTipoCelda() {
        Celda celda=new Celda(TipoCelda.SUELO);
        celda.setTipoCelda(TipoCelda.PARED);
        assertEquals(TipoCelda.PARED,celda.getTipoCelda());
    }

    @Test
    void isTransitableSuelo() {
        Celda celda=new Celda(TipoCelda.SUELO);
        assertTrue(celda.isTransitable());
    }

    @Test
    void isTransitablePared() {
        Celda celda=new Celda(TipoCelda.PARED);
        assertFalse(celda.isTransitable());
    }

    @Test
    void puertaFuncionamiento() {
        Celda celda=new Celda(TipoCelda.SUELO);
        assertFalse(celda.tienePuerta());
        Puerta puerta=new Puerta();

        celda.setPuerta(puerta);
        assertTrue(celda.tienePuerta());
        assertEquals(puerta,celda.getPuerta());
    }

    @Test
    void entidadFuncionamiento() {
        Celda celda=new Celda(TipoCelda.SUELO);

        assertFalse(celda.isOcupada());
        assertFalse(celda.tieneEntidad());

        Entidad entidad=new Enemigo("GATO",new Estadisticas(10,12,12,12));
        celda.setEntidad(entidad);

        assertTrue(celda.isOcupada());
        assertTrue(celda.tieneEntidad());
        assertEquals(entidad,celda.getEntidad());
    }

    @Test
    void objetoFuncionamiento() {
        Celda celda=new Celda(TipoCelda.SUELO);

        assertFalse(celda.tieneObjeto());

        Objeto objeto=new Objeto();
        celda.setObjeto(objeto);

        assertTrue(celda.tieneObjeto());
        assertEquals(objeto,celda.getObjeto());
    }

    @Test
    void npcFuncionamiento() {
        Celda celda=new Celda(TipoCelda.SUELO);

        assertFalse(celda.tieneNPC());

        NPC npc=new NPC("Pedro", TipoNPC.ALDEANO);
        celda.setNpc(npc);

        assertTrue(celda.tieneNPC());
        assertEquals(npc,celda.getNpc());
    }

    @Test
    void esAdyacenteCorrecto() {
        Celda c1=new Celda(TipoCelda.SUELO);
        Celda c2=new Celda(TipoCelda.SUELO);

        c1.setRow(0);
        c1.setCol(0);

        c2.setRow(0);
        c2.setCol(1);

        assertTrue(c1.esAdyacente(c2));
    }

    @Test
    void noEsAdyacente() {
        Celda c1=new Celda(TipoCelda.SUELO);
        Celda c2=new Celda(TipoCelda.SUELO);

        c1.setRow(0);
        c1.setCol(0);

        c2.setRow(3);
        c2.setCol(3);

        assertFalse(c1.esAdyacente(c2));
    }

    @Test
    void esAdyacenteDistancia() {
        Celda c1=new Celda(TipoCelda.SUELO);
        Celda c2=new Celda(TipoCelda.SUELO);

        c1.setRow(0);
        c1.setCol(0);

        c2.setRow(0);
        c2.setCol(2);

        assertTrue(c1.esAdyacenteDistancia(c2,2));
    }
}