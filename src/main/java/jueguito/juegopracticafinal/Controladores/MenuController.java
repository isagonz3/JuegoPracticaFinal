package jueguito.juegopracticafinal.Controladores;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import jueguito.juegopracticafinal.App.JueguitoFX;
import jueguito.juegopracticafinal.Modelo.Core.LoadJSON;
import jueguito.juegopracticafinal.Modelo.Core.MapaLoader;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Mundo.GrafoZonas;
import jueguito.juegopracticafinal.Modelo.Mundo.Puerta;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Lista;

import java.io.File;

//Controlador del menú principal del juego: gestiona la creación de nuevas partidas, la carga de partidas guardadas con archivos JSON y la salida de la aplicación
public class MenuController {

    // ATRIBUTOS

    @FXML private Button nuevaPartidaBtn;
    @FXML private Button cargarPartidaBtn;
    @FXML private Button salirBtn;

    private JueguitoFX app;

    private static final String[] NOMBRES_ZONAS = {
            "InteriorHabitacion", "ExteriorHabitacion", "BosqueOeste", "Aldea",
            "ExteriorTienda", "InteriorTienda", "Lago", "BosqueEste",
            "ExteriorCastillo", "InteriorCastillo"
    };


    // GETTERS Y SETTERS

    public void setApp(JueguitoFX app) {
        this.app = app;
    }


    // MÉTODOS

    //Inicializa el controlador del menú: asigna las acciones de los botones para crear una partida nueva, cargar partida desde archivo o salir del juego
    @FXML
    private void initialize() {

        nuevaPartidaBtn.setOnAction(e -> {
            GrafoZonas grafo = crearGrafo();
            Partida partida = new Partida(grafo);
            partida.iniciar();
            app.irAJuego(partida);
        });

        cargarPartidaBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Cargar Partida");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Archivos JSON", "*.json")
            );
            fc.setInitialDirectory(new File(System.getProperty("user.dir")));

            File file = fc.showOpenDialog(app.getStage());

            if (file != null) {
                GrafoZonas grafo = crearGrafo();
                Partida partida = LoadJSON.cargarPartida(file.getAbsolutePath(), grafo);

                if (partida != null) {
                    app.irAJuego(partida);
                }
            }
        });

        salirBtn.setOnAction(e -> Platform.exit());
    }

    //Crea el grafo de zonas del juego a partir de los datos cargados asignando nombres a las zonas, las añade al grafo y conecta las puertas entre ellas
    private GrafoZonas crearGrafo() {

        Lista<Zona> zonas = MapaLoader.cargarZonas();
        GrafoZonas grafo = new GrafoZonas();

        for (int i = 0; i < zonas.getSize(); i++) {
            Zona zona = zonas.get(i);

            int id = zona.getIdZona();
            if (id >= 0 && id < NOMBRES_ZONAS.length) {
                zona.setNombreZona(NOMBRES_ZONAS[id]);
            }

            grafo.addZona(zona);
        }

        Lista<Puerta> puertas = MapaLoader.cargarPuertas();
        MapaLoader.conectarPuertas(grafo, puertas);

        return grafo;
    }
}