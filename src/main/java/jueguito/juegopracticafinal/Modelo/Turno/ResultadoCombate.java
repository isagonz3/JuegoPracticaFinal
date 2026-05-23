package jueguito.juegopracticafinal.Modelo.Turno;

public class ResultadoCombate {
    private final int hit;
    private final int vidaRestante;
    private final boolean enemigoKO;
    private final String mensaje;

    public ResultadoCombate(int hit, int vidaRestante, boolean enemigoKO, String mensaje) {
        this.hit = hit;
        this.vidaRestante = vidaRestante;
        this.enemigoKO = enemigoKO;
        this.mensaje = mensaje;
    }

    public int getHit() { return hit; }
    public int getVidaRestante() { return vidaRestante; }
    public boolean isEnemigoKO() { return enemigoKO; }
    public String getMensaje() { return mensaje; }
}