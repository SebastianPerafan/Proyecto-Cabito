package minijuegos;

import java.util.function.Consumer;

public interface Minijuego {

    void iniciar();

    boolean estaTerminado();

    void setOnTerminado(Consumer<ResultadoMinijuego> callback);
}

