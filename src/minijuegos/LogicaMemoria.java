package minijuegos;

import java.util.Random;
import java.util.function.Consumer;

public class LogicaMemoria implements Minijuego {

    public static final int FILAS = 4;
    public static final int COLUMNAS = 4;
    public static final int TOTAL_PAREJAS = 8;
    public static final int INTENTOS_INICIALES = 14;

    private static final String[] PALABRAS_CLAVE = {
        "Biblioteca",
        "Auditorio",
        "Carné",
        "Gorra",
        "Cabito",
        "Plazoleta",
        "Gafas",
        "Buzo"
    };

    private final String[][] tablero;
    private final boolean[][] reveladas;
    private int intentosRestantes;
    private int parejasEncontradas;
    private boolean terminado;
    private Consumer<ResultadoMinijuego> onTerminado;
    private final Random random;

    public LogicaMemoria() {
        this(new Random());
    }

    public LogicaMemoria(Random random) {
        this.tablero = new String[FILAS][COLUMNAS];
        this.reveladas = new boolean[FILAS][COLUMNAS];
        this.random = (random != null) ? random : new Random();
        this.intentosRestantes = INTENTOS_INICIALES;
        this.parejasEncontradas = 0;
        this.terminado = false;
    }

    @Override
    public void iniciar() {
        this.intentosRestantes = INTENTOS_INICIALES;
        this.parejasEncontradas = 0;
        this.terminado = false;
        inicializarTablero();
    }

    public void inicializarTablero() {
        int indicePalabra = 0;
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                this.tablero[f][c] = PALABRAS_CLAVE[indicePalabra / 2];
                this.reveladas[f][c] = false;
                indicePalabra++;
            }
        }

        for (int f = FILAS - 1; f >= 0; f--) {
            for (int c = COLUMNAS - 1; c >= 0; c--) {
                int filaAleatoria = random.nextInt(f + 1);
                int colAleatoria = random.nextInt(c + 1);

                String temporal = this.tablero[f][c];
                this.tablero[f][c] = this.tablero[filaAleatoria][colAleatoria];
                this.tablero[filaAleatoria][colAleatoria] = temporal;
            }
        }
    }

    public boolean procesarJugada(int f1, int c1, int f2, int c2) {
        if (this.terminado) {
            return false;
        }
        if (!esPosicionValida(f1, c1) || !esPosicionValida(f2, c2)) {
            return false;
        }
        if (f1 == f2 && c1 == c2) {
            return false;
        }
        if (this.reveladas[f1][c1] || this.reveladas[f2][c2]) {
            return false;
        }

        boolean esPareja = this.tablero[f1][c1].equals(this.tablero[f2][c2]);
        if (esPareja) {
            this.reveladas[f1][c1] = true;
            this.reveladas[f2][c2] = true;
            this.parejasEncontradas++;
        } else {
            this.intentosRestantes = Math.max(0, this.intentosRestantes - 1);
        }

        verificarFinDeJuego();
        return esPareja;
    }

    public void verificarFinDeJuego() {
        if (this.terminado) {
            return;
        }

        if (this.parejasEncontradas >= TOTAL_PAREJAS) {
            this.terminado = true;
            notificarResultado(new ResultadoMinijuego(this.parejasEncontradas, 10, 40, true));
        } else if (this.intentosRestantes <= 0) {
            this.terminado = true;
            notificarResultado(new ResultadoMinijuego(this.parejasEncontradas, 0, 10, false));
        }
    }

    private void notificarResultado(ResultadoMinijuego resultado) {
        if (this.onTerminado != null) {
            this.onTerminado.accept(resultado);
        }
    }

    private boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS;
    }

    @Override
    public boolean estaTerminado() {
        return terminado;
    }

    @Override
    public void setOnTerminado(Consumer<ResultadoMinijuego> callback) {
        this.onTerminado = callback;
    }

    public String getValorCarta(int fila, int columna) {
        if (!esPosicionValida(fila, columna)) {
            return "";
        }
        return tablero[fila][columna];
    }

    public boolean estaRevelada(int fila, int columna) {
        if (!esPosicionValida(fila, columna)) {
            return false;
        }
        return reveladas[fila][columna];
    }

    public String[][] getTablero() {
        return tablero;
    }

    public boolean[][] getReveladas() {
        return reveladas;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    public int getParejasEncontradas() {
        return parejasEncontradas;
    }
}

