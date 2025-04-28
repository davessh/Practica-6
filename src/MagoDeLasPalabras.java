import java.util.*;
import java.util.stream.Collectors;

public class MagoDeLasPalabras {
    private HashMap<String, Integer> puntuaciones; // Nombre del jugador → puntaje total
    private HashSet<String> palabrasUsadas;        // Registro de palabras ya utilizadas
    private int numeroDeJugadores;
    private int dificultad;
    private int rondaActual;
    private static final int TOTAL_RONDAS = 3;
    private Diccionario diccionario;
    private List<Character> letrasGeneradas;
    private ArrayList<String> nombresJugadores;

    /**
     * Constructor principal. Inicializa todas las estructuras necesarias y carga el diccionario desde archivo.
     */
    public MagoDeLasPalabras() {
        this.puntuaciones = new HashMap<>();
        this.palabrasUsadas = new HashSet<>();
        this.diccionario = new Diccionario();
        this.rondaActual = 1;
        this.nombresJugadores = new ArrayList<>();
        diccionario.cargarDesdeArchivo("palabras.txt");
    }

    // Getters y setters
    public void setNumeroDeJugadores(int numeroDeJugadores) {
        this.numeroDeJugadores = numeroDeJugadores;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public int getNumeroDeJugadores() {
        return numeroDeJugadores;
    }

    public int getDificultad() {
        return dificultad;
    }

    public HashMap<String, Integer> getPuntuaciones() {
        return puntuaciones;
    }

    public HashSet<String> getPalabrasUsadas() {
        return palabrasUsadas;
    }

    public List<Character> getLetrasGeneradas() {
        return letrasGeneradas;
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public ArrayList<String> getNombresJugadores() {
        return nombresJugadores;
    }

    /**
     * Verifica si el juego ha terminado en función del número total de rondas.
     */
    public boolean juegoTerminado() {
        return rondaActual > TOTAL_RONDAS;
    }

    /**
     * Inicializa un jugador en la estructura de puntuaciones.
     */
    public void inicializarJugador(String nombre) {
        puntuaciones.put(nombre, 0);
        nombresJugadores.add(nombre);
    }

    /**
     * Genera un conjunto de letras aleatorias en función de la dificultad elegida.
     */
    public void generarLetras(int dificultad) {
        this.letrasGeneradas = GeneradorLetras.generarLetras(10, dificultad);
    }

    /**
     * Verifica si una palabra ya ha sido utilizada en esta ronda.
     */
    public boolean esPalabraUsada(String palabra) {
        return palabrasUsadas.contains(palabra);
    }

    /**
     * Verifica si una palabra es válida, es decir, si está en el diccionario.
     */
    public boolean esPalabraValida(String palabra) {
        return diccionario.contienePalabra(palabra);
    }

    /**
     * Devuelve el puntaje que vale una palabra según el diccionario.
     */
    public int obtenerPuntajePalabra(String palabra) {
        return diccionario.obtenerPuntaje(palabra);
    }

    /**
     * Registra una palabra válida que no haya sido usada antes.
     * Suma los puntos al jugador y la guarda como palabra utilizada.
     */
    public boolean registrarPalabra(String jugador, String palabra) {
        if (!esPalabraUsada(palabra) && esPalabraValida(palabra)) {
            int puntos = obtenerPuntajePalabra(palabra);
            int totalActual = puntuaciones.get(jugador);
            puntuaciones.put(jugador, totalActual + puntos);
            palabrasUsadas.add(palabra);
            return true;
        }
        return false;
    }

    /**
     * Resta puntos al jugador. El puntaje no puede ser menor que cero.
     */
    public void restarPuntos(String jugador, int puntos) {
        int totalActual = puntuaciones.get(jugador);
        puntuaciones.put(jugador, Math.max(0, totalActual - puntos));
    }

    /**
     * Avanza a la siguiente ronda y reinicia las palabras usadas.
     * También se generan nuevas letras para la nueva ronda.
     */
    public void iniciarNuevaRonda() {
        rondaActual++;
        palabrasUsadas.clear();
        generarLetras(dificultad);
    }

    /**
     * Determina el ganador del juego al finalizar las rondas.
     * Si hay empate, lo informa. Si no hay jugadores, también.
     */
    public String determinarGanador() {
        if (puntuaciones.isEmpty()) {
            return "No hay jugadores";
        }

        int maxPuntuacion = puntuaciones.values().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        long cantidadGanadores = puntuaciones.values().stream()
                .filter(puntos -> puntos == maxPuntuacion)
                .count();

        if (cantidadGanadores > 1) {
            return "EMPATE";
        } else {
            return puntuaciones.entrySet().stream()
                    .filter(entry -> entry.getValue() == maxPuntuacion)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("No hay ganador");
        }
    }
}