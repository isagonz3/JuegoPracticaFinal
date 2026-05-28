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
import jueguito.juegopracticafinal.TADs.Matrix;

//Renderiza visualmente el mapa del juego: dibuja el escenario sobre un GridPane utilizando spirtes para el fondo, entidades, NPCs, objetos y celdas
public class MapaRenderer {

    private final GridPane mapaGrid;
    private final StackPane mapaStackPane;
    private final SpriteManager sprites;
    private final Lista<Matrix<Image>> cacheCeldas =  new Lista<>();

    private static final int TILE_SIZE_SOURCE = 16;


    //Es el constructor del renderizador del mapa que inicializa el GridPane donde se dibujará el mapa y los sprites utilizados para cargar las imágenes
    public MapaRenderer(GridPane mapaGrid, StackPane mapaStackPane, SpriteManager sprites) {
        this.mapaGrid = mapaGrid;
        this.mapaStackPane = mapaStackPane;
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

        double viewportW = mapaStackPane.getWidth();
        double viewportH = mapaStackPane.getHeight();
        if (viewportW <= 0 || viewportH <= 0) {
            viewportW = 850;
            viewportH = 610;
        }
        double tileSize = Math.min(viewportW / cols, viewportH / rows);

        Lista<Celda> accesibles = partida.getCeldasAccesibles();
        boolean[][] checkCelda = null;

        if (accesibles != null && accesibles.getSize() > 0) {
            checkCelda = new boolean[rows][cols];

            for (int i = 0; i < accesibles.getSize(); i++) {
                Celda c = accesibles.get(i);
                checkCelda[c.getRow()][c.getCol()] = true;
            }
        }

        boolean[][] caminoCelda = null;
        if (partida.isMapaActivo() && partida.getCaminoMapa() != null) {
            caminoCelda = new boolean[rows][cols];
            for (int i = 0; i < partida.getCaminoMapa().getSize(); i++) {
                Celda c = partida.getCaminoMapa().get(i);
                if (c != null) {
                    caminoCelda[c.getRow()][c.getCol()] = true;
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {



                Image tile = getTile(zonaImg,idZona,i,j);

                ImageView tileView = new ImageView(tile);
                tileView.setFitWidth(tileSize);
                tileView.setFitHeight(tileSize);

                StackPane celdaPane = new StackPane(tileView);

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
                    Rectangle ilum = new Rectangle(
                            tileSize,
                            tileSize,
                            Color.rgb(206, 0, 100, 0.25)
                    );

                    celdaPane.getChildren().add(ilum);
                }

                if (caminoCelda != null && caminoCelda[i][j]) {
                    Rectangle caminoMapa = new Rectangle(
                            tileSize / 2,
                            tileSize / 2,
                            Color.rgb(250, 0, 60, 0.4)
                    );
                    celdaPane.getChildren().add(caminoMapa);
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

    private Image getTile(Image imagenZona, int idZona, int row, int col) {
        while(cacheCeldas.getSize() <= idZona) {
            cacheCeldas.add(null);
        }

        Matrix<Image> zonaCache = cacheCeldas.get(idZona);
        if(zonaCache == null){
            int rows = (int)(imagenZona.getHeight() / TILE_SIZE_SOURCE);
            int cols = (int)(imagenZona.getWidth() / TILE_SIZE_SOURCE);
            zonaCache = new Matrix<>(rows, cols);
            PixelReader pr = imagenZona.getPixelReader();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    zonaCache.set(r, c,
                            new WritableImage(pr, c * TILE_SIZE_SOURCE, r * TILE_SIZE_SOURCE, TILE_SIZE_SOURCE, TILE_SIZE_SOURCE)
                    );
                }
            }
            cacheCeldas.set(idZona, zonaCache);
        }
        return zonaCache.get(row, col);
    }
}

