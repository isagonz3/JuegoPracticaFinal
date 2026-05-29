package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;

//Representa una celda del mapa almacenando su posición, tipo y los elementos que puede contener
public class Celda {

    //ATRIBUTOS

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


    //MÉTODOS

    //Comprueba si la celda puede ser transitada según su tipo
    public boolean isTransitable() {
        return tipoCelda == TipoCelda.SUELO || tipoCelda == TipoCelda.PUERTA || tipoCelda == TipoCelda.AGUA || tipoCelda == TipoCelda.SALIDA;
    }

    //Comprueba si la celda contiene una puerta
    public boolean tienePuerta() {
        return puerta != null;
    }

    //Comprueba si la celda está ocupada por una entidad
    public boolean isOcupada() {
        return entidad != null;
    }

    //Comprueba si la celda contiene una entidad
    public boolean tieneEntidad() {
        return entidad != null;
    }

    //Comprueba si la celda contiene un objeto
    public boolean tieneObjeto() {
        return objeto != null;
    }

    //Comprueba si la celda contiene un NPC
    public boolean tieneNPC() {
        return npc != null;
    }

    //Comprueba si una celda es adyacente a otra
    public boolean esAdyacente(Celda otra) {
        if (otra == null) return false;

        return Math.abs(this.row - otra.row) + Math.abs(this.col - otra.col) == 1;
    }

    //Comprueba si una celda se encuentra a una distancia concreta de otra
    public boolean esAdyacenteDistancia(Celda otra, int dist) {

        int dx = Math.abs(this.row - otra.row);
        int dy = Math.abs(this.col - otra.col);

        return (dx + dy) == dist;
    }


    //GETTERS Y SETTERS

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

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public TipoCelda getTipoCelda() {
        return tipoCelda;
    }

    public void setTipoCelda(TipoCelda tipoCelda) {
        this.tipoCelda = tipoCelda;
    }

    public Puerta getPuerta() {
        return puerta;
    }

    public void setPuerta(Puerta puerta) {
        this.puerta = puerta;
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

    public NPC getNpc() {
        return npc;
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }
}