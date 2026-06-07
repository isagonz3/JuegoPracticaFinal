package jueguito.juegopracticafinal.Modelo.Entidades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GatoTest {

    @Test
    void constructorDebeAsignarNombreYEstadisticas() {
        Estadisticas stats = new Estadisticas(10, 5, 2, 1);
        Gato gato = new Gato("Gatiko", stats);

        assertEquals("Gatiko", gato.getNombre());
        assertEquals(stats, gato.getEstadisticas());
    }

    @Test
    void getTipoEntidadDebeDevolverEntidad() {
        Gato gato = new Gato("Michi", new Estadisticas(8, 3, 1, 2));

        assertEquals("ENTIDAD", gato.getTipoEntidad());
    }

    @Test
    void estarVivoConVidaPositivaDebeSerTrue() {
        Gato gato = new Gato("Tom", new Estadisticas(10, 5, 2, 1));

        assertTrue(gato.estarVivo());
    }

    @Test
    void recibirAtaqueDebeReducirVida() {
        Gato gato = new Gato("Felix", new Estadisticas(10, 5, 2, 1));
        int vidaAntes = gato.getEstadisticas().getVidaActual();

        gato.recibirAtaque(4);

        assertTrue(gato.getEstadisticas().getVidaActual() < vidaAntes);
    }
}
