package jueguito.juegopracticafinal.Modelo.Inventario;

import jueguito.juegopracticafinal.TADs.Lista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventario {

    private Lista<Objeto> objetos;
    private int sizeInventario;

    // 🟦 NUEVO: usados
    private Lista<Objeto> objetosUsados;

    // 🟩 NUEVO: equipados
    private Map<SlotEquipable, Objeto> objetosEquipados;


    public Inventario() {
        this.objetos = new Lista<>();
        this.sizeInventario = 10;

        // 🆕 INIT
        this.objetosUsados = new Lista<>();
        this.objetosEquipados = new HashMap<>();
    }

    public Inventario(int size) {
        this.objetos = new Lista<>();
        this.sizeInventario = size;

        // 🆕 INIT
        this.objetosUsados = new Lista<>();
        this.objetosEquipados = new HashMap<>();
    }

    // =========================
    // AÑADIR OBJETO (SIN STACK)
    // =========================
    public boolean addObjeto(Objeto objeto) {

        if (objetos.getSize() >= sizeInventario) {
            return false;
        }

        objetos.add(objeto);
        return true;
    }

    // =========================
    // ELIMINAR OBJETO
    // =========================
    public boolean removeObjeto(Objeto objeto) {
        return objetos.delete(objeto) != null;
    }

    // =========================
    // CONTIENE OBJETO
    // =========================
    public boolean containsObjeto(String nombre) {

        for (int i = 0; i < objetos.getSize(); i++) {

            if (objetos.get(i).getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }

        return false;
    }


    public boolean contiene(Objeto objeto) {

        for (int i = 0; i < objetos.getSize(); i++) {

            if (objetos.get(i).equals(objeto)) {
                return true;
            }
        }

        return false;
    }

    // =========================
    // GET POR ÍNDICE
    // =========================
    public Objeto getObjeto(int index) {
        return objetos.get(index);
    }

    // =========================
    // BUSCAR OBJETO
    // =========================
    public Objeto findObjeto(String nombre) {

        for (int i = 0; i < objetos.getSize(); i++) {

            Objeto o = objetos.get(i);

            if (o.getNombre().equalsIgnoreCase(nombre)) {
                return o;
            }
        }

        return null;
    }

    // =========================
    // SIZE
    // =========================
    public int size() {
        return objetos.getSize();
    }

    public boolean inventarioLleno() {
        return objetos.getSize() >= sizeInventario;
    }

    public boolean inventarioVacio() {
        return objetos.isEmpty();
    }

    public Lista<Objeto> getObjetos() {
        return objetos;
    }

    public void setSizeInventario(int size) {
        this.sizeInventario = size;
    }


    // =====================================================
    // 🆕🆕🆕 LO QUE TE FALTABA (AÑADIDO SIN ROMPER NADA)
    // =====================================================

    // =========================
    // USADOS
    // =========================
    public void registrarUsado(Objeto o) {
        objetosUsados.add(o);
    }

    public Lista<Objeto> getObjetosUsados() {
        return objetosUsados;
    }

    // =========================
    // EQUIPAMIENTO
    // =========================
    public boolean equipar(Objeto o) {

        if (o == null) return false;

        SlotEquipable slot = o.getSlot();
        if (slot == null) return false;

        objetosEquipados.put(slot, o);
        return true;
    }

    public void desequipar(SlotEquipable slot) {
        objetosEquipados.remove(slot);
    }

    public Objeto getEquipado(SlotEquipable slot) {
        return objetosEquipados.get(slot);
    }

    public Map<SlotEquipable, Objeto> getObjetosEquipados() {
        return objetosEquipados;
    }
}
