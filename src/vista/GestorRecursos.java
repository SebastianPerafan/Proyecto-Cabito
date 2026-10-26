package vista;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GestorRecursos {

    private static final String RUTA_BASE_ARCHIVOS = "src/recursos/imagenes/";
    private static final String RUTA_BASE_CLASSPATH = "/recursos/imagenes/";
    private static final HashMap<String, Image> CACHE_IMAGENES = new HashMap<>();
    private static final HashSet<String> ARCHIVOS_NO_ENCONTRADOS = new HashSet<>();

    private GestorRecursos() {
    }

    public static Image obtenerImagen(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            return null;
        }

        if (CACHE_IMAGENES.containsKey(nombreArchivo)) {
            return CACHE_IMAGENES.get(nombreArchivo);
        }

        if (ARCHIVOS_NO_ENCONTRADOS.contains(nombreArchivo)) {
            return null;
        }

        try {
            File archivoLocal = new File(RUTA_BASE_ARCHIVOS + nombreArchivo);
            Image imagenCargada = null;

            if (archivoLocal.exists() && archivoLocal.isFile()) {
                imagenCargada = ImageIO.read(archivoLocal);
            } else {
                URL recursoClasspath = GestorRecursos.class.getResource(RUTA_BASE_CLASSPATH + nombreArchivo);
                if (recursoClasspath != null) {
                    imagenCargada = ImageIO.read(recursoClasspath);
                }
            }

            if (imagenCargada != null) {
                CACHE_IMAGENES.put(nombreArchivo, imagenCargada);
                return imagenCargada;
            } else {
                ARCHIVOS_NO_ENCONTRADOS.add(nombreArchivo);
                System.err.println("No se encontró el archivo de imagen: " + RUTA_BASE_ARCHIVOS + nombreArchivo);
                return null;
            }
        } catch (IOException e) {
            ARCHIVOS_NO_ENCONTRADOS.add(nombreArchivo);
            System.err.println("Error al leer la imagen '" + nombreArchivo + "': " + e.getMessage());
            return null;
        }
    }

    public static Image escalarImagen(Image original, int ancho, int alto) {
        if (original == null || ancho <= 0 || alto <= 0) {
            return null;
        }
        return original.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
    }

    public static Image obtenerImagenEscalada(String nombreArchivo, int ancho, int alto) {
        if (nombreArchivo == null || nombreArchivo.isBlank() || ancho <= 0 || alto <= 0) {
            return null;
        }

        String claveEscalada = nombreArchivo + "_" + ancho + "x" + alto;
        if (CACHE_IMAGENES.containsKey(claveEscalada)) {
            return CACHE_IMAGENES.get(claveEscalada);
        }

        Image original = obtenerImagen(nombreArchivo);
        if (original == null) {
            return null;
        }

        Image escalada = escalarImagen(original, ancho, alto);
        if (escalada != null) {
            CACHE_IMAGENES.put(claveEscalada, escalada);
        }
        return escalada;
    }

    public static ImageIcon obtenerIcono(String nombreArchivo, int ancho, int alto) {
        Image imagen = obtenerImagenEscalada(nombreArchivo, ancho, alto);
        return (imagen != null) ? new ImageIcon(imagen) : null;
    }

    public static void limpiarCache() {
        CACHE_IMAGENES.clear();
        ARCHIVOS_NO_ENCONTRADOS.clear();
    }
}

