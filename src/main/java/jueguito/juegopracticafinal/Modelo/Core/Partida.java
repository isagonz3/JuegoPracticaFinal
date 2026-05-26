package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.*;
import jueguito.juegopracticafinal.Modelo.Inventario.*;
import jueguito.juegopracticafinal.Modelo.Log.LogMovimiento;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.NPC.*;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.Modelo.Turno.ResultadoCombate;
import jueguito.juegopracticafinal.Modelo.Vista.UIRenderer;
import jueguito.juegopracticafinal.TADs.Lista;

import static jueguito.juegopracticafinal.Modelo.Core.Configuracion.get;

public class Partida {

    private Jugador jugador;
    private Gato gato;
    private GrafoZonas grafo;
    private Zona zonaActual;
    private int idZonaActual;
    private EstadoJuego estadoActual = EstadoJuego.EN_CURSO;
    private LogMovimiento log = new LogMovimiento();
    private int turnoActual;
    private int idZonaGato = -1;
    private boolean gatoEncontrado;
    private boolean faseJugadorActiva = true;

    // Nuevos módulos
    private PartidaMovimiento movimiento;
    private PartidaObjetosYCombate objetosYCombate;
    private UIRenderer ui;


    public Partida(GrafoZonas grafo) {
        this.grafo = grafo;
        this.movimiento = new PartidaMovimiento(this);
        this.objetosYCombate = new PartidaObjetosYCombate(this);
    }

    // ============================================================
    // INICIO DE PARTIDA
    // ============================================================

    public void iniciar() {
        Zona zi = grafo.getZona(get().zona.zonaInicial);
        if (zi == null) {
            log.registrar("ERROR: Zona inicial no encontrada");
            estadoActual = EstadoJuego.DERROTA;
            return;
        }

        Posicion spawn = zi.getSpawnJugador();
        if (spawn == null) {
            spawn = findSpawn(zi);
            zi.setSpawnJugador(spawn);
        }

        idZonaActual = get().zona.zonaInicial;
        zonaActual = zi;

        jugador = new Jugador(
                "NoLink",
                new Estadisticas(
                        get().jugador.vidaMax,
                        get().jugador.ataqueBase,
                        get().jugador.defensaBase,
                        get().jugador.rangoMov
                ),
                spawn
        );

        zonaActual.getCelda(spawn.getRow(), spawn.getCol()).setEntidad(jugador);

        gato = new Gato(
                "Gatiko",
                new Estadisticas(
                        get().gato.vida,
                        get().gato.ataque,
                        get().gato.defensa,
                        get().gato.rangoMov
                )
        );

        turnoActual = 0;

        colocarGatoFijo();
        colocarLlaveFija();
        colocarNPCsFijos();

        log.registrar("INICIANDO PARTIDA \n Bienvenido. Estas en el Interior de tu Casa");
    }

    // ============================================================
    // TURNOS
    // ============================================================

    public void iniciarTurno() {
        if (estadoActual != EstadoJuego.EN_CURSO) return;

        faseJugadorActiva = true;

        jugador.getEstadisticas()
                .setPuntosMovDisponibles(jugador.getRangoTotal());

        log.registrar("--- Turno " + turnoActual + " ---");
    }

    public void terminarTurno() {
        if (estadoActual != EstadoJuego.EN_CURSO) return;

        faseJugadorActiva = false;

        movimiento.moverEnemigos();
        objetosYCombate.atacarJugador();

        jugador.getInventario().avanzarTurno();

        zonaActual.setCountTurnos(zonaActual.getCountTurnos() + 1);
        turnoActual++;

        if (checkDerrota() || checkVictoria()) return;

        iniciarTurno();
    }

    private boolean checkVictoria() {
        if (gatoEncontrado &&
                idZonaActual == get().zona.zonaCastillo &&
                zonaActual.getCelda(
                        jugador.getPosicion().getRow(),
                        jugador.getPosicion().getCol()
                ).getTipoCelda() == TipoCelda.SALIDA) {

            estadoActual = EstadoJuego.VICTORIA;
            log.registrar("Has logrado recuperar el gato de la princesa!");
            return true;
        }
        return false;
    }

