package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;

public abstract class Accion {
    public abstract boolean ejecutar(Partida partida);
    public abstract String getDescripcion();
}