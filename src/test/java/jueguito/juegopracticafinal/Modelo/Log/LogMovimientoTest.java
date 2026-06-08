package jueguito.juegopracticafinal.Modelo.Log;

import static org.junit.jupiter.api.Assertions.*;

import jueguito.juegopracticafinal.Modelo.Excepciones.DeshacerMovimientoInvalido;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import org.junit.jupiter.api.Test;

class LogMovimientoTest {

    @Test
    void registrarAnyadeEntrada() {
        LogMovimiento log = new LogMovimiento();
        log.registrar("mensaje de prueba");
        assertEquals(1, log.getEntradas().getSize());
    }

    @Test
    void getUltimaEntradaDevuelveUltimoMensaje() {
        LogMovimiento log = new LogMovimiento();
        log.registrar("primero");
        log.registrar("segundo");
        assertEquals("segundo", log.getUltimaEntrada());
    }

    @Test
    void getUltimaEntradaLogVacioDevuelveVacio() {
        LogMovimiento log = new LogMovimiento();
        assertEquals("", log.getUltimaEntrada());
    }

    @Test
    void registrarMovimientoYDeshacerDevuelvePosicionAnterior() {
        LogMovimiento log = new LogMovimiento();
        Zona zona = new Zona(1, "TestZona", 3, 3);
        Posicion anterior = new Posicion(0, 0);
        log.registrarMovimiento(anterior, zona);
        Posicion resultado = log.deshacerMovimiento(zona);
        assertEquals(anterior, resultado);
    }

    @Test
    void deshacerMovimientoConZonaDistintaLanzaExcepcion() {
        LogMovimiento log = new LogMovimiento();
        Zona zona1 = new Zona(1, "Zona1", 3, 3);
        zona1.setIdZona(1);
        Zona zona2 = new Zona(2, "Zona2", 3, 3);
        zona2.setIdZona(2);
        log.registrarMovimiento(new Posicion(0, 0), zona1);
        assertThrows(DeshacerMovimientoInvalido.class, () -> log.deshacerMovimiento(zona2));
    }

    @Test
    void deshacerMovimientoConZonaNullLanzaExcepcion() {
        LogMovimiento log = new LogMovimiento();
        Zona zona = new Zona(1, "Zona", 3, 3);
        log.registrarMovimiento(new Posicion(0, 0), zona);
        assertThrows(DeshacerMovimientoInvalido.class, () -> log.deshacerMovimiento(null));
    }

    @Test
    void deshacerMovimientoPilaVaciaDevuelveNull() {
        LogMovimiento log = new LogMovimiento();
        Zona zona = new Zona(1, "Zona", 3, 3);
        assertNull(log.deshacerMovimiento(zona));
    }

    @Test
    void deshacerMovimientoMultipleDesapilaEnOrdenInverso() {
        LogMovimiento log = new LogMovimiento();
        Zona zona = new Zona(1, "Zona", 3, 3);
        log.registrarMovimiento(new Posicion(0, 0), zona);
        log.registrarMovimiento(new Posicion(1, 1), zona);
        Posicion primera = log.deshacerMovimiento(zona);
        Posicion segunda = log.deshacerMovimiento(zona);
        assertEquals(new Posicion(1, 1), primera);
        assertEquals(new Posicion(0, 0), segunda);
    }

    @Test
    void getEntradasNoEsNull() {
        LogMovimiento log = new LogMovimiento();
        assertNotNull(log.getEntradas());
        assertTrue(log.getEntradas().isEmpty());
    }

    @Test
    void deshacerMovimientoAnyadeEntradaAlLog() {
        LogMovimiento log = new LogMovimiento();
        Zona zona = new Zona(1, "Zona", 3, 3);
        log.registrarMovimiento(new Posicion(0, 0), zona);
        log.deshacerMovimiento(zona);
        assertEquals("Movimiento deshecho", log.getUltimaEntrada());
    }

    @Test
    void registrarMovimientoActualizaUltimaZona() {
        LogMovimiento log = new LogMovimiento();
        Zona zona = new Zona(1, "Zona", 3, 3);
        log.registrarMovimiento(new Posicion(0, 0), zona);
        Posicion resultado = log.deshacerMovimiento(zona);
        assertNotNull(resultado);
    }

    @Test
    void deshacerUltimoMovimientoLimpiaUltimaZona() {
        LogMovimiento log = new LogMovimiento();
        Zona zona = new Zona(1, "Zona", 3, 3);
        log.registrarMovimiento(new Posicion(0, 0), zona);
        log.deshacerMovimiento(zona);
        assertNull(log.deshacerMovimiento(zona));
    }

    @Test
    void reiniciarMovimientosLimpiaPilaYDespuesNoSePuedeDeshacer() {
        LogMovimiento log = new LogMovimiento();
        Zona zona = new Zona(1, "Zona", 3, 3);
        log.registrarMovimiento(new Posicion(0, 0), zona);

        log.reiniciarMovimientos();

        assertNull(log.deshacerMovimiento(zona));
    }

    @Test
    void reiniciarMovimientosPilaVaciaNoRompe() {
        LogMovimiento log = new LogMovimiento();
        assertDoesNotThrow(log::reiniciarMovimientos);
    }

    @Test
    void reiniciarMovimientosNoAfectaEntradasLog() {
        LogMovimiento log = new LogMovimiento();
        log.registrar("mensaje antes");
        Zona zona = new Zona(1, "Zona", 3, 3);
        log.registrarMovimiento(new Posicion(0, 0), zona);

        log.reiniciarMovimientos();

        assertEquals(1, log.getEntradas().getSize());
        assertEquals("mensaje antes", log.getUltimaEntrada());
    }
}
