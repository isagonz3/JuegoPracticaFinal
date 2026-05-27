package jueguito.juegopracticafinal.Modelo.Mundo;

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
}