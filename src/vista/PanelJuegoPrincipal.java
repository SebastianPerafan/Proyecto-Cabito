package vista;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
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

    private static final Color AMARILLO_DORADO = new Color(255, 215, 0);
    private static final Color CELESTE_BRILLANTE = new Color(90, 200, 250);
    private static final Color VERDE_ESTADO = new Color(110, 235, 155);

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

    private final JLabel lblUsuario;
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
    private final JButton btnGuardarPartida;
    private final JButton btnCargarPartida;
    private final JButton btnCambiarMundo;
    private final JButton btnCambiarUsuario;

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

        this.panelUI = new PanelCapaUI();
        this.panelUI.setOpaque(false);
        this.panelUI.setBounds(0, 0, ANCHO_PANEL, ALTO_PANEL);
        this.layeredPane.add(this.panelUI, JLayeredPane.POPUP_LAYER);

        this.pbHambre = crearBarraProgreso(
                "Hambre: 80%", 80,
                new Color(245, 166, 35), new Color(224, 112, 20),
                20, 18
        );
        this.pbEnergia = crearBarraProgreso(
                "Energía: 100%", 100,
                new Color(255, 222, 45), new Color(235, 178, 12),
                20, 53
        );
        this.pbHigiene = crearBarraProgreso(
                "Higiene: 80%", 80,
                new Color(90, 200, 250), new Color(41, 128, 185),
                20, 88
        );
        this.pbFelicidad = crearBarraProgreso(
                "Felicidad: 80%", 80,
                new Color(188, 108, 222), new Color(138, 62, 175),
                20, 123
        );
        this.pbSalud = crearBarraProgreso(
                "Salud: 100%", 100,
                new Color(76, 217, 100), new Color(34, 166, 82),
                20, 158
        );

        this.panelUI.add(this.pbHambre);
        this.panelUI.add(this.pbEnergia);
        this.panelUI.add(this.pbHigiene);
        this.panelUI.add(this.pbFelicidad);
        this.panelUI.add(this.pbSalud);

        this.lblUsuario = crearEtiquetaInfo("Jugador: jugador", 244, 15, 332, 26, CELESTE_BRILLANTE);
        this.lblNivel = crearEtiquetaInfo("Nivel: 0", 244, 46, 162, 26, CELESTE_BRILLANTE);
        this.lblXp = crearEtiquetaInfo("XP: 0 / 100", 412, 46, 164, 26, AMARILLO_DORADO);
        this.lblMonedas = crearEtiquetaInfo("Monedas: 30", 244, 76, 162, 26, AMARILLO_DORADO);
        this.lblInteligencia = crearEtiquetaInfo("Inteligencia: 10", 412, 76, 164, 26, CELESTE_BRILLANTE);
        this.lblMundo = crearEtiquetaInfo("Mundo: La Plazoleta", 244, 106, 172, 26, Color.WHITE);
        this.lblEstado = crearEtiquetaInfo("Estado: Feliz", 420, 106, 156, 26, VERDE_ESTADO);

        this.panelUI.add(this.lblUsuario);
        this.panelUI.add(this.lblNivel);
        this.panelUI.add(this.lblXp);
        this.panelUI.add(this.lblMonedas);
        this.panelUI.add(this.lblInteligencia);
        this.panelUI.add(this.lblMundo);
        this.panelUI.add(this.lblEstado);

        this.btnCambiarMundo = crearBoton(
                "Cambiar Mundo", 598, 14, 180, 31,
                new Color(52, 152, 219), new Color(28, 98, 150), 28, 13
        );
        this.btnGuardarPartida = crearBoton(
                "Guardar Partida", 598, 49, 180, 31,
                new Color(46, 204, 113), new Color(27, 138, 74), 28, 13
        );
        this.btnCargarPartida = crearBoton(
                "Cargar Partida", 598, 84, 180, 31,
                new Color(128, 145, 158), new Color(82, 98, 110), 28, 13
        );
        this.btnCambiarUsuario = crearBoton(
                "Cambiar Usuario", 598, 119, 180, 31,
                new Color(165, 92, 198), new Color(110, 48, 142), 28, 13
        );

        this.panelUI.add(this.btnCambiarMundo);
        this.panelUI.add(this.btnGuardarPartida);
        this.panelUI.add(this.btnCargarPartida);
        this.panelUI.add(this.btnCambiarUsuario);

        Color azulReySup = new Color(0, 71, 194);
        Color azulReyInf = new Color(0, 43, 127);

        this.btnExplorar = crearBoton("Explorar", 22, 496, 140, 46, azulReySup, azulReyInf, 32, 14);
        this.btnTienda = crearBoton("Tienda", 176, 496, 140, 46, azulReySup, azulReyInf, 32, 14);
        this.btnInventario = crearBoton("Inventario", 330, 496, 140, 46, azulReySup, azulReyInf, 32, 14);
        this.btnMinijuegos = crearBoton("Minijuegos", 484, 496, 140, 46, azulReySup, azulReyInf, 32, 14);
        this.btnDormir = crearBoton(
                "Dormir", 638, 496, 140, 46,
                new Color(148, 72, 182), new Color(91, 44, 111), 32, 14
        );

        this.panelUI.add(this.btnExplorar);
        this.panelUI.add(this.btnTienda);
        this.panelUI.add(this.btnInventario);
        this.panelUI.add(this.btnMinijuegos);
        this.panelUI.add(this.btnDormir);
    }

    private JProgressBar crearBarraProgreso(
            String textoInicial,
            int valorInicial,
            Color colorInicio,
            Color colorFin,
            int x,
            int y) {
        BarraEstadoCapsula barra = new BarraEstadoCapsula(textoInicial, valorInicial, colorInicio, colorFin);
        barra.setBounds(x, y, 200, 26);
        return barra;
    }

    private JLabel crearEtiquetaInfo(String texto, int x, int y, int ancho, int alto, Color colorValor) {
        EtiquetaCristal etiqueta = new EtiquetaCristal(texto, colorValor);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }

    private JButton crearBoton(
            String texto,
            int x,
            int y,
            int ancho,
            int alto,
            Color colorArriba,
            Color colorAbajo,
            int radio,
            int tamFuente) {
        BotonPildora boton = new BotonPildora(texto, colorArriba, colorAbajo, radio, tamFuente);
        boton.setBounds(x, y, ancho, alto);
        return boton;
    }

    public void setControlesHabilitados(boolean habilitados) {
        this.btnExplorar.setEnabled(habilitados);
        this.btnTienda.setEnabled(habilitados);
        this.btnInventario.setEnabled(habilitados);
        this.btnMinijuegos.setEnabled(habilitados);
        this.btnDormir.setEnabled(habilitados);
        this.btnCambiarMundo.setEnabled(habilitados);
        this.btnGuardarPartida.setEnabled(habilitados);
        this.btnCargarPartida.setEnabled(habilitados);
        this.btnCambiarUsuario.setEnabled(habilitados);
        this.btnDormir.setText(habilitados ? "Dormir" : "Durmiendo...");
        this.lblCabito.setCursor(Cursor.getPredefinedCursor(
                habilitados ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR
        ));
        this.panelUI.repaint();
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

    public void actualizarUsuario(String nombreUsuario) {
        String mostrar = (nombreUsuario != null && !nombreUsuario.isBlank()) ? nombreUsuario : "jugador";
        this.lblUsuario.setText("Jugador: " + mostrar);
        this.panelUI.repaint();
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
        this.panelUI.repaint();
    }

    private static class PanelCapaUI extends JPanel {

        public PanelCapaUI() {
            super(null);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int x = 236;
            int y = 12;
            int w = 348;
            int h = 126;

            g2.setColor(new Color(0, 8, 22, 65));
            g2.fillRoundRect(x + 2, y + 3, w, h, 24, 24);

            RoundRectangle2D.Float cristalCentral = new RoundRectangle2D.Float(x, y, w, h, 24, 24);
            GradientPaint gpCristal = new GradientPaint(
                    0, y, new Color(12, 26, 52, 175),
                    0, y + h, new Color(8, 18, 38, 160)
            );
            g2.setPaint(gpCristal);
            g2.fill(cristalCentral);

            g2.setColor(new Color(255, 255, 255, 105));
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(cristalCentral);

            g2.setColor(new Color(180, 215, 255, 55));
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawLine(x + 16, y + 32, x + w - 16, y + 32);
            g2.drawLine(x + 16, y + 63, x + w - 16, y + 63);
            g2.drawLine(x + 16, y + 93, x + w - 16, y + 93);
            g2.drawLine(x + w / 2, y + 36, x + w / 2, y + h - 8);

            g2.dispose();
        }
    }

    private static class EtiquetaCristal extends JLabel {

        private final Color colorDestacado;

        public EtiquetaCristal(String texto, Color colorDestacado) {
            super(texto, SwingConstants.CENTER);
            this.colorDestacado = (colorDestacado != null) ? colorDestacado : Color.WHITE;
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 13));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            String textoCompleto = getText() != null ? getText() : "";
            int w = getWidth();
            int h = getHeight();

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();

            String prefijo = textoCompleto;
            String valor = "";
            int idx = textoCompleto.indexOf(':');
            if (idx >= 0 && idx + 1 < textoCompleto.length()) {
                prefijo = textoCompleto.substring(0, idx + 2);
                valor = textoCompleto.substring(idx + 2);
            }

            if (fm.stringWidth(textoCompleto) > w - 8 && valor.length() > 4) {
                while (valor.length() > 3 && fm.stringWidth(prefijo + valor + "...") > w - 8) {
                    valor = valor.substring(0, valor.length() - 1);
                }
                valor = valor + "...";
            }

            int anchoTotal = fm.stringWidth(prefijo) + fm.stringWidth(valor);
            int tx = Math.max(4, (w - anchoTotal) / 2);
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(new Color(0, 8, 22, 195));
            g2.drawString(prefijo + valor, tx + 1, ty + 1);

            g2.setColor(Color.WHITE);
            g2.drawString(prefijo, tx, ty);

            g2.setColor(this.colorDestacado);
            g2.drawString(valor, tx + fm.stringWidth(prefijo), ty);

            g2.dispose();
        }
    }

    private static class BarraEstadoCapsula extends JProgressBar {

        private final Color colorInicio;
        private final Color colorFin;

        public BarraEstadoCapsula(String textoInicial, int valorInicial, Color colorInicio, Color colorFin) {
            super(0, 100);
            this.colorInicio = colorInicio;
            this.colorFin = colorFin;
            setValue(valorInicial);
            setStringPainted(true);
            setString(textoInicial);
            setForeground(colorInicio);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setOpaque(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int radio = h - 2;

            RoundRectangle2D.Float capsulaFondo = new RoundRectangle2D.Float(1, 1, w - 2, h - 2, radio, radio);
            g2.setColor(new Color(12, 22, 42, 175));
            g2.fill(capsulaFondo);

            int valor = Math.max(0, Math.min(100, getValue()));
            if (valor > 0) {
                int anchoMaxRelleno = w - 6;
                int anchoRelleno = Math.max(10, (int) Math.round((valor / 100.0) * anchoMaxRelleno));

                Shape clipPrevio = g2.getClip();
                g2.clip(new RoundRectangle2D.Float(3, 3, w - 6, h - 6, radio - 4, radio - 4));

                GradientPaint gpRelleno = new GradientPaint(
                        3, 3, this.colorInicio,
                        3 + anchoMaxRelleno, h - 3, this.colorFin
                );
                g2.setPaint(gpRelleno);
                g2.fillRoundRect(3, 3, anchoRelleno, h - 6, radio - 4, radio - 4);

                g2.setColor(new Color(255, 255, 255, 65));
                g2.fillRoundRect(3, 3, anchoRelleno, (h - 6) / 2, radio - 4, radio - 4);

                g2.setClip(clipPrevio);
            }

            g2.setColor(new Color(255, 255, 255, 110));
            g2.setStroke(new BasicStroke(1.3f));
            g2.draw(capsulaFondo);

            String texto = getString() != null ? getString() : "";
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(texto)) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(new Color(0, 8, 20, 215));
            g2.drawString(texto, tx + 1, ty + 1);

            g2.setColor(Color.WHITE);
            g2.drawString(texto, tx, ty);

            g2.dispose();
        }
    }

    private static class BotonPildora extends JButton {

        private final Color colorArriba;
        private final Color colorAbajo;
        private final int radio;
        private boolean hover;

        public BotonPildora(String texto, Color colorArriba, Color colorAbajo, int radio, int tamFuente) {
            super(texto);
            this.colorArriba = colorArriba;
            this.colorAbajo = colorAbajo;
            this.radio = radio;
            this.hover = false;

            setFont(new Font("SansSerif", Font.BOLD, tamFuente));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
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
        public void setEnabled(boolean b) {
            super.setEnabled(b);
            if (!b) {
                this.hover = false;
            }
            setCursor(Cursor.getPredefinedCursor(b ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            boolean activo = isEnabled();

            Composite compPrevio = g2.getComposite();
            if (!activo) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.48f));
            }

            g2.setColor(new Color(0, 8, 22, 70));
            g2.fillRoundRect(2, 3, w - 3, h - 3, this.radio, this.radio);

            RoundRectangle2D.Float capsula = new RoundRectangle2D.Float(1, 1, w - 3, h - 3, this.radio, this.radio);

            Color sup = (activo && this.hover) ? aclararColor(this.colorArriba, 28) : this.colorArriba;
            Color inf = (activo && this.hover) ? aclararColor(this.colorAbajo, 22) : this.colorAbajo;
            g2.setPaint(new GradientPaint(0, 1, sup, 0, h - 2, inf));
            g2.fill(capsula);

            if (activo && this.hover) {
                g2.setColor(new Color(255, 255, 255, 38));
                g2.fill(capsula);
            }

            g2.setColor((activo && this.hover) ? AMARILLO_DORADO : new Color(255, 255, 255, 135));
            g2.setStroke(new BasicStroke((activo && this.hover) ? 1.9f : 1.4f));
            g2.draw(capsula);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String texto = getText() != null ? getText() : "";
            int tx = (w - fm.stringWidth(texto)) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(new Color(0, 8, 22, 175));
            g2.drawString(texto, tx + 1, ty + 1);

            g2.setColor(Color.WHITE);
            g2.drawString(texto, tx, ty);

            g2.setComposite(compPrevio);
            g2.dispose();
        }

        private static Color aclararColor(Color base, int delta) {
            return new Color(
                    Math.min(255, base.getRed() + delta),
                    Math.min(255, base.getGreen() + delta),
                    Math.min(255, base.getBlue() + delta)
            );
        }
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

    public JLabel getLblUsuario() {
        return lblUsuario;
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

    public JButton getBtnGuardarPartida() {
        return btnGuardarPartida;
    }

    public JButton getBtnCargarPartida() {
        return btnCargarPartida;
    }

    public JButton getBtnCambiarMundo() {
        return btnCambiarMundo;
    }

    public JButton getBtnCambiarUsuario() {
        return btnCambiarUsuario;
    }
}
