package jueguito.juegopracticafinal.Modelo.Inventario;

public class Objeto {

    //ATRIBUTOS

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
    private int turnosActivos = 0;
    private int duracionTurnos = -1;
    private static int contador = 0;
    private int id;
    private int cantidad = 1;


    public Objeto() {
        this.nombre = "Objeto";
        this.tipo = TipoObjeto.USABLE;
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
        this.id = contador++;
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


    //MÉTODOS

    //Reduce en uno los usos restantes del objeto si todavía puede ser utilizado
    public boolean usarObjeto() {
        if (usosRestantes <= 0) {
            return false;
        }
        usosRestantes--;
        return true;
    }

    //Devuelve una representación en texto del objeto mostrando su nombre y cantidad si es mayor que uno
    @Override
    public String toString() {
        if (cantidad > 1) {
            return nombre + " x" + cantidad;
        }
        return nombre;
    }


    //GETTERS Y SETTERS

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoObjeto getTipo() {
        return tipo;
    }

    public SlotEquipable getSlot() {
        return slot;
    }

    public void setSlot(SlotEquipable slot) {
        this.slot = slot;
    }

    public int getAtaqueBonus() {
        return ataqueBonus;
    }

    public int getDefensaBonus() {
        return defensaBonus;
    }

    public int getRangoBonus() {
        return rangoBonus;
    }

    public int getVidaBonus() {
        return vidaBonus;
    }

    public int getUsosRestantes() {
        return usosRestantes;
    }

    public int getTurnosActivos() {
        return turnosActivos;
    }

    public void incrementarTurno() {
        turnosActivos++;
    }

    public void resetTurnos() {
        turnosActivos = 0;
    }

    public void setDuracionTurnos(int duracion) {
        this.duracionTurnos = duracion;
    }

    public int getDuracionTurnos() {
        return duracionTurnos;
    }
}