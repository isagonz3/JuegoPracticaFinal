package jueguito.juegopracticafinal.TADs;

import jueguito.juegopracticafinal.Modelo.Excepciones.ExcepcionTADs;

public class Arista<N> implements InterfazArista<N> {

    private final N origen;
    private final N destino;

    public Arista(N origen, N destino) {
        if(origen==null || destino==null){
            throw new ExcepcionTADs("Origen y destino no pueden tener valor nulo");
        }
        this.origen = origen;
        this.destino = destino;
    }

    @Override
    public N getOrigen() {
        return origen;
    }

    @Override
    public N getDestino() {
        return destino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arista<?> that = (Arista<?>) o;
        if (!origen.equals(that.origen)) {
            return false;
        }
        return destino.equals(that.destino);
    }

    @Override
    public String toString() {
        return origen + " -> " + destino;
    }
}
