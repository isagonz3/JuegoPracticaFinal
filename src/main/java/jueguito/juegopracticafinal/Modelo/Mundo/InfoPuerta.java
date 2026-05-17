package jueguito.juegopracticafinal.Modelo.Mundo;

public class InfoPuerta {
    private int idZonaDestino;
    private int rowDestino;
    private int colDestino;
    private boolean necesitaLlave;
    private String idLlave;

    public InfoPuerta(){
        this.idZonaDestino = 0;
        this.rowDestino = 0;
        this.colDestino = 0;
        this.necesitaLlave = false;
        this.idLlave = "";
    }

    public int getIdZonaDestino() {
        return idZonaDestino;
    }

    public void setIdZonaDestino(int idZonaDestino) {
        this.idZonaDestino = idZonaDestino;
    }

    public int getRowDestino() {
        return rowDestino;
    }

    public void setRowDestino(int rowDestino) {
        this.rowDestino = rowDestino;
    }

    public int getColDestino() {
        return colDestino;
    }

    public void setColDestino(int colDestino) {
        this.colDestino = colDestino;
    }

    public boolean isNecesitaLlave() {
        necesitaLlave = true;
        return true;
    }


}
