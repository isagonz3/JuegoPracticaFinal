package jueguito.juegopracticafinal.Modelo.Log;

import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Excepciones.DeshacerMovimientoInvalido;
import jueguito.juegopracticafinal.TADs.Lista;
import jueguito.juegopracticafinal.TADs.Pila;

public class LogMovimiento {

    private final Lista<EntradaLog> entradas = new Lista<>();
    private final Pila<Posicion> pilaMovimientos = new Pila<>();

    private Zona ultimaZonaMovimiento = null;

    public void registrar(String mensaje) {
        entradas.add(new EntradaLog(mensaje));
    }

    // ============================================================
    // REGISTRAR MOVIMIENTO (con zona para validación de undo)
    // ============================================================
    public void registrarMovimiento(Posicion posicionAnterior, Zona zonaActual) {
        pilaMovimientos.push(posicionAnterior);
        ultimaZonaMovimiento = zonaActual;
    }

    public Lista<EntradaLog> getEntradas() {
        return entradas;
    }

    public String getUltimaEntrada() {
        if (entradas.isEmpty()) return "";
        return entradas.get(entradas.getSize() - 1).getMensaje();
    }

    // ============================================================
    // DESHACER MOVIMIENTO
    // ============================================================
    public Posicion deshacerMovimiento(Zona zonaActual) {

        if (pilaMovimientos.isEmpty()) {
            return null;
        }

        if (ultimaZonaMovimiento == null ||
                zonaActual == null ||
                ultimaZonaMovimiento.getIdZona() != zonaActual.getIdZona()) {

            throw new DeshacerMovimientoInvalido(
                    "No puedes deshacer un movimiento realizado en otra zona"
            );
        }

        Posicion anterior = pilaMovimientos.pop();

        entradas.add(new EntradaLog("Movimiento deshecho"));

        if (pilaMovimientos.isEmpty()) {
            ultimaZonaMovimiento = null;
        }

        return anterior;
    }
}