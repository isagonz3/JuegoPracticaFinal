package jueguito.juegopracticafinal.Vista;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Log.EntradaLog;
import jueguito.juegopracticafinal.TADs.Lista;

//Renderiza y actualiza la interfaz: sincroniza el estado del modelo(partida,jugador,inventario,log...) con los elementos visuales de JavaFX
public class UIRenderer {

    private final Label zonaLabel;
    private final Label turnoLabel;
    private final Label vidaLabel;
    private final ProgressBar vidaBar;
    private final TextArea logArea;
    private final ListView<Objeto> inventarioList;
    private final TextArea objetosUsadosArea;
    private final TextArea objetosEquipadosArea;

    private int ultimoLogIndex = 0;


    //Constructor del renderizador de UI que recibe los componentes gráficos que se actualizan durante la ejecución del juego
    public UIRenderer(
            Label zonaLabel,
            Label turnoLabel,
            Label vidaLabel,
            ProgressBar vidaBar,
            TextArea logArea,
            ListView<Objeto> inventarioList,
            TextArea objetosUsadosArea,
            TextArea objetosEquipadosArea
    ) {
        this.zonaLabel = zonaLabel;
        this.turnoLabel = turnoLabel;
        this.vidaLabel = vidaLabel;
        this.vidaBar = vidaBar;
        this.logArea = logArea;
        this.inventarioList = inventarioList;
        this.objetosUsadosArea = objetosUsadosArea;
        this.objetosEquipadosArea = objetosEquipadosArea;
    }


    //Actualiza la interfaz del usuario con el estado actual de la partida sincronizando la zona, los turnos, el inventario, los objetos usados...
    public void actualizarUI(Partida partida) {

        Jugador j = partida.getJugador();
        if (j == null) return;

        zonaLabel.setText("Zona: " + partida.getZonaActual().getNombreZona());
        turnoLabel.setText("Turno: " + partida.getTurnoActual());

        vidaLabel.setText(
                j.getEstadisticas().getVidaActual() + "/" +
                        j.getEstadisticas().getVidaMax()
        );

        vidaBar.setProgress(
                (double) j.getEstadisticas().getVidaActual() /
                        j.getEstadisticas().getVidaMax()
        );

        actualizarLog(partida);

        actualizarInventario(j);

        actualizarUsados(j);

        actualizarEquipados(j);
    }


    //Actualiza el área de log mostrando las entradas nuevas correspondientes a las acciones y movimientos del jugador
    private void actualizarLog(Partida partida) {

        Lista<EntradaLog> entradas = partida.getLog().getEntradas();

        for (int i = ultimoLogIndex; i < entradas.getSize(); i++) {
            logArea.appendText(entradas.get(i).getMensaje() + "\n");
        }

        ultimoLogIndex = entradas.getSize();
    }


    //Actualiza la lista del inventario del jugador
    private void actualizarInventario(Jugador j) {

        inventarioList.getItems().clear();

        Lista<Objeto> objetos = j.getInventario().getObjetos();

        for (int i = 0; i < objetos.getSize(); i++) {
            inventarioList.getItems().add(objetos.get(i));
        }
    }


    //Muestra los objetos que están siendo usados por el jugador
    private void actualizarUsados(Jugador j) {
        StringBuilder usados = new StringBuilder();
        Lista<Objeto> listaUsados = j.getInventario().getObjetosUsados();

        for (int i = 0; i < listaUsados.getSize(); i++) {
            if (listaUsados.get(i) != null) {
                usados.append("- ").append(listaUsados.get(i).getNombre()).append("\n");
            }
        }

        objetosUsadosArea.setText(usados.toString());
    }


    //Muestra los objetos que están siendo equipados por el jugador (máximo dos)
    private void actualizarEquipados(Jugador j) {
        StringBuilder equip = new StringBuilder();
        Lista<Objeto> listaEquipados = j.getInventario().getObjetosEquipados();

        for (int i = 0; i < listaEquipados.getSize(); i++) {
            if (listaEquipados.get(i) != null) {
                equip.append("- ").append(listaEquipados.get(i).getNombre()).append("\n");
            }
        }

        objetosEquipadosArea.setText(equip.toString());
    }
}