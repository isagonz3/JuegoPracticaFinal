package jueguito.juegopracticafinal.TADs;


public class Grafo {

    private Nodo[] nodos;
    private int numNodos;
    private static final int MAX_ZONAS = 11;


    public Grafo() {
        this.nodos = new Nodo[MAX_ZONAS];
        this.numNodos = 0;
    }

    public void addZona(int id, String nombre, int rows, int cols) {
        if(id < 0 || id >= MAX_ZONAS) {
            throw new IllegalArgumentException("Zona no existe");
        }
        nodos[id] = new Nodo(id, nombre, rows, cols);
        numNodos++;
    }

    public void conectarZona(int zona1, int zona2) {
        if(nodos[zona1] == null || nodos[zona2] == null) {
            throw new IllegalArgumentException("Zona no existe");
        }
        nodos[zona1].addAdyacente(zona2);
        nodos[zona2].addAdyacente(zona1);
    }

    public Lista<Integer> getZonasConectadas(int zona) {
        if(nodos[zona] == null) {
            return new Lista<>();
        }
        return nodos[zona].getAdyacentes();
    }

    public Lista<Integer> BFS(int origen, int destino) {
        if(nodos[origen] == null || nodos[destino] == null) {
            return null;
        }

        Cola<Integer> cola = new Cola<>();
        boolean[] visitados = new boolean[MAX_ZONAS];
        int[] raiz = new int[MAX_ZONAS];

        cola.enqueue(origen);
        visitados[origen] = true;
        raiz[origen] = -1;

        while(!cola.isEmpty()) {
            int actual = cola.dequeue();
            if(actual == destino) {
                return buildCamino(raiz, destino);
            }

            Lista<Integer> adyacentes = nodos[actual].getAdyacentes();
            for(int i = 0; i < adyacentes.getSize(); i++){
                int vecino = adyacentes.get(i);
                if(!visitados[vecino]) {
                    visitados[vecino] = true;
                    raiz[vecino] = actual;
                    cola.enqueue(vecino);
                }
            }
        }
        return null;
    }

    private Lista<Integer> buildCamino(int[] raiz, int destino) {
        Lista<Integer> camino = new Lista<>();
        int actual = destino;
        while(actual != -1) {
            camino.addFirst(actual);
            actual = raiz[actual];
        }
        return camino;
    }

}
