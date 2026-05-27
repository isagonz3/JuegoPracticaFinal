package jueguito.juegopracticafinal.Modelo.Entidades;

public class Gato extends Entidad {

    public Gato(String nombre, Estadisticas estadisticas) {
        super(nombre, estadisticas);
    }

    @Override
    public String getTipoEntidad() {
        return "GATO";
    }
}