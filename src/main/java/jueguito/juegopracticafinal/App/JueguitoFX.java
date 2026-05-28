package jueguito.juegopracticafinal.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Log.EntradaLog;
import jueguito.juegopracticafinal.Controladores.EndController;
import jueguito.juegopracticafinal.Controladores.Juego.JuegoController;
import jueguito.juegopracticafinal.Controladores.MenuController;
import jueguito.juegopracticafinal.TADs.Lista;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorCargaVistaException;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorCargaJuegoException;
import java.io.IOException;

// Es la clase principal de JavaFX, en ella se inicia la aplicación, almacena el escenario principal y gestiona la navegación entre las distintas vistas del juego
public class JueguitoFX extends Application {

    private Stage stage;


    //Inicia la aplicación de JavaFX: carga el menú principal, configura el escenario y muestra la ventana de la aplicación
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(
                JueguitoFX.class.getResource("/jueguito/juegopracticafinal/menu-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 960, 576);

        MenuController controller = fxmlLoader.getController();
        controller.setApp(this);

        stage.setTitle("A TAD of Adventure");
        stage.setScene(scene);
        stage.show();
    }


    //Cambia la vista del menú a la pantalla principal del juego, inicializa el controlador del juego con la nueva pantalla y configura el uso de eventos con las teclas
    public void irAJuego(Partida partida) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/jueguito/juegopracticafinal/main-view.fxml"));

            Scene scene = new Scene(loader.load());

            JuegoController ctrl = loader.getController();
            ctrl.inicializar(partida, this);

            scene.setOnKeyPressed(ctrl::manejarTecla);
            stage.setScene(scene);

        } catch (IOException e) {
            throw new ErrorCargaJuegoException(
                    "No se pudo cargar la vista del juego", e);
        }
    }


    //Cambia la pantalla actual a la del final del juego, inicializa el controlador final con el resultado de la partida y el log generado durante el juego
    public void irAEnd(String resultado, Lista<EntradaLog> log) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/jueguito/juegopracticafinal/end-view.fxml"));

            Scene scene = new Scene(loader.load());

            EndController ctrl = loader.getController();
            ctrl.inicializar(resultado, log, this);

            stage.setScene(scene);

        } catch (IOException e) {
            throw new ErrorCargaVistaException(
                    "No se pudo cargar la pantalla final", e);
        }
    }


    //Vuelve a carga y mostrar el menú principal de la aplicación, inicializa de nuevo el controlador del menú y actualiza la escena del escenario principal
    public void volverAlMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/jueguito/juegopracticafinal/menu-view.fxml"));

            Scene scene = new Scene(loader.load(), 960, 576);

            MenuController ctrl = loader.getController();
            ctrl.setApp(this);

            stage.setScene(scene);

        } catch (IOException e) {
            throw new ErrorCargaVistaException(
                    "No se pudo cargar el menú principal", e);
        }
    }


    //Devuelve el escenario principal de la aplicación
    public Stage getStage() {
        return stage;
    }
}