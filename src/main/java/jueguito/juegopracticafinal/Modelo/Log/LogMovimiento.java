package jueguito.juegopracticafinal.Modelo.Log;

import jueguito.juegopracticafinal.TADs.Lista;
import jueguito.juegopracticafinal.TADs.Pila;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;

public class LogMovimiento {

    private final Lista<EntradaLog> entradas = new Lista<>();
    private final Pila<Posicion> pilaMovimientos = new Pila<>();

    public void registrar(String mensaje) {
        entradas.add(new EntradaLog(mensaje));
    }

    public void registrarMovimiento(Posicion posicionAnterior) {
        pilaMovimientos.push(posicionAnterior);
    }

    public Lista<EntradaLog> getEntradas() {
        return entradas;
    }

    public String getUltimaEntrada() {
        if (entradas.isEmpty()) return "";
        return entradas.get(entradas.getSize() - 1).getMensaje();
    }

    public Posicion deshacerMovimiento() {
        if (pilaMovimientos.isEmpty()) {
            return null;
        }

        Posicion anterior = pilaMovimientos.pop();
        entradas.add(new EntradaLog("Movimiento deshecho"));
        return anterior;
    }
}