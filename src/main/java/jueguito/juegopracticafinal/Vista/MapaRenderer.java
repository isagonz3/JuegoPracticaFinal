package jueguito.juegopracticafinal.Vista;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Iterador;
import jueguito.juegopracticafinal.TADs.Lista;

import static jueguito.juegopracticafinal.Modelo.Core.Configuracion.get;

public class MapaRenderer {

    private final GridPane mapaGrid;
    private final SpriteManager sprites;


    private static final int TILE_PIX = get().ui.tileSize;

    public MapaRenderer(GridPane mapaGrid, SpriteManager sprites) {
        this.mapaGrid = mapaGrid;
        this.sprites = sprites;
        mapaGrid.setHgap(0);
        mapaGrid.setVgap(0);
        mapaGrid.setStyle("-fx-grid-lines-visible: false;");
        mapaGrid.setBackground(Background.EMPTY);

    }

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

        int maxAnchoMapa = 554;
        int maxAltoMapa  = 500;
        int tileW = maxAnchoMapa / cols;
        int tileH = maxAltoMapa  / rows;
        int tileSize = Math.min(tileW, tileH);
        tileSize = Math.max(12, Math.min(32, tileSize));

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
                        j * TILE_PIX,
                        i * TILE_PIX,
                        TILE_PIX,
                        TILE_PIX
                );

                ImageView tileView = new ImageView(tile);
                tileView.setFitWidth(tileSize);
                tileView.setFitHeight(tileSize);
                tileView.setSmooth(false);

                StackPane celdaPane = new StackPane(tileView);
                celdaPane.setBackground(Background.EMPTY);
                celdaPane.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
                celdaPane.setPadding(Insets.EMPTY);

                Celda celda = zona.getCelda(i, j);

                if (celda != null && celda.tieneEntidad()) {
                    ImageView entView = getEntidadSprite(celda);
                    if (entView != null) {
                        entView.setFitWidth(tileSize);
                        entView.setFitHeight(tileSize);
                        celdaPane.getChildren().add(entView);
                    }
                }

                if (celda != null && celda.tieneNPC()) {
                    ImageView npcView = new ImageView(
                            sprites.getNPCSprite(celda.getNpc().getNombre())
                    );
                    npcView.setFitWidth(tileSize);
                    npcView.setFitHeight(tileSize);
                    celdaPane.getChildren().add(npcView);
                }

                if (celda != null && celda.tieneObjeto()) {
                    ImageView objView = new ImageView(
                            sprites.getObjetoSprite(celda.getObjeto().getNombre())
                    );
                    objView.setFitWidth(tileSize);
                    objView.setFitHeight(tileSize);
                    celdaPane.getChildren().add(objView);
                }

                if (checkCelda != null && checkCelda[i][j]) {
                    Rectangle ilum = new Rectangle(tileSize, tileSize, Color.rgb(206, 0, 100, 0.25));
                    celdaPane.getChildren().add(ilum);
                }

                mapaGrid.add(celdaPane, j, i);
            }
        }
    }


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
