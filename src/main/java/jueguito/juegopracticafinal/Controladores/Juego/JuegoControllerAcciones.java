package jueguito.juegopracticafinal.Controladores.Juego;

import javafx.scene.input.KeyEvent;
import jueguito.juegopracticafinal.App.JueguitoFX;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Inventario.Inventario;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;

public class JuegoControllerAcciones {

    private final JuegoController ctrl;
    private final Partida partida;
    private final JueguitoFX app;

    private boolean ataqueRealizado = false;

    public JuegoControllerAcciones(JuegoController ctrl, Partida partida, JueguitoFX app) {
        this.ctrl = ctrl;
        this.partida = partida;
        this.app = app;
    }


    // ============================================================
    // MANEJO DE TECLAS
    // ============================================================

    public void manejarTecla(KeyEvent event) {

        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) return;

        switch (event.getCode()) {

            case W: case UP:
                ctrl.setBaseRow(-1);
                ctrl.setBaseCol(0);
                moverTecla();
                break;

            case S: case DOWN:
                ctrl.setBaseRow(1);
                ctrl.setBaseCol(0);
                moverTecla();
                break;

            case A: case LEFT:
                ctrl.setBaseRow(0);
                ctrl.setBaseCol(-1);
                moverTecla();
                break;

            case D: case RIGHT:
                ctrl.setBaseRow(0);
                ctrl.setBaseCol(1);
                moverTecla();
                break;

            case E: case TAB:
                terminarTurno();
                break;
        }

        event.consume();
    }

    private void moverTecla() {

        Posicion pos = partida.getJugador().getPosicion();

        if (partida.moverJugador(pos.getRow() + ctrl.getBaseRow(),
                pos.getCol() + ctrl.getBaseCol())) {


            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);
        }
    }


    // ============================================================
    // BOTÓN DE ATAQUE
    // ============================================================

    public void atacarBoton() {

        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) return;

        if (ataqueRealizado) {
            partida.getLog().registrar("Ya has realizado un ataque en este turno.");
            ctrl.getUiRenderer().actualizarUI(partida);
            return;
        }

        Zona zona = partida.getZonaActual();
        Posicion posJugador = partida.getJugador().getPosicion();

        int filaObjetivo = posJugador.getRow() + ctrl.getBaseRow();
        int colObjetivo = posJugador.getCol() + ctrl.getBaseCol();

        if (!zona.esValida(filaObjetivo, colObjetivo)) {
            partida.getLog().registrar("No hay ningún objetivo en esa dirección.");
            ctrl.getUiRenderer().actualizarUI(partida);
            return;
        }

        Celda celdaObjetivo = zona.getCelda(filaObjetivo, colObjetivo);

        if (celdaObjetivo == null ||
                !celdaObjetivo.tieneEntidad() ||
                !(celdaObjetivo.getEntidad() instanceof Enemigo)) {

            partida.getLog().registrar("No hay enemigos para atacar.");
            ctrl.getUiRenderer().actualizarUI(partida);
            return;
        }

        Enemigo enemigo = (Enemigo) celdaObjetivo.getEntidad();

        int vidaAntes = enemigo.getEstadisticas().getVidaActual();

        partida.atacarDireccion(ctrl.getBaseRow(), ctrl.getBaseCol());
        enemigo.setAtacado(true);

        int vidaDespues = enemigo.getEstadisticas().getVidaActual();
        int dano = vidaAntes - vidaDespues;

        partida.getLog().registrar(
                "Has hecho " + dano +
                        " de daño. Vida restante del enemigo: " +
                        Math.max(vidaDespues, 0)
        );

        ataqueRealizado = true;

        ctrl.getUiRenderer().actualizarUI(partida);
        ctrl.getMapaRenderer().render(partida);

        int vidaJugadorAntes = partida.getJugador().getEstadisticas().getVidaActual();

        terminarTurno();

        int vidaJugadorDespues = partida.getJugador().getEstadisticas().getVidaActual();
        int danoRecibido = vidaJugadorAntes - vidaJugadorDespues;

        if (danoRecibido > 0) {
            partida.getLog().registrar(
                    "El enemigo te ha atacado y te ha quitado " + danoRecibido + " puntos de vida."
            );
        } else {
            partida.getLog().registrar("El enemigo ha huido o no ha conseguido atacarte.");
        }

        ataqueRealizado = false;

        ctrl.getUiRenderer().actualizarUI(partida);
        ctrl.getMapaRenderer().render(partida);
    }


    // ============================================================
    // INTERACCIÓN CON NPC
    // ============================================================

    public void interactuarBoton() {
        // 1. Intentar interactuar con NPC
        NPC npc = partida.interactNPC();
        if (npc != null) {
            if (npc.getTipo() == TipoNPC.COMERCIANTE) {
                abrirTienda(npc);
            } else {
                ctrl.getLogArea().appendText(npc.getNombre() + ": " + npc.hablar() + "\n");
            }
            return;
        }

        // 2. Intentar interactuar con Gato
        if (partida.interactuarGato()) {
            ctrl.getLogArea().appendText("¡Has rescatado al gato!\n");
        } else {
            ctrl.getLogArea().appendText("No hay nadie ni nada con lo que interactuar...\n");
        }

        ctrl.getUiRenderer().actualizarUI(partida);
        ctrl.getMapaRenderer().render(partida);
    }


    // ============================================================
    // TIENDA
    // ============================================================

    private void abrirTienda(NPC npc) {

        ctrl.getTiendaPanel().setVisible(true);
        ctrl.getLogArea().appendText("Has entrado en la tienda del comerciante\n");
    }

    public void cerrarTienda() {
        ctrl.getTiendaPanel().setVisible(false);
    }

    public void comprar(String item, int coste) {

        Inventario inv = partida.getJugador().getInventario();

        int gemas = 0;

        for (int i = 0; i < inv.size(); i++) {
            if (inv.getObjeto(i).getNombre().equalsIgnoreCase("gema")) {
                gemas++;
            }
        }

        if (gemas < coste) {
            ctrl.getLogArea().appendText("No tienes suficientes gemas\n");
            return;
        }

        for (int c = 0; c < coste; c++) {
            for (int i = 0; i < inv.size(); i++) {
                Objeto o = inv.getObjeto(i);
                if (o.getNombre().equalsIgnoreCase("gema")) {
                    inv.removeObjeto(o);
                    break;
                }
            }
        }

        inv.addObjeto(new Objeto(item, TipoObjeto.NPC));

        ctrl.getLogArea().appendText("Coste: " + coste + " gemas\n");
        ctrl.getLogArea().appendText(item + " comprado\n");

        ctrl.getUiRenderer().actualizarUI(partida);
    }


    // ============================================================
    // FIN DE TURNO
    // ============================================================

    public void terminarTurno() {

        partida.terminarTurno();

        if (partida.getEstadoActual() == EstadoJuego.VICTORIA) {
            app.irAEnd("VICTORIA: Has encontrado al gato y llegado al castillo.",
                    partida.getLog().getEntradas());
            return;
        }

        if (partida.getEstadoActual() == EstadoJuego.DERROTA) {
            app.irAEnd("DERROTA: " + partida.getLog().getUltimaEntrada(),
                    partida.getLog().getEntradas());
            return;
        }

        ctrl.getUiRenderer().actualizarUI(partida);
        ctrl.getMapaRenderer().render(partida);
    }
}