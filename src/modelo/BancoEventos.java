package modelo;

import java.util.LinkedList;
import java.util.Random;

public class BancoEventos {

    private static final LinkedList<Evento> HALLAZGOS = new LinkedList<>();
    private static final LinkedList<Evento> QUIZZES = new LinkedList<>();

    static {
        inicializarEventosPorDefecto();
    }

    private BancoEventos() {
    }

    private static void inicializarEventosPorDefecto() {
        HALLAZGOS.clear();
        QUIZZES.clear();

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿En qué año fue fundada la Universidad Jorge Tadeo Lozano?",
                new String[]{"1954", "1810", "1991"},
                0
        ));

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "El diseño arquitectónico abierto del campus en Bogotá busca integrarse con la ciudad. ¿En qué barrio está?",
                new String[]{"Chapinero", "Las Nieves", "Teusaquillo"},
                1
        ));

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿A qué expedición científica rinde homenaje el nombre de la universidad?",
                new String[]{"Ruta de Darwin", "Expedición Botánica", "Expedición Corográfica"},
                1
        ));

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿En qué ciudad costera tiene la UTADEO su sede de Ciencias del Mar?",
                new String[]{"Cartagena", "Barranquilla", "Santa Marta"},
                2
        ));

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Cómo se llama la emisora oficial de la universidad?",
                new String[]{"Óyeme UJTL", "Tadeo Radio", "Frecuencia T"},
                0
        ));

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué figura histórica colombiana fundó la universidad junto a Julio César García?",
                new String[]{"Simón Bolívar", "Francisco José de Caldas", "Javier Ocampo López"},
                2
        ));

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Cuál es la principal revista institucional de la universidad?",
                new String[]{"Revista Mutis", "Revista Utadeo", "Gaceta Tadeísta"},
                1
        ));

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué carrera fue pionera y emblemática en la historia de la UTADEO?",
                new String[]{"Ingeniería de Sistemas", "Biología Marina", "Medicina"},
                1
        ));

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿En qué localidad de Bogotá se ubica la sede principal?",
                new String[]{"Santa Fe", "Teusaquillo", "Chapinero"},
                0
        ));

        HALLAZGOS.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué tipo de institución de educación superior es la UTADEO?",
                new String[]{"Pública", "Privada", "Mixta"},
                1
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "En economía, ¿qué significan las siglas PIB?",
                new String[]{"Presupuesto Interno", "Producto Interno Bruto", "Porcentaje Base"},
                1
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cuál de las siguientes estructuras en Java permite almacenar datos en filas y columnas?",
                new String[]{"Matriz", "Variable", "Scanner"},
                0
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué lenguaje compila su código a Bytecode para correr en una Máquina Virtual?",
                new String[]{"Python", "C++", "Java"},
                2
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué comando SQL se usa para extraer datos de una base de datos relacional?",
                new String[]{"SELECT", "EXTRACT", "PULL"},
                0
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué estructura de datos usa el principio LIFO (Último en entrar, primero en salir)?",
                new String[]{"Cola (Queue)", "Pila (Stack)", "Lista (List)"},
                1
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "En arquitectura de software, ¿qué significa la letra 'C' en MVC?",
                new String[]{"Clase", "Componente", "Controlador"},
                2
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cuál es el código de estado HTTP para 'No Encontrado'?",
                new String[]{"200", "404", "500"},
                1
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué sistema de control de versiones permite trabajar con ramas (branches)?",
                new String[]{"Git", "Docker", "Jira"},
                0
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué principio de POO oculta el estado interno de un objeto?",
                new String[]{"Herencia", "Polimorfismo", "Encapsulamiento"},
                2
        ));

        QUIZZES.add(new Evento(
                TipoEvento.QUIZ,
                "¿En qué base numérica opera el lenguaje máquina de las computadoras?",
                new String[]{"Decimal", "Hexadecimal", "Binario"},
                2
        ));
    }

    public static Evento obtenerEventoAleatorio(Random random) {
        Random generador = (random != null) ? random : new Random();
        boolean elegirHallazgo = generador.nextBoolean();

        LinkedList<Evento> listaSeleccionada = elegirHallazgo ? HALLAZGOS : QUIZZES;
        if (listaSeleccionada.isEmpty()) {
            listaSeleccionada = elegirHallazgo ? QUIZZES : HALLAZGOS;
        }

        int indice = generador.nextInt(listaSeleccionada.size());
        return listaSeleccionada.get(indice);
    }

    public static LinkedList<Evento> getHallazgos() {
        return new LinkedList<>(HALLAZGOS);
    }

    public static LinkedList<Evento> getQuizzes() {
        return new LinkedList<>(QUIZZES);
    }
}
