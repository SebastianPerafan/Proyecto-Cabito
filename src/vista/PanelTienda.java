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
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelTienda extends JPanel {

    public static final int ANCHO_PANEL = 800;
    public static final int ALTO_PANEL = 600;

    public static final Color AZUL_PROFUNDO = new Color(30, 51, 90);
    public static final Color AZUL_GORRA_OSCURO = new Color(40, 67, 114);
    public static final Color AZUL_GORRA_MEDIO = new Color(56, 90, 148);
    public static final Color DORADO_MONEDAS = new Color(255, 215, 0);
    public static final Color DORADO_HUESO = new Color(246, 208, 77);
    public static final Color BORDE_CELESTE = new Color(184, 208, 240);
    public static final Color VERDE_ESMERALDA = new Color(30, 158, 82);
    public static final Color VERDE_ESMERALDA_HOVER = new Color(37, 186, 99);

    private final JLabel lblTitulo;
    private final JLabel lblMonedas;
    private final JButton btnVolver;
    private final BotonPestana btnTabCafeteria;
    private final BotonPestana btnTabSaludEstudio;
    private final JPanel panelContenedorItems;

    private final List<TarjetaProducto> tarjetasCafeteria;
    private final List<TarjetaProducto> tarjetasSaludEstudio;
    private int pestanaActiva;

    public PanelTienda() {
        setLayout(null);
        setPreferredSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setSize(ANCHO_PANEL, ALTO_PANEL);
        setBackground(new Color(220, 232, 245));

        this.tarjetasCafeteria = new ArrayList<>();
        this.tarjetasSaludEstudio = new ArrayList<>();
        this.pestanaActiva = 0;

        this.btnVolver = new BotonVolverRedondeado("← Volver");
        this.btnVolver.setBounds(48, 22, 112, 36);
        add(this.btnVolver);

        this.lblTitulo = new JLabel("TIENDA UNIVERSITARIA UTADEO", SwingConstants.CENTER);
        this.lblTitulo.setBounds(168, 22, 406, 36);
        this.lblTitulo.setForeground(Color.WHITE);
        this.lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(this.lblTitulo);

        this.lblMonedas = new JLabel("Monedas: 0", SwingConstants.CENTER);
        this.lblMonedas.setBounds(582, 23, 168, 34);
        this.lblMonedas.setForeground(DORADO_MONEDAS);
        this.lblMonedas.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(this.lblMonedas);

        this.btnTabCafeteria = new BotonPestana("Cafetería y Energía", true);
        this.btnTabCafeteria.setBounds(135, 88, 255, 36);
        this.btnTabCafeteria.addActionListener(e -> cambiarPestana(0));
        add(this.btnTabCafeteria);

        this.btnTabSaludEstudio = new BotonPestana("Salud, Estudio y Ropa", false);
        this.btnTabSaludEstudio.setBounds(410, 88, 255, 36);
        this.btnTabSaludEstudio.addActionListener(e -> cambiarPestana(1));
        add(this.btnTabSaludEstudio);

        this.panelContenedorItems = new JPanel(null);
        this.panelContenedorItems.setOpaque(false);
        this.panelContenedorItems.setBounds(55, 136, 690, 415);
        add(this.panelContenedorItems);
    }

    private void cambiarPestana(int indice) {
        this.pestanaActiva = indice;
        this.btnTabCafeteria.setActiva(indice == 0);
        this.btnTabSaludEstudio.setActiva(indice == 1);
        actualizarTarjetasVisibles();
    }

    public JButton agregarItemTienda(String nombre, String precio, String descripcion, String rutaImagen) {
        TarjetaProducto tarjeta = new TarjetaProducto(nombre, precio, descripcion, rutaImagen);

        if (esItemCafeteriaOEnergia(nombre, descripcion)) {
            this.tarjetasCafeteria.add(tarjeta);
        } else {
            this.tarjetasSaludEstudio.add(tarjeta);
        }

        actualizarTarjetasVisibles();
        return tarjeta.getBtnComprar();
    }

    private boolean esItemCafeteriaOEnergia(String nombre, String descripcion) {
        String texto = ((nombre != null ? nombre : "") + " " + (descripcion != null ? descripcion : "")).toLowerCase();
        return texto.contains("hambre")
                || texto.contains("energ")
                || texto.contains("manzana")
                || texto.contains("sopa")
                || texto.contains("perro")
                || texto.contains("caf");
    }

    private void actualizarTarjetasVisibles() {
        this.panelContenedorItems.removeAll();

        List<TarjetaProducto> listaActiva = (this.pestanaActiva == 0)
                ? this.tarjetasCafeteria
                : this.tarjetasSaludEstudio;

        int anchoTarjeta = 328;
        int altoTarjeta = 118;
        int gapX = 24;
        int gapY = 14;

        for (int i = 0; i < listaActiva.size(); i++) {
            int fila = i / 2;
            int col = i % 2;
            int x = 6 + col * (anchoTarjeta + gapX);
            int y = 6 + fila * (altoTarjeta + gapY);

            if (i == listaActiva.size() - 1 && listaActiva.size() % 2 == 1) {
                x = (this.panelContenedorItems.getWidth() - anchoTarjeta) / 2;
            }

            TarjetaProducto tarjeta = listaActiva.get(i);
            tarjeta.setBounds(x, y, anchoTarjeta, altoTarjeta);
            this.panelContenedorItems.add(tarjeta);
        }

        this.panelContenedorItems.revalidate();
        this.panelContenedorItems.repaint();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Image imgFondo = GestorRecursos.obtenerImagenEscalada("fondo_tienda.png", ANCHO_PANEL, ALTO_PANEL);
        if (imgFondo == null) {
            imgFondo = GestorRecursos.obtenerImagenEscalada("fondo_tienda.jpg", ANCHO_PANEL, ALTO_PANEL);
        }

        if (imgFondo != null) {
            g2.drawImage(imgFondo, 0, 0, ANCHO_PANEL, ALTO_PANEL, this);
        } else {
            dibujarFondoTiendaRespaldo(g2);
        }

        RoundRectangle2D.Float vitrina = new RoundRectangle2D.Float(35, 75, 730, 490, 28, 28);
        g2.setColor(new Color(10, 25, 55, 95));
        g2.fill(vitrina);
        g2.setColor(new Color(255, 255, 255, 110));
        g2.setStroke(new BasicStroke(1.8f));
        g2.draw(vitrina);

        RoundRectangle2D.Float barraCabecera = new RoundRectangle2D.Float(35, 15, 730, 50, 22, 22);
        g2.setColor(new Color(40, 67, 114, 235));
        g2.fill(barraCabecera);
        g2.setColor(DORADO_HUESO);
        g2.setStroke(new BasicStroke(2.0f));
        g2.draw(barraCabecera);

        RoundRectangle2D.Float insigniaMonedas = new RoundRectangle2D.Float(582, 23, 168, 34, 26, 26);
        g2.setColor(AZUL_PROFUNDO);
        g2.fill(insigniaMonedas);
        g2.setColor(BORDE_CELESTE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(insigniaMonedas);

        g2.dispose();
    }

    private void dibujarFondoTiendaRespaldo(Graphics2D g2) {
        GradientPaint pared = new GradientPaint(0, 0, new Color(242, 228, 205), 0, ALTO_PANEL, new Color(215, 192, 162));
        g2.setPaint(pared);
        g2.fillRect(0, 0, ANCHO_PANEL, ALTO_PANEL);

        g2.setColor(new Color(145, 98, 56));
        g2.fillRect(0, 430, ANCHO_PANEL, 170);
        g2.setColor(new Color(115, 74, 38));
        g2.fillRect(0, 420, ANCHO_PANEL, 14);
    }

    public void cargarCatalogo() {
        actualizarTarjetasVisibles();
    }

    public void actualizarMonedas(int monedas) {
        this.lblMonedas.setText("Monedas: " + monedas);
        repaint();
    }

    public void limpiarTienda() {
        this.tarjetasCafeteria.clear();
        this.tarjetasSaludEstudio.clear();
        this.panelContenedorItems.removeAll();
        this.panelContenedorItems.revalidate();
        this.panelContenedorItems.repaint();
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JLabel getLblTitulo() {
        return lblTitulo;
    }

    public JLabel getLblMonedas() {
        return lblMonedas;
    }

    public JPanel getPanelContenedorItems() {
        return panelContenedorItems;
    }

    private static class BotonVolverRedondeado extends JButton {

        private boolean hover;

        public BotonVolverRedondeado(String texto) {
            super(texto);
            this.hover = false;
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
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

            g2.setColor(hover ? new Color(242, 98, 82) : new Color(224, 79, 62));
            g2.fillRoundRect(1, 1, w - 2, h - 2, 18, 18);

            g2.setColor(new Color(255, 255, 255, 190));
            g2.setStroke(new BasicStroke(1.5f));
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

    private static class BotonPestana extends JButton {

        private boolean activa;
        private boolean hover;

        public BotonPestana(String texto, boolean activa) {
            super(texto);
            this.activa = activa;
            this.hover = false;
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
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

        public void setActiva(boolean activa) {
            this.activa = activa;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (this.activa) {
                GradientPaint gp = new GradientPaint(0, 0, AZUL_GORRA_MEDIO, 0, h, AZUL_GORRA_OSCURO);
                g2.setPaint(gp);
                g2.fillRoundRect(1, 1, w - 2, h - 2, 24, 24);
                g2.setColor(DORADO_HUESO);
                g2.setStroke(new BasicStroke(2.2f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 24, 24);
                g2.setColor(DORADO_HUESO);
            } else {
                g2.setColor(hover ? new Color(255, 255, 255, 215) : new Color(255, 255, 255, 165));
                g2.fillRoundRect(1, 1, w - 2, h - 2, 24, 24);
                g2.setColor(AZUL_GORRA_OSCURO);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 24, 24);
                g2.setColor(AZUL_PROFUNDO);
            }

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(getText())) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), tx, ty);

            g2.dispose();
        }
    }

    private static class TarjetaProducto extends JPanel {

        private final String nombre;
        private final String descripcion;
        private final String rutaImagen;
        private final BotonCompraPill btnComprar;

        public TarjetaProducto(String nombre, String precio, String descripcion, String rutaImagen) {
            setLayout(null);
            setOpaque(false);
            this.nombre = (nombre != null) ? nombre : "Producto";
            this.descripcion = (descripcion != null) ? descripcion : "";
            this.rutaImagen = rutaImagen;

            String textoPrecio = "$" + (precio != null ? precio : "0") + "  •  Comprar";
            this.btnComprar = new BotonCompraPill(textoPrecio);
            this.btnComprar.setBounds(138, 70, 176, 34);
            add(this.btnComprar);
        }

        public JButton getBtnComprar() {
            return btnComprar;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            RoundRectangle2D.Float fondoTarjeta = new RoundRectangle2D.Float(1.5f, 1.5f, w - 3f, h - 3f, 20, 20);
            g2.setColor(new Color(255, 255, 255, 215));
            g2.fill(fondoTarjeta);

            g2.setColor(AZUL_GORRA_OSCURO);
            g2.setStroke(new BasicStroke(1.6f));
            g2.draw(fondoTarjeta);

            int tamCajaIcono = 82;
            int xIcono = 14;
            int yIcono = (h - tamCajaIcono) / 2;
            g2.setColor(new Color(235, 243, 255));
            g2.fillRoundRect(xIcono, yIcono, tamCajaIcono, tamCajaIcono, 18, 18);
            g2.setColor(BORDE_CELESTE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(xIcono, yIcono, tamCajaIcono, tamCajaIcono, 18, 18);

            Image iconoImg = GestorRecursos.obtenerImagenIcono(this.rutaImagen, 70, 70);
            if (iconoImg != null) {
                g2.drawImage(iconoImg, xIcono + 6, yIcono + 6, 70, 70, this);
            } else {
                dibujarIconoIlustrado(g2, xIcono, yIcono, tamCajaIcono);
            }

            g2.setColor(AZUL_PROFUNDO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.drawString(this.nombre, 108, 34);

            Color colorEfecto = this.descripcion.contains("-")
                    ? new Color(55, 85, 115)
                    : new Color(24, 125, 68);
            g2.setColor(colorEfecto);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString(this.descripcion, 108, 55);

            g2.dispose();
            super.paintComponent(g);
        }

        private void dibujarIconoIlustrado(Graphics2D g2, int x, int y, int tam) {
            String clave = this.nombre.toLowerCase();
            int cx = x + tam / 2;
            int cy = y + tam / 2;

            if (clave.contains("energ")) {
                int lataX = cx - 17;
                int lataY = cy - 25;
                int lataW = 34;
                int lataH = 50;

                g2.setColor(new Color(190, 200, 212));
                g2.fillRoundRect(lataX + 3, lataY - 3, lataW - 6, 6, 4, 4);

                GradientPaint gpLata = new GradientPaint(
                        lataX, lataY, AZUL_GORRA_MEDIO,
                        lataX + lataW, lataY + lataH, AZUL_PROFUNDO
                );
                g2.setPaint(gpLata);
                g2.fillRoundRect(lataX, lataY + 2, lataW, lataH - 2, 9, 9);

                g2.setColor(DORADO_HUESO);
                g2.fillRect(lataX + 2, lataY + 11, lataW - 4, 5);
                g2.fillRect(lataX + 2, lataY + lataH - 9, lataW - 4, 4);

                g2.setColor(AZUL_PROFUNDO);
                g2.setStroke(new BasicStroke(1.6f));
                g2.drawRoundRect(lataX, lataY + 2, lataW, lataH - 2, 9, 9);

                Polygon rayo = new Polygon(
                        new int[]{cx + 3, cx - 7, cx, cx - 3, cx + 8, cx + 1},
                        new int[]{cy - 10, cy + 1, cy + 1, cy + 13, cy - 1, cy - 1},
                        6
                );
                g2.setColor(DORADO_MONEDAS);
                g2.fill(rayo);
            } else {
                g2.setColor(AZUL_GORRA_MEDIO);
                g2.fillOval(cx - 22, cy - 22, 44, 44);
                g2.setColor(DORADO_HUESO);
                g2.setFont(new Font("SansSerif", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                String inicial = !this.nombre.isBlank() ? this.nombre.substring(0, 1).toUpperCase() : "★";
                g2.drawString(inicial, cx - fm.stringWidth(inicial) / 2, cy + 7);
            }
        }
    }

    private static class BotonCompraPill extends JButton {

        private boolean hover;

        public BotonCompraPill(String texto) {
            super(texto);
            this.hover = false;
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setForeground(Color.WHITE);
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

            Color base = hover ? VERDE_ESMERALDA_HOVER : VERDE_ESMERALDA;
            GradientPaint gp = new GradientPaint(0, 0, base.brighter(), 0, h, base);
            g2.setPaint(gp);
            g2.fillRoundRect(1, 1, w - 2, h - 2, 26, 26);

            g2.setColor(hover ? DORADO_HUESO : new Color(255, 255, 255, 190));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, 26, 26);

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
