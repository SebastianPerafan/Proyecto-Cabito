package vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelAtrapaComida extends JPanel {

    public static final int ANCHO_LOGICO = 800;
    public static final int ALTO_LOGICO = 600;
    public static final int Y_SUELO_CAPTURA = 468;
    public static final int ANCHO_CABITO = 84;
    public static final int ALTO_CABITO = 92;
    public static final int TAMANO_CABITO = 88;
    public static final int TAMANO_COMIDA = 48;
    private static final int META_PUNTAJE = 800;

    private static final Color AZUL_REY = new Color(0, 51, 160);
    private static final Color AZUL_REY_OSCURO = new Color(16, 36, 82);
    private static final Color AMARILLO_DORADO = new Color(255, 215, 0);
    private static final Color ROJO_CORAL = new Color(224, 79, 62);
    private static final Color ROJO_CORAL_HOVER = new Color(242, 98, 82);
    private static final Color VERDE_ESMERALDA = new Color(30, 158, 82);
    private static final Color CELESTE_CAPSULA = new Color(232, 244, 255);

    private final JButton btnVolver;
    private final JLabel lblPuntaje;
    private final JLabel lblTiempo;
    private final LienzoJuego lienzo;

    private int xCabito;
    private int puntajeActual;
    private int tiempoRestanteMillis;
    private List<String[]> comidasActivas;
    private final List<TextoFlotante> textosFlotantes;

    public PanelAtrapaComida() {
        setLayout(new BorderLayout());
        setBackground(AZUL_REY_OSCURO);
        setPreferredSize(new Dimension(VentanaPrincipal.ANCHO_VENTANA, VentanaPrincipal.ALTO_VENTANA));
        setFocusable(true);

        this.xCabito = ANCHO_LOGICO / 2;
        this.puntajeActual = 0;
        this.tiempoRestanteMillis = 30000;
        this.comidasActivas = new ArrayList<>();
        this.textosFlotantes = new ArrayList<>();

        this.lblPuntaje = new JLabel("Puntaje: 0 pts", SwingConstants.CENTER);
        this.lblTiempo = new JLabel("Tiempo: 30.0 s", SwingConstants.CENTER);

        this.btnVolver = new BotonSalirArcade("Salir / Terminar");
        this.btnVolver.setBounds(618, 19, 146, 36);

        this.lienzo = new LienzoJuego();
        this.lienzo.setLayout(null);
        this.lienzo.add(this.btnVolver);

        add(this.lienzo, BorderLayout.CENTER);
    }

    public void actualizarLienzo(int xCab, List<String[]> comidas, int puntaje, int tiempoMillis) {
        if (tiempoMillis >= 29800 && puntaje == 0) {
            this.puntajeActual = 0;
            this.textosFlotantes.clear();
        } else if (puntaje != this.puntajeActual) {
            int diferencia = puntaje - this.puntajeActual;
            String textoDelta = (diferencia > 0) ? "+" + diferencia : String.valueOf(diferencia);
            this.textosFlotantes.add(new TextoFlotante(
                    textoDelta,
                    xCab,
                    Y_SUELO_CAPTURA - (ALTO_CABITO / 2) - 8,
                    diferencia >= 0
            ));
            this.puntajeActual = puntaje;
        }

        this.xCabito = xCab;
        this.tiempoRestanteMillis = Math.max(0, tiempoMillis);
        this.comidasActivas = (comidas != null) ? new ArrayList<>(comidas) : new ArrayList<>();

        this.lblPuntaje.setText("Puntaje: " + puntaje + " pts");
        double segundos = this.tiempoRestanteMillis / 1000.0;
        this.lblTiempo.setText(String.format("Tiempo: %.1f s", segundos));

        actualizarTextosFlotantes();
        this.lienzo.repaint();
    }

    private void actualizarTextosFlotantes() {
        Iterator<TextoFlotante> it = this.textosFlotantes.iterator();
        while (it.hasNext()) {
            TextoFlotante tf = it.next();
            tf.y -= 2.4f;
            tf.vida -= 0.042f;
            if (tf.vida <= 0f) {
                it.remove();
            }
        }
    }

    private static class TextoFlotante {

        final String texto;
        final int x;
        float y;
        final boolean positivo;
        float vida;

        TextoFlotante(String texto, int x, float y, boolean positivo) {
            this.texto = texto;
            this.x = x;
            this.y = y;
            this.positivo = positivo;
            this.vida = 1.0f;
        }
    }

    public class LienzoJuego extends JPanel {

        public LienzoJuego() {
            setBackground(AZUL_REY_OSCURO);
            setFocusable(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = Math.max(1, getWidth());
            int h = Math.max(1, getHeight());

            dibujarEscenarioCancha(g2, w, h);

            double escalaX = (double) w / ANCHO_LOGICO;
            double escalaY = (double) h / ALTO_LOGICO;

            dibujarComidas(g2, escalaX, escalaY);
            dibujarCabito(g2, escalaX, escalaY);
            dibujarTextosFlotantes(g2, escalaX, escalaY);
            dibujarHudSuperior(g2, w);
            dibujarBarraInferiorControles(g2, w, h);

            g2.dispose();
        }

        private void dibujarEscenarioCancha(Graphics2D g2, int w, int h) {
            Image imgFondo = GestorRecursos.obtenerImagenEscalada("fondo_cancha.png", w, h);
            if (imgFondo == null) {
                imgFondo = GestorRecursos.obtenerImagenEscalada("fondo_cancha.jpg", w, h);
            }

            if (imgFondo != null) {
                g2.drawImage(imgFondo, 0, 0, w, h, this);
            } else {
                GradientPaint cielo = new GradientPaint(0, 0, new Color(115, 178, 235), 0, (int) (h * 0.72), new Color(205, 230, 250));
                g2.setPaint(cielo);
                g2.fillRect(0, 0, w, h);

                int ySuelo = (int) (h * 0.74);
                GradientPaint cancha = new GradientPaint(0, ySuelo, new Color(76, 158, 92), 0, h, new Color(48, 118, 64));
                g2.setPaint(cancha);
                g2.fillRect(0, ySuelo, w, h - ySuelo);

                g2.setColor(new Color(255, 255, 255, 150));
                g2.setStroke(new BasicStroke(3f));
                g2.drawLine(0, ySuelo, w, ySuelo);
                g2.drawOval(w / 2 - 90, ySuelo + 18, 180, 56);
            }

            g2.setColor(new Color(10, 20, 42, 35));
            g2.fillRect(0, 0, w, h);
        }

        private void dibujarCabito(Graphics2D g2, double escalaX, double escalaY) {
            int centroX = (int) Math.round(xCabito * escalaX);
            int centroY = (int) Math.round(Y_SUELO_CAPTURA * escalaY);

            int drawX = centroX - (ANCHO_CABITO / 2);
            int drawY = centroY - (ALTO_CABITO / 2);

            g2.setColor(new Color(0, 0, 0, 65));
            g2.fillOval(centroX - 34, drawY + ALTO_CABITO - 12, 68, 16);

            Image imgCabito = GestorRecursos.obtenerImagenIcono("cabito_jugando.png", ANCHO_CABITO, ALTO_CABITO);
            if (imgCabito == null) {
                imgCabito = GestorRecursos.obtenerImagenIcono("cabito_feliz.png", ANCHO_CABITO, ALTO_CABITO);
            }
            if (imgCabito == null) {
                imgCabito = GestorRecursos.obtenerImagenEscalada("cabito_jugando.png", ANCHO_CABITO, ALTO_CABITO);
            }

            if (imgCabito != null) {
                g2.drawImage(imgCabito, drawX, drawY, ANCHO_CABITO, ALTO_CABITO, this);
            } else {
                g2.setColor(new Color(211, 84, 0));
                g2.fillOval(drawX, drawY, ANCHO_CABITO, ALTO_CABITO);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.drawString("CABITO", drawX + 16, drawY + 50);
            }
        }

        private void dibujarComidas(Graphics2D g2, double escalaX, double escalaY) {
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

                int centroX = (int) Math.round(xLogico * escalaX);
                int centroY = (int) Math.round(yLogico * escalaY);
                int drawX = centroX - (TAMANO_COMIDA / 2);
                int drawY = centroY - (TAMANO_COMIDA / 2);

                int radioGlow = TAMANO_COMIDA + 12;
                Color baseGlow = obtenerColorPorTipo(tipo);
                g2.setColor(new Color(baseGlow.getRed(), baseGlow.getGreen(), baseGlow.getBlue(), 60));
                g2.fillOval(centroX - radioGlow / 2, centroY - radioGlow / 2, radioGlow, radioGlow);

                g2.setColor(new Color(255, 255, 255, 95));
                g2.fillOval(drawX - 2, drawY - 2, TAMANO_COMIDA + 4, TAMANO_COMIDA + 4);

                String archivoImg = obtenerNombreImagenPorTipo(tipo);
                Image imgComida = GestorRecursos.obtenerImagenIcono(archivoImg, TAMANO_COMIDA, TAMANO_COMIDA);
                if (imgComida == null) {
                    imgComida = GestorRecursos.obtenerImagenEscalada(archivoImg, TAMANO_COMIDA, TAMANO_COMIDA);
                }

                if (imgComida != null) {
                    g2.drawImage(imgComida, drawX, drawY, TAMANO_COMIDA, TAMANO_COMIDA, this);
                } else {
                    g2.setColor(baseGlow);
                    g2.fillOval(drawX + 2, drawY + 2, TAMANO_COMIDA - 4, TAMANO_COMIDA - 4);
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawOval(drawX + 2, drawY + 2, TAMANO_COMIDA - 4, TAMANO_COMIDA - 4);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                    FontMetrics fm = g2.getFontMetrics();
                    String inicial = !tipo.isBlank() ? tipo.substring(0, 1).toUpperCase() : "★";
                    g2.drawString(inicial, centroX - fm.stringWidth(inicial) / 2, centroY + 6);
                }
            }
        }

        private void dibujarTextosFlotantes(Graphics2D g2, double escalaX, double escalaY) {
            if (textosFlotantes.isEmpty()) {
                return;
            }
            g2.setFont(new Font("SansSerif", Font.BOLD, 20));
            FontMetrics fm = g2.getFontMetrics();

            for (TextoFlotante tf : textosFlotantes) {
                int alpha = Math.max(0, Math.min(255, (int) (tf.vida * 255)));
                int px = (int) Math.round(tf.x * escalaX) - fm.stringWidth(tf.texto) / 2;
                int py = (int) Math.round(tf.y * escalaY);

                g2.setColor(new Color(10, 20, 40, alpha));
                g2.drawString(tf.texto, px + 2, py + 2);

                Color colorPrincipal = tf.positivo
                        ? new Color(46, 224, 118, alpha)
                        : new Color(242, 82, 68, alpha);
                g2.setColor(colorPrincipal);
                g2.drawString(tf.texto, px, py);
            }
        }

        private void dibujarHudSuperior(Graphics2D g2, int w) {
            int margenX = 24;
            int yBarra = 10;
            int anchoBarra = w - (margenX * 2);
            int altoBarra = 54;

            btnVolver.setBounds(w - margenX - 158, yBarra + 9, 144, 36);

            RoundRectangle2D.Float cabecera = new RoundRectangle2D.Float(margenX, yBarra, anchoBarra, altoBarra, 24, 24);
            GradientPaint gpCabecera = new GradientPaint(
                    0, yBarra, new Color(0, 51, 160, 235),
                    0, yBarra + altoBarra, new Color(14, 36, 98, 240)
            );
            g2.setPaint(gpCabecera);
            g2.fill(cabecera);

            g2.setColor(AMARILLO_DORADO);
            g2.setStroke(new BasicStroke(2.0f));
            g2.draw(cabecera);

            int xPildoraPuntaje = margenX + 14;
            int yPildora = yBarra + 8;
            int wPildoraPuntaje = 330;
            int hPildora = 38;

            g2.setColor(AZUL_REY_OSCURO);
            g2.fillRoundRect(xPildoraPuntaje, yPildora, wPildoraPuntaje, hPildora, 20, 20);
            g2.setColor(new Color(184, 208, 240));
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(xPildoraPuntaje, yPildora, wPildoraPuntaje, hPildora, 20, 20);

            g2.setColor(AMARILLO_DORADO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            String txtPts = "Puntaje: " + puntajeActual + " pts";
            g2.drawString(txtPts, xPildoraPuntaje + 14, yPildora + 25);

            int xProg = xPildoraPuntaje + 178;
            int yProg = yPildora + 11;
            int wProg = 136;
            int hProg = 16;

            g2.setColor(new Color(8, 18, 42));
            g2.fillRoundRect(xProg, yProg, wProg, hProg, 12, 12);

            double fraccionMeta = Math.min(1.0, (double) puntajeActual / META_PUNTAJE);
            int anchoLlenado = (int) Math.round(fraccionMeta * (wProg - 4));
            if (anchoLlenado > 0) {
                Color cIni = (puntajeActual >= META_PUNTAJE) ? new Color(46, 204, 113) : AMARILLO_DORADO;
                Color cFin = (puntajeActual >= META_PUNTAJE) ? VERDE_ESMERALDA : new Color(230, 155, 10);
                g2.setPaint(new GradientPaint(xProg, yProg, cIni, xProg + wProg, yProg, cFin));
                g2.fillRoundRect(xProg + 2, yProg + 2, Math.max(6, anchoLlenado), hProg - 4, 10, 10);
            }

            g2.setColor(new Color(255, 255, 255, 160));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(xProg, yProg, wProg, hProg, 12, 12);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            String txtMeta = Math.min(puntajeActual, META_PUNTAJE) + " / " + META_PUNTAJE;
            FontMetrics fmMeta = g2.getFontMetrics();
            g2.drawString(txtMeta, xProg + (wProg - fmMeta.stringWidth(txtMeta)) / 2, yProg + 12);

            double segundos = tiempoRestanteMillis / 1000.0;
            boolean alertaTiempo = segundos < 5.0;

            int xPildoraTiempo = xPildoraPuntaje + wPildoraPuntaje + 18;
            int wPildoraTiempo = 205;

            Color fondoTiempo = alertaTiempo ? ROJO_CORAL : CELESTE_CAPSULA;
            Color bordeTiempo = alertaTiempo ? AMARILLO_DORADO : new Color(120, 175, 235);
            Color textoTiempo = alertaTiempo ? Color.WHITE : AZUL_REY_OSCURO;

            g2.setColor(fondoTiempo);
            g2.fillRoundRect(xPildoraTiempo, yPildora, wPildoraTiempo, hPildora, 20, 20);
            g2.setColor(bordeTiempo);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRoundRect(xPildoraTiempo, yPildora, wPildoraTiempo, hPildora, 20, 20);

            g2.setColor(textoTiempo);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            String strTiempo = String.format("⏱ Tiempo: %.1f s", segundos);
            FontMetrics fmT = g2.getFontMetrics();
            g2.drawString(strTiempo, xPildoraTiempo + (wPildoraTiempo - fmT.stringWidth(strTiempo)) / 2, yPildora + 25);
        }

        private void dibujarBarraInferiorControles(Graphics2D g2, int w, int h) {
            int wPildora = 520;
            int hPildora = 40;
            int xPildora = (w - wPildora) / 2;
            int yPildora = h - hPildora - 12;

            RoundRectangle2D.Float pildora = new RoundRectangle2D.Float(xPildora, yPildora, wPildora, hPildora, 24, 24);
            g2.setColor(new Color(14, 34, 78, 225));
            g2.fill(pildora);

            g2.setColor(new Color(184, 208, 240, 190));
            g2.setStroke(new BasicStroke(1.6f));
            g2.draw(pildora);

            int yTecla = yPildora + 6;
            dibujarTeclaTeclado(g2, xPildora + 18, yTecla, 38, 28, "←");
            dibujarTeclaTeclado(g2, xPildora + 62, yTecla, 38, 28, "→");

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.drawString("Mueve a Cabito por la cancha", xPildora + 114, yPildora + 25);

            g2.setColor(AMARILLO_DORADO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.drawString("•   Meta: 800 pts", xPildora + 356, yPildora + 25);
        }

        private void dibujarTeclaTeclado(Graphics2D g2, int x, int y, int w, int h, String simbolo) {
            g2.setColor(new Color(10, 22, 50));
            g2.fillRoundRect(x, y + 2, w, h, 8, 8);

            GradientPaint gpTecla = new GradientPaint(x, y, Color.WHITE, x, y + h, new Color(215, 228, 245));
            g2.setPaint(gpTecla);
            g2.fillRoundRect(x, y, w, h - 2, 8, 8);

            g2.setColor(AZUL_REY);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(x, y, w, h - 2, 8, 8);

            g2.setColor(AZUL_REY_OSCURO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(simbolo, x + (w - fm.stringWidth(simbolo)) / 2, y + 19);
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
                case "galleta" -> new Color(218, 158, 92);
                case "queso" -> new Color(241, 196, 15);
                case "pez" -> new Color(52, 152, 219);
                default -> new Color(155, 89, 182);
            };
        }
    }

    private static class BotonSalirArcade extends JButton {

        private boolean hover;

        public BotonSalirArcade(String texto) {
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

            Color colorFondo = hover ? ROJO_CORAL_HOVER : ROJO_CORAL;
            g2.setColor(colorFondo);
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
