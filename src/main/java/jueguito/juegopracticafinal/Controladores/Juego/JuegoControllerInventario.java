package jueguito.juegopracticafinal.Controladores.Juego;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
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

            // Actualizar UI y mapa con los nuevos módulos
            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);
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
            ctrl.getUiRenderer().actualizarUI(partida);
            return;
        }

        if (partida.equiparObjeto(objetoSeleccionado, slot)) {

            // Actualizar UI y mapa con los nuevos módulos
            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);
        }
    }
}
