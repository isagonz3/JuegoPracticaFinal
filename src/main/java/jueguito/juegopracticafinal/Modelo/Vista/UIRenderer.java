package jueguito.juegopracticafinal.Modelo.Vista;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Log.EntradaLog;
import jueguito.juegopracticafinal.TADs.Lista;

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

    // ============================================================
    // ACTUALIZAR UI COMPLETA
    // ============================================================

    public void actualizarUI(Partida partida) {

        Jugador j = partida.getJugador();
        if (j == null) return;

        // Zona y turno
        zonaLabel.setText("Zona: " + partida.getZonaActual().getNombreZona());
        turnoLabel.setText("Turno: " + partida.getTurnoActual());

        // Vida
        vidaLabel.setText(
                j.getEstadisticas().getVidaActual() + "/" +
                        j.getEstadisticas().getVidaMax()
        );

        vidaBar.setProgress(
                (double) j.getEstadisticas().getVidaActual() /
                        j.getEstadisticas().getVidaMax()
        );

        // Log
        actualizarLog(partida);

        // Inventario
        actualizarInventario(j);

        // Objetos usados
        actualizarUsados(j);

        // Objetos equipados
        actualizarEquipados(j);
    }

    // ============================================================
    // LOG
    // ============================================================

    private void actualizarLog(Partida partida) {

        Lista<EntradaLog> entradas = partida.getLog().getEntradas();

        for (int i = ultimoLogIndex; i < entradas.getSize(); i++) {
            logArea.appendText(entradas.get(i).getMensaje() + "\n");
        }

        ultimoLogIndex = entradas.getSize();
    }

    public void limpiarLog() {
        logArea.clear();
        ultimoLogIndex = 0;
    }

    // ============================================================
    // INVENTARIO
    // ============================================================

    private void actualizarInventario(Jugador j) {

        inventarioList.getItems().clear();

        Lista<Objeto> objetos = j.getInventario().getObjetos();

        for (int i = 0; i < objetos.getSize(); i++) {
            inventarioList.getItems().add(objetos.get(i));
        }
    }

    // ============================================================
    // OBJETOS USADOS
    // ============================================================

    private void actualizarUsados(Jugador j) {

        StringBuilder usados = new StringBuilder();

        Lista<Objeto> listaUsados = j.getInventario().getObjetosUsados();

        for (int i = 0; i < listaUsados.getSize(); i++) {
            usados.append("- ").append(listaUsados.get(i).getNombre()).append("\n");
        }

        objetosUsadosArea.setText(usados.toString());
    }

    // ============================================================
    // OBJETOS EQUIPADOS
    // ============================================================

    private void actualizarEquipados(Jugador j) {

        StringBuilder equip = new StringBuilder();

        Objeto[] equipArray = j.getEquipamiento();

        for (Objeto o : equipArray) {
            if (o != null) {
                equip.append(o.getNombre()).append("\n");
            }
        }

        objetosEquipadosArea.setText(equip.toString());
    }
}

