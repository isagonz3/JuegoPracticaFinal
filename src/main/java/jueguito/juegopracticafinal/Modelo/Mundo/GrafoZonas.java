package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.TADs.Lista;

//Representa un grafo de zonas del mundo almacenando las conexiones entre ellas mediante listas de adyacencia
public class GrafoZonas {

    //ATRIBUTOS
    private Lista<Zona> zonas;
    private Lista<Lista<Integer>> ady;


    public GrafoZonas() {
        this.zonas = new Lista<>();
        this.ady = new Lista<>();
    }


    //MÉTODOS

    //Añade una nueva zona al grafo e inicializa su lista de conexiones
    public void addZona(Zona zona) {
        zonas.add(zona);
        ady.add(new Lista<>());
    }

    //Obtiene el índice de una zona dentro de la lista a partir de su identificador
    private int indice(int idZona) {

        for (int i = 0; i < zonas.getSize(); i++) {

            if (zonas.get(i).getIdZona() == idZona) {
                return i;
            }
        }
        return -1;
    }

    //Conecta dos zonas entre sí añadiendo sus identificadores a las listas de adyacencia
    public void conectar(int id1, int id2) {

        int i1 = indice(id1);
        int i2 = indice(id2);

        if (i1 >= 0 && i2 >= 0) {

            if (!ady.get(i1).contains(id2)) {
                ady.get(i1).add(id2);
            }

            if (!ady.get(i2).contains(id1)) {
                ady.get(i2).add(id1);
            }
        }
    }


    //GETTERS Y SETTERS

    //Obtiene una zona a partir de su identificador
    public Zona getZona(int idZona) {

        int i = indice(idZona);

        return i >= 0 ? zonas.get(i) : null;
    }

    //Obtiene la lista de zonas adyacentes a una zona concreta
    public Lista<Integer> getAdyacentes(int idZona) {

        int index = indice(idZona);

        if(index >= 0) {
            return ady.get(index);
        }

        return new Lista<>();
    }

    //Obtiene el número total de zonas almacenadas en el grafo
    public int getNumZonas(){
        return zonas.getSize();
    }
}