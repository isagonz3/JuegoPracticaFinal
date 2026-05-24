package jueguito.juegopracticafinal.Modelo.Entidades;

import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;

public abstract class  Entidad {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(Estadisticas estadisticas) {
        this.estadisticas = estadisticas;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public boolean estarVivo(){
        return estadisticas != null && estadisticas.estaVivo();
    }

    public void recibirAtaque(int puntos){
        if(estadisticas != null){
            estadisticas.recibirAtaque(puntos);
        }
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

    public boolean moverA(Posicion nueva){
        if(nueva == null){
            return false;
        }
        this.posicion = nueva;
        return true;
    }

    public abstract String getTipoEntidad();

    @Override
    public String toString() {
        if(estadisticas != null) {
            return nombre + " " + estadisticas.toString();
        }
        return " ";
    }


}
