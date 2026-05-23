package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Log.LogSistema;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;
import jueguito.juegopracticafinal.Modelo.NPC.Tradeo;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import jueguito.juegopracticafinal.Modelo.Turno.ResultadoCombate;
import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.Lista;

public class Partida {
    private static final int MAX_TURNOS = 1000;

    private Jugador jugador;
    private Gato gato;
    private GrafoZonas grafo;
    private Zona zonaActual;
    private int idZonaActual;
    private EstadoJuego estadoActual;
    private LogSistema log;
    private int turnoActual;
    private boolean gatoEncontrado;
    private boolean accionRealizada;
    private boolean movimientoRealizado;

    public Partida(GrafoZonas grafo){
        this.grafo = grafo;
        this.log = new LogSistema();
        this.estadoActual = EstadoJuego.EN_CURSO;
        this.turnoActual = 0;
        this.gatoEncontrado = false;
        this.accionRealizada = false;
        this.movimientoRealizado = false;
    }

    public void iniciar(){
        Zona zonaInicial = grafo.getZona(0);
        if(zonaInicial == null){
            log.registrar("ERROR: No se ha podido encontrar la zona inicial");
            estadoActual = EstadoJuego.DERROTA;
            return;
        }

        Posicion spawn = zonaInicial.getSpawnJugador();
        if(spawn == null){
            spawn = findSpawnDisponible(zonaInicial);
            zonaInicial.setSpawnJugador(spawn);
        }

        this.idZonaActual = 0;
        this.zonaActual = zonaInicial;

        Estadisticas statsJugador = new Estadisticas(20,5,2,8);
        this.jugador = new Jugador("Klin",statsJugador,spawn);
        zonaActual.getCelda(spawn.getRow(),spawn.getCol()).setEntidad(jugador);

        Estadisticas statsGato = new Estadisticas(10,0,0,4);
        this.gato = new Gato("Naay",statsGato);

        int[] zonasGato = {2, 4, 6, 7, 8};
        int zonaGato = zonasGato[(int)(Math.random()*zonasGato.length)];

        Posicion spawnGato = spawnAleatorio(grafo.getZona(zonaGato));
        if(spawnGato != null){
            gato.setPosicion(spawnGato);
            grafo.getZona(zonaGato).getCelda(spawnGato.getRow(),spawnGato.getCol()).setEntidad(gato);
        }

        this.turnoActual = 0;
        log.registrar("INICIANDO PARTIDA");
        log.registrar("Bienvenido. Estas en el Interior de tu casa");
    }

    public void iniciarTurno(){
        if(estadoActual != EstadoJuego.EN_CURSO){
            return;
        }
        jugador.getEstadisticas().resetMovimiento();
        accionRealizada = false;
        movimientoRealizado = false;
        log.registrar("--- Turno " +  turnoActual + " ---");
    }

    public void terminarTurno(){
        if(estadoActual != EstadoJuego.EN_CURSO){
            return;
        }
        getMovimientoEnemigo();
        getMovimientoGato();

        zonaActual.setCountTurnos(zonaActual.getCountTurnos()+1);
        turnoActual++;
        accionRealizada = false;
        movimientoRealizado = false;

        if(checkDerrota()){
            return;
        }

        if(checkVictoria()){
            return;
        }
        iniciarTurno();
    }

