package jueguito.juegopracticafinal.Modelo.Mundo;

public class Posicion {
    private int idZona;
    private int row;
    private int col;

    public Posicion(int idZona, int row) {
        this.idZona = idZona;
        this.row = row;
        this.col = col;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
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

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Posicion posicion = (Posicion) o;

        return idZona == posicion.idZona && row == posicion.row && col == posicion.col;
    }

    public String toString(){
        return idZona + "-" + row + "-" + col;
    }
}
