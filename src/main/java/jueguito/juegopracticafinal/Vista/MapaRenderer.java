package jueguito.juegopracticafinal.Vista;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Lista;

//Renderiza visualmente el mapa del juego: dibuja el escenario sobre un GridPane utilizando spirtes para el fondo, entidades, NPCs, objetos y celdas
public class MapaRenderer {

    private final GridPane mapaGrid;
    private final SpriteManager sprites;

    private static final int TILE_SIZE = 16;


    //Es el constructor del renderizador del mapa que inicializa el GridPane donde se dibujará el mapa y los sprites utilizados para cargar las imágenes
    public MapaRenderer(GridPane mapaGrid, SpriteManager sprites) {
        this.mapaGrid = mapaGrid;
        this.sprites = sprites;
    }


    //Renderiza el estado actual de la partida dibujando el fondo de la zona, las entidades, objetos y celdas accesibles para el jugador coloreadas
    public void render(Partida partida) {

        mapaGrid.getChildren().clear();

        Zona zona = partida.getZonaActual();
        int idZona = zona.getIdZona();

        Image zonaImg = sprites.getZonaBackground(idZona);
        if (zonaImg == null) return;

        PixelReader pixelReader = zonaImg.getPixelReader();
        if (pixelReader == null) return;

        int rows = zona.getRows();
        int cols = zona.getCols();

        Lista<Celda> accesibles = partida.getCeldasAccesibles();
        boolean[][] checkCelda = null;

        if (accesibles != null && accesibles.getSize() > 0) {
            checkCelda = new boolean[rows][cols];

            for (int i = 0; i < accesibles.getSize(); i++) {
                Celda c = accesibles.get(i);
                checkCelda[c.getRow()][c.getCol()] = true;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                WritableImage tile = new WritableImage(
                        pixelReader,
                        j * TILE_SIZE,
                        i * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                );

                ImageView tileView = new ImageView(tile);
                tileView.setFitWidth(TILE_SIZE);
                tileView.setFitHeight(TILE_SIZE);

                StackPane celdaPane = new StackPane(tileView);

                Celda celda = zona.getCelda(i, j);

                if (celda != null && celda.tieneEntidad()) {
                    ImageView entView = getEntidadSprite(celda);

                    if (entView != null) {
                        entView.setFitWidth(TILE_SIZE);
                        entView.setFitHeight(TILE_SIZE);
                        celdaPane.getChildren().add(entView);
                    }
                }

                if (celda != null && celda.tieneNPC()) {
                    ImageView npcView = new ImageView(
                            sprites.getNPCSprite(celda.getNpc().getNombre())
                    );

                    npcView.setFitWidth(TILE_SIZE);
                    npcView.setFitHeight(TILE_SIZE);
                    celdaPane.getChildren().add(npcView);
                }

                if (celda != null && celda.tieneObjeto()) {
                    ImageView objView = new ImageView(
                            sprites.getObjetoSprite(celda.getObjeto().getNombre())
                    );

                    objView.setFitWidth(TILE_SIZE);
                    objView.setFitHeight(TILE_SIZE);
                    celdaPane.getChildren().add(objView);
                }

                if (checkCelda != null && checkCelda[i][j]) {
                    Rectangle ilum = new Rectangle(
                            TILE_SIZE,
                            TILE_SIZE,
                            Color.rgb(206, 0, 100, 0.25)
                    );

                    celdaPane.getChildren().add(ilum);
                }

                mapaGrid.add(celdaPane, j, i);
            }
        }
    }


    //Obtiene el sprite correspondiente a cada entidad en las celdas y devuelve la imagen según su tipo
    private ImageView getEntidadSprite(Celda celda) {

        Entidad e = celda.getEntidad();

        if (e instanceof Jugador) {
            return new ImageView(sprites.getJugadorSprite());
        }

        if (e instanceof Gato) {
            return new ImageView(sprites.getGatoSprite());
        }

        if (e instanceof Enemigo) {
            return new ImageView(sprites.getEnemigoSprite());
        }

        return null;
    }
}