    public boolean moverJugador(int row, int col){
        if(estadoActual != EstadoJuego.EN_CURSO){
            return false;
        }
        Celda destino = zonaActual.getCelda(row,col);
        if(destino == null){
            return false;
        }
        if(!destino.isTransitable()){
            log.registrar("No puedes moverte a " + destino.getTipoCelda());
            return false;
        }
        if(destino.isOcupada()){
            return  false;
        }

        Posicion actual = jugador.getPosicion();
        int puntosMov = jugador.getEstadisticas().getPuntosMovDisponibles();
        Lista<Celda> accesibles = zonaActual.getCeldasAccesibles(actual.getRow(),actual.getCol(),puntosMov);

        boolean accesible = false;
        for (int i = 0; i < accesibles.getSize(); i++){
            Celda celda = accesibles.get(i);
            if(celda.getRow() == row && celda.getCol() == col){
                accesible = true;
                break;
            }
        }
        if(!accesible){
            log.registrar("No tienes suficientes puntos de movimiento");
            return false;
        }

        int coste = calcularDist(actual.getRow(),actual.getCol(),row,col);
        if(!jugador.getEstadisticas().usarMovimiento(coste)){
            return false;
        }

        Celda origen = zonaActual.getCelda(actual.getRow(),actual.getCol());
        origen.setEntidad(null);
        jugador.moverA(new Posicion(row,col));
        destino.setEntidad(jugador);
        movimientoRealizado = true;

        if(destino.tienePuerta()){
            cambiarZona(destino.getPuerta());
        }
        if(destino.getTipoCelda() == TipoCelda.AGUA && destino.tieneObjeto()){
            Objeto objeto = destino.getObjeto();
            log.registrar("Has encontrado " + objeto.getNombre() + " en el agua!");
            destino.setObjeto(null);
        }
        return true;
    }

