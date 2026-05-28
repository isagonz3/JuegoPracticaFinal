package jueguito.juegopracticafinal.Modelo.Entidades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemigoTest {

    @Test
    void constructorDebeAsignarNombreYEstadisticas() {
        Estadisticas stats=new Estadisticas(10,5,2,1);

        Enemigo enemigo=new Enemigo("Soldado",stats);

        assertEquals("Soldado",enemigo.getNombre());
        assertEquals(stats,enemigo.getEstadisticas());
    }

    @Test
    void getTipoEntidadDebeDevolverEnemigo() {
        Enemigo enemigo=new Enemigo("djkfhs",new Estadisticas(5,3,1,1));

        assertEquals("ENEMIGO",enemigo.getTipoEntidad());
    }

    @Test
    void atacadoPorDefectoDebeSerFalse() {
        Enemigo enemigo=new Enemigo("Slime",new Estadisticas(1,1,1,1));

        assertFalse(enemigo.isAtacado());
    }

    @Test
    void setAtacadoDebeCambiarEstado() {
        Enemigo enemigo=new Enemigo("Enemigo1",new Estadisticas(50,20,10,5));

        enemigo.setAtacado(true);

        assertTrue(enemigo.isAtacado());
    }

    @Test
    void setAtacadoFalseDebeFuncionar() {
        Enemigo enemigo=new Enemigo("Enemigo2",new Estadisticas(8,4,2,2));
        enemigo.setAtacado(true);
        enemigo.setAtacado(false);

        assertFalse(enemigo.isAtacado());
    }
}