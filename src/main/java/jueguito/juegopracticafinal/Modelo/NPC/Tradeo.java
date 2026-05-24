package jueguito.juegopracticafinal.Modelo.NPC;

import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;

public class Tradeo {
    private Objeto objetoPedido;
    private Objeto objetoOfrecido;

    public Tradeo(Objeto objetoPedido, Objeto objetoOfrecido){
        this.objetoPedido = objetoPedido;
        this.objetoOfrecido = objetoOfrecido;
    }

    public Tradeo() {
    }

    public Objeto getObjetoPedido() {
        return objetoPedido;
    }

    public Objeto getObjetoOfrecido() {
        return objetoOfrecido;
    }

    public boolean ofertaValida(Jugador jugador){
        return jugador.getInventario().containsObjeto(objetoPedido.getNombre());
    }

    public boolean tradear(Jugador jugador){
        if(!ofertaValida(jugador)){
            return false;
        }
        Objeto pedido = jugador.getInventario().findObjeto(objetoPedido.getNombre());
        if(pedido == null){
            return false;
        }
        jugador.getInventario().removeObjeto(pedido);
        jugador.getInventario().addObjeto(objetoOfrecido);
        return true;
    }
}
