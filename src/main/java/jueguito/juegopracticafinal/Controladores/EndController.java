package jueguito.juegopracticafinal.Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import jueguito.juegopracticafinal.App.JueguitoFX;
import jueguito.juegopracticafinal.Modelo.Log.EntradaLog;
import jueguito.juegopracticafinal.TADs.Lista;

public class EndController {
    @FXML private Label resultadoLabel;
    @FXML
    private TextArea logFinal;
    @FXML private Button volverBtn;

    private JueguitoFX app;

    public void inicializar(String resultado, Lista<EntradaLog> logs, JueguitoFX app) {
        this.app = app;
        resultadoLabel.setText(resultado);

        if(resultado.toLowerCase().contains("victoria")){
            resultadoLabel.setText("Victoria");
            resultadoLabel.setTextFill(Color.GREEN);
        }
        else{
            resultadoLabel.setTextFill(Color.RED);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < logs.getSize(); i++) {
            sb.append(logs.get(i).mensaje()).append("\n");
        }
        logFinal.setText(sb.toString());

        volverBtn.setOnAction(e -> app.volverAlMenu());
    }


}