    private boolean checkDerrota() {
        if (!jugador.estarVivo()) {
            estadoActual = EstadoJuego.DERROTA;
            log.registrar("Has perdido: no te quedan puntos de vida!");
            return true;
        }

        if (turnoActual >= get().turno.maxTurnos) {
            estadoActual = EstadoJuego.DERROTA;
            log.registrar("Has perdido: no has logrado encontrar el gato a tiempo!");
            return true;
        }

        return false;
    }

    // ============================================================
    // CAMBIO DE ZONA
    // ============================================================

    public void cambiarZona(Puerta puerta) {

        if (puerta == null) return;
        if (estadoActual != EstadoJuego.EN_CURSO) return;
        if (!faseJugadorActiva) return;

        int nid, dr, dc;

        if (puerta.getZonaOrigen() == idZonaActual) {
            nid = puerta.getZonaDestino();
            dr = puerta.getYDestino();
            dc = puerta.getXDestino();
        } else {
            nid = puerta.getZonaOrigen();
            dr = puerta.getYOrigen();
            dc = puerta.getXOrigen();
        }

        Zona nz = grafo.getZona(nid);

        if (nz == null) {
            log.registrar("Esta puerta no lleva a ninguna zona conocida");
            return;
        }

        if (nz.getCountTurnos() == 0) {
            objetosYCombate.poblarZona(nz);
            objetosYCombate.ponerObjetos(nz);
        }

        if (nid == get().zona.zonaCastillo && !gatoEncontrado) {
            log.registrar("DETENTE: has entrado sin el gato");
            estadoActual = EstadoJuego.DERROTA;
            return;
        }

        zonaActual.getCelda(
                jugador.getPosicion().getRow(),
                jugador.getPosicion().getCol()
        ).setEntidad(null);

        if (!nz.isVisitada()) {
            log.registrar("Nueva zona descubierta!");
        }

        nz.setVisitada(true);

        idZonaActual = nid;
        zonaActual = nz;

        dr = Math.min(Math.max(dr, 0), nz.getRows() - 1);
        dc = Math.min(Math.max(dc, 0), nz.getCols() - 1);

        jugador.moverA(new Posicion(dr, dc));
        nz.getCelda(dr, dc).setEntidad(jugador);

        log.registrar("Has entrado en: " + nz.getNombreZona());

        accionCambioZona();
    }

    public void accionCambioZona() {

        if (estadoActual != EstadoJuego.EN_CURSO) return;

        zonaActual.setCountTurnos(zonaActual.getCountTurnos() + 1);
        turnoActual++;

        jugador.getInventario().avanzarTurno();

        log.registrar("--- Turno " + turnoActual + " ---");

        faseJugadorActiva = true;

        jugador.getEstadisticas()
                .setPuntosMovDisponibles(jugador.getRangoTotal());
    }

    // ============================================================
    // GATO
    // ============================================================

    public boolean findGato() {
        if (gatoEncontrado) return true;
        if (zonaGato(gato.getPosicion()) != idZonaActual) return false;

        Posicion pg = gato.getPosicion();
        Posicion pj = jugador.getPosicion();

        if (Math.abs(pj.getRow() - pg.getRow()) <= 1 &&
                Math.abs(pj.getCol() - pg.getCol()) <= 1) {

            gatoEncontrado = true;
            log.registrar("Encontraste al gato! Toca llevarlo hasta la princesa.");
            return true;
        }

        return false;
    }

    public int zonaGato(Posicion p) {
        if (idZonaGato >= 0) return idZonaGato;

        for (int i = 0; i < get().zona.numZonas; i++) {
            Zona z = grafo.getZona(i);
            if (z != null &&
                    z.esValida(p.getRow(), p.getCol()) &&
                    z.getCelda(p.getRow(), p.getCol()).getEntidad() == gato) {
                idZonaGato = i;
                return i;
            }
        }

        return -1;
    }

    // ============================================================
    // DELEGACIÓN DE MÉTODOS A LOS MÓDULOS
    // ============================================================

    public boolean moverJugador(int row, int col) {
        return movimiento.moverJugador(row, col);
    }

