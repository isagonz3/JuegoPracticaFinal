package jueguito.juegopracticafinal.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TiendaController {

    @FXML
    private Button comprarBarcaBtn;

    @FXML
    private Button comprarMapaBtn;

    @FXML
    private Button salirBtn;

    private JuegoController juegoController; // para acceder a inventario y log

    public void setJuegoController(JuegoController jc) {
        this.juegoController = jc;
    }

    @FXML
    private void initialize() {

        comprarBarcaBtn.setOnAction(e -> comprarBarca());

        comprarMapaBtn.setOnAction(e -> comprarMapa());

        salirBtn.setOnAction(e -> cerrarTienda());
    }

    private void comprarBarca() {
        juegoController.comprar("barca", 5);
    }

    private void comprarMapa() {
        juegoController.comprar("mapa", 3);
    }

    private void cerrarTienda() {
        // cerrar ventana
        salirBtn.getScene().getWindow().hide();
    }
}