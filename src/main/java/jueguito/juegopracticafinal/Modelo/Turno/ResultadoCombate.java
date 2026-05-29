package jueguito.juegopracticafinal.Modelo.Turno;

//Representa el resultado de un combate almacenando el daño realizado, la vida restante del enemigo, si fue derrotado y el mensaje del combate
public class ResultadoCombate {

    //ATRIBUTOS

    private final int hit;
    private final int vidaRestante;
    private final boolean enemigoKO;
    private final String mensaje;


    //Es el constructor del resultado del combate que inicializa el daño causado, la vida restante, el estado del enemigo y el mensaje
    public ResultadoCombate(int hit, int vidaRestante, boolean enemigoKO, String mensaje) {
        this.hit = hit;
        this.vidaRestante = vidaRestante;
        this.enemigoKO = enemigoKO;
        this.mensaje = mensaje;
    }
}