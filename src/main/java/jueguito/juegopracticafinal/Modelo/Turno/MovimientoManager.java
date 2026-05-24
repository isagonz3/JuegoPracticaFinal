package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.Lista;

public class MovimientoManager {
    private Partida partida;

    public MovimientoManager(Partida partida){
        this.partida = partida;
    }


    public boolean moverJugador(int row,int col){
        if(partida.getEstadoActual() != EstadoJuego.EN_CURSO){
            return false;
        }

        Zona zonaActual = partida.getZonaActual();
        Celda destino = zonaActual.getCelda(row,col);
        if(destino == null || !destino.isTransitable() || destino.isOcupada()){
            partida.getLog().registrar("No puedes moverte a " + destino.getTipoCelda());
            return false;
        }

        Jugador jugador = partida.getJugador();
        Posicion actual = jugador.getPosicion();
        int puntosMov = jugador.getEstadisticas().getPuntosMovDisponibles();
        Lista<Celda> accesibles = zonaActual.getCeldasAccesibles(actual.getRow(), actual.getCol(),puntosMov);

        boolean accesible = false;
        for(int i = 0; i < accesibles.getSize(); i++){
            Celda celda = accesibles.get(i);
            if(celda.getRow() == row && celda.getCol() == col){
                accesible = true;
                break;
            }
        }
        if(!accesible){
            partida.getLog().registrar("No tienes suficientes puntos de movimiento (PM)");
            return false;
        }

        int coste = calcularDist(actual.getRow(), actual.getCol(),row,col,zonaActual);
        if(!jugador.getEstadisticas().usarMovimiento(coste)){
            return  false;
        }

        Celda origen = zonaActual.getCelda(actual.getRow(),actual.getCol());
        origen.setEntidad(null);
        jugador.moverA(new Posicion(row,col));
        destino.setEntidad(jugador);
        partida.setMovimientoRealizado(true);

        if (destino.tienePuerta()) partida.cambiarZona(destino.getPuerta());
        if(destino.tieneObjeto()) partida.recogerObjeto(destino);
        return true;
    }

    private int calcularDist(int row1, int col1, int row2, int col2,Zona zonaActual){
        boolean[][] visitadas = new boolean[zonaActual.getRows()][zonaActual.getCols()];
        Cola<int[]> cola = new Cola<>();

        cola.enqueue(new int[]{row1,col1,0});
        visitadas[row1][col1] = true;

        while(!cola.isEmpty()){
            int[] actual = cola.dequeue();
            int row = actual[0];
            int col = actual[1];
            int data = actual[2];

            if(row == row2 && col == col2){
                return data;
            }

            int[][] coordenadas = {{-1,0}, {1,0}, {0,-1}, {0,1}}; //Norte, Sur, Oeste, Este
            for(int[] coordenada : coordenadas){
                int newRow = row + coordenada[0];
                int newCol = col + coordenada[1];

                if(zonaActual.esValida(newRow,newCol) && !visitadas[newRow][newCol]){
                    Celda celda = zonaActual.getCelda(newRow,newCol);
                    if(celda != null && celda.isTransitable() && !celda.isOcupada()){
                        visitadas[newRow][newCol] = true;
                        cola.enqueue(new int[]{newRow,newCol,actual[2] +1});
                    }
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    public Lista<Celda> getCeldasAccesibles() {
        if (partida.getJugador() == null || partida.getZonaActual() == null) return new Lista<>();
        return partida.getZonaActual().getCeldasAccesibles(
                partida.getJugador().getPosicion().getRow(),
                partida.getJugador().getPosicion().getCol(),
                partida.getJugador().getEstadisticas().getPuntosMovDisponibles());
    }

    public Lista<Celda> getCaminoMinimo(int destinoRow, int destinoCol) {
        Jugador jugador = partida.getJugador();
        if(jugador == null) return new Lista<>();

        Zona zonaActual = partida.getZonaActual();
        if(zonaActual == null) return new Lista<>();

        return zonaActual.getCaminoMinimo(
                jugador.getPosicion().getRow(),jugador.getPosicion().getCol(),destinoRow,destinoCol
        );
    }

}
