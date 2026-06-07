package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorAccesoZonaBloqueada;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorCasillaOcupada;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorMovimientoInvalido;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorMovimientoSinBarca;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.InterfazIterador;
import jueguito.juegopracticafinal.TADs.Lista;

public class PartidaMovimiento {

    //ATRIBUTOS

    private final Partida partida;

    public PartidaMovimiento(Partida partida) {
        this.partida = partida;
    }


    //MÉTODOS

    // MOVIMIENTO DEL JUGADOR

    public boolean moverJugador(int row, int col)
            throws ErrorMovimientoInvalido,
            ErrorCasillaOcupada,
            ErrorMovimientoSinBarca,
            ErrorAccesoZonaBloqueada {

        //Validar el estado de la partida
        if (partida.getEstadoActual()
                != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO) {

            throw new ErrorMovimientoInvalido(
                    "La partida no está en curso"
            );
        }

        Zona zonaActual = partida.getZonaActual();
        Jugador jugador = partida.getJugador();

        //Obtener destino
        Celda destino = zonaActual.getCelda(row, col);

        if (destino == null) {

            throw new ErrorMovimientoInvalido(
                    "No puedes moverte fuera del mapa"
            );
        }

        //Validad si la casilla puede ser transitable
        if (!destino.isTransitable()) {

            throw new ErrorMovimientoInvalido(
                    "La celda no es transitable"
            );
        }

        //Validad que la casilla esté ocupada
        if (destino.isOcupada()) {

            throw new ErrorCasillaOcupada(
                    "La casilla está ocupada"
            );
        }

        //Validación de que la barca esté en el inventario
        if (zonaActual.getIdZona() == 6 && destino.getTipoCelda() == TipoCelda.AGUA) {
            boolean tieneBarca = false;
            InterfazIterador<Objeto> it = jugador.getInventario().getObjetos().iterador();
            while (it.hasNext()) {
                Objeto obj = it.next();
                if ("Barca".equalsIgnoreCase(obj.getNombre())) {
                    tieneBarca = true;
                    break;
                }
            }
            if (!tieneBarca) {
                throw new ErrorMovimientoSinBarca("Necesitas la Barca para cruzar el agua");
            }
        }

        //Validad camino
        Posicion actual = jugador.getPosicion();

        int coste = calcularDist(
                actual.getRow(),
                actual.getCol(),
                row,
                col,
                zonaActual
        );
        if (coste == Integer.MAX_VALUE) {

            throw new ErrorMovimientoInvalido(
                    "No existe un camino válido"
            );
        }

        //Validad que queden puntos de vida
        if (coste >
                jugador.getEstadisticas()
                        .getPuntosMovDisponibles()) {

            throw new ErrorMovimientoInvalido(
                    "No tienes suficientes puntos de movimiento"
            );
        }

        //Gastar movimientos
        if (!jugador.getEstadisticas()
                .usarMovimiento(coste)) {

            throw new ErrorMovimientoInvalido(
                    "No se pudo consumir movimiento"
            );
        }

        //Mover objetos
        partida.getLog().registrarMovimiento(
                actual,
                partida.getZonaActual()
        );

        zonaActual.getCelda(
                actual.getRow(),
                actual.getCol()
        ).setEntidad(null);

        jugador.moverA(new Posicion(row, col));

        destino.setEntidad(jugador);

        partida.getLog().registrar(
                "Te moviste a la casilla [" + row + ", " + col + "]"
        );

        //Objetos
        if (destino.tieneObjeto()) {

            partida.recogerObjeto(destino);
        }

        //Puertas
        if (destino.tienePuerta()) {

            partida.cambiarZona(destino.getPuerta());
        }

        return true;
    }


    //BFS PARA DISTANCIA

