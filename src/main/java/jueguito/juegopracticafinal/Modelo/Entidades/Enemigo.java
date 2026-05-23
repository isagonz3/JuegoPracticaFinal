package jueguito.juegopracticafinal.Modelo.Entidades;

public class Enemigo extends Entidad {

    public Enemigo(String nombre, Estadisticas estadisticas) {
        super(nombre, estadisticas);
    }

    @Override
    public String getTipoEntidad() {
        return "ENEMIGO";
    }
}
