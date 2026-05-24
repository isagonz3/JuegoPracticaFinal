package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Lista;

public class EnemigoManager {
    private Partida partida;

    public EnemigoManager(Partida partida){
        this.partida = partida;
    }

    public void moverEnemigos(){
        Zona zona = partida.getZonaActual();
        Posicion posJugador = partida.getJugador().getPosicion();

        for(int i = 0; i < zona.getRows(); i++){
            for(int j = 0; j < zona.getCols(); j++){
                Celda celda = zona.getCelda(i, j);
                if (!(celda.getEntidad() instanceof Enemigo e) || !e.estarVivo()) continue;
                if (celda.esAdyacente(zona.getCelda(posJugador.getRow(), posJugador.getCol()))) continue;
                moverEnemigo(zona, e, i, j, posJugador);
            }
        }
    }

    private void moverEnemigo(Zona zona, Enemigo enemigo, int i, int j, Posicion posJugador){
        int bfsDir = -1;
        int bfsDistancia = Integer.MAX_VALUE;

        int[][] coordenadas = {{-1,0},{1,0},{0,-1},{0,1}};
        for(int dir = 0; dir < 4; dir++){
            int newRow = i + coordenadas[dir][0];
            int newCol = j + coordenadas[dir][1];

            if(!zona.esValida(newRow, newCol)) continue;
            Celda ady = zona.getCelda(newRow, newCol);

            if(!ady.isTransitable() || ady.isOcupada()) continue;
            int dist = Math.abs(newRow - posJugador.getRow()) + Math.abs(newCol - posJugador.getCol());
            if(dist < bfsDistancia){
                bfsDir = dir;
                bfsDistancia = dist;
            }
        }
        if(bfsDir < 0){
            return;
        }
        int newRow = i + coordenadas[bfsDir][0];
        int newCol = j + coordenadas[bfsDir][1];
        zona.getCelda(i,j).setEntidad(null);
        enemigo.moverA(new Posicion(newRow,newCol));
        zona.getCelda(newRow,newCol).setEntidad(enemigo);
    }

    public void atacarJugador(){
        Zona zona = partida.getZonaActual();
        Posicion posJugador = partida.getJugador().getPosicion();

        for(int i = 0; i < zona.getRows(); i++){
            for(int j = 0; j < zona.getCols(); j++){
                Celda celda = zona.getCelda(i, j);
                if(!(celda.getEntidad() instanceof Enemigo enemigo) || !enemigo.estarVivo()) continue;
                if(!celda.esAdyacente(zona.getCelda(posJugador.getRow(), posJugador.getCol()))) continue;

                int hit = (int)Math.max(0, enemigo.getAtaqueTotal()*Math.random()*2 - partida.getJugador().getDefensaTotal());
                partida.getJugador().recibirAtaque(hit);
                partida.getLog().registrar("Has sido atacado por " + enemigo.getNombre() + ", recibes "+ hit +" puntos de daño");
            }

        }
    }

    public void poblarZona(Zona zona){
        if(zona.getIdZona() == 0 || zona.getIdZona() == 5){
            return;
        }

        Lista<Posicion> spawns = zona.getSpawnEnemigos();
        for(int i = 0; i < spawns.getSize(); i++){
            Posicion spawn = spawns.get(i);
            if(!zona.esValida(spawn.getRow(), spawn.getCol())) continue;

            Celda celda = zona.getCelda(spawn.getRow(), spawn.getCol());
            if(celda.isOcupada()) continue;

            Enemigo enemigo = new Enemigo("Chungo",
                    new Estadisticas(5+(int)(Math.random()*10),2+(int)(Math.random()*3),1+(int)(Math.random()*2),4));
            enemigo.setPosicion(new Posicion(spawn.getRow(), spawn.getCol()));
            celda.setEntidad(enemigo);
        }
    }
}
