package jueguito.juegopracticafinal.Modelo.Entidades;

public class Enemigo extends Entidad {

    private boolean atacado = false;

    public Enemigo(String nombre, Estadisticas estadisticas) {
        super(nombre, estadisticas);
    }

    @Override
    public String getTipoEntidad() {
        return "ENEMIGO";
    }

    public boolean isAtacado() {
        return atacado;
    }

    public void setAtacado(boolean atacado) {
        this.atacado = atacado;
    }
}