package jueguito.juegopracticafinal.Modelo.Mundo;

public class Posicion {
    private int idZona;
    private int row;
    private int col;

    public Posicion(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
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
