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
        panelNorte.setBackground(new Color(21, 67, 96));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        this.btnVolver = new JButton("Volver");
        this.btnVolver.setBackground(new Color(231, 76, 60));
        this.btnVolver.setForeground(Color.WHITE);
        this.btnVolver.setFocusPainted(false);
        this.btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));

        this.lblTitulo = new JLabel("Mi Mochila Tadeísta", SwingConstants.CENTER);
        this.lblTitulo.setForeground(Color.WHITE);
        this.lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel espaciadorDerecho = new JLabel("");
        espaciadorDerecho.setPreferredSize(new Dimension(85, 28));

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
        JPanel tarjeta = new JPanel(new BorderLayout(10, 8));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        tarjeta.setPreferredSize(new Dimension(340, 95));

        JLabel lblIcono = crearEtiquetaIcono(nombre, rutaImagen);
        tarjeta.add(lblIcono, BorderLayout.WEST);

        JPanel panelInfo = new JPanel(new GridLayout(2, 1, 0, 4));
        panelInfo.setOpaque(false);

        JLabel lblNombre = new JLabel(nombre != null ? nombre : "Consumible");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setForeground(new Color(20, 45, 76));

        JLabel lblDetalle = new JLabel(cantidad != null ? cantidad : "Disponible");
        lblDetalle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDetalle.setForeground(new Color(90, 105, 120));

        panelInfo.add(lblNombre);
        panelInfo.add(lblDetalle);
        tarjeta.add(panelInfo, BorderLayout.CENTER);

        JButton btnUsar = new JButton("Usar");
        btnUsar.setBackground(new Color(41, 128, 185));
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
        JPanel tarjeta = new JPanel(new BorderLayout(10, 8));
        tarjeta.setBackground(desbloqueado ? Color.WHITE : new Color(235, 238, 242));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(equipado ? new Color(39, 174, 96) : new Color(200, 210, 220), equipado ? 2 : 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        tarjeta.setPreferredSize(new Dimension(340, 105));

        JLabel lblIcono = crearEtiquetaIcono(nombre, rutaImagen);
        tarjeta.add(lblIcono, BorderLayout.WEST);

        JPanel panelInfo = new JPanel(new GridLayout(3, 1, 0, 2));
        panelInfo.setOpaque(false);

        JLabel lblNombre = new JLabel(nombre != null ? nombre : "Accesorio");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setForeground(desbloqueado ? new Color(20, 45, 76) : new Color(120, 130, 140));

        JLabel lblRequisito = new JLabel(descripcionLogro != null ? descripcionLogro : "");
        lblRequisito.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblRequisito.setForeground(new Color(95, 105, 115));

        String textoEstado = !desbloqueado ? "Estado: Bloqueado por logro" : (equipado ? "Estado: Equipado" : "Estado: Desbloqueado");
        JLabel lblEstado = new JLabel(textoEstado);
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblEstado.setForeground(!desbloqueado ? new Color(192, 57, 43) : (equipado ? new Color(39, 174, 96) : new Color(41, 128, 185)));

        panelInfo.add(lblNombre);
        panelInfo.add(lblRequisito);
        panelInfo.add(lblEstado);
        tarjeta.add(panelInfo, BorderLayout.CENTER);

        String textoBoton = !desbloqueado ? "Bloqueado" : (equipado ? "Desequipar" : "Equipar");
        JButton btnAccion = new JButton(textoBoton);
        btnAccion.setEnabled(desbloqueado);
        btnAccion.setBackground(!desbloqueado
                ? new Color(189, 195, 199)
                : (equipado ? new Color(230, 126, 34) : new Color(39, 174, 96)));
        btnAccion.setForeground(Color.WHITE);
        btnAccion.setFocusPainted(false);
        btnAccion.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel panelBoton = new JPanel(new BorderLayout());
        panelBoton.setOpaque(false);
        panelBoton.add(btnAccion, BorderLayout.SOUTH);
        tarjeta.add(panelBoton, BorderLayout.EAST);

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
}

