package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;

public class AccionRecogerObj extends Accion {
    private final Celda celda;

    public AccionRecogerObj(Celda celda) {
        this.celda = celda;
    }

    @Override
    public boolean ejecutar(Partida partida) {
        return partida.recogerObjeto(celda);
    }

    @Override
    public String getDescripcion() {
        return "Recoger objeto";
    }
}