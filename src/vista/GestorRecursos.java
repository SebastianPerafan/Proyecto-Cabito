package vista;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GestorRecursos {

    private static final String RUTA_BASE_ARCHIVOS = "src/recursos/imagenes/";
    private static final String RUTA_BASE_SUBCARPETA = "Proyecto-Cabito/src/recursos/imagenes/";
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

        String[] candidatos = construirNombresCandidatos(nombreArchivo.trim());
        for (String candidato : candidatos) {
            Image cargada = intentarCargarImagen(candidato);
            if (cargada != null) {
                CACHE_IMAGENES.put(nombreArchivo, cargada);
                CACHE_IMAGENES.put(candidato, cargada);
                return cargada;
            }
        }

        ARCHIVOS_NO_ENCONTRADOS.add(nombreArchivo);
        return null;
    }

    private static String[] construirNombresCandidatos(String nombreOriginal) {
        int punto = nombreOriginal.lastIndexOf('.');
        String base = (punto > 0) ? nombreOriginal.substring(0, punto) : nombreOriginal;
        return new String[]{
            nombreOriginal,
            base + ".png",
            base + ".jpg",
            base + ".jpeg"
        };
    }

    private static Image intentarCargarImagen(String nombreCandidato) {
        try {
            File archivoDirecto = new File(RUTA_BASE_ARCHIVOS + nombreCandidato);
            if (archivoDirecto.exists() && archivoDirecto.isFile()) {
                return ImageIO.read(archivoDirecto);
            }

            File archivoSubcarpeta = new File(RUTA_BASE_SUBCARPETA + nombreCandidato);
            if (archivoSubcarpeta.exists() && archivoSubcarpeta.isFile()) {
                return ImageIO.read(archivoSubcarpeta);
            }

            URL recursoClasspath = GestorRecursos.class.getResource(RUTA_BASE_CLASSPATH + nombreCandidato);
            if (recursoClasspath != null) {
                return ImageIO.read(recursoClasspath);
            }
        } catch (IOException ignored) {
        }
        return null;
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

    public static Image obtenerImagenIcono(String nombreArchivo, int anchoMax, int altoMax) {
        if (nombreArchivo == null || nombreArchivo.isBlank() || anchoMax <= 0 || altoMax <= 0) {
            return null;
        }

        String claveCrop = nombreArchivo + "_crop_" + anchoMax + "x" + altoMax;
        if (CACHE_IMAGENES.containsKey(claveCrop)) {
            return CACHE_IMAGENES.get(claveCrop);
        }

        Image original = obtenerImagen(nombreArchivo);
        if (original == null) {
            return null;
        }

        BufferedImage buffered = convertirABufferedImage(original);
        if (buffered == null) {
            return obtenerImagenEscalada(nombreArchivo, anchoMax, altoMax);
        }

        BufferedImage recortada = recortarBordesTransparentes(buffered);
        int wOrig = Math.max(1, recortada.getWidth());
        int hOrig = Math.max(1, recortada.getHeight());

        double escala = Math.min((double) anchoMax / wOrig, (double) altoMax / hOrig);
        int nuevoW = Math.max(1, (int) Math.round(wOrig * escala));
        int nuevoH = Math.max(1, (int) Math.round(hOrig * escala));

        BufferedImage lienzo = new BufferedImage(anchoMax, altoMax, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = lienzo.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Image suavizada = recortada.getScaledInstance(nuevoW, nuevoH, Image.SCALE_SMOOTH);
        int x = (anchoMax - nuevoW) / 2;
        int y = (altoMax - nuevoH) / 2;
        g2.drawImage(suavizada, x, y, null);
        g2.dispose();

        CACHE_IMAGENES.put(claveCrop, lienzo);
        return lienzo;
    }

    public static ImageIcon obtenerIconoRecortado(String nombreArchivo, int anchoMax, int altoMax) {
        Image img = obtenerImagenIcono(nombreArchivo, anchoMax, altoMax);
        return (img != null) ? new ImageIcon(img) : null;
    }

    private static BufferedImage convertirABufferedImage(Image img) {
        if (img instanceof BufferedImage bi) {
            return bi;
        }
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        if (w <= 0 || h <= 0) {
            return null;
        }
        BufferedImage copia = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = copia.createGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.dispose();
        return copia;
    }

    private static BufferedImage recortarBordesTransparentes(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int minX = w;
        int minY = h;
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int alpha = (img.getRGB(x, y) >>> 24) & 0xFF;
                if (alpha > 15) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            return img;
        }

        int margen = 4;
        minX = Math.max(0, minX - margen);
        minY = Math.max(0, minY - margen);
        maxX = Math.min(w - 1, maxX + margen);
        maxY = Math.min(h - 1, maxY + margen);

        return img.getSubimage(minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
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
