package modelo;

import java.util.LinkedList;

public class Logro {

    private String id;
    private String nombre;
    private String descripcion;
    private boolean desbloqueado;
    private String idAccesorioRecompensa;

    public Logro(String id, String nombre, String descripcion, boolean desbloqueado, String idAccesorioRecompensa) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.desbloqueado = desbloqueado;
        this.idAccesorioRecompensa = idAccesorioRecompensa;
    }

    public static LinkedList<Logro> crearLogrosIniciales() {
        LinkedList<Logro> logros = new LinkedList<>();
        logros.add(new Logro(
                "LOGRO_1",
                "Primíparo Tadeísta",
                "Alcanza el Nivel 1 de experiencia.",
                false,
                "ACC_CARNE"));
        logros.add(new Logro(
                "LOGRO_2",
                "Cliente Frecuente",
                "Realiza 5 compras en la Tienda de la Universidad.",
                false,
                "ACC_GORRA"));
        logros.add(new Logro(
                "LOGRO_3",
                "Mente Brillante",
                "Alcanza 80 puntos de Inteligencia.",
                false,
                "ACC_GAFAS"));
        logros.add(new Logro(
                "LOGRO_4",
                "Campeón Universitario",
                "Acumula 3 victorias en cualquier minijuego.",
                false,
                "ACC_BUZO"));
        logros.add(new Logro(
                "LOGRO_5",
                "Orgullo Tadeísta",
                "Alcanza el Nivel 4 de experiencia.",
                false,
                "ACC_BIRRETE"));
        return logros;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isDesbloqueado() {
        return desbloqueado;
    }

    public void setDesbloqueado(boolean desbloqueado) {
        this.desbloqueado = desbloqueado;
    }

    public String getIdAccesorioRecompensa() {
        return idAccesorioRecompensa;
    }

    public void setIdAccesorioRecompensa(String idAccesorioRecompensa) {
        this.idAccesorioRecompensa = idAccesorioRecompensa;
    }
}
