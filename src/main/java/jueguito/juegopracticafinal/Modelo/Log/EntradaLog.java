package jueguito.juegopracticafinal.Modelo.Log;

//Representa una entrada del registro del juego almacenando un mensaje descriptivo
public class EntradaLog {

    //ATRIBUTOS

    private final String mensaje;


    public EntradaLog(String mensaje) {
        this.mensaje = mensaje;
    }


    //GETTERS Y SETTERS
    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        return mensaje;
    }
}