package jueguito.juegopracticafinal.Modelo.Excepciones;

public class ErrorSinTurnos extends RuntimeException {
    public ErrorSinTurnos(String message) {
        super(message);
    }
}
