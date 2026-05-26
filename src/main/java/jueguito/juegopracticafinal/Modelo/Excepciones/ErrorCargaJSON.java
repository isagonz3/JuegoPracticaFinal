package jueguito.juegopracticafinal.Modelo.Excepciones;

public class ErrorCargaJSON extends RuntimeException {
    public ErrorCargaJSON(String message) {
        super(message);
    }
}
