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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class PanelInventario extends JPanel {

    public static final int ANCHO_PANEL = 800;
    public static final int ALTO_PANEL = 600;
    private static final int TOTAL_CASILLAS_GRILLA = 12;

    public static final Color AZUL_FONDO_OSCURO = new Color(20, 35, 64);
    public static final Color AZUL_PROFUNDO = new Color(30, 51, 90);
    public static final Color AZUL_GORRA_OSCURO = new Color(40, 67, 114);
    public static final Color AZUL_GORRA_MEDIO = new Color(56, 90, 148);
    public static final Color AZUL_GORRA_CLARO = new Color(72, 112, 178);
    public static final Color DORADO_HUESO = new Color(246, 208, 77);
    public static final Color DORADO_OSCURO = new Color(218, 165, 32);
    public static final Color BORDE_CELESTE = new Color(184, 208, 240);
    public static final Color VERDE_EQUIPADO = new Color(30, 158, 82);
    public static final Color NARANJA_DESEQUIPAR = new Color(230, 126, 34);

    private final JLabel lblTitulo;
    private final JButton btnVolver;
    private final JTabbedPane tabbedPane;
    private final JPanel panelConsumibles;
    private final JPanel panelArmario;

    private final BotonCategoriaLateral btnCatTodos;
    private final BotonCategoriaLateral btnCatComida;
    private final BotonCategoriaLateral btnCatSaludEstudio;
    private final BotonCategoriaLateral btnCatArmario;

    private final JPanel panelGrillaCasillas;
    private final JPanel panelAccionDetalle;

    private final List<EntradaInventario> listaConsumibles;
    private final List<EntradaInventario> listaArmario;
    private final List<EntradaInventario> entradasFiltradas;

    private int categoriaActiva;
    private String ultimoNombreSeleccionado;
    private EntradaInventario entradaSeleccionada;
    private String mensajeVacioConsumibles;

    private int nivelJugador;
    private int xpJugador;
    private int xpSiguienteJugador;
    private int saludJugador;
    private int energiaJugador;

    public PanelInventario() {
        setLayout(null);
        setPreferredSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setSize(ANCHO_PANEL, ALTO_PANEL);
        setBackground(AZUL_FONDO_OSCURO);

        this.listaConsumibles = new ArrayList<>();
        this.listaArmario = new ArrayList<>();
        this.entradasFiltradas = new ArrayList<>();
        this.categoriaActiva = 0;
        this.ultimoNombreSeleccionado = null;
        this.entradaSeleccionada = null;
        this.mensajeVacioConsumibles = "Tu mochila está vacía. ¡Visita la Tienda!";

        this.nivelJugador = 1;
        this.xpJugador = 50;
        this.xpSiguienteJugador = 100;
        this.saludJugador = 100;
        this.energiaJugador = 100;

        this.lblTitulo = new JLabel("MI MOCHILA TADEÍSTA", SwingConstants.CENTER);
        this.lblTitulo.setBounds(235, 18, 400, 34);
        this.lblTitulo.setForeground(Color.WHITE);
        this.lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(this.lblTitulo);

        this.btnVolver = new BotonVolverInventario("← Volver");
        this.btnVolver.setBounds(656, 18, 114, 36);
        add(this.btnVolver);

        this.btnCatTodos = new BotonCategoriaLateral("MOCHILA", true);
        this.btnCatTodos.setBounds(28, 128, 174, 54);
        this.btnCatTodos.addActionListener(e -> cambiarCategoria(0));
        add(this.btnCatTodos);

        this.btnCatComida = new BotonCategoriaLateral("COMIDA Y CAFÉ", false);
        this.btnCatComida.setBounds(28, 196, 174, 54);
        this.btnCatComida.addActionListener(e -> cambiarCategoria(1));
        add(this.btnCatComida);

        this.btnCatSaludEstudio = new BotonCategoriaLateral("SALUD Y ESTUDIO", false);
        this.btnCatSaludEstudio.setBounds(28, 264, 174, 54);
        this.btnCatSaludEstudio.addActionListener(e -> cambiarCategoria(2));
        add(this.btnCatSaludEstudio);

        this.btnCatArmario = new BotonCategoriaLateral("ARMARIO", false);
        this.btnCatArmario.setBounds(28, 332, 174, 54);
        this.btnCatArmario.addActionListener(e -> cambiarCategoria(3));
        add(this.btnCatArmario);

        this.panelGrillaCasillas = new JPanel(new GridLayout(3, 4, 12, 12));
        this.panelGrillaCasillas.setOpaque(false);
        this.panelGrillaCasillas.setBounds(238, 92, 514, 326);
        add(this.panelGrillaCasillas);

        this.panelAccionDetalle = new JPanel(new BorderLayout());
        this.panelAccionDetalle.setOpaque(false);
        this.panelAccionDetalle.setBounds(588, 468, 162, 42);
        add(this.panelAccionDetalle);

        this.tabbedPane = new JTabbedPane();
        this.panelConsumibles = new JPanel();
        this.panelArmario = new JPanel();

        reconstruirGrilla();
    }

    public void actualizarEstadisticasJugador(int nivel, int xp, int xpSiguiente, int salud, int energia) {
        this.nivelJugador = Math.max(0, nivel);
        this.xpJugador = Math.max(0, xp);
        this.xpSiguienteJugador = Math.max(1, xpSiguiente);
        this.saludJugador = Math.max(0, Math.min(100, salud));
        this.energiaJugador = Math.max(0, Math.min(100, energia));
        repaint();
    }

    private void cambiarCategoria(int nuevaCategoria) {
        this.categoriaActiva = nuevaCategoria;
        this.btnCatTodos.setActiva(nuevaCategoria == 0);
        this.btnCatComida.setActiva(nuevaCategoria == 1);
        this.btnCatSaludEstudio.setActiva(nuevaCategoria == 2);
        this.btnCatArmario.setActiva(nuevaCategoria == 3);
        reconstruirGrilla();
    }

    public JButton agregarItemConsumible(String nombre, String descripcion, String rutaImagen) {
        return agregarItemConsumible(nombre, descripcion, 1, rutaImagen);
    }

    public JButton agregarItemConsumible(String nombre, String descripcion, int cantidad, String rutaImagen) {
        int totalUnidades = Math.max(1, cantidad);
        BotonAccionInspector btnUsar = new BotonAccionInspector("USAR OBJETO", AZUL_GORRA_CLARO, true);

        EntradaInventario entrada = new EntradaInventario(
                nombre != null ? nombre : "Consumible",
                descripcion != null ? descripcion : "",
                totalUnidades,
                true,
                false,
                false,
                rutaImagen,
                btnUsar
        );
        this.listaConsumibles.add(entrada);
        reconstruirGrilla();
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
        String textoBoton = !desbloqueado ? "BLOQUEADO" : (equipado ? "DESEQUIPAR" : "EQUIPAR");
        Color colorBoton = !desbloqueado
                ? new Color(110, 122, 138)
                : (equipado ? NARANJA_DESEQUIPAR : VERDE_EQUIPADO);

        BotonAccionInspector btnAccion = new BotonAccionInspector(textoBoton, colorBoton, desbloqueado);
        btnAccion.setEnabled(desbloqueado);

        EntradaInventario entrada = new EntradaInventario(
                nombre != null ? nombre : "Accesorio",
                descripcionLogro != null ? descripcionLogro : "",
                0,
                desbloqueado,
                equipado,
                true,
                rutaImagen,
                btnAccion
        );
        this.listaArmario.add(entrada);
        reconstruirGrilla();
        return btnAccion;
    }

    public void mostrarMensajeVacioConsumibles(String mensaje) {
        if (mensaje != null && !mensaje.isBlank()) {
            this.mensajeVacioConsumibles = mensaje;
        }
        reconstruirGrilla();
    }

    public void limpiarInventario() {
        this.listaConsumibles.clear();
        this.listaArmario.clear();
        this.entradasFiltradas.clear();
        this.entradaSeleccionada = null;
        reconstruirGrilla();
    }

    private void reconstruirGrilla() {
        this.entradasFiltradas.clear();

        if (this.categoriaActiva == 0) {
            this.entradasFiltradas.addAll(this.listaConsumibles);
        } else if (this.categoriaActiva == 1) {
            for (EntradaInventario item : this.listaConsumibles) {
                if (esComidaOBebida(item.nombre, item.descripcion)) {
                    this.entradasFiltradas.add(item);
                }
            }
        } else if (this.categoriaActiva == 2) {
            for (EntradaInventario item : this.listaConsumibles) {
                if (!esComidaOBebida(item.nombre, item.descripcion)) {
                    this.entradasFiltradas.add(item);
                }
            }
        } else {
            this.entradasFiltradas.addAll(this.listaArmario);
        }

        this.entradaSeleccionada = null;
        if (this.ultimoNombreSeleccionado != null) {
            for (EntradaInventario item : this.entradasFiltradas) {
                if (item.nombre.equalsIgnoreCase(this.ultimoNombreSeleccionado)) {
                    this.entradaSeleccionada = item;
                    break;
                }
            }
        }
        if (this.entradaSeleccionada == null && !this.entradasFiltradas.isEmpty()) {
            this.entradaSeleccionada = this.entradasFiltradas.get(0);
            this.ultimoNombreSeleccionado = this.entradaSeleccionada.nombre;
        }

        this.panelGrillaCasillas.removeAll();
        for (int i = 0; i < TOTAL_CASILLAS_GRILLA; i++) {
            EntradaInventario entrada = (i < this.entradasFiltradas.size()) ? this.entradasFiltradas.get(i) : null;
            boolean seleccionada = (entrada != null && entrada == this.entradaSeleccionada);
            CasillaInventario casilla = new CasillaInventario(entrada, seleccionada);
            this.panelGrillaCasillas.add(casilla);
        }

        actualizarPanelAccionInferior();
        this.panelGrillaCasillas.revalidate();
        this.panelGrillaCasillas.repaint();
        repaint();
    }

    private void seleccionarEntrada(EntradaInventario entrada) {
        if (entrada == null) {
            return;
        }
        this.entradaSeleccionada = entrada;
        this.ultimoNombreSeleccionado = entrada.nombre;
        reconstruirGrilla();
    }

    private void actualizarPanelAccionInferior() {
        this.panelAccionDetalle.removeAll();
        if (this.entradaSeleccionada != null && this.entradaSeleccionada.botonAccion != null) {
            this.panelAccionDetalle.add(this.entradaSeleccionada.botonAccion, BorderLayout.CENTER);
        }
        this.panelAccionDetalle.revalidate();
        this.panelAccionDetalle.repaint();
    }

    private boolean esComidaOBebida(String nombre, String descripcion) {
        String nom = (nombre != null ? nombre : "").toLowerCase();
        if (nom.contains("pelota") || nom.contains("juguete")) {
            return false;
        }
        String texto = (nom + " " + (descripcion != null ? descripcion.toLowerCase() : ""));
        return texto.contains("hambre")
                || texto.contains("energ")
                || texto.contains("manzana")
                || texto.contains("sopa")
                || texto.contains("perro")
                || texto.contains("helado")
                || texto.contains("galleta")
                || texto.contains("caf");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        GradientPaint fondoGeneral = new GradientPaint(0, 0, new Color(18, 32, 60), 0, h, new Color(28, 48, 86));
        g2.setPaint(fondoGeneral);
        g2.fillRect(0, 0, w, h);

        RoundRectangle2D.Float marcoExterior = new RoundRectangle2D.Float(12, 10, w - 24, h - 20, 26, 26);
        GradientPaint pinturaTablero = new GradientPaint(0, 10, AZUL_PROFUNDO, 0, h - 10, new Color(24, 42, 76));
        g2.setPaint(pinturaTablero);
        g2.fill(marcoExterior);

        g2.setColor(DORADO_HUESO);
        g2.setStroke(new BasicStroke(2.8f));
        g2.draw(marcoExterior);

        RoundRectangle2D.Float marcoInterior = new RoundRectangle2D.Float(16, 14, w - 32, h - 28, 22, 22);
        g2.setColor(new Color(184, 208, 240, 90));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(marcoInterior);

        dibujarInsigniaNivelYBarras(g2);
        dibujarPanelInfoLateral(g2);
        dibujarTableroGrilla(g2);
        dibujarInspectorInferior(g2);

        g2.dispose();
    }

    private void dibujarInsigniaNivelYBarras(Graphics2D g2) {
        int cx = 56;
        int cy = 62;
        int r = 28;

        Polygon hexagonoExterior = crearHexagono(cx, cy, r);
        g2.setColor(DORADO_HUESO);
        g2.fill(hexagonoExterior);
        g2.setColor(AZUL_FONDO_OSCURO);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(hexagonoExterior);

        Polygon hexagonoInterior = crearHexagono(cx, cy, r - 4);
        GradientPaint gpHex = new GradientPaint(cx, cy - r, AZUL_GORRA_MEDIO, cx, cy + r, AZUL_PROFUNDO);
        g2.setPaint(gpHex);
        g2.fill(hexagonoInterior);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();
        String txtNivel = String.valueOf(this.nivelJugador);
        g2.drawString(txtNivel, cx - fm.stringWidth(txtNivel) / 2, cy + 7);

        int xBarra = 88;
        int ySalud = 42;
        int anchoBarra = 110;
        int altoBarra = 14;

        g2.setColor(new Color(15, 26, 48));
        g2.fillRoundRect(xBarra, ySalud, anchoBarra, altoBarra, 10, 10);
        int llenadoSalud = (int) Math.round((this.saludJugador / 100.0) * (anchoBarra - 4));
        GradientPaint gpSalud = new GradientPaint(xBarra, ySalud, new Color(46, 204, 113), xBarra + anchoBarra, ySalud, new Color(30, 158, 82));
        g2.setPaint(gpSalud);
        g2.fillRoundRect(xBarra + 2, ySalud + 2, Math.max(4, llenadoSalud), altoBarra - 4, 8, 8);
        g2.setColor(DORADO_HUESO);
        g2.setStroke(new BasicStroke(1.3f));
        g2.drawRoundRect(xBarra, ySalud, anchoBarra, altoBarra, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 9));
        g2.drawString("SALUD " + this.saludJugador + "%", xBarra + 18, ySalud + 11);

        int yXp = 64;
        int altoXp = 12;
        g2.setColor(new Color(15, 26, 48));
        g2.fillRoundRect(xBarra + 8, yXp, anchoBarra - 8, altoXp, 8, 8);
        double propXp = Math.min(1.0, (double) this.xpJugador / Math.max(1, this.xpSiguienteJugador));
        int llenadoXp = (int) Math.round(propXp * (anchoBarra - 12));
        g2.setColor(new Color(52, 152, 219));
        g2.fillRoundRect(xBarra + 10, yXp + 2, Math.max(4, llenadoXp), altoXp - 4, 6, 6);
        g2.setColor(BORDE_CELESTE);
        g2.setStroke(new BasicStroke(1.1f));
        g2.drawRoundRect(xBarra + 8, yXp, anchoBarra - 8, altoXp, 8, 8);

        g2.setColor(DORADO_HUESO);
        g2.fillOval(xBarra - 2, yXp - 2, 16, 16);
        g2.setColor(AZUL_FONDO_OSCURO);
        g2.setFont(new Font("SansSerif", Font.BOLD, 8));
        g2.drawString("XP", xBarra + 1, yXp + 9);
    }

    private Polygon crearHexagono(int cx, int cy, int radio) {
        Polygon p = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angulo = Math.toRadians(60 * i);
            int px = (int) Math.round(cx + radio * Math.cos(angulo));
            int py = (int) Math.round(cy + radio * Math.sin(angulo));
            p.addPoint(px, py);
        }
        return p;
    }

    private void dibujarPanelInfoLateral(Graphics2D g2) {
        RoundRectangle2D.Float cajaResumen = new RoundRectangle2D.Float(28, 404, 174, 134, 18, 18);
        g2.setColor(new Color(18, 32, 58, 210));
        g2.fill(cajaResumen);
        g2.setColor(BORDE_CELESTE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(cajaResumen);

        g2.setColor(DORADO_HUESO);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("RESUMEN DE MOCHILA", 45, 428);

        g2.setColor(new Color(184, 208, 240, 120));
        g2.drawLine(40, 435, 190, 435);

        int tiposConsumibles = this.listaConsumibles.size();
        int totalUnidades = 0;
        for (EntradaInventario e : this.listaConsumibles) {
            totalUnidades += e.cantidad;
        }
        int desbloqueados = 0;
        for (EntradaInventario a : this.listaArmario) {
            if (a.desbloqueado) {
                desbloqueados++;
            }
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("• Objetos totales: " + totalUnidades, 40, 460);
        g2.drawString("• Tipos en mochila: " + tiposConsumibles, 40, 484);
        g2.drawString("• Armario: " + desbloqueados + " / " + this.listaArmario.size(), 40, 508);

        g2.setColor(DORADO_HUESO);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.drawString("Clic en casilla para ver", 48, 528);
    }

    private void dibujarTableroGrilla(Graphics2D g2) {
        int x = 220;
        int y = 74;
        int w = 550;
        int h = 362;

        RoundRectangle2D.Float marcoGrilla = new RoundRectangle2D.Float(x, y, w, h, 22, 22);
        GradientPaint gpGrilla = new GradientPaint(0, y, new Color(22, 38, 70), 0, y + h, new Color(16, 28, 52));
        g2.setPaint(gpGrilla);
        g2.fill(marcoGrilla);

        g2.setColor(DORADO_HUESO);
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(marcoGrilla);

        RoundRectangle2D.Float bordeInterno = new RoundRectangle2D.Float(x + 4, y + 4, w - 8, h - 8, 18, 18);
        g2.setColor(new Color(184, 208, 240, 90));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(bordeInterno);

        dibujarEsquineroDecorativo(g2, x - 3, y - 3, 1, 1);
        dibujarEsquineroDecorativo(g2, x + w + 3, y - 3, -1, 1);
        dibujarEsquineroDecorativo(g2, x - 3, y + h + 3, 1, -1);
        dibujarEsquineroDecorativo(g2, x + w + 3, y + h + 3, -1, -1);
    }

    private void dibujarEsquineroDecorativo(Graphics2D g2, int x, int y, int dirX, int dirY) {
        Polygon esquina = new Polygon(
                new int[]{x, x + dirX * 22, x + dirX * 16, x + dirX * 6, x + dirX * 6, x},
                new int[]{y, y, y + dirY * 6, y + dirY * 6, y + dirY * 16, y + dirY * 22},
                6
        );
        g2.setColor(DORADO_HUESO);
        g2.fill(esquina);
        g2.setColor(AZUL_FONDO_OSCURO);
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(esquina);
    }

    private void dibujarInspectorInferior(Graphics2D g2) {
        int x = 220;
        int y = 446;
        int w = 550;
        int h = 92;

        RoundRectangle2D.Float cajaInspector = new RoundRectangle2D.Float(x, y, w, h, 20, 20);
        GradientPaint gpInsp = new GradientPaint(0, y, AZUL_GORRA_OSCURO, 0, y + h, new Color(22, 38, 70));
        g2.setPaint(gpInsp);
        g2.fill(cajaInspector);

        g2.setColor(BORDE_CELESTE);
        g2.setStroke(new BasicStroke(2.0f));
        g2.draw(cajaInspector);

        if (this.entradaSeleccionada == null) {
            g2.setColor(DORADO_HUESO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            String msg = (this.categoriaActiva == 3)
                    ? "No hay accesorios en esta categoría."
                    : this.mensajeVacioConsumibles;
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, x + (w - fm.stringWidth(msg)) / 2, y + 52);
            return;
        }

        int xIcono = x + 16;
        int yIcono = y + 12;
        int tamCaja = 68;
        g2.setColor(new Color(235, 243, 255));
        g2.fillRoundRect(xIcono, yIcono, tamCaja, tamCaja, 16, 16);
        g2.setColor(DORADO_HUESO);
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawRoundRect(xIcono, yIcono, tamCaja, tamCaja, 16, 16);

        Image img = GestorRecursos.obtenerImagenIcono(this.entradaSeleccionada.rutaImagen, 58, 58);
        if (img != null) {
            g2.drawImage(img, xIcono + 5, yIcono + 5, 58, 58, this);
        } else {
            dibujarIconoRespaldo(g2, this.entradaSeleccionada.nombre, xIcono, yIcono, tamCaja);
        }

        int xTexto = x + 98;
        g2.setColor(DORADO_HUESO);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        String tituloItem = this.entradaSeleccionada.esAccesorio
                ? this.entradaSeleccionada.nombre
                : this.entradaSeleccionada.nombre + "  (Disponibles: x" + this.entradaSeleccionada.cantidad + ")";
        g2.drawString(tituloItem, xTexto, y + 32);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        String desc = this.entradaSeleccionada.descripcion;
        if (desc.length() > 46) {
            desc = desc.substring(0, 44) + "...";
        }
        g2.drawString(desc, xTexto, y + 54);

        String subEstado;
        Color colorEstado;
        if (this.entradaSeleccionada.esAccesorio) {
            if (!this.entradaSeleccionada.desbloqueado) {
                subEstado = "Estado: Bloqueado por logro tadeísta";
                colorEstado = new Color(255, 130, 120);
            } else if (this.entradaSeleccionada.equipado) {
                subEstado = "Estado: Equipado actualmente en Cabito";
                colorEstado = new Color(88, 214, 141);
            } else {
                subEstado = "Estado: Desbloqueado y listo para equipar";
                colorEstado = BORDE_CELESTE;
            }
        } else {
            subEstado = "Haz clic en 'USAR OBJETO' o doble clic en la casilla";
            colorEstado = BORDE_CELESTE;
        }
        g2.setColor(colorEstado);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString(subEstado, xTexto, y + 74);
    }

    private static void dibujarIconoRespaldo(Graphics2D g2, String nombre, int x, int y, int tam) {
        int cx = x + tam / 2;
        int cy = y + tam / 2;
        String minuscula = (nombre != null) ? nombre.toLowerCase() : "";
        if (minuscula.contains("energ")) {
            int lataX = cx - 14;
            int lataY = cy - 21;
            int lataW = 28;
            int lataH = 42;

            g2.setColor(new Color(190, 200, 212));
            g2.fillRoundRect(lataX + 3, lataY - 2, lataW - 6, 5, 3, 3);

            GradientPaint gpLata = new GradientPaint(lataX, lataY, AZUL_GORRA_MEDIO, lataX + lataW, lataY + lataH, AZUL_PROFUNDO);
            g2.setPaint(gpLata);
            g2.fillRoundRect(lataX, lataY + 2, lataW, lataH - 2, 8, 8);

            g2.setColor(DORADO_HUESO);
            g2.fillRect(lataX + 2, lataY + 9, lataW - 4, 4);
            g2.fillRect(lataX + 2, lataY + lataH - 8, lataW - 4, 3);

            g2.setColor(AZUL_PROFUNDO);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(lataX, lataY + 2, lataW, lataH - 2, 8, 8);

            Polygon rayo = new Polygon(
                    new int[]{cx + 2, cx - 6, cx, cx - 2, cx + 7, cx + 1},
                    new int[]{cy - 8, cy + 1, cy + 1, cy + 11, cy - 1, cy - 1},
                    6
            );
            g2.setColor(new Color(255, 215, 0));
            g2.fill(rayo);
        } else if (minuscula.contains("pelota") || minuscula.contains("juguete")) {
            int r = 21;
            Ellipse2D.Float circuloPelota = new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2);

            GradientPaint gpPelota = new GradientPaint(
                    cx - r, cy - r, new Color(72, 122, 198),
                    cx + r, cy + r, AZUL_PROFUNDO
            );
            g2.setPaint(gpPelota);
            g2.fill(circuloPelota);

            Shape clipAnterior = g2.getClip();
            g2.clip(circuloPelota);

            g2.setColor(DORADO_HUESO);
            g2.setStroke(new BasicStroke(4.2f));
            g2.drawArc(cx - r - 10, cy - r + 3, r * 2, r * 2, -45, 115);
            g2.drawArc(cx - r + 10, cy - r - 3, r * 2, r * 2, 135, 115);

            g2.setColor(new Color(255, 255, 255, 95));
            g2.fillOval(cx - r + 5, cy - r + 4, 14, 9);

            g2.setClip(clipAnterior);

            Polygon estrella = crearEstrella(cx, cy, 10, 4);
            g2.setColor(new Color(255, 215, 0));
            g2.fill(estrella);
            g2.setColor(AZUL_PROFUNDO);
            g2.setStroke(new BasicStroke(1.1f));
            g2.draw(estrella);

            g2.setColor(AZUL_PROFUNDO);
            g2.setStroke(new BasicStroke(1.8f));
            g2.draw(circuloPelota);
        } else if (minuscula.contains("helado") || minuscula.contains("galleta")) {
            Polygon cono = new Polygon(
                    new int[]{cx - 13, cx + 13, cx},
                    new int[]{cy + 1, cy + 1, cy + 23},
                    3
            );
            GradientPaint gpCono = new GradientPaint(
                    cx - 10, cy + 2, new Color(230, 172, 92),
                    cx + 8, cy + 22, new Color(184, 120, 44)
            );
            g2.setPaint(gpCono);
            g2.fill(cono);

            g2.setColor(new Color(148, 92, 28));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawLine(cx - 8, cy + 5, cx + 3, cy + 16);
            g2.drawLine(cx + 8, cy + 5, cx - 3, cy + 16);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(cono);

            g2.setColor(new Color(255, 130, 178));
            g2.fillOval(cx - 17, cy - 13, 20, 18);
            g2.setColor(new Color(255, 220, 105));
            g2.fillOval(cx - 3, cy - 13, 20, 18);
            g2.setColor(new Color(145, 225, 250));
            g2.fillOval(cx - 11, cy - 22, 22, 19);

            g2.setColor(AZUL_PROFUNDO);
            g2.setStroke(new BasicStroke(1.3f));
            g2.drawArc(cx - 17, cy - 13, 20, 18, 70, 210);
            g2.drawArc(cx - 3, cy - 13, 20, 18, -100, 210);
            g2.drawArc(cx - 11, cy - 22, 22, 19, 0, 180);

            g2.setColor(new Color(224, 48, 62));
            g2.fillOval(cx - 4, cy - 27, 8, 8);
        } else {
            g2.setColor(AZUL_GORRA_MEDIO);
            g2.fillOval(cx - 18, cy - 18, 36, 36);
            g2.setColor(DORADO_HUESO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            FontMetrics fm = g2.getFontMetrics();
            String ini = (!minuscula.isBlank()) ? nombre.substring(0, 1).toUpperCase() : "★";
            g2.drawString(ini, cx - fm.stringWidth(ini) / 2, cy + 6);
        }
    }

    private static Polygon crearEstrella(int cx, int cy, int radioExt, int radioInt) {
        Polygon estrella = new Polygon();
        for (int i = 0; i < 10; i++) {
            double angulo = Math.toRadians(-90 + i * 36);
            int r = (i % 2 == 0) ? radioExt : radioInt;
            int px = (int) Math.round(cx + r * Math.cos(angulo));
            int py = (int) Math.round(cy + r * Math.sin(angulo));
            estrella.addPoint(px, py);
        }
        return estrella;
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

    private static class EntradaInventario {

        final String nombre;
        final String descripcion;
        final int cantidad;
        final boolean desbloqueado;
        final boolean equipado;
        final boolean esAccesorio;
        final String rutaImagen;
        final JButton botonAccion;

        public EntradaInventario(
                String nombre,
                String descripcion,
                int cantidad,
                boolean desbloqueado,
                boolean equipado,
                boolean esAccesorio,
                String rutaImagen,
                JButton botonAccion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.cantidad = cantidad;
            this.desbloqueado = desbloqueado;
            this.equipado = equipado;
            this.esAccesorio = esAccesorio;
            this.rutaImagen = rutaImagen;
            this.botonAccion = botonAccion;
        }
    }

    private class CasillaInventario extends JPanel {

        private final EntradaInventario entrada;
        private final boolean seleccionada;
        private boolean hover;

        public CasillaInventario(EntradaInventario entrada, boolean seleccionada) {
            this.entrada = entrada;
            this.seleccionada = seleccionada;
            this.hover = false;
            setOpaque(false);

            if (entrada != null) {
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

                    @Override
                    public void mousePressed(MouseEvent e) {
                        seleccionarEntrada(entrada);
                        if (e.getClickCount() >= 2 && entrada.botonAccion != null && entrada.botonAccion.isEnabled()) {
                            entrada.botonAccion.doClick();
                        }
                    }
                });
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            RoundRectangle2D.Float formaCasilla = new RoundRectangle2D.Float(2, 2, w - 4, h - 4, 18, 18);

            if (this.entrada == null) {
                g2.setColor(new Color(15, 27, 50));
                g2.fill(formaCasilla);
                g2.setColor(new Color(48, 76, 122));
                g2.setStroke(new BasicStroke(2.0f));
                g2.draw(formaCasilla);
                g2.dispose();
                return;
            }

            if (this.entrada.esAccesorio && !this.entrada.desbloqueado) {
                g2.setColor(new Color(195, 204, 216));
            } else if (this.seleccionada || this.hover) {
                g2.setColor(new Color(245, 250, 255));
            } else {
                g2.setColor(new Color(230, 240, 254));
            }
            g2.fill(formaCasilla);

            Color colorBorde;
            float grosor;
            if (this.seleccionada) {
                colorBorde = DORADO_HUESO;
                grosor = 3.2f;
            } else if (this.hover) {
                colorBorde = new Color(255, 225, 110);
                grosor = 2.6f;
            } else if (this.entrada.esAccesorio && this.entrada.equipado) {
                colorBorde = VERDE_EQUIPADO;
                grosor = 2.8f;
            } else {
                colorBorde = BORDE_CELESTE;
                grosor = 2.0f;
            }

            g2.setColor(colorBorde);
            g2.setStroke(new BasicStroke(grosor));
            g2.draw(formaCasilla);

            int tamIcono = 66;
            int ix = (w - tamIcono) / 2;
            int iy = (h - tamIcono) / 2 - 4;

            Image img = GestorRecursos.obtenerImagenIcono(this.entrada.rutaImagen, tamIcono, tamIcono);
            if (img != null) {
                g2.drawImage(img, ix, iy, tamIcono, tamIcono, this);
            } else {
                dibujarIconoRespaldo(g2, this.entrada.nombre, ix, iy, tamIcono);
            }

            if (!this.entrada.esAccesorio && this.entrada.cantidad > 0) {
                String txtCant = "x" + this.entrada.cantidad;
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int bw = Math.max(30, fm.stringWidth(txtCant) + 12);
                int bh = 22;
                int bx = w - bw - 6;
                int by = h - bh - 6;

                g2.setColor(AZUL_PROFUNDO);
                g2.fillRoundRect(bx, by, bw, bh, 10, 10);
                g2.setColor(DORADO_HUESO);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(bx, by, bw, bh, 10, 10);

                g2.setColor(DORADO_HUESO);
                g2.drawString(txtCant, bx + (bw - fm.stringWidth(txtCant)) / 2, by + 15);
            } else if (this.entrada.esAccesorio) {
                String txtEstado = !this.entrada.desbloqueado ? "BLOQ." : (this.entrada.equipado ? "EQUIPADO" : "LISTO");
                Color bgBadge = !this.entrada.desbloqueado
                        ? new Color(120, 130, 145)
                        : (this.entrada.equipado ? VERDE_EQUIPADO : AZUL_GORRA_MEDIO);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                FontMetrics fm = g2.getFontMetrics();
                int bw = fm.stringWidth(txtEstado) + 12;
                int bh = 18;
                int bx = (w - bw) / 2;
                int by = h - bh - 5;

                g2.setColor(bgBadge);
                g2.fillRoundRect(bx, by, bw, bh, 8, 8);
                g2.setColor(Color.WHITE);
                g2.drawString(txtEstado, bx + 6, by + 13);
            }

            g2.dispose();
        }
    }

    private static class BotonCategoriaLateral extends JButton {

        private boolean activa;
        private boolean hover;

        public BotonCategoriaLateral(String texto, boolean activa) {
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

            RoundRectangle2D.Float forma = new RoundRectangle2D.Float(2, 2, w - 4, h - 4, 14, 14);

            if (this.activa) {
                GradientPaint gp = new GradientPaint(0, 0, DORADO_HUESO, 0, h, DORADO_OSCURO);
                g2.setPaint(gp);
                g2.fill(forma);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.2f));
                g2.draw(forma);
                g2.setColor(AZUL_FONDO_OSCURO);
            } else {
                Color sup = this.hover ? AZUL_GORRA_CLARO : AZUL_GORRA_MEDIO;
                Color inf = this.hover ? AZUL_GORRA_MEDIO : AZUL_GORRA_OSCURO;
                g2.setPaint(new GradientPaint(0, 0, sup, 0, h, inf));
                g2.fill(forma);
                g2.setColor(this.hover ? DORADO_HUESO : BORDE_CELESTE);
                g2.setStroke(new BasicStroke(1.8f));
                g2.draw(forma);
                g2.setColor(Color.WHITE);
            }

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(getText())) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), tx, ty);

            g2.dispose();
        }
    }

    private static class BotonAccionInspector extends JButton {

        private final Color colorBase;
        private boolean hover;

        public BotonAccionInspector(String texto, Color colorBase, boolean habilitado) {
            super(texto);
            this.colorBase = colorBase;
            this.hover = false;
            setEnabled(habilitado);
            setFont(new Font("SansSerif", Font.BOLD, 13));
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

            Color sup = hover && isEnabled() ? DORADO_HUESO : this.colorBase.brighter();
            Color inf = hover && isEnabled() ? DORADO_OSCURO : this.colorBase;
            g2.setPaint(new GradientPaint(0, 0, sup, 0, h, inf));
            g2.fillRoundRect(1, 1, w - 2, h - 2, 20, 20);

            g2.setColor(hover && isEnabled() ? Color.WHITE : DORADO_HUESO);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(getText())) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(hover && isEnabled() ? AZUL_FONDO_OSCURO : Color.WHITE);
            g2.drawString(getText(), tx, ty);

            g2.dispose();
        }
    }

    private static class BotonVolverInventario extends JButton {

        private boolean hover;

        public BotonVolverInventario(String texto) {
            super(texto);
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

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(hover ? new Color(242, 98, 82) : new Color(224, 79, 62));
            g2.fillRoundRect(1, 1, w - 2, h - 2, 16, 16);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, 16, 16);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(getText())) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), tx, ty);

            g2.dispose();
        }
    }
}
