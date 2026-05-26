package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.Lista;

public class PartidaMovimiento {

    private final Partida partida;

    public PartidaMovimiento(Partida partida) {
        this.partida = partida;
    }

    // ============================================================
    // MOVIMIENTO DEL JUGADOR
    // ============================================================

    public boolean moverJugador(int row, int col) {

        if (partida.getEstadoActual() != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO)
            return false;

        Zona zonaActual = partida.getZonaActual();
        Jugador jugador = partida.getJugador();

        Celda destino = zonaActual.getCelda(row, col);
        if (destino == null) {
            partida.getLog().registrar("No puedes moverte aqui");
            return false;
        }

        if (!destino.isTransitable() || destino.isOcupada()) {
            partida.getLog().registrar("No puedes moverte a " + destino.getTipoCelda());
            return false;
        }

        Posicion actual = jugador.getPosicion();
        int coste = calcularDist(actual.getRow(), actual.getCol(), row, col, zonaActual);

        if (coste == Integer.MAX_VALUE) {
            partida.getLog().registrar("No hay camino disponible");
            return false;
        }

        if (coste > jugador.getEstadisticas().getPuntosMovDisponibles()) {
            partida.getLog().registrar("No tienes suficientes PM");
            return false;
        }

        if (!jugador.getEstadisticas().usarMovimiento(coste))
            return false;

        zonaActual.getCelda(actual.getRow(), actual.getCol()).setEntidad(null);

        jugador.moverA(new Posicion(row, col));
        destino.setEntidad(jugador);

        if (destino.tienePuerta())
            partida.cambiarZona(destino.getPuerta());

        if (destino.tieneObjeto())
            partida.recogerObjeto(destino);

        return true;
    }

    // ============================================================
    // BFS PARA DISTANCIA
    // ============================================================

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

                    if (c != null && c.isTransitable() && !c.isOcupada()) {
                        visit[nr][nc] = true;
                        cola.enqueue(new int[]{nr, nc, a[2] + 1});
                    }
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    // ============================================================
    // CELDAS ACCESIBLES
    // ============================================================

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

    // ============================================================
    // CAMINO MÍNIMO
    // ============================================================

    public Lista<Celda> getCaminoMinimo(int dr, int dc) {

        Jugador jugador = partida.getJugador();
        Zona zonaActual = partida.getZonaActual();

        if (jugador == null || zonaActual == null)
            return new Lista<>();

        return zonaActual.getCaminoMinimo(
                jugador.getPosicion().getRow(),
                jugador.getPosicion().getCol(),
                dr, dc
        );
    }

    // ============================================================
    // MOVIMIENTO DE ENEMIGOS
    // ============================================================

    public void moverEnemigos() {

        Zona zonaActual = partida.getZonaActual();
        Jugador jugador = partida.getJugador();
        Posicion pj = jugador.getPosicion();

        Lista<Enemigo> enemigos = new Lista<>();

        // Copiar enemigos vivos
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

        // Procesar enemigos
        for (int k = 0; k < enemigos.getSize(); k++) {

            Enemigo e = enemigos.get(k);
            Posicion p = e.getPosicion();

            int i = p.getRow();
            int j = p.getCol();

            if (!e.estarVivo())
                continue;

            int distJugador =
                    Math.abs(i - pj.getRow()) +
                            Math.abs(j - pj.getCol());

            if (distJugador > 10)
                continue;

            int[][] dirs = {
                    {-1,0},
                    {1,0},
                    {0,-1},
                    {0,1}
            };

            int bestDir = -1;

            // ============================================================
            // SI FUE ATACADO
            // ============================================================

            if (e.isAtacado()) {

                int decision = (int)(Math.random() * 2);

                // HUIR
                if (decision == 0) {

                    int bestDist = -1;

                    for (int d = 0; d < 4; d++) {

                        int nr = i + dirs[d][0];
                        int nc = j + dirs[d][1];

                        if (!zonaActual.esValida(nr, nc)) continue;

                        Celda ady = zonaActual.getCelda(nr, nc);

                        if (!ady.isTransitable() || ady.isOcupada()) continue;

                        int dist = Math.abs(nr - pj.getRow()) +
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

                        partida.getLog().registrar(e.getNombre() + " ha huido.");
                    }
                }

                // ATACAR
                else {

                    Celda pjCelda = zonaActual.getCelda(pj.getRow(), pj.getCol());

                    if (pjCelda != null &&
                            zonaActual.getCelda(i, j).esAdyacente(pjCelda)) {

                        int hit = (int)Math.max(
                                0,
                                e.getAtaqueTotal() * Math.random() * 2
                                        - jugador.getDefensaTotal()
                        );

                        jugador.recibirAtaque(hit);

                        partida.getLog().registrar(
                                e.getNombre() +
                                        " te ha atacado y te ha quitado " +
                                        hit + " de vida."
                        );
                    }
                }

                e.setAtacado(false);
                continue;
            }

            // ============================================================
            // PERSEGUIR AL JUGADOR
            // ============================================================

            Celda pjCelda = zonaActual.getCelda(pj.getRow(), pj.getCol());

            if (zonaActual.getCelda(i, j).esAdyacente(pjCelda))
                continue;

            int bestDist = Integer.MAX_VALUE;

            for (int d = 0; d < 4; d++) {

                int nr = i + dirs[d][0];
                int nc = j + dirs[d][1];

                if (!zonaActual.esValida(nr, nc)) continue;

                Celda ady = zonaActual.getCelda(nr, nc);

                if (!ady.isTransitable() || ady.isOcupada()) continue;

                int dist = Math.abs(nr - pj.getRow()) +
                        Math.abs(nc - pj.getCol());

                if (dist < bestDist) {
                    bestDist = dist;
                    bestDir = d;
                }
            }

            if (bestDir < 0)
                continue;

            int nr = i + dirs[bestDir][0];
            int nc = j + dirs[bestDir][1];

            zonaActual.getCelda(i, j).setEntidad(null);

            e.moverA(new Posicion(nr, nc));

            zonaActual.getCelda(nr, nc).setEntidad(e);
        }
    }
}
