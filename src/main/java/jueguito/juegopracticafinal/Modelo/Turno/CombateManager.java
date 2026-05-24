package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Log.LogSistema;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;

public class CombateManager {
    private Partida partida;

    public CombateManager(Partida partida) {
        this.partida = partida;
    }

    public ResultadoCombate atacarEnemigo(Enemigo enemigo){
        if(partida.getEstadoActual() != EstadoJuego.EN_CURSO){
            return null;
        }
        if(!enemigo.estarVivo()){
            return new ResultadoCombate(0,0,false,"Enemigo ya derrotado");
        }

        Jugador jugador = partida.getJugador();
        int ataque = jugador.getAtaqueTotal();
        int defensa = jugador.getDefensaTotal();
        int hit = Math.max(0,(int)(ataque*Math.random()*2) - defensa);
        enemigo.recibirAtaque(hit);
        boolean enemigoKO = !enemigo.estarVivo();

        String mensaje;
        if(enemigoKO){
            Zona zonaActual = partida.getZonaActual();
            Celda celda = zonaActual.getCelda(enemigo.getPosicion().getRow(),enemigo.getPosicion().getCol());
            celda.setEntidad(null);
            mensaje = "Has derrotado a " + enemigo.getNombre() + "!!";
        }
        else{
            mensaje = "Has atacado a " + enemigo.getNombre() + ", recibe " + hit + " puntos de daño";
        }

        partida.getLog().registrar(mensaje);
        partida.setAccionRealizada(true);
        return new ResultadoCombate(hit,enemigo.getEstadisticas().getVidaActual(),enemigoKO,mensaje);
    }

    public boolean atacarDireccion(int row, int col){
        if(partida.getEstadoActual() != EstadoJuego.EN_CURSO){
            return false;
        }

        Jugador jugador = partida.getJugador();
        Posicion posicion = jugador.getPosicion();
        int enemigoRow = posicion.getRow() + row;
        int enemigoCol = posicion.getCol() + col;

        Zona zonaActual = partida.getZonaActual();
        Celda celdaEnemigo = zonaActual.getCelda(enemigoRow, enemigoCol);
        if(celdaEnemigo == null || !celdaEnemigo.tieneEntidad() || !(celdaEnemigo.getEntidad() instanceof Enemigo)){
            partida.getLog().registrar("No hay enemigos por aquí");
            return false;
        }
        atacarEnemigo((Enemigo) celdaEnemigo.getEntidad());
        return true;
    }

    public void getAtaqueEnemigo(){
        Jugador jugador = partida.getJugador();
        Zona zonaActual = partida.getZonaActual();
        Posicion posicion = jugador.getPosicion();
        for(int i = 0; i < zonaActual.getRows(); i++){
            for(int j = 0; j < zonaActual.getCols(); j++){
                Celda celda = zonaActual.getCelda(i, j);
                if(celda.tieneEntidad() && celda.getEntidad() instanceof Enemigo){
                    Enemigo enemigo = (Enemigo)celda.getEntidad();

                    if(!enemigo.estarVivo()){
                        continue;
                    }

                    if(celda.esAdyacente(zonaActual.getCelda(posicion.getRow(),posicion.getCol()))){
                        int hit = (int) Math.max(0, enemigo.getAtaqueTotal()*(Math.random()*2) - jugador.getDefensaTotal());
                        jugador.recibirAtaque(hit);
                        partida.getLog().registrar("Has sido atacado por " + enemigo.getNombre() + ", recibes " + hit + " puntos de daño");
                    }
                }
            }
        }
    }
}
