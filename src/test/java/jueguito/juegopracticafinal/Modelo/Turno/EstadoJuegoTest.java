package jueguito.juegopracticafinal.Modelo.Turno;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoJuegoTest {

    @Test
    void enumeracionDebeContenerValoresEsperados() {
        assertNotNull(EstadoJuego.valueOf("EN_CURSO"));
        assertNotNull(EstadoJuego.valueOf("VICTORIA"));
        assertNotNull(EstadoJuego.valueOf("DERROTA"));
        assertNotNull(EstadoJuego.valueOf("MENU"));
    }

    @Test
    void estadoGlobalInicialDebeSerMENU() {
        assertEquals(EstadoJuego.MENU, EstadoJuego.getEstadoGlobal());
    }

    @Test
    void setEstadoGlobalDebeCambiarEstado() {
        EstadoJuego.setEstadoGlobal(EstadoJuego.EN_CURSO);

        assertEquals(EstadoJuego.EN_CURSO, EstadoJuego.getEstadoGlobal());
    }
}
