package modelo;

import java.util.Arrays;

public class Evento {

    public static final int RECOMPENSA_MONEDAS_DEFECTO = 5;
    public static final int RECOMPENSA_XP_DEFECTO = 25;
    public static final int BONO_INTELIGENCIA_DEFECTO = 8;
    public static final int PENALIZACION_MONEDAS_DEFECTO = 3;
    public static final int CONSOLACION_XP_DEFECTO = 5;

    private TipoEvento tipo;
    private String pregunta;
    private String[] opciones;
    private int indiceRespuestaCorrecta;
    private int recompensaMonedas;
    private int recompensaXp;
    private int bonoInteligencia;
    private int penalizacionMonedas;
    private int consolacionXp;

    public Evento(TipoEvento tipo, String pregunta, String[] opciones, int indiceRespuestaCorrecta) {
        this(
                tipo,
                pregunta,
                opciones,
                indiceRespuestaCorrecta,
                RECOMPENSA_MONEDAS_DEFECTO,
                RECOMPENSA_XP_DEFECTO,
                BONO_INTELIGENCIA_DEFECTO,
                PENALIZACION_MONEDAS_DEFECTO,
                CONSOLACION_XP_DEFECTO
        );
    }

    public Evento(
            TipoEvento tipo,
            String pregunta,
            String[] opciones,
            int indiceRespuestaCorrecta,
            int recompensaMonedas,
            int recompensaXp,
            int bonoInteligencia,
            int penalizacionMonedas,
            int consolacionXp) {
        if (opciones == null || opciones.length != 3) {
            throw new IllegalArgumentException("El evento debe tener exactamente 3 opciones.");
        }
        if (indiceRespuestaCorrecta < 0 || indiceRespuestaCorrecta > 2) {
            throw new IllegalArgumentException("El índice de respuesta correcta debe ser 0, 1 o 2.");
        }
        this.tipo = (tipo != null) ? tipo : TipoEvento.HALLAZGO;
        this.pregunta = (pregunta != null) ? pregunta : "";
        this.opciones = Arrays.copyOf(opciones, 3);
        this.indiceRespuestaCorrecta = indiceRespuestaCorrecta;
        this.recompensaMonedas = Math.max(0, recompensaMonedas);
        this.recompensaXp = Math.max(0, recompensaXp);
        this.bonoInteligencia = Math.max(0, bonoInteligencia);
        this.penalizacionMonedas = Math.max(0, penalizacionMonedas);
        this.consolacionXp = Math.max(0, consolacionXp);
    }

    public boolean evaluarRespuesta(int indiceSeleccionado) {
        return this.indiceRespuestaCorrecta == indiceSeleccionado;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        if (tipo != null) {
            this.tipo = tipo;
        }
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        if (pregunta != null) {
            this.pregunta = pregunta;
        }
    }

    public String[] getOpciones() {
        return Arrays.copyOf(opciones, opciones.length);
    }

    public void setOpciones(String[] opciones) {
        if (opciones != null && opciones.length == 3) {
            this.opciones = Arrays.copyOf(opciones, 3);
        }
    }

    public int getIndiceRespuestaCorrecta() {
        return indiceRespuestaCorrecta;
    }

    public void setIndiceRespuestaCorrecta(int indiceRespuestaCorrecta) {
        if (indiceRespuestaCorrecta >= 0 && indiceRespuestaCorrecta <= 2) {
            this.indiceRespuestaCorrecta = indiceRespuestaCorrecta;
        }
    }

    public int getRecompensaMonedas() {
        return recompensaMonedas;
    }

    public void setRecompensaMonedas(int recompensaMonedas) {
        this.recompensaMonedas = Math.max(0, recompensaMonedas);
    }

    public int getRecompensaXp() {
        return recompensaXp;
    }

    public void setRecompensaXp(int recompensaXp) {
        this.recompensaXp = Math.max(0, recompensaXp);
    }

    public int getBonoInteligencia() {
        return bonoInteligencia;
    }

    public void setBonoInteligencia(int bonoInteligencia) {
        this.bonoInteligencia = Math.max(0, bonoInteligencia);
    }

    public int getPenalizacionMonedas() {
        return penalizacionMonedas;
    }

    public void setPenalizacionMonedas(int penalizacionMonedas) {
        this.penalizacionMonedas = Math.max(0, penalizacionMonedas);
    }

    public int getConsolacionXp() {
        return consolacionXp;
    }

    public void setConsolacionXp(int consolacionXp) {
        this.consolacionXp = Math.max(0, consolacionXp);
    }
}

