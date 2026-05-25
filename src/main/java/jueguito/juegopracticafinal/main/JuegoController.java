package jueguito.juegopracticafinal.main;

import javafx.fxml.FXML;
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
import jueguito.juegopracticafinal.Modelo.Core.LoadJSON;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.TADs.Lista;

import java.io.File;

public class JuegoController {

    @FXML private Label zonaLabel;
    @FXML private Label turnoLabel;
    @FXML private Label vidaLabel;
    @FXML private ProgressBar vidaBar;
    @FXML private TextArea logArea;
    @FXML private GridPane mapaGrid;

    @FXML private Button terminarTurnoBtn;
    @FXML private Button guardarBtn;
    @FXML private Button usarBtn;
    @FXML private Button equiparBtn;

    // NUEVOS BOTONES
    @FXML private Button atacarBtn;
    @FXML private Button defenderBtn;

    @FXML private ListView<String> inventarioList;

    private Partida partida;
    private JueguitoFX app;

    private Image imgJugador;
    private Image imgGato;
    private Image imgEnemigo1;

    private int baseRow = 1;
    private int baseCol = 0;

    private boolean ataqueRealizado = false;

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

    public void inicializar(Partida partida, JueguitoFX app) {

        this.partida = partida;
        this.app = app;

        imgJugador = cargaImagen("/jugador/jugador_down00.png");
        imgGato = cargaImagen("/gato/gato_quieto.png");
        imgEnemigo1 = cargaImagen("/enemigos/soldado_quieto.png");

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

        for (String[] nombre : MAPA_PNG) {

            if (Integer.parseInt(nombre[0]) == idZona) {
                nombreArchivo = nombre[1];
                break;
            }
        }

        if (nombreArchivo == null) {
            return;
        }

        Image mapa = new Image(
                getClass().getResourceAsStream("/zonas/" + nombreArchivo + ".png")
        );

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

                WritableImage tile = new WritableImage(
                        pixelReader,
                        j * TILE_SIZE,
                        i * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                );

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

                    celdaPane.getChildren().add(
                            new Circle(TILE_SIZE * 0.35, Color.BLUEVIOLET)
                    );
                }

                if (celda != null && celda.tieneObjeto()) {

                    celdaPane.getChildren().add(
                            new Circle(TILE_SIZE * 0.35, Color.RED)
                    );
                }

                if (checkCelda != null && checkCelda[i][j]) {

                    Rectangle ilum = new Rectangle(
                            TILE_SIZE,
                            TILE_SIZE,
                            Color.rgb(206, 0, 100, 0.25)
                    );

                    celdaPane.getChildren().add(ilum);
                }

                int row = i;
                int col = j;

                celdaPane.setOnMouseClicked(
                        (e) -> manejarClickCelda(row, col)
                );

