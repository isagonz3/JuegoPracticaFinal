package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.*;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorAccesoZonaBloqueada;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorCasillaOcupada;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorMovimientoInvalido;
import jueguito.juegopracticafinal.Modelo.Excepciones.ErrorMovimientoSinBarca;
import jueguito.juegopracticafinal.Modelo.Inventario.*;
import jueguito.juegopracticafinal.Modelo.Log.LogMovimiento;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.NPC.*;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.Vista.UIRenderer;
import jueguito.juegopracticafinal.TADs.Lista;

import static jueguito.juegopracticafinal.Modelo.Core.Configuracion.get;

public class Partida {

    //ABTRIBUTOS

    private Jugador jugador;
    private Gato gato;
    private GrafoZonas grafo;
    private Zona zonaActual;
    private int idZonaActual;
    private EstadoJuego estadoActual = EstadoJuego.EN_CURSO;
    private LogMovimiento log = new LogMovimiento();
    private int turnoActual;
    private int idZonaGato = -1;
    private boolean gatoEncontrado;
    private boolean faseJugadorActiva = true;
    private boolean movimientoRealizado = false;
    private boolean accionRealizada = false;
    private Lista<Celda> caminoMapa;
    private boolean mapaActivo;
    private PartidaMovimiento movimiento;
    private PartidaObjetosYCombate objetosYCombate;
    private UIRenderer ui;


    public Partida(GrafoZonas grafo) {
        this.grafo = grafo;
        this.movimiento = new PartidaMovimiento(this);
        this.objetosYCombate = new PartidaObjetosYCombate(this);
    }


    // INICIO DE PARTIDA

    public void iniciar() {
        Zona zi = grafo.getZona(get().zona.zonaInicial);
        if (zi == null) {
            log.registrar("ERROR: Zona inicial no encontrada");
            estadoActual = EstadoJuego.DERROTA;
            return;
        }

        Posicion spawn = zi.getSpawnJugador();
        if (spawn == null) {
            spawn = findSpawn(zi);
            zi.setSpawnJugador(spawn);
        }

        idZonaActual = get().zona.zonaInicial;
        zonaActual = zi;

        jugador = new Jugador(
                "NoLink",
                new Estadisticas(
                        get().jugador.vidaMax,
                        get().jugador.ataqueBase,
                        get().jugador.defensaBase,
                        get().jugador.rangoMov
                ),
                spawn
        );

        zonaActual.getCelda(spawn.getRow(), spawn.getCol()).setEntidad(jugador);

        gato = new Gato(
                "Gatiko",
                new Estadisticas(
                        get().gato.vida,
                        get().gato.ataque,
                        get().gato.defensa,
                        get().gato.rangoMov
                )
        );

        turnoActual = 0;

        colocarGatoFijo();
        colocarLlaveFija();
        colocarNPCsFijos();

        log.registrar("INICIANDO PARTIDA \n Bienvenido. Estas en el Interior de tu Casa");
    }


    // TURNOS

    public void iniciarTurno() {
        if (estadoActual != EstadoJuego.EN_CURSO) return;

        faseJugadorActiva = true;

        jugador.getEstadisticas()
                .setPuntosMovDisponibles(jugador.getRangoTotal());

        log.registrar("--- Turno " + turnoActual + " ---");
        movimientoRealizado = false;
        accionRealizada = false;
    }

    public void terminarTurno() {
        if (estadoActual != EstadoJuego.EN_CURSO) return;

        faseJugadorActiva = false;

        movimiento.moverEnemigos();
        objetosYCombate.atacarJugador();

        jugador.getInventario().avanzarTurno();

        zonaActual.setCountTurnos(zonaActual.getCountTurnos() + 1);
        turnoActual++;

        if (checkDerrota() || checkVictoria()) return;

        iniciarTurno();
    }


    // FIN DE PARTIDA

    private boolean checkVictoria() {

        if (gatoEncontrado && idZonaActual == 9) {

            estadoActual = EstadoJuego.VICTORIA;
            log.registrar("Has logrado recuperar el gato de la princesa!");
            return true;
        }

        return false;
    }

