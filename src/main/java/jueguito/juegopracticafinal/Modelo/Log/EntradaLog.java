package jueguito.juegopracticafinal.Modelo.Log;

public record EntradaLog(String mensaje) {

    @Override
    public String toString() {
        return mensaje;
    }
}