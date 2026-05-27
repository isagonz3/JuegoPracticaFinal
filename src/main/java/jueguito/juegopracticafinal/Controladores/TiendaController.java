package jueguito.juegopracticafinal.Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import jueguito.juegopracticafinal.Controladores.Juego.JuegoController;

public class TiendaController {

    @FXML private Button comprarBarcaBtn;
    @FXML private Button comprarMapaBtn;
    @FXML private Button salirBtn;

    private JuegoController juegoController; // para acceder a inventario y log

    @FXML
    private void initialize() {
        comprarBarcaBtn.setOnAction(e -> comprarBarca());

        comprarMapaBtn.setOnAction(e -> comprarMapa());

        salirBtn.setOnAction(e -> cerrarTienda());
    }

    private void comprarBarca() {
        juegoController.getAcciones().comprar("Barca", 5);
    }

    private void comprarMapa() {
        juegoController.getAcciones().comprar("Mapa", 3);

    }

    private void cerrarTienda() {
        salirBtn.getScene().getWindow().hide();
    }
}