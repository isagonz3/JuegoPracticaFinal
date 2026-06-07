package jueguito.juegopracticafinal.TADs;

import com.google.gson.Gson;
import java.io.*;

public class GsonUtil {

    private static final Gson GSON = new Gson();

    //Metodo para guardar un objeto en un archivo JSON
    public static <T> void guardarObjetoEnArchivo(String rutaArchivo, T objeto) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            GSON.toJson(objeto, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo." + e.getMessage(), e);
        }
    }

    // Metodo para cargar un objeto desde un archivo JSON
    public static <T> T cargarObjetoDesdeArchivo(String rutaArchivo, Class<T> clase) {
        T objeto = null;
        try (FileReader reader = new FileReader(rutaArchivo)) {
            objeto = GSON.fromJson(reader, clase);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Archivo no encontrado: " + rutaArchivo);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo.", e);
        }
        return objeto;
    }

    //Metodo que guarda varios objetos en
    public static <T> void guardarArray(String rutaArchivo, T[] datos) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            GSON.toJson(datos, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo.", e);
        }
    }

    //Metodo que carga un array de objetos desde un archivo Json
    public static <T> T[] cargarArray(String rutaArchivo, Class<T[]> claseArray) {
        T[] datos=null;
        try (FileReader reader=new FileReader(rutaArchivo)) {
            datos = GSON.fromJson(reader, claseArray);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Archivo no encontrado: "+rutaArchivo);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo.", e);
        }
        return datos;
    }

    //Metodo que imprime el toString
    public static <T> void imprimirToStringDesdeArchivo(String rutaArchivo, Class<T> clase) {
        T objeto = cargarObjetoDesdeArchivo(rutaArchivo, clase);
        if (objeto!=null) {
            System.out.println(objeto.toString());
        } else {
            throw new RuntimeException("No se pudo leer el fichero: "+rutaArchivo);
        }
    }

    //Metodo que elimina un fichero
    public static boolean eliminarFichero(String rutaArchivo) {
        java.io.File file=new java.io.File(rutaArchivo);
        return file.delete();
    }

    public static boolean existeArchivo(String rutaArchivo) {
        File file=new java.io.File(rutaArchivo);
        return file.exists();
    }
}
