package modelo;

public enum CategoriaItem {
    COMIDA("Comida"),
    BEBIDA("Bebidas"),
    SALUD_ASEO("Salud y Aseo"),
    ACADEMICO("Académico"),
    VESTIMENTA("Armario Tadeísta");

    private final String etiqueta;

    CategoriaItem(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
