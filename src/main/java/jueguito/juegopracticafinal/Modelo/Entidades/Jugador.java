package jueguito.juegopracticafinal.Modelo.Entidades;

import jueguito.juegopracticafinal.Modelo.Inventario.Inventario;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Log.LogSistema;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;

public class Jugador extends Entidad{

    private Inventario inventario;
    private Objeto[] equipamiento;
    private LogSistema log;

    public Jugador(String nombre, Estadisticas estadisticas, Posicion posicion) {
        super(nombre, estadisticas, posicion);
        this.inventario = new Inventario();
        this.equipamiento = new Objeto[2];
    }

    public Inventario getInventario() {
        return inventario;
    }

    public Objeto getEquipamiento(SlotEquipable slot){
        return equipamiento[slot.ordinal()];
    }

    public Objeto[] getEquipamiento(){
        return equipamiento;
    }

    public boolean equipar(Objeto objeto, SlotEquipable slot) {
        if(objeto.getTipo() != TipoObjeto.EQUIPABLE){
            return false;
        }

        Objeto actual = equipamiento[slot.ordinal()];
        if(actual != null && !inventario.addObjeto(actual)){
                return false;
        }

        if(!inventario.removeObjeto(objeto)){
            return false;
        }
        equipamiento[slot.ordinal()] = objeto;
        addBonusEquipamiento();
        return true;

    }

    public void quitarEquipamiento(SlotEquipable slot){
        Objeto actual = equipamiento[slot.ordinal()];
        if(actual != null && inventario.addObjeto(actual)){
            equipamiento[slot.ordinal()] = null;
        }
        addBonusEquipamiento();
    }

    private void addBonusEquipamiento(){
        int ataque = estadisticas.getAtaqueBase();
        int defensa = estadisticas.getDefensaBase();
        int rango = estadisticas.getRangoMov();

        for (Objeto objeto : equipamiento) {
            if (objeto != null) {
                ataque += objeto.getAtaqueBonus();
                defensa += objeto.getDefensaBonus();
                rango += objeto.getRangoBonus();
            }
        }

        estadisticas.setAtaqueBase(ataque);
        estadisticas.setDefensaBase(defensa);
        estadisticas.setRangoMov(rango);
    }

    public void setLog(LogSistema log) {
        this.log = log;
    }

    public LogSistema getLog() {
        return log;
    }

    @Override
    public String getTipoEntidad(){
        return "JUGADOR";
    }

}
