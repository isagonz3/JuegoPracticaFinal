package jueguito.juegopracticafinal.Controladores.Juego;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;

//Controlador del inventario: permite seleccionar, usar y equipar objetos del inventario del jugador, y comprobar si la partida ha terminado tras dichas acciones
public class JuegoControllerInventario {

    // ATRIBUTOS

    private final JuegoController ctrl;
    private final Partida partida;

    private Objeto objetoSeleccionado;


    // CONSTRUCTOR

    public JuegoControllerInventario(JuegoController ctrl, Partida partida) {
        this.ctrl = ctrl;
        this.partida = partida;
    }


    // GETTERS Y SETTERS

    public void seleccionarObjeto(Objeto nuevo) {
        this.objetoSeleccionado = nuevo;
    }


    // MÉTODOS

    //Usa el objeto seleccionado del inventario: si el objeto es válido, aplica los cambios en la partida y limpia el objeto del inventario y actualiza la UI y el mapa
    public void usarObjeto() {
        if (objetoSeleccionado == null) return;

        Objeto aUsar = objetoSeleccionado;

        if (partida.usarObjeto(aUsar)) {

            ctrl.getInventarioList().getSelectionModel().clearSelection();
            objetoSeleccionado = null;

            if (verificarFinJuego()) return;

            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);
        }
    }

    //Equipa el objeto seleccionado del inventario: si el objeto es válido y no hay más de dos, aplica los cambios en la partida y limpia el objeto del inventario y actualiza la UI y el mapa
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

            ctrl.getInventarioList().getSelectionModel().clearSelection();
            objetoSeleccionado = null;

            if (verificarFinJuego()) return;

            ctrl.getUiRenderer().actualizarUI(partida);
            ctrl.getMapaRenderer().render(partida);
        }
    }

    //Comprueba si la partida ha terminado, y en caso de finalización muestra el resultado correspondiente y el log
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