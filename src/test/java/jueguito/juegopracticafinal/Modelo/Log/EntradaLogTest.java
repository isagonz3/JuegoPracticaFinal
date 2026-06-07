package jueguito.juegopracticafinal.Modelo.Log;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EntradaLogTest {

    @Test
    void constructorAsignaMensaje() {
        EntradaLog entrada = new EntradaLog("test message");
        assertEquals("test message", entrada.getMensaje());
    }

    @Test
    void toStringDevuelveMensaje() {
        EntradaLog entrada = new EntradaLog("log entry");
        assertEquals("log entry", entrada.toString());
    }

    @Test
    void mensajeVacioEsValido() {
        EntradaLog entrada = new EntradaLog("");
        assertEquals("", entrada.getMensaje());
    }
}
