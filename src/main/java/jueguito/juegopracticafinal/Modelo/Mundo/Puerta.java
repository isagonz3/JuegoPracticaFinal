package jueguito.juegopracticafinal.Modelo.Mundo;

public class Puerta implements Comparable<Puerta> {
    private int xOrigen;
    private int yOrigen;
    private int xDestino;
    private int yDestino;
    private int zonaOrigen;
    private int zonaDestino;

    public Puerta(int zonaOrigen,int zonaDestino,int xOrigen, int yOrigen, int xDestino, int yDestino) {
        this.xOrigen = xOrigen;
        this.yOrigen = yOrigen;
        this.xDestino = xDestino;
        this.yDestino = yDestino;
        this.zonaOrigen = zonaOrigen;
        this.zonaDestino = zonaDestino;
    }

    public Puerta() {

    }

    public int getXOrigen() {
        return xOrigen;
    }

    public int getYOrigen() {
        return yOrigen;
    }

    public int getXDestino() {
        return xDestino;
    }

    public int getYDestino() {
        return yDestino;
    }

    public int getZonaOrigen() {
        return zonaOrigen;
    }

    public int getZonaDestino() {
        return zonaDestino;
    }

    @Override
    public int compareTo(Puerta o) {
        int cmp1 = Integer.compare(this.zonaOrigen, o.zonaOrigen);
        if(cmp1!=0) return cmp1;
        int cmp2 = Integer.compare(this.zonaDestino, o.zonaDestino);
        if(cmp2!=0) return cmp2;
        int cmp3 = Integer.compare(this.xOrigen, o.xOrigen);
        if(cmp3!=0) return cmp3;
        return Integer.compare(this.yOrigen, o.yOrigen);
    }
}