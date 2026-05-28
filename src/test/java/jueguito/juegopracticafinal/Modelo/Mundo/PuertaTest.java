package jueguito.juegopracticafinal.Modelo.Mundo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PuertaTest {

    @Test
    void gettersFuncionan() {
        Puerta puerta=new Puerta(1,2,10,20,30,40);

        assertEquals(10,puerta.getXOrigen());
        assertEquals(20,puerta.getYOrigen());
        assertEquals(30,puerta.getXDestino());
        assertEquals(40,puerta.getYDestino());
        assertEquals(1,puerta.getZonaOrigen());
        assertEquals(2,puerta.getZonaDestino());
    }

    @Test
    void constructorVacioValoresPorDefecto() {
        Puerta puerta=new Puerta();

        assertEquals(0,puerta.getXOrigen());
        assertEquals(0,puerta.getYOrigen());
        assertEquals(0,puerta.getXDestino());
        assertEquals(0,puerta.getYDestino());
        assertEquals(0,puerta.getZonaOrigen());
        assertEquals(0,puerta.getZonaDestino());
    }

    @Test
    void compareToPorZonaOrigen() {
        Puerta p1=new Puerta(1,2,0,0,0,0);
        Puerta p2=new Puerta(2,2,0,0,0,0);

        assertTrue(p1.compareTo(p2)<0);
    }

    @Test
    void compareToPorZonaDestino() {
        Puerta p1=new Puerta(1,1,0,0,0,0);
        Puerta p2=new Puerta(1,2,0,0,0,0);

        assertTrue(p1.compareTo(p2)<0);
    }

    @Test
    void compareToPorXOrigen() {
        Puerta p1=new Puerta(1,1,1,0,0,0);
        Puerta p2=new Puerta(1,1,2,0,0,0);

        assertTrue(p1.compareTo(p2)<0);
    }

    @Test
    void compareToIgualesDebeDarCero() {
        Puerta p1=new Puerta(1,1,1,1,1,1);
        Puerta p2=new Puerta(1,1,1,1,1,1);

        assertEquals(0,p1.compareTo(p2));
    }
}