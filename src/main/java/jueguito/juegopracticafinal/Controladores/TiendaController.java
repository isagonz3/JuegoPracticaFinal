package jueguito.juegopracticafinal.Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import jueguito.juegopracticafinal.Controladores.Juego.JuegoController;

//Controlador de la tienda del juego: gestiona las acciones de compra de objetos y permite cerrar la ventana para volver al juego
public class TiendaController {

    // ATRIBUTOS

    @FXML private Button comprarBarcaBtn;
    @FXML private Button comprarMapaBtn;
    @FXML private Button salirBtn;

    private JuegoController juegoController;


    // MÉTODOS

    //Metodo para inicializar en JavaFX la tienda y permite asgnar las acciones para comprar objetos o cerrar la tienda a los botones
    @FXML
    private void initialize() {
        comprarBarcaBtn.setOnAction(e -> comprarBarca());

        comprarMapaBtn.setOnAction(e -> comprarMapa());

        salirBtn.setOnAction(e -> cerrarTienda());
    }

    //Gestiona la compra del objeto barca llamando al sistema de acciones para realizar la compra a cambio de un coste
    private void comprarBarca() {
        juegoController.getAcciones().comprar("Barca", 5);
    }

    //Gestiona la compra del objeto mapa llamando al sistema de acciones para realizar la compra a cambio de un coste
    private void comprarMapa() {
        juegoController.getAcciones().comprar("Mapa", 3);
    }

    //Cierra la ventana de la tienda y vuelve a la pantalla del juego
    private void cerrarTienda() {
        salirBtn.getScene().getWindow().hide();
    }
}