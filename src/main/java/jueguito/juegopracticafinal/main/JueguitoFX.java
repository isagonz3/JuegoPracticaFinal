package jueguito.juegopracticafinal.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Log.EntradaLog;
import jueguito.juegopracticafinal.TADs.Lista;

import java.io.IOException;

public class JueguitoFX extends Application {
    private Stage stage;

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

    public void irAJuego(Partida partida) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/jueguito/juegopracticafinal/main-view.fxml"));
            Scene scene = new Scene(loader.load());
            JuegoController ctrl = loader.getController();
            ctrl.inicializar(partida, this);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void irAEnd(String resultado, Lista<EntradaLog> log) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/jueguito/juegopracticafinal/end-view.fxml"));
            Scene scene = new Scene(loader.load());
            EndController ctrl = loader.getController();
            ctrl.inicializar(resultado, log, this);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void volverAlMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/jueguito/juegopracticafinal/menu-view.fxml"));
            Scene scene = new Scene(loader.load(), 960, 576);
            MenuController ctrl = loader.getController();
            ctrl.setApp(this);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() { return stage; }
}