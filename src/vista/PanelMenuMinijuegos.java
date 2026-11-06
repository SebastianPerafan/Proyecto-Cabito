package vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelMenuMinijuegos extends JPanel {

    public static final int ANCHO_PANEL = 800;
    public static final int ALTO_PANEL = 600;

    public static final Color AZUL_PROFUNDO = new Color(30, 51, 90);
    public static final Color AZUL_GORRA_OSCURO = new Color(40, 67, 114);
    public static final Color AZUL_GORRA_MEDIO = new Color(56, 90, 148);
    public static final Color AZUL_GORRA_CLARO = new Color(68, 108, 174);
    public static final Color VERDE_LIMA_MEMORIA = new Color(108, 192, 39);
    public static final Color DORADO_HUESO = new Color(246, 208, 77);
    public static final Color NARANJA_HOVER = new Color(255, 153, 0);
    public static final Color BORDE_CELESTE = new Color(184, 208, 240);

    private final JLabel lblTitulo;
    private final JLabel lblEstado;
    private final JButton btnVolver;
    private final TarjetaMinijuego btnJugarAtrapaComida;
    private final TarjetaMinijuego btnJugarMemoria;

    public PanelMenuMinijuegos() {
        setLayout(null);
        setPreferredSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setSize(ANCHO_PANEL, ALTO_PANEL);
        setBackground(new Color(228, 236, 248));

        JPanel panelCabecera = new JPanel(null);
        panelCabecera.setBounds(0, 0, ANCHO_PANEL, 65);
        panelCabecera.setBackground(AZUL_GORRA_OSCURO);
        panelCabecera.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDE_CELESTE));

        this.lblTitulo = new JLabel("JUEGOS DE CABITO - ARCADE");
        this.lblTitulo.setBounds(20, 15, 340, 35);
        this.lblTitulo.setForeground(Color.WHITE);
        this.lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelCabecera.add(this.lblTitulo);

        this.lblEstado = new JLabel("Energía: 100%  |  Costo: -15", SwingConstants.CENTER);
        this.lblEstado.setBounds(365, 15, 265, 35);
        this.lblEstado.setOpaque(true);
        this.lblEstado.setBackground(AZUL_PROFUNDO);
        this.lblEstado.setForeground(DORADO_HUESO);
        this.lblEstado.setFont(new Font("SansSerif", Font.BOLD, 13));
        this.lblEstado.setBorder(BorderFactory.createLineBorder(BORDE_CELESTE, 1));
        panelCabecera.add(this.lblEstado);

        this.btnVolver = new JButton("Volver al Campus");
        this.btnVolver.setBounds(645, 14, 135, 36);
        this.btnVolver.setBackground(new Color(224, 79, 62));
        this.btnVolver.setForeground(Color.WHITE);
        this.btnVolver.setFont(new Font("SansSerif", Font.BOLD, 12));
        this.btnVolver.setFocusPainted(false);
        this.btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelCabecera.add(this.btnVolver);

        add(panelCabecera);

        JLabel lblSubtitulo = new JLabel(
                "Selecciona una consola para entrenar a Cabito, ganar Monedas, XP y desbloquear Logros",
                SwingConstants.CENTER
        );
        lblSubtitulo.setBounds(40, 80, 720, 30);
        lblSubtitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblSubtitulo.setForeground(AZUL_PROFUNDO);
        add(lblSubtitulo);

        this.btnJugarAtrapaComida = new TarjetaMinijuego(
                "ATRAPA LA COMIDA",
                "Usa ◄ ► o A/D para atrapar alimentos en 30s",
                AZUL_GORRA_CLARO,
                true
        );
        this.btnJugarAtrapaComida.setBounds(45, 125, 335, 370);
        add(this.btnJugarAtrapaComida);

        this.btnJugarMemoria = new TarjetaMinijuego(
                "MEMORIA TADEÍSTA",
                "Encuentra las 8 parejas en máximo 14 intentos",
                VERDE_LIMA_MEMORIA,
                false
        );
        this.btnJugarMemoria.setBounds(420, 125, 335, 370);
        add(this.btnJugarMemoria);
    }

    public void actualizarEnergia(int energiaActual) {
        this.lblEstado.setText("Energía: " + energiaActual + "%  |  Costo: -15");
    }

    public void actualizarEnergia(int energiaActual, int saludActual) {
        this.lblEstado.setText("Energía: " + energiaActual + "%  |  Costo: -15");
    }

    public JButton getBtnJugarAtrapaComida() {
        return btnJugarAtrapaComida;
    }

    public JButton getBtnJugarMemoria() {
        return btnJugarMemoria;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    private static class TarjetaMinijuego extends JButton {

        private final String tituloBanner;
        private final String descripcionCorta;
        private final Color colorBanner;
        private final boolean esAtrapaComida;
        private boolean hover;

        public TarjetaMinijuego(
                String tituloBanner,
                String descripcionCorta,
                Color colorBanner,
                boolean esAtrapaComida) {
            super("");
            this.tituloBanner = tituloBanner;
            this.descripcionCorta = descripcionCorta;
            this.colorBanner = colorBanner;
            this.esAtrapaComida = esAtrapaComida;
            this.hover = false;

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isEnabled()) {
                        hover = true;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(20, 35, 65, hover ? 50 : 28));
            g2.fillRoundRect(5, 7, w - 8, h - 9, 26, 26);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(2, 2, w - 6, h - 6, 24, 24);

            g2.setColor(hover ? DORADO_HUESO : BORDE_CELESTE);
            g2.setStroke(new BasicStroke(hover ? 3.0f : 2.0f));
            g2.drawRoundRect(2, 2, w - 6, h - 6, 24, 24);

            dibujarBannerSuperior(g2);
            dibujarConsolaPreview(g2);
            dibujarInsigniaCircular(g2);
            dibujarBotonInferior(g2);

            g2.dispose();
        }

        private void dibujarBannerSuperior(Graphics2D g2) {
            int x = 25;
            int y = 10;
            int w = 285;
            int h = 38;

            Color colorArriba = hover ? this.colorBanner.brighter() : this.colorBanner;
            Color colorAbajo = this.colorBanner.darker();
            g2.setPaint(new GradientPaint(0, y, colorArriba, 0, y + h, colorAbajo));
            g2.fillRoundRect(x, y, w, h, 18, 18);

            g2.setColor(AZUL_PROFUNDO);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRoundRect(x, y, w, h, 18, 18);

            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            int tx = x + (w - fm.stringWidth(this.tituloBanner)) / 2;
            int ty = y + (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(new Color(0, 0, 0, 70));
            g2.drawString(this.tituloBanner, tx + 1, ty + 1);
            g2.setColor(Color.WHITE);
            g2.drawString(this.tituloBanner, tx, ty);
        }

        private void dibujarConsolaPreview(Graphics2D g2) {
            g2.setColor(new Color(34, 37, 42));
            g2.fillRoundRect(10, 58, 315, 210, 32, 32);

            g2.setColor(new Color(85, 91, 102));
            g2.setStroke(new BasicStroke(3.0f));
            g2.drawRoundRect(10, 58, 315, 210, 32, 32);

            g2.setColor(new Color(18, 20, 24));
            g2.fillRoundRect(16, 145, 4, 36, 3, 3);
            g2.setColor(new Color(70, 195, 120));
            g2.fillOval(314, 159, 6, 6);

            Shape clipOriginal = g2.getClip();
            RoundRectangle2D.Double areaPantalla = new RoundRectangle2D.Double(26, 70, 283, 186, 18, 18);
            g2.clip(areaPantalla);

            if (this.esAtrapaComida) {
                dibujarEscenaAtrapaComida(g2);
            } else {
                dibujarEscenaMemoria(g2);
            }

            g2.setClip(clipOriginal);

            g2.setColor(BORDE_CELESTE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(areaPantalla);
        }

        private void dibujarEscenaAtrapaComida(Graphics2D g2) {
            GradientPaint cielo = new GradientPaint(
                    0, 70, new Color(115, 175, 235),
                    0, 226, new Color(215, 238, 255)
            );
            g2.setPaint(cielo);
            g2.fillRect(26, 70, 283, 186);

            g2.setColor(new Color(255, 255, 255, 190));
            g2.fillRoundRect(88, 82, 48, 16, 12, 12);
            g2.fillRoundRect(225, 88, 54, 16, 12, 12);

            g2.setColor(new Color(210, 192, 172));
            g2.fillRect(185, 178, 92, 52);
            g2.setColor(AZUL_GORRA_CLARO);
            for (int i = 0; i < 4; i++) {
                g2.fillRect(193 + i * 20, 186, 13, 20);
            }

            g2.setColor(new Color(88, 182, 92));
            g2.fillRect(26, 226, 283, 30);
            g2.setColor(new Color(65, 150, 70));
            g2.fillRect(26, 226, 283, 4);

            g2.setColor(AZUL_PROFUNDO);
            g2.fillRoundRect(32, 78, 38, 170, 10, 10);
            g2.setColor(DORADO_HUESO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 9));
            g2.drawString("PTS", 41, 93);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString("120", 41, 106);

            g2.setColor(AZUL_GORRA_OSCURO);
            g2.fillRoundRect(44, 114, 14, 124, 7, 7);
            g2.setColor(new Color(46, 204, 113));
            g2.fillRoundRect(44, 148, 14, 90, 7, 7);

            g2.setColor(new Color(255, 255, 255, 170));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(118, 98, 118, 106);
            g2.setColor(new Color(231, 76, 60));
            g2.fillOval(107, 107, 22, 20);
            g2.setColor(new Color(46, 204, 113));
            g2.fillOval(118, 103, 7, 5);

            g2.setColor(new Color(255, 255, 255, 170));
            g2.drawLine(180, 122, 180, 130);
            g2.setColor(new Color(211, 84, 0));
            g2.fillRoundRect(170, 131, 19, 21, 5, 5);
            g2.setColor(Color.WHITE);
            g2.fillRect(172, 138, 15, 6);

            g2.setColor(new Color(255, 255, 255, 170));
            g2.drawLine(246, 102, 246, 110);
            g2.setColor(AZUL_GORRA_OSCURO);
            g2.fillRoundRect(235, 111, 22, 18, 4, 4);
            g2.setColor(DORADO_HUESO);
            g2.fillRect(239, 115, 14, 3);

            g2.setColor(AZUL_GORRA_OSCURO);
            g2.fillRoundRect(142, 226, 72, 22, 10, 10);
            g2.setColor(DORADO_HUESO);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRoundRect(142, 226, 72, 22, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 9));
            g2.drawString("◄ CABITO ►", 150, 240);
        }

        private void dibujarEscenaMemoria(Graphics2D g2) {
            GradientPaint tablero = new GradientPaint(
                    0, 70, new Color(122, 92, 24),
                    0, 256, new Color(92, 68, 16)
            );
            g2.setPaint(tablero);
            g2.fillRect(26, 70, 283, 186);

            g2.setColor(AZUL_PROFUNDO);
            g2.fillRoundRect(32, 78, 38, 170, 10, 10);
            g2.setColor(DORADO_HUESO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 8));
            g2.drawString("PARES", 37, 93);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString("6/8", 43, 106);

            g2.setColor(AZUL_GORRA_OSCURO);
            g2.fillRoundRect(44, 114, 14, 124, 7, 7);
            g2.setColor(VERDE_LIMA_MEMORIA);
            g2.fillRoundRect(44, 138, 14, 100, 7, 7);

            int inicioX = 90;
            int inicioY = 88;
            int tamCarta = 38;
            int gap = 6;

            String[][] simbolos = {
                {"★", "?", "★", "?"},
                {"?", "♫", "?", "♫"},
                {"♦", "?", "?", "♦"}
            };
            boolean[][] volteadas = {
                {true, false, true, false},
                {false, true, false, true},
                {true, false, false, true}
            };

            for (int f = 0; f < 3; f++) {
                for (int c = 0; c < 4; c++) {
                    int cx = inicioX + c * (tamCarta + gap);
                    int cy = inicioY + f * (tamCarta + gap);

                    if (volteadas[f][c]) {
                        g2.setColor(Color.WHITE);
                        g2.fillRoundRect(cx, cy, tamCarta, tamCarta, 8, 8);
                        g2.setColor(VERDE_LIMA_MEMORIA);
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawRoundRect(cx, cy, tamCarta, tamCarta, 8, 8);

                        g2.setColor(AZUL_GORRA_OSCURO);
                        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                        FontMetrics fm = g2.getFontMetrics();
                        String sim = simbolos[f][c];
                        g2.drawString(sim, cx + (tamCarta - fm.stringWidth(sim)) / 2, cy + 25);
                    } else {
                        g2.setColor(AZUL_GORRA_OSCURO);
                        g2.fillRoundRect(cx, cy, tamCarta, tamCarta, 8, 8);
                        g2.setColor(BORDE_CELESTE);
                        g2.setStroke(new BasicStroke(1.5f));
                        g2.drawRoundRect(cx, cy, tamCarta, tamCarta, 8, 8);

                        g2.setColor(DORADO_HUESO);
                        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString("?", cx + (tamCarta - fm.stringWidth("?")) / 2, cy + 25);
                    }
                }
            }
        }

        private void dibujarInsigniaCircular(Graphics2D g2) {
            int x = 285;
            int y = 230;
            int d = 36;

            Ellipse2D.Double circulo = new Ellipse2D.Double(x, y, d, d);
            g2.setColor(hover ? NARANJA_HOVER : DORADO_HUESO);
            g2.fill(circulo);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.5f));
            g2.draw(circulo);

            g2.setColor(AZUL_PROFUNDO);
            Polygon play = new Polygon(
                    new int[]{x + 14, x + 14, x + 26},
                    new int[]{y + 10, y + 26, y + 18},
                    3
            );
            g2.fill(play);
        }

        private void dibujarBotonInferior(Graphics2D g2) {
            g2.setColor(AZUL_PROFUNDO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            FontMetrics fmDesc = g2.getFontMetrics();
            int xDesc = (getWidth() - fmDesc.stringWidth(this.descripcionCorta)) / 2;
            g2.drawString(this.descripcionCorta, Math.max(10, xDesc), 286);

            int x = 35;
            int y = 300;
            int w = 265;
            int h = 44;

            Color sup = hover ? DORADO_HUESO : AZUL_GORRA_MEDIO;
            Color inf = hover ? NARANJA_HOVER : AZUL_GORRA_OSCURO;
            g2.setPaint(new GradientPaint(0, y, sup, 0, y + h, inf));
            g2.fillRoundRect(x, y, w, h, 36, 36);

            g2.setColor(AZUL_PROFUNDO);
            g2.setStroke(new BasicStroke(2.2f));
            g2.drawRoundRect(x, y, w, h, 36, 36);

            String textoBoton = "¡JUGAR AHORA! (-15 Energía)";
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            FontMetrics fmBtn = g2.getFontMetrics();
            int tx = x + (w - fmBtn.stringWidth(textoBoton)) / 2;
            int ty = y + (h - fmBtn.getHeight()) / 2 + fmBtn.getAscent();

            g2.setColor(hover ? AZUL_PROFUNDO : Color.WHITE);
            g2.drawString(textoBoton, tx, ty);
        }
    }
}
