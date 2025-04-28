import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Diccionario {
    // Estructura para almacenar las palabras y su puntaje correspondiente.
    private HashMap<String, Integer> palabras;

    //Constructor
    public Diccionario() {
        palabras = new HashMap<>();
    }

    /**
     * Carga las palabras desde un archivo de texto.
     * Cada línea del archivo debe contener una sola palabra.
     * Al cargarla, se calcula su puntaje y se almacena en el HashMap.
     *
     **/
    public void cargarDesdeArchivo(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Se eliminan espacios innecesarios y se convierte a minúsculas
                linea = linea.trim().toLowerCase();
                if (!linea.isEmpty()) {
                    // Si la línea no está vacía, se añade al diccionario con su puntaje
                    palabras.put(linea, calcularPuntajePalabra(linea));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo " + e.getMessage());
        }
    }

    /**
     * Calcula el puntaje de una palabra.
     * Las vocales valen 5 puntos y las consonantes 3 puntos.
     *
     */
    public int calcularPuntajePalabra(String palabra){
        int puntos = 0;
        String vocales = "aeiouAEIOUáéíóúÁÉÍÓÚ";
        return palabra.chars()
                .filter(Character::isLetter) // Solo se consideran letras
                .map(c -> vocales.indexOf(c) != -1 ? 5 : 3) // Se asigna puntaje según sea vocal o no
                .sum(); // Se suman todos los valores
    }
    /**
     * Verifica si una palabra existe en el diccionario.
     *
     */
    public boolean contienePalabra(String palabra) {
        return palabras.containsKey(palabra.toLowerCase());
    }

    /**
     * Obtiene el puntaje de una palabra almacenada en el diccionario.
     * Si no se encuentra, devuelve 0.
     *
     */
    public int obtenerPuntaje(String palabra) {
        return palabras.getOrDefault(palabra.toLowerCase(), 0);//si no encuentra la palabra devuelve 0
    }
}