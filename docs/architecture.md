# ARQUITECTURA TÉCNICA Y HERRAMIENTAS: "Cabito: Aventura Tadeísta"

## 1. Stack Tecnológico Base
*   **Lenguaje:** Java 21 (Nativo). Aprovechamiento de características modernas como `records` para transferencia de datos inmutables y `switch` con *pattern matching*.
*   **Interfaz Gráfica:** Java Swing (`javax.swing` y `java.awt`). Cero dependencias externas o frameworks (prohibido JavaFX, Spring, Maven o Gradle).
*   **Entorno de Desarrollo:** Antigravity IDE (basado en VS Code).
*   **Persistencia:** Archivos de texto plano locales en formato CSV estructurado por tipos de registro (`partida_cabito.csv`), procesados mediante `java.nio.file` (`BufferedReader` / `BufferedWriter`) en la clase `GestorArchivos`.

## 2. Patrón de Diseño: MVC Estricto
La aplicación separa obligatoriamente las reglas de negocio de la interfaz gráfica para garantizar mantenibilidad.
*   **Modelo (`modelo`, `minijuegos`):** Clases puras de Java. Gestiona las matemáticas, condicionales de estado, matrices, listas y economía. **Prohibición estricta:** Ninguna clase del Modelo puede importar paquetes de `javax.swing` o `java.awt`.
*   **Vista (`vista`):** Contenedores pasivos (`JFrame`, `JPanel`). Dibuja la información que recibe y captura clics del usuario, pero no toma decisiones lógicas.
*   **Controlador (`controlador`):** Orquestador central. Escucha los eventos de la Vista (mediante `ActionListener`), llama a los métodos del Modelo para procesar la acción, orquesta el reloj interno (`javax.swing.Timer`) y ordena a la Vista actualizarse.
*   **Punto de Entrada:** La clase `Main` inicializará la interfaz gráfica obligatoriamente dentro del Event Dispatch Thread utilizando `SwingUtilities.invokeLater()`.

## 3. Navegación y Gestión de Pantallas
*   **Ventana Única:** Se utilizará un solo `JFrame` principal para toda la aplicación.
*   **CardLayout:** El cambio entre la pantalla principal, la tienda, el inventario y los minijuegos se gestionará con un gestor de diseño `CardLayout`. Esto permite apilar y alternar paneles en el centro de la pantalla sin abrir múltiples ventanas emergentes.

## 4. Estructuras de Datos y Persistencia
*   **Colecciones Dinámicas (`LinkedList`):** Uso obligatorio para el manejo del inventario del jugador (consumibles y armario) y para la gestión en tiempo real de los objetos que caen en el minijuego "Atrapa la Comida".
*   **Matrices Bidimensionales (`[][]`):** Uso obligatorio para definir el tablero lógico estático de 4x4 del minijuego de "Memoria Tadeísta" (`String[][]` para valores y `boolean[][]` para estados).
*   **Persistencia Plana en CSV (Por Tipos de Registro):** Guardado y carga de la `Partida` (incluyendo sus `LinkedList` de consumibles, armario y logros) mediante lectura y escritura línea por línea en `partida_cabito.csv`. Cada fila inicia con una etiqueta discriminadora (`PARTIDA`, `CABITO`, `MUNDO`, `LOGRO`, `CONSUMIBLE`, `ARMARIO`) seguida de sus campos separados por comas (`,`), facilitando la inspección directa, la edición manual de pruebas y la reconstrucción exacta de las colecciones en memoria sin depender de serialización binaria.

## 5. Gestión de Recursos Gráficos e Imágenes
Para asegurar un renderizado fluido y facilitar el ensamblaje visual, los assets gráficos deben cumplir pautas estrictas:
*   **Formato y Fondo:** Exclusivamente formato PNG estático. Todas las imágenes de Cabito y sus prendas deben tener un fondo 100% transparente para evitar recuadros blancos. Prohibido el uso de GIFs animados.
*   **Dimensiones Fijas (Estandarización):** Los *sprites* de Cabito en todos sus estados (Feliz, Hambriento, etc.) y los accesorios (Gorra, Gafas) deben exportarse en un lienzo de tamaño idéntico (ej. 300x300 píxeles) con el personaje centrado exactamente en la misma coordenada. Esto asegura que al superponer una prenda, encaje perfectamente sin programar cálculos de desplazamiento (X, Y).
*   **Superposición Visual (`JLayeredPane`):** La interfaz utilizará un panel de capas tridimensional para ensamblar la pantalla central:
    *   *Capa 0 (Fondo):* Escenario tadeísta actual (Plazoleta, Biblioteca, Auditorio).
    *   *Capa 1 (Personaje):* Estado actual de Cabito (PNG base).
    *   *Capa 2 (Accesorios):* Prenda equipada desde el inventario.
*   **Caché en Memoria (`ImageIO`):** Para evitar congelamientos cada vez que el reloj interno actualiza el estado, se implementará una clase utilitaria (`GestorRecursos`). Esta clase cargará todos los archivos PNG en un `HashMap` al iniciar el juego, leyéndolos desde el disco duro una sola vez y entregando las referencias directamente desde la memoria RAM cuando la Vista las solicite.