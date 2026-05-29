package jueguito.juegopracticafinal.Modelo.Mundo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PosicionTest {

    @Test
    void getRowYGetColFuncionan() {
        Posicion p=new Posicion(3,5);

        assertEquals(3,p.getRow());
        assertEquals(5,p.getCol());
    }

    @Test
    void equalsMismoValorDebeSerTrue() {
        Posicion p1=new Posicion(1,2);
        Posicion p2=new Posicion(1,2);

        assertTrue(p1.equals(p2));
    }

    @Test
    void equalsDistintosValoresDebeSerFalse() {
        Posicion p1=new Posicion(1,2);
        Posicion p2=new Posicion(3,4);

        assertFalse(p1.equals(p2));
    }

    @Test
    void equalsConNullDebeSerFalse() {
        Posicion p=new Posicion(1,2);

        assertFalse(p.equals(null));
    }

    @Test
    void equalsConOtroObjetoDebeSerFalse() {
        Posicion p=new Posicion(1,2);

        assertFalse(p.equals("hola"));
    }

    @Test
    void toStringDebeFuncionarCorrectamente() {
        Posicion p=new Posicion(1,2);

        assertEquals("0-1-2",p.toString());
    }
}