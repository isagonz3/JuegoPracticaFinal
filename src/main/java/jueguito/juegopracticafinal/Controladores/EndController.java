package jueguito.juegopracticafinal.Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import jueguito.juegopracticafinal.App.JueguitoFX;
import jueguito.juegopracticafinal.Modelo.Log.EntradaLog;
import jueguito.juegopracticafinal.TADs.InterfazIterador;
import jueguito.juegopracticafinal.TADs.Lista;

//Controlador de la pantalla final del juego: muestra el resultado de la partida y el log de eventos ocurridos durante la partida
public class EndController {

    // ATRIBUTOS

    @FXML private Label resultadoLabel;
    @FXML private TextArea logFinal;
    @FXML private Button volverBtn;

    private JueguitoFX app;


    // MÉTODOS

    //Inicializa la pantalla final con los datos de la partida y muestra el resultado de la partida, el log de eventos ocurridos durante la partida y el botón para volver al menú
    public void inicializar(String resultado, Lista<EntradaLog> logs, JueguitoFX app) {

        this.app = app;

        resultadoLabel.setText(resultado);

        resultadoLabel.getStyleClass().add("game-title");
        if (resultado.toLowerCase().contains("victoria")) {
            resultadoLabel.setText("Victoria");
            resultadoLabel.getStyleClass().add("victoria");
        } else {
            resultadoLabel.getStyleClass().add("derrota");
        }

        StringBuilder sb = new StringBuilder();

        InterfazIterador<EntradaLog> it = logs.iterador();
        while (it.hasNext()) {
            sb.append(it.next().getMensaje()).append("\n");
        }

        logFinal.setText(sb.toString());

        volverBtn.setOnAction(e -> app.volverAlMenu());
    }
}