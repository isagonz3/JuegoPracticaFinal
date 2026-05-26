package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;
import jueguito.juegopracticafinal.Modelo.Turno.ResultadoCombate;
import jueguito.juegopracticafinal.TADs.Lista;

import static jueguito.juegopracticafinal.Modelo.Core.Configuracion.get;

public class PartidoObjetosYCombate {

    private final Partida partida;

    public PartidoObjetosYCombate(Partida partida) {
        this.partida = partida;
    }

    // ============================================================
    // COMBATE
    // ============================================================

    public ResultadoCombate atacarEnemigo(Enemigo e) {

        if (partida.getEstadoActual() != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO)
            return null;

        if (!e.estarVivo()) {
            return new ResultadoCombate(0, 0, false, "Ya derrotado");
        }

        Jugador jugador = partida.getJugador();

        int ataque = jugador.getAtaqueTotal();
        int defensa = e.getDefensaTotal();

        double aleatorio = Math.random();

        int hit = Math.max(
                0,
                (int) (ataque * (aleatorio * 2) - defensa)
        );

        e.recibirAtaque(hit);
        e.setAtacado(true);

        Zona zonaActual = partida.getZonaActual();

        if (!e.estarVivo()) {
            zonaActual.getCelda(
                    e.getPosicion().getRow(),
                    e.getPosicion().getCol()
            ).setEntidad(null);

            partida.getLog().registrar("Has derrotado a " + e.getNombre() + "!!");
        } else {
            partida.getLog().registrar("Has atacado a " + e.getNombre() + ", recibe " + hit + " de daño");
        }

        return new ResultadoCombate(
                hit,
                e.getEstadisticas().getVidaActual(),
                !e.estarVivo(),
                ""
        );
    }

    public boolean atacarDireccion(int row, int col) {

        if (partida.getEstadoActual() != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO)
            return false;

        Jugador jugador = partida.getJugador();
        Zona zonaActual = partida.getZonaActual();

        Celda c = zonaActual.getCelda(
                jugador.getPosicion().getRow() + row,
                jugador.getPosicion().getCol() + col
        );

        if (c == null || !c.tieneEntidad() || !(c.getEntidad() instanceof Enemigo)) {
            partida.getLog().registrar("No hay enemigos por aqui");
            return false;
        }

        atacarEnemigo((Enemigo) c.getEntidad());
        return true;
    }

    public void atacarJugador() {

        Jugador jugador = partida.getJugador();
        Zona zonaActual = partida.getZonaActual();
        Posicion pj = jugador.getPosicion();

        for (int i = 0; i < zonaActual.getRows(); i++) {
            for (int j = 0; j < zonaActual.getCols(); j++) {

                Celda c = zonaActual.getCelda(i, j);

                if (!(c.getEntidad() instanceof Enemigo e) || !e.estarVivo())
                    continue;

                if (!c.esAdyacente(zonaActual.getCelda(pj.getRow(), pj.getCol())))
                    continue;

                int ataque = e.getAtaqueTotal();
                int defensa = jugador.getDefensaTotal();

                double aleatorio = Math.random();

                int hit = Math.max(
                        0,
                        (int) (ataque * (aleatorio * 2) - defensa)
                );

                jugador.recibirAtaque(hit);

                partida.getLog().registrar(
                        "Has sido atacado por " + e.getNombre() +
                                ", recibes " + hit + " de daño"
                );
            }
        }
    }

    // ============================================================
    // OBJETOS
    // ============================================================

