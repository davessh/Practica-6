import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class InterfazGrafica {
    private MagoDeLasPalabras juego;

    //Componentes principales
    private JFrame frame;
    private JPanel panelLetras, panelPalabra, panelPuntuaciones, panelBotones, panelPalabrasUsadas;
    private JTextField campoTexto;
    private JButton botonEnviar, botonPasar, botonJugar, botonSalir;
    private ArrayList<JButton> botonesLetras;
    private ArrayList<JLabel> etiquetasPuntuaciones;
    private JLabel etiquetaTurnoActual;
    private JLabel etiquetaRonda;
    private JLabel etiquetaPuntosRonda;
    private JTextArea areaPalabrasUsadas;
    private StringBuilder palabraActual;
    private ArrayList<Integer> indicesUsados;

    public InterfazGrafica() {
        this.juego = new MagoDeLasPalabras();
        this.botonesLetras = new ArrayList<>();
        this.etiquetasPuntuaciones = new ArrayList<>();
        this.palabraActual = new StringBuilder();
        this.indicesUsados = new ArrayList<>();
        iniciarMenuJuego();
    }
    public void iniciarMenuJuego() {
        frame = new JFrame("Mago de las Palabras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(new Color(250, 245, 255));

        JLabel tituloLabel = new JLabel("✧ MAGO DE LAS PALABRAS ✧");
        tituloLabel.setFont(new Font("Serif", Font.ITALIC, 40));
        tituloLabel.setForeground(Color.decode("#550078"));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMenu.add(Box.createVerticalStrut(20));
        panelMenu.add(tituloLabel);

        ImageIcon icono = new ImageIcon("G:\\4toSemestre\\POO\\Practica-6.1\\src\\mago.png");
        //ImageIcon icono = new ImageIcon("C:\\Users\\Usuario\\IdeaProjects\\Practica-6.2\\src\\mago.png");
        Image imagen = icono.getImage();
        Image nuevaImagen = imagen.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        icono = new ImageIcon(nuevaImagen);
        JLabel etiquetaImagen = new JLabel(icono);
        etiquetaImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMenu.add(Box.createVerticalStrut(20));
        panelMenu.add(etiquetaImagen);


        botonJugar = new JButton("Jugar");
        botonJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonJugar.setMaximumSize(new Dimension(200, 40));
        botonJugar.setFont(new Font("Arial", Font.BOLD, 16));
        panelMenu.add(Box.createVerticalStrut(30));
        panelMenu.add(botonJugar);

        botonSalir = new JButton("Salir");
        botonSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonSalir.setMaximumSize(new Dimension(200, 40));
        botonSalir.setFont(new Font("Arial", Font.BOLD, 16));
        panelMenu.add(Box.createVerticalStrut(10));
        panelMenu.add(botonSalir);

        botonSalir.addActionListener(e -> System.exit(0));
        botonJugar.addActionListener(e -> {
            inicializarJugadores();
        });

        frame.add(panelMenu, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void inicializarJugadores() {
        int numeroDeJugadores = 0;

        //seleccionar número de jugadores
        while (numeroDeJugadores < 2 || numeroDeJugadores > 4) {
            String input = JOptionPane.showInputDialog(frame,
                    "Ingrese el número de jugadores (2 a 4):",
                    "Número de Jugadores", JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                return; // El usuario canceló
            }

            try {
                numeroDeJugadores = Integer.parseInt(input);
                if (numeroDeJugadores < 2 || numeroDeJugadores > 4) {
                    JOptionPane.showMessageDialog(frame,
                            "Por favor ingrese un número entre 2 y 4",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame,
                        "Por favor ingrese un número válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        juego.setNumeroDeJugadores(numeroDeJugadores);

        // Seleccion de dificultad
        Object[] opciones = {"Normal", "Experto"};
        int opcion = JOptionPane.showOptionDialog(frame,
                "Seleccione la dificultad del juego:",
                "Dificultad",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (opcion == JOptionPane.CLOSED_OPTION) {
            return; // El usuario canceló
        }

        int dificultad = opcion + 1; // 1 para Normal, 2 para Experto
        juego.setDificultad(dificultad);

        // Pedir nombres de los jugadores
        for (int i = 1; i <= numeroDeJugadores; i++) {
            String nombre = JOptionPane.showInputDialog(frame,
                    "Nombre del jugador " + i + ":",
                    "Nombre del Jugador",
                    JOptionPane.QUESTION_MESSAGE);

            if (nombre == null) {
                nombre = "Jugador " + i;
            } else if (nombre.trim().isEmpty()) {
                nombre = "Jugador " + i;
            }

            juego.inicializarJugador(nombre);
        }
        iniciarJuego();
    }

    private void iniciarJuego() {
        if (frame != null) {
            frame.dispose();
        }
        juego.generarLetras(juego.getDificultad());
        frame = new JFrame("Mago de las Palabras - Juego");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 700); // Aumentamos el ancho para acomodar el nuevo panel
        frame.setLayout(new BorderLayout());

        // Panel principal que contendrá todo excepto el panel de palabras usadas
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setBackground(new Color(245, 245, 255));

        // Panel para palabras usadas (lado izquierdo)
        panelPalabrasUsadas = new JPanel(new BorderLayout());
        panelPalabrasUsadas.setPreferredSize(new Dimension(250, frame.getHeight()));
        panelPalabrasUsadas.setBackground(new Color(240, 240, 250));
        panelPalabrasUsadas.setBorder(BorderFactory.createTitledBorder("Palabras Usadas"));

        areaPalabrasUsadas = new JTextArea();
        areaPalabrasUsadas.setEditable(false);
        areaPalabrasUsadas.setFont(new Font("Serif", Font.PLAIN, 14));
        areaPalabrasUsadas.setLineWrap(true);
        areaPalabrasUsadas.setWrapStyleWord(true);
        areaPalabrasUsadas.setBackground(new Color(240, 240, 250));

        JScrollPane scrollPalabras = new JScrollPane(areaPalabrasUsadas);
        panelPalabrasUsadas.add(scrollPalabras, BorderLayout.CENTER);

        frame.add(panelPalabrasUsadas, BorderLayout.WEST);

        // Panel para mostrar las letras generadas
        panelLetras = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelLetras.setBounds(350, 100, 500, 150);
        panelLetras.setBackground(new Color(230, 230, 250));
        panelPrincipal.add(panelLetras);

        // Panel para la palabra que se está formando
        panelPalabra = new JPanel(new BorderLayout());
        panelPalabra.setBounds(350, 270, 500, 80);
        panelPalabra.setBackground(new Color(255, 250, 240));

        campoTexto = new JTextField();
        campoTexto.setFont(new Font("Arial", Font.BOLD, 24));
        campoTexto.setHorizontalAlignment(JTextField.CENTER);
        campoTexto.setEditable(false);
        panelPalabra.add(campoTexto, BorderLayout.CENTER);

        panelPrincipal.add(panelPalabra);

        // Panel para puntuaciones
        panelPuntuaciones = new JPanel();
        panelPuntuaciones.setLayout(new BoxLayout(panelPuntuaciones, BoxLayout.Y_AXIS));
        panelPuntuaciones.setBounds(900, 100, 170, 250);
        panelPuntuaciones.setBackground(new Color(240, 248, 255));
        panelPuntuaciones.setBorder(BorderFactory.createTitledBorder("Puntuaciones"));
        panelPrincipal.add(panelPuntuaciones);

        // Panel de botones
        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.setBounds(350, 370, 500, 100);
        panelBotones.setBackground(new Color(250, 240, 230));

        botonEnviar = new JButton("Enviar Palabra");
        botonEnviar.setPreferredSize(new Dimension(150, 40));
        botonEnviar.setFont(new Font("Arial", Font.BOLD, 14));

        botonPasar = new JButton("Pasar Turno");
        botonPasar.setPreferredSize(new Dimension(150, 40));
        botonPasar.setFont(new Font("Arial", Font.BOLD, 14));

        panelBotones.add(botonEnviar);
        panelBotones.add(botonPasar);

        panelPrincipal.add(panelBotones);

        // Etiqueta para el turno actual
        etiquetaTurnoActual = new JLabel();
        etiquetaTurnoActual.setBounds(350, 30, 500, 30);
        etiquetaTurnoActual.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaTurnoActual.setHorizontalAlignment(JLabel.CENTER);
        panelPrincipal.add(etiquetaTurnoActual);

        // Etiqueta para la ronda
        etiquetaRonda = new JLabel();
        etiquetaRonda.setBounds(350, 60, 500, 30);
        etiquetaRonda.setFont(new Font("Arial", Font.ITALIC, 16));
        etiquetaRonda.setHorizontalAlignment(JLabel.CENTER);
        panelPrincipal.add(etiquetaRonda);

        // Etiqueta para los puntos de la ronda
        etiquetaPuntosRonda = new JLabel();
        etiquetaPuntosRonda.setBounds(350, 480, 500, 30);
        etiquetaPuntosRonda.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaPuntosRonda.setHorizontalAlignment(JLabel.CENTER);
        panelPrincipal.add(etiquetaPuntosRonda);

        frame.add(panelPrincipal, BorderLayout.CENTER);

        botonEnviar.addActionListener(e -> procesarPalabra());
        botonPasar.addActionListener(e -> pasarTurno());
        actualizarPuntuaciones();
        actualizarPalabrasUsadas(); // Actualizamos el panel de palabras usadas
        iniciarRonda();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void iniciarRonda() {
        // Limpiar el panel de palabras usadas al comenzar nueva ronda
        areaPalabrasUsadas.setText("");

        etiquetaRonda.setText("RONDA " + juego.getRondaActual() + " de 3");
        etiquetaTurnoActual.setText("Turno de: " + juego.getJugadorActual());

        // Mostrar las letras generadas
        mostrarLetrasGeneradas();

        resetearPalabraSinNuevasLetras();
        actualizarPuntuaciones();
    }

    private void mostrarLetrasGeneradas() {
        panelLetras.removeAll();
        botonesLetras.clear();

        List<Character> letras = juego.getLetrasGeneradas();

        for (int i = 0; i < letras.size(); i++) {
            final int indice = i;
            JButton botonLetra = new JButton(letras.get(i).toString());
            botonLetra.setFont(new Font("Arial", Font.BOLD, 24));
            botonLetra.setPreferredSize(new Dimension(60, 60));


            botonLetra.addActionListener(e -> {
                if (!indicesUsados.contains(indice)) {
                    palabraActual.append(letras.get(indice));
                    campoTexto.setText(palabraActual.toString().toLowerCase());
                    indicesUsados.add(indice);
                    botonLetra.setEnabled(false);
                }
            });

            botonesLetras.add(botonLetra);
            panelLetras.add(botonLetra);
        }

        // Agregar botón para borrar letras
        JButton botonBorrar = new JButton("←");
        botonBorrar.setFont(new Font("Arial", Font.BOLD, 24));
        botonBorrar.setPreferredSize(new Dimension(60, 60));

        botonBorrar.addActionListener(e -> {
            if (palabraActual.length() > 0 && !indicesUsados.isEmpty()) {
                int ultimoIndice = indicesUsados.remove(indicesUsados.size() - 1);
                palabraActual.deleteCharAt(palabraActual.length() - 1);
                campoTexto.setText(palabraActual.toString().toLowerCase());
                botonesLetras.get(ultimoIndice).setEnabled(true);
            }
        });

        panelLetras.add(botonBorrar);

        panelLetras.revalidate();
        panelLetras.repaint();
    }

    private void procesarPalabra() {
        String palabra = palabraActual.toString().toLowerCase();
        String jugadorActual = juego.getJugadorActual();

        if (palabra.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Por favor forma una palabra con las letras disponibles",
                    "Palabra vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (juego.esPalabraUsada(palabra)) {
            JOptionPane.showMessageDialog(frame,
                    "Esa palabra ya fue usada en esta ronda.\nNo se otorgan puntos.",
                    "Palabra repetida", JOptionPane.WARNING_MESSAGE);
            juego.siguienteTurno();
            actualizarInterfazTurno();
            resetearPalabraSinNuevasLetras();
            return;
        }

        if (!juego.verificarLetrasDisponibles(palabra)) {
            int puntosPenalizacion = (juego.getDificultad() == 2) ? 7 : 5;
            JOptionPane.showMessageDialog(frame,
                    "No puedes formar esa palabra con las letras disponibles.\n" +
                            "Se restan " + puntosPenalizacion + " puntos.",
                    "Letras inválidas", JOptionPane.ERROR_MESSAGE);
            juego.restarPuntos(jugadorActual, puntosPenalizacion);
            juego.siguienteTurno();
            actualizarInterfazTurno();
            actualizarPuntuaciones();
            resetearPalabraSinNuevasLetras();
            return;
        }

        if (!juego.esPalabraValida(palabra)) {
            int opcion = JOptionPane.showConfirmDialog(frame,
                    "Palabra no encontrada en el diccionario.\n" +
                            "¿Deseas agregarla al diccionario?",
                    "Palabra no válida", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                juego.agregarPalabraADiccionario(palabra);
                int puntos = juego.obtenerPuntajePalabra(palabra);
                juego.registrarPalabra(jugadorActual, palabra);

                JOptionPane.showMessageDialog(frame,
                        "Palabra agregada al diccionario.\nPuntos obtenidos: " + puntos,
                        "Palabra registrada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Se restan 5 puntos por palabra inválida.",
                        "Penalización", JOptionPane.WARNING_MESSAGE);
                juego.restarPuntos(jugadorActual, 5);
            }

            juego.siguienteTurno();
            actualizarInterfazTurno();
            actualizarPuntuaciones();
            actualizarPalabrasUsadas(); // Actualizamos el panel de palabras usadas
            resetearPalabraSinNuevasLetras();
            return;
        }

        // Palabra válida
        int puntos = juego.obtenerPuntajePalabra(palabra);
        juego.registrarPalabra(jugadorActual, palabra);

        JOptionPane.showMessageDialog(frame,
                "¡Palabra válida!\nPuntos obtenidos: " + puntos,
                "Puntos obtenidos", JOptionPane.INFORMATION_MESSAGE);

        juego.siguienteTurno();
        actualizarInterfazTurno();
        actualizarPuntuaciones();
        actualizarPalabrasUsadas(); // Actualizamos el panel de palabras usadas
        resetearPalabraSinNuevasLetras();

        if (juego.juegoTerminado()) {
            mostrarResultadosFinales();
        }
    }

    private void actualizarPalabrasUsadas() {
        HashSet<String> palabrasUsadas = juego.getPalabrasUsadas();

        if (palabrasUsadas.isEmpty()) {
            areaPalabrasUsadas.setText("No hay palabras usadas aún en esta ronda");
            return;
        }

        StringBuilder sb = new StringBuilder("Palabras usadas en esta ronda:\n\n");

        Iterator<String> iterador = palabrasUsadas.iterator();
        while (iterador.hasNext()) {
            String palabra = iterador.next();
            sb.append("• ").append(palabra).append(" (")
                    .append(juego.obtenerPuntajePalabra(palabra)).append(" pts)\n");
        }

        areaPalabrasUsadas.setText(sb.toString());
        areaPalabrasUsadas.setCaretPosition(0);
    }

    private void pasarTurno() {
        juego.jugadorPaso();

        if (juego.juegoTerminado()) {
            mostrarResultadosFinales();
            return;
        }

        // Verificamos si hemos cambiado de ronda
        // comprobando si el contador de ronda ha aumentado
        if (Integer.parseInt(etiquetaRonda.getText().split(" ")[1]) != juego.getRondaActual()) {
            // Si la ronda cambió, actualizamos la interfaz y mostramos mensaje
            JOptionPane.showMessageDialog(frame,
                    "¡Todos los jugadores han pasado! Comienza la ronda " + juego.getRondaActual(),
                    "Nueva Ronda", JOptionPane.INFORMATION_MESSAGE);

            iniciarRonda(); // Actualizamos la interfaz para la nueva ronda
        } else {
            // Si seguimos en la misma ronda, solo actualizamos la interfaz para el siguiente jugador
            actualizarInterfazTurno();
            actualizarPuntuaciones();
            resetearPalabraSinNuevasLetras();

            JOptionPane.showMessageDialog(frame,
                    juego.getJugadorActual() + " es tu turno.",
                    "Turno cambiado", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void resetearPalabraSinNuevasLetras() {
        palabraActual = new StringBuilder();
        campoTexto.setText("");
        indicesUsados.clear();

        for (int i = 0; i < botonesLetras.size(); i++) {
            if (!indicesUsados.contains(i)) {
                botonesLetras.get(i).setEnabled(true);
            }
        }
    }

    private void actualizarInterfazTurno() {
        etiquetaTurnoActual.setText("Turno de: " + juego.getJugadorActual());
        etiquetaRonda.setText("RONDA " + juego.getRondaActual() + " de 3");
        actualizarPuntuaciones();
    }


    private void resetearPalabra() {
        // Resetear la palabra actual
        palabraActual = new StringBuilder();
        campoTexto.setText("");
        indicesUsados.clear();

        juego.generarLetras(juego.getDificultad());
        mostrarLetrasGeneradas();
    }


    private void actualizarPuntuaciones() {
        panelPuntuaciones.removeAll();
        etiquetasPuntuaciones.clear();

        JLabel titulo = new JLabel("Puntuaciones");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPuntuaciones.add(titulo);
        panelPuntuaciones.add(Box.createVerticalStrut(10));

        HashMap<String, Integer> puntuaciones = juego.getPuntuaciones();
        String texto = etiquetaTurnoActual.getText();
        String jugadorActual = texto.contains(":") ? texto.substring(texto.indexOf(":") + 2) : "";

        for (Map.Entry<String, Integer> entrada : puntuaciones.entrySet()) {
            JLabel etiqueta = new JLabel(entrada.getKey() + ": " + entrada.getValue() + " pts");
            etiqueta.setFont(new Font("Arial", entrada.getKey().equals(jugadorActual) ?
                    Font.BOLD : Font.PLAIN, 14));
            etiqueta.setForeground(entrada.getKey().equals(jugadorActual) ?
                    Color.BLUE : Color.BLACK);
            etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

            etiquetasPuntuaciones.add(etiqueta);
            panelPuntuaciones.add(etiqueta);
            panelPuntuaciones.add(Box.createVerticalStrut(5));
        }

        panelPuntuaciones.revalidate();
        panelPuntuaciones.repaint();
    }

    private void mostrarResultadosFinales() {
        String ganador = juego.determinarGanador();
        StringBuilder mensaje = new StringBuilder("Resultados Finales:\n\n");

        ArrayList<Map.Entry<String, Integer>> listaOrdenada = new ArrayList<>(juego.getPuntuaciones().entrySet());
        listaOrdenada.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        for (Map.Entry<String, Integer> entrada : listaOrdenada) {
            mensaje.append(entrada.getKey()).append(": ").append(entrada.getValue()).append(" puntos\n");
        }

        mensaje.append("\n");

        if (ganador.equals("EMPATE")) {
            int maxPuntuacion = Collections.max(juego.getPuntuaciones().values());
            mensaje.append("¡EMPATE! Varios jugadores tienen ").append(maxPuntuacion).append(" puntos.");
        } else if (ganador.equals("No hay jugadores") || ganador.equals("No hay ganador")) {
            mensaje.append(ganador);
        } else {
            int puntosGanador = juego.getPuntuaciones().get(ganador);
            mensaje.append("¡").append(ganador.toUpperCase()).append(" es el ganador con ")
                    .append(puntosGanador).append(" puntos!");
        }

        JOptionPane.showMessageDialog(frame, mensaje.toString(),
                "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);

        // Preguntar si quiere jugar de nuevo
        int opcion = JOptionPane.showConfirmDialog(frame,
                "¿Deseas jugar de nuevo?",
                "Jugar de nuevo", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            frame.dispose();
            new InterfazGrafica();
        } else {
            System.exit(0);
        }
    }
}