package modelo;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import minijuegos.ResultadoMinijuego;

public class Partida {

    public static final int NIVEL_MAXIMO = 5;
    private static final int[] UMBRALES_XP = {0, 100, 250, 450, 700, 1000};

    private String nombrePartida;
    private Cabito cabito;
    private int nivel;
    private int experiencia;
    private int monedas;
    private Mundo mundoActual;
    private int comprasRealizadas;
    private int victoriasMinijuegos;

    private final Map<Mundo, Boolean> mundosDesbloqueados;
    private final LinkedList<Item> consumibles;
    private final LinkedList<Item> armario;
    private final LinkedList<Logro> logros;

    public Partida(String nombrePartida) {
        this.nombrePartida = (nombrePartida == null || nombrePartida.isBlank()) ? "Estudiante Tadeísta" : nombrePartida.trim();
        this.cabito = new Cabito();
        this.nivel = 0;
        this.experiencia = 0;
        this.monedas = 30;
        this.mundoActual = Mundo.PLAZOLETA;
        this.comprasRealizadas = 0;
        this.victoriasMinijuegos = 0;

        this.mundosDesbloqueados = new EnumMap<>(Mundo.class);
        this.mundosDesbloqueados.put(Mundo.PLAZOLETA, true);
        this.mundosDesbloqueados.put(Mundo.BIBLIOTECA, false);
        this.mundosDesbloqueados.put(Mundo.AUDITORIO, false);

        this.consumibles = new LinkedList<>();
        this.armario = Item.crearArmarioInicial();
        this.logros = Logro.crearLogrosIniciales();
    }

    public void ganarExperiencia(int cantidadXp) {
        if (cantidadXp <= 0) {
            return;
        }
        this.experiencia += cantidadXp;
        verificarSubidaNivel();
        verificarLogros();
    }

    private void verificarSubidaNivel() {
        while (this.nivel < NIVEL_MAXIMO && this.experiencia >= UMBRALES_XP[this.nivel + 1]) {
            this.nivel++;
            aplicarRecompensaNivel(this.nivel);
        }
    }

    private void aplicarRecompensaNivel(int nuevoNivel) {
        switch (nuevoNivel) {
            case 1 -> agregarMonedas(15);
            case 2 -> mundosDesbloqueados.put(Mundo.BIBLIOTECA, true);
            case 3 -> agregarMonedas(20);
            case 4 -> mundosDesbloqueados.put(Mundo.AUDITORIO, true);
            case 5 -> agregarMonedas(50);
            default -> {
            }
        }
    }

    public void verificarLogros() {
        for (Logro logro : logros) {
            if (logro.isDesbloqueado()) {
                continue;
            }
            boolean cumplido = switch (logro.getId()) {
                case "LOGRO_1" -> this.nivel >= 1;
                case "LOGRO_2" -> this.comprasRealizadas >= 5;
                case "LOGRO_3" -> this.cabito.getInteligencia() >= 80;
                case "LOGRO_4" -> this.victoriasMinijuegos >= 3;
                case "LOGRO_5" -> this.nivel >= 4;
                default -> false;
            };
            if (cumplido) {
                logro.setDesbloqueado(true);
                desbloquearAccesorioEnArmario(logro.getIdAccesorioRecompensa());
            }
        }
    }

    private void desbloquearAccesorioEnArmario(String idAccesorio) {
        if (idAccesorio == null) {
            return;
        }
        for (Item prenda : armario) {
            if (idAccesorio.equalsIgnoreCase(prenda.getId())) {
                prenda.setDesbloqueado(true);
                break;
            }
        }
    }

    public boolean comprarConsumible(Item itemCatalogo) {
        if (itemCatalogo == null || itemCatalogo.getTipo() != TipoItem.CONSUMIBLE) {
            return false;
        }
        if (this.monedas < itemCatalogo.getPrecio()) {
            return false;
        }
        this.monedas -= itemCatalogo.getPrecio();
        this.consumibles.add(itemCatalogo.copiar());
        this.comprasRealizadas++;
        verificarLogros();
        return true;
    }

    public boolean usarConsumible(Item itemInventario) {
        if (itemInventario == null || !this.consumibles.remove(itemInventario)) {
            return false;
        }
        this.cabito.aplicarConsumible(itemInventario);
        if (itemInventario.getBonoXp() > 0) {
            ganarExperiencia(itemInventario.getBonoXp());
        } else {
            verificarLogros();
        }
        return true;
    }

    public boolean alternarEquipadoAccesorio(String idAccesorio) {
        Item objetivo = null;
        for (Item prenda : armario) {
            if (prenda.getId().equalsIgnoreCase(idAccesorio)) {
                objetivo = prenda;
                break;
            }
        }
        if (objetivo == null || !objetivo.isDesbloqueado()) {
            return false;
        }
        boolean nuevoEstado = !objetivo.isEquipado();
        if (nuevoEstado) {
            for (Item prenda : armario) {
                prenda.setEquipado(false);
            }
        }
        objetivo.setEquipado(nuevoEstado);
        return true;
    }

