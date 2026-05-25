package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.*;
import jueguito.juegopracticafinal.Modelo.Inventario.*;
import jueguito.juegopracticafinal.Modelo.Log.LogSistema;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.NPC.*;
import jueguito.juegopracticafinal.Modelo.Turno.*;
import jueguito.juegopracticafinal.TADs.Lista;

import static jueguito.juegopracticafinal.Modelo.Data.JugadorData.*;
import static jueguito.juegopracticafinal.Modelo.Data.GatoData.*;
import static jueguito.juegopracticafinal.Modelo.Data.ZonaData.*;

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

    private MovimientoManager movimientoManager;
    private CombateManager combateManager;
    private EnemigoManager enemigoManager;
    private GatoManager gatoManager;
    private ZonaManager zonaManager;
    private TurnoManager turnoManager;
    private ObjetoManager objetoManager;

    public Partida(GrafoZonas grafo) {
        this.grafo = grafo;
        this.movimientoManager = new MovimientoManager(this);
        this.combateManager = new CombateManager(this);
        this.enemigoManager = new EnemigoManager(this);
        this.gatoManager = new GatoManager(this);
        this.zonaManager = new ZonaManager(this);
        this.turnoManager = new TurnoManager(this);
        this.objetoManager = new ObjetoManager(this);
    }

    public void iniciar() {
        Zona zi = grafo.getZona(ZONA_INICIAL);
        if (zi == null) { log.registrar("ERROR: Zona inicial no encontrada"); estadoActual = EstadoJuego.DERROTA; return; }
        Posicion spawn = zi.getSpawnJugador();
        if (spawn == null) { spawn = findSpawn(zi); zi.setSpawnJugador(spawn); }

        idZonaActual = ZONA_INICIAL;
        zonaActual = zi;
        jugador = new Jugador("NoLink", new Estadisticas(VIDA_MAX, ATAQUE_BASE, DEFENSA_BASE, RANGO_MOV_J), spawn);
        jugador.setLog(log);
        zonaActual.getCelda(spawn.getRow(), spawn.getCol()).setEntidad(jugador);

        gato = new Gato("Gatiko", new Estadisticas(VIDA, ATAQUE, DEFENSA, RANGO_MOV_G));
        Posicion sg = gatoManager.spawnGato();
        if (sg != null) {
            gato.setPosicion(sg);
            for (int z : ZONAS_PERMITIDAS) {
                if (grafo.getZona(z).esValida(sg.getRow(), sg.getCol())) {
                    grafo.getZona(z).getCelda(sg.getRow(), sg.getCol()).setEntidad(gato); break;
                }
            }
        }
        turnoActual = 0;
        log.registrar("INICIANDO PARTIDA \n Bienvenido. Estas en el Interior de tu Casa");
    }

    public void iniciarTurno() { turnoManager.iniciarTurno(); }
    public void terminarTurno() { turnoManager.terminarTurno(); }
    public boolean moverJugador(int r, int c) { return movimientoManager.moverJugador(r, c); }
    public ResultadoCombate atacarEnemigo(Enemigo e) { return combateManager.atacarEnemigo(e); }
    public boolean atacarDireccion(int r, int c) { return combateManager.atacarDireccion(r, c); }
    public void cambiarZona(Puerta p) { zonaManager.cambiarZona(p); }
    public boolean findGato() { return gatoManager.findGato(); }
    public Lista<Celda> getCeldasAccesibles() { return movimientoManager.getCeldasAccesibles(); }


    public boolean usarObjeto(Objeto o) {
        if (estadoActual != EstadoJuego.EN_CURSO) return false;
        if (o.objetoGastado()) { jugador.getInventario().removeObjeto(o); log.registrar(o.getNombre()+" gastado!"); return false; }
        if (o.getVidaBonus() > 0) { jugador.getEstadisticas().curarVida(o.getVidaBonus()); log.registrar("Usaste "+o.getNombre()+": +"+o.getVidaBonus()+" vida"); }
        if (o.getAtaqueBonus() > 0) { jugador.getEstadisticas().aumentarAtaque(o.getAtaqueBonus()); log.registrar("Usaste "+o.getNombre()+": +"+o.getAtaqueBonus()+" ataque"); }
        if (o.getDefensaBonus() > 0) { jugador.getEstadisticas().aumentarDefensa(o.getDefensaBonus()); log.registrar("Usaste "+o.getNombre()+": +"+o.getDefensaBonus()+" defensa"); }
        if (o.getRangoBonus() > 0) { jugador.getEstadisticas().aumentarRangoMov(o.getRangoBonus()); log.registrar("Usaste "+o.getNombre()+": +"+o.getRangoBonus()+" rango"); }
        o.usarObjeto();
        if (o.objetoGastado()) { jugador.getInventario().removeObjeto(o); log.registrar(o.getNombre()+" gastado!"); }
        return true;
    }

    public boolean recogerObjeto(Celda c) {
        if (estadoActual != EstadoJuego.EN_CURSO || !c.tieneObjeto()) return false;
        if (jugador.getInventario().inventarioLleno()) { log.registrar("Inventario lleno!"); return false; }
        Objeto o = c.getObjeto();
        if (jugador.getInventario().addObjeto(o)) { c.setObjeto(null); log.registrar("Recogiste "+o.getNombre()+"!"); return true; }
        return false;
    }

    public boolean equiparObjeto(Objeto o, SlotEquipable s) {
        if (estadoActual != EstadoJuego.EN_CURSO) return false;
        if (jugador.equipar(o, s)) { log.registrar("Equipaste "+o.getNombre()+"!"); return true; }
        return false;
    }

    public boolean interactNPC() {
        if (estadoActual != EstadoJuego.EN_CURSO) return false;
        Posicion pj = jugador.getPosicion();
        for (int i = 0; i < zonaActual.getRows(); i++)
            for (int j = 0; j < zonaActual.getCols(); j++) {
                Celda c = zonaActual.getCelda(i, j);
                if (c.tieneNPC() && c.esAdyacente(zonaActual.getCelda(pj.getRow(), pj.getCol()))) {
                    NPC npc = c.getNpc();
                    log.registrar(npc.getNombre()+": "+npc.hablar());
                    if (npc.tieneInfoGato()) log.registrar("Pista: "+npc.getInfoGato());
                    return true;
                }
            }
        log.registrar("Hablar solo da mal rollo!"); return false;
    }

    public boolean realizarTradeo(NPC n, Tradeo t) {
        if (estadoActual != EstadoJuego.EN_CURSO || !n.comerciar()) return false;
        if (t.tradear(jugador)) { log.registrar("Un placer hacer negocios"); return true; }
        log.registrar("No hay trato"); return false;
    }


    private Posicion findSpawn(Zona zona) {
        for (int i = 0; i < zona.getRows(); i++)
            for (int j = 0; j < zona.getCols(); j++) {
                Celda c = zona.getCelda(i, j);
                if (c != null && c.isTransitable() && !c.isOcupada()) return new Posicion(i, j);
            }
        return new Posicion(0, 0);
    }

    public Lista<Celda> getCaminoMinimo(int row, int col) {
        return movimientoManager.getCaminoMinimo(row, col);
    }


    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Gato getGato() { return gato; }
    public void setGato(Gato gato) { this.gato = gato; }

    public GrafoZonas getGrafo() { return grafo; }

    public Zona getZonaActual() { return zonaActual; }
    public void setZonaActual(Zona z) { zonaActual = z; }

    public int getIdZonaActual() { return idZonaActual; }
    public void setIdZonaActual(int id) { idZonaActual = id; zonaActual = grafo.getZona(id); }

    public EstadoJuego getEstadoActual() { return estadoActual; }
    public void setEstadoActual(EstadoJuego e) { estadoActual = e; }

    public LogSistema getLog() { return log; }

    public int getTurnoActual() { return turnoActual; }
    public void setTurnoActual(int t) { turnoActual = t; }

    public boolean isGatoEncontrado() { return gatoEncontrado; }
    public void setGatoEncontrado(boolean b) { gatoEncontrado = b; }

    public int getIdZonaGato() { return idZonaGato; }
    public void setIdZonaGato(int id) { idZonaGato = id; }


    public MovimientoManager getMovimientoManager() { return movimientoManager; }
    public CombateManager getCombateManager() { return combateManager; }
    public EnemigoManager getEnemigoManager() { return enemigoManager; }
    public GatoManager getGatoManager() { return gatoManager; }
    public ZonaManager getZonaManager() { return zonaManager; }
    public TurnoManager getTurnoManager() { return turnoManager; }
    public ObjetoManager getObjetoManager() { return objetoManager; }

}