    private boolean checkDerrota() {
        if (!jugador.estarVivo()) {
            estadoActual = EstadoJuego.DERROTA;
            log.registrar("Has perdido: no te quedan puntos de vida!");
            return true;
        }

        if (turnoActual >= get().turno.maxTurnos) {
            estadoActual = EstadoJuego.DERROTA;
            log.registrar("Has perdido: no has logrado encontrar el gato a tiempo!");
            return true;
        }

        return false;
    }


    // CAMBIO DE ZONA

    public void cambiarZona(Puerta puerta)
            throws ErrorAccesoZonaBloqueada {

        if (puerta == null) return;
        if (estadoActual != EstadoJuego.EN_CURSO) return;
        if (!faseJugadorActiva) return;

        boolean mapaEstabaActivo = mapaActivo;
        int nid, dr, dc;

        if (puerta.getZonaOrigen() == idZonaActual) {

            nid = puerta.getZonaDestino();
            dr = puerta.getYDestino();
            dc = puerta.getXDestino();

        } else {

            nid = puerta.getZonaOrigen();
            dr = puerta.getYOrigen();
            dc = puerta.getXOrigen();
        }

        Zona nz = grafo.getZona(nid);

        if (nz == null) {

            log.registrar("Esta puerta no lleva a ninguna zona conocida");
            return;
        }

        //Validar que el jugador haya cogido la llave
        if (idZonaActual == 7 && nid == 8) {

            boolean tieneLlave = false;

            jueguito.juegopracticafinal.Modelo.Inventario.Inventario inv =
                    jugador.getInventario();

            for (int i = 0; i < inv.size(); i++) {

                if (inv.getObjeto(i) != null &&
                        "Llave".equalsIgnoreCase(
                                inv.getObjeto(i).getNombre()
                        )) {

                    tieneLlave = true;
                    break;
                }
            }

            if (!tieneLlave) {

                throw new ErrorAccesoZonaBloqueada(
                        "Necesitas la Llave para acceder a la Zona 8"
                );
            }

            log.registrar(
                    "Usaste la Llave para abrir el gran portón a la Zona 8..."
            );
        }

        //Registrar el cambio de zona
        log.registrar(
                "CAMBIO_ZONA:" + idZonaActual + "->" + nid
        );

        //Poblar la zona
        if (nz.getCountTurnos() == 0) {

            objetosYCombate.poblarZona(nz);
            objetosYCombate.ponerObjetos(nz);
        }

        //Validar que el jugador haya rescatado al gato
        if (idZonaActual == 8 && nid == 9) {

            if (gatoEncontrado) {

                log.registrar(
                        "Solo falta entregarle el gato a la princesa, acércate a ella"
                );

            } else {

                log.registrar(
                        "Has estrado sin el gato, vuelve a por él"
                );
            }
        }

        //Limpiar la posición en la que se encuentra el jugador
        zonaActual.getCelda(
                jugador.getPosicion().getRow(),
                jugador.getPosicion().getCol()
        ).setEntidad(null);

        //Descubrimiento de zona
        if (!nz.isVisitada()) {

            log.registrar("Nueva zona descubierta!");
        }

        nz.setVisitada(true);

        //Actualizar la zona actual
        idZonaActual = nid;
        zonaActual = nz;

        //Ajustar la posición
        dr = Math.min(
                Math.max(dr, 0),
                nz.getRows() - 1
        );

        dc = Math.min(
                Math.max(dc, 0),
                nz.getCols() - 1
        );

        //Mover el jugado
        jugador.moverA(new Posicion(dr, dc));

        nz.getCelda(dr, dc).setEntidad(jugador);

        //Log final
        log.registrar(
                "Has entrado en: " + nz.getNombreZona()
        );

        if (mapaEstabaActivo) {
            usarMapa();
        }

        accionCambioZona();
    }

    public void accionCambioZona() {

        if (estadoActual != EstadoJuego.EN_CURSO) return;

        zonaActual.setCountTurnos(zonaActual.getCountTurnos() + 1);
        turnoActual++;

        movimientoRealizado = false;
        accionRealizada = false;

        jugador.getInventario().avanzarTurno();

        log.registrar("--- Turno " + turnoActual + " ---");

        faseJugadorActiva = true;

        jugador.getEstadisticas()
                .setPuntosMovDisponibles(jugador.getRangoTotal());
    }


