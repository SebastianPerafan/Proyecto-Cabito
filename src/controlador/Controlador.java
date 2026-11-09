package controlador;

import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
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
import vista.DialogoTadeista;
import vista.PanelAtrapaComida;
import vista.PanelInventario;
import vista.PanelJuegoPrincipal;
import vista.PanelMemoria;
import vista.PanelMenuMinijuegos;
import vista.PanelTienda;
import vista.VentanaPrincipal;

public class Controlador {

    private static final int INTERVALO_RELOJ_MILLIS = 8500;
    private static final int DURACION_ESTADO_TEMPORAL_MILLIS = 20000;
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
        this.partida = (partida != null) ? partida : new Partida("Cabito", "jugador");
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

    private boolean estaCabitoDurmiendo() {
        return (this.timerEstadoTemporal != null && this.timerEstadoTemporal.isRunning())
                || (this.partida != null
                && this.partida.getCabito() != null
                && this.partida.getCabito().getEstadoActual() == EstadoCabito.DORMIDO);
    }

    public void actualizarVistaPrincipal() {
        PanelJuegoPrincipal panelPrincipal = this.vista.getPanelPrincipal();
        Cabito cabito = this.partida.getCabito();

        panelPrincipal.actualizarUsuario(this.partida.getNombreUsuario());
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
        panelPrincipal.setControlesHabilitados(!estaCabitoDurmiendo());
        this.vista.getPanelMenuMinijuegos().actualizarEnergia(cabito.getEnergia(), cabito.getSalud());
        this.vista.getPanelInventario().actualizarEstadisticasJugador(
                this.partida.getNivel(),
                this.partida.getExperiencia(),
                this.partida.getXpSiguienteNivel(),
                cabito.getSalud(),
                cabito.getEnergia()
        );
    }

    private void inicializarNavegacion() {
        PanelJuegoPrincipal panelPrincipal = this.vista.getPanelPrincipal();
        PanelMenuMinijuegos panelMenuMinijuegos = this.vista.getPanelMenuMinijuegos();

        panelPrincipal.getBtnTienda().addActionListener(e -> {
            if (estaCabitoDurmiendo()) {
                return;
            }
            cargarVistaTienda();
            this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_TIENDA);
        });

