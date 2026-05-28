package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.TADs.Lista;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapaLoaderTest {

    @Test
    void cargarZonasNoDebeDevolverNull() {
        Lista<Zona> zonas=MapaLoader.cargarZonas();

        assertNotNull(zonas);
    }

    @Test
    void cargarPuertasNoDebeDevolverNull() {
        Lista<Puerta> puertas=MapaLoader.cargarPuertas();

        assertNotNull(puertas);
    }

    @Test
    void conectarPuertasConListaNullNoDebeRomper() {
        GrafoZonas grafo=new GrafoZonas();

        assertDoesNotThrow(() ->
                MapaLoader.conectarPuertas(grafo,null)
        );
    }

    @Test
    void conectarPuertasDebeAsignarPuertaACeldas() {
        Celda[][] mapa=new Celda[2][2];
        for (int i=0; i<2;i++) {
            for (int j=0; j<2;j++) {
                mapa[i][j]=new Celda(TipoCelda.SUELO);
            }
        }

        Zona z1=new Zona(1,"Z1",mapa);
        Zona z2=new Zona(2,"Z2",mapa);

        GrafoZonas grafo = new GrafoZonas();
        grafo.addZona(z1);
        grafo.addZona(z2);

        Puerta p=new Puerta(1, 2, 0, 0, 1, 1);

        Lista<Puerta> puertas=new Lista<>();
        puertas.add(p);

        MapaLoader.conectarPuertas(grafo, puertas);

        assertEquals(TipoCelda.PUERTA, z1.getCelda(0, 0).getTipoCelda());
        assertEquals(TipoCelda.PUERTA, z2.getCelda(1, 1).getTipoCelda());
    }

    @Test
    void conectarPuertasDebeCambiarTipoCeldaAPuerta() {

        GrafoZonas grafo=new GrafoZonas();

        Celda[][] celdas1=new Celda[3][3];
        Celda[][] celdas2=new Celda[3][3];

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                celdas1[i][j]=new Celda(TipoCelda.SUELO);
                celdas2[i][j]=new Celda(TipoCelda.SUELO);
            }
        }

        Zona z1=new Zona(1,"Zona1",celdas1);
        Zona z2=new Zona(2,"Zona2",celdas2);

        grafo.addZona(z1);
        grafo.addZona(z2);

        Puerta puerta=new Puerta(1,2,0,0,1,1);

        Lista<Puerta> puertas=new Lista<>();
        puertas.add(puerta);

        MapaLoader.conectarPuertas(grafo,puertas);

        assertEquals(TipoCelda.PUERTA,z1.getCelda(0,0).getTipoCelda());
        assertEquals(TipoCelda.PUERTA,z2.getCelda(1,1).getTipoCelda());
    }

    @Test
    void conectarPuertasConPuertaNullDebeContinuar() {
        GrafoZonas grafo=new GrafoZonas();

        Lista<Puerta> puertas=new Lista<>();
        puertas.add(null);

        assertDoesNotThrow(() ->
                MapaLoader.conectarPuertas(grafo,puertas)
        );
    }

    @Test
    void conectarPuertasConZonasInvalidasNoDebeRomper() {
        GrafoZonas grafo=new GrafoZonas();

        Puerta puerta=new Puerta(1,2,10,10,20,20);

        Lista<Puerta> puertas=new Lista<>();
        puertas.add(puerta);

        assertDoesNotThrow(() ->
                MapaLoader.conectarPuertas(grafo,puertas)
        );
    }
}