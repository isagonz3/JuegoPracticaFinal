package jueguito.juegopracticafinal.Modelo.Entidades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticasTest {

    @Test
    void constructorDebeAsignarValoresCorrectamente() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        assertEquals(100,stats.getVidaMax());
        assertEquals(100,stats.getVidaActual());
        assertEquals(20,stats.getAtaqueBase());
        assertEquals(10,stats.getDefensaBase());
        assertEquals(5,stats.getRangoMov());
        assertEquals(5,stats.getPuntosMovDisponibles());
    }

    @Test
    void setVidaActualDebeFuncionar() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.setVidaActual(50);

        assertEquals(50,stats.getVidaActual());
    }

    @Test
    void setPuntosMovDisponiblesDebeFuncionar() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.setPuntosMovDisponibles(2);

        assertEquals(2,stats.getPuntosMovDisponibles());
    }

    @Test
    void estaVivoDebeSerTrueConVidaMayorACero() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        assertTrue(stats.estaVivo());
    }

    @Test
    void estaVivoDebeSerFalseConVidaCero() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.setVidaActual(0);

        assertFalse(stats.estaVivo());
    }

    @Test
    void usarMovimientoDebeReducirPuntos() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        assertTrue(stats.usarMovimiento(2));
        assertEquals(3,stats.getPuntosMovDisponibles());
    }

    @Test
    void usarMovimientoDebeFallarSiPuntosInvalidos() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        assertFalse(stats.usarMovimiento(0));
        assertFalse(stats.usarMovimiento(-1));
        assertFalse(stats.usarMovimiento(10));
    }

    @Test
    void curarVidaDebeRecuperarVida() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.setVidaActual(50);

        stats.curarVida(20);

        assertEquals(70,stats.getVidaActual());
    }

    @Test
    void curarVidaNoDebeSuperarVidaMaxima() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.setVidaActual(90);

        stats.curarVida(50);

        assertEquals(100,stats.getVidaActual());
    }

    @Test
    void aumentarAtaqueDebeFuncionar() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.aumentarAtaque(5);

        assertEquals(25,stats.getAtaqueBase());
    }

    @Test
    void aumentarDefensaDebeFuncionar() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.aumentarDefensa(3);

        assertEquals(13,stats.getDefensaBase());
    }

    @Test
    void aumentarRangoMovimientoDebeFuncionar() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.aumentarRangoMov(2);

        assertEquals(7,stats.getRangoMov());
    }

    @Test
    void recibirAtaqueDebeReducirVidaSegunDefensa() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.recibirAtaque(20);

        assertEquals(90,stats.getVidaActual());
    }

    @Test
    void recibirAtaqueNoDebeDejarVidaNegativa() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.recibirAtaque(1000);

        assertEquals(0,stats.getVidaActual());
    }

    @Test
    void restarVidaDirectaDebeReducirVida() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.restarVidaDirecta(30);

        assertEquals(70,stats.getVidaActual());
    }

    @Test
    void restarVidaDirectaNoDebeDejarVidaNegativa() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        stats.restarVidaDirecta(500);

        assertEquals(0,stats.getVidaActual());
    }

    @Test
    void toStringNoDebeSerNull() {
        Estadisticas stats=new Estadisticas(100,20,10,5);

        assertNotNull(stats.toString());
    }
}