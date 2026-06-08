package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Enemigo;
import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartidaObjetosYCombateTest{

    private Partida partidaPrueba() {
        GrafoZonas grafo=new jueguito.juegopracticafinal.Modelo.Mundo.GrafoZonas();
        grafo.addZona(zonaPrueba(0));
        Partida partida=new Partida(grafo);
        partida.iniciar();
        partida.setEstadoActual(EstadoJuego.EN_CURSO);
        return partida;
    }


    private Zona zonaPrueba(int id){
        Celda[][] celdas=new Celda[3][3];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                celdas[i][j]=new Celda(TipoCelda.SUELO);
            }
        }
        return new Zona(id, "Z"+id, celdas);
    }


    @Test
    void calcularHitSiempreNoNegativoInclusoConDefensaAlta(){
        int hit=PartidaObjetosYCombate.calcularHit(1,999);
        assertTrue(hit>=0);
    }

    @Test
    void atacarEnemigoMuertoNoRompeYDevuelveResultado(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        Enemigo e=new Enemigo("Orco",new Estadisticas(0,5,2,1));

        assertNotNull(poc.atacarEnemigo(e));
    }

    @Test
    void atacarDireccionSinEntidadDevuelveFalse(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        assertFalse(poc.atacarDireccion(1,1));
    }

    @Test
    void atacarDireccionFueraMapaDevuelveFalse(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        assertFalse(poc.atacarDireccion(999,999));
    }

    @Test
    void usarObjetoNullDevuelveFalseSinRomper(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        assertFalse(poc.usarObjeto(null));
    }

    @Test
    void recogerObjetoCeldaVaciaDevuelveFalse(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        Celda c=new Celda(TipoCelda.SUELO);

        assertFalse(poc.recogerObjeto(c));
    }

    @Test
    void equiparObjetoNullSlotDevuelveFalse(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        assertFalse(poc.equiparObjeto(null,null));
    }

    @Test
    void interactNpcSinNpcDevuelveNull(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        assertNull(poc.interactNPC());
    }

    @Test
    void atacarJugadorConEnemigosLeHaceDaño(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        Zona z=p.getZonaActual();

        Enemigo e=new Enemigo("Orco",new Estadisticas(10,5,1,1));
        z.getCelda(0,1).setEntidad(e);

        int vida=p.getJugador().getEstadisticas().getVidaActual();

        poc.atacarJugador();

        assertTrue(p.getJugador().getEstadisticas().getVidaActual()<=vida);
    }

    @Test
    void usarObjetoNoEnInventarioDevuelveFalse(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        Objeto o=new Objeto("Pocion",TipoObjeto.USABLE,0,0,5,0,1,"cura");

        assertFalse(poc.usarObjeto(o));
    }

    @Test
    void recogerObjetoLoEliminaDeCelda(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        Celda c=new Celda(TipoCelda.SUELO);

        Objeto o=new Objeto("Pocion",TipoObjeto.USABLE,0,0,5,0,1,"cura");
        c.setObjeto(o);

        boolean r=poc.recogerObjeto(c);

        assertTrue(r);
        assertFalse(c.tieneObjeto());
    }

    @Test
    void poblarZonaProhibidaNoRompe(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        Zona z=zonaPrueba(0);

        assertDoesNotThrow(() -> poc.poblarZona(z));
    }

    @Test
    void ponerObjetosZonaNoRompe(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        Zona z=zonaPrueba(0);

        assertDoesNotThrow(() -> poc.ponerObjetos(z));
    }

    @Test
    void interactuarGatoSinGatoDevuelveFalse(){
        Partida p=partidaPrueba();
        PartidaObjetosYCombate poc=new PartidaObjetosYCombate(p);

        assertFalse(poc.interactuarGato());
    }

    @Test
    void usarObjetoEnInventarioDevuelveTrueYConsume() {
        Partida p = partidaPrueba();
        PartidaObjetosYCombate poc = new PartidaObjetosYCombate(p);

        Objeto pocion = new Objeto("Pocion", TipoObjeto.USABLE, 0, 0, 5, 0, 1, "cura");
        p.getJugador().getInventario().addObjeto(pocion);

        boolean resultado = poc.usarObjeto(pocion);

        assertTrue(resultado);
        assertFalse(p.getJugador().getInventario().contiene(pocion));
        assertTrue(p.getJugador().getInventario().getObjetosUsados().contains(pocion));
    }

    @Test
    void equiparObjetoValidoDevuelveTrueYLoEquipa() {
        Partida p = partidaPrueba();
        PartidaObjetosYCombate poc = new PartidaObjetosYCombate(p);

        Objeto espada = new Objeto("Espada", TipoObjeto.EQUIPABLE, 5, 2, 0, 0, 3, null);
        espada.setSlot(SlotEquipable.ARMA);
        p.getJugador().getInventario().addObjeto(espada);

        boolean resultado = poc.equiparObjeto(espada, SlotEquipable.ARMA);

        assertTrue(resultado);
        assertFalse(p.getJugador().getInventario().contiene(espada));
        assertTrue(p.getJugador().getInventario().getObjetosEquipados().contains(espada));
    }

    @Test
    void ponerObjetosEnZonaValidaColocaAlMenosUnaPocion() {
        Partida p = partidaPrueba();
        PartidaObjetosYCombate poc = new PartidaObjetosYCombate(p);

        Zona z = zonaPrueba(1);  // zona 1 NO está en la skip list
        poc.ponerObjetos(z);

        boolean hayPocion = false;
        for (int i = 0; i < z.getRows() && !hayPocion; i++) {
            for (int j = 0; j < z.getCols() && !hayPocion; j++) {
                Celda c = z.getCelda(i, j);
                if (c != null && c.tieneObjeto()
                        && c.getObjeto().getNombre().toLowerCase().startsWith("pocion")) {
                    hayPocion = true;
                }
            }
        }
        assertTrue(hayPocion, "Debería haber al menos 1 poción en zona permitida");
    }

    @Test
    void ponerObjetosEnZonaExcluidaNoColocaPociones() {
        Partida p = partidaPrueba();
        PartidaObjetosYCombate poc = new PartidaObjetosYCombate(p);

        Zona z = zonaPrueba(0);
        poc.ponerObjetos(z);

        for (int i = 0; i < z.getRows(); i++) {
            for (int j = 0; j < z.getCols(); j++) {
                Celda c = z.getCelda(i, j);
                assertNull(c.getObjeto(), "Zona 0 no debería tener objetos");
            }
        }
    }
}