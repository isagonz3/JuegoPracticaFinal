package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Inventario;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.NPC.TipoNPC;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.Modelo.Turno.ResultadoCombate;
import jueguito.juegopracticafinal.TADs.Lista;

import static jueguito.juegopracticafinal.Modelo.Core.Configuracion.get;

public class PartidaObjetosYCombate {

    private final Partida partida;

    public PartidaObjetosYCombate(Partida partida) {
        this.partida = partida;
    }

    // ============================================================
    // COMBATE
    // ============================================================

    //Calcular el daño con la formula dada

    public static int calcularHit(int ataque, int defensa) {
        return Math.max(0, (int)(ataque * (Math.random() * 2) - defensa));
    }

    public ResultadoCombate atacarEnemigo(Enemigo e) {

        if (partida.getEstadoActual() != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO)
            return null;

        if (!e.estarVivo()) {
            return new ResultadoCombate(0, 0, false, "Ya derrotado");
        }

        Jugador jugador = partida.getJugador();

        int ataque = jugador.getAtaqueTotal();
        int defensa = e.getDefensaTotal();

        int hit = calcularHit(ataque, defensa);

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

        if (partida.isAccionRealizada()) { partida.getLog().registrar("Ya realizaste una acción este turno"); return false; }

        Celda c = zonaActual.getCelda(
                jugador.getPosicion().getRow() + row,
                jugador.getPosicion().getCol() + col
        );

        if (c == null || !c.tieneEntidad() || !(c.getEntidad() instanceof Enemigo)) {
            partida.getLog().registrar("No hay enemigos por aqui");
            return false;
        }

        atacarEnemigo((Enemigo) c.getEntidad());
        partida.setAccionRealizada(true);
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

                // 1. Calculamos el daño mitigado por la defensa una sola vez
                int hit = calcularHit(ataque, defensa);

                // 2. Cambiamos recibirAtaque por nuestro nuevo método directo
                jugador.getEstadisticas().restarVidaDirecta(hit);

                // 3. El Log ahora mostrará exactamente lo que ha bajado en la barra azul
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
        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) return false;
        if (!partida.isFaseJugadorActiva()) return false;

        Jugador jugador = partida.getJugador();
        Inventario inv = jugador.getInventario();

        // Validaciones de seguridad (Si falla, NO pasa el turno, el jugador conserva su acción)
        if (o == null || o.getTipo() != TipoObjeto.USABLE || !inv.contiene(o)) {
            partida.getLog().registrar("No puedes usar este objeto o no lo tienes.");
            return false;
        }

        // Aplicar efectos del objeto de tu base de datos
        if (!o.usarObjeto()) {
            partida.getLog().registrar("No se pudo usar el objeto.");
            return false;
        }

        // Aplicar bonus estadísticas
        if (o.getVidaBonus() > 0) jugador.getEstadisticas().curarVida(o.getVidaBonus());
        if (o.getAtaqueBonus() > 0) jugador.getEstadisticas().aumentarAtaque(o.getAtaqueBonus());
        if (o.getDefensaBonus() > 0) jugador.getEstadisticas().aumentarDefensa(o.getDefensaBonus());
        if (o.getRangoBonus() > 0) jugador.getEstadisticas().aumentarRangoMov(o.getRangoBonus());

        partida.getLog().registrar("Usaste " + o.getNombre());

        o.setDuracionTurnos(2);
        o.resetTurnos();

        inv.getObjetosUsados().add(o);
        inv.removeObjeto(o);

