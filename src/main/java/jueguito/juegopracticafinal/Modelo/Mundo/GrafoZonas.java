package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.TADs.Arista;
import jueguito.juegopracticafinal.TADs.Grafo;
import jueguito.juegopracticafinal.TADs.InterfazIterador;
import jueguito.juegopracticafinal.TADs.Lista;

//Representa un grafo de zonas del mundo almacenando las conexiones entre ellas mediante listas de adyacencia
public class GrafoZonas {

    //ATRIBUTOS
    private final Grafo<Integer, Arista<Integer>> grafo;
    private final Lista<Zona> zonas;


    public GrafoZonas() {
        this.grafo = new Grafo<>();
        this.zonas = new Lista<>();
    }


    //MÉTODOS

    //Añade una nueva zona al grafo e inicializa su lista de conexiones
    public void addZona(Zona zona) {
        if(zona == null) return;
        if (getZona(zona.getIdZona()) != null) return;
        grafo.addNodo(zona.getIdZona());
        zonas.add(zona);
    }

    //Conecta dos zonas entre sí añadiendo sus identificadores a las listas de adyacencia
    public void conectar(int id1, int id2) {
        if (grafo.getIndex(id1) < 0 || grafo.getIndex(id2) < 0) return;
        grafo.addArista(new Arista<>(id1, id2));
        grafo.addArista(new Arista<>(id2, id1));
    }


    //GETTERS Y SETTERS

    //Obtiene una zona a partir de su identificador
    public Zona getZona(int idZona) {
        InterfazIterador<Zona> it = zonas.iterador();
        while (it.hasNext()) {
            Zona z = it.next();
            if (z.getIdZona() == idZona) return z;
        }
        return null;
    }

    //Obtiene la lista de zonas adyacentes a una zona concreta
    public Lista<Integer> getAdyacentes(int idZona) {
        return grafo.getAdyacentes(idZona);
    }

    //Obtiene el número total de zonas almacenadas en el grafo
    public int getNumZonas(){
        return grafo.size();
    }

    public Lista<Integer> bfs(int origen){
        return grafo.bfs(origen);
    }

    public Lista<Integer> bfsCamino(int origen, int destino){
        return grafo.bfsCamino(origen, destino);
    }

    public Lista<Integer> dfs(int origen) {
        return grafo.dfs(origen);
    }

    public boolean contieneCiclo() {
        return grafo.contieneCiclo();
    }

    public boolean existeConexion(int id1, int id2) {
        return grafo.existeArista(id1, id2);
    }

    public boolean isEmpty() {
        return grafo.isEmpty();
    }

    @Override
    public String toString() {
        return grafo.toString();
    }
}