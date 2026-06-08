package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    @Test
    void guardarYCargarMantieneEstadoBasicoYInventario() throws Exception {
        File temp=File.createTempFile("partida_test", ".json");

        GrafoZonas grafo=new GrafoZonas();
        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)},
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)}
        });
        grafo.addZona(z);

        Partida p=new Partida(grafo);


        Jugador j=new Jugador("Test", new Estadisticas(10, 5, 5, 4), new Posicion(0, 0));
        p.setJugador(j);
        z.getCelda(0,0).setEntidad(j);


        Objeto espada=new Objeto("Espada", TipoObjeto.EQUIPABLE, 2, 1, 0, 0, 3, null);
        espada.setSlot(SlotEquipable.ARMA);
        j.getInventario().addObjeto(espada);


        Gato g=new Gato("PutoGato", new Estadisticas(10, 0, 0, 4));
        p.setGato(g);

        p.setZonaActual(z);
        p.setIdZonaActual(0);
        p.setEstadoActual(EstadoJuego.EN_CURSO);


        LoadJSON.guardarPartida(p, temp.getAbsolutePath());
        Partida cargada=LoadJSON.cargarPartida(temp.getAbsolutePath(), grafo);

        assertNotNull(cargada);
        Jugador jc=cargada.getJugador();
        assertEquals("Test", jc.getNombre());
        assertEquals(EstadoJuego.EN_CURSO, cargada.getEstadoActual());
        assertEquals(0, cargada.getIdZonaActual());
        assertTrue(jc.getInventario().size() >= 1);

        Objeto o=jc.getInventario().getObjeto(0);
        assertEquals("Espada", o.getNombre());
        assertEquals(TipoObjeto.EQUIPABLE, o.getTipo());
        assertEquals(2, o.getAtaqueBonus());
        assertEquals(3, o.getUsosRestantes());
        if (o.getSlot()!=null) {
            assertEquals(SlotEquipable.ARMA, o.getSlot());
        }
    }

    @Test
    void iniciarDebeInicializarCorrectamenteLaPartida() {
        GrafoZonas grafo=new GrafoZonas();

        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO),new Celda(TipoCelda.SUELO)},
                {new Celda(TipoCelda.SUELO),new Celda(TipoCelda.SUELO)}
        });

        grafo.addZona(z);

        Partida p=new Partida(grafo);

        p.iniciar();

        assertNotNull(p.getJugador());
        assertNotNull(p.getGato());
        assertNotNull(p.getZonaActual());

        assertEquals(0, p.getIdZonaActual());
        assertEquals(EstadoJuego.EN_CURSO,p.getEstadoActual());
        assertEquals(0, p.getTurnoActual());

        boolean jugadorEnMapa =
                z.getCelda(0, 0).getEntidad()==p.getJugador();
        assertTrue(jugadorEnMapa);
    }

    @Test
    void iniciarTurnoDebeResetearFaseJugadorYPuntosMovimiento() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)}
        });
        grafo.addZona(z);

        Partida p=new Partida(grafo);
        p.iniciar();

        p.getJugador().getEstadisticas().setPuntosMovDisponibles(0);

        p.iniciarTurno();

        assertTrue(p.isFaseJugadorActiva());
        assertEquals(p.getJugador().getRangoTotal(),p.getJugador().getEstadisticas().getPuntosMovDisponibles());
    }

    @Test
    void checkDerrotaDebeCambiarEstadoSiJugadorMuere() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO)}
        });
        grafo.addZona(z);

        Partida p = new Partida(grafo);
        p.iniciar();

        // Simulamos muerte del jugador
        p.getJugador().getEstadisticas().setVidaActual(0);

        p.terminarTurnoPublic();

        assertEquals(EstadoJuego.DERROTA, p.getEstadoActual());
    }

    @Test
    void checkDerrotaDebeCambiarEstadoSiSuperaMaxTurnos() {
        GrafoZonas grafo=new GrafoZonas();
        Zona z=new Zona(0, "Z0", new Celda[][]{
                {new Celda(TipoCelda.SUELO)}
        });
        grafo.addZona(z);

        Partida p=new Partida(grafo);
        p.iniciar();

        int maxTurnosSimulado = Configuracion.get().turno.maxTurnos;
        p.setTurnoActual(maxTurnosSimulado);

        p.terminarTurnoPublic();

        assertEquals(EstadoJuego.DERROTA, p.getEstadoActual());
    }

    // --- NUEVOS TESTS: usarMapa ---

    @Test
    void usarMapaConSalidaEnZonaActualCalculaCamino() {
        GrafoZonas grafo = new GrafoZonas();
        Celda[][] celdas = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        celdas[2][2] = new Celda(TipoCelda.SALIDA);
        Zona z = new Zona(0, "Z0", celdas);
        grafo.addZona(z);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.usarMapa();

        assertNotNull(p.getCaminoMapa());
        assertFalse(p.getCaminoMapa().isEmpty());
        assertTrue(p.isMapaActivo());
    }

    @Test
    void usarMapaSinSalidaNiRutaAlCastilloNoRompe() {
        GrafoZonas grafo = new GrafoZonas();
        Celda[][] celdas = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        Zona z = new Zona(0, "Z0", celdas);
        grafo.addZona(z);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.usarMapa();

        assertNull(p.getCaminoMapa());
        assertFalse(p.isMapaActivo());
    }

    @Test
    void usarMapaConRutaAlCastilloEncuentraPuerta() {
        GrafoZonas grafo = new GrafoZonas();

        Celda[][] celdas0 = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas0[i][j] = new Celda(TipoCelda.SUELO);
        Zona z0 = new Zona(0, "Z0", celdas0);

        Puerta p01 = new Puerta(0, 1, 0, 1, 0, 0);
        celdas0[1][0].setTipoCelda(TipoCelda.PUERTA);
        celdas0[1][0].setPuerta(p01);

        Celda[][] celdas1 = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas1[i][j] = new Celda(TipoCelda.SUELO);
        Zona z1 = new Zona(1, "Z1", celdas1);

        Puerta p19 = new Puerta(1, 9, 0, 0, 0, 0);
        celdas1[0][0].setTipoCelda(TipoCelda.PUERTA);
        celdas1[0][0].setPuerta(p19);

        Celda[][] celdas9 = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas9[i][j] = new Celda(TipoCelda.SUELO);
        Zona z9 = new Zona(9, "Castle", celdas9);

        grafo.addZona(z0);
        grafo.addZona(z1);
        grafo.addZona(z9);
        grafo.conectar(0, 1);
        grafo.conectar(1, 9);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.usarMapa();

        assertNotNull(p.getCaminoMapa());
        assertFalse(p.getCaminoMapa().isEmpty());
        assertTrue(p.isMapaActivo());
    }

    // --- NUEVOS TESTS: cambiarZona ---

    @Test
    void cambiarZonaPuertaNullNoCambiaEstado() {
        GrafoZonas grafo = new GrafoZonas();
        Celda[][] celdas = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        Zona z0 = new Zona(0, "Z0", celdas);
        grafo.addZona(z0);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.iniciarTurno();

        int idAntes = p.getIdZonaActual();
        assertDoesNotThrow(() -> p.cambiarZona(null));
        assertEquals(idAntes, p.getIdZonaActual());
    }

    @Test
    void cambiarZonaPuertaValidaCambiaZonaActual() {
        GrafoZonas grafo = new GrafoZonas();

        Celda[][] celdas0 = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas0[i][j] = new Celda(TipoCelda.SUELO);
        Zona z0 = new Zona(0, "Z0", celdas0);

        Celda[][] celdas1 = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas1[i][j] = new Celda(TipoCelda.SUELO);
        Zona z1 = new Zona(1, "Z1", celdas1);

        grafo.addZona(z0);
        grafo.addZona(z1);
        grafo.conectar(0, 1);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.iniciarTurno();

        Puerta puerta = new Puerta(0, 1, 0, 1, 0, 0);
        celdas0[1][0].setTipoCelda(TipoCelda.PUERTA);
        celdas0[1][0].setPuerta(puerta);

        assertDoesNotThrow(() -> p.cambiarZona(puerta));
        assertEquals(1, p.getIdZonaActual());
        assertSame(z1, p.getZonaActual());
    }

    @Test
    void cambiarZonaReiniciaPilaMovimientos() {
        GrafoZonas grafo = new GrafoZonas();
        Celda[][] celdas0 = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas0[i][j] = new Celda(TipoCelda.SUELO);
        Zona z0 = new Zona(0, "Z0", celdas0);
        Celda[][] celdas1 = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas1[i][j] = new Celda(TipoCelda.SUELO);
        Zona z1 = new Zona(1, "Z1", celdas1);
        grafo.addZona(z0);
        grafo.addZona(z1);
        grafo.conectar(0, 1);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.iniciarTurno();

        p.getLog().registrarMovimiento(new Posicion(0, 0), z0);

        Puerta puerta = new Puerta(0, 1, 0, 1, 0, 0);
        celdas0[1][0].setTipoCelda(TipoCelda.PUERTA);
        celdas0[1][0].setPuerta(puerta);

        assertDoesNotThrow(() -> p.cambiarZona(puerta));
        assertEquals(1, p.getIdZonaActual());

        assertNull(p.getLog().deshacerMovimiento(z1));
    }

    @Test
    void checkVictoriaConGatoEnCastilloCambiaEstado() {
        GrafoZonas grafo = new GrafoZonas();
        Celda[][] celdasInicial = new Celda[][]{
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)},
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)}
        };
        grafo.addZona(new Zona(0, "Casa", celdasInicial));
        Celda[][] celdas = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        Zona z = new Zona(9, "Castillo", celdas);
        grafo.addZona(z);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.setGatoEncontrado(true);
        p.setZonaActual(z);
        p.setIdZonaActual(9);
        p.setEstadoActual(EstadoJuego.EN_CURSO);

        p.terminarTurnoPublic();

        assertEquals(EstadoJuego.VICTORIA, p.getEstadoActual());
    }

    @Test
    void checkVictoriaSinGatoEnCastilloNoCambiaEstado() {
        GrafoZonas grafo = new GrafoZonas();
        Celda[][] celdasInicial = new Celda[][]{
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)},
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)}
        };
        grafo.addZona(new Zona(0, "Casa", celdasInicial));
        Celda[][] celdas = new Celda[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        Zona z = new Zona(9, "Castillo", celdas);
        grafo.addZona(z);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.setGatoEncontrado(false);
        p.setZonaActual(z);
        p.setIdZonaActual(9);
        p.setEstadoActual(EstadoJuego.EN_CURSO);

        p.terminarTurnoPublic();

        assertNotEquals(EstadoJuego.VICTORIA, p.getEstadoActual());
    }

    @Test
    void cambiarZonaAlCastilloConGatoDisparaVictoria() {
        GrafoZonas grafo = new GrafoZonas();

        Celda[][] celdas0 = new Celda[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                celdas0[i][j] = new Celda(TipoCelda.SUELO);
        Zona z0 = new Zona(0, "Casa", celdas0);
        grafo.addZona(z0);

        Celda[][] celdas9 = new Celda[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                celdas9[i][j] = new Celda(TipoCelda.SUELO);
        Zona z9 = new Zona(9, "Castillo", celdas9);
        grafo.addZona(z9);

        grafo.conectar(0, 9);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.iniciarTurno();
        p.setGatoEncontrado(true);

        Puerta puerta = new Puerta(0, 9, 0, 0, 0, 0);
        celdas0[0][0].setTipoCelda(TipoCelda.PUERTA);
        celdas0[0][0].setPuerta(puerta);

        assertDoesNotThrow(() -> p.cambiarZona(puerta));
        assertEquals(EstadoJuego.VICTORIA, p.getEstadoActual());
    }

    @Test
    void cambiarZonaAlCastilloSinGatoNoDisparaVictoria() {
        GrafoZonas grafo = new GrafoZonas();

        Celda[][] celdas0 = new Celda[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                celdas0[i][j] = new Celda(TipoCelda.SUELO);
        Zona z0 = new Zona(0, "Casa", celdas0);
        grafo.addZona(z0);

        Celda[][] celdas9 = new Celda[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                celdas9[i][j] = new Celda(TipoCelda.SUELO);
        Zona z9 = new Zona(9, "Castillo", celdas9);
        grafo.addZona(z9);

        grafo.conectar(0, 9);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.iniciarTurno();
        p.setGatoEncontrado(false);

        Puerta puerta = new Puerta(0, 9, 0, 0, 0, 0);
        celdas0[0][0].setTipoCelda(TipoCelda.PUERTA);
        celdas0[0][0].setPuerta(puerta);

        assertDoesNotThrow(() -> p.cambiarZona(puerta));
        assertNotEquals(EstadoJuego.VICTORIA, p.getEstadoActual());
    }

    @Test
    void accionCambioZonaIncrementaTurnoYReseteaFlags() {
        GrafoZonas grafo = new GrafoZonas();
        Celda[][] celdas = new Celda[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        Zona z = new Zona(0, "Z0", celdas);
        grafo.addZona(z);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.setEstadoActual(EstadoJuego.EN_CURSO);
        p.setAccionRealizada(true);
        int turnoAntes = p.getTurnoActual();

        p.accionCambioZona();

        assertEquals(turnoAntes + 1, p.getTurnoActual());
        assertFalse(p.isAccionRealizada());
    }

    @Test
    void accionCambioZonaNoEjecutaSiNoEstaEnCurso() {
        GrafoZonas grafo = new GrafoZonas();
        Celda[][] celdas = new Celda[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                celdas[i][j] = new Celda(TipoCelda.SUELO);
        grafo.addZona(new Zona(0, "Z0", celdas));

        Partida p = new Partida(grafo);
        p.iniciar();
        p.setEstadoActual(EstadoJuego.DERROTA);
        int turnoAntes = p.getTurnoActual();

        p.accionCambioZona();

        assertEquals(turnoAntes, p.getTurnoActual());
    }

    @Test
    void colocarNPCsFijosSinZonasNoRompe() {
        GrafoZonas grafo = new GrafoZonas();
        Partida p = new Partida(grafo);

        assertDoesNotThrow(() -> p.colocarNPCsFijos());
    }

    @Test
    void cambiarZonaSinLlaveDe7a8LanzaExcepcion() {
        GrafoZonas grafo = new GrafoZonas();

        Celda[][] celdasInit = new Celda[][]{
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)},
                {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)}
        };
        grafo.addZona(new Zona(0, "Inicio", celdasInit));

        Celda[][] celdas7 = new Celda[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                celdas7[i][j] = new Celda(TipoCelda.SUELO);
        Zona z7 = new Zona(7, "Z7", celdas7);
        grafo.addZona(z7);

        Celda[][] celdas8 = new Celda[2][2];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                celdas8[i][j] = new Celda(TipoCelda.SUELO);
        Zona z8 = new Zona(8, "Z8", celdas8);
        grafo.addZona(z8);

        grafo.conectar(0, 7);
        grafo.conectar(7, 8);

        Partida p = new Partida(grafo);
        p.iniciar();
        p.iniciarTurno();
        p.setIdZonaActual(7);
        p.setZonaActual(z7);

        Puerta puerta = new Puerta(7, 8, 0, 0, 0, 0);
        celdas7[0][0].setTipoCelda(TipoCelda.PUERTA);
        celdas7[0][0].setPuerta(puerta);

        assertThrows(jueguito.juegopracticafinal.Modelo.Excepciones.ErrorAccesoZonaBloqueada.class,
                () -> p.cambiarZona(puerta));
    }
}