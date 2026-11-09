package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import modelo.Cabito;
import modelo.CategoriaItem;
import modelo.EstadoCabito;
import modelo.Item;
import modelo.Logro;
import modelo.Mundo;
import modelo.Partida;

public class GestorArchivos {

    public static final String PREFIJO_ARCHIVO = "partida_";
    public static final String EXTENSION_ARCHIVO = ".csv";
    public static final String ARCHIVO_POR_DEFECTO = "partida_cabito.csv";
    private static final String SEPARADOR = ",";

    private Path rutaArchivo;

    public GestorArchivos() {
        this(resolverRutaArchivo(obtenerNombreArchivo("cabito")));
    }

    public GestorArchivos(String rutaONombreUsuario) {
        if (rutaONombreUsuario != null && rutaONombreUsuario.toLowerCase().endsWith(EXTENSION_ARCHIVO)) {
            this.rutaArchivo = resolverRutaArchivo(rutaONombreUsuario);
        } else {
            this.rutaArchivo = resolverRutaArchivo(obtenerNombreArchivo(rutaONombreUsuario));
        }
    }

    public GestorArchivos(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public static String normalizarNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            return "jugador";
        }
        String sinAcentos = Normalizer.normalize(nombreUsuario.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String limpio = sinAcentos.toLowerCase().replaceAll("[^a-z0-9_-]", "");
        return limpio.isEmpty() ? "jugador" : limpio;
    }

    public static String obtenerNombreArchivo(String nombreUsuario) {
        String nombreLimpio = normalizarNombreUsuario(nombreUsuario);
        return PREFIJO_ARCHIVO + nombreLimpio + EXTENSION_ARCHIVO;
    }

    private static Path obtenerDirectorioBase() {
        Path subcarpetaProyecto = Paths.get("Proyecto-Cabito");
        if (Files.isDirectory(subcarpetaProyecto) && Files.isDirectory(subcarpetaProyecto.resolve("src"))) {
            return subcarpetaProyecto;
        }
        return Paths.get(".");
    }

    private static Path resolverRutaArchivo(String nombreArchivo) {
        Path candidatoDirecto = Paths.get(nombreArchivo);
        if (Files.exists(candidatoDirecto) && Files.isRegularFile(candidatoDirecto)) {
            return candidatoDirecto;
        }
        Path base = obtenerDirectorioBase();
        return base.resolve(nombreArchivo);
    }

    public static List<String> listarUsuariosRegistrados() {
        Set<String> usuarios = new LinkedHashSet<>();
        escanearDirectorioPerfiles(obtenerDirectorioBase().toFile(), usuarios);

        File directorioActual = new File(".");
        if (!directorioActual.toPath().normalize().equals(obtenerDirectorioBase().normalize())) {
            escanearDirectorioPerfiles(directorioActual, usuarios);
        }

        List<String> resultado = new ArrayList<>(usuarios);
        Collections.sort(resultado);
        return resultado;
    }

    private static void escanearDirectorioPerfiles(File directorio, Set<String> usuarios) {
        if (directorio == null || !directorio.exists() || !directorio.isDirectory()) {
            return;
        }
        File[] archivos = directorio.listFiles();
        if (archivos == null) {
            return;
        }
        for (File archivo : archivos) {
            if (!archivo.isFile()) {
                continue;
            }
            String nombre = archivo.getName().toLowerCase();
            if (nombre.startsWith(PREFIJO_ARCHIVO) && nombre.endsWith(EXTENSION_ARCHIVO)) {
                String usuarioExtraido = nombre.substring(
                        PREFIJO_ARCHIVO.length(),
                        nombre.length() - EXTENSION_ARCHIVO.length()
                ).trim();
                if (!usuarioExtraido.isEmpty()) {
                    usuarios.add(usuarioExtraido);
                }
            }
        }
    }

    public static boolean guardarPartida(Partida partida) {
        if (partida == null) {
            return false;
        }
        try {
            new GestorArchivos().guardarPartidaEnDisco(partida);
            return true;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al guardar la partida: " + e.getMessage());
            return false;
        }
    }

    public static Partida cargarPartida() {
        return cargarPartida("cabito");
    }

    public static Partida cargarPartida(String nombreUsuario) {
        try {
            return new GestorArchivos(nombreUsuario).cargarPartidaDesdeDisco(nombreUsuario);
        } catch (IOException e) {
            return null;
        }
    }

