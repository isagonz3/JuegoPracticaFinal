package jueguito.juegopracticafinal.Modelo.Inventario;

public class Objeto {
    private String nombre;
    private TipoObjeto tipo;
    private SlotEquipable slot;
    private int ataqueBonus;
    private int defensaBonus;
    private int vidaBonus;
    private int rangoBonus;
    private int usosMax;
    private int usosRestantes;
    private String descripcion;

    public Objeto() {
        this.nombre = "Objeto";
        this.tipo = TipoObjeto.CONSUMIBLE;
        this.slot = null;
        this.usosMax = 1;
        this.usosRestantes = 1;
        this.descripcion = "";
    }

    public Objeto(String nombre, TipoObjeto tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public Objeto(String nombre, TipoObjeto tipo, int ataqueBonus, int defensaBonus, int vidaBonus, int rangoBonus, int usosMax, String descripcion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ataqueBonus = ataqueBonus;
        this.defensaBonus = defensaBonus;
        this.vidaBonus = vidaBonus;
        this.rangoBonus = rangoBonus;
        this.usosMax = usosMax;
        this.usosRestantes = usosMax;
        this.descripcion = descripcion;
    }

    public Objeto(String espada) {
        //placeholder
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoObjeto getTipo() { return tipo; }
    public void setTipo(TipoObjeto tipo) { this.tipo = tipo; }

    public SlotEquipable getSlot() { return slot; }
    public void setSlot(SlotEquipable slot) { this.slot = slot; }

    public int getAtaqueBonus() { return ataqueBonus; }
    public int getDefensaBonus() { return defensaBonus; }
    public int getRangoBonus() { return rangoBonus; }
    public int getVidaBonus(){ return vidaBonus; }
    public int getUsosMax() { return usosMax; }
    public int getUsosRestantes() { return usosRestantes; }
    public String getDescripcion() { return descripcion; }

    public boolean isConsumible() { return this.tipo == TipoObjeto.CONSUMIBLE || this.tipo == TipoObjeto.LLAVE; }

    public boolean usarObjeto(){
        if(usosRestantes <= 0){
            return false;
        }
        usosRestantes--;
        return true;
    }

    public boolean objetoGastado(){
        return isConsumible() && usosRestantes <= 0;
    }

    public Objeto copiar(){
        Objeto copia = new Objeto(nombre,tipo,ataqueBonus,defensaBonus,vidaBonus,rangoBonus,usosMax,descripcion);
        copia.slot = this.slot;
        return copia;
    }


    @Override
    public String toString() {
        return nombre;
    }
}