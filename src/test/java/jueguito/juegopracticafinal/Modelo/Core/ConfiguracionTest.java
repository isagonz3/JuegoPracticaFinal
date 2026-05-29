package jueguito.juegopracticafinal.Modelo.Core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionTest {

    @Test
    void singletonDevuelveSiempreLaMismaInstancia() {
        Configuracion c1=Configuracion.get();
        Configuracion c2=Configuracion.get();

        assertSame(c1, c2);
    }

    @Test
    void configuracionNoEsNull() {
        Configuracion cfg=Configuracion.get();

        assertNotNull(cfg);
        assertNotNull(cfg.jugador);
        assertNotNull(cfg.gato);
    }

    @Test
    void valoresJugadorSonCorrectos() {
        Configuracion cfg=Configuracion.get();

        assertTrue(cfg.jugador.vidaMax>0);
        assertTrue(cfg.jugador.ataqueBase>=0);
    }

    @Test
    void zonasPermitidasNoEstaVacio() {
        Configuracion cfg=Configuracion.get();

        assertTrue(cfg.gato.zonasPermitidas.length>0);
    }
}