package jueguito.juegopracticafinal.Modelo.Excepciones;

public class ErrorCargaVistaException extends RuntimeException {

    public ErrorCargaVistaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}