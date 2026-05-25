package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.Lista;

public class GrafoZonas {
    private Lista<Zona> zonas;
    private Lista<Lista<Integer>> ady;

    public GrafoZonas() {
        this.zonas = new Lista<>();
        this.ady = new Lista<>();
    }

    public void addZona(Zona zona) {
        zonas.add(zona);
        ady.add(new Lista<>());
    }

    private int indice(int idZona) {
        for (int i = 0; i < zonas.getSize(); i++)
            if (zonas.get(i).getIdZona() == idZona) return i;
        return -1;
    }

    public Zona getZona(int idZona) {
        int i = indice(idZona);
        return i >= 0 ? zonas.get(i) : null;
    }

    public void conectar(int id1, int id2) {
        int i1 = indice(id1), i2 = indice(id2);
        if (i1 >= 0 && i2 >= 0) {
            if (!ady.get(i1).contains(id2)) ady.get(i1).add(id2);
            if (!ady.get(i2).contains(id1)) ady.get(i2).add(id1);
        }
    }

    public boolean zonaValida(int idZona) { return indice(idZona) >= 0; }

    public Lista<Integer> getZonasVecinas(int idZona) {
        int i = indice(idZona);
        return i >= 0 ? ady.get(i) : new Lista<>();
    }

    public boolean existeCamino(int origen, int destino) {
        return BFSZonas(origen, destino) != null;
    }

    public Lista<Integer> BFSZonas(int origen, int destino) {
        if (origen == destino) { Lista<Integer> r = new Lista<>(); r.add(origen); return r; }
        int iO = indice(origen), iD = indice(destino);
        if (iO < 0 || iD < 0) return null;

        int n = zonas.getSize();
        boolean[] visit = new boolean[n];
        int[] padreId = new int[n];
        for (int i = 0; i < n; i++) padreId[i] = -1;

        Cola<Integer> cola = new Cola<>();
        cola.enqueue(origen);
        visit[iO] = true;

        while (!cola.isEmpty()) {
            int actual = cola.dequeue();
            int iAct = indice(actual);
            if (actual == destino) {
                Lista<Integer> camino = new Lista<>();
                int cur = destino;
                while (cur != -1) {
                    camino.addFirst(cur);
                    cur = padreId[indice(cur)];
                }
                return camino;
            }
            Lista<Integer> vecinos = ady.get(iAct);
            for (int j = 0; j < vecinos.getSize(); j++) {
                int v = vecinos.get(j);
                int iV = indice(v);
                if (!visit[iV]) {
                    visit[iV] = true;
                    padreId[iV] = actual;
                    cola.enqueue(v);
                }
            }
        }
        return null;
    }
}