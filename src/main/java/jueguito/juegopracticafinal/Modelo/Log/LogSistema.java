package jueguito.juegopracticafinal.Modelo.Log;

import jueguito.juegopracticafinal.TADs.Lista;

public class LogSistema {

    private final Lista<EntradaLog> entradas = new Lista<>();

    public void registrar(String mensaje) {
        entradas.add(new EntradaLog(mensaje));
    }

    public Lista<EntradaLog> getEntradas() {
        return entradas;
    }

    public String getUltimaEntrada() {
        if (entradas.isEmpty()) return "";
        return entradas.get(entradas.getSize() - 1).getMensaje();
    }
}