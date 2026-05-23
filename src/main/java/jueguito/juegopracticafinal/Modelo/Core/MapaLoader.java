package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.TADs.GsonUtil;
import jueguito.juegopracticafinal.TADs.Lista;
import jueguito.juegopracticafinal.TADs.ListaIndex;

public class MapaLoader {
    private static final char PARED = '#';
    private static final char SUELO = '_';
    private static final char PUERTA = 'P';
    private static final char INTERACTUABLE = '?';
    private static final char AGUA = '~';
    private static final char VACIO = '0';


    public static Lista<Zona> cargarZonas(){
        ZonaData[] datos= GsonUtil.cargarArray("src/main/resources/mapasJSON/habitaciones.json", ZonaData[].class);
        Lista<Zona> zonas=new Lista<>();
        if(datos!=null){
            for(ZonaData d:datos){
                zonas.add(crearDesdeTexto(d.mapa));
            }
        }
        return zonas;
    }

    public static ListaIndex<Puerta> cargarPuertas(){
        PuertaData[] datos = GsonUtil.cargarArray("src/DATOS/puertas.json",PuertaData[].class);
        ListaIndex<Puerta> puertas = new ListaIndex<>();
        if(datos!=null){
            for(PuertaData d:datos){
                puertas.addLast(new Puerta(d.zonaOrigen,d.zonaDestino,d.xOrigen,d.yOrigen,d.xDestino,d.yDestino));
            }
        }
        return puertas;
    }

    private static Zona crearDesdeTexto(String[] mapa){
        Celda[][] celdas=new Celda[mapa.length][];
        for(int y=0; y<mapa.length; y++){
            celdas[y]=new Celda[mapa[y].length()];
            for(int x=0;x<mapa[y].length();x++){
                char simbolo=mapa[y].charAt(x);
                celdas[y][x]=switch(simbolo){
                    case '#'->new Celda(TipoCelda.PARED);
                    case '_'->new Celda(TipoCelda.SUELO);
                    case 'P'->new Celda(TipoCelda.PUERTA);
                    case '?'->new Celda(TipoCelda.INTERACTUABLE);
                    case '~'->new Celda(TipoCelda.AGUA);
                    case '0'->new Celda(TipoCelda.VACIO);
                    default -> throw new IllegalArgumentException("Símbolo desconocido: "+simbolo);
                };
            }
        }
        return new Zona(celdas);
    }
}
