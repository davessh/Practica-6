import javax.swing.*;
import java.awt.*;

public class MagoDeLasPalabrasGrafico extends JFrame {
    private MagoDeLasPalabras juego;
    private JPanel panelActual;

    // Diferentes paneles para el juego que se crearán
    private PanelInicio panelInicio;
    private PanelJuego panelJuego;
    private PanelResultados panelResultados;

    public MagoDeLasPalabrasGrafico() {
        super("Mago de las Palabras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        juego = new MagoDeLasPalabras();

        // Inicializar paneles
        panelInicio = new PanelInicio(this);

        // Mostrar el panel de inicio primero
        mostrarPanel(panelInicio);

        setVisible(true);
    }

    public void mostrarPanel(JPanel panel) {
        if (panelActual != null) {
            getContentPane().remove(panelActual);
        }
        panelActual = panel;
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    public MagoDeLasPalabras getJuego() {
        return juego;
    }

    public void iniciarJuego(int numeroJugadores, int dificultad) {
        juego.setNumeroDeJugadores(numeroJugadores);
        juego.setDificultad(dificultad);
        panelJuego = new PanelJuego(this);
        mostrarPanel(panelJuego);
    }

    public void mostrarResultados() {
        panelResultados = new PanelResultados(this);
        mostrarPanel(panelResultados);
    }

    public void reiniciarJuego() {
        juego = new MagoDeLasPalabras();
        mostrarPanel(panelInicio);
    }

    public static void main(String[] args) {
        new MagoDeLasPalabrasGrafico();
    }
}