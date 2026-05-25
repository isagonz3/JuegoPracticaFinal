package jueguito.juegopracticafinal.main;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import jueguito.juegopracticafinal.Modelo.Core.LoadJSON;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.*;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.TADs.Lista;

import java.io.File;

public class JuegoController {
    @FXML private Label zonaLabel;
    @FXML private Label turnoLabel;
    @FXML private Label vidaLabel, ataqueLabel,defensaLabel,movLabel;
    @FXML private Label armaLabel, escudoLabel, llaveLabel;
    @FXML private Label puntosMovLabel;
    @FXML private ProgressBar vidaBar;
    @FXML private TextArea logArea;
    @FXML private GridPane mapaGrid;
    @FXML private Button terminarTurnoBtn, guardarBtn, menuBtn;
    @FXML private ListView<String> inventarioList;
    @FXML private Button usarBtn, equiparBtn, atacarBtn, recogerBtn;

    private Partida partida;
    private JueguitoFX app;
    private Image[][] imgJugadorAndar;
    private Image[][] imgJugadorAtaque;
    private Image[][] imgGatoAndar;
    private Image imgJugadorAgua;
    private Image imgGato;
    private Image imgEnemigo1;
    private int baseRow = 1;
    private int baseCol = 0;
    private static final int DIR_DOWN = 0;
    private static final int DIR_UP = 1;
    private static final int DIR_LEFT = 2;
    private static final int DIR_RIGHT = 3;
    private int direccionActual = DIR_DOWN;
    private int frameAndar = 0;
    private boolean animacionAtaque = false;
    private int frameAtaque = 0;
    private Lista<Celda> celdasAccesibles = null;
    private Objeto objetoSeleccionado;

    private static final int TILE_SIZE = 16;

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

