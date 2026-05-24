package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda;

import static jueguito.juegopracticafinal.Modelo.Data.TurnoData.MAX_TURNOS;

public class TurnoManager {
    private Partida partida;
    private EnemigoManager enemigoManager;
    private GatoManager gatoManager;

    public TurnoManager(Partida partida){
        this.partida = partida;
        this.enemigoManager = partida.getEnemigoManager();
        this.gatoManager = partida.getGatoManager();
    }

    public void iniciarTurno() {
        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) return;
        partida.getJugador().getEstadisticas().resetMovimiento();
        partida.setAccionRealizada(false);
        partida.setMovimientoRealizado(false);
    }

    public void terminarTurno() {
        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) return;
        enemigoManager.moverEnemigos();
        gatoManager.moverGato();
        partida.getZonaActual().setCountTurnos(partida.getZonaActual().getCountTurnos() + 1);
        partida.setTurnoActual(partida.getTurnoActual() + 1);
        partida.setAccionRealizada(false);
        partida.setMovimientoRealizado(false);
        if (checkDerrota() || checkVictoria()) return;
        partida.getLog().registrar("--- Turno " + partida.getTurnoActual() + " ---");
        iniciarTurno();
    }

    private boolean checkVictoria() {
        if (partida.isGatoEncontrado() && partida.getIdZonaActual() == 9) {
            if (partida.getZonaActual().getCelda(
                    partida.getJugador().getPosicion().getRow(),
                    partida.getJugador().getPosicion().getCol()).getTipoCelda() == TipoCelda.SALIDA) {
                partida.setEstadoActual(EstadoJuego.VICTORIA);
                partida.getLog().registrar("Has logrado recuperar el gato de la princesa!");
                return true;
            }
        }
        return false;
    }

    private boolean checkDerrota() {
        if (!partida.getJugador().estarVivo()) {
            partida.setEstadoActual(EstadoJuego.DERROTA);
            partida.getLog().registrar("Has perdido: no te quedan puntos de vida!");
            return true;
        }
        if (partida.getTurnoActual() >= MAX_TURNOS) {
            partida.setEstadoActual(EstadoJuego.DERROTA);
            partida.getLog().registrar("Has perdido: no has logrado encontrar el gato a tiempo!");
            return true;
        }
        return false;
    }
}
