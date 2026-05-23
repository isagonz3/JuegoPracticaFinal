package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;

public class AccionUsarObj extends Accion {
    private final Objeto objeto;

    public AccionUsarObj(Objeto objeto) {
        this.objeto = objeto;
    }

    @Override
    public boolean ejecutar(Partida partida) {
        return partida.usarObjeto(objeto);
    }

    @Override
    public String getDescripcion() {
        return "Usar " + objeto.getNombre();
    }
}