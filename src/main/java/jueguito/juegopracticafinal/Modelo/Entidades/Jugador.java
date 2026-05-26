package jueguito.juegopracticafinal.Modelo.Entidades;

import jueguito.juegopracticafinal.Modelo.Inventario.Inventario;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Log.LogMovimiento;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;

public class Jugador extends Entidad{

    public Inventario inventario;
    private Objeto[] equipamiento;
    private LogMovimiento log;
    private int bonusAtaqueEq;
    private int bonusDefensaEq;
    private int bonusRangoEq;

    public Jugador(String nombre, Estadisticas estadisticas, Posicion posicion) {
        super(nombre, estadisticas, posicion);
        this.inventario = new Inventario();
        this.equipamiento = new Objeto[2];
    }

    public Jugador() {
        super();
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
        bonusAtaqueEq = 0;
        bonusDefensaEq = 0;
        bonusRangoEq = 0;

        for (Objeto objeto : equipamiento) {
            if (objeto != null) {
                bonusAtaqueEq += objeto.getAtaqueBonus();
                bonusDefensaEq += objeto.getDefensaBonus();
                bonusRangoEq += objeto.getRangoBonus();
            }
        }
    }


    public int getDefensaEscudo() {
        Objeto escudo = inventario.getEquipado(SlotEquipable.ESCUDO);
        return (escudo != null) ? escudo.getDefensaBonus() : 0;
    }


    @Override
    public int getAtaqueTotal(){
        return estadisticas.getAtaqueBase() + bonusAtaqueEq;
    }

    @Override
    public int getDefensaTotal(){
        return estadisticas.getDefensaBase() + bonusDefensaEq;
    }

    public int getRangoTotal(){
        return estadisticas.getRangoMov() + bonusRangoEq;
    }

    public int getBonusAtaqueEq() {
        return bonusAtaqueEq;
    }

    public int getBonusDefensaEq() {
        return bonusDefensaEq;
    }

    public int getBonusRangoEq() {
        return bonusRangoEq;
    }

    public void setLog(LogMovimiento log) {
        this.log = log;
    }
    public LogMovimiento getLog() {
        return log;
    }

    @Override
    public String getTipoEntidad(){
        return "JUGADOR";
    }

}
