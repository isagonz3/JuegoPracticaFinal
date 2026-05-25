package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Puerta;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;

public class ZonaManager {
    private Partida partida;

    public ZonaManager(Partida partida){
        this.partida = partida;
    }

    public void cambiarZona(Puerta puerta){

        if(puerta == null){
            return;
        }

        int nuevaIdZona;
        int destinoRow;
        int destinoCol;

        if(puerta.getZonaOrigen() == partida.getIdZonaActual()){
            nuevaIdZona = puerta.getZonaDestino();
            destinoRow = puerta.getYDestino();
            destinoCol = puerta.getXDestino();
        }
        else{
            nuevaIdZona = puerta.getZonaOrigen();
            destinoRow = puerta.getYOrigen();
            destinoCol = puerta.getXOrigen();
        }

        Zona nuevaZona = partida.getGrafo().getZona(nuevaIdZona);
        if(nuevaZona == null){
            partida.getLog().registrar("Esta puerta no lleva a ninguna zona conocida");
            return;
        }

        if(nuevaZona.getCountTurnos() == 0){
            partida.getEnemigoManager().poblarZona(nuevaZona);
            partida.getObjetoManager().ponerObjetos(nuevaZona);
        }

        if(nuevaIdZona == 9 && !partida.isGatoEncontrado()){
            partida.getLog().registrar("DETENGAN AL INTRUSO!: Has entrado al castillo sin el gato y la guardia real te ha mandado al calabozo");
            partida.setEstadoActual(EstadoJuego.DERROTA);
            return;
        }

        Zona zonaActual = partida.getZonaActual();

        Celda origen = zonaActual.getCelda(partida.getJugador().getPosicion().getRow(),partida.getJugador().getPosicion().getCol());
        origen.setEntidad(null);

        if(!nuevaZona.isVisitada()){
            partida.setTurnoActual(Math.max(0, partida.getTurnoActual() - 1));
            partida.getLog().registrar("Has descubierto una nueva zona del mapa!");
        }

        nuevaZona.setVisitada(true);
        partida.setIdZonaActual(nuevaIdZona);

        int destRow = Math.min(Math.max(destinoRow, 0), nuevaZona.getRows()-1);
        int destCol = Math.min(Math.max(destinoCol, 0), nuevaZona.getCols()-1);

        partida.getJugador().moverA(new Posicion(destRow,destCol));
        nuevaZona.getCelda(destRow,destCol).setEntidad(partida.getJugador());

        if(partida.isGatoEncontrado()){
            Gato gato = partida.getGato();
            Celda celdaGato = zonaActual.findCelda(gato.getPosicion().getRow(), gato.getPosicion().getCol());
            if(celdaGato != null) celdaGato.setEntidad(null);

            int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
            for(int[] d : dirs){
                int cr = destRow + d[0], cc = destCol + d[1];
                if(nuevaZona.esValida(cr, cc)){
                    Celda c = nuevaZona.getCelda(cr, cc);
                    if(c != null && c.isTransitable() && !c.isOcupada()){
                        gato.moverA(new Posicion(cr, cc));
                        c.setEntidad(gato);
                        partida.setIdZonaGato(nuevaIdZona);
                        partida.getLog().registrar("El gato te ha seguido hasta: " + nuevaZona.getNombreZona());
                        break;
                    }
                }
            }
        }

        partida.getLog().registrar("Has entrado en: " + nuevaZona.getNombreZona());
    }

    public void cruzarPuerta(Celda destino) {
        if (destino.tienePuerta()) cambiarZona(destino.getPuerta());
    }
}
