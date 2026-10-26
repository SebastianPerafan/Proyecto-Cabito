package controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import minijuegos.LogicaAtrapaComida;
import minijuegos.LogicaMemoria;
import minijuegos.ResultadoMinijuego;
import modelo.BancoEventos;
import modelo.Cabito;
import modelo.EstadoCabito;
import modelo.Evento;
import modelo.Item;
import modelo.Logro;
import modelo.Mundo;
import modelo.Partida;
import modelo.TipoEvento;
import persistencia.GestorArchivos;
import vista.PanelAtrapaComida;
import vista.PanelInventario;
import vista.PanelJuegoPrincipal;
import vista.PanelMemoria;
import vista.PanelTienda;
import vista.VentanaPrincipal;

public class Controlador {

    private static final int INTERVALO_RELOJ_MILLIS = 5000;
    private static final int DURACION_ESTADO_TEMPORAL_MILLIS = 4000;
    private static final int TICK_ATRAPA_COMIDA_MILLIS = 33;

    private Partida partida;
    private final VentanaPrincipal vista;
    private Timer relojPrincipal;
    private Timer timerEstadoTemporal;
    private Timer timerAtrapaComida;

    private final LogicaMemoria logicaMemoria;
    private int primeraFilaMemoria;
    private int primeraColumnaMemoria;

    private final LogicaAtrapaComida logicaAtrapaComida;
    private final Random random;

    public Controlador(Partida partida, VentanaPrincipal vista) {
        this.partida = (partida != null) ? partida : new Partida("Cabito");
        this.vista = vista;
        this.random = new Random();
        this.logicaMemoria = new LogicaMemoria(this.random);
        this.logicaAtrapaComida = new LogicaAtrapaComida(this.random);
        this.primeraFilaMemoria = -1;
        this.primeraColumnaMemoria = -1;

        inicializarReloj();
        inicializarNavegacion();
        inicializarAcciones();
        inicializarMinijuegos();
    }

    private void inicializarReloj() {
        this.relojPrincipal = new Timer(INTERVALO_RELOJ_MILLIS, e -> {
            this.partida.getCabito().aplicarDesgasteCiclo();
            actualizarVistaPrincipal();
        });
    }

    public void actualizarVistaPrincipal() {
        PanelJuegoPrincipal panelPrincipal = this.vista.getPanelPrincipal();
        Cabito cabito = this.partida.getCabito();

        panelPrincipal.actualizarEstadisticas(
                cabito.getHambre(),
                cabito.getEnergia(),
                cabito.getHigiene(),
                cabito.getFelicidad(),
                cabito.getSalud(),
                cabito.getInteligencia(),
                this.partida.getNivel(),
                this.partida.getExperiencia(),
                this.partida.getXpSiguienteNivel(),
                this.partida.getMonedas(),
                cabito.getEstadoActual().getNombreMostrar(),
                this.partida.getMundoActual().getNombre()
        );

        panelPrincipal.actualizarFondo(this.partida.getMundoActual().getNombreImagenFondo());
        panelPrincipal.actualizarCabito(cabito.getEstadoActual().getNombreImagen());

        String imagenAccesorioEquipado = null;
        int offsetX = 0;
        int offsetY = 0;
        double escala = 1.0;
        for (Item prenda : this.partida.getArmario()) {
            if (prenda.isDesbloqueado() && prenda.isEquipado()) {
                imagenAccesorioEquipado = prenda.getNombreImagen();
                offsetX = prenda.getOffsetX();
                offsetY = prenda.getOffsetY();
                escala = prenda.getEscala();
                break;
            }
        }
        panelPrincipal.actualizarAccesorio(imagenAccesorioEquipado, offsetX, offsetY, escala);
    }

    private void inicializarNavegacion() {
        PanelJuegoPrincipal panelPrincipal = this.vista.getPanelPrincipal();

        panelPrincipal.getBtnTienda().addActionListener(e -> {
            cargarVistaTienda();
            this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_TIENDA);
        });

