package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelMemoria extends JPanel {

    public static final int FILAS = 4;
    public static final int COLUMNAS = 4;

    private final JButton btnVolver;
    private final JLabel lblIntentos;
    private final JLabel lblParejas;
    private final JPanel panelTablero;
    private final JButton[][] botonesCartas;
    private boolean tableroBloqueado;

    public PanelMemoria() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(240, 244, 248));
        setPreferredSize(new Dimension(VentanaPrincipal.ANCHO_VENTANA, VentanaPrincipal.ALTO_VENTANA));

        JPanel panelNorte = new JPanel(new BorderLayout(15, 0));
        panelNorte.setBackground(new Color(21, 67, 96));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        this.btnVolver = new JButton("Volver");
        this.btnVolver.setBackground(new Color(231, 76, 60));
        this.btnVolver.setForeground(Color.WHITE);
        this.btnVolver.setFocusPainted(false);
        this.btnVolver.setFocusable(false);
        this.btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel panelEstadisticas = new JPanel(new GridLayout(1, 2, 20, 0));
        panelEstadisticas.setOpaque(false);

        this.lblIntentos = new JLabel("Intentos restantes: 14", SwingConstants.CENTER);
        this.lblIntentos.setForeground(Color.WHITE);
        this.lblIntentos.setFont(new Font("SansSerif", Font.BOLD, 16));

        this.lblParejas = new JLabel("Parejas: 0 / 8", SwingConstants.CENTER);
        this.lblParejas.setForeground(new Color(241, 196, 15));
        this.lblParejas.setFont(new Font("SansSerif", Font.BOLD, 16));

        panelEstadisticas.add(this.lblIntentos);
        panelEstadisticas.add(this.lblParejas);

        panelNorte.add(this.btnVolver, BorderLayout.WEST);
        panelNorte.add(panelEstadisticas, BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH);

        this.panelTablero = new JPanel(new GridLayout(FILAS, COLUMNAS, 10, 10));
        this.panelTablero.setBackground(new Color(240, 244, 248));
        this.panelTablero.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        this.botonesCartas = new JButton[FILAS][COLUMNAS];
        this.tableroBloqueado = false;

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                JButton boton = new JButton("?");
                boton.setFont(new Font("Arial", Font.BOLD, 16));
                boton.setFocusable(false);
                boton.setFocusPainted(false);
                boton.setOpaque(true);
                aplicarEstiloCarta(boton, "", false);
                this.botonesCartas[f][c] = boton;
                this.panelTablero.add(boton);
            }
        }

        add(this.panelTablero, BorderLayout.CENTER);
    }

    public void actualizarBoton(int f, int c, String texto, boolean revelada) {
        if (f < 0 || f >= FILAS || c < 0 || c >= COLUMNAS) {
            return;
        }
        JButton boton = this.botonesCartas[f][c];
        aplicarEstiloCarta(boton, texto, revelada);
    }

    private void aplicarEstiloCarta(JButton boton, String texto, boolean revelada) {
        if (revelada) {
            boton.setText(texto != null ? texto : "");
            boton.setEnabled(false);
            boton.setBackground(new Color(212, 239, 223));
            boton.setForeground(new Color(20, 90, 50));
            boton.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2));
        } else {
            boton.setText("?");
            boton.setEnabled(true);
            boton.setBackground(new Color(41, 128, 185));
            boton.setForeground(Color.WHITE);
            boton.setBorder(BorderFactory.createLineBorder(new Color(21, 67, 96), 2));
        }
    }

    public void reiniciarTableroVisual() {
        this.tableroBloqueado = false;
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                aplicarEstiloCarta(this.botonesCartas[f][c], "", false);
            }
        }
        this.panelTablero.revalidate();
        this.panelTablero.repaint();
    }

    public void actualizarEstadisticas(int intentos, int parejas) {
        this.lblIntentos.setText("Intentos restantes: " + intentos);
        this.lblParejas.setText("Parejas: " + parejas + " / 8");
    }

    public boolean isTableroBloqueado() {
        return tableroBloqueado;
    }

    public void setTableroBloqueado(boolean tableroBloqueado) {
        this.tableroBloqueado = tableroBloqueado;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JLabel getLblIntentos() {
        return lblIntentos;
    }

    public JLabel getLblParejas() {
        return lblParejas;
    }

    public JPanel getPanelTablero() {
        return panelTablero;
    }

    public JButton[][] getBotonesCartas() {
        return botonesCartas;
    }
}

