package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.TADs.Lista;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZonaTest {

    @Test
    void deberiaCrearZonaCorrectamente() {
        Zona zona=new Zona(1,"Bosque",3,3);

        assertEquals(1,zona.getIdZona());
        assertEquals("Bosque",zona.getNombreZona());
        assertEquals(3,zona.getRows());
        assertEquals(3,zona.getCols());
    }

    @Test
    void esValidaDebeFuncionar() {
        Zona zona=new Zona(1,"Test",3,3);

        assertTrue(zona.esValida(0,0));
        assertFalse(zona.esValida(10,10));
    }

    @Test
    void setYGetSpawnJugador() {
        Zona zona=new Zona(1,"Test",3,3);

        Posicion p=new Posicion(1,1);
        zona.setSpawnJugador(p);

        assertEquals(p,zona.getSpawnJugador());
    }

    @Test
    void numCeldasAccesiblesDebeContarTransitable() {
        Zona zona=new Zona(1,"Test",2,2);

        Celda[][] celdas=new Celda[2][2];
        celdas[0][0]=new Celda(TipoCelda.SUELO);
        celdas[0][1]=new Celda(TipoCelda.PARED);
        celdas[1][0]=new Celda(TipoCelda.SUELO);
        celdas[1][1]=new Celda(TipoCelda.SUELO);

        Zona zona2=new Zona(1,"Test",celdas);

        assertEquals(3,zona2.numCeldasAccesibles());
    }

    @Test
    void getCeldaDebeDevolverCeldaCorrecta() {
        Celda[][] celdas=new Celda[1][1];
        celdas[0][0]=new Celda(TipoCelda.SUELO);

        Zona zona=new Zona(1,"Test",celdas);

        assertNotNull(zona.getCelda(0,0));
    }

    @Test
    void getCeldasAccesiblesDebeDevolverAlgo() {
        Celda[][] celdas=new Celda[3][3];

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                celdas[i][j]=new Celda(TipoCelda.SUELO);
            }
        }

        Zona zona=new Zona(1,"Test",celdas);

        var lista=zona.getCeldasAccesibles(1,1,1);

        assertFalse(lista.isEmpty());
    }

    @Test
    void caminoMinimoDebeEncontrarRuta() {
        Celda[][] celdas=new Celda[3][3];

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                celdas[i][j]=new Celda(TipoCelda.SUELO);
            }
        }

        Zona zona=new Zona(1,"Test",celdas);

        Lista<Celda> camino=zona.getCaminoMinimo(0,0,2,2);

        assertFalse(camino.isEmpty());
    }

    @Test
    void caminoMinimoInvalidoDebeDevolverListaVacia() {
        Zona zona=new Zona(1,"Test",3,3);

        Lista<Celda> camino=zona.getCaminoMinimo(0,0,10,10);

        assertTrue(camino.isEmpty());
    }

    @Test
    void getYSetCountTurnos() {
        Zona zona=new Zona(1,"Test",3,3);
        zona.setCountTurnos(5);
        assertEquals(5,zona.getCountTurnos());
    }

    @Test
    void visitadaDebeCambiarEstado() {
        Zona zona=new Zona(1,"Test",3,3);

        assertFalse(zona.isVisitada());

        zona.setVisitada(true);

        assertTrue(zona.isVisitada());
    }

    @Test
    void spawnEnemigosNoDebeSerNull() {
        Zona zona=new Zona(1,"Test",3,3);

        assertNotNull(zona.getSpawnEnemigos());
        assertTrue(zona.getSpawnEnemigos().isEmpty());
    }

    @Test
    void spawnObjetosNoDebeSerNull() {
        Zona zona=new Zona(1,"Test",3,3);

        assertNotNull(zona.getSpawnObjetos());
        assertTrue(zona.getSpawnObjetos().isEmpty());
    }

    @Test
    void addPuertaDebeAñadirCelda() {
        Zona zona=new Zona(1,"Test",3,3);

        Celda celda=new Celda(TipoCelda.SUELO);

        zona.addPuerta(celda);

        assertNotNull(zona);
    }
}