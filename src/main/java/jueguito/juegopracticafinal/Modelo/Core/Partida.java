package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.*;
import jueguito.juegopracticafinal.Modelo.Inventario.*;
import jueguito.juegopracticafinal.Modelo.Log.LogSistema;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.NPC.*;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.Modelo.Turno.ResultadoCombate;
import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.Lista;

import static jueguito.juegopracticafinal.Modelo.Core.Configuracion.get;

public class Partida {
    private Jugador jugador;
    private Gato gato;
    private GrafoZonas grafo;
    private Zona zonaActual;
    private int idZonaActual;
    private EstadoJuego estadoActual = EstadoJuego.EN_CURSO;
    private LogSistema log = new LogSistema();
    private int turnoActual;
    private int idZonaGato = -1;
    private boolean gatoEncontrado;

    public Partida(GrafoZonas grafo) { this.grafo = grafo; }

    //Iniciar partida

    public void iniciar() {
        Zona zi = grafo.getZona(get().zona.zonaInicial);
        if (zi == null) {
            log.registrar("ERROR: Zona inicial no encontrada");
            estadoActual = EstadoJuego.DERROTA; return;
        }
        Posicion spawn = zi.getSpawnJugador();
        if (spawn == null) {
            spawn = findSpawn(zi);
            zi.setSpawnJugador(spawn);
        }

        idZonaActual = get().zona.zonaInicial;
        zonaActual = zi;

        jugador = new Jugador("NoLink",
                new Estadisticas(get().jugador.vidaMax, get().jugador.ataqueBase, get().jugador.defensaBase, get().jugador.rangoMov),
                spawn);

        jugador.setLog(log);
        zonaActual.getCelda(spawn.getRow(), spawn.getCol()).setEntidad(jugador);

        gato = new Gato("Gatiko",
                new Estadisticas(get().gato.vida, get().gato.ataque, get().gato.defensa, get().gato.rangoMov));

        turnoActual = 0;

        colocarGatoFijo();
        colocarLlaveFija();
        colocarNPCsFijos();

        log.registrar("INICIANDO PARTIDA \n Bienvenido. Estas en el Interior de tu Casa");
    }

    //Lógica del sistema de turnos

    public void iniciarTurno() {
        if (estadoActual != EstadoJuego.EN_CURSO) return;
        jugador.getEstadisticas().setPuntosMovDisponibles(jugador.getRangoTotal());
    }

    public void terminarTurno() {
        if (estadoActual != EstadoJuego.EN_CURSO) return;
        moverEnemigos(); atacarJugador();
        zonaActual.setCountTurnos(zonaActual.getCountTurnos() + 1);
        turnoActual++;
        if (checkDerrota() || checkVictoria()) return;
        log.registrar("--- Turno " + turnoActual + " ---");
        iniciarTurno();
    }

    private boolean checkVictoria() {
        if (gatoEncontrado && idZonaActual == get().zona.zonaCastillo
                && zonaActual.getCelda(jugador.getPosicion().getRow(), jugador.getPosicion().getCol()).getTipoCelda() == TipoCelda.SALIDA) {
            estadoActual = EstadoJuego.VICTORIA;
            log.registrar("Has logrado recuperar el gato de la princesa!");
            return true;
        }
        return false;
    }

    private boolean checkDerrota() {
        if (!jugador.estarVivo()) { estadoActual = EstadoJuego.DERROTA; log.registrar("Has perdido: no te quedan puntos de vida!"); return true; }
        if (turnoActual >= get().turno.maxTurnos) { estadoActual = EstadoJuego.DERROTA; log.registrar("Has perdido: no has logrado encontrar el gato a tiempo!"); return true; }
        return false;
    }

    //Lógica de movimiento para el jugador

