# CONTEXTO GENERAL DEL PROYECTO

## Naturaleza del Proyecto
El proyecto es una aplicación de mascota virtual de escritorio desarrollada estrictamente en **Java 21 nativo**, sin el uso de frameworks externos (como Spring, Maven o Gradle) ni bases de datos. La interfaz gráfica se construirá utilizando exclusivamente **Java Swing**.

## El Protagonista
El personaje principal es **Cabito**, un perro animado que representa a la mascota oficial de la Universidad Jorge Tadeo Lozano (UTADEO). El tono del juego es universitario, colorido y enfocado en la vida académica y social de un estudiante.

## Directrices Técnicas (Arquitectura)
1. **Patrón MVC Estricto:** Separación total entre la lógica (Modelo), la interfaz (Vista) y el puente de eventos (Controlador). El paquete modelo no debe tener ninguna importación de `javax.swing`.
2. **Estructuras de Datos:** Obligatorio el uso de Colecciones dinámicas (`LinkedList`) para gestionar inventarios y objetos en pantalla, y arreglos/matrices bidimensionales (`[][]`) para tableros lógicos.
3. **Persistencia:** Todo el guardado y carga de progreso se realiza mediante archivos de texto plano en formato **CSV estructurado por tipos de registro** (`partida_cabito.csv`), utilizando lectura y escritura nativa (`java.nio.file` / `BufferedReader` y `BufferedWriter`), sin emplear serialización binaria.
4. **Navegación Visual:** Uso de `CardLayout` para intercambiar pantallas en una sola ventana, y `JLayeredPane` para superponer imágenes PNG transparentes (ej. ponerle ropa a la mascota).

---

# DOCUMENTO DE ALCANCE: "Cabito: Aventura Tadeísta"

## 1. Núcleo del Sistema y Atributos de Cabito
Cabito, la mascota oficial, posee 6 atributos principales medidos en una escala estricta de 0 a 100. El juego funciona con un reloj interno que aplica un desgaste cada 20 segundos de tiempo real, simulando el paso del tiempo.

### 1.1. Atributos y Desgaste:
* **Hambre (Inicia en 80):** Pierde -3 puntos por ciclo. Si llega a 0, la Salud empieza a bajar.
* **Energía (Inicia en 100):** Pierde -2 puntos por ciclo. Necesaria para explorar y jugar.
* **Higiene (Inicia en 80):** Pierde -2 puntos por ciclo. Si llega a 0, la Salud empieza a bajar.
* **Felicidad (Inicia en 80):** Pierde -2 puntos por ciclo.
* **Salud (Inicia en 100):** Se mantiene estable a menos que el Hambre o Higiene estén en 0 (pierde -5 puntos por ciclo).
* **Inteligencia (Inicia en 10):** No se degrada con el tiempo. Solo sube estudiando o respondiendo quizzes.

### 1.2. Estados Gráficos Automáticos (Jerarquía de Condicionales):
La interfaz central cambiará la imagen PNG de Cabito (300x300px) automáticamente evaluando sus atributos de arriba hacia abajo. Existen dos estados temporales visuales que duran 3 a 5 segundos cuando el usuario hace clic en los botones: DORMIDO y JUGANDO.
* **ENFERMO:** Si Salud <= 25 (Bloquea minijuegos y exploración).
* **HAMBRIENTO:** Si Hambre <= 25.
* **CANSADO:** Si Energía <= 20.
* **TRISTE:** Si Felicidad <= 25 o Higiene <= 20.
* **FELIZ:** Si Felicidad >= 80, Hambre >= 70 y Salud >= 70.
* **NORMAL:** Si no cumple ninguna de las anteriores (estado base).

## 2. Sistema de Progresión (Independiente de los Logros)
La progresión de niveles se basa únicamente en la acumulación de Experiencia (XP). Cabito inicia en el Nivel 0. Subir de nivel desbloquea nuevas zonas o entrega bonos económicos.
* **Nivel 0 (0 XP):** Inicia el juego. Solo la Plazoleta está desbloqueada.
* **Nivel 1 (100 XP):** Recompensa de +15 Monedas.
* **Nivel 2 (250 XP):** Desbloquea el Mundo 2: La Biblioteca UTADEO.
* **Nivel 3 (450 XP):** Recompensa de +20 Monedas.
* **Nivel 4 (700 XP):** Desbloquea el Mundo 3: El Auditorio.
* **Nivel 5 (1000 XP - Nivel Máximo):** Recompensa final de +50 Monedas.

## 3. Economía, Tienda de la Universidad e Inventario
La Tienda de la Universidad vende productos cuyos precios oscilan entre 10 y 30 monedas para mantener una dificultad moderada. Todo lo adquirido (o ganado) va al Inventario, el cual está dividido en dos secciones distintas.

