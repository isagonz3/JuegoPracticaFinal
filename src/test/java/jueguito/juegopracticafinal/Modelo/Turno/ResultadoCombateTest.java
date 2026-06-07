package jueguito.juegopracticafinal.Modelo.Turno;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoCombateTest {

    @Test
    void constructorConEnemigoVivoNoDebeFallar() {
        ResultadoCombate rc = new ResultadoCombate(10, 20, false, "Golpe acertado");

        assertNotNull(rc);
    }

    @Test
    void constructorConEnemigoDerrotadoNoDebeFallar() {
        ResultadoCombate rc = new ResultadoCombate(50, 0, true, "Enemigo derrotado");

        assertNotNull(rc);
    }

    @Test
    void constructorConDanioCeroNoDebeFallar() {
        ResultadoCombate rc = new ResultadoCombate(0, 30, false, "Fallaste");

        assertNotNull(rc);
    }

    @Test
    void constructorConMensajeNullNoDebeFallar() {
        ResultadoCombate rc = new ResultadoCombate(5, 15, false, null);

        assertNotNull(rc);
    }
}