    public boolean moverJugador(int row, int col) {
        if (estadoActual != EstadoJuego.EN_CURSO) return false;
        Celda destino = zonaActual.getCelda(row, col);
        if (destino == null) { log.registrar("No puedes moverte aqui"); return false; }
        if (!destino.isTransitable() || destino.isOcupada()) { log.registrar("No puedes moverte a " + destino.getTipoCelda()); return false; }

        Posicion actual = jugador.getPosicion();
        int coste = calcularDist(actual.getRow(), actual.getCol(), row, col, zonaActual);
        if (coste == Integer.MAX_VALUE) { log.registrar("No hay camino disponible"); return false; }
        if (coste > jugador.getEstadisticas().getPuntosMovDisponibles()) { log.registrar("No tienes suficientes PM"); return false; }
        if (!jugador.getEstadisticas().usarMovimiento(coste)) return false;

        zonaActual.getCelda(actual.getRow(), actual.getCol()).setEntidad(null);
        jugador.moverA(new Posicion(row, col));
        destino.setEntidad(jugador);
        if (destino.tienePuerta()) cambiarZona(destino.getPuerta());
        if (destino.tieneObjeto()) recogerObjeto(destino);
        return true;
    }

    private int calcularDist(int r1, int c1, int r2, int c2, Zona z) {
        boolean[][] visit = new boolean[z.getRows()][z.getCols()];
        Cola<int[]> cola = new Cola<>();
        cola.enqueue(new int[]{r1, c1, 0});
        visit[r1][c1] = true;
        while (!cola.isEmpty()) {
            int[] a = cola.dequeue();
            if (a[0] == r2 && a[1] == c2) return a[2];
            for (int[] d : new int[][]{{-1,0},{1,0},{0,-1},{0,1}}) {
                int nr = a[0]+d[0], nc = a[1]+d[1];
                if (z.esValida(nr,nc) && !visit[nr][nc]) {
                    Celda c = z.getCelda(nr,nc);
                    if (c != null && c.isTransitable() && !c.isOcupada()) { visit[nr][nc]=true; cola.enqueue(new int[]{nr,nc,a[2]+1}); }
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    public Lista<Celda> getCeldasAccesibles() {
        if (jugador == null || zonaActual == null) return new Lista<>();
        return zonaActual.getCeldasAccesibles(jugador.getPosicion().getRow(), jugador.getPosicion().getCol(), jugador.getEstadisticas().getPuntosMovDisponibles());
    }

    public Lista<Celda> getCaminoMinimo(int dr, int dc) {
        if (jugador == null || zonaActual == null) return new Lista<>();
        return zonaActual.getCaminoMinimo(jugador.getPosicion().getRow(), jugador.getPosicion().getCol(), dr, dc);
    }

    //Lógica de combates

    public ResultadoCombate atacarEnemigo(Enemigo e) {
        if (estadoActual != EstadoJuego.EN_CURSO) return null;
        if (!e.estarVivo()) return new ResultadoCombate(0,0,false,"Ya derrotado");
        int hit = Math.max(0, (int)(jugador.getAtaqueTotal()*Math.random()*2) - jugador.getDefensaTotal());
        e.recibirAtaque(hit);
        e.setAtacado(true);
        if (!e.estarVivo()) { zonaActual.getCelda(e.getPosicion().getRow(),e.getPosicion().getCol()).setEntidad(null); log.registrar("Has derrotado a "+e.getNombre()+"!!"); }
        else log.registrar("Has atacado a "+e.getNombre()+", recibe "+hit+" de dano");
        return new ResultadoCombate(hit, e.getEstadisticas().getVidaActual(), !e.estarVivo(), "");
    }

    public boolean atacarDireccion(int row, int col) {
        if (estadoActual != EstadoJuego.EN_CURSO) return false;
        Celda c = zonaActual.getCelda(jugador.getPosicion().getRow()+row, jugador.getPosicion().getCol()+col);
        if (c==null || !c.tieneEntidad() || !(c.getEntidad() instanceof Enemigo)) { log.registrar("No hay enemigos por aqui"); return false; }
        atacarEnemigo((Enemigo)c.getEntidad()); return true;
    }

    //Lógica de movimiento y ataques de enemigos

    private void moverEnemigos() {

        Posicion pj = jugador.getPosicion();

        // ==============================
        // 1. COPIA DE ENEMIGOS (IMPORTANTE)
        // ==============================
        Lista<Enemigo> enemigos = new Lista<>();

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

        // ==============================
        // 2. PROCESAR ENEMIGOS
        // ==============================
        for (int k = 0; k < enemigos.getSize(); k++) {

            Enemigo e = enemigos.get(k);
            Posicion p = e.getPosicion();

            int i = p.getRow();
            int j = p.getCol();

            // si ya murió en este turno
            if (!e.estarVivo()) {
                continue;
            }

            int distJugador =
                    Math.abs(i - pj.getRow()) +
                            Math.abs(j - pj.getCol());

            // ==============================
            // FUERA DE RANGO
            // ==============================
            if (distJugador > 10) {
                continue;
            }

            int[][] dirs = {
                    {-1,0},
                    {1,0},
                    {0,-1},
                    {0,1}
            };

            int bestDir = -1;

            // ==============================
            // CASO: ATACADO
            // ==============================
            if (e.isAtacado()) {

                int decision = (int)(Math.random() * 2);

                // ===== HUIR =====
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

                        log.registrar(e.getNombre() + " ha huido.");
                    }
                }

                // ===== ATACAR =====
                else {

                    Celda pjCelda = zonaActual.getCelda(pj.getRow(), pj.getCol());

                    if (pjCelda != null && zonaActual.getCelda(i, j).esAdyacente(pjCelda)) {

                        int hit = (int)Math.max(
                                0,
                                e.getAtaqueTotal() * Math.random() * 2
                                        - jugador.getDefensaTotal()
                        );

                        jugador.recibirAtaque(hit);

                        log.registrar(
                                e.getNombre() +
                                        " te ha atacado y te ha quitado " +
                                        hit + " de vida."
                        );
                    }
                }

                e.setAtacado(false);
                continue;
            }

            // ==============================
            // CASO NORMAL: PERSEGUIR
            // ==============================
            Celda pjCelda = zonaActual.getCelda(pj.getRow(), pj.getCol());

            if (zonaActual.getCelda(i, j).esAdyacente(pjCelda)) {
                continue;
            }

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

            if (bestDir < 0) continue;

            int nr = i + dirs[bestDir][0];
            int nc = j + dirs[bestDir][1];

            zonaActual.getCelda(i, j).setEntidad(null);

            e.moverA(new Posicion(nr, nc));

            zonaActual.getCelda(nr, nc).setEntidad(e);
        }
    }

    private void atacarJugador() {
        Posicion pj = jugador.getPosicion();
        for (int i = 0; i < zonaActual.getRows(); i++) {
            for (int j = 0; j < zonaActual.getCols(); j++) {
                Celda c = zonaActual.getCelda(i, j);
                if (!(c.getEntidad() instanceof Enemigo e) || !e.estarVivo()) continue;
                if (!c.esAdyacente(zonaActual.getCelda(pj.getRow(), pj.getCol()))) continue;
                int hit = (int) Math.max(0, e.getAtaqueTotal() * Math.random() * 2 - jugador.getDefensaTotal());
                jugador.recibirAtaque(hit);
                log.registrar("Has sido atacado por " + e.getNombre() + ", recibes " + hit + " de dano");
            }
        }
    }

    private void poblarZona(Zona zona) {
        if (zona.getIdZona()==0||zona.getIdZona()==1||zona.getIdZona()==5) return;
        Lista<Posicion> spawns = zona.getSpawnEnemigos();
        if (spawns.getSize()>0) {
            for (int i=0;i<spawns.getSize();i++) {
                Posicion s=spawns.get(i);
                if (!zona.esValida(s.getRow(),s.getCol())) continue;
                Celda c=zona.getCelda(s.getRow(),s.getCol());
                if (c.isOcupada()) continue;
                Enemigo e=new Enemigo("Chungo",new Estadisticas(get().enemigos.vidaBase,get().enemigos.ataqueBase,get().enemigos.defensaBase,get().enemigos.rangoMov));
                e.setPosicion(new Posicion(s.getRow(),s.getCol())); c.setEntidad(e);
            } return;
        }

        int n=get().enemigos.min+(int)(Math.random()*(get().enemigos.max-get().enemigos.min+1));
        for (int i=0;i<n;i++) {
            for (int intentos = 0; intentos < 50; intentos++) {
                int r = (int) (Math.random() * zona.getRows()), co = (int) (Math.random() * zona.getCols());
                Celda c = zona.getCelda(r, co);
                if (c != null && c.isTransitable() && !c.isOcupada() && c.getTipoCelda() != TipoCelda.PUERTA) {
                    Enemigo e = new Enemigo("Chungo", new Estadisticas(get().enemigos.vidaBase + (int) (Math.random() * 10), get().enemigos.ataqueBase + (int) (Math.random() * 3), get().enemigos.defensaBase + (int) (Math.random() * 2), get().enemigos.rangoMov));
                    e.setPosicion(new Posicion(r, co));
                    c.setEntidad(e);
                    break;
                }
            }
        }
    }

    // Lógica de movimiento del gato

    public boolean findGato() {
        if (gatoEncontrado) return true;
        if (zonaGato(gato.getPosicion())!=idZonaActual) return false;
        Posicion pg=gato.getPosicion(), pj=jugador.getPosicion();
        if (Math.abs(pj.getRow()-pg.getRow())<=1&&Math.abs(pj.getCol()-pg.getCol())<=1) {
            gatoEncontrado=true; log.registrar("Encontraste al gato! Toca llevarlo hasta la princesa."); return true;
        } return false;
    }



    private int zonaGato(Posicion p) {
        if (idZonaGato>=0) return idZonaGato;
        for (int i=0;i<get().zona.numZonas;i++) { Zona z=grafo.getZona(i); if (z!=null&&z.esValida(p.getRow(),p.getCol())&&z.getCelda(p.getRow(),p.getCol()).getEntidad()==gato) { idZonaGato=i; return i; } }
        return -1;
    }

    private boolean zonasPermitidas(int id) { for (int z:get().gato.zonasPermitidas) if (z==id) return true; return false; }

    // Métodos para cambiar de zona (habitacion)

    public void cambiarZona(Puerta puerta) {
        if (puerta==null) return;
        int nid, dr, dc;
        if (puerta.getZonaOrigen()==idZonaActual) { nid=puerta.getZonaDestino(); dr=puerta.getYDestino(); dc=puerta.getXDestino(); }
        else { nid=puerta.getZonaOrigen(); dr=puerta.getYOrigen(); dc=puerta.getXOrigen(); }
        Zona nz=grafo.getZona(nid);
        if (nz==null) { log.registrar("Esta puerta no lleva a ninguna zona conocida"); return; }
        if (nz.getCountTurnos()==0) { poblarZona(nz); ponerObjetos(nz); }
        if (nid==get().zona.zonaCastillo&&!gatoEncontrado) { log.registrar("DETENGAN AL INTRUSO!: Has entrado al castillo sin el gato"); estadoActual=EstadoJuego.DERROTA; return; }
        zonaActual.getCelda(jugador.getPosicion().getRow(),jugador.getPosicion().getCol()).setEntidad(null);
        if (!nz.isVisitada()) { turnoActual=Math.max(0,turnoActual-1); log.registrar("Has descubierto una nueva zona!"); }
        nz.setVisitada(true); idZonaActual=nid; zonaActual=nz;
        dr=Math.min(Math.max(dr,0),nz.getRows()-1); dc=Math.min(Math.max(dc,0),nz.getCols()-1);
        jugador.moverA(new Posicion(dr,dc)); nz.getCelda(dr,dc).setEntidad(jugador);
        if (gatoEncontrado) {
            Celda cg=zonaActual.getCelda(gato.getPosicion().getRow(),gato.getPosicion().getCol());
            if (cg!=null) cg.setEntidad(null);
            for (int[] d:new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) { int cr=dr+d[0],cc=dc+d[1];
                if (nz.esValida(cr,cc)) { Celda c=nz.getCelda(cr,cc); if (c!=null&&c.isTransitable()&&!c.isOcupada()) { gato.moverA(new Posicion(cr,cc)); c.setEntidad(gato); idZonaGato=nid; log.registrar("El gato te ha seguido"); break; } } }
        }
        log.registrar("Has entrado en: "+nz.getNombreZona());
    }

    // Colocar objetos en el mapa

    private void ponerObjetos(Zona zona) {

        // Zonas donde NO spawnear objetos
        if (zona.getIdZona() == 0 ||
                zona.getIdZona() == 5 ||
                zona.getIdZona() == 9) {
            return;
        }

        Lista<Posicion> spawns = zona.getSpawnObjetos();

        // =========================
        // 1. SPAWNS FIJOS
        // =========================
        if (spawns.getSize() > 0) {

            for (int i = 0; i < spawns.getSize(); i++) {

                Posicion pos = spawns.get(i);

                if (!zona.esValida(pos.getRow(), pos.getCol())) continue;

                Celda celda = zona.getCelda(pos.getRow(), pos.getCol());

                if (celda != null && !celda.isOcupada() && !celda.tieneObjeto()) {
                    celda.setObjeto(crearObjetoRandom());
                }
            }

            asegurarMinimoGemas(zona, 3);
            return;
        }

        // =========================
        // 2. SPAWN ALEATORIO
        // =========================
        int cantidad = get().objetos.min +
                (int)(Math.random() * (get().objetos.max - get().objetos.min + 1));

        for (int i = 0; i < cantidad; i++) {

            for (int intentos = 0; intentos < 50; intentos++) {

                int fila = (int)(Math.random() * zona.getRows());
                int col = (int)(Math.random() * zona.getCols());

                Celda celda = zona.getCelda(fila, col);

                if (celda == null) continue;

                if (!celda.isTransitable()) continue;
                if (celda.isOcupada()) continue;
                if (celda.tieneObjeto()) continue;
                if (celda.getTipoCelda() == TipoCelda.PUERTA) continue;

                celda.setObjeto(crearObjetoRandom());
                break;
            }
        }

        asegurarMinimoGemas(zona, 2);
    }

    private void asegurarMinimoGemas(Zona zona, int minimo) {

        int contador = 0;

        // contar gemas existentes
        for (int i = 0; i < zona.getRows(); i++) {
            for (int j = 0; j < zona.getCols(); j++) {

                Celda celda = zona.getCelda(i, j);

                if (celda != null &&
                        celda.tieneObjeto() &&
                        "Gema".equals(celda.getObjeto().getNombre())) {
                    contador++;
                }
            }
        }

        // añadir gemas hasta llegar al mínimo
        while (contador < minimo) {

            int fila = (int)(Math.random() * zona.getRows());
            int col = (int)(Math.random() * zona.getCols());

            Celda celda = zona.getCelda(fila, col);

            if (celda == null) continue;

            if (!celda.isTransitable()) continue;
            if (celda.isOcupada()) continue;
            if (celda.tieneObjeto()) continue;

            celda.setObjeto(new Objeto(
                    "Gema",
                    TipoObjeto.CONSUMIBLE,
                    0, 0, 0, 0,
                    1,
                    "Gema"
            ));

            contador++;
        }
    }

    private Objeto crearObjetoRandom() {

        double r = Math.random(); // 0.0 a 1.0

        if (r < 0.5) {
            // 50% Gema (base)
            return new Objeto(
                    "Gema",
                    TipoObjeto.CONSUMIBLE,
                    0, 0, 0, 0,
                    1,
                    "Gema"
            );
        }

        if (r < 0.65) {
            // 15% Espada
            Objeto o = new Objeto(
                    "Espada",
                    TipoObjeto.EQUIPABLE,
                    3, 0, 0, 0,
                    99,
                    "Espada"
            );
            o.setSlot(SlotEquipable.ARMA);
            return o;
        }

        if (r < 0.8) {
            // 15% Escudo
            Objeto o = new Objeto(
                    "Escudo",
                    TipoObjeto.EQUIPABLE,
                    0, 2, 0, 0,
                    99,
                    "Escudo"
            );
            o.setSlot(SlotEquipable.ESCUDO);
            return o;
        }

        // 20% Pociones
        int tipo = (int)(Math.random() * 3);

        return switch (tipo) {
            case 0 -> new Objeto("Pocion de vida", TipoObjeto.CONSUMIBLE, 0, 0, 5, 0, 1, "Cura 5");
            case 1 -> new Objeto("Pocion de ataque", TipoObjeto.CONSUMIBLE, 2, 0, 0, 5, 1, "+2 atk +5 rango");
            default -> new Objeto("Pocion de defensa", TipoObjeto.CONSUMIBLE, 0, 1, 0, 0, 1, "+1 def");
        };
    }


    // Acciones que puede hacer el jugador

    public boolean usarObjeto(Objeto o) {
        if (estadoActual!=EstadoJuego.EN_CURSO) return false;
        if (o.objetoGastado()) { jugador.getInventario().removeObjeto(o); log.registrar(o.getNombre()+" gastado!"); return false; }
        if (o.getVidaBonus()>0) { jugador.getEstadisticas().curarVida(o.getVidaBonus()); log.registrar("Usaste "+o.getNombre()+": +"+o.getVidaBonus()+" vida"); }
        if (o.getAtaqueBonus()>0) { jugador.getEstadisticas().aumentarAtaque(o.getAtaqueBonus()); log.registrar("Usaste "+o.getNombre()+": +"+o.getAtaqueBonus()+" ataque"); }
        if (o.getDefensaBonus()>0) { jugador.getEstadisticas().aumentarDefensa(o.getDefensaBonus()); log.registrar("Usaste "+o.getNombre()+": +"+o.getDefensaBonus()+" defensa"); }
        if (o.getRangoBonus()>0) { jugador.getEstadisticas().aumentarRangoMov(o.getRangoBonus()); log.registrar("Usaste "+o.getNombre()+": +"+o.getRangoBonus()+" rango"); }
        o.usarObjeto();
        if (o.objetoGastado()) { jugador.getInventario().removeObjeto(o); log.registrar(o.getNombre()+" gastado!"); }
        return true;
    }

    public boolean recogerObjeto(Celda c) {
        if (estadoActual!=EstadoJuego.EN_CURSO||!c.tieneObjeto()) return false;
        if (jugador.getInventario().inventarioLleno()) { log.registrar("Inventario lleno!"); return false; }
        Objeto o=c.getObjeto();
        if (jugador.getInventario().addObjeto(o)) { c.setObjeto(null); log.registrar("Recogiste "+o.getNombre()+"!"); return true; }
        return false;
    }

    public boolean equiparObjeto(Objeto o, SlotEquipable s) {
        if (estadoActual!=EstadoJuego.EN_CURSO) return false;
        if (jugador.equipar(o,s)) { log.registrar("Equipaste "+o.getNombre()+"!"); return true; }
        return false;
    }

    public boolean interactNPC() {
        if (estadoActual!=EstadoJuego.EN_CURSO) return false;
        Posicion pj=jugador.getPosicion();
        for (int i=0;i<zonaActual.getRows();i++) for (int j=0;j<zonaActual.getCols();j++) {
            Celda c=zonaActual.getCelda(i,j);
            if (c.tieneNPC()&&c.esAdyacente(zonaActual.getCelda(pj.getRow(),pj.getCol()))) { NPC npc=c.getNpc(); log.registrar(npc.getNombre()+": "+npc.hablar()); if (npc.tieneInfoGato()) log.registrar("Pista: "+npc.getInfoGato()); return true; }
        }
        log.registrar("Hablar solo da mal rollo!"); return false;
    }

    public boolean realizarTradeo(NPC n, Tradeo t) {
        if (estadoActual!=EstadoJuego.EN_CURSO||!n.comerciar()) return false;
        if (t.tradear(jugador)) { log.registrar("Un placer hacer negocios"); return true; }
        log.registrar("No hay trato"); return false;
    }

    private Posicion findSpawn(Zona zona) {
        for (int i=0;i<zona.getRows();i++) for (int j=0;j<zona.getCols();j++) { Celda c=zona.getCelda(i,j); if (c!=null&&c.isTransitable()&&!c.isOcupada()) return new Posicion(i,j); }
        return new Posicion(0,0);
    }


    private void colocarGatoFijo() {

        // Zona fija donde quieres el gato
        int zonaId = 8;

        Zona zonaGato = grafo.getZona(zonaId);
        if (zonaGato == null) return;

        // Crear el gato si no existe
        if (gato == null) {
            gato = new Gato(
                    "Gatiko",
                    new Estadisticas(
                            get().gato.vida,
                            get().gato.ataque,
                            get().gato.defensa,
                            get().gato.rangoMov
                    )
            );
        }

        // Posición fija
        Posicion pos = new Posicion(16, 14);

        // Validación básica
        if (!zonaGato.esValida(16, 14)) return;

        Celda celda = zonaGato.getCelda(16, 14);
        if (celda == null || celda.isOcupada()) return;

        // Colocar gato
        gato.setPosicion(pos);
        celda.setEntidad(gato);

        // Guardar zona del gato
        idZonaGato = zonaId;
    }


    private void colocarLlaveFija() {

        int zonaId = 6; // ejemplo
        Zona zona = grafo.getZona(zonaId);
        if (zona == null) return;

        int row = 9;
        int col = 9;

        if (!zona.esValida(row, col)) return;

        Celda celda = zona.getCelda(row, col);
        if (celda == null) return;

        if (celda.tieneObjeto()) return;

        Objeto llave = new Objeto(
                "Llave",
                TipoObjeto.LLAVE,
                0,0,0,0,
                1,
                "Abre una puerta"
        );

        celda.setObjeto(llave);
    }

    private void colocarNPCsFijos() {

        // =========================
        // NPC 1
        // =========================
        Zona zona1 = grafo.getZona(5);

        if (zona1 != null && zona1.esValida(4, 9)) {

            Celda celda1 = zona1.getCelda(4, 9);

            if (celda1 != null && !celda1.isOcupada()) {

                NPC npc1 = new NPC(
                        "Mercader",
                        TipoNPC.COMERCIANTE
                );

                npc1.setSprite("/entidades/NPC_1.png");

                celda1.setNpc(npc1);
            }
        }

        // =========================
        // NPC 2
        // =========================
        Zona zona2 = grafo.getZona(6);

        if (zona2 != null && zona2.esValida(13,1)) {

            Celda celda2 = zona2.getCelda(13, 1);

            if (celda2 != null && !celda2.isOcupada()) {

                NPC npc2 = new NPC(
                        "Viejo Sabio",
                        TipoNPC.ALDEANO
                );

                npc2.setSprite("/entidades/NPC_2.png");

                celda2.setNpc(npc2);
            }
        }

        // =========================
        // NPC 3
        // =========================
        Zona zona3 = grafo.getZona(7);

        if (zona3 != null && zona3.esValida(5, 8)) {

            Celda celda3 = zona3.getCelda(5, 8);

            if (celda3 != null && !celda3.isOcupada()) {

                NPC npc3 = new NPC(
                        "Guardia",
                        TipoNPC.ALDEANO
                );

                npc3.setSprite("/entidades/NPC_3.png");

                celda3.setNpc(npc3);
            }
        }
    }

    // Getters y setters

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador j) { this.jugador=j; }
    public Gato getGato() { return gato; }
    public void setGato(Gato g) { this.gato=g; }
    public GrafoZonas getGrafo() { return grafo; }
    public Zona getZonaActual() { return zonaActual; }
    public void setZonaActual(Zona z) { zonaActual=z; }
    public int getIdZonaActual() { return idZonaActual; }
    public void setIdZonaActual(int id) { idZonaActual=id; zonaActual=grafo.getZona(id); }
    public EstadoJuego getEstadoActual() { return estadoActual; }
    public void setEstadoActual(EstadoJuego e) { estadoActual=e; }
    public LogSistema getLog() { return log; }
    public int getTurnoActual() { return turnoActual; }
    public void setTurnoActual(int t) { turnoActual=t; }
    public boolean isGatoEncontrado() { return gatoEncontrado; }
    public void setGatoEncontrado(boolean b) { gatoEncontrado=b; }
    public int getIdZonaGato() { return idZonaGato; }
    public void setIdZonaGato(int id) { idZonaGato=id; }
}