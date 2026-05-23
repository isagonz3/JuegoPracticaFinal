package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;

public class AccionMover extends Accion {
    private final int rowDestino;
    private final int colDestino;

    public AccionMover(int rowDestino, int colDestino) {
        this.rowDestino = rowDestino;
        this.colDestino = colDestino;
    }

    public int getRowDestino() { return rowDestino; }
    public int getColDestino() { return colDestino; }

    @Override
    public boolean ejecutar(Partida partida) {
        return partida.moverJugador(rowDestino, colDestino);
    }

    @Override
    public String getDescripcion() {
        return "Mover a (" + rowDestino + "," + colDestino + ")";
    }
}