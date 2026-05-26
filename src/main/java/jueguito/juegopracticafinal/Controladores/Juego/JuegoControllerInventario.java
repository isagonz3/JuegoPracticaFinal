package jueguito.juegopracticafinal.Controladores.Juego;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;

public class JuegoControllerInventario {

    private final JuegoController ctrl;
    private final Partida partida;

    private Objeto objetoSeleccionado;

    public JuegoControllerInventario(JuegoController ctrl, Partida partida) {
        this.ctrl = ctrl;
        this.partida = partida;
    }

    // ============================================================
    // SELECCIÓN DE OBJETO
    // ============================================================

    public void seleccionarObjeto(Objeto nuevo) {

        if (nuevo == null) {
            objetoSeleccionado = null;
            return;
        }

        objetoSeleccionado = partida.getJugador()
                .getInventario()
                .findObjeto(String.valueOf(nuevo));
    }

    // ============================================================
    // USAR OBJETO
    // ============================================================

    public void usarObjeto() {

        if (objetoSeleccionado == null) return;

        if (partida.usarObjeto(objetoSeleccionado)) {

            ctrl.renderMapa();
            ctrl.actualizarUI();
            actualizarInventario();
            actualizarInventarioExtra();
        }
    }

    // ============================================================
    // EQUIPAR OBJETO
    // ============================================================

    public void equiparObjeto() {

        if (objetoSeleccionado == null) return;

        SlotEquipable slot = objetoSeleccionado.getSlot();

        if (slot == null) {
            partida.getLog().registrar("Este objeto no es equipable");
            ctrl.actualizarUI();
            return;
        }

        if (partida.equiparObjeto(objetoSeleccionado, slot)) {

            ctrl.renderMapa();
            ctrl.actualizarUI();
            actualizarInventario();
            actualizarInventarioExtra();
        }
    }

    // ============================================================
    // ACTUALIZAR INVENTARIO
    // ============================================================

    public void actualizarInventario() {

        ctrl.getInventarioList().getItems().clear();

        Jugador jugador = partida.getJugador();

        for (int i = 0; i < jugador.getInventario().getObjetos().getSize(); i++) {

            Objeto o = jugador.getInventario().getObjetos().get(i);
            ctrl.getInventarioList().getItems().add(o);
        }
    }

    // ============================================================
    // PANEL DE OBJETOS USADOS Y EQUIPADOS
    // ============================================================

    public void actualizarInventarioExtra() {

        Jugador jugador = partida.getJugador();

        String usados = "";

        for (int i = 0; i < jugador.getInventario().getObjetosUsados().getSize(); i++) {

            Objeto o = jugador.getInventario().getObjetosUsados().get(i);
            usados += "- " + o.getNombre() + "\n";
        }

        String equipados = "";

        Objeto arma = jugador.getInventario().getEquipado(SlotEquipable.ARMA);
        Objeto escudo = jugador.getInventario().getEquipado(SlotEquipable.ESCUDO);

        if (arma != null) equipados += arma.getNombre() + "\n";
        if (escudo != null) equipados += escudo.getNombre() + "\n";

        ctrl.getObjetosUsadosArea().setText(usados);
        ctrl.getObjetosEquipadosArea().setText(equipados);
    }
}