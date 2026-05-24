package jueguito.juegopracticafinal.main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import jueguito.juegopracticafinal.Modelo.Core.LoadJSON;
import jueguito.juegopracticafinal.Modelo.Core.MapaLoader;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Mundo.GrafoZonas;
import jueguito.juegopracticafinal.Modelo.Mundo.Puerta;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Lista;

import java.io.File;

public class MenuController {
    @FXML
    private Button nuevaPartidaBtn, cargarPartidaBtn, salirBtn;
    private JueguitoFX app;

    public void setApp(JueguitoFX app) { this.app = app; }

    private GrafoZonas crearGrafo(){
        Lista<Zona> zonas = MapaLoader.cargarZonas();
        GrafoZonas grafo = new GrafoZonas();

        for(int i = 0; i < zonas.getSize(); i++){
            Zona zona = zonas.get(i);
            zona.setIdZona(i);
            switch (i) {
                case 0: zona.setNombreZona("InteriorHabitacion"); break;
                case 1: zona.setNombreZona("ExteriorHabitacion"); break;
                case 2: zona.setNombreZona("BosqueOeste"); break;
                case 3: zona.setNombreZona("Aldea"); break;
                case 4: zona.setNombreZona("ExteriorTienda"); break;
                case 5: zona.setNombreZona("InteriorTienda"); break;
                case 6: zona.setNombreZona("Lago"); break;
                case 7: zona.setNombreZona("BosqueEste"); break;
                case 8: zona.setNombreZona("ExteriorCastillo"); break;
                case 9: zona.setNombreZona("InteriorCastillo"); break;
            }
            grafo.addZona(zona);
        }
        Lista<Puerta> puertas = MapaLoader.cargarPuertas();
        MapaLoader.conectarPuertas(grafo, puertas);
        return grafo;
    }

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
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JSON", "*.json"));
            fc.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fc.showOpenDialog(app.getStage());

            if (file != null) {
                GrafoZonas grafo = crearGrafo();
                Partida partida = LoadJSON.cargarPartida(file.getAbsolutePath(), grafo);

                if(partida != null){
                    app.irAJuego(partida);
                }
            }


        });
        salirBtn.setOnAction(e -> Platform.exit());
    }
}
