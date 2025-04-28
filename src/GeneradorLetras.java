import java.util.*;

public class GeneradorLetras {
    // Lista de letras utilizadas en el modo normal del juego.
    // Se declara como 'static final' porque su contenido no cambia en tiempo de ejecución.
    private static final List<Character> LETRAS_NORMALES = Arrays.asList(
            'A', 'E', 'I', 'O', 'U', 'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L',
            'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'
    );

    // Lista de letras utilizadas en el modo experto.
    // Incluye vocales acentuadas y la letra Ñ.
    private static final List<Character> LETRAS_EXPERTAS = Arrays.asList(
            'Á','A', 'É','E', 'Í','I', 'Ó','O', 'Ú','U', 'Ñ','N', 'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K',
            'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'
    );


    private static final List<Character> VOCALES = Arrays.asList(
            'A','E','I','O','U'
    );
    /**
     * Genera una lista de letras aleatorias a partir del modo de juego seleccionado.
     */

//    public static List<Character> generarVocal(){
//        Random rand = new Random();
//        List<Character> vocalGenerada = new ArrayList<>(VOCALES);
//        List<Character> vocal = new ArrayList<>();
//
//    }
    public static List<Character> generarLetras(int cantidad, int experto) {
        Random random = new Random(); // Se utiliza para seleccionar letras aleatorias
        List<Character> letrasGeneradas = new ArrayList<>();
        List<Character> fuente = new ArrayList<>();

        // Se elige la lista de letras según el nivel de dificultad
        if (experto == 1){
            fuente = LETRAS_NORMALES;
        } else if (experto == 2){
            fuente = LETRAS_EXPERTAS;
        }
        List<Character> vocal = new ArrayList<>(VOCALES);

        // Se generan las letras aleatorias a partir de la fuente seleccionada
        for (int i = 0; i < cantidad-1; i++) {
            letrasGeneradas.add(fuente.get(random.nextInt(fuente.size())));
        }
        letrasGeneradas.add(vocal.get(random.nextInt(vocal.size())));

        return letrasGeneradas;
    }
}
