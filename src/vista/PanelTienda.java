package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class PanelTienda extends JPanel {

    private final JLabel lblTitulo;
    private final JLabel lblMonedas;
    private final JButton btnVolver;
    private final JPanel panelContenedorItems;
    private final JScrollPane scrollPane;

    public PanelTienda() {
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
        this.btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));

        this.lblTitulo = new JLabel("Tienda de la Universidad", SwingConstants.CENTER);
        this.lblTitulo.setForeground(Color.WHITE);
        this.lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        this.lblMonedas = new JLabel("Monedas: 0", SwingConstants.RIGHT);
        this.lblMonedas.setForeground(new Color(241, 196, 15));
        this.lblMonedas.setFont(new Font("SansSerif", Font.BOLD, 15));

        panelNorte.add(this.btnVolver, BorderLayout.WEST);
        panelNorte.add(this.lblTitulo, BorderLayout.CENTER);
        panelNorte.add(this.lblMonedas, BorderLayout.EAST);
        add(panelNorte, BorderLayout.NORTH);

        this.panelContenedorItems = new JPanel(new GridLayout(0, 2, 12, 12));
        this.panelContenedorItems.setBackground(new Color(240, 244, 248));
        this.panelContenedorItems.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel panelEnvolvente = new JPanel(new BorderLayout());
        panelEnvolvente.setBackground(new Color(240, 244, 248));
        panelEnvolvente.add(this.panelContenedorItems, BorderLayout.NORTH);

        this.scrollPane = new JScrollPane(panelEnvolvente);
        this.scrollPane.setBorder(null);
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(this.scrollPane, BorderLayout.CENTER);
    }

    public JButton agregarItemTienda(String nombre, String precio, String descripcion, String rutaImagen) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 8));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        tarjeta.setPreferredSize(new Dimension(340, 105));

        JLabel lblIcono = crearEtiquetaIcono(nombre, rutaImagen);
        tarjeta.add(lblIcono, BorderLayout.WEST);

        JPanel panelInfo = new JPanel(new GridLayout(3, 1, 0, 2));
        panelInfo.setOpaque(false);

        JLabel lblNombre = new JLabel(nombre != null ? nombre : "Producto");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setForeground(new Color(20, 45, 76));

        JLabel lblDescripcion = new JLabel(descripcion != null ? descripcion : "");
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDescripcion.setForeground(new Color(90, 105, 120));

        JLabel lblPrecio = new JLabel("Precio: " + (precio != null ? precio : "0") + " monedas");
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPrecio.setForeground(new Color(183, 149, 11));

        panelInfo.add(lblNombre);
        panelInfo.add(lblDescripcion);
        panelInfo.add(lblPrecio);
        tarjeta.add(panelInfo, BorderLayout.CENTER);

        JButton btnComprar = new JButton("Comprar");
        btnComprar.setBackground(new Color(39, 174, 96));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel panelBoton = new JPanel(new BorderLayout());
        panelBoton.setOpaque(false);
        panelBoton.add(btnComprar, BorderLayout.SOUTH);
        tarjeta.add(panelBoton, BorderLayout.EAST);

        this.panelContenedorItems.add(tarjeta);
        this.panelContenedorItems.revalidate();
        this.panelContenedorItems.repaint();
        return btnComprar;
    }

    private JLabel crearEtiquetaIcono(String nombre, String rutaImagen) {
        JLabel lblIcono = new JLabel("", SwingConstants.CENTER);
        lblIcono.setPreferredSize(new Dimension(64, 64));
        ImageIcon icono = GestorRecursos.obtenerIcono(rutaImagen, 56, 56);
        if (icono != null) {
            lblIcono.setIcon(icono);
        } else {
            String inicial = (nombre != null && !nombre.isBlank()) ? nombre.substring(0, 1).toUpperCase() : "?";
            lblIcono.setText("[" + inicial + "]");
            lblIcono.setFont(new Font("SansSerif", Font.BOLD, 16));
            lblIcono.setOpaque(true);
            lblIcono.setBackground(new Color(230, 238, 246));
            lblIcono.setForeground(new Color(41, 128, 185));
            lblIcono.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220), 1));
        }
        return lblIcono;
    }

    public void actualizarMonedas(int monedas) {
        this.lblMonedas.setText("Monedas: " + monedas);
    }

    public void limpiarTienda() {
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
}

