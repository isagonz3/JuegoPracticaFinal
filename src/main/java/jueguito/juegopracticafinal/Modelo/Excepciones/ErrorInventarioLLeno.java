package jueguito.juegopracticafinal.Modelo.Excepciones;

public class ErrorInventarioLLeno extends RuntimeException {
    public ErrorInventarioLLeno(String message) {
        super(message);
    }
}
