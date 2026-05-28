package jueguito.juegopracticafinal.Modelo.Entidades;

import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntidadTest {

    static class EntidadPrueba extends Entidad {

        public EntidadPrueba(String nombre, Estadisticas estadisticas, Posicion posicion) {
            super(nombre, estadisticas, posicion);
        }

        public EntidadPrueba(String nombre, Estadisticas estadisticas) {
            super(nombre, estadisticas);
        }

        public EntidadPrueba() {
            super();
        }

        @Override
        public String getTipoEntidad() {
            return "PRUEBA";
        }
    }

    @Test
    void constructorDebeAsignarValoresCorrectamente() {
        Estadisticas stats=new Estadisticas(10,5,2,1);
        Posicion pos=new Posicion(1,1);

        EntidadPrueba entidad=new EntidadPrueba("Heroe",stats,pos);

        assertEquals("Heroe",entidad.getNombre());
        assertEquals(stats,entidad.getEstadisticas());
        assertEquals(pos,entidad.getPosicion());
    }

    @Test
    void constructorSinPosicionDebeAsignarNull() {
        Estadisticas stats=new Estadisticas(10,5,2,1);

        EntidadPrueba entidad=new EntidadPrueba("Orco",stats);

        assertNull(entidad.getPosicion());
    }

    @Test
    void constructorVacioNoDebeRomper() {
        EntidadPrueba entidad=new EntidadPrueba();

        assertNotNull(entidad);
    }

    @Test
    void setPosicionDebeCambiarPosicion() {
        EntidadPrueba entidad=new EntidadPrueba();

        Posicion pos=new Posicion(3,4);

        entidad.setPosicion(pos);

        assertEquals(pos,entidad.getPosicion());
    }

    @Test
    void estarVivoDebeSerTrueSiTieneVida() {
        Estadisticas stats=new Estadisticas(10,5,2,1);

        EntidadPrueba entidad=new EntidadPrueba("Heroe",stats);

        assertTrue(entidad.estarVivo());
    }

    @Test
    void estarVivoDebeSerFalseSiNoTieneEstadisticas() {
        EntidadPrueba entidad=new EntidadPrueba();

        assertFalse(entidad.estarVivo());
    }

    @Test
    void recibirAtaqueDebeReducirVida() {
        Estadisticas stats=new Estadisticas(10,5,2,1);

        EntidadPrueba entidad=new EntidadPrueba("Heroe",stats);

        entidad.recibirAtaque(5);

        assertTrue(entidad.getEstadisticas().getVidaActual()<10);
    }

    @Test
    void getAtaqueTotalDebeDevolverAtaqueBase() {
        Estadisticas stats=new Estadisticas(10,7,2,1);

        EntidadPrueba entidad=new EntidadPrueba("Heroe",stats);

        assertEquals(7,entidad.getAtaqueTotal());
    }

    @Test
    void getDefensaTotalDebeDevolverDefensaBase() {
        Estadisticas stats=new Estadisticas(10,7,4,1);

        EntidadPrueba entidad=new EntidadPrueba("Heroe",stats);

        assertEquals(4,entidad.getDefensaTotal());
    }

    @Test
    void moverADebeCambiarPosicion() {
        EntidadPrueba entidad=new EntidadPrueba();

        Posicion nueva=new Posicion(5,5);

        assertTrue(entidad.moverA(nueva));
        assertEquals(nueva,entidad.getPosicion());
    }

    @Test
    void moverANullDebeDevolverFalse() {
        EntidadPrueba entidad=new EntidadPrueba();

        assertFalse(entidad.moverA(null));
    }

    @Test
    void getTipoEntidadDebeFuncionar() {
        EntidadPrueba entidad=new EntidadPrueba();

        assertEquals("PRUEBA",entidad.getTipoEntidad());
    }

    @Test
    void toStringNoDebeSerNull() {
        Estadisticas stats=new Estadisticas(10,5,2,1);

        EntidadPrueba entidad=new EntidadPrueba("Heroe",stats);

        assertNotNull(entidad.toString());
    }
}