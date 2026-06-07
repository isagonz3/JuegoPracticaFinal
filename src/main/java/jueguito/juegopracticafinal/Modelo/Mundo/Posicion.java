package jueguito.juegopracticafinal.Modelo.Mundo;

//Representa una posición dentro del mundo almacenando la zona, fila y columna correspondientes
public class Posicion {

    //ATRIBUTOS

    private int idZona;
    private int row;
    private int col;


    public Posicion(int row, int col) {
        this.row = row;
        this.col = col;
    }


    //MÉTODOS

    //Compara dos posiciones verificando si pertenecen a la misma zona y tienen las mismas coordenadas
    @Override
    public boolean equals(Object o){

        if(this == o) {
            return true;
        }

        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Posicion posicion = (Posicion) o;

        return idZona == posicion.idZona &&
                row == posicion.row &&
                col == posicion.col;
    }

    @Override
    public int hashCode() {
        return 31 * (31 * idZona + row) + col;
    }

    //Devuelve una representación en texto de la posición con el formato zona-fila-columna
    public String toString(){
        return idZona + "-" + row + "-" + col;
    }


    //GETTERS Y SETTERS

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}