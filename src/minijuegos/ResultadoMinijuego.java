package minijuegos;

public record ResultadoMinijuego(
        int puntaje,
        int recompensaMonedas,
        int recompensaXp,
        boolean esVictoria,
        boolean otorgaManzana) {

    public ResultadoMinijuego(int puntaje, int recompensaMonedas, int recompensaXp, boolean esVictoria) {
        this(puntaje, recompensaMonedas, recompensaXp, esVictoria, false);
    }
}

