package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import org.jetbrains.annotations.NotNull;

import static jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda.PUERTA;
import static jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda.VACIA;

public class Celda implements Comparable<Celda> {
    private int row;
    private int col;
    private boolean isTransitable;

    private TipoCelda tipoCelda;
    private Entidad entidad;
    private Objeto objeto;
    private NPC npc;
    private boolean vacia;
    private boolean ilum;

    public Celda(TipoCelda tipoCelda, Entidad entidad) {
        this.tipoCelda = tipoCelda;
        this.entidad = entidad;
    }

    public TipoCelda getTipoCelda() {
        return tipoCelda;
    }

    public void setTipoCelda(TipoCelda tipoCelda) {
        this.tipoCelda = tipoCelda;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public InfoPuerta getInfoPuerta() {
        return new InfoPuerta();
    }

    public boolean isTransitable() {
        return tipoCelda != TipoCelda.PARED;
    }

    public boolean isOcupada(){
        return this.entidad != null;
    }

    public boolean tieneEntidad(){
        return this.entidad != null;
    }

    public boolean tieneObjeto(){
        return this.objeto != null;
    }

    public boolean tieneNPC(){
        return this.npc != null;
    }

    public boolean esAdyacente(Celda celda) {
        if(celda == null){
            return false;
        }
        return Math.abs(this.row-celda.row) + Math.abs(this.col-celda.col) == 1;
    }

    public int getDistancia(Celda celda) {
        if(celda == null){
            return Integer.MAX_VALUE;
        }
        return Math.abs(this.row-celda.row) + Math.abs(this.col-celda.col);
    }

    @Override
    public int compareTo(@NotNull Celda o) {
        return 0;
    }
}