    public boolean usarObjeto(Objeto o) {

        if (partida.getEstadoActual() != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO)
            return false;

        if (!partida.isFaseJugadorActiva())
            return false;

        Jugador jugador = partida.getJugador();

        if (!jugador.getInventario().contiene(o))
            return false;

        if (!o.usarObjeto())
            return false;

        if (o.getVidaBonus() > 0)
            jugador.getEstadisticas().curarVida(o.getVidaBonus());

        if (o.getAtaqueBonus() > 0)
            jugador.getEstadisticas().aumentarAtaque(o.getAtaqueBonus());

        if (o.getDefensaBonus() > 0)
            jugador.getEstadisticas().aumentarDefensa(o.getDefensaBonus());

        if (o.getRangoBonus() > 0)
            jugador.getEstadisticas().aumentarRangoMov(o.getRangoBonus());

        partida.getLog().registrar("Usaste " + o.getNombre());

        if (o.objetoGastado()) {
            jugador.getInventario().removeObjeto(o);
        }

        partida.terminarTurnoPublic();

        return true;
    }

    public boolean recogerObjeto(Celda c) {

        if (partida.getEstadoActual() != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO)
            return false;

        if (!c.tieneObjeto())
            return false;

        Jugador jugador = partida.getJugador();
        Objeto o = c.getObjeto();

        if (jugador.getInventario().inventarioLleno()) {
            partida.getLog().registrar("Inventario lleno!");
            return false;
        }

        boolean ok = jugador.getInventario().addObjeto(o);

        if (ok) {
            c.setObjeto(null);
            partida.getLog().registrar("Recogiste " + o.getNombre() + "!");
            return true;
        }

        return false;
    }

    public boolean equiparObjeto(Objeto o, SlotEquipable s) {

        if (partida.getEstadoActual() != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO)
            return false;

        if (!partida.isFaseJugadorActiva())
            return false;

        Jugador jugador = partida.getJugador();

        if (o == null || o.getSlot() == null)
            return false;

        if (!o.getSlot().equals(s))
            return false;

        if (!jugador.getInventario().contiene(o))
            return false;

        Objeto actual = jugador.getInventario().getEquipado(s);

        if (actual != null) {
            partida.getLog().registrar("Ya tienes un " + s + " equipado");
            return false;
        }

        boolean ok = jugador.getInventario().equipar(o);

        if (!ok)
            return false;

        jugador.getInventario().removeObjeto(o);

        o.setDuracionTurnos(5);
        o.resetTurnos();

        partida.getLog().registrar("Equipaste " + o.getNombre());

        partida.terminarTurnoPublic();

        return true;
    }

    // ============================================================
    // NPC
    // ============================================================

    public NPC interactNPC() {

        if (partida.getEstadoActual() != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO)
            return null;

        Jugador jugador = partida.getJugador();
        Zona zonaActual = partida.getZonaActual();

        Posicion pj = jugador.getPosicion();
        Celda celdaJugador = zonaActual.getCelda(pj.getRow(), pj.getCol());

        for (int i = 0; i < zonaActual.getRows(); i++) {
            for (int j = 0; j < zonaActual.getCols(); j++) {

                Celda c = zonaActual.getCelda(i, j);

                if (c.tieneNPC()) {

                    NPC npc = c.getNpc();

                    if (npc.getTipo() == TipoNPC.ALDEANO &&
                            c.esAdyacente(celdaJugador)) {
                        return npc;
                    }

                    if (npc.getTipo() == TipoNPC.COMERCIANTE &&
                            c.esAdyacenteDistancia(celdaJugador, 2)) {
                        return npc;
                    }
                }
            }
        }

        return null;
    }

    // ============================================================
    // SPAWN DE ENEMIGOS
    // ============================================================

