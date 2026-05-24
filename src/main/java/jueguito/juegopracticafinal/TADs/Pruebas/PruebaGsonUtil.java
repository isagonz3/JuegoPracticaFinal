package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.GsonUtil;

public class PruebaGsonUtil {

    public static void main(String[] args) {

        System.out.println("PRUEBA GSON UTIL");

        String ruta = "persona.json";

        Persona persona = new Persona("Juan", 20);

        // guardar objeto
        GsonUtil.guardarObjetoEnArchivo(ruta, persona);

        System.out.println("Objeto guardado");

        // comprobar archivo
        System.out.println("Existe archivo?");
        System.out.println(GsonUtil.existeArchivo(ruta));

        // cargar objeto
        Persona personaLeida =
                GsonUtil.cargarObjetoDesdeArchivo(ruta, Persona.class);

        System.out.println("Objeto cargado:");
        System.out.println(personaLeida);

        // eliminar archivo
        GsonUtil.eliminarFichero(ruta);

        System.out.println("Archivo eliminado");
    }
}