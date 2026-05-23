package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;

public class AccionInteract extends Accion {
    @Override
    public boolean ejecutar(Partida partida) {
        return partida.interactNPC();
    }

    @Override
    public String getDescripcion() {
        return "Interactuar con NPC";
    }
}