    public void guardarPartidaEnDisco(Partida partida) throws IOException {
        if (partida == null) {
            throw new IllegalArgumentException("La partida a guardar no puede ser nula.");
        }

        String usuarioNormalizado = normalizarNombreUsuario(partida.getNombreUsuario());
        partida.setNombreUsuario(usuarioNormalizado);
        this.rutaArchivo = obtenerDirectorioBase().resolve(obtenerNombreArchivo(usuarioNormalizado));

        Path directorioPadre = this.rutaArchivo.getParent();
        if (directorioPadre != null && !Files.exists(directorioPadre)) {
            Files.createDirectories(directorioPadre);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(this.rutaArchivo, StandardCharsets.UTF_8)) {
            writer.write("TIPO_REGISTRO,CAMPO_1,CAMPO_2,CAMPO_3,CAMPO_4,CAMPO_5,CAMPO_6,CAMPO_7,CAMPO_8,CAMPO_9,CAMPO_10,CAMPO_11,CAMPO_12,CAMPO_13");
            writer.newLine();

            writer.write(unirCampos(
                    "PARTIDA",
                    limpiarTexto(partida.getNombrePartida()),
                    String.valueOf(partida.getNivel()),
                    String.valueOf(partida.getExperiencia()),
                    String.valueOf(partida.getMonedas()),
                    partida.getMundoActual().name(),
                    String.valueOf(partida.getComprasRealizadas()),
                    String.valueOf(partida.getVictoriasMinijuegos()),
                    limpiarTexto(partida.getNombreUsuario())
            ));
            writer.newLine();

            Cabito cabito = partida.getCabito();
            writer.write(unirCampos(
                    "CABITO",
                    String.valueOf(cabito.getHambre()),
                    String.valueOf(cabito.getEnergia()),
                    String.valueOf(cabito.getHigiene()),
                    String.valueOf(cabito.getFelicidad()),
                    String.valueOf(cabito.getSalud()),
                    String.valueOf(cabito.getInteligencia()),
                    cabito.getEstadoActual().name()
            ));
            writer.newLine();

            for (Mundo mundo : Mundo.values()) {
                writer.write(unirCampos(
                        "MUNDO",
                        mundo.name(),
                        String.valueOf(partida.estaMundoDesbloqueado(mundo))
                ));
                writer.newLine();
            }

            for (Logro logro : partida.getLogros()) {
                writer.write(unirCampos(
                        "LOGRO",
                        limpiarTexto(logro.getId()),
                        limpiarTexto(logro.getNombre()),
                        limpiarTexto(logro.getDescripcion()),
                        String.valueOf(logro.isDesbloqueado()),
                        limpiarTexto(logro.getIdAccesorioRecompensa())
                ));
                writer.newLine();
            }

            for (Item item : partida.getConsumibles()) {
                writer.write(unirCampos(
                        "CONSUMIBLE",
                        limpiarTexto(item.getId()),
                        limpiarTexto(item.getNombre()),
                        limpiarTexto(item.getDescripcion()),
                        item.getCategoria().name(),
                        String.valueOf(item.getPrecio()),
                        String.valueOf(item.getBonoHambre()),
                        String.valueOf(item.getBonoEnergia()),
                        String.valueOf(item.getBonoHigiene()),
                        String.valueOf(item.getBonoFelicidad()),
                        String.valueOf(item.getBonoSalud()),
                        String.valueOf(item.getBonoInteligencia()),
                        String.valueOf(item.getBonoXp()),
                        limpiarTexto(item.getNombreImagen())
                ));
                writer.newLine();
            }

            for (Item prenda : partida.getArmario()) {
                writer.write(unirCampos(
                        "ARMARIO",
                        limpiarTexto(prenda.getId()),
                        limpiarTexto(prenda.getNombre()),
                        limpiarTexto(prenda.getDescripcion()),
                        String.valueOf(prenda.isDesbloqueado()),
                        String.valueOf(prenda.isEquipado()),
                        limpiarTexto(prenda.getNombreImagen()),
                        String.valueOf(prenda.getOffsetX()),
                        String.valueOf(prenda.getOffsetY()),
                        String.valueOf(prenda.getEscala())
                ));
                writer.newLine();
            }
        }
    }

    public Partida cargarPartidaDesdeDisco(String nombreUsuario) throws IOException {
        String usuarioNormalizado = normalizarNombreUsuario(nombreUsuario);
        this.rutaArchivo = resolverRutaArchivo(obtenerNombreArchivo(usuarioNormalizado));
        Partida partida = cargarPartidaDesdeDisco();
        partida.setNombreUsuario(usuarioNormalizado);
        return partida;
    }