        imgJugadorAndar = new Image[4][6];
        imgJugadorAtaque = new Image[4][7];
        imgGatoAndar = new Image[4][4];
        imgGato = cargaImagen("/gato/gato_quieto.png");
        imgEnemigo1 = cargaImagen("/enemigos/soldado_quieto.png");
        imgJugadorAgua = cargaImagen("/jugador/jugador_agua.png");

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < JUGADOR_ANDAR_FRAMES[i]; j++) {
                imgJugadorAndar[i][j] = cargaImagen("/jugador/" + JUGADOR_ANDAR[i][j] + ".png");
            }
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < JUGADOR_ATAQUE_FRAMES[i]; j++) {
                imgJugadorAtaque[i][j] = cargaImagen("/jugador/" + JUGADOR_ATAQUE[i][j] + ".png");
            }
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < GATO_ANDAR_FRAMES[i]; j++) {
                imgGatoAndar[i][j] = cargaImagen("/gato/" + GATO_ANDAR[i][j] + ".png");
            }
        }

        partida.iniciarTurno();
        actualizarUI();
        renderMapa();
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

        boolean[][] checkCelda = null;
        if(celdasAccesibles != null){
            checkCelda = new boolean[zona.getRows()][zona.getCols()];
            for(int c = 0; c < celdasAccesibles.getSize(); c++){
                Celda access = celdasAccesibles.get(c);
                checkCelda[access.getRow()][access.getCol()] = true;
            }
        }

        PixelReader pixelReader = mapa.getPixelReader();
        for(int i = 0; i < zona.getRows(); i++){
            for(int j = 0; j < zona.getCols(); j++){
                WritableImage tile = new WritableImage(pixelReader, j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                ImageView tileView = new ImageView(tile);

                StackPane celdaPane = new StackPane(tileView);
                Celda celda = zona.getCelda(i,j);

                if(celda!=null && celda.tieneEntidad()){
                    Entidad entidad = celda.getEntidad();

                    if(entidad instanceof Jugador){
                        ImageView jugadorView = new ImageView(getJugadorSprite());
                        jugadorView.setPreserveRatio(true);
                        jugadorView.setFitHeight(TILE_SIZE);
                        jugadorView.setFitWidth(TILE_SIZE);
                        if(direccionActual == DIR_RIGHT){
                            jugadorView.setScaleX(-1);
                        }
                        StackPane.setAlignment(jugadorView, Pos.BOTTOM_CENTER);
                        celdaPane.getChildren().add(jugadorView);
                    }
                    else if(entidad instanceof Gato){
                        ImageView gatoView = new ImageView(getGatoSprite());
                        gatoView.setPreserveRatio(true);
                        gatoView.setFitHeight(TILE_SIZE);
                        gatoView.setFitWidth(TILE_SIZE);
                        StackPane.setAlignment(gatoView, Pos.BOTTOM_CENTER);
                        celdaPane.getChildren().add(gatoView);
                    }

                    else if(entidad instanceof Enemigo){
                        ImageView enemigo1View = new ImageView(imgEnemigo1);
                        enemigo1View.setPreserveRatio(true);
                        enemigo1View.setFitHeight(TILE_SIZE);
                        enemigo1View.setFitWidth(TILE_SIZE);
                        StackPane.setAlignment(enemigo1View, Pos.BOTTOM_CENTER);
                        celdaPane.getChildren().add(enemigo1View);
                    }
                    else{
                        Circle circle = new Circle(TILE_SIZE*0.35, Color.PINK);
                        celdaPane.getChildren().add(circle);
                    }
                }

                if(celda != null && celda.tieneNPC()){
                    ImageView npcView = new ImageView(seleccionarSpriteNPC(celda.getNpc()));
                    npcView.setFitHeight(TILE_SIZE);
                    npcView.setFitWidth(TILE_SIZE);
                    celdaPane.getChildren().add(npcView);
                }

                if(celda != null && celda.tieneObjeto()){
                    ImageView objView = new ImageView(seleccionarSpriteObjeto(celda.getObjeto()));
                    objView.setFitHeight(TILE_SIZE);
                    objView.setFitWidth(TILE_SIZE);
                    celdaPane.getChildren().add(objView);
                }

                if(checkCelda != null && checkCelda[i][j]){
                    Rectangle ilum = new Rectangle(TILE_SIZE, TILE_SIZE, Color.rgb(206, 0, 100, 0.25));
                    celdaPane.getChildren().add(ilum);
                }

                mapaGrid.add(celdaPane,j,i);
            }
        }
    }


    void manejarTecla(KeyEvent event) {
        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) return;
        switch (event.getCode()) {
            case W: case UP:    baseRow = -1; baseCol = 0;  moverTecla(); break;
            case S: case DOWN:  baseRow = 1;  baseCol = 0;  moverTecla(); break;
            case A: case LEFT:  baseRow = 0;  baseCol = -1; moverTecla(); break;
            case D: case RIGHT: baseRow = 0;  baseCol = 1;  moverTecla(); break;
            case SPACE: case ENTER: atacarTecla(); break;
            default: return;
        }
        event.consume();
    }

    private void moverTecla() {
        direccionActual = deltaADireccion(baseRow, baseCol);
        Posicion pos = partida.getJugador().getPosicion();
        if (partida.moverJugador(pos.getRow() + baseRow, pos.getCol() + baseCol)) {
            frameAndar = (frameAndar + 1) % JUGADOR_ANDAR_FRAMES[direccionActual];
            actualizarUI();
            renderMapa();
            partida.findGato();
        }
    }

    private void atacarTecla() {
        if(baseRow != 0 || baseCol != 0){
            direccionActual = deltaADireccion(baseRow, baseCol);
        }
        animacionAtaque = true;
        frameAtaque = 0;
        int framesTotales = JUGADOR_ATAQUE_FRAMES[direccionActual];

        partida.atacarDireccion(baseRow, baseCol);
        actualizarUI();
        renderMapa();

        PauseTransition pausa = new PauseTransition(Duration.millis(80));
        pausa.setOnFinished(e -> {
            frameAtaque++;
            if(frameAtaque < framesTotales){
                renderMapa();
                pausa.playFromStart();
            } else {
                animacionAtaque = false;
                renderMapa();
                terminaTurno();
            }
        });
        pausa.play();
    }

    private void actualizarUI(){
        Jugador jugador = partida.getJugador();
        Zona zona = partida.getZonaActual();
        zonaLabel.setText("Zona: " + zona.getNombreZona() + "(" + zona.getIdZona() + ")");
        turnoLabel.setText("Turno: " + partida.getTurnoActual() + " Puntos Movimiento (PM): " + jugador.getEstadisticas().getPuntosMovDisponibles());
        vidaLabel.setText("Vida: " + jugador.getEstadisticas().getVidaActual() + "/" + jugador.getEstadisticas().getVidaMax());
        vidaBar.setProgress((double) jugador.getEstadisticas().getVidaActual() / jugador.getEstadisticas().getVidaMax());
        ataqueLabel.setText("Ataque: " + jugador.getEstadisticas().getAtaqueBase());
        defensaLabel.setText("Defensa: " + jugador.getEstadisticas().getDefensaBase());
        movLabel.setText("Rango Mov: " + jugador.getEstadisticas().getRangoMov());
        puntosMovLabel.setText("PM disp: " + jugador.getEstadisticas().getPuntosMovDisponibles());
        Objeto arma = jugador.getEquipamiento(SlotEquipable.ARMA);
        Objeto escudo = jugador.getEquipamiento(SlotEquipable.ESCUDO);
        armaLabel.setText("Arma: " + (arma != null ? arma.getNombre() : "ninguna"));
        escudoLabel.setText("Escudo: " + (escudo != null ? escudo.getNombre() : "ninguno"));
        String lastLog = partida.getLog().getUltimaEntrada();

        if(!lastLog.isEmpty()){
            logArea.appendText(lastLog + "\n");
        }

        if (partida.getEstadoActual() == EstadoJuego.EN_CURSO) {
            celdasAccesibles = partida.getCeldasAccesibles();
        }

        else {
            celdasAccesibles = null;
        }
        inventarioList.getItems().clear();
        Jugador jug = partida.getJugador();
        for (int i = 0; i < jug.getInventario().size(); i++) {
            inventarioList.getItems().add(jug.getInventario().getObjeto(i).toString());
        }
    }

    private void configEventos(){
        terminarTurnoBtn.setOnAction(e -> terminaTurno());
        guardarBtn.setOnAction(e -> guardaPartida());
        inventarioList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) { objetoSeleccionado = null; return; }
            objetoSeleccionado = partida.getJugador().getInventario().findObjeto(newVal);
        });

        usarBtn.setOnAction(e -> {
            if (objetoSeleccionado == null) return;
            if (partida.usarObjeto(objetoSeleccionado)) {
                renderMapa();
                terminaTurno();
            }
        });

        equiparBtn.setOnAction(e -> {
            if (objetoSeleccionado == null) return;
            SlotEquipable slot = objetoSeleccionado.getSlot();
            if (slot == null) {
                partida.getLog().registrar("Este objeto no es equipable");
                actualizarUI();
                return;
            }
            if (partida.equiparObjeto(objetoSeleccionado, slot)) {
                renderMapa();
                terminaTurno();
            }
        });

        atacarBtn.setOnAction(e -> atacarTecla());
        recogerBtn.setOnAction(e -> {
            Posicion p = partida.getJugador().getPosicion();
            Celda c = partida.getZonaActual().getCelda(p.getRow(), p.getCol());
            if(c.tieneObjeto() && partida.recogerObjeto(c)){
                renderMapa();
                terminaTurno();
            }
        });
        menuBtn.setOnAction(e -> app.volverAlMenu());
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

    private Image cargaImagen(String ruta) {
        var is = getClass().getResourceAsStream(ruta);
        return is != null ? new Image(is) : null;
    }

    private Image seleccionarSpriteNPC(NPC npc) {
        return cargaImagen("/NPCs/NPC_1.png");
    }

    private Image seleccionarSpriteObjeto(Objeto objeto) {
        String ruta;
        switch(objeto.getNombre().toLowerCase()){
            case "espada": ruta = "/objetos/espada.png"; break;
            case "escudo": ruta = "/objetos/escudo.png"; break;
            case "llave":  ruta = "/objetos/llave.png"; break;
            default:       ruta = "/objetos/pocionVida.png";
        }
        return cargaImagen(ruta);
    }

    private int deltaADireccion(int row, int col){
        if(row == -1) return DIR_UP;
        if(row == 1)  return DIR_DOWN;
        if(col == 1) return DIR_LEFT;
        if(col == -1)  return DIR_RIGHT;
        return direccionActual;
    }

    private Image getJugadorSprite(){
        if(animacionAtaque){
            return imgJugadorAtaque[direccionActual][frameAtaque];
        }
        Zona zona = partida.getZonaActual();
        Posicion pos = partida.getJugador().getPosicion();
        if(zona != null && zona.esValida(pos.getRow(), pos.getCol())){
            Celda celda = zona.getCelda(pos.getRow(), pos.getCol());
            if(celda != null && celda.getTipoCelda() == TipoCelda.AGUA){
                return imgJugadorAgua;
            }
        }
        return imgJugadorAndar[direccionActual][frameAndar];
    }

    private Image getGatoSprite(){
        Gato gato = partida.getGato();
        if(gato == null) return imgGato;

        if(partida.isGatoEncontrado()){
            Posicion posGato = gato.getPosicion();
            Posicion posJug = partida.getJugador().getPosicion();
            if(posGato != null && posJug != null){
                int dr = posJug.getRow() - posGato.getRow();
                int dc = posJug.getCol() - posGato.getCol();
                int dirGato;
                if(Math.abs(dr) >= Math.abs(dc)){
                    dirGato = dr > 0 ? DIR_DOWN : DIR_UP;
                } else {
                    dirGato = dc > 0 ? DIR_RIGHT : DIR_LEFT;
                }
                return imgGatoAndar[dirGato][frameAndar % 4];
            }
        }
        return imgGato;
    }

    private static final String[][] JUGADOR_ANDAR = {
            {"jugador_down00","jugador_down01","jugador_down02","jugador_down03","jugador_down04","jugador_down05"},
            {"jugador_up00","jugador_up01","jugador_up02","jugador_up03","jugador_up04","jugador_up05"},
            {"jugador_left00","jugador_left01","jugador_left02","jugador_left03","jugador_left04","jugador_left05"},
            {"jugador_left00","jugador_left01","jugador_left02","jugador_left03","jugador_left04","jugador_left05"} // RIGHT = mirror de LEFT
    };

    private static final String[][] JUGADOR_ATAQUE = {
            {"jugador_atakDown00","jugador_atakDown01","jugador_atakDown02","jugador_atakDown03","jugador_atakDown04","jugador_atakDown05"},
            {"jugador_atakUp00","jugador_atakUp01","jugador_atakUp02","jugador_atakUp03","jugador_atakUp04","jugador_atakUp05"},
            {"jugador_atakLeft00","jugador_atakLeft01","jugador_atakLeft02","jugador_atakLeft03","jugador_atakLeft04","jugador_atakLeft05","jugador_atakLeft06"},
            {"jugador_atakLeft00","jugador_atakLeft01","jugador_atakLeft02","jugador_atakLeft03","jugador_atakLeft04","jugador_atakLeft05","jugador_atakLeft06"} // RIGHT = mirror
    };

    private static final String[][] GATO_ANDAR = {
            {"gato_down00","gato_down01","gato_down02","gato_down03"},
            {"gato_up00","gato_up01","gato_up02","gato_up03"},
            {"gato_left00","gato_left01","gato_left02","gato_left03"},
            {"gato_right00","gato_right01","gato_right02","gato_right03"} // El gato SÍ tiene right
    };

    private static final int[] JUGADOR_ANDAR_FRAMES = {6, 6, 6, 6};
    private static final int[] JUGADOR_ATAQUE_FRAMES = {6, 6, 7, 7};
    private static final int[] GATO_ANDAR_FRAMES = {4, 4, 4, 4};
}
