package jueguito.juegopracticafinal.TADs;

import jueguito.juegopracticafinal.Modelo.Excepciones.ExcepcionTADs;

public class Grafo<N,A extends InterfazArista<N>> implements InterfazGrafo<N,A> {

    private final Lista<N> nodos;
    private final Lista<Lista<A>> adyacentes;
    private final Lista<Lista<Integer>> adyacentesIndex;

    public Grafo() {
        this.nodos = new Lista<>();
        this.adyacentes = new Lista<>();
        this.adyacentesIndex = new Lista<>();
    }


    @Override
    public void addNodo(N nodo) {
        if(nodo == null){
            throw new ExcepcionTADs("El nodo no puede tener valor nulo");
        }
        if(getIndex(nodo) >= 0){
            return;
        }
        nodos.add(nodo);
        adyacentes.add(new Lista<>());
        adyacentesIndex.add(new Lista<>());
    }

    @Override
    public boolean addArista(A arista) {
        if(arista == null){
            throw new ExcepcionTADs("La arista no puede tener valor nulo");
        }

        int iOrigen = getIndex(arista.getOrigen());
        int iDestino = getIndex(arista.getDestino());

        if(iOrigen < 0 || iDestino < 0){
            throw new ExcepcionTADs("Nodo origen y nodo destino deben estar en el grafo");
        }

        Lista<A> adyO = adyacentes.get(iOrigen);
        InterfazIterador<A> it = adyO.iterador();
        while (it.hasNext()) {
            if (it.next().getDestino().equals(arista.getDestino()))
                return false;
        }

        adyO.add(arista);
        adyacentesIndex.get(iOrigen).add(iDestino);
        return true;
    }

    @Override
    public boolean existeArista(N origen, N destino) {
        return getArista(origen, destino) != null;
    }

    @Override
    public Lista<N> getNodos() {
        Lista<N> lista = new Lista<>();
        lista.addAll(nodos);
        return lista;
    }

    @Override
    public Lista<A> getAristas() {
        Lista<A> lista = new Lista<>();
        for(int i = 0; i < adyacentes.getSize(); i++){
            Lista<A> ady = adyacentes.get(i);
            InterfazIterador<A> it = ady.iterador();
            while (it.hasNext()) {
                lista.add(it.next());
            }
        }
        return lista;
    }

    @Override
    public Lista<N> getAdyacentes(N nodo) {
        int index = getIndex(nodo);
        if(index < 0){
            return new Lista<>();
        }

        Lista<N> vecinos = new Lista<>();
        Lista<A> ady = adyacentes.get(index);
        InterfazIterador<A> it = ady.iterador();
        while (it.hasNext()) {
            vecinos.add(it.next().getDestino());
        }
        return vecinos;
    }

    @Override
    public Lista<A> getAristasDesde(N nodo) {
        int index = getIndex(nodo);
        if(index < 0){
            return new Lista<>();
        }
        Lista<A> lista = new Lista<>();
        lista.addAll(adyacentes.get(index));
        return lista;
    }

    @Override
    public A getArista(N origen, N destino) {
        int index = getIndex(origen);
        if(index < 0){
            return null;
        }
        Lista<A> ady = adyacentes.get(index);
        InterfazIterador<A> it = ady.iterador();
        while (it.hasNext()) {
            A a = it.next();
            if (a.getDestino().equals(destino)) return a;
        }
        return null;
    }

    @Override
    public int getIndex(N nodo) {
        InterfazIterador<N> it = nodos.iterador();
        for (int i = 0; it.hasNext(); i++) {
            if (it.next().equals(nodo)) return i;
        }
        return -1;
    }

    @Override
    public int size() {
        return nodos.getSize();
    }

    @Override
    public boolean isEmpty() {
        return nodos.isEmpty();
    }