    //MÉTODOS NECESARIOS

    public boolean moverJugador(int row, int col)
            throws ErrorMovimientoInvalido,
            ErrorCasillaOcupada,
            ErrorMovimientoSinBarca,
            ErrorAccesoZonaBloqueada {

        return movimiento.moverJugador(row, col);
    }

    public Lista<Celda> getCeldasAccesibles() {
        return movimiento.getCeldasAccesibles();
    }

    public boolean atacarDireccion(int row, int col) {
        return objetosYCombate.atacarDireccion(row, col);
    }

    public boolean usarObjeto(Objeto o) {
        return objetosYCombate.usarObjeto(o);
    }

    public boolean recogerObjeto(Celda c) {
        return objetosYCombate.recogerObjeto(c);
    }

    public boolean equiparObjeto(Objeto o, SlotEquipable s) {
        return objetosYCombate.equiparObjeto(o, s);
    }

    public NPC interactNPC() {
        return objetosYCombate.interactNPC();
    }

    public boolean interactuarGato() {
        return objetosYCombate.interactuarGato();
    }

    public void usarMapa(){
        Zona zona = getZonaActual();
        Posicion pj = jugador.getPosicion();
        caminoMapa = null;
        mapaActivo = false;

        Celda salida = null;
        for (int i = 0; i < zona.getRows() && salida == null; i++) {
            for (int j = 0; j < zona.getCols() && salida == null; j++) {
                Celda c = zona.getCelda(i, j);
                if(c != null && c.getTipoCelda() == TipoCelda.SALIDA){
                    salida = c;
                }
            }
        }

        if(salida != null){
            caminoMapa = movimiento.calcularCamino(
                    pj.getRow(),pj.getCol(),salida.getRow(),salida.getCol(),zona
            );

            if(caminoMapa != null && caminoMapa.getSize() > 0){
                mapaActivo = true;
                log.registrar("Este es el mejor camino hacia el castillo");
                return;
            }
        }
        Lista<Integer> visitZonas = new Lista<>();
        Lista<Integer> prevZonas = new Lista<>();
        Cola<Integer> colaZonas = new Cola<>();

        for(int i = 0; i < grafo.getNumZonas();i++){
            visitZonas.add(-1);
            prevZonas.add(-1);
        }

        visitZonas.set(idZonaActual,idZonaActual);
        colaZonas.enqueue(idZonaActual);

        while(!colaZonas.isEmpty()){
            int actual = colaZonas.dequeue();
            if(actual == 9) break;

            Lista<Integer> ady = grafo.getAdyacentes(actual);
            for(int i = 0; i < ady.getSize();i++){
                int next = ady.get(i);
                if(visitZonas.get(next) == -1){
                    visitZonas.set(next,next);
                    prevZonas.set(next,actual);
                    colaZonas.enqueue(next);
                }
            }
        }

        if(visitZonas.get(9) != -1){
            int nextZona = 9;
            while(prevZonas.get(nextZona) != idZonaActual && prevZonas.get(nextZona) != -1){
                nextZona = prevZonas.get(nextZona);
            }

            Celda puerta = null;
            for(int i = 0; i < zona.getRows() && puerta == null; i++) {
                for (int j = 0; j < zona.getCols() && puerta == null; j++) {
                    Celda c = zona.getCelda(i, j);
                    if(c != null && c.tienePuerta()){
                        Puerta p = c.getPuerta();
                        int conectaA;
                        if(p.getZonaOrigen() == idZonaActual){
                            conectaA = p.getZonaDestino();
                        }
                        else{
                            conectaA = p.getZonaOrigen();
                        }

                        if(conectaA == nextZona){
                            puerta = c;
                        }
                    }
                }
            }
            if(puerta != null){
                caminoMapa = movimiento.calcularCamino(
                        pj.getRow(),pj.getCol(),puerta.getRow(),puerta.getCol(),zona
                );

                if(caminoMapa != null && caminoMapa.getSize() > 0){
                    mapaActivo = true;
                    log.registrar("Este es el mejor camino hacia la siguiente zona");
                    return;
                }
            }
        }
        log.registrar("No hay caminos hasta el castillo");
    }


