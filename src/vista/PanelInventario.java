package vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class PanelInventario extends JPanel {

    private final JLabel lblTitulo;
    private final JButton btnVolver;
    private final JTabbedPane tabbedPane;
    private final JPanel panelConsumibles;
    private final JPanel panelArmario;

    public PanelInventario() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(240, 244, 248));
        setPreferredSize(new Dimension(VentanaPrincipal.ANCHO_VENTANA, VentanaPrincipal.ALTO_VENTANA));

        JPanel panelNorte = new JPanel(new BorderLayout(15, 0));
        panelNorte.setBackground(new Color(40, 67, 114));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        this.btnVolver = new JButton("← Volver");
        this.btnVolver.setBackground(new Color(224, 79, 62));
        this.btnVolver.setForeground(Color.WHITE);
        this.btnVolver.setFocusPainted(false);
        this.btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));

        this.lblTitulo = new JLabel("Mi Mochila Tadeísta", SwingConstants.CENTER);
        this.lblTitulo.setForeground(Color.WHITE);
        this.lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel espaciadorDerecho = new JLabel("");
        espaciadorDerecho.setPreferredSize(new Dimension(95, 28));

        panelNorte.add(this.btnVolver, BorderLayout.WEST);
        panelNorte.add(this.lblTitulo, BorderLayout.CENTER);
        panelNorte.add(espaciadorDerecho, BorderLayout.EAST);
        add(panelNorte, BorderLayout.NORTH);

        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

        this.panelConsumibles = new JPanel(new GridLayout(0, 2, 12, 12));
        this.panelConsumibles.setBackground(new Color(240, 244, 248));
        this.panelConsumibles.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel envolventeConsumibles = new JPanel(new BorderLayout());
        envolventeConsumibles.setBackground(new Color(240, 244, 248));
        envolventeConsumibles.add(this.panelConsumibles, BorderLayout.NORTH);

        JScrollPane scrollConsumibles = new JScrollPane(envolventeConsumibles);
        scrollConsumibles.setBorder(null);
        scrollConsumibles.getVerticalScrollBar().setUnitIncrement(16);

        this.panelArmario = new JPanel(new GridLayout(0, 2, 12, 12));
        this.panelArmario.setBackground(new Color(240, 244, 248));
        this.panelArmario.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel envolventeArmario = new JPanel(new BorderLayout());
        envolventeArmario.setBackground(new Color(240, 244, 248));
        envolventeArmario.add(this.panelArmario, BorderLayout.NORTH);

        JScrollPane scrollArmario = new JScrollPane(envolventeArmario);
        scrollArmario.setBorder(null);
        scrollArmario.getVerticalScrollBar().setUnitIncrement(16);

        this.tabbedPane.addTab("Consumibles", scrollConsumibles);
        this.tabbedPane.addTab("Armario", scrollArmario);
        add(this.tabbedPane, BorderLayout.CENTER);
    }

    public JButton agregarItemConsumible(String nombre, String cantidad, String rutaImagen) {
        JPanel tarjeta = new JPanel(new BorderLayout(12, 8));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(184, 208, 240), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        tarjeta.setPreferredSize(new Dimension(350, 110));

        JComponent iconoVista = crearComponenteIcono(nombre, rutaImagen);
        tarjeta.add(iconoVista, BorderLayout.WEST);

        JPanel panelInfo = new JPanel(new GridLayout(2, 1, 0, 4));
        panelInfo.setOpaque(false);

        JLabel lblNombre = new JLabel(nombre != null ? nombre : "Consumible");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNombre.setForeground(new Color(30, 51, 90));

        JLabel lblDetalle = new JLabel(cantidad != null ? cantidad : "Disponible");
        lblDetalle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblDetalle.setForeground(new Color(24, 125, 68));

        panelInfo.add(lblNombre);
        panelInfo.add(lblDetalle);
        tarjeta.add(panelInfo, BorderLayout.CENTER);

        JButton btnUsar = new JButton("Usar");
        btnUsar.setBackground(new Color(56, 90, 148));
        btnUsar.setForeground(Color.WHITE);
        btnUsar.setFocusPainted(false);
        btnUsar.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel panelBoton = new JPanel(new BorderLayout());
        panelBoton.setOpaque(false);
        panelBoton.add(btnUsar, BorderLayout.SOUTH);
        tarjeta.add(panelBoton, BorderLayout.EAST);

        this.panelConsumibles.add(tarjeta);
        this.panelConsumibles.revalidate();
        this.panelConsumibles.repaint();
        return btnUsar;
    }

    public JButton agregarItemArmario(String nombre, boolean equipado, String rutaImagen) {
        return agregarItemArmario(nombre, "Prenda universitaria desbloqueada", true, equipado, rutaImagen);
    }

    public JButton agregarItemArmario(
            String nombre,
            String descripcionLogro,
            boolean desbloqueado,
            boolean equipado,
            String rutaImagen) {
        JPanel tarjeta = new JPanel(new BorderLayout(12, 8));
        tarjeta.setBackground(desbloqueado ? Color.WHITE : new Color(235, 238, 242));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        equipado ? new Color(30, 158, 82) : new Color(184, 208, 240),
                        equipado ? 2 : 1
                ),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        tarjeta.setPreferredSize(new Dimension(350, 118));

        JComponent iconoVista = crearComponenteIcono(nombre, rutaImagen);
        tarjeta.add(iconoVista, BorderLayout.WEST);

        JPanel panelCentro = new JPanel(new BorderLayout(0, 4));
        panelCentro.setOpaque(false);

        JPanel panelTextos = new JPanel(new GridLayout(2, 1, 0, 2));
        panelTextos.setOpaque(false);

        JLabel lblNombre = new JLabel(nombre != null ? nombre : "Accesorio");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNombre.setForeground(desbloqueado ? new Color(30, 51, 90) : new Color(120, 130, 140));

        JLabel lblRequisito = new JLabel(descripcionLogro != null ? descripcionLogro : "");
        lblRequisito.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblRequisito.setForeground(new Color(85, 100, 115));

        panelTextos.add(lblNombre);
        panelTextos.add(lblRequisito);
        panelCentro.add(panelTextos, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(8, 0));
        panelInferior.setOpaque(false);

        String textoEstado = !desbloqueado
                ? "Estado: Bloqueado"
                : (equipado ? "Estado: Equipado" : "Estado: Desbloqueado");
        JLabel lblEstado = new JLabel(textoEstado);
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblEstado.setForeground(!desbloqueado
                ? new Color(192, 57, 43)
                : (equipado ? new Color(30, 158, 82) : new Color(56, 90, 148)));

        String textoBoton = !desbloqueado ? "Bloqueado" : (equipado ? "Desequipar" : "Equipar");
        JButton btnAccion = new JButton(textoBoton);
        btnAccion.setEnabled(desbloqueado);
        btnAccion.setBackground(!desbloqueado
                ? new Color(189, 195, 199)
                : (equipado ? new Color(230, 126, 34) : new Color(30, 158, 82)));
        btnAccion.setForeground(Color.WHITE);
        btnAccion.setFocusPainted(false);
        btnAccion.setFont(new Font("SansSerif", Font.BOLD, 12));

        panelInferior.add(lblEstado, BorderLayout.CENTER);
        panelInferior.add(btnAccion, BorderLayout.EAST);
        panelCentro.add(panelInferior, BorderLayout.SOUTH);

        tarjeta.add(panelCentro, BorderLayout.CENTER);

        this.panelArmario.add(tarjeta);
        this.panelArmario.revalidate();
        this.panelArmario.repaint();
        return btnAccion;
    }

    public void mostrarMensajeVacioConsumibles(String mensaje) {
        JLabel lblVacio = new JLabel(mensaje, SwingConstants.CENTER);
        lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblVacio.setForeground(new Color(110, 120, 130));
        lblVacio.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        this.panelConsumibles.add(lblVacio);
        this.panelConsumibles.revalidate();
        this.panelConsumibles.repaint();
    }

    private JComponent crearComponenteIcono(String nombre, String rutaImagen) {
        return new RecuadroIconoGrande(nombre, rutaImagen);
    }

    public void limpiarInventario() {
        this.panelConsumibles.removeAll();
        this.panelConsumibles.revalidate();
        this.panelConsumibles.repaint();

        this.panelArmario.removeAll();
        this.panelArmario.revalidate();
        this.panelArmario.repaint();
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JLabel getLblTitulo() {
        return lblTitulo;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JPanel getPanelConsumibles() {
        return panelConsumibles;
    }

    public JPanel getPanelArmario() {
        return panelArmario;
    }

    private static class RecuadroIconoGrande extends JComponent {

        private static final int TAM_CAJA = 88;
        private static final int TAM_ICONO = 76;

        private final String nombre;
        private final String rutaImagen;

        public RecuadroIconoGrande(String nombre, String rutaImagen) {
            this.nombre = (nombre != null) ? nombre : "";
            this.rutaImagen = rutaImagen;
            setPreferredSize(new Dimension(TAM_CAJA, TAM_CAJA));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(235, 243, 255));
            g2.fillRoundRect(1, 1, w - 2, h - 2, 18, 18);
            g2.setColor(new Color(184, 208, 240));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, 18, 18);

            Image img = GestorRecursos.obtenerImagenIcono(this.rutaImagen, TAM_ICONO, TAM_ICONO);
            if (img != null) {
                int x = (w - TAM_ICONO) / 2;
                int y = (h - TAM_ICONO) / 2;
                g2.drawImage(img, x, y, TAM_ICONO, TAM_ICONO, this);
            } else {
                dibujarRespaldoVectorial(g2, w, h);
            }

            g2.dispose();
        }

        private void dibujarRespaldoVectorial(Graphics2D g2, int w, int h) {
            int cx = w / 2;
            int cy = h / 2;
            if (this.nombre.toLowerCase().contains("energ")) {
                int lataX = cx - 17;
                int lataY = cy - 25;
                int lataW = 34;
                int lataH = 50;

                g2.setColor(new Color(190, 200, 212));
                g2.fillRoundRect(lataX + 3, lataY - 3, lataW - 6, 6, 4, 4);

                GradientPaint gpLata = new GradientPaint(
                        lataX, lataY, new Color(56, 90, 148),
                        lataX + lataW, lataY + lataH, new Color(30, 51, 90)
                );
                g2.setPaint(gpLata);
                g2.fillRoundRect(lataX, lataY + 2, lataW, lataH - 2, 9, 9);

                g2.setColor(new Color(246, 208, 77));
                g2.fillRect(lataX + 2, lataY + 11, lataW - 4, 5);
                g2.fillRect(lataX + 2, lataY + lataH - 9, lataW - 4, 4);

                g2.setColor(new Color(30, 51, 90));
                g2.setStroke(new BasicStroke(1.6f));
                g2.drawRoundRect(lataX, lataY + 2, lataW, lataH - 2, 9, 9);

                Polygon rayo = new Polygon(
                        new int[]{cx + 3, cx - 7, cx, cx - 3, cx + 8, cx + 1},
                        new int[]{cy - 10, cy + 1, cy + 1, cy + 13, cy - 1, cy - 1},
                        6
                );
                g2.setColor(new Color(255, 215, 0));
                g2.fill(rayo);
            } else {
                g2.setColor(new Color(56, 90, 148));
                g2.fillOval(cx - 22, cy - 22, 44, 44);
                g2.setColor(new Color(246, 208, 77));
                g2.setFont(new Font("SansSerif", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                String inicial = !this.nombre.isBlank() ? this.nombre.substring(0, 1).toUpperCase() : "★";
                g2.drawString(inicial, cx - fm.stringWidth(inicial) / 2, cy + 7);
            }
        }
    }
}
