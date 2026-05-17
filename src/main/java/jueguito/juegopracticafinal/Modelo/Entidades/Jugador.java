package jueguito.juegopracticafinal.Modelo.Entidades;

import jueguito.juegopracticafinal.Modelo.Inventario.Inventario;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;

public class Jugador extends Entidad{

    protected Inventario inventario;



    protected Jugador(String nombre, Estadisticas estadisticas, Posicion posicion) {
        super(nombre, estadisticas, posicion);
    }

    @Override
    public String getTipoEntidad() {
        return "";
    }
}