    public void poblarZona(Zona zona) {

        if (zona.getIdZona() == 0 || zona.getIdZona() == 1 || zona.getIdZona() == 5)
            return;

        Lista<Posicion> spawns = zona.getSpawnEnemigos();

        if (spawns.getSize() > 0) {

            for (int i = 0; i < spawns.getSize(); i++) {

                Posicion s = spawns.get(i);

                if (!zona.esValida(s.getRow(), s.getCol()))
                    continue;

                Celda c = zona.getCelda(s.getRow(), s.getCol());

                if (c.isOcupada())
                    continue;

                Enemigo e = new Enemigo(
                        "Chungo",
                        new jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas(
                                get().enemigos.vidaBase,
                                get().enemigos.ataqueBase,
                                get().enemigos.defensaBase,
                                get().enemigos.rangoMov
                        )
                );

                e.setPosicion(new Posicion(s.getRow(), s.getCol()));
                c.setEntidad(e);
            }

            return;
        }

        int n = get().enemigos.min +
                (int) (Math.random() * (get().enemigos.max - get().enemigos.min + 1));

        for (int i = 0; i < n; i++) {

            for (int intentos = 0; intentos < 50; intentos++) {

                int r = (int) (Math.random() * zona.getRows());
                int co = (int) (Math.random() * zona.getCols());

                Celda c = zona.getCelda(r, co);

                if (c != null &&
                        c.isTransitable() &&
                        !c.isOcupada() &&
                        c.getTipoCelda() != TipoCelda.PUERTA) {

                    Enemigo e = new Enemigo(
                            "Chungo",
                            new jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas(
                                    get().enemigos.vidaBase + (int) (Math.random() * 10),
                                    get().enemigos.ataqueBase + (int) (Math.random() * 3),
                                    get().enemigos.defensaBase + (int) (Math.random() * 2),
                                    get().enemigos.rangoMov
                            )
                    );

                    e.setPosicion(new Posicion(r, co));
                    c.setEntidad(e);
                    break;
                }
            }
        }
    }

    // ============================================================
    // SPAWN DE OBJETOS
    // ============================================================

    public void ponerObjetos(Zona zona) {

        if (zona.getIdZona() == 0 ||
                zona.getIdZona() == 5 ||
                zona.getIdZona() == 9) {
            return;
        }

        Lista<Posicion> spawns = zona.getSpawnObjetos();

        if (spawns.getSize() > 0) {

            for (int i = 0; i < spawns.getSize(); i++) {

                Posicion pos = spawns.get(i);

                if (!zona.esValida(pos.getRow(), pos.getCol()))
                    continue;

                Celda celda = zona.getCelda(pos.getRow(), pos.getCol());

                if (celda != null && !celda.isOcupada() && !celda.tieneObjeto()) {
                    celda.setObjeto(crearObjetoRandom());
                }
            }

            asegurarMinimoGemas(zona, 3);
            return;
        }

        int cantidad = get().objetos.min +
                (int) (Math.random() * (get().objetos.max - get().objetos.min + 1));

        for (int i = 0; i < cantidad; i++) {

            for (int intentos = 0; intentos < 50; intentos++) {

                int fila = (int) (Math.random() * zona.getRows());
                int col = (int) (Math.random() * zona.getCols());

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

    public void asegurarMinimoGemas(Zona zona, int minimo) {

        int contador = 0;

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

        while (contador < minimo) {

            int fila = (int) (Math.random() * zona.getRows());
            int col = (int) (Math.random() * zona.getCols());

            Celda celda = zona.getCelda(fila, col);

            if (celda == null) continue;
            if (!celda.isTransitable()) continue;
            if (celda.isOcupada()) continue;
            if (celda.tieneObjeto()) continue;

            celda.setObjeto(new Objeto(
                    "Gema",
                    TipoObjeto.NPC,
                    0, 0, 0, 0,
                    1,
                    "Gema"
            ));

            contador++;
        }
    }

    public Objeto crearObjetoRandom() {

        double r = Math.random();

        if (r < 0.5) {
            return new Objeto(
                    "Gema",
                    TipoObjeto.NPC,
                    0, 0, 0, 0,
                    1,
                    "Gema"
            );
        }

        if (r < 0.65) {

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

        int tipo = (int) (Math.random() * 3);

        return switch (tipo) {
            case 0 -> new Objeto("Pocion de vida", TipoObjeto.USABLE, 0, 0, 5, 0, 1, "Cura 5");
            case 1 -> new Objeto("Pocion de ataque", TipoObjeto.USABLE, 2, 0, 0, 5, 1, "+2 atk +5 rango");
            default -> new Objeto("Pocion de defensa", TipoObjeto.USABLE, 0, 1, 0, 0, 1, "+1 def");
        };
    }
}