    public Partida cargarPartidaDesdeDisco() throws IOException {
        if (!existeArchivoGuardado()) {
            throw new IOException("No existe el archivo de partida en: " + rutaArchivo.toAbsolutePath());
        }

        Partida partida = new Partida("Estudiante Tadeísta", "jugador");
        partida.getConsumibles().clear();

        boolean armarioReiniciado = false;
        boolean logrosReiniciados = false;

        try (BufferedReader reader = Files.newBufferedReader(rutaArchivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String lineaLimpia = linea.trim();
                if (lineaLimpia.isEmpty() || lineaLimpia.startsWith("#") || lineaLimpia.startsWith("TIPO_REGISTRO")) {
                    continue;
                }

                String[] columnas = lineaLimpia.split(SEPARADOR, -1);
                String tipoRegistro = columnas[0].trim().toUpperCase();

                switch (tipoRegistro) {
                    case "PARTIDA" -> procesarLineaPartida(columnas, partida);
                    case "CABITO" -> procesarLineaCabito(columnas, partida);
                    case "MUNDO" -> procesarLineaMundo(columnas, partida);
                    case "LOGRO" -> {
                        if (!logrosReiniciados) {
                            partida.getLogros().clear();
                            logrosReiniciados = true;
                        }
                        procesarLineaLogro(columnas, partida);
                    }
                    case "CONSUMIBLE" -> procesarLineaConsumible(columnas, partida);
                    case "ARMARIO" -> {
                        if (!armarioReiniciado) {
                            partida.getArmario().clear();
                            armarioReiniciado = true;
                        }
                        procesarLineaArmario(columnas, partida);
                    }
                    default -> {
                    }
                }
            }
        }

        partida.verificarLogros();
        return partida;
    }

    public boolean existeArchivoGuardado() {
        return rutaArchivo != null && Files.exists(rutaArchivo) && Files.isRegularFile(rutaArchivo);
    }

    public Path getRutaArchivo() {
        return rutaArchivo;
    }

    private void procesarLineaPartida(String[] col, Partida partida) {
        if (col.length < 8) {
            return;
        }
        partida.setNombrePartida(col[1]);
        partida.setNivel(parseIntSeguro(col[2], 0));
        partida.setExperiencia(parseIntSeguro(col[3], 0));
        partida.setMonedas(parseIntSeguro(col[4], 30));
        partida.forzarMundoActual(parseEnumSeguro(Mundo.class, col[5], Mundo.PLAZOLETA));
        partida.setComprasRealizadas(parseIntSeguro(col[6], 0));
        partida.setVictoriasMinijuegos(parseIntSeguro(col[7], 0));
        if (col.length >= 9 && !col[8].isBlank()) {
            partida.setNombreUsuario(col[8]);
        }
    }

    private void procesarLineaCabito(String[] col, Partida partida) {
        if (col.length < 7) {
            return;
        }
        int hambre = parseIntSeguro(col[1], 80);
        int energia = parseIntSeguro(col[2], 100);
        int higiene = parseIntSeguro(col[3], 80);
        int felicidad = parseIntSeguro(col[4], 80);
        int salud = parseIntSeguro(col[5], 100);
        int inteligencia = parseIntSeguro(col[6], 10);

        EstadoCabito estado = null;
        if (col.length >= 8 && !col[7].isBlank()) {
            estado = parseEnumSeguro(EstadoCabito.class, col[7], null);
        }

        Cabito cabito = new Cabito(hambre, energia, higiene, felicidad, salud, inteligencia, estado);
        if (estado == null || !estado.esTemporal()) {
            cabito.actualizarEstadoAutomatico();
        }
        partida.setCabito(cabito);
    }

    private void procesarLineaMundo(String[] col, Partida partida) {
        if (col.length < 3) {
            return;
        }
        Mundo mundo = parseEnumSeguro(Mundo.class, col[1], null);
        if (mundo != null) {
            boolean desbloqueado = Boolean.parseBoolean(col[2].trim());
            partida.setMundoDesbloqueado(mundo, desbloqueado);
        }
    }

