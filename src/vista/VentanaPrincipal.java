package vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaPrincipal extends JFrame {

    public static final int ANCHO_VENTANA = 800;
    public static final int ALTO_VENTANA = 600;

    public static final String PANTALLA_INICIO = "INICIO";
    public static final String PANTALLA_PRINCIPAL = "PRINCIPAL";
    public static final String PANTALLA_TIENDA = "TIENDA";
    public static final String PANTALLA_INVENTARIO = "INVENTARIO";
    public static final String PANTALLA_MEMORIA = "MINIJUEGO_MEMORIA";
    public static final String PANTALLA_ATRAPA_COMIDA = "MINIJUEGO_ATRAPA_COMIDA";

    private final JPanel panelContenedor;
    private final CardLayout cardLayout;

    private final PanelJuegoPrincipal panelPrincipal;
    private final PanelTienda panelTienda;
    private final PanelInventario panelInventario;
    private final PanelMemoria panelMemoria;
    private final PanelAtrapaComida panelAtrapaComida;

    public VentanaPrincipal() {
        setTitle("Cabito: Aventura Tadeísta");
        setSize(ANCHO_VENTANA, ALTO_VENTANA);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.cardLayout = new CardLayout();
        this.panelContenedor = new JPanel(this.cardLayout);

        this.panelPrincipal = new PanelJuegoPrincipal();
        this.panelTienda = new PanelTienda();
        this.panelInventario = new PanelInventario();
        this.panelMemoria = new PanelMemoria();
        this.panelAtrapaComida = new PanelAtrapaComida();

        agregarPanel(this.panelPrincipal, PANTALLA_PRINCIPAL);
        agregarPanel(this.panelTienda, PANTALLA_TIENDA);
        agregarPanel(this.panelInventario, PANTALLA_INVENTARIO);
        agregarPanel(this.panelMemoria, PANTALLA_MEMORIA);
        agregarPanel(this.panelAtrapaComida, PANTALLA_ATRAPA_COMIDA);

        setLayout(new BorderLayout());
        add(this.panelContenedor, BorderLayout.CENTER);
        mostrarPanel(PANTALLA_PRINCIPAL);
    }

    public final void agregarPanel(JPanel panel, String nombreCarta) {
        if (panel != null && nombreCarta != null && !nombreCarta.isBlank()) {
            this.panelContenedor.add(panel, nombreCarta);
        }
    }

    public final void mostrarPanel(String nombreCarta) {
        if (nombreCarta == null || nombreCarta.isBlank()) {
            return;
        }
        this.cardLayout.show(this.panelContenedor, nombreCarta);
        this.panelContenedor.revalidate();
        this.panelContenedor.repaint();

        for (Component componente : this.panelContenedor.getComponents()) {
            if (componente.isVisible()) {
                componente.requestFocusInWindow();
                break;
            }
        }
    }

    public JPanel getPanelContenedor() {
        return panelContenedor;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public PanelJuegoPrincipal getPanelPrincipal() {
        return panelPrincipal;
    }

    public PanelTienda getPanelTienda() {
        return panelTienda;
    }

    public PanelInventario getPanelInventario() {
        return panelInventario;
    }

    public PanelMemoria getPanelMemoria() {
        return panelMemoria;
    }

    public PanelAtrapaComida getPanelAtrapaComida() {
        return panelAtrapaComida;
    }
}
