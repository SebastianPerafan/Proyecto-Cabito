package modelo;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class BancoEventos {

    private static final Map<Mundo, LinkedList<Evento>> HALLAZGOS_POR_MUNDO = new EnumMap<>(Mundo.class);
    private static final Map<Mundo, LinkedList<Evento>> QUIZZES_POR_MUNDO = new EnumMap<>(Mundo.class);

    static {
        inicializarEventosPorDefecto();
    }

    private BancoEventos() {
    }

    private static void inicializarEventosPorDefecto() {
        for (Mundo mundo : Mundo.values()) {
            HALLAZGOS_POR_MUNDO.put(mundo, new LinkedList<>());
            QUIZZES_POR_MUNDO.put(mundo, new LinkedList<>());
        }

        cargarEventosPlazoleta();
        cargarEventosBiblioteca();
        cargarEventosAuditorio();
    }

    private static void cargarEventosPlazoleta() {
        LinkedList<Evento> hallazgos = HALLAZGOS_POR_MUNDO.get(Mundo.PLAZOLETA);
        LinkedList<Evento> quizzes = QUIZZES_POR_MUNDO.get(Mundo.PLAZOLETA);

        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿En qué año fue fundada la Universidad Jorge Tadeo Lozano?",
                new String[]{"1954", "1810", "1991"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "El diseño arquitectónico abierto del campus en Bogotá busca integrarse con la ciudad. ¿En qué barrio está?",
                new String[]{"Chapinero", "Las Nieves", "Teusaquillo"},
                1
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿A qué expedición científica del siglo XVIII rinde homenaje el espíritu fundacional de la UTADEO?",
                new String[]{"Ruta de Darwin", "Expedición Botánica", "Expedición Corográfica"},
                1
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Quién fue Jorge Tadeo Lozano en la historia de Colombia?",
                new String[]{"Científico, naturalista y prócer de la Independencia", "Un virrey español del siglo XVII", "Un poeta romántico del siglo XX"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿En qué localidad del centro de Bogotá se encuentra ubicada la sede principal de la UTADEO?",
                new String[]{"Santa Fe", "Usaquén", "Suba"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué animal es Cabito, la mascota querida por la comunidad estudiantil tadeísta?",
                new String[]{"Un perro", "Un cóndor", "Un gato"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué colores institucionales identifican tradicionalmente la imagen de la UTADEO?",
                new String[]{"Azul rey y blanco", "Rojo y negro", "Verde y dorado"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Cuál es el propósito principal de la Plazoleta central dentro de la vida universitaria tadeísta?",
                new String[]{"Punto de encuentro social, cultural y estudiantil", "Parqueadero exclusivo de buses", "Zona cerrada de laboratorios químicos"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Con qué cerros emblemáticos de Bogotá tiene vista directa el campus central de la UTADEO?",
                new String[]{"Monserrate y Guadalupe", "Cerro de la Popa", "Cerro Nutibara"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿En qué ciudad del Caribe colombiano cuenta la UTADEO con su reconocida sede de Ciencias del Mar?",
                new String[]{"Cartagena", "Barranquilla", "Santa Marta"},
                2
        ));

        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cuál es el río más caudaloso y largo de América del Sur?",
                new String[]{"Río Amazonas", "Río Orinoco", "Río Magdalena"},
                0
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cuál es el planeta más grande del sistema solar?",
                new String[]{"Saturno", "Júpiter", "Neptuno"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cuál es la capital de Australia?",
                new String[]{"Sídney", "Melbourne", "Canberra"},
                2
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué gas de la atmósfera es indispensable para la respiración humana?",
                new String[]{"Oxígeno", "Helio", "Argón"},
                0
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿En qué continente se encuentra ubicado Egipto principalmente?",
                new String[]{"Asia", "África", "Europa"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿En qué año llegó el ser humano a la Luna por primera vez en la misión Apolo 11?",
                new String[]{"1959", "1969", "1979"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cuál es el océano más extenso de la Tierra?",
                new String[]{"Océano Atlántico", "Océano Índico", "Océano Pacífico"},
                2
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cuántos huesos tiene aproximadamente el cuerpo humano adulto?",
                new String[]{"206", "105", "350"},
                0
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué cordillera atraviesa a Colombia dividiéndose en tres ramales?",
                new String[]{"Los Alpes", "Los Andes", "Los Pirineos"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cuál es el metal líquido a temperatura ambiente conocido antiguamente como azogue?",
                new String[]{"Aluminio", "Mercurio", "Cobre"},
                1
        ));
    }

    private static void cargarEventosBiblioteca() {
        LinkedList<Evento> hallazgos = HALLAZGOS_POR_MUNDO.get(Mundo.BIBLIOTECA);
        LinkedList<Evento> quizzes = QUIZZES_POR_MUNDO.get(Mundo.BIBLIOTECA);

        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Cuál es el dial en Bogotá de la emisora cultural HJUT de la Universidad Jorge Tadeo Lozano?",
                new String[]{"106.9 FM", "88.9 FM", "95.5 FM"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué revista científica institucional de la UTADEO honra al director de la Expedición Botánica?",
                new String[]{"Revista Mutis", "Revista Caldas", "Revista Humboldt"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué servicio ofrece el Sistema de Bibliotecas de la UTADEO a sus estudiantes?",
                new String[]{"Préstamo bibliográfico, bases de datos científicas y salas de estudio", "Venta de tiquetes aéreos", "Trámites de pasaportes"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Cómo se denomina el sello encargado de publicar libros académicos e investigativos en la universidad?",
                new String[]{"Editorial UTADEO", "Imprenta Nacional", "Ediciones Sabana"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué carrera científica fue pionera y emblemática desde las primeras décadas de la UTADEO?",
                new String[]{"Ciencias del Mar / Biología Marina", "Astronomía Espacial", "Ingeniería Nuclear"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Cómo se llaman las agrupaciones donde los estudiantes tadeístas se inician en la investigación?",
                new String[]{"Semilleros de Investigación", "Clubes de Lectura", "Comités Deportivos"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué reconocimiento institucional destacó la calidad educativa integral de la UTADEO?",
                new String[]{"Acreditación Institucional de Alta Calidad", "Licencia de Comercio Exterior", "Registro Mercantil Internacional"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué repositorio digital conserva las tesis y trabajos de grado producidos en la UTADEO?",
                new String[]{"Expeditio (Repositorio Institucional)", "Archivo General de Indias", "Biblioteca de Alejandría"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué programa de radio estudiantil y juvenil ha hecho parte de la tradición radial tadeísta?",
                new String[]{"Óyeme UJTL", "Onda Nocturna", "Frecuencia Cero"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué estímulo otorga la universidad a los estudiantes con los promedios académicos más sobresalientes?",
                new String[]{"Becas y reconocimientos a la excelencia académica", "Exoneración de asistir a exámenes", "Un viaje espacial"},
                0
        ));

        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Quién escribió la novela 'Cien años de soledad', Premio Nobel de Literatura en 1982?",
                new String[]{"Álvaro Mutis", "Gabriel García Márquez", "José Eustasio Rivera"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cuál es el autor de la obra clásica del Siglo de Oro español 'Don Quijote de la Mancha'?",
                new String[]{"Miguel de Cervantes", "Lope de Vega", "Francisco de Quevedo"},
                0
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué escritor colombiano es el autor de la célebre novela selvática 'La Vorágine'?",
                new String[]{"Jorge Isaacs", "José Eustasio Rivera", "Tomás Carrasquilla"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Quién escribió el libro de cuentos fantásticos 'Ficciones' y 'El Aleph'?",
                new String[]{"Julio Cortázar", "Jorge Luis Borges", "Pablo Neruda"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cómo se llama la novela romántica del siglo XIX escrita por el vallecaucano Jorge Isaacs?",
                new String[]{"María", "Aura", "Manuela"},
                0
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Quién es el autor de las epopeyas griegas antiguas 'La Ilíada' y 'La Odisea'?",
                new String[]{"Sófocles", "Homero", "Eurípides"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué dramaturgo inglés escribió tragedias inmortales como 'Hamlet' y 'Romeo y Julieta'?",
                new String[]{"Charles Dickens", "William Shakespeare", "Oscar Wilde"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cómo se llama el pueblo ficticio donde transcurre 'Cien años de soledad'?",
                new String[]{"Comala", "Macondo", "Santa María"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué poeta y novelista colombiano creó el famoso personaje de 'Maqroll el Gaviero'?",
                new String[]{"Álvaro Mutis", "Rafael Pombo", "José Asunción Silva"},
                0
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿A qué género literario pertenecen las obras escritas en verso como los sonetos y las odas?",
                new String[]{"Lírica / Poesía", "Ensayo periodístico", "Crónica judicial"},
                0
        ));
    }

    private static void cargarEventosAuditorio() {
        LinkedList<Evento> hallazgos = HALLAZGOS_POR_MUNDO.get(Mundo.AUDITORIO);
        LinkedList<Evento> quizzes = QUIZZES_POR_MUNDO.get(Mundo.AUDITORIO);

        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Cómo se llama el emblemático auditorio principal de conciertos y eventos culturales de la UTADEO?",
                new String[]{"Auditorio Fabio Lozano", "Auditorio León de Greiff", "Teatro Colón"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué importante agrupación sinfónica de Bogotá realiza presentaciones frecuentes en el Auditorio Fabio Lozano?",
                new String[]{"Orquesta Filarmónica de Bogotá", "Banda Sinfónica de Viena", "Filarmónica de Berlín"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Cómo se llama el museo universitario de la UTADEO dedicado a la biodiversidad marina y costera?",
                new String[]{"Museo del Mar", "Museo del Oro", "Museo Nacional"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué facultad de la UTADEO es referente nacional en programas como Diseño Gráfico, Industrial y Artes Plásticas?",
                new String[]{"Facultad de Artes y Diseño", "Facultad de Minas", "Facultad de Agronomía"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué edificio moderno del campus de Bogotá fue diseñado por el arquitecto Daniel Bermúdez para las artes?",
                new String[]{"Módulo 20 / Edificio de Artes y Diseño", "Torre Colpatria", "Edificio Avianca"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué tipo de actividades organiza el Centro Arte y Cultura de la UTADEO para la comunidad?",
                new String[]{"Talleres artísticos, coros, danzas, teatro y exposiciones", "Competencias de automovilismo", "Subastas bursátiles"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué muestras protagonizan cada semestre los estudiantes de últimos niveles de Artes y Diseño?",
                new String[]{"Salones y exposiciones de proyectos de grado y creación", "Ferias ganaderas", "Torneos de ajedrez"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Con qué entrada suele contar la comunidad tadeísta para asistir a eventos culturales en el Auditorio Fabio Lozano?",
                new String[]{"Acceso preferencial o entrada libre con carné tadeísta", "Solo con invitación diplomática", "Únicamente para extranjeros"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué género musical predomina históricamente en la programación cultural de la emisora HJUT y el Auditorio?",
                new String[]{"Música clásica, jazz y músicas del mundo", "Reguetón comercial", "Electrónica industrial"},
                0
        ));
        hallazgos.add(new Evento(
                TipoEvento.HALLAZGO,
                "¿Qué ceremonia solemne viven los estudiantes en el Auditorio al culminar exitosamente su carrera?",
                new String[]{"La Ceremonia de Grado Tadeísta", "El Bautizo Primíparo", "La Semana de Inducción"},
                0
        ));

        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué pintor y escultor colombiano es mundialmente famoso por el volumen monumental de sus figuras?",
                new String[]{"Alejandro Obregón", "Fernando Botero", "Omar Rayo"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Quién pintó la famosa obra renacentista 'La Gioconda' (Mona Lisa)?",
                new String[]{"Miguel Ángel", "Leonardo da Vinci", "Rafael Sanzio"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué artista español fue cofundador del movimiento cubista y pintó el 'Guernica'?",
                new String[]{"Salvador Dalí", "Pablo Picasso", "Francisco de Goya"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué compositor alemán escribió la célebre 'Novena Sinfonía' (Himno a la Alegría) aun habiendo perdido el oído?",
                new String[]{"Wolfgang Amadeus Mozart", "Ludwig van Beethoven", "Johann Sebastian Bach"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué pintor neerlandés postimpresionista es el autor de 'La noche estrellada' y 'Los girasoles'?",
                new String[]{"Rembrandt", "Vincent van Gogh", "Claude Monet"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Cómo se conoce popularmente al arte de la cinematografía dentro de las bellas artes?",
                new String[]{"El Séptimo Arte", "El Primer Arte", "El Décimo Arte"},
                0
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué escuela alemana fundada en 1919 revolucionó la arquitectura y el diseño industrial y gráfico moderno?",
                new String[]{"Bauhaus", "Rococó", "Gótico"},
                0
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué artista colombiana es reconocida internacionalmente por sus esculturas conceptuales y de memoria?",
                new String[]{"Doris Salcedo", "Frida Kahlo", "Remedios Varo"},
                0
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿A qué familia de instrumentos musicales pertenecen el violín, la viola y el violonchelo?",
                new String[]{"Viento madera", "Cuerda frotada", "Percusión metálica"},
                1
        ));
        quizzes.add(new Evento(
                TipoEvento.QUIZ,
                "¿Qué movimiento artístico francés del siglo XIX buscaba capturar la luz y el instante con pinceladas sueltas?",
                new String[]{"Impresionismo", "Minimalismo", "Pop Art"},
                0
        ));
    }

    public static Evento obtenerEventoAleatorio(Mundo mundo, Random random) {
        Mundo mundoObjetivo = (mundo != null) ? mundo : Mundo.PLAZOLETA;
        Random generador = (random != null) ? random : new Random();
        boolean elegirHallazgo = generador.nextBoolean();

        LinkedList<Evento> hallazgos = HALLAZGOS_POR_MUNDO.get(mundoObjetivo);
        LinkedList<Evento> quizzes = QUIZZES_POR_MUNDO.get(mundoObjetivo);

        LinkedList<Evento> listaSeleccionada = elegirHallazgo ? hallazgos : quizzes;
        if (listaSeleccionada == null || listaSeleccionada.isEmpty()) {
            listaSeleccionada = elegirHallazgo ? quizzes : hallazgos;
        }

        int indice = generador.nextInt(listaSeleccionada.size());
        return listaSeleccionada.get(indice);
    }

    public static Evento obtenerEventoAleatorio(Random random) {
        return obtenerEventoAleatorio(Mundo.PLAZOLETA, random);
    }

    public static LinkedList<Evento> getHallazgosPorMundo(Mundo mundo) {
        Mundo objetivo = (mundo != null) ? mundo : Mundo.PLAZOLETA;
        return new LinkedList<>(HALLAZGOS_POR_MUNDO.get(objetivo));
    }

    public static LinkedList<Evento> getQuizzesPorMundo(Mundo mundo) {
        Mundo objetivo = (mundo != null) ? mundo : Mundo.PLAZOLETA;
        return new LinkedList<>(QUIZZES_POR_MUNDO.get(objetivo));
    }

    public static LinkedList<Evento> getHallazgos() {
        return getHallazgosPorMundo(Mundo.PLAZOLETA);
    }

    public static LinkedList<Evento> getQuizzes() {
        return getQuizzesPorMundo(Mundo.PLAZOLETA);
    }
}
