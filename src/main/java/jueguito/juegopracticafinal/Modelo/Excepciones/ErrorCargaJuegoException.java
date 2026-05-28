package jueguito.juegopracticafinal.Modelo.Excepciones;

public class ErrorCargaJuegoException extends RuntimeException {
  public ErrorCargaJuegoException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }
}