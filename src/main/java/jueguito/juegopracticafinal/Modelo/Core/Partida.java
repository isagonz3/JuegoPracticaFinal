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

        Posicion sg = spawnGato();
        if (sg != null) {
            gato.setPosicion(sg);
            for (int z : get().gato.zonasPermitidas)
                if (grafo.getZona(z).esValida(sg.getRow(), sg.getCol()))
                { grafo.getZona(z).getCelda(sg.getRow(), sg.getCol()).setEntidad(gato); break; }
        }
        turnoActual = 0;
        log.registrar("INICIANDO PARTIDA \n Bienvenido. Estas en el Interior de tu Casa");
    }

    //Lógica del sistema de turnos

    public void iniciarTurno() {
        if (estadoActual != EstadoJuego.EN_CURSO) return;
        jugador.getEstadisticas().setPuntosMovDisponibles(jugador.getRangoTotal());
    }

    public void terminarTurno() {
        if (estadoActual != EstadoJuego.EN_CURSO) return;
        moverEnemigos(); atacarJugador(); moverGato();
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
        for (int i = 0; i < zonaActual.getRows(); i++)
            for (int j = 0; j < zonaActual.getCols(); j++) {
                Celda c = zonaActual.getCelda(i,j);
                if (!(c.getEntidad() instanceof Enemigo e) || !e.estarVivo()) continue;
                if (c.esAdyacente(zonaActual.getCelda(pj.getRow(),pj.getCol()))) continue;
                int bestDir=-1, bestDist=Integer.MAX_VALUE;
                for (int d=0; d<4; d++) {
                    int[][] dirs={{-1,0},{1,0},{0,-1},{0,1}};
                    int nr=i+dirs[d][0], nc=j+dirs[d][1];
                    if (!zonaActual.esValida(nr,nc)) continue;
                    Celda ady = zonaActual.getCelda(nr,nc);
                    if (!ady.isTransitable()||ady.isOcupada()) continue;
                    int dist = Math.abs(nr-pj.getRow())+Math.abs(nc-pj.getCol());
                    if (dist<bestDist) { bestDir=d; bestDist=dist; }
                }
                if (bestDir<0) continue;
                int[][] dirs={{-1,0},{1,0},{0,-1},{0,1}};
                int nr=i+dirs[bestDir][0], nc=j+dirs[bestDir][1];
                zonaActual.getCelda(i,j).setEntidad(null);
                e.moverA(new Posicion(nr,nc));
                zonaActual.getCelda(nr,nc).setEntidad(e);
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

    private void moverGato() { if (gatoEncontrado) seguirJugador(gato); else moverGatoAleatorio(gato); }

    private void seguirJugador(Gato g) {
        Posicion pj=jugador.getPosicion(), pg=g.getPosicion();
        if (idZonaActual!=zonaGato(pg)) return;
        int nr=pg.getRow()+Integer.signum(pj.getRow()-pg.getRow()), nc=pg.getCol()+Integer.signum(pj.getCol()-pg.getCol());
        if (zonaActual.esValida(nr,nc)) {
            Celda c=zonaActual.getCelda(nr,nc);
            if (c.isTransitable()&&!c.isOcupada()) { zonaActual.getCelda(pg.getRow(),pg.getCol()).setEntidad(null); g.moverA(new Posicion(nr,nc)); c.setEntidad(g); }
        }
    }

    private void moverGatoAleatorio(Gato g) {
        Posicion pg=g.getPosicion(); int idZ=zonaGato(pg);
        if (!zonasPermitidas(idZ)) return;
        Zona zg=grafo.getZona(idZ); if (zg==null) return;
        int[][] dirs={{-1,0},{1,0},{0,-1},{0,1}};
        for (int i=3;i>0;i--) { int j=(int)(Math.random()*(i+1)); int[] t=dirs[i]; dirs[i]=dirs[j]; dirs[j]=t; }
        for (int[] d:dirs) { int nr=pg.getRow()+d[0], nc=pg.getCol()+d[1];
            if (zg.esValida(nr,nc)) { Celda c=zg.getCelda(nr,nc); if (c.isTransitable()&&!c.isOcupada()) { zg.getCelda(pg.getRow(),pg.getCol()).setEntidad(null); g.moverA(new Posicion(nr,nc)); c.setEntidad(g); return; } } }
    }

    public boolean findGato() {
        if (gatoEncontrado) return true;
        if (zonaGato(gato.getPosicion())!=idZonaActual) return false;
        Posicion pg=gato.getPosicion(), pj=jugador.getPosicion();
        if (Math.abs(pj.getRow()-pg.getRow())<=1&&Math.abs(pj.getCol()-pg.getCol())<=1) {
            gatoEncontrado=true; log.registrar("Encontraste al gato! Toca llevarlo hasta la princesa."); return true;
        } return false;
    }

    private Posicion spawnGato() {
        int[] zonas=get().gato.zonasPermitidas;
        int z=zonas[(int)(Math.random()*zonas.length)]; idZonaGato=z;
        Zona zg=grafo.getZona(z);
        for (int i=0;i<50;i++) { int r=(int)(Math.random()*zg.getRows()), c=(int)(Math.random()*zg.getCols());
            Celda cel=zg.getCelda(r,c); if (cel!=null&&cel.isTransitable()&&!cel.isOcupada()&&cel.getTipoCelda()!=TipoCelda.AGUA) return new Posicion(r,c); }
        return null;
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
        if (zona.getIdZona()==0||zona.getIdZona()==5||zona.getIdZona()==9) return;
        Lista<Posicion> spawns=zona.getSpawnObjetos();

        if (spawns.getSize()>0) {
            for (int i=0;i<spawns.getSize();i++) {
                Posicion p = spawns.get(i);
                if (!zona.esValida(p.getRow(), p.getCol())) continue;
                Celda c = zona.getCelda(p.getRow(), p.getCol());
                if (!c.isOcupada() ) {
                    c.setObjeto(crearObjetoRandom());
                }
            }
            return;
        }
        int n=get().objetos.min+(int)(Math.random()*(get().objetos.max-get().objetos.min+1));
        for (int i=0;i<n;i++) {
            for (int intentos = 0; intentos < 50; intentos++) {
                int r = (int) (Math.random() * zona.getRows()), c = (int) (Math.random() * zona.getCols());
                Celda cel = zona.getCelda(r, c);
                if (cel != null && cel.isTransitable() && !cel.isOcupada() && cel.getTipoCelda() != TipoCelda.PUERTA && !cel.tieneObjeto()) {
                    cel.setObjeto(crearObjetoRandom());
                    break;
                }
            }
        }
    }

    private Objeto crearObjetoRandom() {
        return switch ((int)(Math.random()*5)) {
            case 0 -> new Objeto("Pocion de vida",TipoObjeto.CONSUMIBLE,0,0,5,0,1,"Cura 5");
            case 1 -> new Objeto("Pocion de ataque",TipoObjeto.CONSUMIBLE,2,0,0,5,1,"+2 atk +5 rango");
            case 2 -> new Objeto("Pocion de defensa",TipoObjeto.CONSUMIBLE,0,1,0,0,1,"+1 def");
            case 3 -> { Objeto o=new Objeto("Espada",TipoObjeto.EQUIPABLE,3,0,0,0,99,"Espada"); o.setSlot(SlotEquipable.ARMA); yield o; }
            default -> { Objeto o=new Objeto("Escudo",TipoObjeto.EQUIPABLE,0,2,0,0,99,"Escudo"); o.setSlot(SlotEquipable.ESCUDO); yield o; }
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