    public int calcularDist(int r1, int c1, int r2, int c2, Zona z) {

        boolean[][] visit = new boolean[z.getRows()][z.getCols()];
        Cola<int[]> cola = new Cola<>();

        cola.enqueue(new int[]{r1, c1, 0});
        visit[r1][c1] = true;

        while (!cola.isEmpty()) {

            int[] a = cola.dequeue();

            if (a[0] == r2 && a[1] == c2)
                return a[2];

            int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

            for (int[] d : dirs) {

                int nr = a[0] + d[0];
                int nc = a[1] + d[1];

                if (z.esValida(nr, nc) && !visit[nr][nc]) {

                    Celda c = z.getCelda(nr, nc);

                    if (c != null && c.isTransitable()) {

                        if ((nr == r2 && nc == c2) || !c.isOcupada()) {
                            visit[nr][nc] = true;
                            cola.enqueue(new int[]{nr, nc, a[2] + 1});
                        }
                    }
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    public Lista<Celda> calcularCamino(int rO, int cO, int rD, int cD, Zona z) {
        boolean[][] visitada = new boolean[z.getRows()][z.getCols()];
        int[][] prevRow = new int[z.getRows()][z.getCols()];
        int[][] prevCol = new int[z.getRows()][z.getCols()];

        if (rO == rD && cO == cD) {
            Lista<Celda> camino = new Lista<>();
            camino.add(z.getCelda(rO, cO));
            return camino;
        }

        for (int i = 0; i < z.getRows(); i++) {
            for (int j = 0; j < z.getCols(); j++) {
                prevRow[i][j] = prevCol[i][j] = -1;
            }
        }

        Cola<int[]> cola = new Cola<>();
        cola.enqueue(new int[]{rO, cO});
        visitada[rO][cO] = true;

        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

        while (!cola.isEmpty()) {
            int[] a = cola.dequeue();

            if(a[0] == rD && a[1] == cD){
                Lista<Celda> camino = new Lista<>();
                int actualR = rD;
                int actualC = cD;

                while(actualR != rO || actualC != cO){
                    camino.addFirst(z.getCelda(actualR, actualC));
                    int prevR = prevRow[actualR][actualC];
                    int prevC = prevCol[actualR][actualC];
                    actualR = prevR;
                    actualC = prevC;
                }
                return camino;
            }
            for(int[] d : dirs){
                int nr = a[0] + d[0];
                int nc = a[1] + d[1];

                if(z.esValida(nr, nc) && !visitada[nr][nc]) {
                    Celda c = z.getCelda(nr, nc);

                    if (c != null && c.isTransitable() && ((nr == rD && nc == cD) || !c.isOcupada())) {
                            visitada[nr][nc] = true;
                            prevRow[nr][nc] = a[0];
                            prevCol[nr][nc] = a[1];
                            cola.enqueue(new int[]{nr,nc});
                        }

                }
            }
        }
        return null;
    }


    // CELDAS ACCESIBLES

    public Lista<Celda> getCeldasAccesibles() {

        Jugador jugador = partida.getJugador();
        Zona zonaActual = partida.getZonaActual();

        if (jugador == null || zonaActual == null)
            return new Lista<>();

        return zonaActual.getCeldasAccesibles(
                jugador.getPosicion().getRow(),
                jugador.getPosicion().getCol(),
                jugador.getEstadisticas().getPuntosMovDisponibles()
        );
    }


    // MOVIMIENTO DE ENEMIGOS

    //Valida si la casilla a la que se va a mover el enemigo es accesible o no
    private boolean casillaValidaEnemigo(Zona zona, int r, int c, Jugador jugador) {

        if (!zona.esValida(r, c)) return false;

        Celda celda = zona.getCelda(r, c);
        if (celda == null) return false;

        if (!celda.isTransitable()) return false;
        if (celda.isOcupada()) return false;

        Posicion pj = jugador.getPosicion();
        if (pj.getRow() == r && pj.getCol() == c) return false;

        if (celda.tieneObjeto()) return false;

        if (celda.getEntidad() != null &&
                !(celda.getEntidad() instanceof Enemigo)) {
            return false;
        }

        if (estaCercaDePuerta(zona, r, c)) return false;

        return true;
    }

    //Detecta si la casilla es adyacente a una puerta para no bloquearla
    private boolean estaCercaDePuerta(Zona zona, int fila, int col) {

        for (int i = 0; i < zona.getRows(); i++) {
            for (int j = 0; j < zona.getCols(); j++) {

                Celda celda = zona.getCelda(i, j);

                if (celda != null &&
                        (celda.getTipoCelda() == TipoCelda.PUERTA ||
                                celda.getTipoCelda() == TipoCelda.SALIDA)) {

                    int dr = Math.abs(fila - i);
                    int dc = Math.abs(col - j);

                    if (dr <= 1 && dc <= 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Algoritmo de movimiento del enemigo
    public void moverEnemigos() {

        Zona zonaActual = partida.getZonaActual();
        Jugador jugador = partida.getJugador();
        Posicion pj = jugador.getPosicion();

        Lista<Enemigo> enemigos = new Lista<>();

        // recopilar enemigos
        for (int i = 0; i < zonaActual.getRows(); i++) {
            for (int j = 0; j < zonaActual.getCols(); j++) {

                Celda c = zonaActual.getCelda(i, j);

                if (c != null &&
                        c.getEntidad() instanceof Enemigo e &&
                        e.estarVivo()) {
                    enemigos.add(e);
                }
            }
        }

        InterfazIterador<Enemigo> it = enemigos.iterador();

        while (it.hasNext()) {

            Enemigo e = it.next();
            Posicion p = e.getPosicion();

            int i = p.getRow();
            int j = p.getCol();

            int[][] dirs = {
                    {-1,0},{1,0},{0,-1},{0,1}
            };

            int bestDir = -1;

            // distancia jugador
            int distJugador =
                    Math.abs(i - pj.getRow()) +
                            Math.abs(j - pj.getCol());

            if (distJugador > 10) continue;

            // ======================
            // SI HA SIDO ATACADO
            // ======================
            if (e.isAtacado()) {

                if (Math.random() < 0.5) {
                    // huida
                    int bestDist = -1;

                    for (int d = 0; d < 4; d++) {

                        int nr = i + dirs[d][0];
                        int nc = j + dirs[d][1];

                        if (!casillaValidaEnemigo(zonaActual, nr, nc, jugador))
                            continue;

                        int dist =
                                Math.abs(nr - pj.getRow()) +
                                        Math.abs(nc - pj.getCol());

                        if (dist > bestDist) {
                            bestDist = dist;
                            bestDir = d;
                        }
                    }

                    if (bestDir >= 0) {

                        int nr = i + dirs[bestDir][0];
                        int nc = j + dirs[bestDir][1];

                        zonaActual.getCelda(i, j).setEntidad(null);
                        e.moverA(new Posicion(nr, nc));
                        zonaActual.getCelda(nr, nc).setEntidad(e);
                    }

                } else {

                    // ataque
                    Celda pjCelda = zonaActual.getCelda(pj.getRow(), pj.getCol());

                    if (zonaActual.getCelda(i, j).esAdyacente(pjCelda)) {

                        int hit = PartidaObjetosYCombate.calcularHit(
                                e.getAtaqueTotal(),
                                jugador.getDefensaTotal()
                        );

                        jugador.getEstadisticas().restarVidaDirecta(hit);
                    }
                }

                e.setAtacado(false);
                continue;
            }

            // ======================
            // PERSECUCIÓN
            // ======================
            for (int d = 0; d < 4; d++) {

                int nr = i + dirs[d][0];
                int nc = j + dirs[d][1];

                if (!casillaValidaEnemigo(zonaActual, nr, nc, jugador))
                    continue;

                int dist =
                        Math.abs(nr - pj.getRow()) +
                                Math.abs(nc - pj.getCol());

                if (dist < distJugador) {
                    distJugador = dist;
                    bestDir = d;
                }
            }

            if (bestDir < 0) continue;

            int nr = i + dirs[bestDir][0];
            int nc = j + dirs[bestDir][1];

            zonaActual.getCelda(i, j).setEntidad(null);
            e.moverA(new Posicion(nr, nc));
            zonaActual.getCelda(nr, nc).setEntidad(e);
        }
    }
}