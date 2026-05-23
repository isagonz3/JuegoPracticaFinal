package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;

public class AccionAtacar extends Accion {
    private final Enemigo enemigo;

    public AccionAtacar(Enemigo objetivo) {
        this.enemigo = objetivo;
    }

    public Enemigo getEnemigo() { return enemigo; }

    @Override
    public boolean ejecutar(Partida partida) {
        ResultadoCombate res = partida.atacarEnemigo(enemigo);
        return res != null;
    }

    @Override
    public String getDescripcion() {
        return "Atacar a " + enemigo.getNombre();
    }
}