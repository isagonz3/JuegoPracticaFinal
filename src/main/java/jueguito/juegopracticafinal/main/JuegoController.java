package jueguito.juegopracticafinal.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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
    @FXML private Label zonaLabel;
    @FXML private Label turnoLabel;
    @FXML private Label vidaLabel;
    @FXML private ProgressBar vidaBar;
    @FXML private TextArea logArea;
    @FXML private GridPane mapaGrid;
    @FXML private Button terminarTurnoBtn, guardarBtn;

    private Partida partida;
    private JueguitoFX app;
    private static final int tileSize = 16;
    private Image imgJugador;
    private Image imgGato;
    private int baseRow = 1;
    private int baseCol = 0;

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

        imgJugador = new Image(getClass().getResourceAsStream("/jugador/jugador_down00.png"));
        imgGato = new Image(getClass().getResourceAsStream("/gato/gato_quieto.png"));

        partida.iniciarTurno();
        renderMapa();
        actualizarUI();
        configEventos();
    }

    private void renderMapa() {
        mapaGrid.getChildren().clear();
        Zona zona = partida.getZonaActual();
        int idZona = zona.getIdZona();

        String nombreArchivo = null;
        for(String[] nombre : MAPA_PNG){
            if(Integer.parseInt(nombre[0])==idZona){
                nombreArchivo = nombre[1];
                break;
            }
        }

        if(nombreArchivo==null){
            return;
        }

        Image mapa = new Image(getClass().getResourceAsStream("/zonas/" + nombreArchivo + ".png"));
        for(int i = 0; i < zona.getRows(); i++){
            for(int j = 0; j < zona.getCols(); j++){
                PixelReader pixelReader = mapa.getPixelReader();
                WritableImage tile = new WritableImage(pixelReader, j*tileSize, i*tileSize, tileSize, tileSize);
                ImageView tileView = new ImageView(tile);

                StackPane celdaPane = new StackPane(tileView);
                Celda celda = zona.getCelda(i,j);

                if(celda!=null && celda.tieneEntidad()){
                    Entidad entidad = celda.getEntidad();

                    if(entidad instanceof Jugador){
                        ImageView jugadorView = new ImageView(imgJugador);
                        jugadorView.setFitHeight(tileSize);
                        jugadorView.setFitWidth(tileSize);
                        celdaPane.getChildren().add(jugadorView);
                    }
                    else if(entidad instanceof Gato){
                        ImageView gatoView = new ImageView(imgGato);
                        gatoView.setFitHeight(tileSize);
                        gatoView.setFitWidth(tileSize);
                        celdaPane.getChildren().add(gatoView);
                    }
                    else{
                        Circle circle = new Circle(tileSize*0.35, Color.PINK);
                        celdaPane.getChildren().add(circle);
                    }
                }

                if(celda != null && celda.tieneNPC()){
                    celdaPane.getChildren().add(new Circle(tileSize*0.35, Color.BLUEVIOLET));
                }

                if(celda != null && celda.tieneObjeto()){
                    celdaPane.getChildren().add(new Circle(tileSize*0.35, Color.RED));
                }

                int row = i;
                int col = j;
                celdaPane.setOnMouseClicked((e) -> manejarClickCelda(row,col));
                mapaGrid.add(celdaPane,j,i);
            }
        }
    }

    private void manejarClickCelda(int i, int j){
        if(partida.getEstadoActual() != EstadoJuego.EN_CURSO){
            return;
        }

        Zona zona = partida.getZonaActual();
        if(!zona.esValida(i,j)){
            return;
        }

        Celda celda = zona.getCelda(i,j);
        if(celda==null){
            return;
        }

        Posicion posJugador = partida.getJugador().getPosicion();
        Celda celdaJugador = zona.getCelda(posJugador.getRow(),posJugador.getCol());

        if(i == posJugador.getRow() && j == posJugador.getCol()){
            return;
        }

        if(partida.moverJugador(i,j)){
            renderMapa();
            actualizarUI();
            partida.findGato();
        }
    }

    void manejarTecla(KeyEvent event) {
        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) return;
        switch (event.getCode()) {
            case W: case UP:    baseRow = -1; baseCol = 0;  moverTecla(); break;
            case S: case DOWN:  baseRow = 1;  baseCol = 0;  moverTecla(); break;
            case A: case LEFT:  baseRow = 0;  baseRow = -1; moverTecla(); break;
            case D: case RIGHT: baseRow = 0;  baseCol = 1;  moverTecla(); break;
            case SPACE: case ENTER: atacarTecla(); break;
            default: return;
        }
        event.consume();
    }

    private void moverTecla() {
        Posicion pos = partida.getJugador().getPosicion();
        if (partida.moverJugador(pos.getRow() + baseRow, pos.getCol() + baseCol)) {
            renderMapa();
            actualizarUI();
            partida.findGato();
        }
    }

    private void atacarTecla() {
        partida.atacarDireccion(baseRow, baseCol);
        renderMapa();
        actualizarUI();
    }

    private void actualizarUI(){
        Jugador jugador = partida.getJugador();
        Zona zona = partida.getZonaActual();
        zonaLabel.setText("Zona: " + zona.getNombreZona() + "(" + zona.getIdZona() + ")");
        turnoLabel.setText("Turno: " + partida.getTurnoActual() + " Puntos Movimiento (PM): " + jugador.getEstadisticas().getPuntosMovDisponibles());
        vidaLabel.setText("Vida: " + jugador.getEstadisticas().getVidaActual() + "/" + jugador.getEstadisticas().getVidaMax());
        vidaBar.setProgress((double) jugador.getEstadisticas().getVidaActual() / jugador.getEstadisticas().getVidaMax());
        String lastLog = partida.getLog().getUltimaEntrada();

        if(!lastLog.isEmpty()){
            logArea.appendText(lastLog + "\n");
        }
    }

    private void configEventos(){
        terminarTurnoBtn.setOnAction(e -> terminaTurno());
        guardarBtn.setOnAction(e -> guardaPartida());
    }

    private void terminaTurno() {
        partida.terminarTurno();
        if (partida.getEstadoActual() == EstadoJuego.VICTORIA) {
            app.irAEnd("VICTORIA: Has encontrado al gato y llegado al castillo.",
                    partida.getLog().getEntradas());
            return;
        }
        if (partida.getEstadoActual() == EstadoJuego.DERROTA) {
            app.irAEnd("DERROTA: " + partida.getLog().getUltimaEntrada(),
                    partida.getLog().getEntradas());
            return;
        }
        renderMapa();
        actualizarUI();
    }

    private void guardaPartida() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar Partida");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        fc.setInitialFileName("partida.json");
        File file = fc.showSaveDialog(app.getStage());
        if (file != null) {
            LoadJSON.guardarPartida(partida, file.getAbsolutePath());
            partida.getLog().registrar("Partida guardada en " + file.getName());
            actualizarUI();
        }
    }


}
