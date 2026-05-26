package jueguito.juegopracticafinal.Modelo.Entidades;

public class Estadisticas {
    private int vidaMax;
    private int vidaActual;
    private int ataqueBase;
    private int defensaBase;
    private int rangoMov;
    private int puntosMovDisponibles;


    public Estadisticas(int vidaMax, int ataqueBase, int defensaBase, int rangoMov) {
        this.vidaMax = vidaMax;
        this.vidaActual = vidaMax;
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.rangoMov = rangoMov;
        this.puntosMovDisponibles = rangoMov;
    }

    public int getVidaMax() {
        return vidaMax;
    }

    public void setVidaMax(int vidaMax) {
        this.vidaMax = vidaMax;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = vidaActual;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public void setAtaqueBase(int ataqueBase) {
        this.ataqueBase = ataqueBase;
    }

    public int getDefensaBase() {
        return defensaBase;
    }

    public void setDefensaBase(int defensaBase) {
        this.defensaBase = defensaBase;
    }

    public int getRangoMov() {
        return rangoMov;
    }

    public void setRangoMov(int rangoMov) {
        this.rangoMov = rangoMov;
    }

    public int getPuntosMovDisponibles() {
        return puntosMovDisponibles;
    }

    public void setPuntosMovDisponibles(int puntosMovDisponibles) {
        this.puntosMovDisponibles = puntosMovDisponibles;
    }

    public boolean estaVivo(){
        return vidaActual > 0;
    }

    public void resetMovimiento() {
        this.puntosMovDisponibles = this.rangoMov;
    }

    public boolean usarMovimiento(int puntos){
        if(puntos <= 0 || puntos > puntosMovDisponibles){
            return false;
        }
        this.puntosMovDisponibles -= puntos;
        return true;
    }

    public boolean puedeMoverse(){
        return this.puntosMovDisponibles > 0;
    }

    public Estadisticas clonar(){
        Estadisticas estadisticas = new Estadisticas(vidaMax, ataqueBase, defensaBase, rangoMov);
        estadisticas.vidaActual = this.vidaActual;
        estadisticas.puntosMovDisponibles = this.puntosMovDisponibles;
        return estadisticas;
    }

    public void curarVida(int bonus){
        this.vidaActual = Math.min(vidaMax, this.vidaActual + bonus);
    }

    public void aumentarAtaque(int bonus){
        this.ataqueBase += bonus;
    }

    public void aumentarDefensa(int bonus){
        this.defensaBase += bonus;
    }

    public void aumentarRangoMov(int bonus){
        this.rangoMov += bonus;
    }

    private void aplicarAtaque(int ataque, int reduccionDefensaExtra) {

        int defensaTotal = this.defensaBase + reduccionDefensaExtra;

        int dano = ataque - defensaTotal;

        if (dano < 0) {
            dano = 0;
        }

        this.vidaActual = Math.max(0, this.vidaActual - dano);
    }

    // 🔥 NUEVO: Resta el daño real directamente sin volver a aplicar la defensa
    public void restarVidaDirecta(int danoReal) {
        this.vidaActual = Math.max(0, this.vidaActual - danoReal);
    }

    @Override
    public String toString() {
        return "VIDA: " + vidaActual + "/" + vidaMax +
                "ATAQUE: " +  ataqueBase +
                "DEFENSA: " + defensaBase +
                "MOVIMIENTO: " + puntosMovDisponibles + "/" + rangoMov;
    }
}
