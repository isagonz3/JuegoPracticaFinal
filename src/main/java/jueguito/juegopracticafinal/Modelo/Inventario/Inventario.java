package jueguito.juegopracticafinal.Modelo.Inventario;

import jueguito.juegopracticafinal.TADs.Lista;

public class Inventario {

    private Lista<Objeto> objetos;
    private int sizeInventario;

    private Lista<Objeto> objetosUsados;
    private Lista<Objeto> objetosEquipados;

    public Inventario() {
        this.objetos = new Lista<>();
        this.objetosUsados = new Lista<>();
        this.objetosEquipados = new Lista<>();
        this.sizeInventario = 10;
    }

    public Inventario(int size) {
        this.objetos = new Lista<>();
        this.objetosUsados = new Lista<>();
        this.objetosEquipados = new Lista<>();
        this.sizeInventario = size;
    }

    public boolean addObjeto(Objeto objeto) {

        if (objetos.getSize() >= sizeInventario) return false;

        objetos.add(objeto);
        return true;
    }

    public boolean removeObjeto(Objeto objeto) {
        return objetos.delete(objeto) != null;
    }

    public int size() {
        return objetos.getSize();
    }

    public boolean inventarioLleno() {
        return objetos.getSize() >= sizeInventario;
    }

    public Lista<Objeto> getObjetos() {
        return objetos;
    }

    public Objeto getObjeto(int index) {
        return objetos.get(index);
    }

    public boolean contiene(Objeto objeto) {

        for (int i = 0; i < objetos.getSize(); i++) {

            if (objetos.get(i).getId() == objeto.getId()) {
                return true;
            }
        }
        return false;
    }

    public Lista<Objeto> getObjetosUsados() {
        return objetosUsados;
    }
    public Lista<Objeto> getObjetosEquipados() {
        return objetosEquipados;
    }


    public boolean equipar(Objeto o) {

        if (o == null || o.getSlot() == null) return false;

        // máximo 2 equipados
        if (objetosEquipados.getSize() >= 2) return false;

        // solo 1 por tipo (ARMA / ESCUDO)
        for (int i = 0; i < objetosEquipados.getSize(); i++) {

            Objeto equipado = objetosEquipados.get(i);

            if (equipado.getSlot() == o.getSlot()) {
                return false;
            }
        }

        objetosEquipados.add(o);
        return true;
    }

    // =========================
    // TURNOS DE LOS OBJETOS
    // =========================
    public void avanzarTurno() {

        // =========================
        // OBJETOS USADOS (2 turnos)
        // =========================
        for (int i = 0; i < objetosUsados.getSize(); i++) {

            Objeto o = objetosUsados.get(i);

            o.incrementarTurno();

            if (o.getTurnosActivos() >= o.getDuracionTurnos()) {
                objetosUsados.delete(o);
                i--;
            }
        }

        // =========================
        // OBJETOS EQUIPADOS (5 turnos)
        // =========================
        for (int i = 0; i < objetosEquipados.getSize(); i++) {

            Objeto o = objetosEquipados.get(i);

            o.incrementarTurno();

            if (o.getTurnosActivos() >= o.getDuracionTurnos()) {

                objetosEquipados.delete(o);
                i--;

            }
        }
    }
}
