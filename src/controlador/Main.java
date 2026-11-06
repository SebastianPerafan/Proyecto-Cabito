package controlador;

import javax.swing.SwingUtilities;
import modelo.Partida;
import vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Partida partidaInicial = new Partida("Cabito", "cabito");
            VentanaPrincipal vista = new VentanaPrincipal();
            Controlador controlador = new Controlador(partidaInicial, vista);
            controlador.iniciar();
        });
    }
}
