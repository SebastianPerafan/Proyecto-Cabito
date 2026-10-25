package modelo;

public class Cabito {

    public static final int VALOR_MINIMO = 0;
    public static final int VALOR_MAXIMO = 100;

    private int hambre;
    private int energia;
    private int higiene;
    private int felicidad;
    private int salud;
    private int inteligencia;
    private EstadoCabito estadoActual;

    public Cabito() {
        this.hambre = 80;
        this.energia = 100;
        this.higiene = 80;
        this.felicidad = 80;
        this.salud = 100;
        this.inteligencia = 10;
        this.estadoActual = evaluarEstadoAutomatico();
    }

    public Cabito(int hambre, int energia, int higiene, int felicidad, int salud, int inteligencia, EstadoCabito estado) {
        this.hambre = limitarRango(hambre);
        this.energia = limitarRango(energia);
        this.higiene = limitarRango(higiene);
        this.felicidad = limitarRango(felicidad);
        this.salud = limitarRango(salud);
        this.inteligencia = limitarRango(inteligencia);
        this.estadoActual = (estado != null) ? estado : evaluarEstadoAutomatico();
    }

    public void aplicarDesgasteCiclo() {
        setHambre(this.hambre - 3);
        setEnergia(this.energia - 2);
        setHigiene(this.higiene - 2);
        setFelicidad(this.felicidad - 2);

        if (this.hambre == 0 || this.higiene == 0) {
            setSalud(this.salud - 5);
        }

        if (this.estadoActual == null || !this.estadoActual.esTemporal()) {
            actualizarEstadoAutomatico();
        }
    }

    public EstadoCabito evaluarEstadoAutomatico() {
        if (this.salud <= 25) {
            return EstadoCabito.ENFERMO;
        }
        if (this.hambre <= 25) {
            return EstadoCabito.HAMBRIENTO;
        }
        if (this.energia <= 20) {
            return EstadoCabito.CANSADO;
        }
        if (this.felicidad <= 25 || this.higiene <= 20) {
            return EstadoCabito.TRISTE;
        }
        if (this.felicidad >= 80 && this.hambre >= 70 && this.salud >= 70) {
            return EstadoCabito.FELIZ;
        }
        return EstadoCabito.NORMAL;
    }

    public void actualizarEstadoAutomatico() {
        this.estadoActual = evaluarEstadoAutomatico();
    }

    public void aplicarConsumible(Item item) {
        if (item == null || item.getTipo() != TipoItem.CONSUMIBLE) {
            return;
        }
        setHambre(this.hambre + item.getBonoHambre());
        setEnergia(this.energia + item.getBonoEnergia());
        setHigiene(this.higiene + item.getBonoHigiene());
        setFelicidad(this.felicidad + item.getBonoFelicidad());
        setSalud(this.salud + item.getBonoSalud());
        setInteligencia(this.inteligencia + item.getBonoInteligencia());
        actualizarEstadoAutomatico();
    }

    public boolean puedeExplorar() {
        return !estaEnfermo() && this.energia >= 10;
    }

    public boolean puedeJugarMinijuego() {
        return !estaEnfermo() && this.energia >= 15;
    }

    public boolean estaEnfermo() {
        return this.salud <= 25;
    }

    private int limitarRango(int valor) {
        return Math.max(VALOR_MINIMO, Math.min(VALOR_MAXIMO, valor));
    }

    public int getHambre() {
        return hambre;
    }

    public void setHambre(int hambre) {
        this.hambre = limitarRango(hambre);
    }

    public int getEnergia() {
        return energia;
    }

    public void setEnergia(int energia) {
        this.energia = limitarRango(energia);
    }

    public int getHigiene() {
        return higiene;
    }

    public void setHigiene(int higiene) {
        this.higiene = limitarRango(higiene);
    }

    public int getFelicidad() {
        return felicidad;
    }

    public void setFelicidad(int felicidad) {
        this.felicidad = limitarRango(felicidad);
    }

    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        this.salud = limitarRango(salud);
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = limitarRango(inteligencia);
    }

    public EstadoCabito getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoCabito estadoActual) {
        this.estadoActual = (estadoActual != null) ? estadoActual : evaluarEstadoAutomatico();
    }
}
