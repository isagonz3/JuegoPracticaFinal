package jueguito.juegopracticafinal.Modelo.Log;

import jueguito.juegopracticafinal.TADs.Lista;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EntradaLogTest {

    @Test void constructor() {
        EntradaLog e = new EntradaLog("Mensaje de prueba");
        assertEquals("Mensaje de prueba", e.getMensaje());
    }

    @Test void toStringDevuelveMensaje() {
        EntradaLog e = new EntradaLog("Hola");
        assertEquals("Hola", e.toString());
    }
}

class LogSistemaTest {

    @Test void registrar() {
        LogSistema log = new LogSistema();
        log.registrar("Entrada 1");
        log.registrar("Entrada 2");
        Lista<EntradaLog> entradas = log.getEntradas();
        assertEquals(2, entradas.getSize());
        assertEquals("Entrada 1", entradas.get(0).getMensaje());
        assertEquals("Entrada 2", entradas.get(1).getMensaje());
    }

    @Test void getUltimaEntrada() {
        LogSistema log = new LogSistema();
        assertEquals("", log.getUltimaEntrada());
        log.registrar("Primera");
        assertEquals("Primera", log.getUltimaEntrada());
        log.registrar("Segunda");
        assertEquals("Segunda", log.getUltimaEntrada());
    }

    @Test void getEntradas() {
        LogSistema log = new LogSistema();
        assertNotNull(log.getEntradas());
        assertTrue(log.getEntradas().isEmpty());
        log.registrar("X");
        assertEquals(1, log.getEntradas().getSize());
    }

    @Test void multipleEntradas() {
        LogSistema log = new LogSistema();
        for (int i = 0; i < 10; i++) log.registrar("Msg " + i);
        assertEquals(10, log.getEntradas().getSize());
        assertEquals("Msg 9", log.getUltimaEntrada());
    }
}
