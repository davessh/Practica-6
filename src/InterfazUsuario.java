import java.util.*;

public class InterfazUsuario {
    private Scanner scanner;
    private MagoDeLasPalabras juego;
    private int dificultad;

    public InterfazUsuario() {
        this.scanner = new Scanner(System.in);
        this.juego = new MagoDeLasPalabras();
    }

    public void iniciar() {
        mostrarMenu();
        inicializarJugadores();
        while (!juego.juegoTerminado()) {
            iniciarRonda();
            if (juego.juegoTerminado()) {
                mostrarResultadosFinales();
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n◈ MAGO DE LAS PALABRAS ◈");

        int numeroDeJugadores;
        do {
            System.out.print("Ingrese el número de jugadores (2 a 4): ");
            numeroDeJugadores = scanner.nextInt();
        } while (numeroDeJugadores < 2 || numeroDeJugadores > 4);
        juego.setNumeroDeJugadores(numeroDeJugadores);

        System.out.println("Modos de juego disponibles: ");
        System.out.println("1) Normal");
        System.out.println("2) Experto");
        System.out.print("Seleccione la dificultad:");
        do {
            dificultad = scanner.nextInt();
        } while (dificultad != 1 && dificultad != 2);
        juego.setDificultad(dificultad);
    }

    private void inicializarJugadores() {
        scanner.nextLine();
        for (int i = 1; i <= juego.getNumeroDeJugadores(); i++) {
            System.out.print("Nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            juego.inicializarJugador(nombre);
        }
    }

    private void iniciarRonda() {
        System.out.println("\nRONDA " + juego.getRondaActual());
        juego.generarLetras(juego.getDificultad());
        mostrarPalabrasGeneradas();

        boolean rondaTerminada = false;
        ArrayList<String> jugadoresPasaron = new ArrayList<>();

        while (!rondaTerminada) {
            for (String jugador : juego.getNombresJugadores()) {
                if (jugadoresPasaron.contains(jugador)) continue;
                boolean turnoTerminado = false;
                while (!turnoTerminado) {
                    System.out.println("Turno de " + jugador);
                    System.out.println("1) Escribir palabra");
                    System.out.println("2) Pasar turno");
                    System.out.print("Selecciona tu jugada:");
                    int opcion = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcion) {
                        case 1:
                            mostrarPalabrasGeneradas();
                            procesarPalabra(jugador);
                            System.out.println("Puntuación actual de " + jugador + ": " + juego.getPuntuaciones().get(jugador) + " puntos");
                            turnoTerminado = true;
                            break;
                        case 2:
                            System.out.println(jugador + " ha pasado su turno.");
                            jugadoresPasaron.add(jugador);
                            System.out.println(jugador + ": " + juego.getPuntuaciones().get(jugador) + " puntos");
                            turnoTerminado = true;
                            break;
                        default:
                            System.out.println("Opción no válida. Intenta nuevamente.");
                            break;
                    }
                }
                if (jugadoresPasaron.size() == juego.getNumeroDeJugadores()) {
                    rondaTerminada = true;
                    break;
                }
            }
        }

        mostrarResultadosRonda();

        juego.iniciarNuevaRonda();
    }

    private void procesarPalabra(String jugador) {
        System.out.print(jugador + ", ingresa una palabra: ");
        String palabra = scanner.nextLine().toLowerCase();

        if (juego.esPalabraUsada(palabra)) {
            mostrarPalabrasUsadas();
            System.out.println("Esa palabra ya fue usada, no se otorgan puntos.");
            return;
        }

        if (!verificarLetrasDisponibles(palabra)) {
            if (dificultad == 2) {
                System.out.println("No puedes formar esa palabra con las letras disponibles.");
                System.out.println("Se restan 7 puntos (modo experto)");
                mostrarPalabrasUsadas();
                juego.restarPuntos(jugador, 7);
            } else {
                System.out.println("No puedes formar esa palabra con las letras disponibles.");
                System.out.println("Se restan 5 puntos");
                mostrarPalabrasUsadas();
                juego.restarPuntos(jugador, 5);
            }
            return;
        }

        if (!juego.esPalabraValida(palabra)) {
            System.out.println("Palabra no encontrada en el diccionario.");
            System.out.println("Se restan 5 puntos por palabra inválida.");
            mostrarPalabrasUsadas();
            juego.restarPuntos(jugador, 5);
            return;
        }
        int puntos = juego.obtenerPuntajePalabra(palabra);
        System.out.println("Palabra válida. Puntos obtenidos: " + puntos);
        juego.registrarPalabra(jugador, palabra);
        mostrarPalabrasUsadas();
    }
    private boolean verificarLetrasDisponibles(String palabra) {
        Map<Character, Integer> contadorLetras = new HashMap<>();

        for (char c : juego.getLetrasGeneradas()) {
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
    private void mostrarPalabrasGeneradas() {
        List<Character> letras = juego.getLetrasGeneradas();

        System.out.print("Letras generadas: ");
        for (char letra : letras) {
            System.out.print(letra + " ");
        }
        System.out.println("\n");
    }

    private void mostrarPalabrasUsadas() {
        if (juego.getPalabrasUsadas().isEmpty()) {
            System.out.print("\n");
        } else {
            System.out.println("Palabras usadas: ");
            juego.getPalabrasUsadas().stream()
                    .forEach(System.out::println);
        }
    }
    private void mostrarResultadosRonda() {
        System.out.println("\n Resultados de la ronda " + juego.getRondaActual());
        juego.getPuntuaciones().forEach((jugador, puntos) ->
                System.out.println(jugador + ": " + puntos + " puntos"));
    }

    private void mostrarResultadosFinales() {
        System.out.println("\nResultados finales: ");

        // Usando lambda para mostrar las puntuaciones
        juego.getPuntuaciones().forEach((jugador, puntos) ->
                System.out.println(jugador + ": " + puntos + " puntos"));

        String resultado = juego.determinarGanador();

        if (resultado.equals("EMPATE")) {
            int maxPuntuacion = juego.getPuntuaciones().values().stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0);
            System.out.println("\nHubo un empate con " + maxPuntuacion + " puntos");
        } else if (resultado.equals("No hay jugadores") || resultado.equals("No hay ganador")) {
            System.out.println("\n" + resultado);
        } else {
            int puntosGanador = juego.getPuntuaciones().get(resultado);
            System.out.println("\nGanador es " + resultado + " con " + puntosGanador + " en puntaje acumulado");
        }
    }
}