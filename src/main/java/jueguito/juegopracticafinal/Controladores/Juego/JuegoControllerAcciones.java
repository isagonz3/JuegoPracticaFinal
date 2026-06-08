package jueguito.juegopracticafinal.Controladores.Juego;

import javafx.scene.input.KeyEvent;
import jueguito.juegopracticafinal.App.JueguitoFX;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorAccesoZonaBloqueada;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorCasillaOcupada;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorMovimientoInvalido;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorMovimientoSinBarca;
import jueguito.juegopracticafinal.Modelo.Inventario.Inventario;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.TADs.InterfazIterador;

//Controlador de las acciones principales del jugador: gestiona el movimiento, ataque, interacciones, comercio y avance de turnos
public class JuegoControllerAcciones {

    // ATRIBUTOS

    private final JuegoController ctrl;
    private final Partida partida;
    private final JueguitoFX app;



    // CONSTRUCTOR

    public JuegoControllerAcciones(JuegoController ctrl, Partida partida, JueguitoFX app) {
        this.ctrl = ctrl;
        this.partida = partida;
        this.app = app;
    }


    // MOVIMIENTO

    //Gestiona las teclas pulsadas por el usuario durante la partida para mover al jugador
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
        }

        event.consume();
    }

    //Ejecuta el movimiento del jugador según la dirección seleccionada, gestiona que no haya colisiones, errores de movimiento y actualiza la UI
    private void moverTecla() {

        Posicion pos = partida.getJugador().getPosicion();

        try {

            boolean movido = partida.moverJugador(
                    pos.getRow() + ctrl.getBaseRow(),
                    pos.getCol() + ctrl.getBaseCol()
            );

            if (movido) {
                ctrl.getUiRenderer().actualizarUI(partida);
                ctrl.getMapaRenderer().render(partida);
            }

            if (partida.getEstadoActual() == EstadoJuego.VICTORIA) {
                app.irAEnd("VICTORIA: Has encontrado al gato y llegado al castillo.",
                        partida.getLog().getEntradas());
            }

            else if (partida.getEstadoActual() == EstadoJuego.DERROTA) {
                app.irAEnd("DERROTA: " + partida.getLog().getUltimaEntrada(),
                        partida.getLog().getEntradas());

            }

        } catch (ErrorCasillaOcupada e) {

            partida.getLog().registrar("Hay algo bloqueando el paso");
            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);

        } catch (ErrorMovimientoSinBarca | ErrorAccesoZonaBloqueada | ErrorMovimientoInvalido e) {

            partida.getLog().registrar(e.getMessage());
            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);

        }
    }


    // ATAQUE

    //Ejecuta el ataque del jugador en la dirección seleccionada: comprueba si hay un enemigo, aplica daño, registra el combate y gestiona el coontraataque
    public void atacarBoton() {

        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) return;

        if (partida.isAccionRealizada()) {
            partida.getLog().registrar("Ya has realizado una acción en este turno.");
            ctrl.getUiRenderer().actualizarUI(partida);
            return;
        }

        Zona zona = partida.getZonaActual();
        Posicion posJugador = partida.getJugador().getPosicion();

        int[][] direcciones = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        Enemigo enemigo = null;
        int dirIdx = -1;

        for (int d = 0; d < direcciones.length; d++) {
            int fila = posJugador.getRow() + direcciones[d][0];
            int col = posJugador.getCol() + direcciones[d][1];
            if (!zona.esValida(fila, col)) continue;
            Celda celda = zona.getCelda(fila, col);
            if (celda != null && celda.tieneEntidad()
                    && celda.getEntidad() instanceof Enemigo e && e.estarVivo()) {
                enemigo = (Enemigo) celda.getEntidad();
                dirIdx = d;
                break;
            }
        }

        if (enemigo == null) {
            partida.getLog().registrar("No hay enemigos adyacentes para atacar.");
            ctrl.getUiRenderer().actualizarUI(partida);
            return;
        }

        int vidaAntes = enemigo.getEstadisticas().getVidaActual();

        partida.atacarDireccion(direcciones[dirIdx][0], direcciones[dirIdx][1]);
        enemigo.setAtacado(true);

        int vidaDespues = enemigo.getEstadisticas().getVidaActual();
        int dano = vidaAntes - vidaDespues;

        partida.getLog().registrar(
                "Has hecho " + dano +
                        " de daño. Vida restante del enemigo: " +
                        Math.max(vidaDespues, 0)
        );

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

        ctrl.getUiRenderer().actualizarUI(partida);
        ctrl.getMapaRenderer().render(partida);
    }


    // INTERACCIÓN NPC

    //Gestiona la interacción del jugador con los NPCs: puede abrir la tienda, mostrar diálogos o interactuar con objetos especiales
    public void interactuarBoton() {

        NPC npc = partida.interactNPC();
        if (npc != null) {
            if (npc.getTipo() == TipoNPC.COMERCIANTE) {
                abrirTienda(npc);
                return;
            }

            if(npc.getNombre().equals("Princesa") && partida.isGatoEncontrado()){
                ctrl.getLogArea().appendText(npc.getNombre() + ": Has encontrado a mi gato !!! \n");
                terminarTurno();
                return;
            }

            else {
                ctrl.getLogArea().appendText(npc.getNombre() + ": " + npc.hablar() + "\n");
            }
            return;
        }

        if (partida.interactuarGato()) {
            ctrl.getLogArea().appendText("¡Has rescatado al gato!\n");
            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);
            return;
        }

        //Buscar objetos en celdas adyacentes
        Zona zona = partida.getZonaActual();
        Posicion pj = partida.getJugador().getPosicion();
        int[][] direcciones = {{-1,0}, {1,0}, {0,-1}, {0,1}};

        for (int[] dir : direcciones) {
            int fila = pj.getRow() + dir[0];
            int col = pj.getCol() + dir[1];
            if (!zona.esValida(fila, col)) continue;
            Celda celda = zona.getCelda(fila, col);
            if (celda != null && celda.tieneObjeto()) {
                partida.recogerObjeto(celda);
                ctrl.getUiRenderer().actualizarUI(partida);
                ctrl.getMapaRenderer().render(partida);
                return;
            }
        }

        ctrl.getLogArea().appendText("No hay nadie ni nada con lo que interactuar...\n");

        ctrl.getUiRenderer().actualizarUI(partida);
        ctrl.getMapaRenderer().render(partida);
    }


    // TIENDA

    private void abrirTienda(NPC npc) {
        ctrl.getTiendaPanel().setVisible(true);
        ctrl.getLogArea().appendText("Has entrado en la tienda del comerciante\n");
    }

    public void cerrarTienda() {
        ctrl.getTiendaPanel().setVisible(false);
        ctrl.getUiRenderer().actualizarUI(partida);
        ctrl.getMapaRenderer().render(partida);
        ctrl.getMapaGrid().requestFocus();
    }

    //Gestiona la compra de objetos en la tienda verificando si el jugador tiene suficientes gemas y añade el objeto al inventario en caso de que sí
    public void comprar(String item, int coste) {

        Inventario inv = partida.getJugador().getInventario();

        int gemas = 0;

        InterfazIterador<Objeto> it = inv.getObjetos().iterador();
        while (it.hasNext()) {
            if (it.next().getNombre().equalsIgnoreCase("gema")) gemas++;
        }

        if (gemas < coste) {
            ctrl.getLogArea().appendText("No tienes suficientes gemas\n");
            return;
        }

        for (int c = 0; c < coste; c++) {
            InterfazIterador<Objeto> itO = inv.getObjetos().iterador();
            while (itO.hasNext()) {
                Objeto o = itO.next();
                if (o.getNombre().equalsIgnoreCase("gema")) {
                    inv.removeObjeto(o);
                    break;
                }
            }
        }

        if ("Mapa".equalsIgnoreCase(item)) {
            inv.addObjeto(new Objeto("Mapa", TipoObjeto.USABLE, 0, 0, 0, 0, 99, "Muestra el camino a la salida"));
        } else {
            inv.addObjeto(new Objeto(item, TipoObjeto.NPC));
        }

        ctrl.getLogArea().appendText("Coste: " + coste + " gemas\n");
        ctrl.getLogArea().appendText(item + " comprado\n");
        partida.terminarTurno();

        ctrl.getUiRenderer().actualizarUI(partida);
    }


    // FIN DE TURNO

    //Finaliza el turno del jugador y comprueba si hay victoria o derrota, actualiza el estado de juego y la interfaz
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