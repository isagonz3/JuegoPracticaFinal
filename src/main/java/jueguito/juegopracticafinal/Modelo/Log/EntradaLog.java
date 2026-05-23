package jueguito.juegopracticafinal.Modelo.Log;

public class EntradaLog {
    private final String mensaje;

    public EntradaLog(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        return mensaje;
    }
}