    public void agregarMonedas(int cantidad) {
        this.monedas = Math.max(0, this.monedas + cantidad);
    }

    public void registrarVictoriaMinijuego() {
        this.victoriasMinijuegos++;
        verificarLogros();
    }

    public boolean iniciarMinijuego() {
        if (!this.cabito.puedeJugarMinijuego()) {
            return false;
        }
        this.cabito.setEnergia(this.cabito.getEnergia() - 15);
        this.cabito.actualizarEstadoAutomatico();
        return true;
    }

    public void aplicarResultadoMinijuego(ResultadoMinijuego resultado) {
        if (resultado == null) {
            return;
        }
        agregarMonedas(resultado.recompensaMonedas());
        if (resultado.otorgaManzana()) {
            this.consumibles.add(Item.crearConsumible(
                    "MANZANA", "Manzana", "+20 Hambre",
                    CategoriaItem.COMIDA, 10,
                    20, 0, 0, 0, 0, 0, 0, "item_manzana.png"));
        }
        if (resultado.esVictoria()) {
            this.victoriasMinijuegos++;
        }
        if (resultado.recompensaXp() > 0) {
            ganarExperiencia(resultado.recompensaXp());
        } else {
            verificarLogros();
        }
    }

    public boolean iniciarExploracion() {
        if (!this.cabito.puedeExplorar()) {
            return false;
        }
        this.cabito.setEnergia(this.cabito.getEnergia() - 10);
        this.cabito.actualizarEstadoAutomatico();
        return true;
    }

    public boolean procesarResultadoEvento(Evento evento, int indiceSeleccionado) {
        if (evento == null) {
            return false;
        }
        boolean acertado = evento.evaluarRespuesta(indiceSeleccionado);
        if (acertado) {
            agregarMonedas(evento.getRecompensaMonedas());
            this.cabito.setInteligencia(this.cabito.getInteligencia() + evento.getBonoInteligencia());
            this.cabito.actualizarEstadoAutomatico();
            ganarExperiencia(evento.getRecompensaXp());
        } else {
            agregarMonedas(-evento.getPenalizacionMonedas());
            ganarExperiencia(evento.getConsolacionXp());
        }
        return acertado;
    }

    public boolean estaMundoDesbloqueado(Mundo mundo) {
        return mundosDesbloqueados.getOrDefault(mundo, false);
    }

    public void setMundoDesbloqueado(Mundo mundo, boolean desbloqueado) {
        if (mundo != null) {
            mundosDesbloqueados.put(mundo, desbloqueado);
        }
    }

    public int getXpSiguienteNivel() {
        if (this.nivel >= NIVEL_MAXIMO) {
            return UMBRALES_XP[NIVEL_MAXIMO];
        }
        return UMBRALES_XP[this.nivel + 1];
    }

    public String getNombrePartida() {
        return nombrePartida;
    }

    public void setNombrePartida(String nombrePartida) {
        if (nombrePartida != null && !nombrePartida.isBlank()) {
            this.nombrePartida = nombrePartida.trim();
        }
    }

    public Cabito getCabito() {
        return cabito;
    }

    public void setCabito(Cabito cabito) {
        if (cabito != null) {
            this.cabito = cabito;
        }
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = Math.max(0, Math.min(NIVEL_MAXIMO, nivel));
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = Math.max(0, experiencia);
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = Math.max(0, monedas);
    }

    public Mundo getMundoActual() {
        return mundoActual;
    }

    public void setMundoActual(Mundo mundoActual) {
        if (mundoActual != null && estaMundoDesbloqueado(mundoActual)) {
            this.mundoActual = mundoActual;
        }
    }

    public void forzarMundoActual(Mundo mundoActual) {
        if (mundoActual != null) {
            this.mundoActual = mundoActual;
        }
    }

    public int getComprasRealizadas() {
        return comprasRealizadas;
    }

    public void setComprasRealizadas(int comprasRealizadas) {
        this.comprasRealizadas = Math.max(0, comprasRealizadas);
    }

    public int getVictoriasMinijuegos() {
        return victoriasMinijuegos;
    }

    public void setVictoriasMinijuegos(int victoriasMinijuegos) {
        this.victoriasMinijuegos = Math.max(0, victoriasMinijuegos);
    }

    public LinkedList<Item> getConsumibles() {
        return consumibles;
    }

    public LinkedList<Item> getArmario() {
        return armario;
    }

    public LinkedList<Logro> getLogros() {
        return logros;
    }
}
