package jueguito.juegopracticafinal.Modelo.Excepciones;

public class DeshacerMovimientoInvalido extends RuntimeException {
    public DeshacerMovimientoInvalido(String mensaje) {
        super(mensaje);
    }
}