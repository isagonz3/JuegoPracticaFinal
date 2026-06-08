package jueguito.juegopracticafinal.Controladores.Juego;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import jueguito.juegopracticafinal.App.JueguitoFX;
import jueguito.juegopracticafinal.Modelo.Core.LoadJSON;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Vista.MapaRenderer;
import jueguito.juegopracticafinal.Vista.SpriteManager;
import jueguito.juegopracticafinal.Vista.UIRenderer;
import java.io.File;

//Controlador principal del juego: se encarga de coordinar toda la logica de la partida en ejecución, siendo el intermediario entre la vista y el modelo
public class JuegoController {

    // ATRIBUTOS FXML

    @FXML private Label zonaLabel;
    @FXML private Label turnoLabel;
    @FXML private Label vidaLabel;
    @FXML private ProgressBar vidaBar;
    @FXML private Label ataqueLabel;
    @FXML private Label defensaLabel;
    @FXML private TextArea logArea;
    @FXML private GridPane mapaGrid;
    @FXML private StackPane mapaStackPane;
    @FXML private ScrollPane mapaScrollPane;

    @FXML private Button terminarTurnoBtn;
    @FXML private Button guardarBtn;
    @FXML private Button entregarGatoBtn;

    @FXML private ListView<Objeto> inventarioList;
    @FXML private Button usarBtn;
    @FXML private Button equiparBtn;
    @FXML private Button atacarBtn;
    @FXML private Button interactuarBtn;

    @FXML private VBox tiendaPanel;
    @FXML private Button comprarBarcaBtn;
    @FXML private Button comprarMapaBtn;
    @FXML private Button salirTiendaBtn;

    @FXML private TextArea objetosUsadosArea;
    @FXML private TextArea objetosEquipadosArea;

    @FXML private Button deshacerMovimientoBtn;


    // DATOS

    private Partida partida;
    private JueguitoFX app;

    private int baseRow = 1;
    private int baseCol = 0;


    // MÓDULOS

    private JuegoControllerAcciones acciones;
    private JuegoControllerInventario inventarioCtrl;

    private SpriteManager spriteManager;
    private MapaRenderer mapaRenderer;
    private UIRenderer uiRenderer;


    // MÉTODOS

    //Inicializa el controlador del juego con la partida actual y la aplicación, crea las acciones, el inventario renderizado, inicia el turno y configura la interfaz
    public void inicializar(Partida partida, JueguitoFX app) {

        this.partida = partida;
        this.app = app;

        this.acciones = new JuegoControllerAcciones(this, partida, app);
        this.inventarioCtrl = new JuegoControllerInventario(this, partida);

        this.spriteManager = new SpriteManager();
        this.mapaRenderer = new MapaRenderer(mapaGrid, mapaStackPane, mapaScrollPane,spriteManager);

        this.uiRenderer = new UIRenderer(
                zonaLabel, turnoLabel, vidaLabel, vidaBar,
                ataqueLabel, defensaLabel,
                logArea, inventarioList,
                objetosUsadosArea, objetosEquipadosArea
        );

        partida.setUi(this.uiRenderer);

        partida.iniciarTurno();

        uiRenderer.actualizarUI(partida);
        mapaRenderer.render(partida);


        configEventos();
    }

    //Configura todos los eventos de la interfaz gráfica asociando los botones con acciones del juego
    private void configEventos() {

        terminarTurnoBtn.setOnAction(e -> acciones.terminarTurno());

        guardarBtn.setOnAction(e -> guardaPartida());

        inventarioList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
                inventarioCtrl.seleccionarObjeto(newVal)
        );

        usarBtn.setOnAction(e -> inventarioCtrl.usarObjeto());
        equiparBtn.setOnAction(e -> inventarioCtrl.equiparObjeto());

        atacarBtn.setOnAction(e -> acciones.atacarBoton());
        interactuarBtn.setOnAction(e -> acciones.interactuarBoton());

        comprarBarcaBtn.setOnAction(e -> acciones.comprar("Barca", 5));
        comprarMapaBtn.setOnAction(e -> acciones.comprar("Mapa", 3));
        salirTiendaBtn.setOnAction(e -> acciones.cerrarTienda());

        deshacerMovimientoBtn.setOnAction(e -> onDeshacerMovimiento());
    }

    //Dirige los eventos del teclado a las acciones del juego
    public void manejarTecla(KeyEvent event) {
        acciones.manejarTecla(event);
    }

    //Guarda la partida actual en un archivo JSON con el estado actual del juego
    private void guardaPartida() {

        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar Partida");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );
        fc.setInitialFileName("partida.json");

        File file = fc.showSaveDialog(app.getStage());

        if (file != null) {
            LoadJSON.guardarPartida(partida, file.getAbsolutePath());
            partida.getLog().registrar("Partida guardada en " + file.getName());
            uiRenderer.actualizarUI(partida);
        }
    }

    //Deshace el ultimo movimiento del jugador recuperando la posición en la que estaba si no se ha cambiado de zona
    @FXML
    private void onDeshacerMovimiento() {

        Posicion anterior = partida.getLog().deshacerMovimiento(partida.getZonaActual());

        if (anterior == null) {
            System.out.println("No hay movimientos para deshacer");
            return;
        }

        Zona zonaActual = partida.getZonaActual();
        Jugador jugador = partida.getJugador();

        Posicion actual = jugador.getPosicion();

        zonaActual.getCelda(actual.getRow(), actual.getCol()).setEntidad(null);

        jugador.moverA(anterior);
        zonaActual.getCelda(anterior.getRow(), anterior.getCol()).setEntidad(jugador);

        mapaRenderer.render(partida);
    }


    // GETTERS Y SETTERS

    public Partida getPartida() { return partida; }
    public JueguitoFX getApp() { return app; }

    public int getBaseRow() { return baseRow; }
    public int getBaseCol() { return baseCol; }
    public void setBaseRow(int r) { baseRow = r; }
    public void setBaseCol(int c) { baseCol = c; }

    public TextArea getLogArea() { return logArea; }
    public VBox getTiendaPanel() { return tiendaPanel; }
    public ListView<Objeto> getInventarioList() { return inventarioList; }

    public JuegoControllerAcciones getAcciones() { return acciones; }
    public UIRenderer getUiRenderer() { return uiRenderer; }
    public MapaRenderer getMapaRenderer() { return mapaRenderer; }
    public GridPane getMapaGrid() { return mapaGrid; }
}