    //COLOCAR LAS ENTIDADES FIJAS

    private Posicion findSpawn(Zona zona) {
        for (int i = 0; i < zona.getRows(); i++)
            for (int j = 0; j < zona.getCols(); j++) {
                Celda c = zona.getCelda(i, j);
                if (c != null && c.isTransitable() && !c.isOcupada())
                    return new Posicion(i, j);
            }
        return new Posicion(0, 0);
    }

    private void colocarGatoFijo() {
        int zonaId = 8;

        Zona zonaGato = grafo.getZona(zonaId);
        if (zonaGato == null) return;

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

        Posicion pos = new Posicion(24, 30);

        if (!zonaGato.esValida(24, 30)) return;

        Celda celda = zonaGato.getCelda(24, 30);
        if (celda == null || celda.isOcupada()) return;

        gato.setPosicion(pos);
        celda.setEntidad(gato);

        idZonaGato = zonaId;
    }

    private void colocarLlaveFija() {
        int zonaId = 6;
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
                TipoObjeto.NPC,
                0, 0, 0, 0,
                1,
                "Llave especial"
        );

        celda.setObjeto(llave);
    }

    public void colocarNPCsFijos() {
        //NPC 1
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

        //NPC 2
        Zona zona2 = grafo.getZona(6);

        if (zona2 != null && zona2.esValida(13,1)) {

            Celda celda2 = zona2.getCelda(13, 1);

            if (celda2 != null && !celda2.isOcupada()) {

                NPC npc2 = new NPC(
                        "Sirena",
                        TipoNPC.ALDEANO
                );

                npc2.setSprite("/entidades/NPC_2.png");

                celda2.setNpc(npc2);
            }
        }

        //NPC 3
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

        //NPC princesa
        Zona zonaPrincesa = grafo.getZona(9);

        if (zonaPrincesa != null && zonaPrincesa.esValida(25, 8)) {

            Celda celdaPrincesa = zonaPrincesa.getCelda(25, 8);

            if (celdaPrincesa != null && !celdaPrincesa.isOcupada()) {

                NPC princesa = new NPC(
                        "Princesa",
                        TipoNPC.ALDEANO
                );

                princesa.setSprite("/entidades/princesa.png");

                celdaPrincesa.setNpc(princesa);
            }
        }
    }


    // GETTERS Y SETTERS

    public Jugador getJugador() { return jugador; }
    public Zona getZonaActual() { return zonaActual; }
    public LogMovimiento getLog() { return log; }
    public boolean isFaseJugadorActiva() { return faseJugadorActiva; }
    public EstadoJuego getEstadoActual() { return estadoActual; }
    public void terminarTurnoPublic() { terminarTurno(); }
    public Gato getGato() {
        return gato;
    }
    public boolean isGatoEncontrado() {
        return gatoEncontrado;
    }
    public int getIdZonaActual() {
        return idZonaActual;
    }
    public int getTurnoActual() {
        return turnoActual;
    }
    public Lista<Celda> getCaminoMapa() { return caminoMapa; }
    public boolean isMapaActivo() { return mapaActivo; }
    public boolean isAccionRealizada() { return accionRealizada; }
    public void setAccionRealizada(boolean b) { this.accionRealizada = b; }
    public GrafoZonas getGrafoZonas() {
        return grafo;
    }
    public void setIdZonaActual(int idZonaActual) {
        this.idZonaActual = idZonaActual;
    }
    public void setZonaActual(Zona zonaActual) {
        this.zonaActual = zonaActual;
    }
    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }
    public void setGatoEncontrado(boolean gatoEncontrado) {
        this.gatoEncontrado = gatoEncontrado;
    }
    public void setEstadoActual(EstadoJuego estadoActual) {
        this.estadoActual = estadoActual;
    }
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    public void setGato(Gato gato) {
        this.gato = gato;
    }
    public void setUi(UIRenderer ui) {
        this.ui = ui;
    }
    public UIRenderer getUi() {
        return ui;
    }
}