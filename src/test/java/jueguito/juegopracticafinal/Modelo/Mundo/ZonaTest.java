package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.TADs.Lista;
import jueguito.juegopracticafinal.TADs.Matrix;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZonaTest {

    private Zona zona;

    @BeforeEach
    void setUp() {
        zona = new Zona(0, "Habitacion", 5, 7);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++)
                zona.setCelda(i, j, new Celda(TipoCelda.SUELO));
    }

    @Test void constructorConCeldas() {
        Celda[][] celdas = new Celda[3][4];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        Zona z = new Zona(1, "Test", celdas);
        assertEquals(1, z.getIdZona());
        assertEquals("Test", z.getNombreZona());
        assertEquals(3, z.getRows());
        assertEquals(4, z.getCols());
    }

    @Test void getIdZona() { assertEquals(0, zona.getIdZona()); }
    @Test void setIdZona() { zona.setIdZona(5); assertEquals(5, zona.getIdZona()); }
    @Test void getNombreZona() { assertEquals("Habitacion", zona.getNombreZona()); }
    @Test void setNombreZona() { zona.setNombreZona("Bosque"); assertEquals("Bosque", zona.getNombreZona()); }
    @Test void getRows() { assertEquals(5, zona.getRows()); }
    @Test void getCols() { assertEquals(7, zona.getCols()); }
    @Test void getCountTurnos() { assertEquals(0, zona.getCountTurnos()); }
    @Test void setCountTurnos() { zona.setCountTurnos(3); assertEquals(3, zona.getCountTurnos()); }
    @Test void isVisitada() { assertFalse(zona.isVisitada()); }
    @Test void setVisitada() { zona.setVisitada(true); assertTrue(zona.isVisitada()); }

    @Test void spawnJugador() {
        Posicion p = new Posicion(2, 3);
        zona.setSpawnJugador(p);
        assertEquals(p, zona.getSpawnJugador());
    }

    @Test void spawnEnemigos() {
        Lista<Posicion> spawns = zona.getSpawnEnemigos();
        assertNotNull(spawns);
        assertTrue(spawns.isEmpty());
    }

    @Test void spawnObjetos() {
        Lista<Posicion> spawns = zona.getSpawnObjetos();
        assertNotNull(spawns);
    }

    @Test void spawnNPCs() {
        Lista<Posicion> spawns = zona.getSpawnNPCs();
        assertNotNull(spawns);
    }

    @Test void getCelda() {
        Celda c = zona.getCelda(1, 2);
        assertNotNull(c);
        assertEquals(TipoCelda.SUELO, c.getTipoCelda());
    }

    @Test void getCeldaInvalida() {
        assertNull(zona.getCelda(-1, 0));
        assertNull(zona.getCelda(10, 10));
    }

    @Test void setCelda() {
        Celda nueva = new Celda(TipoCelda.PARED);
        zona.setCelda(2, 3, nueva);
        assertEquals(TipoCelda.PARED, zona.getCelda(2, 3).getTipoCelda());
    }

    @Test void esValida() {
        assertTrue(zona.esValida(0, 0));
        assertTrue(zona.esValida(4, 6));
        assertFalse(zona.esValida(-1, 0));
        assertFalse(zona.esValida(0, 7));
        assertFalse(zona.esValida(5, 0));
    }

    @Test void puertas() {
        assertTrue(zona.getPuertas().isEmpty());
        Celda c = zona.getCelda(2, 2);
        c.setTipoCelda(TipoCelda.PUERTA);
        zona.addPuerta(c);
        assertEquals(1, zona.getPuertas().getSize());
        assertEquals(c, zona.getPuertas().get(0));
    }

    @Test void getCeldasVecinas() {
        Lista<Celda> vecinas = zona.getCeldasVecinas(2, 2);
        assertEquals(4, vecinas.getSize());
    }

    @Test void getCeldasVecinasConParedes() {
        zona.getCelda(1, 2).setTipoCelda(TipoCelda.PARED);
        Lista<Celda> vecinas = zona.getCeldasVecinas(2, 2);
        assertEquals(3, vecinas.getSize());
    }

    @Test void getCeldasVecinasEsquina() {
        Lista<Celda> vecinas = zona.getCeldasVecinas(0, 0);
        assertEquals(2, vecinas.getSize());
    }

    @Test void getCeldasAccesibles() {
        Lista<Celda> acc = zona.getCeldasAccesibles(2, 2, 1);
        assertEquals(4, acc.getSize());
    }

    @Test void getCeldasAccesiblesRangoCero() {
        Lista<Celda> acc = zona.getCeldasAccesibles(2, 2, 0);
        assertTrue(acc.isEmpty());
    }

    @Test void getCeldasAccesiblesRangoInvalido() {
        Lista<Celda> acc = zona.getCeldasAccesibles(-1, 0, 3);
        assertTrue(acc.isEmpty());
    }

    @Test void getCeldasAccesiblesConOcupadas() {
        Gato g = new Gato("G", new Estadisticas(10, 0, 0, 4));
        g.setPosicion(new Posicion(2, 3));
        zona.getCelda(2, 3).setEntidad(g);
        Lista<Celda> acc = zona.getCeldasAccesibles(2, 2, 1);
        assertEquals(3, acc.getSize());
    }

    @Test void numCeldasAccesibles() {
        assertEquals(35, zona.numCeldasAccesibles());
        zona.getCelda(0, 0).setTipoCelda(TipoCelda.PARED);
        assertEquals(34, zona.numCeldasAccesibles());
    }

    @Test void getCaminoMinimoDirecto() {
        Lista<Celda> camino = zona.getCaminoMinimo(2, 2, 2, 4);
        assertEquals(3, camino.getSize());
    }

    @Test void getCaminoMinimoInalcanzable() {
        Lista<Celda> camino = zona.getCaminoMinimo(-1, 0, 2, 2);
        assertTrue(camino.isEmpty());
    }

    @Test void getCaminoMinimoMismaCelda() {
        Lista<Celda> camino = zona.getCaminoMinimo(2, 2, 2, 2);
        assertEquals(1, camino.getSize());
        assertEquals(zona.getCelda(2, 2), camino.get(0));
    }

    @Test void getCaminoMinimoDestinoInvalido() {
        Lista<Celda> camino = zona.getCaminoMinimo(2, 2, -1, 0);
        assertTrue(camino.isEmpty());
    }

    @Test void getMatrix() {
        assertNotNull(zona.getMatrix());
    }

    @Test void setMatrix() {
        Matrix<Celda> m = new Matrix<>(3, 3);
        zona.setMatrix(m);
        assertEquals(3, zona.getRows());
    }

    @Test void constructorConCeldasActualizaCoordenadas() {
        Celda[][] celdas = new Celda[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        Zona z = new Zona(0, "Test", celdas);
        assertNotNull(z.getCelda(0, 0));
    }
}
