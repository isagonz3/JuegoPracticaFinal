package jueguito.juegopracticafinal.Modelo.Log;

import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Excepciones.DeshacerMovimientoInvalido;
import jueguito.juegopracticafinal.TADs.Lista;
import jueguito.juegopracticafinal.TADs.Pila;

//Gestiona el registro de eventos y movimientos del jugador permitiendo deshacer acciones válidas
public class LogMovimiento {

    //ATRIBUTOS

    private final Lista<EntradaLog> entradas = new Lista<>();
    private final Pila<Posicion> pilaMovimientos = new Pila<>();

    private Zona ultimaZonaMovimiento = null;


    //MÉTODOS

    //Registra un mensaje en el historial del log
    public void registrar(String mensaje) {
        entradas.add(new EntradaLog(mensaje));
    }

    //Registra una posición anterior del jugador junto a la zona actual para permitir deshacer el movimiento
    public void registrarMovimiento(Posicion posicionAnterior, Zona zonaActual) {
        pilaMovimientos.push(posicionAnterior);
        ultimaZonaMovimiento = zonaActual;
    }

    //Deshace el último movimiento realizado comprobando que pertenece a la misma zona actual
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


    //GETTERS Y SETTERS

    public Lista<EntradaLog> getEntradas() {
        return entradas;
    }

    public String getUltimaEntrada() {

        if (entradas.isEmpty()) {
            return "";
        }

        return entradas.get(entradas.getSize() - 1).getMensaje();
    }
}