    public Lista<N> bfs(N origen){
        int iOrigen = getIndex(origen);
        if (iOrigen < 0) return new Lista<>();

        Lista<N> visitados = new Lista<>();
        Cola<Integer> cola = new Cola<>();
        boolean[] visitado = new boolean[size()];
        visitado[iOrigen] = true;
        cola.enqueue(iOrigen);

        while(!cola.isEmpty()){
            int idx = cola.dequeue();
            visitados.add(nodos.get(idx));
            Lista<Integer> idxVecinos = adyacentesIndex.get(idx);
            InterfazIterador<Integer> it = idxVecinos.iterador();
            while (it.hasNext()) {
                int vec = it.next();
                if(!visitado[vec]){
                    visitado[vec] = true;
                    cola.enqueue(vec);
                }
            }
        }
        return visitados;
    }

    public Lista<N> dfs(N origen) {
        int iOrigen = getIndex(origen);
        if (iOrigen < 0) return new Lista<>();

        Lista<N> visitados = new Lista<>();
        Pila<Integer> pila = new Pila<>();
        boolean[] visitado = new boolean[size()];
        visitado[iOrigen] = true;
        pila.push(iOrigen);

        while (!pila.isEmpty()) {
            int idx = pila.pop();
            visitados.add(nodos.get(idx));
            Lista<Integer> idxVecinos = adyacentesIndex.get(idx);
            InterfazIterador<Integer> it = idxVecinos.iterador();
            while (it.hasNext()) {
                int vec = it.next();
                if(!visitado[vec]){
                    visitado[vec] = true;
                    pila.push(vec);
                }
            }
        }
        return visitados;
    }

    public Lista<N> bfsCamino(N origen, N destino) {
        if (origen == null || destino == null) return new Lista<>();
        int iOrigen = getIndex(origen);
        int iDestino = getIndex(destino);
        if (iOrigen < 0 || iDestino < 0) return new Lista<>();
        if (iOrigen == iDestino) {
            Lista<N> r = new Lista<>();
            r.add(origen);
            return r;
        }

        int n = size();
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) prev[i] = -1;

        boolean[] visitado = new boolean[n];
        Cola<Integer> cola = new Cola<>();
        visitado[iOrigen] = true;
        cola.enqueue(iOrigen);

        while (!cola.isEmpty()) {
            int idx = cola.dequeue();
            if (idx == iDestino) break;

            Lista<Integer> idxVecinos = adyacentesIndex.get(idx);
            InterfazIterador<Integer> it = idxVecinos.iterador();
            while (it.hasNext()) {
                int iv = it.next();
                if (!visitado[iv]) {
                    visitado[iv] = true;
                    prev[iv] = idx;
                    cola.enqueue(iv);
                }
            }
        }

        if (prev[iDestino] == -1) return new Lista<>();

        Lista<N> camino = new Lista<>();
        int idx = iDestino;
        while (idx != -1) {
            camino.addFirst(nodos.get(idx));
            idx = prev[idx];
        }
        return camino;
    }

    public boolean contieneCiclo() {
        int n = size();
        if (n == 0) return false;
        int[] estado = new int[n];
        for (int i = 0; i < n; i++) {
            if (estado[i] == 0 && dfsCiclo(i, estado))
                return true;
        }
        return false;
    }

    private boolean dfsCiclo(int idx, int[] estado) {
        estado[idx] = 1;
        Lista<Integer> idxVecinos = adyacentesIndex.get(idx);
        InterfazIterador<Integer> it = idxVecinos.iterador();
        while (it.hasNext()) {
            int iv = it.next();
            if (estado[iv] == 1) return true;
            if (estado[iv] == 0 && dfsCiclo(iv, estado)) return true;
        }
        estado[idx] = 2;
        return false;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Grafo: \n");
        for(int i = 0; i < nodos.getSize(); i++){
            sb.append(" ").append(nodos.get(i)).append(" -> ");
            Lista<A> aristas = adyacentes.get(i);
            InterfazIterador<A> it = aristas.iterador();
            while (it.hasNext()) {
                sb.append(it.next().getDestino());
                if (it.hasNext()) sb.append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
