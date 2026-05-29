package jueguito.juegopracticafinal.Modelo.Entidades;

public class Estadisticas {

    //ATRIBUTOS

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


    //MÉTODOS

    //Indica si la entidad sigue viva comprobando si su vida actual es mayor que cero
    public boolean estaVivo(){
        return vidaActual > 0;
    }

    //Reduce los puntos de movimiento disponibles si la cantidad es válida
    public boolean usarMovimiento(int puntos){
        if(puntos <= 0 || puntos > puntosMovDisponibles){
            return false;
        }

        this.puntosMovDisponibles -= puntos;
        return true;
    }

    //Recupera vida sin superar el máximo permitido
    public void curarVida(int bonus){
        this.vidaActual = Math.min(vidaMax, this.vidaActual + bonus);
    }

    //Aumenta el valor base de ataque de la entidad
    public void aumentarAtaque(int bonus){
        this.ataqueBase += bonus;
    }

    //Aumenta el valor base de defensa de la entidad
    public void aumentarDefensa(int bonus){
        this.defensaBase += bonus;
    }

    //Aumenta el rango de movimiento disponible de la entidad
    public void aumentarRangoMov(int bonus){
        this.rangoMov += bonus;
    }

    //Gestiona la recepción de un ataque aplicando el daño a las estadísticas de la entidad
    public void recibirAtaque(int puntos){

        aplicarAtaque(puntos, 0);
    }

    //Aplica el cálculo de daño considerando el ataque recibido y la defensa total de la entidad
    private void aplicarAtaque(int ataque, int reduccionDefensaExtra) {

        int defensaTotal = this.defensaBase + reduccionDefensaExtra;

        int dano = ataque - defensaTotal;

        if (dano < 0) {
            dano = 0;
        }

        this.vidaActual = Math.max(0, this.vidaActual - dano);
    }

    //Reduce directamente la vida de la entidad sin aplicar defensa
    public void restarVidaDirecta(int danoReal) {
        this.vidaActual = Math.max(0, this.vidaActual - danoReal);
    }

    //Devuelve una representación en texto de las estadísticas de la entidad
    @Override
    public String toString() {
        return "VIDA: " + vidaActual + "/" + vidaMax +
                "ATAQUE: " +  ataqueBase +
                "DEFENSA: " + defensaBase +
                "MOVIMIENTO: " + puntosMovDisponibles + "/" + rangoMov;
    }


    //GETTERS Y SETTERS

    public int getVidaMax() {
        return vidaMax;
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

    public int getDefensaBase() {
        return defensaBase;
    }

    public int getRangoMov() {
        return rangoMov;
    }

    public int getPuntosMovDisponibles() {
        return puntosMovDisponibles;
    }

    public void setPuntosMovDisponibles(int puntosMovDisponibles) {
        this.puntosMovDisponibles = puntosMovDisponibles;
    }
}
