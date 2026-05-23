package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.Grafo;
import jueguito.juegopracticafinal.TADs.Lista;

public class GrafoZonas {

    private static final int MAX_ZONAS = 10;
    private Zona[] zonas;
    private boolean[][] ady;
    private int numZonas;

    public GrafoZonas() {
        this.zonas = new Zona[MAX_ZONAS];
        this.ady = new boolean[MAX_ZONAS][MAX_ZONAS];
        this.numZonas = 0;
    }

    public void addZona(Zona zona) {
        int idZona = zona.getIdZona();
        if(idZona < 0 || idZona >= MAX_ZONAS) {
            throw new IllegalArgumentException("ID de zona inválido");
        }
        this.zonas[idZona] = zona;
        this.numZonas++;
    }

    public void conectar(int idZona1, int idZona2){
        if(idZona1 < 0 || idZona1 >= MAX_ZONAS || idZona2 < 0 || idZona2 >= MAX_ZONAS) {
            throw new IllegalArgumentException("ID de zona inválido");
        }
        ady[idZona1][idZona2] = true;
        ady[idZona2][idZona1] = true;
    }

    public Zona getZona(int idZona){
        if(idZona < 0 || idZona >= MAX_ZONAS) {
            return null;
        }
        return zonas[idZona];
    }

    public Lista<Integer> getZonasVecinas(int idZona){
        Lista<Integer> vecinas = new Lista<>();
        if(idZona < 0 || idZona >= MAX_ZONAS) {
            return vecinas;
        }

        for(int i = 0; i < MAX_ZONAS; i++) {
            if(ady[idZona][i]) {
                vecinas.add(i);
            }
        }
        return vecinas;
    }

    public boolean existeCamino(int origen, int destino){
        if(origen == destino) {
            return true;
        }

        if(!zonaValida(origen) || !zonaValida(destino)){
            return false;
        }

        boolean[] vistadas = new boolean[MAX_ZONAS];
        Cola<Integer> cola = new Cola<>();
        cola.enqueue(origen);
        vistadas[origen] = true;

        while(!cola.isEmpty()) {
            int actual = cola.dequeue();
            for(int i = 0; i < MAX_ZONAS; i++) {
                if(!vistadas[i] && ady[actual][i]) {
                    if(i == destino) {
                        return true;
                    }
                    vistadas[i] = true;
                    cola.enqueue(i);
                }
            }
        }
        return false;
    }

    public boolean zonaValida(int idZona){
        return idZona >= 0 && idZona < MAX_ZONAS && zonas[idZona] != null;
    }

    public Lista<Integer> BFSZonas(int origen, int destino){
        if(origen == destino) {
            Lista<Integer> lista = new Lista<>();
            lista.add(origen);
            return lista;
        }
        if(!zonaValida(origen) || !zonaValida(destino)){
            return null;
        }

        boolean[] vistadas = new boolean[MAX_ZONAS];
        int[] raiz = new int[MAX_ZONAS];
        Cola<Integer> cola = new Cola<>();

        cola.enqueue(origen);
        vistadas[origen] = true;
        raiz[origen] = -1;

        while(!cola.isEmpty()) {
            int actual = cola.dequeue();
            for(int i = 0; i < MAX_ZONAS; i++) {
                if(!vistadas[i] && ady[actual][i]) {
                    vistadas[i] = true;
                    raiz[i] = actual;

                    if(i == destino) {
                        Lista<Integer> camino = new Lista<>();
                        int d = destino;
                        while(d != -1){
                            camino.addFirst(d);
                            d = raiz[d];
                        }
                        return camino;
                    }
                    cola.enqueue(i);
                }
            }
        }
        return null;
    }


    public static Grafo crearGrafoZonas() {
        Grafo grafo = new Grafo();

        grafo.addZona(0, "InteriorCasa", 15,15);
        grafo.addZona(1, "ExteriorCasa", 20,15);
        grafo.addZona(2, "BosqueOeste", 30,20);
        grafo.addZona(3, "Aldea", 35,25);
        grafo.addZona(4, "ExteriorTienda", 20,20);
        grafo.addZona(5, "InteriorTienda", 15,12);
        grafo.addZona(6, "Lago", 15,15);
        grafo.addZona(7, "BosqueEste", 20,20);
        grafo.addZona(8, "ExteriorCastillo", 63,42);
        grafo.addZona(9, "InteriorCastillo", 17,35);

        grafo.conectarZona(0,1);
        grafo.conectarZona(1,2);
        grafo.conectarZona(2,3);
        grafo.conectarZona(2,4);
        grafo.conectarZona(3,6);
        grafo.conectarZona(3,7);
        grafo.conectarZona(4,5);
        grafo.conectarZona(7,8);
        grafo.conectarZona(8,9);

        return grafo;
    }


}
