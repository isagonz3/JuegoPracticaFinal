package jueguito.juegopracticafinal.Controladores.Juego;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;

public class JuegoControllerInventario {

    private final JuegoController ctrl;
    private final Partida partida;

    private Objeto objetoSeleccionado;

    public JuegoControllerInventario(JuegoController ctrl, Partida partida) {
        this.ctrl = ctrl;
        this.partida = partida;
    }

    public void seleccionarObjeto(Objeto nuevo) {
        // Guardamos la referencia directa del objeto seleccionado en la lista
        this.objetoSeleccionado = nuevo;
    }


    // ============================================================
    // USAR OBJETO
    // ============================================================

    public void usarObjeto() {
        if (objetoSeleccionado == null) return;

        Objeto aUsar = objetoSeleccionado;

        if (partida.usarObjeto(aUsar)) {
            // Limpiamos la selección de la UI antes de actualizar pantallas
            ctrl.getInventarioList().getSelectionModel().clearSelection();
            objetoSeleccionado = null;

            if (verificarFinJuego()) return;

            // Forzamos el renderizado del mapa con el turno ya pasado y enemigos movidos
            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);
        }
    }


    // ============================================================
    // EQUIPAR OBJETO AUTOMÁTICO
    // ============================================================

    public void equiparObjeto() {
        if (objetoSeleccionado == null) return;

        Objeto aEquipar = objetoSeleccionado;
        SlotEquipable slot = aEquipar.getSlot();

        if (slot == null) {
            partida.getLog().registrar("Este objeto no es equipable");
            ctrl.getUiRenderer().actualizarUI(partida);
            return;
        }

        if (partida.equiparObjeto(aEquipar, slot)) {

            // Limpiamos la selección en la interfaz de JavaFX
            ctrl.getInventarioList().getSelectionModel().clearSelection();
            objetoSeleccionado = null;

            if (verificarFinJuego()) return;

            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);
        }
    }

    private boolean verificarFinJuego() {
        if (partida.getEstadoActual() == EstadoJuego.VICTORIA) {
            ctrl.getApp().irAEnd("VICTORIA: Has encontrado al gato y llegado al castillo.",
                    partida.getLog().getEntradas());
            return true;
        }

        if (partida.getEstadoActual() == EstadoJuego.DERROTA) {
            ctrl.getApp().irAEnd("DERROTA: " + partida.getLog().getUltimaEntrada(),
                    partida.getLog().getEntradas());
            return true;
        }
        return false;
    }
}