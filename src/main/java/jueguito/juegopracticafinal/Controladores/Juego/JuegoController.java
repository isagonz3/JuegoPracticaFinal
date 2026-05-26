package jueguito.juegopracticafinal.Controladores.Juego;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import jueguito.juegopracticafinal.App.JueguitoFX;
import jueguito.juegopracticafinal.Modelo.Core.LoadJSON;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Vista.MapaRenderer;
import jueguito.juegopracticafinal.Modelo.Vista.SpriteManager;
import jueguito.juegopracticafinal.Modelo.Vista.UIRenderer;

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

    private int baseRow = 1;
    private int baseCol = 0;

    // ============================
    // MÓDULOS
    // ============================

    private JuegoControllerAcciones acciones;
    private JuegoControllerInventario inventarioCtrl;

    private SpriteManager spriteManager;
    private MapaRenderer mapaRenderer;
    private UIRenderer uiRenderer;

    // ============================
    // INICIALIZACIÓN
    // ============================

    public void inicializar(Partida partida, JueguitoFX app) {

        this.partida = partida;
        this.app = app;

        // Controladores lógicos
        this.acciones = new JuegoControllerAcciones(this, partida, app);
        this.inventarioCtrl = new JuegoControllerInventario(this, partida);

        // Renderers
        this.spriteManager = new SpriteManager();
        this.mapaRenderer = new MapaRenderer(mapaGrid, spriteManager);
        this.uiRenderer = new UIRenderer(
                zonaLabel, turnoLabel, vidaLabel, vidaBar,
                logArea, inventarioList, objetosUsadosArea, objetosEquipadosArea

        );

        partida.iniciarTurno();

        uiRenderer.actualizarUI(partida);
        mapaRenderer.render(partida);

        configEventos();
    }

    public void terminarTurno() {
        acciones.terminarTurno();
    }

    // ============================
    // EVENTOS
    // ============================

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
    }

    // ============================
    // TECLADO
    // ============================

    public void manejarTecla(KeyEvent event) {
        acciones.manejarTecla(event);
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
            uiRenderer.actualizarUI(partida);
        }
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

    public JuegoControllerAcciones getAcciones() { return acciones; }
    public UIRenderer getUiRenderer() { return uiRenderer; }
    public MapaRenderer getMapaRenderer() { return mapaRenderer; }
}