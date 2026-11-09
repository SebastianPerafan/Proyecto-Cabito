package vista;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelMemoria extends JPanel {

    public static final int FILAS = 4;
    public static final int COLUMNAS = 4;
    private static final int TOTAL_PAREJAS = 8;

    private static final Color AZUL_REY = new Color(0, 51, 160);
    private static final Color AZUL_REY_OSCURO = new Color(16, 36, 82);
    private static final Color AZUL_CARTA_ARRIBA = new Color(0, 70, 184);
    private static final Color AZUL_CARTA_ABAJO = new Color(0, 26, 80);
    private static final Color AZUL_CARTA_HOVER_ARRIBA = new Color(24, 102, 224);
    private static final Color AZUL_CARTA_HOVER_ABAJO = new Color(8, 44, 118);
    private static final Color AMARILLO_DORADO = new Color(255, 215, 0);
    private static final Color BORDE_CELESTE = new Color(184, 212, 248);
    private static final Color ROJO_CORAL = new Color(224, 79, 62);
    private static final Color ROJO_CORAL_HOVER = new Color(242, 98, 82);
    private static final Color VERDE_CHECK = new Color(39, 174, 96);
    private static final Color CELESTE_CAPSULA = new Color(232, 244, 255);

    private final JButton btnVolver;
    private final JLabel lblIntentos;
    private final JLabel lblParejas;
    private final JPanel panelTablero;
    private final JButton[][] botonesCartas;
    private boolean tableroBloqueado;

    private int intentosActuales;
    private int parejasActuales;

    public PanelMemoria() {
        setLayout(new BorderLayout());
        setBackground(new Color(8, 18, 38));
        setPreferredSize(new Dimension(VentanaPrincipal.ANCHO_VENTANA, VentanaPrincipal.ALTO_VENTANA));

        this.intentosActuales = 8;
        this.parejasActuales = 0;
        this.tableroBloqueado = false;

        this.lblIntentos = new JLabel("Intentos restantes: 8", SwingConstants.CENTER);
        this.lblParejas = new JLabel("Parejas: 0 / 8", SwingConstants.CENTER);

        JPanel panelCabecera = new JPanel(null);
        panelCabecera.setOpaque(false);
        panelCabecera.setPreferredSize(new Dimension(VentanaPrincipal.ANCHO_VENTANA, 72));

        this.btnVolver = new BotonSalirMemoria("Volver / Salir");
        this.btnVolver.setBounds(618, 19, 144, 36);
        panelCabecera.add(this.btnVolver);

        add(panelCabecera, BorderLayout.NORTH);

        this.panelTablero = new JPanel(new GridLayout(FILAS, COLUMNAS, 18, 16));
        this.panelTablero.setOpaque(false);
        this.panelTablero.setBorder(BorderFactory.createEmptyBorder(14, 56, 30, 56));

        this.botonesCartas = new JButton[FILAS][COLUMNAS];

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                BotonCartaMemoria carta = new BotonCartaMemoria();
                this.botonesCartas[f][c] = carta;
                this.panelTablero.add(carta);
            }
        }

        add(this.panelTablero, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = Math.max(1, getWidth());
        int h = Math.max(1, getHeight());

        Image imgFondo = GestorRecursos.obtenerImagenEscalada("fondo_biblioteca2.png", w, h);
        if (imgFondo == null) {
            imgFondo = GestorRecursos.obtenerImagenEscalada("fondo_biblioteca.png", w, h);
        }

        if (imgFondo != null) {
            g2.drawImage(imgFondo, 0, 0, w, h, this);
        } else {
            GradientPaint gpFondo = new GradientPaint(0, 0, new Color(22, 36, 68), 0, h, new Color(10, 18, 36));
            g2.setPaint(gpFondo);
            g2.fillRect(0, 0, w, h);
        }

        g2.setColor(new Color(0, 15, 40, 180));
        g2.fillRect(0, 0, w, h);

        RoundRectangle2D.Float marcoMesa = new RoundRectangle2D.Float(38, 76, w - 76, h - 94, 28, 28);
        g2.setColor(new Color(255, 255, 255, 22));
        g2.fill(marcoMesa);
        g2.setColor(new Color(184, 212, 248, 85));
        g2.setStroke(new BasicStroke(1.6f));
        g2.draw(marcoMesa);

        dibujarHudSuperior(g2, w);

        g2.dispose();
    }

    private void dibujarHudSuperior(Graphics2D g2, int w) {
        int margenX = 24;
        int yBarra = 10;
        int anchoBarra = w - (margenX * 2);
        int altoBarra = 54;

        this.btnVolver.setBounds(w - margenX - 158, yBarra + 9, 144, 36);

        RoundRectangle2D.Float cabecera = new RoundRectangle2D.Float(margenX, yBarra, anchoBarra, altoBarra, 24, 24);
        GradientPaint gpCabecera = new GradientPaint(
                0, yBarra, new Color(0, 51, 160, 238),
                0, yBarra + altoBarra, new Color(12, 32, 92, 242)
        );
        g2.setPaint(gpCabecera);
        g2.fill(cabecera);

        g2.setColor(AMARILLO_DORADO);
        g2.setStroke(new BasicStroke(2.0f));
        g2.draw(cabecera);

        int xPildoraParejas = margenX + 14;
        int yPildora = yBarra + 8;
        int wPildoraParejas = 315;
        int hPildora = 38;

        g2.setColor(AZUL_REY_OSCURO);
        g2.fillRoundRect(xPildoraParejas, yPildora, wPildoraParejas, hPildora, 20, 20);
        g2.setColor(BORDE_CELESTE);
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawRoundRect(xPildoraParejas, yPildora, wPildoraParejas, hPildora, 20, 20);

        g2.setColor(AMARILLO_DORADO);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        String txtParejas = "Parejas: " + this.parejasActuales + " / " + TOTAL_PAREJAS;
        g2.drawString(txtParejas, xPildoraParejas + 16, yPildora + 25);

        int xProg = xPildoraParejas + 158;
        int yProg = yPildora + 11;
        int wProg = 140;
        int hProg = 16;

        g2.setColor(new Color(8, 18, 42));
        g2.fillRoundRect(xProg, yProg, wProg, hProg, 12, 12);

        double fraccion = Math.min(1.0, (double) this.parejasActuales / TOTAL_PAREJAS);
        int anchoLlenado = (int) Math.round(fraccion * (wProg - 4));
        if (anchoLlenado > 0) {
            GradientPaint gpProg = new GradientPaint(
                    xProg, yProg, new Color(82, 224, 132),
                    xProg + wProg, yProg, VERDE_CHECK
            );
            g2.setPaint(gpProg);
            g2.fillRoundRect(xProg + 2, yProg + 2, Math.max(6, anchoLlenado), hProg - 4, 10, 10);
        }

        g2.setColor(new Color(255, 255, 255, 165));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(xProg, yProg, wProg, hProg, 12, 12);

        boolean pocosIntentos = this.intentosActuales <= 2;
        int xPildoraIntentos = xPildoraParejas + wPildoraParejas + 20;
        int wPildoraIntentos = 220;

        Color fondoIntentos = pocosIntentos ? ROJO_CORAL : CELESTE_CAPSULA;
        Color bordeIntentos = pocosIntentos ? AMARILLO_DORADO : new Color(120, 175, 235);
        Color colorTxtIntentos = pocosIntentos ? Color.WHITE : AZUL_REY_OSCURO;

        g2.setColor(fondoIntentos);
        g2.fillRoundRect(xPildoraIntentos, yPildora, wPildoraIntentos, hPildora, 20, 20);
        g2.setColor(bordeIntentos);
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawRoundRect(xPildoraIntentos, yPildora, wPildoraIntentos, hPildora, 20, 20);

        g2.setColor(colorTxtIntentos);
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        String strIntentos = "Intentos restantes: " + this.intentosActuales;
        FontMetrics fmI = g2.getFontMetrics();
        g2.drawString(strIntentos, xPildoraIntentos + (wPildoraIntentos - fmI.stringWidth(strIntentos)) / 2, yPildora + 25);
    }

    public void actualizarBoton(int f, int c, String texto, boolean revelada) {
        if (f < 0 || f >= FILAS || c < 0 || c >= COLUMNAS) {
            return;
        }
        BotonCartaMemoria carta = (BotonCartaMemoria) this.botonesCartas[f][c];
        carta.configurarEstado(texto != null ? texto : "", revelada);
        sincronizarCartasEmparejadas();
        repaint();
    }

    private void sincronizarCartasEmparejadas() {
        for (int f1 = 0; f1 < FILAS; f1++) {
            for (int c1 = 0; c1 < COLUMNAS; c1++) {
                BotonCartaMemoria carta1 = (BotonCartaMemoria) this.botonesCartas[f1][c1];
                if (!carta1.revelada || carta1.valorTexto.isBlank()) {
                    carta1.setEmparejada(false);
                    continue;
                }
                boolean tieneParejaRevelada = false;
                for (int f2 = 0; f2 < FILAS; f2++) {
                    for (int c2 = 0; c2 < COLUMNAS; c2++) {
                        if (f1 == f2 && c1 == c2) {
                            continue;
                        }
                        BotonCartaMemoria carta2 = (BotonCartaMemoria) this.botonesCartas[f2][c2];
                        if (carta2.revelada && carta1.valorTexto.equalsIgnoreCase(carta2.valorTexto)) {
                            tieneParejaRevelada = true;
                            break;
                        }
                    }
                    if (tieneParejaRevelada) {
                        break;
                    }
                }
                carta1.setEmparejada(tieneParejaRevelada);
            }
        }
    }

    public void reiniciarTableroVisual() {
        this.tableroBloqueado = false;
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                BotonCartaMemoria carta = (BotonCartaMemoria) this.botonesCartas[f][c];
                carta.configurarEstado("", false);
                carta.setEmparejada(false);
            }
        }
        this.panelTablero.revalidate();
        this.panelTablero.repaint();
        repaint();
    }

    public void actualizarEstadisticas(int intentos, int parejas) {
        this.intentosActuales = Math.max(0, intentos);
        this.parejasActuales = Math.max(0, parejas);
        this.lblIntentos.setText("Intentos restantes: " + this.intentosActuales);
        this.lblParejas.setText("Parejas: " + this.parejasActuales + " / " + TOTAL_PAREJAS);
        sincronizarCartasEmparejadas();
        repaint();
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

    private static class BotonCartaMemoria extends JButton {

        private String valorTexto;
        private boolean revelada;
        private boolean emparejada;
        private boolean hover;

        public BotonCartaMemoria() {
            super("?");
            this.valorTexto = "";
            this.revelada = false;
            this.emparejada = false;
            this.hover = false;

            setFocusable(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!revelada && isEnabled()) {
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

        public void configurarEstado(String texto, boolean revelada) {
            this.valorTexto = (texto != null) ? texto : "";
            this.revelada = revelada;
            if (!revelada) {
                this.emparejada = false;
                setText("?");
                setEnabled(true);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setText(this.valorTexto);
                setEnabled(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            repaint();
        }

        public void setEmparejada(boolean emparejada) {
            this.emparejada = emparejada;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (!this.revelada) {
                dibujarCartaBocaAbajo(g2, w, h);
            } else {
                dibujarCartaBocaArriba(g2, w, h);
            }

            g2.dispose();
        }

        private void dibujarCartaBocaAbajo(Graphics2D g2, int w, int h) {
            g2.setColor(new Color(0, 8, 24, 115));
            g2.fillRoundRect(4, 6, w - 6, h - 6, 20, 20);

            RoundRectangle2D.Float formaExterior = new RoundRectangle2D.Float(2, 2, w - 5, h - 6, 20, 20);
            Color cArriba = this.hover ? AZUL_CARTA_HOVER_ARRIBA : AZUL_CARTA_ARRIBA;
            Color cAbajo = this.hover ? AZUL_CARTA_HOVER_ABAJO : AZUL_CARTA_ABAJO;
            g2.setPaint(new GradientPaint(0, 2, cArriba, 0, h - 4, cAbajo));
            g2.fill(formaExterior);

            g2.setColor(this.hover ? AMARILLO_DORADO : new Color(90, 155, 240));
            g2.setStroke(new BasicStroke(this.hover ? 2.4f : 1.8f));
            g2.draw(formaExterior);

            RoundRectangle2D.Float bordeInterior = new RoundRectangle2D.Float(7, 7, w - 15, h - 16, 15, 15);
            g2.setColor(new Color(210, 232, 255, this.hover ? 185 : 130));
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(bordeInterior);

            int cx = w / 2;
            int cy = (h - 2) / 2;
            int rCirculo = Math.min(w, h) / 4 + 4;

            g2.setColor(new Color(255, 215, 0, this.hover ? 52 : 30));
            g2.fillOval(cx - rCirculo, cy - rCirculo, rCirculo * 2, rCirculo * 2);

            g2.setFont(new Font("SansSerif", Font.BOLD, 34));
            FontMetrics fm = g2.getFontMetrics();
            int tx = cx - fm.stringWidth("?") / 2;
            int ty = cy - fm.getHeight() / 2 + fm.getAscent();

            g2.setColor(new Color(0, 12, 38, 160));
            g2.drawString("?", tx + 2, ty + 3);

            g2.setColor(AMARILLO_DORADO);
            g2.drawString("?", tx, ty);
        }

        private void dibujarCartaBocaArriba(Graphics2D g2, int w, int h) {
            Composite compOriginal = g2.getComposite();
            if (this.emparejada) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
            }

            g2.setColor(new Color(0, 8, 24, 95));
            g2.fillRoundRect(4, 6, w - 6, h - 6, 20, 20);

            RoundRectangle2D.Float formaCarta = new RoundRectangle2D.Float(2, 2, w - 5, h - 6, 20, 20);
            GradientPaint gpFondo = new GradientPaint(
                    0, 2, new Color(255, 253, 246),
                    0, h - 4, new Color(232, 242, 255)
            );
            g2.setPaint(gpFondo);
            g2.fill(formaCarta);

            g2.setColor(this.emparejada ? VERDE_CHECK : AZUL_REY);
            g2.setStroke(new BasicStroke(2.8f));
            g2.draw(formaCarta);

            int cx = w / 2;
            int cyIcono = (h / 2) - 10;

            dibujarIlustracionPareja(g2, this.valorTexto, cx, cyIcono);

            g2.setColor(AZUL_REY_OSCURO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            int tx = cx - fm.stringWidth(this.valorTexto) / 2;
            g2.drawString(this.valorTexto, Math.max(8, tx), h - 14);

            g2.setComposite(compOriginal);

            if (this.emparejada) {
                int d = 24;
                int bx = w - d - 8;
                int by = 8;
                g2.setColor(new Color(39, 174, 96, 225));
                g2.fillOval(bx, by, d, d);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.2f));
                g2.drawOval(bx, by, d, d);
                g2.drawLine(bx + 6, by + 12, bx + 10, by + 17);
                g2.drawLine(bx + 10, by + 17, bx + 18, by + 7);
            }
        }

        private void dibujarIlustracionPareja(Graphics2D g2, String claveTexto, int cx, int cy) {
            String clave = (claveTexto != null) ? claveTexto.toLowerCase() : "";

            g2.setColor(new Color(212, 228, 252, 135));
            g2.fillOval(cx - 26, cy - 26, 52, 52);

            String archivoPng = switch (clave) {
                case "cabito" -> "cabito_feliz.png";
                case "carné", "carne" -> "acc_carne.png";
                case "gorra" -> "acc_gorra.png";
                case "gafas" -> "acc_gafas.png";
                case "buzo" -> "acc_buzo.png";
                default -> null;
            };

            if (archivoPng != null) {
                Image img = GestorRecursos.obtenerImagenIcono(archivoPng, 46, 46);
                if (img != null) {
                    g2.drawImage(img, cx - 23, cy - 23, 46, 46, this);
                    return;
                }
            }

            if (clave.contains("biblioteca")) {
                g2.setColor(AZUL_CARTA_ARRIBA);
                g2.fillRoundRect(cx - 20, cy - 14, 19, 26, 4, 4);
                g2.fillRoundRect(cx + 1, cy - 14, 19, 26, 4, 4);
                g2.setColor(new Color(255, 250, 235));
                g2.fillRect(cx - 17, cy - 11, 15, 20);
                g2.fillRect(cx + 2, cy - 11, 15, 20);
                g2.setColor(AMARILLO_DORADO);
                g2.fillRect(cx - 1, cy - 14, 2, 27);
                g2.setColor(AZUL_REY_OSCURO);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(cx - 14, cy - 5, cx - 5, cy - 5);
                g2.drawLine(cx - 14, cy + 1, cx - 5, cy + 1);
                g2.drawLine(cx + 5, cy - 5, cx + 14, cy - 5);
                g2.drawLine(cx + 5, cy + 1, cx + 14, cy + 1);
            } else if (clave.contains("auditorio")) {
                g2.setColor(new Color(180, 42, 58));
                g2.fillRoundRect(cx - 20, cy - 16, 40, 30, 8, 8);
                g2.setColor(AMARILLO_DORADO);
                g2.setStroke(new BasicStroke(2.2f));
                g2.drawRoundRect(cx - 20, cy - 16, 40, 30, 8, 8);
                g2.fillOval(cx - 8, cy, 8, 7);
                g2.fillOval(cx + 4, cy - 3, 8, 7);
                g2.drawLine(cx - 1, cy + 3, cx - 1, cy - 10);
                g2.drawLine(cx + 11, cy, cx + 11, cy - 12);
                g2.drawLine(cx - 1, cy - 10, cx + 11, cy - 12);
            } else if (clave.contains("plazoleta")) {
                g2.setColor(new Color(125, 82, 42));
                g2.fillRect(cx - 4, cy + 2, 8, 14);
                g2.setColor(new Color(46, 178, 92));
                g2.fillOval(cx - 16, cy - 16, 32, 22);
                g2.setColor(new Color(34, 142, 72));
                g2.fillOval(cx - 12, cy - 22, 24, 18);
                g2.setColor(AMARILLO_DORADO);
                g2.fillOval(cx + 10, cy - 22, 10, 10);
            } else {
                g2.setColor(AZUL_CARTA_ARRIBA);
                g2.fillOval(cx - 18, cy - 18, 36, 36);
                g2.setColor(AMARILLO_DORADO);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                String ini = !clave.isBlank() ? claveTexto.substring(0, 1).toUpperCase() : "★";
                g2.drawString(ini, cx - fm.stringWidth(ini) / 2, cy + 6);
            }
        }
    }

    private static class BotonSalirMemoria extends JButton {

        private boolean hover;

        public BotonSalirMemoria(String texto) {
            super(texto);
            this.hover = false;
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setFocusable(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
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

            g2.setColor(hover ? ROJO_CORAL_HOVER : ROJO_CORAL);
            g2.fillRoundRect(1, 1, w - 2, h - 2, 18, 18);

            g2.setColor(new Color(255, 255, 255, 210));
            g2.setStroke(new BasicStroke(1.6f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, 18, 18);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(getText())) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(Color.WHITE);
            g2.drawString(getText(), tx, ty);

            g2.dispose();
        }
    }
}
