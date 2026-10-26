package vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class PanelJuegoPrincipal extends JPanel {

    public static final int ANCHO_PANEL = 800;
    public static final int ALTO_PANEL = 600;

    private final JLayeredPane layeredPane;
    private final JLabel lblFondo;
    private final JLabel lblCabito;
    private final JLabel lblAccesorio;
    private final JPanel panelUI;

    private final JProgressBar pbHambre;
    private final JProgressBar pbEnergia;
    private final JProgressBar pbHigiene;
    private final JProgressBar pbFelicidad;
    private final JProgressBar pbSalud;

    private final JLabel lblNivel;
    private final JLabel lblXp;
    private final JLabel lblMonedas;
    private final JLabel lblInteligencia;
    private final JLabel lblMundo;
    private final JLabel lblEstado;

    private final JButton btnExplorar;
    private final JButton btnTienda;
    private final JButton btnInventario;
    private final JButton btnMinijuegos;
    private final JButton btnDormir;
    private final JButton btnLavarse;
    private final JButton btnGuardarPartida;
    private final JButton btnCargarPartida;
    private final JButton btnCambiarMundo;

    public PanelJuegoPrincipal() {
        setLayout(null);
        setPreferredSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setSize(ANCHO_PANEL, ALTO_PANEL);

        this.layeredPane = new JLayeredPane();
        this.layeredPane.setBounds(0, 0, ANCHO_PANEL, ALTO_PANEL);
        add(this.layeredPane);

        this.lblFondo = new JLabel();
        this.lblFondo.setBounds(0, 0, ANCHO_PANEL, ALTO_PANEL);
        this.lblFondo.setOpaque(true);
        this.lblFondo.setBackground(new Color(225, 238, 248));
        this.layeredPane.add(this.lblFondo, JLayeredPane.DEFAULT_LAYER);

        this.lblCabito = new JLabel("[ Cabito ]", SwingConstants.CENTER);
        this.lblCabito.setBounds(250, 150, 300, 300);
        this.lblCabito.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.lblCabito.setForeground(new Color(20, 55, 95));
        this.layeredPane.add(this.lblCabito, JLayeredPane.PALETTE_LAYER);

        this.lblAccesorio = new JLabel("", SwingConstants.CENTER);
        this.lblAccesorio.setBounds(250, 150, 300, 300);
        this.layeredPane.add(this.lblAccesorio, JLayeredPane.MODAL_LAYER);

        this.panelUI = new JPanel(null);
        this.panelUI.setOpaque(false);
        this.panelUI.setBounds(0, 0, ANCHO_PANEL, ALTO_PANEL);
        this.layeredPane.add(this.panelUI, JLayeredPane.POPUP_LAYER);

        this.pbHambre = crearBarraProgreso("Hambre: 80%", 80, new Color(230, 126, 34), 20, 20);
        this.pbEnergia = crearBarraProgreso("Energía: 100%", 100, new Color(241, 196, 15), 20, 55);
        this.pbHigiene = crearBarraProgreso("Higiene: 80%", 80, new Color(52, 152, 219), 20, 90);
        this.pbFelicidad = crearBarraProgreso("Felicidad: 80%", 80, new Color(155, 89, 182), 20, 125);
        this.pbSalud = crearBarraProgreso("Salud: 100%", 100, new Color(46, 204, 113), 20, 160);

        this.panelUI.add(this.pbHambre);
        this.panelUI.add(this.pbEnergia);
        this.panelUI.add(this.pbHigiene);
        this.panelUI.add(this.pbFelicidad);
        this.panelUI.add(this.pbSalud);

        this.lblNivel = crearEtiquetaInfo("Nivel: 0", 240, 20, 160, 28);
        this.lblXp = crearEtiquetaInfo("XP: 0 / 100", 410, 20, 170, 28);
        this.lblMonedas = crearEtiquetaInfo("Monedas: 30", 240, 55, 160, 28);
        this.lblInteligencia = crearEtiquetaInfo("Inteligencia: 10", 410, 55, 170, 28);
        this.lblMundo = crearEtiquetaInfo("Mundo: La Plazoleta", 240, 90, 175, 28);
        this.lblEstado = crearEtiquetaInfo("Estado: Feliz", 425, 90, 155, 28);

        this.panelUI.add(this.lblNivel);
        this.panelUI.add(this.lblXp);
        this.panelUI.add(this.lblMonedas);
        this.panelUI.add(this.lblInteligencia);
        this.panelUI.add(this.lblMundo);
        this.panelUI.add(this.lblEstado);

        this.btnCambiarMundo = crearBoton("Cambiar Mundo", 600, 20, 175, 32, new Color(41, 128, 185));
        this.btnGuardarPartida = crearBoton("Guardar Partida", 600, 60, 175, 32, new Color(39, 174, 96));
        this.btnCargarPartida = crearBoton("Cargar Partida", 600, 100, 175, 32, new Color(127, 140, 141));

        this.panelUI.add(this.btnCambiarMundo);
        this.panelUI.add(this.btnGuardarPartida);
        this.panelUI.add(this.btnCargarPartida);

        this.btnExplorar = crearBoton("Explorar", 20, 500, 118, 42, new Color(21, 67, 96));
        this.btnTienda = crearBoton("Tienda", 148, 500, 118, 42, new Color(21, 67, 96));
        this.btnInventario = crearBoton("Inventario", 276, 500, 118, 42, new Color(21, 67, 96));
        this.btnMinijuegos = crearBoton("Minijuegos", 404, 500, 118, 42, new Color(21, 67, 96));
        this.btnDormir = crearBoton("Dormir", 532, 500, 118, 42, new Color(91, 44, 111));
        this.btnLavarse = crearBoton("Lavarse", 660, 500, 118, 42, new Color(17, 122, 101));

        this.panelUI.add(this.btnExplorar);
        this.panelUI.add(this.btnTienda);
        this.panelUI.add(this.btnInventario);
        this.panelUI.add(this.btnMinijuegos);
        this.panelUI.add(this.btnDormir);
        this.panelUI.add(this.btnLavarse);
    }

    private JProgressBar crearBarraProgreso(String textoInicial, int valorInicial, Color colorBarra, int x, int y) {
        JProgressBar barra = new JProgressBar(0, 100);
        barra.setValue(valorInicial);
        barra.setStringPainted(true);
        barra.setString(textoInicial);
        barra.setForeground(colorBarra);
        barra.setBackground(new Color(245, 245, 245));
        barra.setFont(new Font("SansSerif", Font.BOLD, 12));
        barra.setBounds(x, y, 200, 26);
        return barra;
    }

    private JLabel crearEtiquetaInfo(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setBounds(x, y, ancho, alto);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(new Color(255, 255, 255, 220));
        etiqueta.setForeground(new Color(20, 40, 60));
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));
        etiqueta.setBorder(BorderFactory.createLineBorder(new Color(180, 195, 210), 1));
        return etiqueta;
    }

    private JButton crearBoton(String texto, int x, int y, int ancho, int alto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        return boton;
    }

    public void actualizarFondo(String nombreImagen) {
        ImageIcon icono = GestorRecursos.obtenerIcono(nombreImagen, ANCHO_PANEL, ALTO_PANEL);
        this.lblFondo.setIcon(icono);
        if (icono == null) {
            this.lblFondo.setOpaque(true);
            if (nombreImagen != null && nombreImagen.contains("biblioteca")) {
                this.lblFondo.setBackground(new Color(235, 225, 210));
            } else if (nombreImagen != null && nombreImagen.contains("auditorio")) {
                this.lblFondo.setBackground(new Color(215, 220, 240));
            } else {
                this.lblFondo.setBackground(new Color(225, 238, 248));
            }
        }
        this.lblFondo.repaint();
    }

    public void actualizarCabito(String nombreImagen) {
        ImageIcon icono = GestorRecursos.obtenerIcono(nombreImagen, 300, 300);
        this.lblCabito.setIcon(icono);
        if (icono == null) {
            String etiquetaRespaldo = (nombreImagen != null && !nombreImagen.isBlank())
                    ? "[ Cabito: " + nombreImagen.replace(".png", "") + " ]"
                    : "[ Cabito ]";
            this.lblCabito.setText(etiquetaRespaldo);
            this.lblCabito.setBorder(BorderFactory.createDashedBorder(new Color(41, 128, 185), 2, 5, 3, true));
        } else {
            this.lblCabito.setText("");
            this.lblCabito.setBorder(null);
        }
        this.lblCabito.repaint();
    }

    public void actualizarAccesorio(String nombreImagen, int offsetX, int offsetY, double escala) {
        double factorEscala = (escala > 0) ? escala : 1.0;
        int nuevoTam = Math.max(1, (int) (300 * factorEscala));
        this.lblAccesorio.setBounds(250 + offsetX, 150 + offsetY, nuevoTam, nuevoTam);

        if (nombreImagen == null || nombreImagen.isBlank()) {
            this.lblAccesorio.setIcon(null);
            this.lblAccesorio.setText("");
            this.lblAccesorio.repaint();
            return;
        }

        ImageIcon originalIcon = GestorRecursos.obtenerIcono(nombreImagen, 300, 300);
        if (originalIcon != null && originalIcon.getImage() != null) {
            Image img = originalIcon.getImage().getScaledInstance(nuevoTam, nuevoTam, Image.SCALE_SMOOTH);
            this.lblAccesorio.setIcon(new ImageIcon(img));
            this.lblAccesorio.setVerticalAlignment(SwingConstants.CENTER);
            this.lblAccesorio.setText("");
        } else {
            this.lblAccesorio.setIcon(null);
            this.lblAccesorio.setVerticalAlignment(SwingConstants.BOTTOM);
            this.lblAccesorio.setText("Equipado: " + nombreImagen.replace(".png", ""));
        }
        this.lblAccesorio.repaint();
    }

    public void actualizarEstadisticas(
            int hambre,
            int energia,
            int higiene,
            int felicidad,
            int salud,
            int inteligencia,
            int nivel,
            int xp,
            int xpSiguiente,
            int monedas,
            String estado,
            String mundo) {
        this.pbHambre.setValue(hambre);
        this.pbHambre.setString("Hambre: " + hambre + "%");

        this.pbEnergia.setValue(energia);
        this.pbEnergia.setString("Energía: " + energia + "%");

        this.pbHigiene.setValue(higiene);
        this.pbHigiene.setString("Higiene: " + higiene + "%");

        this.pbFelicidad.setValue(felicidad);
        this.pbFelicidad.setString("Felicidad: " + felicidad + "%");

        this.pbSalud.setValue(salud);
        this.pbSalud.setString("Salud: " + salud + "%");

        this.lblNivel.setText("Nivel: " + nivel);
        this.lblXp.setText("XP: " + xp + " / " + xpSiguiente);
        this.lblMonedas.setText("Monedas: " + monedas);
        this.lblInteligencia.setText("Inteligencia: " + inteligencia);
        this.lblEstado.setText("Estado: " + (estado != null ? estado : "Normal"));
        this.lblMundo.setText("Mundo: " + (mundo != null ? mundo : "La Plazoleta"));
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    public JLabel getLblFondo() {
        return lblFondo;
    }

    public JLabel getLblCabito() {
        return lblCabito;
    }

    public JLabel getLblAccesorio() {
        return lblAccesorio;
    }

    public JPanel getPanelUI() {
        return panelUI;
    }

    public JProgressBar getPbHambre() {
        return pbHambre;
    }

    public JProgressBar getPbEnergia() {
        return pbEnergia;
    }

    public JProgressBar getPbHigiene() {
        return pbHigiene;
    }

    public JProgressBar getPbFelicidad() {
        return pbFelicidad;
    }

    public JProgressBar getPbSalud() {
        return pbSalud;
    }

    public JLabel getLblNivel() {
        return lblNivel;
    }

    public JLabel getLblXp() {
        return lblXp;
    }

    public JLabel getLblMonedas() {
        return lblMonedas;
    }

    public JLabel getLblInteligencia() {
        return lblInteligencia;
    }

    public JLabel getLblMundo() {
        return lblMundo;
    }

    public JLabel getLblEstado() {
        return lblEstado;
    }

    public JButton getBtnExplorar() {
        return btnExplorar;
    }

    public JButton getBtnTienda() {
        return btnTienda;
    }

    public JButton getBtnInventario() {
        return btnInventario;
    }

    public JButton getBtnMinijuegos() {
        return btnMinijuegos;
    }

    public JButton getBtnDormir() {
        return btnDormir;
    }

    public JButton getBtnLavarse() {
        return btnLavarse;
    }

    public JButton getBtnGuardarPartida() {
        return btnGuardarPartida;
    }

    public JButton getBtnCargarPartida() {
        return btnCargarPartida;
    }

    public JButton getBtnCambiarMundo() {
        return btnCambiarMundo;
    }
}

