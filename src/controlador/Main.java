package controlador;

import javax.swing.SwingUtilities;
import modelo.Partida;
import persistencia.GestorArchivos;
import vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Partida partida = GestorArchivos.cargarPartida();
            if (partida == null) {
                partida = new Partida("Cabito");
            }

            VentanaPrincipal vista = new VentanaPrincipal();
            Controlador controlador = new Controlador(partida, vista);
            controlador.iniciar();
        });
    }
}