### 3.1. Sección de Inventario A: "Consumibles"
Objetos de un solo uso que desaparecen al aplicarse en Cabito.
* **Comida (Hambre):** Manzana (10 monedas | +20 Hambre), Sopa Instantánea (15 monedas | +35 Hambre, -5 Salud), Perro Caliente (25 monedas | +50 Hambre, +10 Felicidad).
* **Bebidas (Energía):** Café Clásico (12 monedas | +30 Energía), Energizante (20 monedas | +50 Energía, -10 Salud).
* **Salud/Aseo:** Gel Antibacterial (15 monedas | +40 Higiene), Medicina (25 monedas | +50 Salud).
* **Académico:** Kit de Fichas (30 monedas | +25 Inteligencia, +20 XP).

### 3.2. Sección de Inventario B: "Armario" (Accesorios por Logros)
Aquí aparecen las prendas de la universidad que se desbloquean al cumplir los Logros. No se gastan y se pueden "Equipar" (dibujándose como una capa PNG transparente sobre Cabito).
* **Carné Estudiantil:** Se desbloquea al alcanzar el Nivel 1.
* **Gorra de la Universidad:** Se desbloquea al comprar 5 veces en la Tienda.
* **Gafas Intelectuales:** Se desbloquea al llegar a 80 de Inteligencia.
* **Buzo Tadeísta:** Se desbloquea al acumular 3 Victorias en cualquier minijuego.
* **Birrete de Grado:** Se desbloquea al alcanzar el Nivel 4.

## 4. Escenarios y Eventos Aleatorios ("Explorar")
El juego cuenta con tres mundos: La Plazoleta, La Biblioteca y El Auditorio. Presionar el botón "Explorar" cuesta -10 puntos de Energía. Al hacerlo, hay un 50% de probabilidad de que aparezca un Hallazgo (Historia de la U) o un Quiz (Académico).
* **Recompensa por acertar:** +5 Monedas, +25 XP, +8 Inteligencia.
* **Penalización por fallar:** -3 Monedas, +5 XP de consolación.
* **Ejemplo Hallazgo Histórico:** "¿En qué año fue fundada la Universidad Jorge Tadeo Lozano?" (A. 1954 / B. 1810 / C. 1991. Correcta: A).
* **Ejemplo Hallazgo de la Plazoleta:** "El diseño arquitectónico abierto del campus en Bogotá busca integrarse con la ciudad. ¿En qué barrio está?" (A. Chapinero / B. Las Nieves / C. Teusaquillo. Correcta: B).

## 5. Minijuegos Algorítmicos

### 5.1. Minijuego 1: "Atrapa la Comida" (Lógica de Teclado y Listas)
* **Mecánica:** Juego dinámico de 30 segundos. El jugador mueve a Cabito usando las flechas del teclado. Desde arriba caen alimentos gestionados por una LinkedList.
* **Puntuaciones:** Manzana/Galleta (+10 pts), Queso (+15 pts), Pez (+25 pts).
* **Recompensa:** Cada 50 puntos otorga 1 Moneda (máximo 8) y +30 XP.
* **Condición de Victoria:** Superar los 150 puntos cuenta como "Victoria" para el Logro #4 y deposita una "Manzana" gratis en el Inventario.

### 5.2. Minijuego 2: "Memoria Tadeísta" (Lógica de Matrices)
* **Mecánica:** Matriz de botones 4x4 (16 cartas, 8 parejas). El jugador debe emparejar conceptos de la universidad usando el ratón.
* **Restricción:** Tiene un máximo de 14 intentos/turnos.
* **Recompensa:** Ganar otorga +10 Monedas, +40 XP y cuenta como 1 Victoria para el Logro #4. Perder otorga 0 Monedas y +10 XP.
*(Ambos minijuegos exigen tener al menos 15 puntos de Energía y Salud mayor a 25).*

## 6. Sistema de Persistencia (CSV por Tipos de Registro)
El botón Guardar Partida exporta todo el estado actual en un único archivo de texto plano `partida_cabito.csv` donde la primera columna (`TIPO_REGISTRO`) identifica la estructura de cada fila:
* **`PARTIDA`:** ID/Nombre de la partida, Nivel, XP, Monedas, Mundo actual, Compras realizadas y Victorias en minijuegos.
* **`CABITO`:** Los 6 atributos principales de Cabito (`hambre`, `energia`, `higiene`, `felicidad`, `salud`, `inteligencia`) y su estado actual.
* **`MUNDO`:** Cada escenario de la universidad y su estado de desbloqueo (`true`/`false`).
* **`LOGRO`:** Cada logro del juego, su estado de desbloqueo (`true`/`false`) y la prenda asociada.
* **`CONSUMIBLE`:** Cada ítem consumible dentro de la `LinkedList<Item>` del inventario (nombre, precio, bonificadores de atributos y ruta de imagen).
* **`ARMARIO`:** Cada accesorio dentro de la `LinkedList<Item>` del armario, indicando si está desbloqueado y si está actualmente equipado (`true`/`false`).