        panelPrincipal.getBtnInventario().addActionListener(e -> {
            if (estaCabitoDurmiendo()) {
                return;
            }
            cargarVistaInventario();
            this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_INVENTARIO);
        });

        panelPrincipal.getBtnMinijuegos().addActionListener(e -> {
            if (estaCabitoDurmiendo()) {
                return;
            }
            abrirSelectorMinijuegos();
        });

        panelMenuMinijuegos.getBtnVolver().addActionListener(e -> {
            actualizarVistaPrincipal();
            this.vista.mostrarPanel(VentanaPrincipal.VISTA_PRINCIPAL);
        });

        panelMenuMinijuegos.getBtnJugarAtrapaComida().addActionListener(e -> iniciarPartidaAtrapaComida());
        panelMenuMinijuegos.getBtnJugarMemoria().addActionListener(e -> iniciarPartidaMemoria());

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
            if (estaCabitoDurmiendo()) {
                return;
            }
            String usuarioActivo = this.partida.getNombreUsuario();
            String nombreArchivo = GestorArchivos.obtenerNombreArchivo(usuarioActivo);
            boolean exito = GestorArchivos.guardarPartida(this.partida);
            if (exito) {
                DialogoTadeista.mostrarMensaje(
                        this.vista,
                        "Guardado Exitoso",
                        "Partida del perfil '" + usuarioActivo + "' guardada exitosamente en:\n" + nombreArchivo
                );
            } else {
                DialogoTadeista.mostrarMensaje(
                        this.vista,
                        "Error al Guardar",
                        "No fue posible guardar la partida del perfil '" + usuarioActivo + "'."
                );
            }
        });

        panelPrincipal.getBtnCargarPartida().addActionListener(e -> {
            if (estaCabitoDurmiendo()) {
                return;
            }
            String usuarioActivo = this.partida.getNombreUsuario();
            String nombreArchivo = GestorArchivos.obtenerNombreArchivo(usuarioActivo);
            Partida cargada = GestorArchivos.cargarPartida(usuarioActivo);
            if (cargada != null) {
                this.partida = cargada;
                actualizarVistaPrincipal();
                DialogoTadeista.mostrarMensaje(
                        this.vista,
                        "Carga Exitosa",
                        "Partida del perfil '" + usuarioActivo + "' recargada desde:\n" + nombreArchivo
                );
            } else {
                DialogoTadeista.mostrarMensaje(
                        this.vista,
                        "Archivo no encontrado",
                        "No se encontró un archivo guardado para el perfil '" + usuarioActivo + "' (" + nombreArchivo + ")."
                );
            }
        });

        panelPrincipal.getBtnCambiarUsuario().addActionListener(e -> {
            if (estaCabitoDurmiendo()) {
                return;
            }
            mostrarSelectorUsuario(true);
        });
        panelPrincipal.getBtnExplorar().addActionListener(e -> ejecutarExploracion());
        panelPrincipal.getBtnCambiarMundo().addActionListener(e -> mostrarSelectorMundo());
        panelPrincipal.getBtnDormir().addActionListener(e -> ejecutarDormir());

        MouseAdapter listenerAcariciar = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                acariciarCabito();
            }
        };
        panelPrincipal.getLblCabito().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelPrincipal.getLblCabito().addMouseListener(listenerAcariciar);
    }

    private void acariciarCabito() {
        if (estaCabitoDurmiendo()) {
            return;
        }
        Cabito cabito = this.partida.getCabito();
        cabito.modificarFelicidad(5);
        actualizarVistaPrincipal();
    }

    public void mostrarSelectorUsuario(boolean permitirCancelar) {
        if (permitirCancelar && estaCabitoDurmiendo()) {
            return;
        }
        boolean relojEstabaActivo = this.relojPrincipal != null && this.relojPrincipal.isRunning();
        if (relojEstabaActivo) {
            this.relojPrincipal.stop();
        }

        List<String> perfilesExistentes = GestorArchivos.listarUsuariosRegistrados();
        String usuarioActual = (this.partida != null) ? this.partida.getNombreUsuario() : "jugador";

        String seleccion = DialogoTadeista.mostrarSelectorUsuario(
                this.vista,
                perfilesExistentes,
                usuarioActual,
                permitirCancelar
        );

        if (seleccion != null && !seleccion.isBlank()) {
            String usuarioLimpio = GestorArchivos.normalizarNombreUsuario(seleccion);
            try {
                this.partida = new GestorArchivos(usuarioLimpio).cargarPartidaDesdeDisco(usuarioLimpio);
            } catch (IOException ex) {
                this.partida = new Partida(seleccion.trim(), usuarioLimpio);
                GestorArchivos.guardarPartida(this.partida);
            }
            if (this.partida.getCabito().getEstadoActual() == EstadoCabito.DORMIDO) {
                this.partida.getCabito().actualizarEstadoAutomatico();
            }
            actualizarVistaPrincipal();
        } else if (!permitirCancelar) {
            String respaldo = !perfilesExistentes.isEmpty() ? perfilesExistentes.get(0) : "jugador";
            Partida cargada = GestorArchivos.cargarPartida(respaldo);
            if (cargada != null) {
                this.partida = cargada;
            } else {
                this.partida = new Partida("Jugador", respaldo);
                GestorArchivos.guardarPartida(this.partida);
            }
            if (this.partida.getCabito().getEstadoActual() == EstadoCabito.DORMIDO) {
                this.partida.getCabito().actualizarEstadoAutomatico();
            }
            actualizarVistaPrincipal();
        }

        if (relojEstabaActivo) {
            this.relojPrincipal.start();
        }
    }

    private void ejecutarExploracion() {
        if (estaCabitoDurmiendo()) {
            return;
        }
        if (!this.partida.iniciarExploracion()) {
            DialogoTadeista.mostrarMensaje(
                    this.vista,
                    "No puede explorar",
                    "Cabito no puede explorar ahora.\nRequiere al menos 10 de Energía y no estar Enfermo (Salud > 25)."
            );
            return;
        }

        actualizarVistaPrincipal();
        Mundo mundoActual = this.partida.getMundoActual();
        Evento evento = BancoEventos.obtenerEventoAleatorio(mundoActual, this.random);
        String tipoEtiqueta = (evento.getTipo() == TipoEvento.HALLAZGO)
                ? "Hallazgo UTADEO"
                : "Quiz Cultural";
        String titulo = tipoEtiqueta + " - " + mundoActual.getNombre();

        String[] opciones = evento.getOpciones();
        int seleccion = DialogoTadeista.mostrarPreguntaExplorar(
                this.vista,
                titulo,
                evento.getPregunta(),
                opciones
        );

        if (seleccion < 0) {
            return;
        }

        int nivelAnterior = this.partida.getNivel();
        boolean acertado = this.partida.procesarResultadoEvento(evento, seleccion);
        actualizarVistaPrincipal();

        if (acertado) {
            StringBuilder mensaje = new StringBuilder("¡Respuesta correcta!\n\n");
            mensaje.append("+").append(evento.getRecompensaMonedas()).append(" Monedas\n");
            mensaje.append("+").append(evento.getRecompensaXp()).append(" XP\n");
            mensaje.append("+").append(evento.getBonoInteligencia()).append(" Inteligencia");
            if (this.partida.getNivel() > nivelAnterior) {
                mensaje.append("\n\n¡Felicidades! Cabito subió al Nivel ").append(this.partida.getNivel()).append(".");
            }
            DialogoTadeista.mostrarMensaje(this.vista, "¡Acierto Tadeísta!", mensaje.toString());
        } else {
            String respuestaCorrecta = opciones[evento.getIndiceRespuestaCorrecta()];
            String mensaje = "Respuesta incorrecta.\nLa opción correcta era: " + respuestaCorrecta
                    + "\n\n-" + evento.getPenalizacionMonedas() + " Monedas"
                    + "\n+" + evento.getConsolacionXp() + " XP de consolación.";
            DialogoTadeista.mostrarMensaje(this.vista, "Intento Fallido", mensaje);
        }
    }

    private void mostrarSelectorMundo() {
        if (estaCabitoDurmiendo()) {
            return;
        }
        List<Mundo> disponibles = new ArrayList<>();
        for (Mundo mundo : Mundo.values()) {
            if (this.partida.estaMundoDesbloqueado(mundo)) {
                disponibles.add(mundo);
            }
        }

        String[] opcionesMundos = new String[disponibles.size() + 1];
        for (int i = 0; i < disponibles.size(); i++) {
            opcionesMundos[i] = disponibles.get(i).getNombre();
        }
        opcionesMundos[disponibles.size()] = "Cancelar";

        int seleccion = DialogoTadeista.mostrarOpciones(
                this.vista,
                "Viajar por el Campus",
                "Selecciona el escenario de la UTADEO al que deseas ir:\n(La Biblioteca se desbloquea en Nivel 2 y El Auditorio en Nivel 4)",
                opcionesMundos
        );

        if (seleccion >= 0 && seleccion < disponibles.size()) {
            this.partida.setMundoActual(disponibles.get(seleccion));
            actualizarVistaPrincipal();
        }
    }

    private void ejecutarDormir() {
        if (estaCabitoDurmiendo()) {
            return;
        }
        Cabito cabito = this.partida.getCabito();
        cabito.setEnergia(cabito.getEnergia() + 35);
        cabito.setSalud(cabito.getSalud() + 5);
        activarEstadoTemporal(EstadoCabito.DORMIDO);
    }

    private void activarEstadoTemporal(EstadoCabito estadoTemporal) {
        if (this.timerEstadoTemporal != null && this.timerEstadoTemporal.isRunning()) {
            this.timerEstadoTemporal.stop();
        }
        this.partida.getCabito().setEstadoActual(estadoTemporal);
        this.vista.getPanelPrincipal().setControlesHabilitados(false);
        actualizarVistaPrincipal();

        this.timerEstadoTemporal = new Timer(DURACION_ESTADO_TEMPORAL_MILLIS, e -> {
            this.partida.getCabito().actualizarEstadoAutomatico();
            this.vista.getPanelPrincipal().setControlesHabilitados(true);
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
                    int totalEnMochila = this.partida.contarConsumiblePorId(itemCatalogo.getId());
                    DialogoTadeista.mostrarMensaje(
                            this.vista,
                            "Compra Exitosa",
                            "Compraste: " + itemCatalogo.getNombre()
                            + "\n(Tienes x" + totalEnMochila + " en tu mochila)."
                    );
                } else {
                    DialogoTadeista.mostrarMensaje(
                            this.vista,
                            "Monedas Insuficientes",
                            "No tienes suficientes monedas para comprar " + itemCatalogo.getNombre() + "."
                    );
                }
            });
        }
    }

    private void cargarVistaInventario() {
        PanelInventario panelInventario = this.vista.getPanelInventario();
        panelInventario.limpiarInventario();
        Cabito cabito = this.partida.getCabito();
        panelInventario.actualizarEstadisticasJugador(
                this.partida.getNivel(),
                this.partida.getExperiencia(),
                this.partida.getXpSiguienteNivel(),
                cabito.getSalud(),
                cabito.getEnergia()
        );

        LinkedList<Item> consumiblesUnicos = this.partida.obtenerConsumiblesUnicos();
        if (consumiblesUnicos.isEmpty()) {
            panelInventario.mostrarMensajeVacioConsumibles("Tu mochila de consumibles está vacía. ¡Visita la Tienda!");
        } else {
            for (Item item : consumiblesUnicos) {
                int cantidad = this.partida.contarConsumiblePorId(item.getId());
                JButton btnUsar = panelInventario.agregarItemConsumible(
                        item.getNombre(),
                        item.getDescripcion(),
                        cantidad,
                        item.getNombreImagen()
                );

                btnUsar.addActionListener(e -> {
                    if (this.partida.usarConsumiblePorId(item.getId())) {
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
        Cabito cabito = this.partida.getCabito();
        if (!cabito.puedeJugarMinijuego()) {
            DialogoTadeista.mostrarMensaje(
                    this.vista,
                    "No puede jugar",
                    "Cabito necesita al menos 15 de Energía y Salud mayor a 25 para jugar minijuegos."
            );
            return;
        }
        this.vista.getPanelMenuMinijuegos().actualizarEnergia(cabito.getEnergia(), cabito.getSalud());
        this.vista.mostrarPanel(VentanaPrincipal.VISTA_MENU_MINIJUEGOS);
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
            DialogoTadeista.mostrarMensaje(
                    this.vista,
                    "No puede jugar",
                    "Cabito necesita al menos 15 de Energía y Salud mayor a 25 para jugar minijuegos."
            );
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
            DialogoTadeista.mostrarMensaje(
                    this.vista,
                    "No puede jugar",
                    "Cabito necesita al menos 15 de Energía y Salud mayor a 25 para jugar minijuegos."
            );
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
        this.partida.getCabito().modificarFelicidad(25);
        restaurarEstadoCabitoTrasJuego();
        actualizarVistaPrincipal();

        StringBuilder resumen = new StringBuilder();
        resumen.append(resultado.esVictoria() ? "¡Victoria en " : "Fin de partida en ").append(nombreJuego).append("!\n\n");
        resumen.append("Puntaje obtenido: ").append(resultado.puntaje()).append("\n");
        resumen.append("Monedas ganadas: +").append(resultado.recompensaMonedas()).append("\n");
        resumen.append("Experiencia ganada: +").append(resultado.recompensaXp()).append(" XP\n");
        resumen.append("Felicidad recuperada: +25 Felicidad");
        if (resultado.otorgaManzana()) {
            resumen.append("\nPremio extra: +1 Manzana gratis en tu mochila");
        }
        if (this.partida.getNivel() > nivelAnterior) {
            resumen.append("\n\n¡Cabito subió al Nivel ").append(this.partida.getNivel()).append("!");
        }

        DialogoTadeista.mostrarMensaje(
                this.vista,
                "Resultado del Minijuego",
                resumen.toString()
        );

        this.vista.mostrarPanel(VentanaPrincipal.PANTALLA_PRINCIPAL);
    }

    private void restaurarEstadoCabitoTrasJuego() {
        this.partida.getCabito().actualizarEstadoAutomatico();
    }

    public void iniciar() {
        actualizarVistaPrincipal();
        this.vista.setVisible(true);
        mostrarSelectorUsuario(false);
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
