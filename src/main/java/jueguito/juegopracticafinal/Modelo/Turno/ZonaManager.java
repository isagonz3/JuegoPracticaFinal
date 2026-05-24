package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Log.LogSistema;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Puerta;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Lista;

public class ZonaManager {
    private Partida partida;

    public ZonaManager(Partida partida){
        this.partida = partida;
    }

    public void cambiarZona(Puerta puerta){

        if(puerta == null){
            return;
        }

        boolean esSalida = (puerta.getXDestino() == -1 && puerta.getYDestino() == -1);
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

        if(esSalida){
            if(nuevaIdZona >= 0 && nuevaIdZona < 10){
                nuevaIdZona = puerta.getZonaDestino();
                destinoRow = puerta.getYDestino();
                destinoCol = puerta.getXDestino();
            }
        }

        Zona nuevaZona = partida.getGrafo().getZona(nuevaIdZona);
        if(nuevaZona == null){
            partida.getLog().registrar("Esta puerta no lleva a ninguna zona conocida");
            return;
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

        partida.getLog().registrar("Has entrado en: " + nuevaZona.getNombreZona());
        if(nuevaZona.getCountTurnos() == 0){
            poblarZona(nuevaZona);
        }
    }

    private void poblarZona(Zona zona) {
        if (zona.getIdZona() == 0 || zona.getIdZona() == 5) return;
        Lista<Posicion> spawns = zona.getSpawnEnemigos();
        for (int i = 0; i < spawns.getSize(); i++) {
            Posicion p = spawns.get(i);
            if (zona.esValida(p.getRow(), p.getCol())) {
                Celda c = zona.getCelda(p.getRow(), p.getCol());
                if (!c.isOcupada()) {
                    int vida = 5 + (int) (Math.random() * 10);
                    int atk = 2 + (int) (Math.random() * 3);
                    int def = 1 + (int) (Math.random() * 2);
                    Enemigo e = new Enemigo("Chungo", new Estadisticas(vida, atk, def, 4));
                    e.setPosicion(new Posicion(p.getRow(), p.getCol()));
                    c.setEntidad(e);
                }
            }
        }
    }

    public void cruzarPuerta(Celda destino) {
        if (destino.tienePuerta()) cambiarZona(destino.getPuerta());
    }
}
