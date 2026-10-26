package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelAtrapaComida extends JPanel {

    public static final int ANCHO_LOGICO = 400;
    public static final int ALTO_LOGICO = 400;
    public static final int Y_SUELO_CAPTURA = 350;
    public static final int TAMANO_CABITO = 60;
    public static final int TAMANO_COMIDA = 32;

    private final JButton btnVolver;
    private final JLabel lblPuntaje;
    private final JLabel lblTiempo;
    private final LienzoJuego lienzo;

    private int xCabito;
    private List<String[]> comidasActivas;

    public PanelAtrapaComida() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 238, 246));
        setPreferredSize(new Dimension(VentanaPrincipal.ANCHO_VENTANA, VentanaPrincipal.ALTO_VENTANA));
        setFocusable(true);

        this.xCabito = 200;
        this.comidasActivas = new ArrayList<>();

        JPanel panelNorte = new JPanel(new BorderLayout(15, 0));
        panelNorte.setBackground(new Color(21, 67, 96));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        this.btnVolver = new JButton("Volver");
        this.btnVolver.setBackground(new Color(231, 76, 60));
        this.btnVolver.setForeground(Color.WHITE);
        this.btnVolver.setFocusPainted(false);
        this.btnVolver.setFocusable(false);
        this.btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel panelInfo = new JPanel(new GridLayout(1, 2, 20, 0));
        panelInfo.setOpaque(false);

        this.lblPuntaje = new JLabel("Puntaje: 0 pts", SwingConstants.CENTER);
        this.lblPuntaje.setForeground(new Color(241, 196, 15));
        this.lblPuntaje.setFont(new Font("SansSerif", Font.BOLD, 16));

        this.lblTiempo = new JLabel("Tiempo: 30.0 s", SwingConstants.CENTER);
        this.lblTiempo.setForeground(Color.WHITE);
        this.lblTiempo.setFont(new Font("SansSerif", Font.BOLD, 16));

        panelInfo.add(this.lblPuntaje);
        panelInfo.add(this.lblTiempo);

        panelNorte.add(this.btnVolver, BorderLayout.WEST);
        panelNorte.add(panelInfo, BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH);

        this.lienzo = new LienzoJuego();
        add(this.lienzo, BorderLayout.CENTER);
    }

    public void actualizarLienzo(int xCab, List<String[]> comidas, int puntaje, int tiempoMillis) {
        this.xCabito = xCab;
        this.comidasActivas = (comidas != null) ? new ArrayList<>(comidas) : new ArrayList<>();
        this.lblPuntaje.setText("Puntaje: " + puntaje + " pts");

        double segundos = Math.max(0.0, tiempoMillis / 1000.0);
        this.lblTiempo.setText(String.format("Tiempo: %.1f s", segundos));

        this.lienzo.repaint();
    }

    public class LienzoJuego extends JPanel {

        public LienzoJuego() {
            setBackground(new Color(215, 228, 242));
            setFocusable(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int offsetX = Math.max(0, (getWidth() - ANCHO_LOGICO) / 2);
            int offsetY = Math.max(0, (getHeight() - ALTO_LOGICO) / 2);

            g2.setColor(new Color(235, 245, 255));
            g2.fillRect(offsetX, offsetY, ANCHO_LOGICO, ALTO_LOGICO);

            g2.setColor(new Color(180, 215, 168));
            g2.fillRect(offsetX, offsetY + Y_SUELO_CAPTURA, ANCHO_LOGICO, ALTO_LOGICO - Y_SUELO_CAPTURA);

            g2.setColor(new Color(41, 128, 185));
            g2.drawRect(offsetX, offsetY, ANCHO_LOGICO, ALTO_LOGICO);

            dibujarCabito(g2, offsetX, offsetY);
            dibujarComidas(g2, offsetX, offsetY);

            g2.setColor(new Color(40, 60, 80));
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("Usa las flechas [ <- ] y [ -> ] para mover a Cabito (Meta: >150 pts)", offsetX + 12, offsetY + ALTO_LOGICO + 20);

            g2.dispose();
        }

        private void dibujarCabito(Graphics2D g2, int offsetX, int offsetY) {
            int drawX = offsetX + xCabito - (TAMANO_CABITO / 2);
            int drawY = offsetY + Y_SUELO_CAPTURA - (TAMANO_CABITO / 2);

            Image imgCabito = GestorRecursos.obtenerImagenEscalada("cabito_jugando.png", TAMANO_CABITO, TAMANO_CABITO);
            if (imgCabito == null) {
                imgCabito = GestorRecursos.obtenerImagenEscalada("cabito_feliz.png", TAMANO_CABITO, TAMANO_CABITO);
            }

            if (imgCabito != null) {
                g2.drawImage(imgCabito, drawX, drawY, this);
            } else {
                g2.setColor(new Color(211, 84, 0));
                g2.fillOval(drawX, drawY, TAMANO_CABITO, TAMANO_CABITO);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                g2.drawString("CABITO", drawX + 9, drawY + 34);
            }
        }

        private void dibujarComidas(Graphics2D g2, int offsetX, int offsetY) {
            if (comidasActivas == null) {
                return;
            }

            for (String[] datosComida : comidasActivas) {
                if (datosComida == null || datosComida.length < 3) {
                    continue;
                }
                String tipo = datosComida[0] != null ? datosComida[0] : "Comida";
                int xLogico;
                int yLogico;
                try {
                    xLogico = Integer.parseInt(datosComida[1]);
                    yLogico = Integer.parseInt(datosComida[2]);
                } catch (NumberFormatException e) {
                    continue;
                }

                int drawX = offsetX + xLogico - (TAMANO_COMIDA / 2);
                int drawY = offsetY + yLogico - (TAMANO_COMIDA / 2);

                String archivoImg = obtenerNombreImagenPorTipo(tipo);
                Image imgComida = GestorRecursos.obtenerImagenEscalada(archivoImg, TAMANO_COMIDA, TAMANO_COMIDA);

                if (imgComida != null) {
                    g2.drawImage(imgComida, drawX, drawY, this);
                } else {
                    g2.setColor(obtenerColorPorTipo(tipo));
                    g2.fillRoundRect(drawX, drawY, TAMANO_COMIDA, TAMANO_COMIDA, 10, 10);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                    String inicial = !tipo.isBlank() ? tipo.substring(0, 1).toUpperCase() : "?";
                    g2.drawString(inicial, drawX + 11, drawY + 21);
                }
            }
        }

        private String obtenerNombreImagenPorTipo(String tipo) {
            return switch (tipo.toLowerCase()) {
                case "manzana" -> "item_manzana.png";
                case "galleta" -> "item_galleta.png";
                case "queso" -> "item_queso.png";
                case "pez" -> "item_pez.png";
                default -> "item_manzana.png";
            };
        }

        private Color obtenerColorPorTipo(String tipo) {
            return switch (tipo.toLowerCase()) {
                case "manzana" -> new Color(231, 76, 60);
                case "galleta" -> new Color(186, 135, 89);
                case "queso" -> new Color(241, 196, 15);
                case "pez" -> new Color(52, 152, 219);
                default -> new Color(155, 89, 182);
            };
        }
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JLabel getLblPuntaje() {
        return lblPuntaje;
    }

    public JLabel getLblTiempo() {
        return lblTiempo;
    }

    public LienzoJuego getLienzo() {
        return lienzo;
    }
}