    private void procesarLineaLogro(String[] col, Partida partida) {
        if (col.length < 6) {
            return;
        }
        Logro logro = new Logro(
                col[1].trim(),
                col[2].trim(),
                col[3].trim(),
                Boolean.parseBoolean(col[4].trim()),
                col[5].trim()
        );
        partida.getLogros().add(logro);
    }

    private void procesarLineaConsumible(String[] col, Partida partida) {
        if (col.length < 14) {
            return;
        }
        CategoriaItem categoria = parseEnumSeguro(CategoriaItem.class, col[4], CategoriaItem.COMIDA);
        Item consumible = Item.crearConsumible(
                col[1].trim(),
                col[2].trim(),
                col[3].trim(),
                categoria,
                parseIntSeguro(col[5], 10),
                parseIntSeguro(col[6], 0),
                parseIntSeguro(col[7], 0),
                parseIntSeguro(col[8], 0),
                parseIntSeguro(col[9], 0),
                parseIntSeguro(col[10], 0),
                parseIntSeguro(col[11], 0),
                parseIntSeguro(col[12], 0),
                col[13].trim()
        );
        partida.getConsumibles().add(consumible);
    }

    private void procesarLineaArmario(String[] col, Partida partida) {
        if (col.length < 7) {
            return;
        }
        String idPrenda = col[1].trim();
        int offsetXPorDefecto = obtenerOffsetXPorDefecto(idPrenda);
        int offsetYPorDefecto = obtenerOffsetYPorDefecto(idPrenda);
        double escalaPorDefecto = obtenerEscalaPorDefecto(idPrenda);

        int offsetX = offsetXPorDefecto;
        int offsetY = offsetYPorDefecto;
        double escala = escalaPorDefecto;

        if (!tieneCalibracionOficial(idPrenda)) {
            if (col.length >= 8) {
                offsetX = parseIntSeguro(col[7], offsetXPorDefecto);
            }
            if (col.length >= 9) {
                offsetY = parseIntSeguro(col[8], offsetYPorDefecto);
            }
            if (col.length >= 10 && !col[9].isBlank()) {
                try {
                    escala = Double.parseDouble(col[9].trim());
                } catch (NumberFormatException e) {
                    escala = escalaPorDefecto;
                }
            }
        }

        Item accesorio = Item.crearAccesorio(
                idPrenda,
                col[2].trim(),
                col[3].trim(),
                Boolean.parseBoolean(col[4].trim()),
                Boolean.parseBoolean(col[5].trim()),
                col[6].trim(),
                offsetX,
                offsetY,
                escala
        );
        partida.getArmario().add(accesorio);
    }

    private boolean tieneCalibracionOficial(String idAccesorio) {
        if (idAccesorio == null) {
            return false;
        }
        String id = idAccesorio.trim().toUpperCase();
        return id.equals("ACC_CARNE")
                || id.equals("ACC_GORRA")
                || id.equals("ACC_GAFAS")
                || id.equals("ACC_BUZO")
                || id.equals("ACC_BIRRETE");
    }

    private int obtenerOffsetXPorDefecto(String idAccesorio) {
        for (Item base : Item.crearArmarioInicial()) {
            if (base.getId().equalsIgnoreCase(idAccesorio)) {
                return base.getOffsetX();
            }
        }
        return 0;
    }

    private int obtenerOffsetYPorDefecto(String idAccesorio) {
        for (Item base : Item.crearArmarioInicial()) {
            if (base.getId().equalsIgnoreCase(idAccesorio)) {
                return base.getOffsetY();
            }
        }
        return 0;
    }

    private double obtenerEscalaPorDefecto(String idAccesorio) {
        for (Item base : Item.crearArmarioInicial()) {
            if (base.getId().equalsIgnoreCase(idAccesorio)) {
                return base.getEscala();
            }
        }
        return 1.0;
    }

    private String unirCampos(String... campos) {
        return String.join(SEPARADOR, campos);
    }

    private String limpiarTexto(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace(SEPARADOR, ";").replace("\n", " ").replace("\r", "").trim();
    }

    private int parseIntSeguro(String texto, int valorPorDefecto) {
        if (texto == null || texto.isBlank()) {
            return valorPorDefecto;
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return valorPorDefecto;
        }
    }

    private <E extends Enum<E>> E parseEnumSeguro(Class<E> claseEnum, String texto, E valorPorDefecto) {
        if (texto == null || texto.isBlank()) {
            return valorPorDefecto;
        }
        try {
            return Enum.valueOf(claseEnum, texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return valorPorDefecto;
        }
    }
}