        panelPrincipal.getBtnInventario().addActionListener(e -> {
            cargarVistaInventario();
            this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_INVENTARIO);
        });

        panelPrincipal.getBtnMinijuegos().addActionListener(e -> abrirSelectorMinijuegos());

        this.vista.getPanelTienda().getBtnVolver().addActionListener(e -> {
            actualizarVistaPrincipal();
            this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_PRINCIPAL);
        });

        this.vista.getPanelInventario().getBtnVolver().addActionListener(e -> {
            actualizarVistaPrincipal();
            this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_PRINCIPAL);
        });

        this.vista.getPanelMemoria().getBtnVolver().addActionListener(e -> {
            restaurarEstadoCabitoTrasJuego();
            actualizarVistaPrincipal();
            this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_PRINCIPAL);
        });

        this.vista.getPanelAtrapaComida().getBtnVolver().addActionListener(e -> {
            detenerTimerAtrapaComida();
            restaurarEstadoCabitoTrasJuego();
            actualizarVistaPrincipal();
            this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_PRINCIPAL);
        });
    }

    private void inicializarAcciones() {
        PanelJuegoPrincipal panelPrincipal = this.vista.getPanelPrincipal();

        panelPrincipal.getBtnGuardarPartida().addActionListener(e -> {
            boolean exito = GestorArchivos.guardarPartida(this.partida);
            if (exito) {
                JOptionPane.showMessageDialog(
                        this.vista,
                        "Partida guardada exitosamente en " + GestorArchivos.ARCHIVO_POR_DEFECTO,
                        "Guardado Exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this.vista,
                        "No fue posible guardar la partida.",
                        "Error al Guardar",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panelPrincipal.getBtnCargarPartida().addActionListener(e -> {
            Partida cargada = GestorArchivos.cargarPartida();
            if (cargada != null) {
                this.partida = cargada;
                actualizarVistaPrincipal();
                JOptionPane.showMessageDialog(
                        this.vista,
                        "Partida cargada correctamente.",
                        "Carga Exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this.vista,
                        "No se encontró un archivo de partida previo (" + GestorArchivos.ARCHIVO_POR_DEFECTO + ").",
                        "Archivo no encontrado",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        panelPrincipal.getBtnExplorar().addActionListener(e -> ejecutarExploracion());
        panelPrincipal.getBtnCambiarMundo().addActionListener(e -> mostrarSelectorMundo());
        panelPrincipal.getBtnDormir().addActionListener(e -> ejecutarDormir());
        panelPrincipal.getBtnLavarse().addActionListener(e -> ejecutarLavarse());
    }

    private void ejecutarExploracion() {
        if (!this.partida.iniciarExploracion()) {
            JOptionPane.showMessageDialog(
                    this.vista,
                    "Cabito no puede explorar ahora. Requiere al menos 10 de Energía y no estar Enfermo (Salud > 25).",
                    "No puede explorar",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        actualizarVistaPrincipal();
        Evento evento = BancoEventos.obtenerEventoAleatorio(this.random);
        String titulo = (evento.getTipo() == TipoEvento.HALLAZGO)
                ? "Hallazgo Histórico Tadeísta"
                : "Quiz Académico";

        String[] opciones = evento.getOpciones();
        int seleccion = JOptionPane.showOptionDialog(
                this.vista,
                evento.getPregunta(),
                titulo,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion < 0) {
            return;
        }

        int nivelAnterior = this.partida.getNivel();
        boolean acertado = this.partida.procesarResultadoEvento(evento, seleccion);
        actualizarVistaPrincipal();

        if (acertado) {
            StringBuilder mensaje = new StringBuilder("¡Respuesta correcta!\n");
            mensaje.append("+").append(evento.getRecompensaMonedas()).append(" Monedas\n");
            mensaje.append("+").append(evento.getRecompensaXp()).append(" XP\n");
            mensaje.append("+").append(evento.getBonoInteligencia()).append(" Inteligencia");
            if (this.partida.getNivel() > nivelAnterior) {
                mensaje.append("\n\n¡Felicidades! Cabito subió al Nivel ").append(this.partida.getNivel()).append(".");
            }
            JOptionPane.showMessageDialog(this.vista, mensaje.toString(), "¡Acierto!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String respuestaCorrecta = opciones[evento.getIndiceRespuestaCorrecta()];
            String mensaje = "Respuesta incorrecta. La opción correcta era: " + respuestaCorrecta
                    + "\n-" + evento.getPenalizacionMonedas() + " Monedas"
                    + "\n+" + evento.getConsolacionXp() + " XP de consolación.";
            JOptionPane.showMessageDialog(this.vista, mensaje, "Intento Fallido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarSelectorMundo() {
        List<Mundo> disponibles = new ArrayList<>();
        for (Mundo mundo : Mundo.values()) {
            if (this.partida.estaMundoDesbloqueado(mundo)) {
                disponibles.add(mundo);
            }
        }

        String[] nombresMundos = new String[disponibles.size()];
        for (int i = 0; i < disponibles.size(); i++) {
            nombresMundos[i] = disponibles.get(i).getNombre();
        }

        int seleccion = JOptionPane.showOptionDialog(
                this.vista,
                "Selecciona el escenario de la UTADEO al que deseas ir:\n(La Biblioteca se desbloquea en Nivel 2 y El Auditorio en Nivel 4)",
                "Viajar por el Campus",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                nombresMundos,
                this.partida.getMundoActual().getNombre()
        );

        if (seleccion >= 0 && seleccion < disponibles.size()) {
            this.partida.setMundoActual(disponibles.get(seleccion));
            actualizarVistaPrincipal();
        }
    }

    private void ejecutarDormir() {
        Cabito cabito = this.partida.getCabito();
        cabito.setEnergia(cabito.getEnergia() + 35);
        cabito.setSalud(cabito.getSalud() + 5);
        activarEstadoTemporal(EstadoCabito.DORMIDO);
    }

    private void ejecutarLavarse() {
        Cabito cabito = this.partida.getCabito();
        cabito.setHigiene(cabito.getHigiene() + 35);
        cabito.setFelicidad(cabito.getFelicidad() + 5);
        cabito.actualizarEstadoAutomatico();
        actualizarVistaPrincipal();
    }

    private void activarEstadoTemporal(EstadoCabito estadoTemporal) {
        if (this.timerEstadoTemporal != null && this.timerEstadoTemporal.isRunning()) {
            this.timerEstadoTemporal.stop();
        }
        this.partida.getCabito().setEstadoActual(estadoTemporal);
        actualizarVistaPrincipal();

        this.timerEstadoTemporal = new Timer(DURACION_ESTADO_TEMPORAL_MILLIS, e -> {
            this.partida.getCabito().actualizarEstadoAutomatico();
            actualizarVistaPrincipal();
        });
        this.timerEstadoTemporal.setRepeats(false);
        this.timerEstadoTemporal.start();
    }

    private void cargarVistaTienda() {
        PanelTienda panelTienda = this.vista.getPanelTienda();
        panelTienda.limpiarTienda();
        panelTienda.actualizarMonedas(this.partida.getMonedas());

        LinkedList<Item> catalogo = Item.obtenerCatalogoTienda();
        for (Item itemCatalogo : catalogo) {
            JButton btnComprar = panelTienda.agregarItemTienda(
                    itemCatalogo.getNombre(),
                    String.valueOf(itemCatalogo.getPrecio()),
                    itemCatalogo.getDescripcion(),
                    itemCatalogo.getNombreImagen()
            );

            btnComprar.addActionListener(e -> {
                if (this.partida.comprarConsumible(itemCatalogo)) {
                    panelTienda.actualizarMonedas(this.partida.getMonedas());
                    actualizarVistaPrincipal();
                    JOptionPane.showMessageDialog(
                            this.vista,
                            "Compraste: " + itemCatalogo.getNombre() + " (guardado en tu mochila).",
                            "Compra Exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this.vista,
                            "No tienes suficientes monedas para comprar " + itemCatalogo.getNombre() + ".",
                            "Monedas Insuficientes",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            });
        }
    }

    private void cargarVistaInventario() {
        PanelInventario panelInventario = this.vista.getPanelInventario();
        panelInventario.limpiarInventario();

        LinkedList<Item> consumibles = this.partida.getConsumibles();
        if (consumibles.isEmpty()) {
            panelInventario.mostrarMensajeVacioConsumibles("Tu mochila de consumibles está vacía. ¡Visita la Tienda!");
        } else {
            for (Item item : new ArrayList<>(consumibles)) {
                JButton btnUsar = panelInventario.agregarItemConsumible(
                        item.getNombre(),
                        item.getDescripcion(),
                        item.getNombreImagen()
                );

                btnUsar.addActionListener(e -> {
                    if (this.partida.usarConsumible(item)) {
                        cargarVistaInventario();
                        actualizarVistaPrincipal();
                    }
                });
            }
        }

        for (Item prenda : this.partida.getArmario()) {
            String descripcionRequisito = obtenerDescripcionLogroPorPrenda(prenda.getId(), prenda.getDescripcion());
            JButton btnEquipar = panelInventario.agregarItemArmario(
                    prenda.getNombre(),
                    descripcionRequisito,
                    prenda.isDesbloqueado(),
                    prenda.isEquipado(),
                    prenda.getNombreImagen()
            );

            btnEquipar.addActionListener(e -> {
                if (this.partida.alternarEquipadoAccesorio(prenda.getId())) {
                    if (prenda.isEquipado()) {
                        this.vista.getPanelPrincipal().actualizarAccesorio(
                                prenda.getNombreImagen(),
                                prenda.getOffsetX(),
                                prenda.getOffsetY(),
                                prenda.getEscala()
                        );
                    } else {
                        this.vista.getPanelPrincipal().actualizarAccesorio(null, 0, 0, 1.0);
                    }
                    cargarVistaInventario();
                    actualizarVistaPrincipal();
                }
            });
        }
    }

    private String obtenerDescripcionLogroPorPrenda(String idAccesorio, String respaldo) {
        for (Logro logro : this.partida.getLogros()) {
            if (logro.getIdAccesorioRecompensa().equalsIgnoreCase(idAccesorio)) {
                return logro.getNombre() + ": " + logro.getDescripcion();
            }
        }
        return respaldo;
    }

    private void abrirSelectorMinijuegos() {
        if (!this.partida.getCabito().puedeJugarMinijuego()) {
            JOptionPane.showMessageDialog(
                    this.vista,
                    "Cabito necesita al menos 15 de Energía y Salud mayor a 25 para jugar minijuegos.",
                    "No puede jugar",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String[] opciones = {"Atrapa la Comida (Teclado)", "Memoria Tadeísta (Matrices)", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(
                this.vista,
                "Elige el minijuego que deseas iniciar (Costo: -15 Energía):",
                "Minijuegos Tadeístas",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (eleccion == 0) {
            iniciarPartidaAtrapaComida();
        } else if (eleccion == 1) {
            iniciarPartidaMemoria();
        }
    }

    private void inicializarMinijuegos() {
        PanelMemoria panelMemoria = this.vista.getPanelMemoria();
        JButton[][] botones = panelMemoria.getBotonesCartas();

        for (int f = 0; f < PanelMemoria.FILAS; f++) {
            for (int c = 0; c < PanelMemoria.COLUMNAS; c++) {
                final int fila = f;
                final int col = c;
                botones[f][c].addActionListener(e -> manejarClicCartaMemoria(fila, col));
            }
        }

        this.logicaMemoria.setOnTerminado(resultado -> finalizarMinijuego(resultado, "Memoria Tadeísta"));

        PanelAtrapaComida panelAtrapa = this.vista.getPanelAtrapaComida();
        panelAtrapa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int codigo = e.getKeyCode();
                if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) {
                    logicaAtrapaComida.moverIzquierda();
                } else if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) {
                    logicaAtrapaComida.moverDerecha();
                }
            }
        });

        this.timerAtrapaComida = new Timer(TICK_ATRAPA_COMIDA_MILLIS, e -> {
            this.logicaAtrapaComida.actualizarTick(TICK_ATRAPA_COMIDA_MILLIS);
            sincronizarLienzoAtrapaComida();
        });

        this.logicaAtrapaComida.setOnTerminado(resultado -> {
            detenerTimerAtrapaComida();
            sincronizarLienzoAtrapaComida();
            finalizarMinijuego(resultado, "Atrapa la Comida");
        });
    }

    private void iniciarPartidaMemoria() {
        if (!this.partida.iniciarMinijuego()) {
            return;
        }
        this.partida.getCabito().setEstadoActual(EstadoCabito.JUGANDO);
        actualizarVistaPrincipal();

        this.primeraFilaMemoria = -1;
        this.primeraColumnaMemoria = -1;
        this.logicaMemoria.iniciar();

        PanelMemoria panelMemoria = this.vista.getPanelMemoria();
        panelMemoria.reiniciarTableroVisual();
        panelMemoria.actualizarEstadisticas(
                this.logicaMemoria.getIntentosRestantes(),
                this.logicaMemoria.getParejasEncontradas()
        );

        this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_MEMORIA);
    }

    private void manejarClicCartaMemoria(int fila, int col) {
        PanelMemoria panelMemoria = this.vista.getPanelMemoria();
        if (panelMemoria.isTableroBloqueado() || this.logicaMemoria.estaTerminado()) {
            return;
        }
        if (this.logicaMemoria.estaRevelada(fila, col)) {
            return;
        }

        if (this.primeraFilaMemoria == -1) {
            this.primeraFilaMemoria = fila;
            this.primeraColumnaMemoria = col;
            panelMemoria.actualizarBoton(fila, col, this.logicaMemoria.getValorCarta(fila, col), true);
            return;
        }

        if (this.primeraFilaMemoria == fila && this.primeraColumnaMemoria == col) {
            return;
        }

        final int f1 = this.primeraFilaMemoria;
        final int c1 = this.primeraColumnaMemoria;
        final int f2 = fila;
        final int c2 = col;

        this.primeraFilaMemoria = -1;
        this.primeraColumnaMemoria = -1;

        panelMemoria.actualizarBoton(f2, c2, this.logicaMemoria.getValorCarta(f2, c2), true);
        boolean acertada = this.logicaMemoria.procesarJugada(f1, c1, f2, c2);
        panelMemoria.actualizarEstadisticas(
                this.logicaMemoria.getIntentosRestantes(),
                this.logicaMemoria.getParejasEncontradas()
        );

        if (!acertada && !this.logicaMemoria.estaTerminado()) {
            panelMemoria.setTableroBloqueado(true);
            Timer timerOcultar = new Timer(900, ev -> {
                panelMemoria.actualizarBoton(f1, c1, "", false);
                panelMemoria.actualizarBoton(f2, c2, "", false);
                panelMemoria.setTableroBloqueado(false);
            });
            timerOcultar.setRepeats(false);
            timerOcultar.start();
        }
    }

    private void iniciarPartidaAtrapaComida() {
        if (!this.partida.iniciarMinijuego()) {
            return;
        }
        this.partida.getCabito().setEstadoActual(EstadoCabito.JUGANDO);
        actualizarVistaPrincipal();

        this.logicaAtrapaComida.iniciar();
        sincronizarLienzoAtrapaComida();
        this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_ATRAPA_COMIDA);
        this.vista.getPanelAtrapaComida().requestFocusInWindow();
        this.timerAtrapaComida.start();
    }

    private void sincronizarLienzoAtrapaComida() {
        List<String[]> datosComidas = new ArrayList<>();
        for (LogicaAtrapaComida.ObjetoComida comida : this.logicaAtrapaComida.getComidasActivas()) {
            datosComidas.add(new String[]{
                comida.getTipo(),
                String.valueOf(comida.getX()),
                String.valueOf(comida.getY())
            });
        }
        this.vista.getPanelAtrapaComida().actualizarLienzo(
                this.logicaAtrapaComida.getPosicionXCabito(),
                datosComidas,
                this.logicaAtrapaComida.getPuntaje(),
                this.logicaAtrapaComida.getTiempoRestanteMillis()
        );
    }

    private void detenerTimerAtrapaComida() {
        if (this.timerAtrapaComida != null && this.timerAtrapaComida.isRunning()) {
            this.timerAtrapaComida.stop();
        }
    }

    private void finalizarMinijuego(ResultadoMinijuego resultado, String nombreJuego) {
        int nivelAnterior = this.partida.getNivel();
        this.partida.aplicarResultadoMinijuego(resultado);
        restaurarEstadoCabitoTrasJuego();
        actualizarVistaPrincipal();

        StringBuilder resumen = new StringBuilder();
        resumen.append(resultado.esVictoria() ? "¡Victoria en " : "Fin de partida en ").append(nombreJuego).append("!\n\n");
        resumen.append("Puntaje obtenido: ").append(resultado.puntaje()).append("\n");
        resumen.append("Monedas ganadas: +").append(resultado.recompensaMonedas()).append("\n");
        resumen.append("Experiencia ganada: +").append(resultado.recompensaXp()).append(" XP");
        if (resultado.otorgaManzana()) {
            resumen.append("\nPremio extra: +1 Manzana gratis en tu mochila");
        }
        if (this.partida.getNivel() > nivelAnterior) {
            resumen.append("\n\n¡Cabito subió al Nivel ").append(this.partida.getNivel()).append("!");
        }

        JOptionPane.showMessageDialog(
                this.vista,
                resumen.toString(),
                "Resultado del Minijuego",
                resultado.esVictoria() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.PLAIN_MESSAGE
        );

        this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_PRINCIPAL);
    }

    private void restaurarEstadoCabitoTrasJuego() {
        this.partida.getCabito().actualizarEstadoAutomatico();
    }

    public void iniciar() {
        actualizarVistaPrincipal();
        this.vista.setVisible(true);
        this.relojPrincipal.start();
    }

    public Partida getPartida() {
        return partida;
    }

    public VentanaPrincipal getVista() {
        return vista;
    }

    public Timer getRelojPrincipal() {
        return relojPrincipal;
    }
}