    private int calcularDist(int row1, int col1, int row2, int col2){
        boolean[][] visitadas = new boolean[zonaActual.getRows()][zonaActual.getCols()];
        Cola<int[]> cola = new Cola<>();

        cola.enqueue(new int[]{row1,col1,0});
        visitadas[row1][col1] = true;

        while(!cola.isEmpty()){
            int[] actual = cola.dequeue();
            int row = actual[0];
            int col = actual[1];
            int data = actual[2];

            if(row == row2 && col == col2){
                return data;
            }

            int[][] coordenadas = {{-1,0}, {1,0}, {0,-1}, {0,1}}; //Norte, Sur, Oeste, Este
            for(int[] coordenada : coordenadas){
                int newRow = row + coordenada[0];
                int newCol = col + coordenada[1];

                if(zonaActual.esValida(newRow,newCol) && !visitadas[newRow][newCol]){
                    Celda celda = zonaActual.getCelda(newRow,newCol);
                    if(celda != null && celda.isTransitable() && !celda.isOcupada()){
                        visitadas[newRow][newCol] = true;
                        cola.enqueue(new int[]{newRow,newCol,data +1});
                    }
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    public void cambiarZona(Puerta puerta){
        if(puerta == null){
            return;
        }
        boolean esSalida = (puerta.getXDestino() == -1 && puerta.getYDestino() == -1);
        int nuevaIdZona;
        int destinoRow;
        int destinoCol;

        if(puerta.getZonaOrigen() == idZonaActual){
            nuevaIdZona = puerta.getZonaDestino();
            destinoRow = puerta.getYDestino();
            destinoCol = puerta.getXDestino();
        }
        else{
            nuevaIdZona = puerta.getZonaOrigen();
            destinoRow = puerta.getYOrigen();
            destinoCol = puerta.getXOrigen();
        }

        if(esSalida){
            if(nuevaIdZona >= 0 && nuevaIdZona < 10){
                nuevaIdZona = puerta.getZonaDestino();
                destinoRow = puerta.getYDestino();
                destinoCol = puerta.getXDestino();
            }
        }

        Zona nuevaZona = grafo.getZona(nuevaIdZona);
        if(nuevaZona == null){
            log.registrar("Esta puerta no lleva a ninguna zona conocida");
            return;
        }

        if(nuevaIdZona == 9 && !gatoEncontrado){
            log.registrar("DETENGAN AL INTRUSO!: Has entrado al castillo sin el gato y la guardia real te ha mandado al calabozo");
            estadoActual = EstadoJuego.DERROTA;
            return;
        }

        Celda origen = zonaActual.getCelda(jugador.getPosicion().getRow(),jugador.getPosicion().getCol());
        origen.setEntidad(null);

        if(!nuevaZona.isVisitada()){
            turnoActual--;
            log.registrar("Has descubierto una nueva zona del mapa!");
        }

        nuevaZona.setVisitada(true);
        this.zonaActual = nuevaZona;
        this.idZonaActual = nuevaIdZona;

        int destRow = Math.min(Math.max(destinoRow, 0), nuevaZona.getRows()-1);
        int destCol = Math.min(Math.max(destinoCol, 0), nuevaZona.getCols()-1);

        jugador.moverA(new Posicion(destRow,destCol));
        nuevaZona.getCelda(destRow,destCol).setEntidad(jugador);

        log.registrar("Has entrado en: " + nuevaZona.getNombreZona());
        if(nuevaZona.getCountTurnos() == 0){
            poblarZona(nuevaZona);
        }
    }

    private void poblarZona(Zona zona){
        if(zona.getIdZona() == 0 || zona.getIdZona() == 5){
            return;
        }

        Lista<Posicion> spawnEnemigos = zona.getSpawnEnemigos();
        for(int i = 0; i < spawnEnemigos.getSize(); i++){
            Posicion posicion = spawnEnemigos.get(i);
            if(zona.esValida(posicion.getRow(),posicion.getCol())){
                Celda celda = zona.getCelda(posicion.getRow(),posicion.getCol());

                if(!celda.isOcupada()){
                    int vida = 5 + (int)(Math.random()*10);
                    int ataque = 2 + (int)(Math.random()*3);
                    int defensa = 1 + (int)(Math.random()*2);

                    Enemigo enemigo = new Enemigo("Chungo",new Estadisticas(vida,ataque,defensa,4));
                    enemigo.setPosicion(new Posicion(posicion.getRow(),posicion.getCol()));
                    celda.setEntidad(enemigo);
                }
            }
        }
    }

    public ResultadoCombate atacarEnemigo(Enemigo enemigo){
        if(estadoActual != EstadoJuego.EN_CURSO){
            return null;
        }
        if(!enemigo.estarVivo()){
            return new ResultadoCombate(0,0,false,"Enemigo ya derrotado");
        }

        int ataque = jugador.getAtaqueTotal();
        int defensa = jugador.getDefensaTotal();
        int hit = Math.max(0,(int)(ataque*Math.random()*2) - defensa);
        enemigo.recibirAtaque(hit);
        boolean enemigoKO = !enemigo.estarVivo();

        String mensaje;
        if(enemigoKO){
            Celda celda = zonaActual.getCelda(enemigo.getPosicion().getRow(),enemigo.getPosicion().getCol());
            celda.setEntidad(null);
            mensaje = "Has derrotado a " + enemigo.getNombre() + "!!";
        }
        else{
            mensaje = "Has atacado a " + enemigo.getNombre() + ", recibe " + hit + " puntos de daño";
        }

        log.registrar(mensaje);
        accionRealizada = true;
        return new ResultadoCombate(hit,enemigo.getEstadisticas().getVidaActual(),enemigoKO,mensaje);
    }

    private void getAtaqueEnemigo(){
        Posicion posicion = jugador.getPosicion();
        for(int i = 0; i < zonaActual.getRows(); i++){
            for(int j = 0; j < zonaActual.getCols(); j++){
                Celda celda = zonaActual.getCelda(i, j);
                if(celda.tieneEntidad() && celda.getEntidad() instanceof Enemigo){
                    Enemigo enemigo = (Enemigo)celda.getEntidad();

                    if(!enemigo.estarVivo()){
                        continue;
                    }

                    if(celda.esAdyacente(zonaActual.getCelda(posicion.getRow(),posicion.getCol()))){
                        int hit = Math.max(0, enemigo.getAtaqueTotal() - jugador.getDefensaTotal());
                        jugador.recibirAtaque(hit);
                        log.registrar("Has sido atacado por " + enemigo.getNombre() + ", recibes " + hit + " puntos de daño");
                    }
                }
            }
        }
    }

    private void getMovimientoEnemigo(){
        Posicion posicion = jugador.getPosicion();
        for(int i = 0; i < zonaActual.getRows(); i++){
            for(int j = 0; j < zonaActual.getCols(); j++){
                Celda celda = zonaActual.getCelda(i, j);
                if(celda.tieneEntidad() && celda.getEntidad() instanceof Enemigo){
                    Enemigo enemigo = (Enemigo)celda.getEntidad();

                    if(!enemigo.estarVivo()){
                        continue;
                    }

                    if(celda.esAdyacente(zonaActual.getCelda(posicion.getRow(),posicion.getCol()))){
                        continue;
                    }

                    int dirEnemigo = -1;
                    int distEnemigo = Integer.MAX_VALUE;
                    int[][] coordenadas = {{-1,0}, {1,0}, {0,-1}, {0,1}}; //Norte, Sur, Oeste, Este

                    for(int d = 0; d < coordenadas.length; d++){
                        int newRow = i  + coordenadas[d][0];
                        int newCol = j + coordenadas[d][1];

                        if(zonaActual.esValida(newRow,newCol)){
                            Celda ady = zonaActual.getCelda(newRow,newCol);
                            if(ady.isTransitable() && !ady.isOcupada()){
                                int distancia = Math.abs(newRow - posicion.getRow()) + Math.abs(newCol - posicion.getCol());
                                if(distancia < distEnemigo){
                                    distEnemigo = distancia;
                                    dirEnemigo = d;
                                }
                            }
                        }
                    }

                    if(dirEnemigo >= 0){
                        int newRow = i  + coordenadas[dirEnemigo][0];
                        int newCol = j + coordenadas[dirEnemigo][1];

                        Celda origen = zonaActual.getCelda(i,j);
                        Celda destino = zonaActual.getCelda(newRow,newCol);
                        origen.setEntidad(null);
                        enemigo.moverA(new Posicion(newRow,newCol));
                        destino.setEntidad(enemigo);
                    }

                }
            }
            getAtaqueEnemigo();
        }
    }

    private void getMovimientoGato(){
        if(gatoEncontrado){
            Posicion posJug = jugador.getPosicion();
            Posicion posGato = gato.getPosicion();

            if(idZonaActual == findZonaPorPosicion(posGato)){
                int destinoRow = Integer.signum(posJug.getRow() - posGato.getRow());
                int destinoCol = Integer.signum(posJug.getCol() - posGato.getCol());

                int newRow = posGato.getRow() + destinoRow;
                int newCol = posGato.getCol() + destinoCol;
                Zona zonaGato = grafo.getZona(findZonaPorPosicion(posGato));

                if(zonaGato != null && zonaGato.esValida(newRow,newCol)){
                    Celda celda = zonaGato.getCelda(newRow,newCol);
                    if(celda.isTransitable() && !celda.isOcupada()){
                        Celda origen = zonaGato.getCelda(posGato.getRow(),posGato.getCol());
                        origen.setEntidad(null);
                        gato.moverA(new Posicion(newRow,newCol));
                        celda.setEntidad(gato);
                    }
                }
            }
        }
        else{
            if(Math.random() < 0.3){  //30% posibilidad que el gato se mueva
                Posicion posGato = gato.getPosicion();
                int idZonaGato = findZonaPorPosicion(posGato);
                Zona zonaGato = grafo.getZona(idZonaGato);
                if(zonaGato == null){
                    return;
                }

                if (!esZonaPermitidaGato(idZonaGato)) return;

                int[][] coordenadas = {{-1,0}, {1,0}, {0,-1}, {0,1}}; //Norte, Sur, Oeste, Este
                int dirGato = (int)(Math.random() * 4); //Movimiento aleatorio del gato entre N S E O
                int newRow = posGato.getRow() + coordenadas[dirGato][0];
                int newCol = posGato.getCol() + coordenadas[dirGato][1];

                if(zonaGato.esValida(newRow,newCol)){
                    Celda celda = zonaGato.getCelda(newRow,newCol);
                    if(celda.isTransitable() && !celda.isOcupada()){
                        Celda origen = zonaGato.getCelda(posGato.getRow(),posGato.getCol());
                        origen.setEntidad(null);
                        gato.moverA(new Posicion(newRow,newCol));
                        celda.setEntidad(gato);
                    }
                }
            }
        }
    }

    private boolean esZonaPermitidaGato(int idZona) {
        return idZona == 2 || idZona == 4 || idZona == 6 || idZona == 7 || idZona == 8;
    }

    private int findZonaPorPosicion(Posicion pos){
        for(int i = 0; i < 10; i++){
            Zona zona = grafo.getZona(i);
            if(zona != null && zona.esValida(pos.getRow(),pos.getCol())){
                Celda celda = zona.getCelda(pos.getRow(),pos.getCol());
                if(celda != null && celda.getEntidad() == gato){
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean usarObjeto(Objeto objeto){
        if(estadoActual != EstadoJuego.EN_CURSO){
            return false;
        }

        if(objeto.objetoGastado()){
            jugador.getInventario().removeObjeto(objeto);
            log.registrar(objeto.getNombre() + "está gastado!");
            return false;
        }

        if(objeto.getVidaBonus() > 0){
            jugador.getEstadisticas().curarVida(objeto.getVidaBonus());
            log.registrar("Has usado " +  objeto.getNombre() + ": recuperas " + objeto.getVidaBonus() + " puntos de vida!");
        }

        if(objeto.getAtaqueBonus() > 0){
            jugador.getEstadisticas().aumentarAtaque(objeto.getAtaqueBonus());
            log.registrar("Has usado " +  objeto.getNombre() + ": tu ataque aumenta  " + objeto.getAtaqueBonus() + " puntos!");
        }

        if(objeto.getDefensaBonus() > 0){
            jugador.getEstadisticas().aumentarDefensa(objeto.getDefensaBonus());
            log.registrar("Has usado " + objeto.getNombre() + ": tu defensa aumenta " + objeto.getDefensaBonus() + " puntos!");
        }

        if(objeto.getRangoBonus() > 0){
            jugador.getEstadisticas().aumentarRangoMov(objeto.getRangoBonus());
            log.registrar("Has usado " + objeto.getNombre() + ": tu rango de movimiento aumenta " + objeto.getRangoBonus() + " puntos!");
        }

        objeto.usarObjeto();
        if(objeto.objetoGastado()){
            jugador.getInventario().removeObjeto(objeto);
            log.registrar(objeto.getNombre() + " gastado!");
        }

        accionRealizada = true;
        return true;
    }

    public boolean recogerObjeto(Celda celda){
        if(estadoActual != EstadoJuego.EN_CURSO){
            return false;
        }

        if(!celda.tieneObjeto()){
            return false;
        }

        if(jugador.getInventario().inventarioLleno()){
            log.registrar("Inventario lleno: no puedes coger más objetos!");
            return false;
        }

        Objeto objeto = celda.getObjeto();
        if(jugador.getInventario().addObjeto(objeto)){
            celda.setObjeto(null);
            log.registrar("Has recogido " + objeto.getNombre() + "!");
            return true;
        }
        return false;
    }

    public boolean equiparObjeto(Objeto objeto, SlotEquipable slot){
        if(estadoActual != EstadoJuego.EN_CURSO){
            return false;
        }

        boolean equip = jugador.equipar(objeto, slot);
        if(equip){
            log.registrar("Has equipado " + objeto.getNombre() + "!");
            accionRealizada = true;
        }
        return equip;
    }

    public boolean interactNPC(){
        if(estadoActual != EstadoJuego.EN_CURSO){
            return false;
        }

        Posicion posJugador = jugador.getPosicion();
        for(int i = 0; i < zonaActual.getRows(); i++){
            for(int j = 0; j < zonaActual.getCols(); j++){
                Celda celda = zonaActual.getCelda(i, j);
                if(celda.tieneNPC()){
                    if(celda.esAdyacente(zonaActual.getCelda(posJugador.getRow(), posJugador.getCol()))){
                        NPC npc = celda.getNpc();
                        String dialogo = npc.hablar();
                        log.registrar(npc.getNombre() + ": " + dialogo);

                        if(npc.tieneInfoGato()){
                            log.registrar("Pista " + npc.getInfoGato());
                        }
                        accionRealizada = true;
                        return true;
                    }
                }
            }
        }
        log.registrar("Hablar solo da mal rollo!");
        return false;
    }

    public boolean realizarTradeo(NPC npc, Tradeo tradeo){
        if(estadoActual != EstadoJuego.EN_CURSO){
            return false;
        }
        if(!npc.comerciar()){
            log.registrar(npc.getNombre() + " no tiene ofertas");
            return false;
        }

        boolean trade = tradeo.tradear(jugador);
        if(trade){
            log.registrar("Un placer hacer negocios con usted");
            accionRealizada = true;
        }
        else{
            log.registrar("No hay trato. Vuelve con algo de interés!");
        }

        return trade;
    }

    public boolean checkVictoria(){
        if(gatoEncontrado && idZonaActual == 9){
            Celda celda = zonaActual.getCelda(jugador.getPosicion().getRow(), jugador.getPosicion().getCol());
            if(celda.getTipoCelda() == TipoCelda.SALIDA){
                estadoActual = EstadoJuego.VICTORIA;
                log.registrar("Has logrado recuperar el gato de la princesa!");
                return true;
            }
        }
        return false;
    }

    public boolean checkDerrota(){
        if(!jugador.estarVivo()){
            estadoActual = EstadoJuego.DERROTA;
            log.registrar("Has perdido: no te quedan puntos de vida!");
            return true;
        }
        if(turnoActual >= MAX_TURNOS){
            estadoActual = EstadoJuego.DERROTA;
            log.registrar("Has perdido: no has logrado encontrar el gato a tiempo!");
            return true;
        }
        return false;
    }

    public boolean findGato(){
        if(gatoEncontrado){
            return true;
        }
        if(idZonaActual == findZonaPorPosicion(gato.getPosicion())){
            Posicion posGato = gato.getPosicion();
            if(Math.abs(jugador.getPosicion().getRow() - posGato.getRow()) <= 1 &&
            Math.abs(jugador.getPosicion().getCol() - posGato.getCol()) <= 1){
                gatoEncontrado = true;
                log.registrar("Encontraste al gato! Toca llevarlo hasta la princesa. No te preocupes, caminará junto a ti");
                return true;
            }
        }
        return false;
    }

    public Lista<Celda> getCeldasAccesibles(){
        if(jugador == null || zonaActual == null){
            return new Lista<>();
        }
        Posicion posJugador = jugador.getPosicion();
        int puntosMov = jugador.getEstadisticas().getPuntosMovDisponibles();

        return zonaActual.getCeldasAccesibles(posJugador.getRow(), posJugador.getCol(), puntosMov);
    }

    public void setJugador(Jugador jugador){
        this.jugador = jugador;
    }

    public Jugador getJugador(){
        return jugador;
    }

    public void setGato(Gato gato){
        this.gato = gato;
    }

    public Gato getGato(){
        return gato;
    }

    public GrafoZonas getGrafo(){
        return grafo;
    }

    public Zona getZonaActual(){
        return zonaActual;
    }

    public void setIdZonaActual(int idZonaActual){
        this.idZonaActual = idZonaActual;
        this.zonaActual = grafo.getZona(idZonaActual);
    }

    public int getIdZonaActual(){
        return idZonaActual;
    }

    public void setEstadoActual(EstadoJuego estadoActual){
        this.estadoActual = estadoActual;
    }

    public EstadoJuego getEstadoActual(){
        return estadoActual;
    }

    public LogSistema getLog(){
        return log;
    }

    public void setTurnoActual(int turnoActual){
        this.turnoActual = turnoActual;
    }

    public int getTurnoActual(){
        return turnoActual;
    }

    public boolean isGatoEncontrado(){
        return gatoEncontrado;
    }

    public void setGatoEncontrado(boolean gatoEncontrado){
        this.gatoEncontrado = gatoEncontrado;
    }

    public boolean isAccionRealizada(){
        return accionRealizada;
    }

    public boolean isMovimientoRealizado() {
        return movimientoRealizado;
    }

    private Posicion findSpawnDisponible(Zona zona){
        for(int i = 0; i < zona.getRows(); i++){
            for(int j = 0; j < zona.getCols(); j++){
                Celda celda = zona.getCelda(i, j);
                if(celda != null && celda.isTransitable() && !celda.isOcupada()){
                    return new Posicion(i, j);
                }
            }
        }
        return new Posicion(0, 0);
    }

    private Posicion spawnAleatorio(Zona zona){
        if(zona == null){
            return null;
        }
        for(int intentos = 0; intentos < 50; intentos++){
            int row = (int)(Math.random() * zona.getRows());
            int col = (int)(Math.random() * zona.getCols());
            Celda celda = zona.getCelda(row, col);

            if(celda != null && celda.isTransitable() && !celda.isOcupada()){
                return new Posicion(row, col);
            }
        }
        return null;
    }

}