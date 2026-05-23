package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;

public class AccionEquipar extends Accion {
    private final Objeto objeto;
    private final SlotEquipable slot;

    public AccionEquipar(Objeto objeto, SlotEquipable slot) {
        this.objeto = objeto;
        this.slot = slot;
    }

    @Override
    public boolean ejecutar(Partida partida) {
        return partida.equiparObjeto(objeto, slot);
    }

    @Override
    public String getDescripcion() {
        return "Equipar " + objeto.getNombre();
    }
}