    public Lista<Celda> getCeldasAccesibles() {
        return movimiento.getCeldasAccesibles();
    }

    public Lista<Celda> getCaminoMinimo(int dr, int dc) {
        return movimiento.getCaminoMinimo(dr, dc);
    }

    public ResultadoCombate atacarEnemigo(Enemigo e) {
        return objetosYCombate.atacarEnemigo(e);
    }

    public boolean atacarDireccion(int row, int col) {
        return objetosYCombate.atacarDireccion(row, col);
    }

    public boolean usarObjeto(Objeto o) {
        return objetosYCombate.usarObjeto(o);
    }

    public boolean recogerObjeto(Celda c) {
        return objetosYCombate.recogerObjeto(c);
    }

    public boolean equiparObjeto(Objeto o, SlotEquipable s) {
        return objetosYCombate.equiparObjeto(o, s);
    }

    public NPC interactNPC() {
        return objetosYCombate.interactNPC();
    }

    // ============================================================
    // UTILIDADES INTERNAS
    // ============================================================

    private Posicion findSpawn(Zona zona) {
        for (int i = 0; i < zona.getRows(); i++)
            for (int j = 0; j < zona.getCols(); j++) {
                Celda c = zona.getCelda(i, j);
                if (c != null && c.isTransitable() && !c.isOcupada())
                    return new Posicion(i, j);
            }
        return new Posicion(0, 0);
    }

    private void colocarGatoFijo() {
        int zonaId = 8;

        Zona zonaGato = grafo.getZona(zonaId);
        if (zonaGato == null) return;

        if (gato == null) {
            gato = new Gato(
                    "Gatiko",
                    new Estadisticas(
                            get().gato.vida,
                            get().gato.ataque,
                            get().gato.defensa,
                            get().gato.rangoMov
                    )
            );
        }

        Posicion pos = new Posicion(16, 14);

        if (!zonaGato.esValida(16, 14)) return;

        Celda celda = zonaGato.getCelda(16, 14);
        if (celda == null || celda.isOcupada()) return;

        gato.setPosicion(pos);
        celda.setEntidad(gato);

        idZonaGato = zonaId;
    }

    private void colocarLlaveFija() {
        int zonaId = 6;
        Zona zona = grafo.getZona(zonaId);
        if (zona == null) return;

        int row = 9;
        int col = 9;

        if (!zona.esValida(row, col)) return;

        Celda celda = zona.getCelda(row, col);
        if (celda == null) return;

        if (celda.tieneObjeto()) return;

        Objeto llave = new Objeto(
                "Llave",
                TipoObjeto.NPC,
                0, 0, 0, 0,
                1,
                "Llave especial"
        );

        celda.setObjeto(llave);
    }

    private void colocarNPCsFijos() {
        // Si tu clase original tenía NPCs fijos, aquí los colocas.
        // Si no, deja este método vacío.
    }

    // ============================================================
    // GETTERS NECESARIOS PARA LOS MÓDULOS
    // ============================================================

    public Jugador getJugador() { return jugador; }
    public Zona getZonaActual() { return zonaActual; }
    public LogMovimiento getLog() { return log; }
    public boolean isFaseJugadorActiva() { return faseJugadorActiva; }
    public void setFaseJugadorActiva(boolean f) { faseJugadorActiva = f; }
    public EstadoJuego getEstadoActual() { return estadoActual; }
    public void terminarTurnoPublic() { terminarTurno(); }
    public Gato getGato() {
        return gato;
    }
    public boolean isGatoEncontrado() {
        return gatoEncontrado;
    }
    public int getIdZonaActual() {
        return idZonaActual;
    }
    public int getTurnoActual() {
        return turnoActual;
    }


    public void setIdZonaActual(int idZonaActual) {
        this.idZonaActual = idZonaActual;
    }

    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }

    public void setGatoEncontrado(boolean gatoEncontrado) {
        this.gatoEncontrado = gatoEncontrado;
    }

    public void setEstadoActual(EstadoJuego estadoActual) {
        this.estadoActual = estadoActual;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }
    public void setUi(UIRenderer ui) {
        this.ui = ui;
    }

    public UIRenderer getUi() {
        return ui;
    }

}