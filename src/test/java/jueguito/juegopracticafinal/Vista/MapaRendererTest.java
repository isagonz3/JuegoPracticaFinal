package jueguito.juegopracticafinal.Vista;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.GrafoZonas;
import jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapaRendererTest {

    private Partida partidaPrueba() {
        GrafoZonas grafo=new jueguito.juegopracticafinal.Modelo.Mundo.GrafoZonas();
        grafo.addZona(zonaPrueba(0));
        Partida partida=new Partida(grafo);
        partida.iniciar();
        return partida;
    }


    private Zona zonaPrueba(int id){
        Celda[][] celdas=new Celda[3][3];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                celdas[i][j]=new Celda(TipoCelda.SUELO);
            }
        }
        return new Zona(id, "Z"+id, celdas);
    }

    @Test
    void constructorDebeCrearMapaRenderer(){

        GridPane grid=new GridPane();

        SpriteManager sprites=new SpriteManager();

        MapaRenderer renderer=new MapaRenderer(grid,new StackPane(),sprites);

        assertNotNull(renderer);
    }

    @Test
    void renderNoDebeRomper(){

        GridPane grid=new GridPane();

        SpriteManager sprites=new SpriteManager();

        MapaRenderer renderer=new MapaRenderer(grid,new StackPane(),sprites);

        Partida partida=partidaPrueba();

        assertDoesNotThrow(() -> renderer.render(partida));
    }

    @Test
    void mapaGridNoDebeSerNull(){

        GridPane grid=new GridPane();

        assertNotNull(grid);
    }

    @Test
    void zonaPruebaDebeCrearseCorrectamente(){

        Zona zona=zonaPrueba(1);

        assertEquals(1,zona.getIdZona());
        assertEquals("Z1",zona.getNombreZona());
    }

    @Test
    void renderDebeAceptarPartidaValida(){

        GridPane grid=new GridPane();

        SpriteManager sprites=new SpriteManager();

        MapaRenderer renderer=new MapaRenderer(grid,new StackPane(),sprites);

        Partida partida=partidaPrueba();

        renderer.render(partida);

        assertNotNull(grid);
    }
}