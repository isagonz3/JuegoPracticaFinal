package jueguito.juegopracticafinal.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import jueguito.juegopracticafinal.Modelo.Core.LoadJSON;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;

import java.io.File;
import java.io.FileInputStream;

public class JuegoController {
    @FXML
    private Label zonaLabel, turnoLabel, vidaLabel;
    @FXML private ProgressBar vidaBar;
    @FXML private TextArea logArea;
    @FXML private StackPane mapaContainer;
    @FXML private ImageView mapaView;
    @FXML private Pane overlayPane;
    @FXML private Button terminarTurnoBtn, guardarBtn;

    private Partida partida;
    private JueguitoFX app;
    private double tileSize = 16;

    private static final String[][] MAPA_PNG = {
            {"0", "0_InteriorHabitacion"},
            {"1", "1_ExteriorHabitacion"},
            {"2", "2_BosqueOeste"},
            {"3", "3_Aldea"},
            {"4", "4_ExteriorTienda"},
            {"5", "5_InteriorTienda"},
            {"6", "6_Lago"},
            {"7", "7_BosqueEste"},
            {"8", "8_ExteriorCastillo"},
            {"9", "9_InteriorCastillo"},
    };

    public void inicializar(Partida partida,JueguitoFX app) {
        this.partida = partida;
        this.app = app;
        partida.iniciarTurno();
        cargarMapaPNG();
        renderOverlay();
        actualizarUI();
        configEventos();
    }

    private void cargarMapaPNG() {
        Zona zona = partida.getZonaActual();
        int idZona = zona.getIdZona();
        Image img = null;

        for(String[] image : MAPA_PNG){
            if(Integer.parseInt(image[0]) == idZona){
                String ruta = "/zonas/" + image[1] + ".png";
                try{
                    img = new Image(getClass().getResourceAsStream(ruta));
                }
                catch (Exception e){
                    try {
                        img = new Image(new FileInputStream(
                                "src/main/resources/zonas/" + image[1] + ".png"));
                    } catch (Exception e2) { }
                }
                break;
            }
        }
        if(img == null){
            zonaLabel.setText("Zona: " + idZona + " sin imagen");
            return;
        }

        mapaView.setImage(img);
        mapaContainer.setPrefSize(img.getWidth(), img.getHeight());
        mapaView.setFitHeight(img.getHeight());
        mapaView.setFitWidth(img.getWidth());
        tileSize = img.getWidth() / zona.getCols();
    }

    private void renderOverlay() {
        overlayPane.getChildren().clear();
        Zona zona = partida.getZonaActual();

        for (int i = 0; i < zona.getRows(); i++) {
            for (int j = 0; j < zona.getCols(); j++) {
                Celda celda = zona.getCelda(i, j);
                if(celda == null){
                    continue;
                }

                double celdaX = j * tileSize + tileSize / 2;
                double celdaY = i * tileSize + tileSize / 2;
                double radio = tileSize *0.35;

                if(celda.tieneEntidad()){
                    Circle circle = new Circle(celdaX, celdaY, radio);
                    Entidad entidad = celda.getEntidad();
                    if(entidad instanceof Jugador){
                        circle.setFill(Color.LIMEGREEN);
                    }
                    else if(entidad instanceof Gato){
                        circle.setFill(Color.RED);
                    }
                    else if(entidad instanceof Enemigo){
                        circle.setFill(Color.BLUE);
                    }
                    else{
                        circle.setFill(Color.GRAY);
                    }
                    overlayPane.getChildren().add(circle);
                }

                if(celda.tieneNPC()){
                    Circle circle = new Circle(celdaX, celdaY, radio);
                    circle.setFill(Color.PINK);
                    overlayPane.getChildren().add(circle);
                }

                if(celda.tieneObjeto()){
                    Circle circle = new Circle(celdaX, celdaY, radio);
                    circle.setFill(Color.YELLOW);
                    overlayPane.getChildren().add(circle);
                }
            }
        }
    }

    private void configEventos() {
        mapaContainer.setOnMouseClicked(this::manejarClick);
        terminarTurnoBtn.setOnAction(e -> terminaTurno());
        guardarBtn.setOnAction(e -> guardaPartida());
    }

    private void manejarClick(MouseEvent e) {
        if(partida.getEstadoActual() != EstadoJuego.EN_CURSO){
            return;
        }

        double x = e.getX();
        double y = e.getY();
        int row = (int) (y / tileSize);
        int col = (int) (x / tileSize);

        Zona zona = partida.getZonaActual();
        if(!zona.esValida(row, col)){
            return;
        }

        Celda celda = zona.getCelda(row, col);
        if(celda == null){
            return;
        }

        Posicion posJugador = partida.getJugador().getPosicion();
        if(row == posJugador.getRow() && col == posJugador.getCol()){
            return;
        }
        if(celda.tieneNPC() && celda.esAdyacente(zona.getCelda(posJugador.getRow(), posJugador.getCol()))){
            partida.interactNPC();
            renderOverlay();
            actualizarUI();
            return;
        }

        if(celda.tieneEntidad() && celda.getEntidad() instanceof Enemigo){
            if(celda.esAdyacente(zona.getCelda(posJugador.getRow(), posJugador.getCol()))){
                partida.atacarEnemigo((Enemigo)  celda.getEntidad());
                renderOverlay();
                actualizarUI();
                return;
            }
        }

        if(row == posJugador.getRow() && col == posJugador.getCol() && celda.tieneObjeto()){
            partida.recogerObjeto(celda);
            renderOverlay();
            actualizarUI();
            return;
        }

        boolean jugadorMueve = partida.moverJugador(row, col);
        if(jugadorMueve){
            renderOverlay();
            actualizarUI();
            partida.findGato();
        }

    }

    private void actualizarUI(){
        Jugador jugador = partida.getJugador();
        Zona zona = partida.getZonaActual();

        zonaLabel.setText("Zona: " + zona.getNombreZona() + "(" + zona.getIdZona() + ")");
        turnoLabel.setText("Turno: " + partida.getTurnoActual() + "Puntos de Movimiento (PM): " + jugador.getEstadisticas().getPuntosMovDisponibles());
        vidaLabel.setText("Vida: " + jugador.getEstadisticas().getVidaActual() + "/" + jugador.getEstadisticas().getVidaMax());
        vidaBar.setProgress((double) jugador.getEstadisticas().getVidaActual() / jugador.getEstadisticas().getVidaMax());

        String lastLog = partida.getLog().getUltimaEntrada();
        if(!lastLog.isEmpty()){
            logArea.appendText(lastLog + "\n");
        }
    }

    private void terminaTurno(){
        partida.terminarTurno();

        if(partida.getEstadoActual() == EstadoJuego.VICTORIA){
            app.irAEnd("¡LOGRASTE ENCONTRAR AL GATO DE LA PRINCESA Y LLEVARLO AL CASTILLO A TIEMPO", partida.getLog().getEntradas());
            return;
        }

        if(partida.getEstadoActual() == EstadoJuego.DERROTA){
            app.irAEnd("HAS FALLADO: No lograste encontrar al gato a tiempo..." + partida.getLog().getUltimaEntrada(), partida.getLog().getEntradas());
        }
    }

    private void guardaPartida(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Partida");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ArchivoJSON", "*.json"));
        fileChooser.setInitialFileName("partida.json");

        File file = fileChooser.showSaveDialog(app.getStage());
        if (file != null) {
            LoadJSON.guardarPartida(partida,file.getAbsolutePath());
            partida.getLog().registrar("Partida guardada en: " + file.getName());
            actualizarUI();
        }
    }
}