        // Uso exitoso = El turno termina automáticamente
        partida.terminarTurnoPublic();
        return true;
    }

    public boolean recogerObjeto(Celda c) {

        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO)
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
            if(partida.getUi() != null) {
                partida.getUi().actualizarUI(partida);
            }
            return true;
        }

        return false;
    }

    public boolean equiparObjeto(Objeto o, SlotEquipable s) {
        if (partida.getEstadoActual() != EstadoJuego.EN_CURSO) return false;
        if (!partida.isFaseJugadorActiva()) return false;

        Jugador jugador = partida.getJugador();
        Inventario inv = jugador.getInventario();

        // Validaciones de seguridad
        if (o == null || o.getTipo() != TipoObjeto.EQUIPABLE || !inv.contiene(o)) {
            partida.getLog().registrar("No puedes equipar este objeto.");
            return false;
        }

        if (o.getSlot() == null || !o.getSlot().equals(s)) {
            partida.getLog().registrar("Este objeto no va en este slot.");
            return false;
        }

        // Intentar equipar en el inventario
        boolean ok = inv.equipar(o);
        if (!ok) {
            partida.getLog().registrar("No se puede equipar este objeto.");
            return false;
        }

        // Configurar la duración del objeto
        o.setDuracionTurnos(4);
        o.resetTurnos();

        inv.removeObjeto(o);

        partida.getLog().registrar("Equipaste " + o.getNombre());

        // El equipamiento ha sido un éxito -> El turno termina automáticamente
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

        // Zonas donde NO spawnear objetos
        if (zona.getIdZona() == 0 ||
                zona.getIdZona() == 5 ||
                zona.getIdZona() == 6 ||
                zona.getIdZona() == 9) {
            return;
        }

        Lista<Posicion> spawns = zona.getSpawnObjetos();

        int GEMAS = 2;
        int ALEATORIOS = 3;

        // ============================================================
        // SPAWNS FIJOS (Vienen del archivo .txt)
        // ============================================================
        if (spawns.getSize() > 0) {

            int colocados = 0;

            // Primero colocar GEMAS
            for (int i = 0; i < spawns.getSize() && colocados < GEMAS; i++) {

                Posicion pos = spawns.get(i);

                if (!zona.esValida(pos.getRow(), pos.getCol())) continue;

                Celda celda = zona.getCelda(pos.getRow(), pos.getCol());

                // Validamos que sea transitable (un '0' real) y que no sea puerta
                if (celda != null && celda.isTransitable() && !celda.isOcupada() && !celda.tieneObjeto()
                        && celda.getTipoCelda() != TipoCelda.PUERTA) {
                    celda.setObjeto(crearGema());
                    colocados++;
                }
            }

            // Luego colocar OBJETOS ALEATORIOS
            int colocadosAleatorios = 0;

            for (int i = 0; i < spawns.getSize() && colocadosAleatorios < ALEATORIOS; i++) {

                Posicion pos = spawns.get(i);

                if (!zona.esValida(pos.getRow(), pos.getCol())) continue;

                Celda celda = zona.getCelda(pos.getRow(), pos.getCol());

                // Validamos que sea transitable (un '0' real) y que no sea puerta
                if (celda != null && celda.isTransitable() && !celda.isOcupada() && !celda.tieneObjeto()
                        && celda.getTipoCelda() != TipoCelda.PUERTA) {
                    celda.setObjeto(crearObjetoRandom());
                    colocadosAleatorios++;
                }
            }

            // Asegurar mínimo de gemas
            asegurarMinimoGemas(zona, GEMAS);
            return;
        }

        // ============================================================
        // 2. SPAWN ALEATORIO (si no hay spawns fijos en el archivo)
        // ============================================================

        // Primero GEMAS
        int colocadasGemas = 0;

        while (colocadasGemas < GEMAS) {

            int fila = (int)(Math.random() * zona.getRows());
            int col = (int)(Math.random() * zona.getCols());

            Celda celda = zona.getCelda(fila, col);

            if (celda == null) continue;
            if (!celda.isTransitable()) continue;
            if (celda.isOcupada()) continue;
            if (celda.tieneObjeto()) continue;
            // No spawnear en puertas de forma aleatoria
            if (celda.getTipoCelda() == TipoCelda.PUERTA) continue;

            celda.setObjeto(crearGema());
            colocadasGemas++;
        }

        // Luego OBJETOS ALEATORIOS
        int colocadosAleatorios = 0;

        while (colocadosAleatorios < ALEATORIOS) {

            int fila = (int)(Math.random() * zona.getRows());
            int col = (int)(Math.random() * zona.getCols());

            Celda celda = zona.getCelda(fila, col);

            if (celda == null) continue;
            if (!celda.isTransitable()) continue;
            if (celda.isOcupada()) continue;
            if (celda.tieneObjeto()) continue;
            // No spawnear en puertas de forma aleatoria
            if (celda.getTipoCelda() == TipoCelda.PUERTA) continue;

            celda.setObjeto(crearObjetoRandom());
            colocadosAleatorios++;
        }

        asegurarMinimoGemas(zona, GEMAS);
    }


    private void asegurarMinimoGemas(Zona zona, int minimo) {

        int contador = 0;

        // Contar gemas existentes
        for (int i = 0; i < zona.getRows(); i++) {
            for (int j = 0; j < zona.getCols(); j++) {

                Celda celda = zona.getCelda(i, j);

                if (celda != null &&
                        celda.tieneObjeto() &&
                        "Gema".equalsIgnoreCase(celda.getObjeto().getNombre())) {

                    contador++;
                }
            }
        }

        // Añadir gemas hasta llegar al mínimo
        while (contador < minimo) {

            int fila = (int)(Math.random() * zona.getRows());
            int col = (int)(Math.random() * zona.getCols());

            Celda celda = zona.getCelda(fila, col);

            if (celda == null) continue;
            if (!celda.isTransitable()) continue;
            if (celda.isOcupada()) continue;
            if (celda.tieneObjeto()) continue;
            // En el relleno de seguridad tampoco permitimos puertas
            if (celda.getTipoCelda() == TipoCelda.PUERTA) continue;

            celda.setObjeto(crearGema());
            contador++;
        }
    }

    private Objeto crearGema() {
        return new Objeto(
                "Gema",
                TipoObjeto.NPC,
                0, 0, 0, 0,
                1,
                "Gema"
        );
    }

    private Objeto crearObjetoRandom() {

        double r = Math.random(); // 0.0 a 1.0

        if (r < 0.50) {
            return crearGema();
        }

        if (r < 0.65) {
            Objeto o = new Objeto("Espada", TipoObjeto.EQUIPABLE, 3, 0, 0, 0, 99, "Espada");
            o.setSlot(SlotEquipable.ARMA);
            return o;
        }

        if (r < 0.80) {
            Objeto o = new Objeto("Escudo", TipoObjeto.EQUIPABLE, 0, 2, 0, 0, 99, "Escudo");
            o.setSlot(SlotEquipable.ESCUDO);
            return o;
        }

        int tipo = (int)(Math.random() * 3);

        return switch (tipo) {
            case 0 -> new Objeto("Pocion de vida", TipoObjeto.USABLE, 0, 0, 5, 0, 1, "Cura 5");
            case 1 -> new Objeto("Pocion de ataque", TipoObjeto.USABLE, 2, 0, 0, 5, 1, "+2 atk +5 rango");
            default -> new Objeto("Pocion de defensa", TipoObjeto.USABLE, 0, 1, 0, 0, 1, "+1 def");
        };
    }

    // ============================================================
    // INTERACCIÓN CON EL GATO
    // ============================================================

    public boolean interactuarGato() {
        if (partida.getEstadoActual() != jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego.EN_CURSO)
            return false;

        Jugador jugador = partida.getJugador();
        Zona zonaActual = partida.getZonaActual();
        Posicion pj = jugador.getPosicion();
        Celda celdaJugador = zonaActual.getCelda(pj.getRow(), pj.getCol());

        // Buscamos al Gato en las celdas adyacentes
        for (int i = pj.getRow() - 1; i <= pj.getRow() + 1; i++) {
            for (int j = pj.getCol() - 1; j <= pj.getCol() + 1; j++) {

                if (!zonaActual.esValida(i, j)) continue;
                Celda c = zonaActual.getCelda(i, j);

                if (c.getEntidad() instanceof Gato) {
                    // 1. Creamos el objeto para el inventario basado en la entidad Gato
                    Objeto objetoGato = new Objeto(
                            "Gato",
                            TipoObjeto.USABLE,
                            0, 0, 0, 0, 1,
                            "El gato rescatado"
                    );

                    // 2. Intentar añadir al inventario
                    if (jugador.getInventario().addObjeto(objetoGato)) {
                        // 3. Eliminamos la entidad Gato del mapa (esto hará que desaparezca el PNG)
                        c.setEntidad(null);

                        // 4. Marcamos en partida que el gato fue encontrado
                        partida.setGatoEncontrado(true);

                        if(partida.getUi() != null) partida.getUi().actualizarUI(partida);
                        return true;
                    } else {
                        partida.getLog().registrar("¡Inventario lleno!");
                        return false;
                    }
                }
            }
        }

        partida.getLog().registrar("No hay ningún gato cerca.");
        return false;
    }
}
