package jueguito.juegopracticafinal.Controladores.Juego;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import jueguito.juegopracticafinal.App.JueguitoFX;
import jueguito.juegopracticafinal.Modelo.Core.LoadJSON;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.TADs.Lista;

import java.io.File;

public class JuegoController {

    // ============================
    // FXML
    // ============================

    @FXML private Label zonaLabel;
    @FXML private Label turnoLabel;
    @FXML private Label vidaLabel;
    @FXML private ProgressBar vidaBar;
    @FXML private TextArea logArea;
    @FXML private GridPane mapaGrid;
    @FXML private Button terminarTurnoBtn, guardarBtn;
    @FXML private ListView<Objeto> inventarioList;
    @FXML private Button usarBtn, equiparBtn;
    @FXML private Button atacarBtn;
    @FXML private Button interactuarBtn;
    @FXML private VBox tiendaPanel;
    @FXML private Button comprarBarcaBtn;
    @FXML private Button comprarMapaBtn;
    @FXML private Button salirTiendaBtn;
    @FXML private TextArea objetosUsadosArea;
    @FXML private TextArea objetosEquipadosArea;

    // ============================
    // Datos
    // ============================

    private Partida partida;
    private JueguitoFX app;

    private Image imgJugador;
    private Image imgGato;
    private Image imgEnemigo1;
    private Image imgNPC;

    private int baseRow = 1;
    private int baseCol = 0;

    private Lista<Celda> celdasAccesibles = null;
    private Objeto objetoSeleccionado;

    private JuegoControllerAcciones acciones;


    private boolean ataqueRealizado = false;

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

    // ============================
    // NUEVOS MÓDULOS
    // ============================

    private JuegoControllerInventario inventarioCtrl;

    // ============================
    // INICIALIZACIÓN
    // ============================

    public void inicializar(Partida partida, JueguitoFX app) {

        this.partida = partida;
        this.app = app;

        this.acciones = new JuegoControllerAcciones(this, partida, app);
        this.inventarioCtrl = new JuegoControllerInventario(this, partida);

        imgJugador = cargaImagen("/entidades/jugador_down00.png");
        imgGato = cargaImagen("/entidades/gato_quieto.png");
        imgEnemigo1 = cargaImagen("/entidades/soldado_quieto.png");

        partida.iniciarTurno();
        actualizarUI();
        renderMapa();
        configEventos();
    }

    public void terminarTurno() {
        acciones.terminarTurno();
    }


    // ============================
    // RENDER DEL MAPA
    // ============================

    public void renderMapa() {

        mapaGrid.getChildren().clear();
        Zona zona = partida.getZonaActual();
        int idZona = zona.getIdZona();

        String nombreArchivo = null;
        for (String[] nombre : MAPA_PNG) {
            if (Integer.parseInt(nombre[0]) == idZona) {
                nombreArchivo = nombre[1];
                break;
            }
        }

        if (nombreArchivo == null) return;

        Image mapa = new Image(getClass().getResourceAsStream("/zonas/" + nombreArchivo + ".png"));

        boolean[][] checkCelda = null;
        if (celdasAccesibles != null) {
            checkCelda = new boolean[zona.getRows()][zona.getCols()];
            for (int c = 0; c < celdasAccesibles.getSize(); c++) {
                Celda access = celdasAccesibles.get(c);
                checkCelda[access.getRow()][access.getCol()] = true;
            }
        }

        PixelReader pixelReader = mapa.getPixelReader();

        for (int i = 0; i < zona.getRows(); i++) {
            for (int j = 0; j < zona.getCols(); j++) {

                WritableImage tile = new WritableImage(pixelReader, j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                ImageView tileView = new ImageView(tile);

                StackPane celdaPane = new StackPane(tileView);
                Celda celda = zona.getCelda(i, j);

                if (celda != null && celda.tieneEntidad()) {
                    Entidad entidad = celda.getEntidad();

                    if (entidad instanceof Jugador) {
                        ImageView jugadorView = new ImageView(imgJugador);
                        jugadorView.setFitHeight(TILE_SIZE);
                        jugadorView.setFitWidth(TILE_SIZE);
                        celdaPane.getChildren().add(jugadorView);
                    }
                    else if (entidad instanceof Gato) {
                        ImageView gatoView = new ImageView(imgGato);
                        gatoView.setFitHeight(TILE_SIZE);
                        gatoView.setFitWidth(TILE_SIZE);
                        celdaPane.getChildren().add(gatoView);
                    }
                    else if (entidad instanceof Enemigo) {
                        ImageView enemigoView = new ImageView(imgEnemigo1);
                        enemigoView.setFitHeight(TILE_SIZE);
                        enemigoView.setFitWidth(TILE_SIZE);
                        celdaPane.getChildren().add(enemigoView);
                    }
                    else {
                        Circle circle = new Circle(TILE_SIZE * 0.35, Color.PINK);
                        celdaPane.getChildren().add(circle);
                    }
                }

                if (celda != null && celda.tieneNPC()) {
                    NPC npc = celda.getNpc();
                    ImageView npcView = new ImageView(seleccionarSpriteNPC(npc));
                    npcView.setFitHeight(TILE_SIZE);
                    npcView.setFitWidth(TILE_SIZE);
                    celdaPane.getChildren().add(npcView);
                }

                if (celda != null && celda.tieneObjeto()) {
                    ImageView objView = new ImageView(seleccionarSpriteObjeto(celda.getObjeto()));
                    objView.setFitHeight(TILE_SIZE);
                    objView.setFitWidth(TILE_SIZE);
                    celdaPane.getChildren().add(objView);
                }

                if (checkCelda != null && checkCelda[i][j]) {
                    Rectangle ilum = new Rectangle(TILE_SIZE, TILE_SIZE, Color.rgb(206, 0, 100, 0.25));
                    celdaPane.getChildren().add(ilum);
                }

                mapaGrid.add(celdaPane, j, i);
            }
        }
    }

    // ============================
    // EVENTOS
    // ============================

    private void configEventos() {

        terminarTurnoBtn.setOnAction(e -> acciones.terminarTurno());
        guardarBtn.setOnAction(e -> guardaPartida());

        inventarioList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            inventarioCtrl.seleccionarObjeto(newVal);
        });

