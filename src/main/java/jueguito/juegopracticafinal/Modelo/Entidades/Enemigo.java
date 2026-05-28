package jueguito.juegopracticafinal.Modelo.Entidades;

public class Enemigo extends Entidad {

    //ATRIBUTOS

    private boolean atacado = false;


    public Enemigo(String nombre, Estadisticas estadisticas) {
        super(nombre, estadisticas);
    }


    //MÉTODOS

    //Indica si el enemigo ha sido atacado
    public boolean isAtacado() {
        return atacado;
    }


    //GETTERS Y SETTERS

    public String getTipoEntidad() {
        return "ENEMIGO";
    }

    public void setAtacado(boolean atacado) {
        this.atacado = atacado;
    }
}