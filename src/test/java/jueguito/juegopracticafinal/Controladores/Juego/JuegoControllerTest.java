package jueguito.juegopracticafinal.Controladores.Juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JuegoControllerTest {

    @Test
    void baseRowYBaseColDebenFuncionar() {
        JuegoController controller=new JuegoController();

        controller.setBaseRow(5);
        controller.setBaseCol(3);

        assertEquals(5,controller.getBaseRow());
        assertEquals(3,controller.getBaseCol());
    }

    @Test
    void gettersModulosDebenSerNullPorDefecto() {
        JuegoController controller=new JuegoController();

        assertNull(controller.getAcciones());
        assertNull(controller.getUiRenderer());
        assertNull(controller.getMapaRenderer());
    }

    @Test
    void gettersPartidaYAppDebenSerNullPorDefecto() {
        JuegoController controller=new JuegoController();

        assertNull(controller.getPartida());
        assertNull(controller.getApp());
    }
}