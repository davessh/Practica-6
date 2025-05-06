import java.util.*;


public class MagoDeLasPalabras {
    private HashMap<String, Integer> puntuaciones; // Nombre del jugador → puntaje total
    private HashSet<String> palabrasUsadas;        // Registro de palabras ya utilizadas
    private int numeroDeJugadores;
    private int dificultad;
    private int rondaActual;
    public static final int TOTAL_RONDAS = 3;
    private Diccionario diccionario;
    private List<Character> letrasGeneradas;
    private ArrayList<String> nombresJugadores;
    private int jugadorActualIndex;
    private int jugadoresQuePasaron;

    public MagoDeLasPalabras() {
        this.puntuaciones = new HashMap<>();
        this.palabrasUsadas = new HashSet<>();
        this.diccionario = new Diccionario();
        this.rondaActual = 1;
        this.nombresJugadores = new ArrayList<>();
        this.jugadorActualIndex = 0;
        this.jugadoresQuePasaron = 0;
        diccionario.cargarDesdeArchivo("G:\\4toSemestre\\POO\\Practica-6.1\\src\\palabras.txt");

//        diccionario.cargarDesdeArchivo("C:\\Users\\GF76\\IdeaProjects\\Practica-6.1\\out\\production\\Practica-6\\palabras.txt");
    }

    public String getJugadorActual() {
        if (nombresJugadores.isEmpty()) return "";
        return nombresJugadores.get(jugadorActualIndex);
    }

    public void siguienteTurno() {
        jugadorActualIndex = (jugadorActualIndex + 1) % numeroDeJugadores;
        //jugadoresQuePasaron = 0;
    }


    public void jugadorPaso() {
        jugadoresQuePasaron++;
        System.out.println("Jugador pasó. Total que han pasado: " + jugadoresQuePasaron + " de " + numeroDeJugadores);

        // Si todos los jugadores han pasado, iniciamos nueva ronda
        if (jugadoresQuePasaron >= numeroDeJugadores) {
            System.out.println("Todos los jugadores pasaron. Iniciando nueva ronda...");
            iniciarNuevaRonda();
        } else {
            // Si no han pasado todos, solo cambiamos al siguiente jugador
            siguienteTurno();
        }
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
        // Verificar que el jugador exista en las puntuaciones
        if (!puntuaciones.containsKey(jugador)) {
            System.out.println("ERROR: El jugador " + jugador + " no existe en las puntuaciones");
            return false;
        }

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
        // Verificar que el jugador exista en las puntuaciones
        if (!puntuaciones.containsKey(jugador)) {
            System.out.println("ERROR: El jugador " + jugador + " no existe en las puntuaciones");
            return;
        }
        int totalActual = puntuaciones.get(jugador);
        puntuaciones.put(jugador, Math.max(0, totalActual - puntos));
    }

    public void iniciarNuevaRonda() {
        if (rondaActual >= TOTAL_RONDAS) {
            rondaActual = TOTAL_RONDAS + 1;
            return;
        }

        rondaActual++;
        palabrasUsadas.clear();
        generarLetras(dificultad);
        jugadorActualIndex = 0;
        jugadoresQuePasaron = 0;
     }



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

    public boolean verificarLetrasDisponibles(String palabra) {
        Map<Character, Integer> contadorLetras = new HashMap<>();

        for (char c : getLetrasGeneradas()) {
            char letraMinuscula = Character.toLowerCase(c);
            contadorLetras.put(letraMinuscula, contadorLetras.getOrDefault(letraMinuscula, 0) + 1);
        }

        for (char c : palabra.toCharArray()) {
            int disponibles = contadorLetras.getOrDefault(c, 0);
            if (disponibles <= 0) {
                return false;
            }
            contadorLetras.put(c, disponibles - 1);
        }

        return true;
    }

    //Agregar una palabra al diccionario
    public void agregarPalabraADiccionario(String palabra) {
        diccionario.agregarPalabra(palabra);
    }

    //Método para devolverle los puntos si el jugador agrega una palabra al diccionario
    //porque se convierte en una válida
    public void devolverPuntos(String jugador, int puntos) {
        int totalActual = puntuaciones.get(jugador);
        puntuaciones.put(jugador, totalActual + puntos);
    }
}