                mapaGrid.add(celdaPane, j, i);
            }
        }
    }

    private void manejarClickCelda(int i, int j) {

        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) {
            return;
        }

        Zona zona = partida.getZonaActual();

        if (!zona.esValida(i, j)) {
            return;
        }

        Celda celda = zona.getCelda(i, j);

        if (celda == null) {
            return;
        }

        Posicion posJugador = partida.getJugador().getPosicion();

        Celda celdaJugador = zona.getCelda(
                posJugador.getRow(),
                posJugador.getCol()
        );

        if (i == celdaJugador.getRow() &&
                j == celdaJugador.getCol()) {

            return;
        }
    }

    void manejarTecla(KeyEvent event) {

        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) {
            return;
        }

        switch (event.getCode()) {

            case W:
            case UP:
                baseRow = -1;
                baseCol = 0;
                moverTecla();
                break;

            case S:
            case DOWN:
                baseRow = 1;
                baseCol = 0;
                moverTecla();
                break;

            case A:
            case LEFT:
                baseRow = 0;
                baseCol = -1;
                moverTecla();
                break;

            case D:
            case RIGHT:
                baseRow = 0;
                baseCol = 1;
                moverTecla();
                break;

            case SPACE:
            case ENTER:
                atacarTecla();
                break;

            default:
                return;
        }

        event.consume();
    }

    private void moverTecla() {

        Posicion pos = partida.getJugador().getPosicion();

        if (partida.moverJugador(
                pos.getRow() + baseRow,
                pos.getCol() + baseCol
        )) {

            actualizarUI();
            renderMapa();

            partida.findGato();
        }
    }


    private void atacarTecla() {

        partida.atacarDireccion(baseRow, baseCol);

        actualizarUI();
        renderMapa();
    }


    private void atacarBoton() {

        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) {
            return;
        }

        // SOLO UN ATAQUE POR TURNO
        if (ataqueRealizado) {

            partida.getLog().registrar(
                    "Ya has realizado un ataque en este turno."
            );

            actualizarUI();
            return;
        }

        Zona zona = partida.getZonaActual();

        Posicion posJugador = partida.getJugador().getPosicion();

        int filaObjetivo = posJugador.getRow() + baseRow;
        int colObjetivo = posJugador.getCol() + baseCol;

        if (!zona.esValida(filaObjetivo, colObjetivo)) {

            partida.getLog().registrar(
                    "No hay ningún objetivo en esa dirección."
            );

            actualizarUI();
            return;
        }

        Celda celdaObjetivo = zona.getCelda(
                filaObjetivo,
                colObjetivo
        );

        if (celdaObjetivo == null ||
                !celdaObjetivo.tieneEntidad() ||
                !(celdaObjetivo.getEntidad() instanceof Enemigo)) {

            partida.getLog().registrar(
                    "No hay enemigos para atacar."
            );

            actualizarUI();
            return;
        }

        Enemigo enemigo = (Enemigo) celdaObjetivo.getEntidad();

        int vidaEnemigoAntes =
                enemigo.getEstadisticas().getVidaActual();

        // ATAQUE DEL JUGADOR
        partida.atacarDireccion(baseRow, baseCol);

        int vidaEnemigoDespues =
                enemigo.getEstadisticas().getVidaActual();

        int danoRealizado =
                vidaEnemigoAntes - vidaEnemigoDespues;

        partida.getLog().registrar(
                "Has hecho " +
                        danoRealizado +
                        " de daño. Vida restante del enemigo: " +
                        Math.max(vidaEnemigoDespues, 0)
        );

        ataqueRealizado = true;

        actualizarUI();
        renderMapa();

        // VIDA DEL JUGADOR ANTES DEL TURNO ENEMIGO
        int vidaJugadorAntes =
                partida.getJugador()
                        .getEstadisticas()
                        .getVidaActual();

        // TURNO ENEMIGO
        terminaTurno();

        // VIDA DESPUÉS DEL ATAQUE ENEMIGO
        int vidaJugadorDespues =
                partida.getJugador()
                        .getEstadisticas()
                        .getVidaActual();

        int danoRecibido =
                vidaJugadorAntes - vidaJugadorDespues;

        // MENSAJES ENEMIGO
        if (danoRecibido > 0) {

            partida.getLog().registrar(
                    "El enemigo te ha atacado y te ha quitado "
                            + danoRecibido +
                            " puntos de vida."
            );
        }

        else {

            partida.getLog().registrar(
                    "El enemigo ha huido o no ha conseguido atacarte."
            );
        }

        ataqueRealizado = false;

        actualizarUI();
        renderMapa();
    }


    // NUEVO MÉTODO DEFENDER BOTÓN
    private void defenderBoton() {

        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) {
            return;
        }

        partida.getLog().registrar(
                "El jugador adopta una posición defensiva."
        );

        actualizarUI();
        renderMapa();
    }

    private void actualizarUI() {

        Jugador jugador = partida.getJugador();
        Zona zona = partida.getZonaActual();

        zonaLabel.setText(
                "Zona: " +
                        zona.getNombreZona() +
                        "(" +
                        zona.getIdZona() +
                        ")"
        );

        turnoLabel.setText(
                "Turno: " +
                        partida.getTurnoActual() +
                        " Puntos Movimiento (PM): " +
                        jugador.getEstadisticas().getPuntosMovDisponibles()
        );

        vidaLabel.setText(
                "Vida: " +
                        jugador.getEstadisticas().getVidaActual() +
                        "/" +
                        jugador.getEstadisticas().getVidaMax()
        );

        vidaBar.setProgress(
                (double) jugador.getEstadisticas().getVidaActual() /
                        jugador.getEstadisticas().getVidaMax()
        );

        String lastLog = partida.getLog().getUltimaEntrada();

        if (!lastLog.isEmpty()) {
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

            inventarioList.getItems().add(
                    jug.getInventario().getObjeto(i).toString()
            );
        }
    }

    private void configEventos() {

        terminarTurnoBtn.setOnAction(e -> terminaTurno());

        guardarBtn.setOnAction(e -> guardaPartida());

        // NUEVOS BOTONES
        atacarBtn.setOnAction(e -> atacarBoton());

        defenderBtn.setOnAction(e -> defenderBoton());

        inventarioList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {

                    if (newVal == null) {

                        objetoSeleccionado = null;
                        return;
                    }

                    objetoSeleccionado =
                            partida.getJugador()
                                    .getInventario()
                                    .findObjeto(newVal);
                });

        usarBtn.setOnAction(e -> {

            if (objetoSeleccionado == null) {
                return;
            }

            if (partida.usarObjeto(objetoSeleccionado)) {

                renderMapa();
                actualizarUI();
            }
        });

        equiparBtn.setOnAction(e -> {

            if (objetoSeleccionado == null) {
                return;
            }

            SlotEquipable slot = objetoSeleccionado.getSlot();

            if (slot == null) {

                partida.getLog().registrar(
                        "Este objeto no es equipable"
                );

                actualizarUI();

                return;
            }

            if (partida.equiparObjeto(
                    objetoSeleccionado,
                    slot
            )) {

                renderMapa();
                actualizarUI();
            }
        });
    }

    private void terminaTurno() {

        partida.terminarTurno();

        if (partida.getEstadoActual() == EstadoJuego.VICTORIA) {

            app.irAEnd(
                    "VICTORIA: Has encontrado al gato y llegado al castillo.",
                    partida.getLog().getEntradas()
            );

            return;
        }

        if (partida.getEstadoActual() == EstadoJuego.DERROTA) {

            app.irAEnd(
                    "DERROTA: " +
                            partida.getLog().getUltimaEntrada(),
                    partida.getLog().getEntradas()
            );

            return;
        }

        actualizarUI();
        renderMapa();
    }

    private void guardaPartida() {

        FileChooser fc = new FileChooser();

        fc.setTitle("Guardar Partida");

        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );

        fc.setInitialFileName("partida.json");

        File file = fc.showSaveDialog(app.getStage());

        if (file != null) {

            LoadJSON.guardarPartida(
                    partida,
                    file.getAbsolutePath()
            );

            partida.getLog().registrar(
                    "Partida guardada en " + file.getName()
            );

            actualizarUI();
        }
    }

    private Image cargaImagen(String ruta) {

        var is = getClass().getResourceAsStream(ruta);

        return is != null ? new Image(is) : null;
    }
}