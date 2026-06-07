package jueguito.juegopracticafinal.TADs;

import jueguito.juegopracticafinal.Modelo.Excepciones.ExcepcionTADs;

public class Grafo<N,A extends InterfazArista<N>> implements InterfazGrafo<N,A> {

    private final Lista<N> nodos;
    private final Lista<Lista<A>> adyacentes;

    public Grafo() {
        this.nodos = new Lista<>();
        this.adyacentes = new Lista<>();
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

        Lista<A> adyacentesO = adyacentes.get(iOrigen);
        for(int i = 0; i < adyacentesO.getSize(); i++){
            if(adyacentesO.get(i).getDestino().equals(arista.getDestino())){
                return false;
            }
        }

        adyacentesO.add(arista);
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
            for(int j = 0; j < ady.getSize(); j++){
                lista.add(ady.get(j));
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
        for(int i = 0; i < ady.getSize(); i++){
            vecinos.add(ady.get(i).getDestino());
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
        Lista<A> ady = adyacentes.get(index);
        for(int i = 0; i < ady.getSize(); i++){
            lista.add(ady.get(i));
        }
        return lista;
    }

    @Override
    public A getArista(N origen, N destino) {
        int index = getIndex(origen);
        if(index < 0){
            return null;
        }
        Lista<A> ady = adyacentes.get(index);
        for(int i = 0; i < ady.getSize(); i++){
            A a = ady.get(i);
            if(a.getDestino().equals(destino)){
                return a;
            }
        }
        return null;
    }

    @Override
    public int getIndex(N nodo) {
        for(int i = 0; i < nodos.getSize(); i++){
            if(nodos.get(i).equals(nodo)){
                return i;
            }
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
        if(origen == null || getIndex(origen) < 0){
            return new Lista<>();
        }

        Lista<N> visitados = new Lista<>();
        Cola<N> cola = new Cola<>();
        visitados.add(origen);
        cola.enqueue(origen);

        while(!cola.isEmpty()){
            N actual = cola.dequeue();
            Lista<N> ady = getAdyacentes(actual);
            for(int i = 0; i < ady.getSize(); i++){
                N vecino = ady.get(i);
                if(!visitados.contains(vecino)){
                    visitados.add(vecino);
                    cola.enqueue(vecino);
                }
            }
        }
        return visitados;
    }

    public Lista<N> dfs(N origen) {
        if (origen == null || getIndex(origen) < 0)
            return new Lista<>();

        Lista<N> visitados = new Lista<>();
        Pila<N> pila = new Pila<>();
        visitados.add(origen);
        pila.push(origen);

        while (!pila.isEmpty()) {
            N actual = pila.pop();
            Lista<N> ady = getAdyacentes(actual);
            for (int i = 0; i < ady.getSize(); i++) {
                N v = ady.get(i);
                if (!visitados.contains(v)) {
                    visitados.add(v);
                    pila.push(v);
                }
            }
        }
        return visitados;
    }

    public Lista<N> bfsCamino(N origen, N destino) {
        if (origen == null || destino == null) return new Lista<>();
        if (origen.equals(destino)) {
            Lista<N> r = new Lista<>();
            r.add(origen);
            return r;
        }

        int n = size();
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) prev[i] = -1;

        int iOrigen = getIndex(origen);
        int iDestino = getIndex(destino);
        if (iOrigen < 0 || iDestino < 0) return new Lista<>();

        boolean[] visitado = new boolean[n];
        Cola<Integer> cola = new Cola<>();
        visitado[iOrigen] = true;
        cola.enqueue(iOrigen);

        while (!cola.isEmpty()) {
            int idx = cola.dequeue();
            if (getNodos().get(idx).equals(destino)) break;

            Lista<N> ady = getAdyacentes(getNodos().get(idx));
            for (int i = 0; i < ady.getSize(); i++) {
                int iv = getIndex(ady.get(i));
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
            camino.addFirst(getNodos().get(idx));
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
        Lista<N> ady = getAdyacentes(getNodos().get(idx));
        for (int i = 0; i < ady.getSize(); i++) {
            int iv = getIndex(ady.get(i));
            if (estado[iv] == 1) return true;
            if (estado[iv] == 0 && dfsCiclo(iv, estado)) return true;
        }
        estado[idx] = 2;
        return false;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Grafo: \n");
        for(int i = 0; i < nodos.getSize(); i++){
            builder.append(" ").append(nodos.get(i)).append(" -> ");
            Lista<A> ady = adyacentes.get(i);
            for(int j = 0; j < ady.getSize(); j++){
                builder.append(ady.get(j).getDestino());
                if(j < ady.getSize() - 1){
                    builder.append(", ");
                }
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
