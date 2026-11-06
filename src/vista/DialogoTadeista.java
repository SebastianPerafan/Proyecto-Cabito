package vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class DialogoTadeista {

    public static final Color AZUL_PROFUNDO = new Color(30, 51, 90);
    public static final Color AZUL_GORRA_OSCURO = new Color(40, 67, 114);
    public static final Color AZUL_GORRA_MEDIO = new Color(56, 90, 148);
    public static final Color AZUL_HOVER_OSCURO = new Color(63, 104, 168);
    public static final Color AZUL_HOVER_CLARO = new Color(82, 130, 206);
    public static final Color GRIS_SECUNDARIO = new Color(92, 108, 132);
    public static final Color GRIS_SECUNDARIO_HOVER = new Color(112, 130, 156);
    public static final Color DORADO_HUESO = new Color(246, 208, 77);
    public static final Color BORDE_CELESTE = new Color(184, 208, 240);
    public static final Color TEXTO_OSCURO = new Color(24, 38, 66);
    public static final Color FONDO_BLANCO = Color.WHITE;
    private static final Color TRANSPARENTE = new Color(0, 0, 0, 0);

    private DialogoTadeista() {
    }

    public static void mostrarMensaje(Component parent, String titulo, String mensaje) {
        JDialog dialogo = crearDialogoBase(parent, titulo);

        PanelTarjetaRedondeada tarjeta = new PanelTarjetaRedondeada(22);
        tarjeta.setLayout(new BorderLayout());

        JPanel cabecera = crearCabeceraTarjeta(dialogo, (titulo != null && !titulo.isBlank()) ? titulo : "Aviso Tadeísta");
        tarjeta.add(cabecera, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setOpaque(false);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(18, 24, 14, 24));

        EtiquetaMultilinea lblMensaje = new EtiquetaMultilinea(
                mensaje,
                new Font("SansSerif", Font.PLAIN, 14),
                TEXTO_OSCURO,
                370,
                56
        );
        cuerpo.add(lblMensaje, BorderLayout.CENTER);
        tarjeta.add(cuerpo, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 20, 18, 20));

        BotonCapsula btnAceptar = new BotonCapsula(null, "Aceptar", false, 13);
        btnAceptar.setPreferredSize(new Dimension(150, 36));
        btnAceptar.addActionListener(e -> dialogo.dispose());
        panelBotones.add(btnAceptar);

        tarjeta.add(panelBotones, BorderLayout.SOUTH);

        dialogo.setContentPane(tarjeta);
        dialogo.getRootPane().setDefaultButton(btnAceptar);
        mostrarCentrado(dialogo, parent, 430);
    }

    public static int mostrarOpciones(Component parent, String titulo, String mensaje, String[] opciones) {
        if (opciones == null || opciones.length == 0) {
            mostrarMensaje(parent, titulo, mensaje);
            return -1;
        }

        final int[] seleccion = {-1};
        JDialog dialogo = crearDialogoBase(parent, titulo);

        PanelTarjetaRedondeada tarjeta = new PanelTarjetaRedondeada(22);
        tarjeta.setLayout(new BorderLayout());

        JPanel cabecera = crearCabeceraTarjeta(dialogo, (titulo != null && !titulo.isBlank()) ? titulo : "Seleccionar Opción");
        tarjeta.add(cabecera, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(16, 24, 20, 24));

        EtiquetaMultilinea lblPregunta = new EtiquetaMultilinea(
                mensaje,
                new Font("SansSerif", Font.BOLD, 14),
                TEXTO_OSCURO,
                380,
                48
        );
        centro.add(lblPregunta, BorderLayout.NORTH);

        JPanel panelOpciones = new JPanel(new GridLayout(0, 1, 0, 8));
        panelOpciones.setOpaque(false);

        for (int i = 0; i < opciones.length; i++) {
            final int indice = i;
            String textoOpcion = opciones[i] != null ? opciones[i] : "";
            boolean esCancelar = textoOpcion.equalsIgnoreCase("Cancelar") || textoOpcion.equalsIgnoreCase("Cerrar");
            BotonCapsula btnOpcion = new BotonCapsula(null, textoOpcion, esCancelar, 13);
            btnOpcion.setPreferredSize(new Dimension(360, 36));
            btnOpcion.addActionListener(e -> {
                seleccion[0] = indice;
                dialogo.dispose();
            });
            panelOpciones.add(btnOpcion);
        }

        centro.add(panelOpciones, BorderLayout.CENTER);
        tarjeta.add(centro, BorderLayout.CENTER);

        dialogo.setContentPane(tarjeta);
        mostrarCentrado(dialogo, parent, 440);
        return seleccion[0];
    }

    public static int mostrarPreguntaExplorar(Component parent, String titulo, String pregunta, String[] opciones) {
        if (opciones == null || opciones.length == 0) {
            mostrarMensaje(parent, titulo, pregunta);
            return -1;
        }

        final int[] seleccion = {-1};
        JDialog dialogo = crearDialogoBase(parent, titulo);

        JPanel contenedor = new JPanel(new BorderLayout(0, 6));
        contenedor.setOpaque(false);
        contenedor.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        PanelCapsula capsulaPregunta = new PanelCapsula(24);
        capsulaPregunta.setLayout(new BorderLayout(0, 2));
        capsulaPregunta.setBorder(BorderFactory.createEmptyBorder(7, 20, 8, 20));
        habilitarArrastre(dialogo, capsulaPregunta);

        JLabel lblInsignia = new JLabel(
                (titulo != null && !titulo.isBlank()) ? titulo.toUpperCase() : "EXPLORACIÓN TADEÍSTA",
                SwingConstants.CENTER
        );
        lblInsignia.setForeground(DORADO_HUESO);
        lblInsignia.setFont(new Font("SansSerif", Font.BOLD, 11));
        capsulaPregunta.add(lblInsignia, BorderLayout.NORTH);

        EtiquetaMultilinea lblPregunta = new EtiquetaMultilinea(
                pregunta,
                new Font("SansSerif", Font.BOLD, 13),
                Color.WHITE,
                545,
                44
        );
        capsulaPregunta.add(lblPregunta, BorderLayout.CENTER);

        contenedor.add(capsulaPregunta, BorderLayout.CENTER);

        JPanel panelRespuestas = construirPanelOpcionesExplorar(dialogo, opciones, seleccion);
        contenedor.add(panelRespuestas, BorderLayout.SOUTH);

        dialogo.setContentPane(contenedor);
        mostrarEnHudExplorar(dialogo, parent, 600, 162);
        return seleccion[0];
    }

    private static JPanel construirPanelOpcionesExplorar(JDialog dialogo, String[] opciones, int[] seleccion) {
        BotonCapsula[] botones = new BotonCapsula[opciones.length];
        char letra = 'A';

        for (int i = 0; i < opciones.length; i++) {
            final int indice = i;
            String textoOpcion = opciones[i] != null ? opciones[i] : "";
            String prefijo = letra + ": ";
            letra++;

            BotonCapsula btn = new BotonCapsula(prefijo, textoOpcion, false, 12);
            btn.setPreferredSize(new Dimension(288, 36));
            btn.addActionListener(e -> {
                seleccion[0] = indice;
                dialogo.dispose();
            });
            botones[i] = btn;
        }

        if (opciones.length == 1) {
            JPanel panelUnico = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            panelUnico.setOpaque(false);
            panelUnico.add(botones[0]);
            return panelUnico;
        }

        if (opciones.length % 2 == 1) {
            JPanel panelCompuesto = new JPanel(new BorderLayout(0, 5));
            panelCompuesto.setOpaque(false);

            int cantidadPares = opciones.length - 1;
            JPanel panelSuperior = new JPanel(new GridLayout(0, 2, 10, 5));
            panelSuperior.setOpaque(false);
            for (int i = 0; i < cantidadPares; i++) {
                panelSuperior.add(botones[i]);
            }

            JPanel panelInferiorCentrado = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            panelInferiorCentrado.setOpaque(false);
            botones[opciones.length - 1].setPreferredSize(new Dimension(296, 36));
            panelInferiorCentrado.add(botones[opciones.length - 1]);

            panelCompuesto.add(panelSuperior, BorderLayout.CENTER);
            panelCompuesto.add(panelInferiorCentrado, BorderLayout.SOUTH);
            return panelCompuesto;
        }

        JPanel panelCuadricula = new JPanel(new GridLayout(0, 2, 10, 5));
        panelCuadricula.setOpaque(false);
        for (BotonCapsula boton : botones) {
            panelCuadricula.add(boton);
        }
        return panelCuadricula;
    }

    public static String mostrarSelectorUsuario(
            Component parent,
            List<String> perfilesExistentes,
            String usuarioActual,
            boolean permitirCancelar) {
        final String[] resultadoUsuario = {null};
        JDialog dialogo = crearDialogoBase(parent, "Perfil Tadeísta");

        PanelTarjetaRedondeada tarjeta = new PanelTarjetaRedondeada(22);
        tarjeta.setLayout(new BorderLayout());

        JPanel cabecera = crearCabeceraTarjeta(dialogo, "Seleccionar o Crear Jugador");
        tarjeta.add(cabecera, BorderLayout.NORTH);

        JPanel panelCuerpo = new JPanel(new GridLayout(0, 1, 0, 7));
        panelCuerpo.setOpaque(false);
        panelCuerpo.setBorder(BorderFactory.createEmptyBorder(16, 24, 12, 24));

        JComboBox<String> comboPerfiles = new JComboBox<>();
        comboPerfiles.setFont(new Font("SansSerif", Font.BOLD, 13));
        comboPerfiles.setBackground(FONDO_BLANCO);
        comboPerfiles.setForeground(TEXTO_OSCURO);

        if (perfilesExistentes != null) {
            for (String perfil : perfilesExistentes) {
                comboPerfiles.addItem(perfil);
            }
        }
        if (usuarioActual != null && !usuarioActual.isBlank()) {
            comboPerfiles.setSelectedItem(usuarioActual);
        }

        JTextField txtNuevoUsuario = new JTextField(16);
        txtNuevoUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtNuevoUsuario.setForeground(TEXTO_OSCURO);
        txtNuevoUsuario.setBackground(FONDO_BLANCO);
        txtNuevoUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AZUL_GORRA_MEDIO, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        if (perfilesExistentes != null && !perfilesExistentes.isEmpty()) {
            JLabel lblExistente = new JLabel("Selecciona un perfil registrado:");
            lblExistente.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblExistente.setForeground(TEXTO_OSCURO);
            panelCuerpo.add(lblExistente);
            panelCuerpo.add(comboPerfiles);

            JLabel lblNuevo = new JLabel("O escribe un nombre nuevo para crear otro perfil:");
            lblNuevo.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblNuevo.setForeground(TEXTO_OSCURO);
            panelCuerpo.add(lblNuevo);
        } else {
            JLabel lblSinPerfiles = new JLabel("Escribe tu nombre de jugador para comenzar:");
            lblSinPerfiles.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblSinPerfiles.setForeground(TEXTO_OSCURO);
            panelCuerpo.add(lblSinPerfiles);
        }
        panelCuerpo.add(txtNuevoUsuario);
        tarjeta.add(panelCuerpo, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(4, 20, 18, 20));

        BotonCapsula btnConfirmar = new BotonCapsula(null, "Continuar", false, 13);
        btnConfirmar.setPreferredSize(new Dimension(150, 36));
        btnConfirmar.addActionListener(e -> {
            String escrito = txtNuevoUsuario.getText() != null ? txtNuevoUsuario.getText().trim() : "";
            String elegido = (String) comboPerfiles.getSelectedItem();
            if (!escrito.isEmpty()) {
                resultadoUsuario[0] = escrito;
            } else if (elegido != null && !elegido.isBlank()) {
                resultadoUsuario[0] = elegido;
            } else {
                resultadoUsuario[0] = "jugador";
            }
            dialogo.dispose();
        });
        panelBotones.add(btnConfirmar);

        if (permitirCancelar) {
            BotonCapsula btnCancelar = new BotonCapsula(null, "Cancelar", true, 13);
            btnCancelar.setPreferredSize(new Dimension(135, 36));
            btnCancelar.addActionListener(e -> {
                resultadoUsuario[0] = null;
                dialogo.dispose();
            });
            panelBotones.add(btnCancelar);
        }

        tarjeta.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setContentPane(tarjeta);
        dialogo.getRootPane().setDefaultButton(btnConfirmar);
        mostrarCentrado(dialogo, parent, 440);
        return resultadoUsuario[0];
    }

    private static JPanel crearCabeceraTarjeta(JDialog dialogo, String tituloMostrar) {
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setOpaque(false);
        panelCabecera.setPreferredSize(new Dimension(100, 44));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        JLabel lblTitulo = new JLabel(tituloMostrar, SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        panelCabecera.add(lblTitulo, BorderLayout.CENTER);

        habilitarArrastre(dialogo, panelCabecera);
        return panelCabecera;
    }

    private static JDialog crearDialogoBase(Component parent, String titulo) {
        Window ventanaPadre = obtenerVentanaPadre(parent);
        JDialog dialogo;
        if (ventanaPadre instanceof Frame frame) {
            dialogo = new JDialog(frame, titulo, true);
        } else if (ventanaPadre instanceof java.awt.Dialog dialog) {
            dialogo = new JDialog(dialog, titulo, true);
        } else {
            dialogo = new JDialog((Frame) null, titulo, true);
        }
        dialogo.setUndecorated(true);
        try {
            dialogo.setBackground(TRANSPARENTE);
        } catch (Exception ignored) {
        }
        return dialogo;
    }

    private static Window obtenerVentanaPadre(Component parent) {
        if (parent == null) {
            return null;
        }
        if (parent instanceof Window ventana) {
            return ventana;
        }
        return SwingUtilities.getWindowAncestor(parent);
    }

    private static void habilitarArrastre(JDialog dialogo, Component componente) {
        final Point puntoInicial = new Point();
        MouseAdapter arrastrador = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                puntoInicial.x = e.getX();
                puntoInicial.y = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point ubicacion = dialogo.getLocation();
                dialogo.setLocation(
                        ubicacion.x + e.getX() - puntoInicial.x,
                        ubicacion.y + e.getY() - puntoInicial.y
                );
            }
        };
        componente.addMouseListener(arrastrador);
        componente.addMouseMotionListener(arrastrador);
    }

    private static void mostrarCentrado(JDialog dialogo, Component parent, int anchoMinimo) {
        dialogo.pack();
        int ancho = Math.max(anchoMinimo, dialogo.getWidth());
        int alto = dialogo.getHeight();
        dialogo.setSize(ancho, alto);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setVisible(true);
    }

    private static void mostrarEnHudExplorar(JDialog dialogo, Component parent, int anchoMaximo, int altoMinimo) {
        dialogo.pack();
        Window ventanaPadre = obtenerVentanaPadre(parent);
        int limiteAnchoVentana = (ventanaPadre != null && ventanaPadre.getWidth() > 200)
                ? ventanaPadre.getWidth() - 160
                : anchoMaximo;
        int ancho = Math.min(anchoMaximo, Math.max(540, limiteAnchoVentana));
        int alto = Math.max(altoMinimo, Math.min(180, dialogo.getHeight()));
        dialogo.setSize(ancho, alto);

        if (ventanaPadre != null && ventanaPadre.isShowing()) {
            Point ubicacionPadre = ventanaPadre.getLocationOnScreen();
            int x = ubicacionPadre.x + (ventanaPadre.getWidth() - ancho) / 2;
            int y = ubicacionPadre.y + ventanaPadre.getHeight() - alto - 68;
            dialogo.setLocation(x, y);
        } else {
            dialogo.setLocationRelativeTo(parent);
        }
        dialogo.setVisible(true);
    }

    private static List<String> dividirEnLineas(String texto, FontMetrics fm, int anchoMaximo) {
        List<String> lineas = new ArrayList<>();
        if (texto == null || texto.isEmpty()) {
            lineas.add("");
            return lineas;
        }

        int limite = Math.max(60, anchoMaximo);
        String[] parrafos = texto.split("\n");
        for (String parrafo : parrafos) {
            String limpio = parrafo.trim();
            if (limpio.isEmpty()) {
                lineas.add("");
                continue;
            }
            String[] palabras = limpio.split("\\s+");
            StringBuilder lineaActual = new StringBuilder();
            for (String palabra : palabras) {
                if (lineaActual.length() == 0) {
                    lineaActual.append(palabra);
                } else {
                    String candidata = lineaActual + " " + palabra;
                    if (fm.stringWidth(candidata) <= limite) {
                        lineaActual.append(" ").append(palabra);
                    } else {
                        lineas.add(lineaActual.toString());
                        lineaActual.setLength(0);
                        lineaActual.append(palabra);
                    }
                }
            }
            if (lineaActual.length() > 0) {
                lineas.add(lineaActual.toString());
            }
        }
        return lineas;
    }

    private static class EtiquetaMultilinea extends JComponent {

        private final String texto;
        private final Font fuente;
        private final Color colorTexto;
        private final int anchoPreferido;
        private final int altoMinimo;

        public EtiquetaMultilinea(String texto, Font fuente, Color colorTexto, int anchoPreferido, int altoMinimo) {
            this.texto = (texto != null) ? texto : "";
            this.fuente = fuente;
            this.colorTexto = colorTexto;
            this.anchoPreferido = anchoPreferido;
            this.altoMinimo = altoMinimo;
            setOpaque(false);
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(this.fuente);
            List<String> lineas = dividirEnLineas(this.texto, fm, this.anchoPreferido - 8);
            int altoCalculado = lineas.size() * (fm.getHeight() + 1) + 4;
            return new Dimension(this.anchoPreferido, Math.max(this.altoMinimo, altoCalculado));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            Font fuenteUsar = this.fuente;
            g2.setFont(fuenteUsar);
            FontMetrics fm = g2.getFontMetrics();

            List<String> lineas = dividirEnLineas(this.texto, fm, w - 8);
            int interlineado = fm.getHeight();
            int altoTotalTexto = lineas.size() * interlineado;

            if (altoTotalTexto > h && fuenteUsar.getSize() > 11) {
                fuenteUsar = fuenteUsar.deriveFont((float) (fuenteUsar.getSize() - 1));
                g2.setFont(fuenteUsar);
                fm = g2.getFontMetrics();
                lineas = dividirEnLineas(this.texto, fm, w - 8);
                interlineado = fm.getHeight();
                altoTotalTexto = lineas.size() * interlineado;
            }

            int y = Math.max(fm.getAscent(), (h - altoTotalTexto) / 2 + fm.getAscent());
            g2.setColor(this.colorTexto);

            for (String linea : lineas) {
                int anchoLinea = fm.stringWidth(linea);
                int x = Math.max(2, (w - anchoLinea) / 2);
                g2.drawString(linea, x, y);
                y += interlineado;
            }

            g2.dispose();
        }
    }

    private static class PanelTarjetaRedondeada extends JPanel {

        private final int radio;
        private static final int ALTURA_CABECERA = 44;

        public PanelTarjetaRedondeada(int radio) {
            this.radio = radio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            RoundRectangle2D.Float tarjetaCompleta = new RoundRectangle2D.Float(1.5f, 1.5f, w - 3f, h - 3f, radio, radio);
            g2.setColor(FONDO_BLANCO);
            g2.fill(tarjetaCompleta);

            Area areaCabecera = new Area(tarjetaCompleta);
            areaCabecera.intersect(new Area(new Rectangle2D.Float(0, 0, w, ALTURA_CABECERA)));

            GradientPaint degradadoCabecera = new GradientPaint(
                    0, 0, AZUL_GORRA_MEDIO,
                    0, ALTURA_CABECERA, AZUL_GORRA_OSCURO
            );
            g2.setPaint(degradadoCabecera);
            g2.fill(areaCabecera);

            g2.setColor(BORDE_CELESTE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(3, ALTURA_CABECERA, w - 4, ALTURA_CABECERA);

            g2.setColor(AZUL_PROFUNDO);
            g2.setStroke(new BasicStroke(2.8f));
            g2.draw(tarjetaCompleta);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class PanelCapsula extends JPanel {

        private final int radio;

        public PanelCapsula(int radio) {
            this.radio = radio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            Shape formaExterior = new RoundRectangle2D.Float(1.5f, 1.5f, w - 3f, h - 3f, radio, radio);
            GradientPaint degradado = new GradientPaint(
                    0, 0, AZUL_GORRA_OSCURO,
                    0, h, AZUL_GORRA_MEDIO
            );
            g2.setPaint(degradado);
            g2.fill(formaExterior);

            g2.setColor(AZUL_PROFUNDO);
            g2.setStroke(new BasicStroke(3.0f));
            g2.draw(formaExterior);

            Shape formaInterior = new RoundRectangle2D.Float(3f, 3f, w - 6f, h - 6f, radio - 2, radio - 2);
            g2.setColor(BORDE_CELESTE);
            g2.setStroke(new BasicStroke(2.0f));
            g2.draw(formaInterior);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class BotonCapsula extends JButton {

        private final String prefijoDorado;
        private final String textoBoton;
        private final boolean secundario;
        private final int tamFuenteBase;
        private boolean hover;

        public BotonCapsula(String prefijoDorado, String texto, boolean secundario, int tamFuente) {
            super("");
            this.prefijoDorado = (prefijoDorado != null) ? prefijoDorado : "";
            this.textoBoton = (texto != null) ? texto : "";
            this.secundario = secundario;
            this.tamFuenteBase = tamFuente;
            this.hover = false;

            setFont(new Font("SansSerif", Font.BOLD, tamFuente));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));

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
            int radio = Math.min(34, h - 2);

            Color colorSuperior;
            Color colorInferior;
            Color colorBorde;
            float grosorBorde;

            if (secundario) {
                colorSuperior = hover ? GRIS_SECUNDARIO_HOVER : GRIS_SECUNDARIO;
                colorInferior = hover ? GRIS_SECUNDARIO : AZUL_PROFUNDO;
                colorBorde = hover ? DORADO_HUESO : BORDE_CELESTE;
                grosorBorde = hover ? 2.2f : 1.6f;
            } else if (hover) {
                colorSuperior = AZUL_HOVER_CLARO;
                colorInferior = AZUL_HOVER_OSCURO;
                colorBorde = DORADO_HUESO;
                grosorBorde = 2.4f;
            } else {
                colorSuperior = AZUL_GORRA_MEDIO;
                colorInferior = AZUL_GORRA_OSCURO;
                colorBorde = BORDE_CELESTE;
                grosorBorde = 1.8f;
            }

            RoundRectangle2D.Float capsula = new RoundRectangle2D.Float(1.5f, 1.5f, w - 3f, h - 3f, radio, radio);
            GradientPaint pintura = new GradientPaint(0, 0, colorSuperior, 0, h, colorInferior);
            g2.setPaint(pintura);
            g2.fill(capsula);

            g2.setColor(AZUL_PROFUNDO);
            g2.setStroke(new BasicStroke(grosorBorde + 1.0f));
            g2.draw(capsula);

            RoundRectangle2D.Float bordeInterno = new RoundRectangle2D.Float(2.2f, 2.2f, w - 4.4f, h - 4.4f, radio - 1, radio - 1);
            g2.setColor(colorBorde);
            g2.setStroke(new BasicStroke(grosorBorde));
            g2.draw(bordeInterno);

            pintarTextoBoton(g2, w, h);

            g2.dispose();
            super.paintComponent(g);
        }

        private void pintarTextoBoton(Graphics2D g2, int w, int h) {
            int margenHorizontal = 24;
            int anchoDisponible = Math.max(60, w - margenHorizontal);
            String textoCompleto = this.prefijoDorado + this.textoBoton;

            Font fuente = new Font("SansSerif", Font.BOLD, this.tamFuenteBase);
            g2.setFont(fuente);
            FontMetrics fm = g2.getFontMetrics();

            if (fm.stringWidth(textoCompleto) <= anchoDisponible) {
                int anchoPrefijo = this.prefijoDorado.isEmpty() ? 0 : fm.stringWidth(this.prefijoDorado);
                int anchoTotal = anchoPrefijo + fm.stringWidth(this.textoBoton);
                int xInicio = Math.max(8, (w - anchoTotal) / 2);
                int yBase = (h - fm.getHeight()) / 2 + fm.getAscent();

                if (!this.prefijoDorado.isEmpty()) {
                    g2.setColor(DORADO_HUESO);
                    g2.drawString(this.prefijoDorado, xInicio, yBase);
                }
                g2.setColor(Color.WHITE);
                g2.drawString(this.textoBoton, xInicio + anchoPrefijo, yBase);
                return;
            }

            Font fuenteCompacta = new Font("SansSerif", Font.BOLD, Math.max(11, this.tamFuenteBase - 1));
            g2.setFont(fuenteCompacta);
            fm = g2.getFontMetrics();

            List<String> lineas = dividirEnLineas(textoCompleto, fm, anchoDisponible);
            int interlineado = fm.getHeight() - 2;
            int altoTotal = lineas.size() * interlineado;
            int y = Math.max(fm.getAscent(), (h - altoTotal) / 2 + fm.getAscent());

            for (int i = 0; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                int anchoLinea = fm.stringWidth(linea);
                int x = Math.max(8, (w - anchoLinea) / 2);

                if (i == 0 && !this.prefijoDorado.isEmpty() && linea.startsWith(this.prefijoDorado)) {
                    String resto = linea.substring(this.prefijoDorado.length());
                    int anchoPref = fm.stringWidth(this.prefijoDorado);
                    g2.setColor(DORADO_HUESO);
                    g2.drawString(this.prefijoDorado, x, y);
                    g2.setColor(Color.WHITE);
                    g2.drawString(resto, x + anchoPref, y);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.drawString(linea, x, y);
                }
                y += interlineado;
            }
        }
    }
}