        usarBtn.setOnAction(e -> inventarioCtrl.usarObjeto());
        equiparBtn.setOnAction(e -> inventarioCtrl.equiparObjeto());

        atacarBtn.setOnAction(e -> acciones.atacarBoton());
        interactuarBtn.setOnAction(e -> acciones.interactuarBoton());

        comprarBarcaBtn.setOnAction(e -> acciones.comprar("Barca", 5));
        comprarMapaBtn.setOnAction(e -> acciones.comprar("Mapa", 3));
        salirTiendaBtn.setOnAction(e -> acciones.cerrarTienda());
    }

    // ============================
    // TECLADO
    // ============================

    public void manejarTecla(KeyEvent event) {
        acciones.manejarTecla(event);
    }

    // ============================
    // UI
    // ============================

    public void actualizarUI() {

        Jugador jugador = partida.getJugador();
        Zona zona = partida.getZonaActual();

        zonaLabel.setText("Zona: " + zona.getNombreZona() + "(" + zona.getIdZona() + ")");
        turnoLabel.setText("Turno: " + partida.getTurnoActual() +
                " PM: " + jugador.getEstadisticas().getPuntosMovDisponibles());

        vidaLabel.setText("Vida: " + jugador.getEstadisticas().getVidaActual() +
                "/" + jugador.getEstadisticas().getVidaMax());

        vidaBar.setProgress(
                (double) jugador.getEstadisticas().getVidaActual() /
                        jugador.getEstadisticas().getVidaMax()
        );

        String lastLog = partida.getLog().getUltimaEntrada();
        if (!lastLog.isEmpty()) logArea.appendText(lastLog + "\n");

        if (partida.getEstadoActual() == EstadoJuego.EN_CURSO) {
            celdasAccesibles = partida.getCeldasAccesibles();
        } else {
            celdasAccesibles = null;
        }

        inventarioCtrl.actualizarInventario();
        inventarioCtrl.actualizarInventarioExtra();
    }

    // ============================
    // GUARDAR PARTIDA
    // ============================

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

    // ============================
    // UTILIDADES
    // ============================

    private Image cargaImagen(String ruta) {
        var is = getClass().getResourceAsStream(ruta);
        return is != null ? new Image(is) : null;
    }

    private Image seleccionarSpriteObjeto(Objeto objeto) {

        String ruta;

        switch (objeto.getNombre().toLowerCase()) {
            case "espada": ruta = "/objetos/espada.png"; break;
            case "escudo": ruta = "/objetos/escudo.png"; break;
            case "llave": ruta = "/objetos/llave.png"; break;
            case "gema": ruta = "/objetos/gema.png"; break;
            case "pocion de vida": ruta = "/objetos/pocionVida.png"; break;
            case "pocion de ataque": ruta = "/objetos/pocionRango.png"; break;
            default: ruta = "/objetos/pocionVida.png";
        }

        return cargaImagen(ruta);
    }

    private Image seleccionarSpriteNPC(NPC npc) {

        String ruta;

        switch (npc.getNombre().toLowerCase()) {
            case "mercader": ruta = "/entidades/NPC_1.png"; break;
            case "sirena": ruta = "/entidades/NPC_2.png"; break;
            case "guardia": ruta = "/entidades/NPC_3.png"; break;
            default: ruta = "/entidades/NPC_1.png";
        }

        return cargaImagen(ruta);
    }

    // ============================
    // GETTERS PARA LOS MÓDULOS
    // ============================

    public Partida getPartida() { return partida; }
    public JueguitoFX getApp() { return app; }
    public int getBaseRow() { return baseRow; }
    public int getBaseCol() { return baseCol; }
    public void setBaseRow(int r) { baseRow = r; }
    public void setBaseCol(int c) { baseCol = c; }
    public TextArea getLogArea() { return logArea; }
    public VBox getTiendaPanel() { return tiendaPanel; }
    public ListView<Objeto> getInventarioList() { return inventarioList; }
    public TextArea getObjetosUsadosArea() { return objetosUsadosArea; }
    public TextArea getObjetosEquipadosArea() { return objetosEquipadosArea; }
    public JuegoControllerAcciones getAcciones() {
        return acciones;
    }

}
