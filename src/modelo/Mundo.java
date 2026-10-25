package modelo;

public enum Mundo {
    PLAZOLETA("La Plazoleta", 0, "fondo_plazoleta.png"),
    BIBLIOTECA("La Biblioteca UTADEO", 2, "fondo_biblioteca.png"),
    AUDITORIO("El Auditorio", 4, "fondo_auditorio.png");

    private final String nombre;
    private final int nivelRequerido;
    private final String nombreImagenFondo;

    Mundo(String nombre, int nivelRequerido, String nombreImagenFondo) {
        this.nombre = nombre;
        this.nivelRequerido = nivelRequerido;
        this.nombreImagenFondo = nombreImagenFondo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNivelRequerido() {
        return nivelRequerido;
    }

    public String getNombreImagenFondo() {
        return nombreImagenFondo;
    }
}
