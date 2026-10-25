package modelo;

import java.util.LinkedList;

public class Item {

    private String id;
    private String nombre;
    private String descripcion;
    private TipoItem tipo;
    private CategoriaItem categoria;
    private int precio;

    private int bonoHambre;
    private int bonoEnergia;
    private int bonoHigiene;
    private int bonoFelicidad;
    private int bonoSalud;
    private int bonoInteligencia;
    private int bonoXp;

    private boolean desbloqueado;
    private boolean equipado;
    private String nombreImagen;

    public Item(
            String id,
            String nombre,
            String descripcion,
            TipoItem tipo,
            CategoriaItem categoria,
            int precio,
            int bonoHambre,
            int bonoEnergia,
            int bonoHigiene,
            int bonoFelicidad,
            int bonoSalud,
            int bonoInteligencia,
            int bonoXp,
            boolean desbloqueado,
            boolean equipado,
            String nombreImagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.categoria = categoria;
        this.precio = Math.max(0, precio);
        this.bonoHambre = bonoHambre;
        this.bonoEnergia = bonoEnergia;
        this.bonoHigiene = bonoHigiene;
        this.bonoFelicidad = bonoFelicidad;
        this.bonoSalud = bonoSalud;
        this.bonoInteligencia = bonoInteligencia;
        this.bonoXp = bonoXp;
        this.desbloqueado = desbloqueado;
        this.equipado = equipado;
        this.nombreImagen = nombreImagen;
    }

    public static Item crearConsumible(
            String id,
            String nombre,
            String descripcion,
            CategoriaItem categoria,
            int precio,
            int bonoHambre,
            int bonoEnergia,
            int bonoHigiene,
            int bonoFelicidad,
            int bonoSalud,
            int bonoInteligencia,
            int bonoXp,
            String nombreImagen) {
        return new Item(
                id,
                nombre,
                descripcion,
                TipoItem.CONSUMIBLE,
                categoria,
                precio,
                bonoHambre,
                bonoEnergia,
                bonoHigiene,
                bonoFelicidad,
                bonoSalud,
                bonoInteligencia,
                bonoXp,
                true,
                false,
                nombreImagen
        );
    }

    public static Item crearAccesorio(
            String id,
            String nombre,
            String descripcion,
            boolean desbloqueado,
            boolean equipado,
            String nombreImagen) {
        return new Item(
                id,
                nombre,
                descripcion,
                TipoItem.ACCESORIO,
                CategoriaItem.VESTIMENTA,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                desbloqueado,
                equipado,
                nombreImagen
        );
    }

    public Item copiar() {
        return new Item(
                this.id,
                this.nombre,
                this.descripcion,
                this.tipo,
                this.categoria,
                this.precio,
                this.bonoHambre,
                this.bonoEnergia,
                this.bonoHigiene,
                this.bonoFelicidad,
                this.bonoSalud,
                this.bonoInteligencia,
                this.bonoXp,
                this.desbloqueado,
                this.equipado,
                this.nombreImagen
        );
    }

    public static LinkedList<Item> obtenerCatalogoTienda() {
        LinkedList<Item> catalogo = new LinkedList<>();

        catalogo.add(crearConsumible(
                "MANZANA", "Manzana", "+20 Hambre",
                CategoriaItem.COMIDA, 10,
                20, 0, 0, 0, 0, 0, 0, "item_manzana.png"));
        catalogo.add(crearConsumible(
                "SOPA", "Sopa Instantánea", "+35 Hambre, -5 Salud",
                CategoriaItem.COMIDA, 15,
                35, 0, 0, 0, -5, 0, 0, "item_sopa.png"));
        catalogo.add(crearConsumible(
                "PERRO_CALIENTE", "Perro Caliente", "+50 Hambre, +10 Felicidad",
                CategoriaItem.COMIDA, 25,
                50, 0, 0, 10, 0, 0, 0, "item_perro_caliente.png"));

        catalogo.add(crearConsumible(
                "CAFE", "Café Clásico", "+30 Energía",
                CategoriaItem.BEBIDA, 12,
                0, 30, 0, 0, 0, 0, 0, "item_cafe.png"));
        catalogo.add(crearConsumible(
                "ENERGIZANTE", "Energizante", "+50 Energía, -10 Salud",
                CategoriaItem.BEBIDA, 20,
                0, 50, 0, 0, -10, 0, 0, "item_energizante.png"));

        catalogo.add(crearConsumible(
                "GEL", "Gel Antibacterial", "+40 Higiene",
                CategoriaItem.SALUD_ASEO, 15,
                0, 0, 40, 0, 0, 0, 0, "item_gel.png"));
        catalogo.add(crearConsumible(
                "MEDICINA", "Medicina", "+50 Salud",
                CategoriaItem.SALUD_ASEO, 25,
                0, 0, 0, 0, 50, 0, 0, "item_medicina.png"));

        catalogo.add(crearConsumible(
                "KIT_FICHAS", "Kit de Fichas", "+25 Inteligencia, +20 XP",
                CategoriaItem.ACADEMICO, 30,
                0, 0, 0, 0, 0, 25, 20, "item_kit_fichas.png"));

        return catalogo;
    }

    public static LinkedList<Item> crearArmarioInicial() {
        LinkedList<Item> armario = new LinkedList<>();
        armario.add(crearAccesorio(
                "ACC_CARNE", "Carné Estudiantil",
                "Se desbloquea al alcanzar el Nivel 1.",
                false, false, "acc_carne.png"));
        armario.add(crearAccesorio(
                "ACC_GORRA", "Gorra de la Universidad",
                "Se desbloquea al comprar 5 veces en la Tienda.",
                false, false, "acc_gorra.png"));
        armario.add(crearAccesorio(
                "ACC_GAFAS", "Gafas Intelectuales",
                "Se desbloquea al llegar a 80 de Inteligencia.",
                false, false, "acc_gafas.png"));
        armario.add(crearAccesorio(
                "ACC_BUZO", "Buzo Tadeísta",
                "Se desbloquea al acumular 3 Victorias en cualquier minijuego.",
                false, false, "acc_buzo.png"));
        armario.add(crearAccesorio(
                "ACC_BIRRETE", "Birrete de Grado",
                "Se desbloquea al alcanzar el Nivel 4.",
                false, false, "acc_birrete.png"));
        return armario;
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

    public TipoItem getTipo() {
        return tipo;
    }

    public void setTipo(TipoItem tipo) {
        this.tipo = tipo;
    }

    public CategoriaItem getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaItem categoria) {
        this.categoria = categoria;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = Math.max(0, precio);
    }

    public int getBonoHambre() {
        return bonoHambre;
    }

    public int getBonoEnergia() {
        return bonoEnergia;
    }

    public int getBonoHigiene() {
        return bonoHigiene;
    }

    public int getBonoFelicidad() {
        return bonoFelicidad;
    }

    public int getBonoSalud() {
        return bonoSalud;
    }

    public int getBonoInteligencia() {
        return bonoInteligencia;
    }

    public int getBonoXp() {
        return bonoXp;
    }

    public boolean isDesbloqueado() {
        return desbloqueado;
    }

    public void setDesbloqueado(boolean desbloqueado) {
        this.desbloqueado = desbloqueado;
        if (!desbloqueado) {
            this.equipado = false;
        }
    }

    public boolean isEquipado() {
        return equipado;
    }

    public void setEquipado(boolean equipado) {
        this.equipado = this.desbloqueado && equipado;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }
}
