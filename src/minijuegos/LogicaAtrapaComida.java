package minijuegos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Consumer;

public class LogicaAtrapaComida implements Minijuego {

    public static final int ANCHO_TABLERO = 400;
    public static final int ALTO_TABLERO = 400;
    public static final int ALTURA_CAPTURA = 350;
    public static final int RADIO_COLISION = 30;
    public static final int DURACION_INICIAL_MILLIS = 30000;
    public static final int PASO_MOVIMIENTO_CABITO = 20;
    public static final int INTERVALO_MAXIMO_SPAWN_MILLIS = 700;

    private final LinkedList<ObjetoComida> comidasActivas;
    private int posicionXCabito;
    private int puntaje;
    private int tiempoRestanteMillis;
    private int tiempoDesdeUltimoSpawnMillis;
    private boolean terminado;
    private Consumer<ResultadoMinijuego> onTerminado;
    private final Random random;

    public static class ObjetoComida {

        private int x;
        private int y;
        private final String tipo;
        private final int puntos;
        private final int velocidadY;

        public ObjetoComida(int x, int y, String tipo, int puntos, int velocidadY) {
            this.x = x;
            this.y = y;
            this.tipo = tipo;
            this.puntos = puntos;
            this.velocidadY = velocidadY;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getTipo() {
            return tipo;
        }

        public int getPuntos() {
            return puntos;
        }

        public int getVelocidadY() {
            return velocidadY;
        }
    }

    public LogicaAtrapaComida() {
        this(new Random());
    }

    public LogicaAtrapaComida(Random random) {
        this.comidasActivas = new LinkedList<>();
        this.random = (random != null) ? random : new Random();
        this.posicionXCabito = ANCHO_TABLERO / 2;
        this.puntaje = 0;
        this.tiempoRestanteMillis = DURACION_INICIAL_MILLIS;
        this.tiempoDesdeUltimoSpawnMillis = 0;
        this.terminado = false;
    }

    @Override
    public void iniciar() {
        this.comidasActivas.clear();
        this.posicionXCabito = ANCHO_TABLERO / 2;
        this.puntaje = 0;
        this.tiempoRestanteMillis = DURACION_INICIAL_MILLIS;
        this.tiempoDesdeUltimoSpawnMillis = 0;
        this.terminado = false;
    }

    public void moverIzquierda() {
        if (this.terminado) {
            return;
        }
        this.posicionXCabito = Math.max(0, this.posicionXCabito - PASO_MOVIMIENTO_CABITO);
    }

    public void moverDerecha() {
        if (this.terminado) {
            return;
        }
        this.posicionXCabito = Math.min(ANCHO_TABLERO, this.posicionXCabito + PASO_MOVIMIENTO_CABITO);
    }

    public void actualizarTick(int deltaMillis) {
        if (this.terminado || deltaMillis <= 0) {
            return;
        }

        this.tiempoRestanteMillis = Math.max(0, this.tiempoRestanteMillis - deltaMillis);
        if (this.tiempoRestanteMillis <= 0) {
            verificarFinDeJuego();
            return;
        }

        this.tiempoDesdeUltimoSpawnMillis += deltaMillis;
        if (this.tiempoDesdeUltimoSpawnMillis >= INTERVALO_MAXIMO_SPAWN_MILLIS || random.nextInt(100) < 8) {
            generarNuevaComida();
            this.tiempoDesdeUltimoSpawnMillis = 0;
        }

        Iterator<ObjetoComida> iterator = this.comidasActivas.iterator();
        while (iterator.hasNext()) {
            ObjetoComida comida = iterator.next();
            comida.setY(comida.getY() + comida.getVelocidadY());

            if (comida.getY() >= ALTURA_CAPTURA && comida.getY() < ALTO_TABLERO) {
                int distanciaX = Math.abs(comida.getX() - this.posicionXCabito);
                if (distanciaX <= RADIO_COLISION) {
                    this.puntaje += comida.getPuntos();
                    iterator.remove();
                    continue;
                }
            }

            if (comida.getY() >= ALTO_TABLERO) {
                iterator.remove();
            }
        }
    }

    private void generarNuevaComida() {
        int margen = 20;
        int coordenadaX = margen + random.nextInt(ANCHO_TABLERO - (margen * 2) + 1);
        int selector = random.nextInt(4);

        ObjetoComida nueva = switch (selector) {
            case 0 -> new ObjetoComida(coordenadaX, 0, "Manzana", 10, 6);
            case 1 -> new ObjetoComida(coordenadaX, 0, "Galleta", 10, 6);
            case 2 -> new ObjetoComida(coordenadaX, 0, "Queso", 15, 8);
            default -> new ObjetoComida(coordenadaX, 0, "Pez", 25, 10);
        };

        this.comidasActivas.add(nueva);
    }

    public void verificarFinDeJuego() {
        if (this.terminado) {
            return;
        }
        this.terminado = true;

        int monedas = Math.min(8, this.puntaje / 50);
        boolean victoria = this.puntaje > 150;
        ResultadoMinijuego resultado = new ResultadoMinijuego(this.puntaje, monedas, 30, victoria, victoria);

        if (this.onTerminado != null) {
            this.onTerminado.accept(resultado);
        }
    }

    @Override
    public boolean estaTerminado() {
        return terminado;
    }

    @Override
    public void setOnTerminado(Consumer<ResultadoMinijuego> callback) {
        this.onTerminado = callback;
    }

    public LinkedList<ObjetoComida> getComidasActivas() {
        return comidasActivas;
    }

    public int getPosicionXCabito() {
        return posicionXCabito;
    }

    public void setPosicionXCabito(int posicionXCabito) {
        this.posicionXCabito = Math.max(0, Math.min(ANCHO_TABLERO, posicionXCabito));
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getTiempoRestanteMillis() {
        return tiempoRestanteMillis;
    }

    public int getTiempoRestanteSegundos() {
        return (int) Math.ceil(this.tiempoRestanteMillis / 1000.0);
    }
}

