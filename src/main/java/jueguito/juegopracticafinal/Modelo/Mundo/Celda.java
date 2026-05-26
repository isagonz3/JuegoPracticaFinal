package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;

public class Celda {
    private int row;
    private int col;
    private TipoCelda tipoCelda;
    private Puerta puerta;
    private Entidad entidad;
    private Objeto objeto;
    private NPC npc;

    public Celda(TipoCelda tipo) {
        this.tipoCelda = tipo;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public TipoCelda getTipoCelda() {
        return tipoCelda;
    }

    public void setTipoCelda(TipoCelda tipoCelda) {
        this.tipoCelda = tipoCelda;
    }

    public boolean isTransitable() {
        return tipoCelda == TipoCelda.SUELO || tipoCelda == TipoCelda.PUERTA || tipoCelda == TipoCelda.AGUA || tipoCelda == TipoCelda.SALIDA;
    }

    public Puerta getPuerta() {
        return puerta;
    }

    public void setPuerta(Puerta puerta) {
        this.puerta = puerta;
    }

    public boolean tienePuerta() {
        return puerta != null;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public boolean isOcupada() {
        return entidad != null;
    }

    public boolean tieneEntidad() {
        return entidad != null;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public boolean tieneObjeto() {
        return objeto != null;
    }

    public NPC getNpc() {
        return npc;
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }

    public boolean tieneNPC() {
        return npc != null;
    }

    public boolean esAdyacente(Celda otra) {
        if (otra == null) return false;
        return Math.abs(this.row - otra.row) + Math.abs(this.col - otra.col) == 1;
    }

    public int getDistancia(Celda otra) {
        if (otra == null) return Integer.MAX_VALUE;
        return Math.abs(this.row - otra.row) + Math.abs(this.col - otra.col);
    }

    public void clear(){
        this.entidad = null;
        this.objeto = null;
        this.npc = null;
        this.puerta = null;
    }

    public boolean esAdyacenteDistancia(Celda otra, int dist) {
        int dx = Math.abs(this.row - otra.row);
        int dy = Math.abs(this.col - otra.col);

        return (dx + dy) == dist;
    }
}


