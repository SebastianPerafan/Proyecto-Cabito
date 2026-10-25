package modelo;

public enum EstadoCabito {
    ENFERMO("Enfermo", "cabito_enfermo.png", false),
    HAMBRIENTO("Hambriento", "cabito_hambriento.png", false),
    CANSADO("Cansado", "cabito_cansado.png", false),
    TRISTE("Triste", "cabito_triste.png", false),
    FELIZ("Feliz", "cabito_feliz.png", false),
    NORMAL("Normal", "cabito_normal.png", false),
    DORMIDO("Dormido", "cabito_dormido.png", true),
    JUGANDO("Jugando", "cabito_jugando.png", true);

    private final String nombreMostrar;
    private final String nombreImagen;
    private final boolean temporal;

    EstadoCabito(String nombreMostrar, String nombreImagen, boolean temporal) {
        this.nombreMostrar = nombreMostrar;
        this.nombreImagen = nombreImagen;
        this.temporal = temporal;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public boolean esTemporal() {
        return temporal;
    }
}
