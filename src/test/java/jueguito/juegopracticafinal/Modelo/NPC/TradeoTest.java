package jueguito.juegopracticafinal.Modelo.NPC;

import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TradeoTest {

    @Test void constructor() {
        Objeto pedido = new Objeto("A", TipoObjeto.CONSUMIBLE);
        Objeto ofrecido = new Objeto("B", TipoObjeto.CONSUMIBLE);
        Tradeo t = new Tradeo(pedido, ofrecido);
        assertEquals(pedido, t.getObjetoPedido());
        assertEquals(ofrecido, t.getObjetoOfrecido());
    }

    @Test void constructorVacio() {
        Tradeo t = new Tradeo();
        assertNull(t.getObjetoPedido());
        assertNull(t.getObjetoOfrecido());
    }

    @Test void ofertaValida() {
        Jugador j = new Jugador("Link", new Estadisticas(100, 10, 5, 5), new Posicion(0, 0));
        Objeto pedido = new Objeto("Llave", TipoObjeto.CONSUMIBLE);
        j.getInventario().addObjeto(pedido);
        Tradeo t = new Tradeo(pedido, new Objeto("Tesoro", TipoObjeto.CONSUMIBLE));
        assertTrue(t.ofertaValida(j));
    }

    @Test void ofertaInvalida() {
        Jugador j = new Jugador("Link", new Estadisticas(100, 10, 5, 5), new Posicion(0, 0));
        Objeto pedido = new Objeto("Inexistente", TipoObjeto.CONSUMIBLE);
        Tradeo t = new Tradeo(pedido, new Objeto("Tesoro", TipoObjeto.CONSUMIBLE));
        assertFalse(t.ofertaValida(j));
    }

    @Test void tradearExitoso() {
        Jugador j = new Jugador("Link", new Estadisticas(100, 10, 5, 5), new Posicion(0, 0));
        Objeto pedido = new Objeto("Manzana", TipoObjeto.CONSUMIBLE);
        Objeto ofrecido = new Objeto("Espada", TipoObjeto.EQUIPABLE);
        j.getInventario().addObjeto(pedido);
        Tradeo t = new Tradeo(pedido, ofrecido);
        assertTrue(t.tradear(j));
        assertFalse(j.getInventario().containsObjeto("Manzana"));
        assertTrue(j.getInventario().containsObjeto("Espada"));
    }

    @Test void tradearSinObjeto() {
        Jugador j = new Jugador("Link", new Estadisticas(100, 10, 5, 5), new Posicion(0, 0));
        Tradeo t = new Tradeo(new Objeto("Manzana", TipoObjeto.CONSUMIBLE), new Objeto("Espada", TipoObjeto.EQUIPABLE));
        assertFalse(t.tradear(j));
    }


}
