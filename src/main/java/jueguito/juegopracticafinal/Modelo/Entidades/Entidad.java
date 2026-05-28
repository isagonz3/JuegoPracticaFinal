package jueguito.juegopracticafinal.Modelo.Entidades;

import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;

public abstract class  Entidad {

    //ATRIBUTOS

    protected Estadisticas estadisticas;
    protected Posicion posicion;
    protected String nombre;


    protected Entidad(String nombre, Estadisticas estadisticas, Posicion posicion) {
        this.nombre = nombre;
        this.estadisticas = estadisticas;
        this.posicion = posicion;
    }

    protected Entidad(String nombre, Estadisticas estadisticas) {
        this.nombre = nombre;
        this.estadisticas = estadisticas;
        this.posicion = null;
    }

    public Entidad() {
    }


    //MÉTODOS

    //Indica si la entidad sigue viva según sus estadísticas
    public boolean estarVivo(){
        return estadisticas != null && estadisticas.estaVivo();
    }

    //Aplica daño recibido a la entidad reduciendo sus estadísticas de vida
    public void recibirAtaque(int puntos){
        if (estadisticas != null){
            estadisticas.recibirAtaque(puntos);
        }
    }

    //Mueve la entidad a una nueva posición si es válida
    public boolean moverA(Posicion nueva){
        if(nueva == null){
            return false;
        }
        this.posicion = nueva;
        return true;
    }

    //Devuelve una representación en texto de la entidad con su nombre y estadísticas
    @Override
    public String toString() {
        if(estadisticas != null) {
            return nombre + " " + estadisticas.toString();
        }
        return " ";
    }


    //GETTERS Y SETTERS

    public String getNombre() {
        return nombre;
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public int getAtaqueTotal(){
        if(estadisticas != null){
            return estadisticas.getAtaqueBase();
        }
        return 0;
    }

    public int getDefensaTotal(){
        if(estadisticas != null){
            return estadisticas.getDefensaBase();
        }
        return 0;
    }
}