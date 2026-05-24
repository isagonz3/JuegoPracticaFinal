package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Mundo.*;

public class GatoManager {
    private Partida partida;
    private static final int[] ZONAS_GATO = {2, 4, 6, 7, 8};

    public GatoManager(Partida partida){
        this.partida = partida;
    }

    public void moverGato(){
        Gato gato = partida.getGato();

        if(partida.isGatoEncontrado()){
            seguirJugador(gato);
        }
        else{
            moverGatoAleatorio(gato);
        }
    }

    private void seguirJugador(Gato gato){
        Posicion posJugador = partida.getJugador().getPosicion();
        Posicion posGato = gato.getPosicion();

        if(partida.getIdZonaActual() != zonaGato(posGato)){
            return;
        }

        Zona zonaActual = partida.getZonaActual();
        int newRow = posGato.getRow() + Integer.signum(posJugador.getRow() - posGato.getRow());
        int newCol = posGato.getCol() + Integer.signum(posJugador.getCol() - posGato.getCol());

        if(zonaActual.esValida(newRow, newCol)){
            Celda celda = zonaActual.getCelda(newRow, newCol);
            if(celda.isTransitable() && !celda.isOcupada()){
                zonaActual.getCelda(posGato.getRow(),posGato.getCol()).setEntidad(null);
                gato.moverA(new Posicion(newRow, newCol));
                celda.setEntidad(gato);
            }
        }
    }

    private void moverGatoAleatorio(Gato gato){
        Posicion posGato = gato.getPosicion();
        int idZona = zonaGato(posGato);
        if (!zonasPermitidas(idZona)) return;
        Zona zonaGato = partida.getGrafo().getZona(idZona);
        if (zonaGato == null) return;

        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        int d = (int)(Math.random() * 4);
        int nr = posGato.getRow() + dirs[d][0], nc = posGato.getCol() + dirs[d][1];
        if (zonaGato.esValida(nr, nc)) {
            Celda c = zonaGato.getCelda(nr, nc);
            if (c.isTransitable() && !c.isOcupada()) {
                zonaGato.getCelda(posGato.getRow(), posGato.getCol()).setEntidad(null);
                gato.moverA(new Posicion(nr, nc));
                c.setEntidad(gato);
            }
        }
    }



    public boolean findGato(){
        if(partida.isGatoEncontrado()){
            return true;
        }

        Gato gato = partida.getGato();
        if(zonaGato(gato.getPosicion()) != partida.getIdZonaActual()){
            return false;
        }

        Posicion posGato = gato.getPosicion();
        Posicion posJugador = partida.getJugador().getPosicion();

        if(Math.abs(posJugador.getRow() - posGato.getRow()) <= 1 && Math.abs(posJugador.getCol() - posGato.getCol()) <= 1){
            partida.setGatoEncontrado(true);
            partida.getLog().registrar("Encontraste al gato! Toca llevarlo hasta la princesa.");
            return true;
        }
        return false;
    }

    public Posicion spawnGato(){
        int z = ZONAS_GATO[(int)(Math.random()*ZONAS_GATO.length)];
        Zona zonaGato = partida.getGrafo().getZona(z);

        for(int intentos = 0; intentos < 50; intentos++){
            int row = (int)(Math.random()*zonaGato.getRows());
            int col = (int)(Math.random()*zonaGato.getCols());

            Celda celda = zonaGato.getCelda(row, col);
            if(celda != null && celda.isTransitable() && !celda.isOcupada() && celda.getTipoCelda() != TipoCelda.AGUA){
                return new Posicion(row, col);
            }
        }
        return null;
    }

    private int zonaGato(Posicion pos) {
        for (int i = 0; i < 10; i++) {
            Zona zona = partida.getGrafo().getZona(i);
            if (zona != null && zona.esValida(pos.getRow(), pos.getCol())) {
                Celda c = zona.getCelda(pos.getRow(), pos.getCol());
                if (c != null && c.getEntidad() == partida.getGato()) return i;
            }
        }
        return -1;
    }

    private boolean zonasPermitidas(int idZona) {
        for (int z : ZONAS_GATO) if (z == idZona) return true;
        return false;
    }
}
