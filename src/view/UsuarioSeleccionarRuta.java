package src.view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import src._helpers.ComponentUtils;
import src._helpers.ComponentUtils.Tarjeta;
import src.controller.HomeUsuarioController;
import src.controller.ReservaController;
import src.model.Itinerario;
import src.model.Unidad;
import src.model.UnidadDAO;
import src.model.Usuario;

public class UsuarioSeleccionarRuta extends JFrame { // copia y pega de colores homeusuario y monedero
    private static final Color FONDO = new Color(11, 11, 35);
    private static final Color PANEL = new Color(19, 29, 61);
    private static final Color AZUL = new Color(125, 182, 245);
    private static final Color TEXTO = new Color(245, 245, 245);
    private static final Color TEXTO_SUAVE = new Color(170, 178, 195);
    private static final Color SECUNDARIO = new Color(150, 150, 150);
    private static final Color VERDE = new Color(30, 150, 60);
    private static final Color NARANJA = new Color(220, 140, 35);
    private static final Color ROJO = new Color(190, 55, 55);
    private static final Color ROJO_BOTON = new Color(220, 30, 30);

    // caebzal
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);

    private final Usuario usuario;
    private final List<DisponibilidadItinerario> disponibilidades = new ArrayList<>();
    private final ReservaController reservaController;
    private final JFrame home; // referencia al home
    private final HomeUsuarioController controlador;
    private final UnidadDAO unidadDAO = new UnidadDAO();
    private final JPanel tarjetas = new JPanel(new GridLayout(0, 1, 0, 16));
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelVistas = new JPanel(cardLayout);
    private UsuarioReservarCupo vistaReserva;
    private final JLabel lblMensaje = new JLabel(" ", SwingConstants.CENTER);

    public UsuarioSeleccionarRuta(Usuario usuario, List<Itinerario> itinerarios, ReservaController reservaController,
            JFrame home) {
        this(usuario, itinerarios, reservaController, home, null);
    }

    public UsuarioSeleccionarRuta(Usuario usuario, List<Itinerario> itinerarios, ReservaController reservaController,
            JFrame home, HomeUsuarioController controlador) {
        this.usuario = usuario;
        this.reservaController = reservaController;
        this.home = home;
        this.controlador = controlador;
         ComponentUtils.aplicarIcono(this);

        if (itinerarios != null) {

            for (Itinerario itinerario : itinerarios)
                disponibilidades.add(new DisponibilidadItinerario(itinerario));
        }

        setTitle("MOVUCV - Reservar Puesto");
        setSize(1366, 768);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                volverAlInicio();
            }
        });

        cargarOcupacion();
        construirPantalla();
    }

    private void construirPantalla() { // cabecera titulo rutas y pie
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout());
        add(crearCabecera(), BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(0, 18));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(26, 42, 22, 42));

        JPanel introduccion = new JPanel();
        introduccion.setOpaque(false);
        introduccion.setLayout(new BoxLayout(introduccion, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Selecciona tu ruta");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(TEXTO);
        JLabel subtitulo = new JLabel("Elige un itinerario para consultar sus asientos disponibles.");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(TEXTO_SUAVE);
        introduccion.add(titulo);
        introduccion.add(Box.createVerticalStrut(5));
        introduccion.add(subtitulo);

        tarjetas.setOpaque(false);
        JPanel lista = new JPanel(new BorderLayout());
        lista.setOpaque(false);
        lista.add(tarjetas, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        contenido.add(introduccion, BorderLayout.NORTH);
        contenido.add(scroll, BorderLayout.CENTER);

        lblMensaje.setForeground(new Color(255, 30, 30));
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 18));
        contenido.add(lblMensaje, BorderLayout.SOUTH);

        panelVistas.setOpaque(false);
        panelVistas.add(contenido, "rutas");
        add(panelVistas, BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);

        actualizarTarjetas();
    }

    // cabezal
    private JPanel crearCabecera() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL);
        p.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izq.setOpaque(false);

        JLabel titulo = new JLabel("MOVUCV");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(TEXTO);

        String ruta = "res/images/logo sin fondo.png";
        File archivoLogo = new File(ruta);
        if (archivoLogo.exists()) {
            ImageIcon imgLogo = new ImageIcon(archivoLogo.getAbsolutePath());
            Image logoMejorado = imgLogo.getImage().getScaledInstance(100, 40, Image.SCALE_SMOOTH);
            titulo.setIcon(new ImageIcon(logoMejorado));
            titulo.setIconTextGap(12);
        } else {
            System.err.println("Error: No se encontró el logo en " + ruta);
        }

        JLabel sep = new JLabel("|");
        sep.setFont(FUENTE_TITULO);
        sep.setForeground(SECUNDARIO);

        JLabel texto1 = new JLabel("Panel Pasajero");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(TEXTO);

        izq.add(titulo);
        izq.add(sep);
        izq.add(texto1);

        JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        dere.setOpaque(false);

        String nombre = "Usuario";
        if (usuario != null)
            nombre = usuario.getNombre();

        dere.add(ComponentUtils.textoPresionable(nombre, TEXTO, FUENTE_TEXTO, null));
        dere.add(ComponentUtils.textoPresionable("|", TEXTO, FUENTE_TEXTO, null));

        HomeUsuario.BotonRedondeado regresarHome = new HomeUsuario.BotonRedondeado("Regresar al Home", AZUL);
        regresarHome.addActionListener(e -> volverAlInicio());
        dere.add(regresarHome);

        HomeUsuario.BotonRedondeado botonCerrar = new HomeUsuario.BotonRedondeado("Cerrar Sesión", ROJO_BOTON);
        botonCerrar.addActionListener(e -> cerrarSesion());
        dere.add(botonCerrar);

        p.add(izq, BorderLayout.WEST);
        p.add(dere, BorderLayout.EAST);
        return p;
    }

    private JPanel crearPie() { // pie de pagina
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(PANEL);
        p.setBorder(new EmptyBorder(6, 0, 6, 0));

        JLabel texto = new JLabel("MOVUCV | Inicio | Datos Protegidos");
        texto.setFont(FUENTE_TEXTO);
        texto.setForeground(TEXTO);
        p.add(texto);

        return p;
    }

    private void cargarOcupacion() {//si falla, no reserva
        if (reservaController == null) { 
            for (DisponibilidadItinerario disponibilidad : disponibilidades) {
                disponibilidad.errorLectura = true;
            }
            return;
        }

        for (DisponibilidadItinerario disponibilidad : disponibilidades) {
            try {
                List<Integer> ocupados = reservaController.obtenerAsientosOcupados(
                        disponibilidad.itinerario.getId());

                disponibilidad.asientosOcupados = ocupados == null ? new ArrayList<>() : ocupados;
                disponibilidad.errorLectura = false;
            } catch (IOException | RuntimeException error) {
                disponibilidad.errorLectura = true;
            }
        }
    }

    public void refrescar() {
        cargarOcupacion();
        actualizarTarjetas();
    }

    private void actualizarTarjetas() { // tarjeta segun la ruta del itinerario disponible
        tarjetas.removeAll();

        if (disponibilidades.isEmpty()) {
            JLabel vacio = new JLabel("No hay itinerarios para mostrar.");
            vacio.setFont(new Font("SansSerif", Font.PLAIN, 16));
            vacio.setForeground(TEXTO_SUAVE);
            vacio.setBorder(new EmptyBorder(22, 4, 22, 4));
            tarjetas.add(vacio);

        } else {
            for (DisponibilidadItinerario disponibilidad : disponibilidades)
                tarjetas.add(crearTarjeta(disponibilidad));
        }

        tarjetas.revalidate();
        tarjetas.repaint();
    }

    private JPanel crearTarjeta(DisponibilidadItinerario disponibilidad) { // fabrica de tarjetas con info de
                                                                           // itinerarios
        Itinerario itinerario = disponibilidad.itinerario;
        Unidad unidad = unidadDAO.buscarUnidad(itinerario.getPlaca());

        int capacidad = 0;
        if (unidad != null)
            capacidad = unidad.getCapacidad();

        List<Integer> ocupados = disponibilidad.asientosOcupados;
        int libres = Math.max(0, capacidad - ocupados.size());
        boolean falloLectura = disponibilidad.errorLectura;
        boolean programado = "Programado".equalsIgnoreCase(itinerario.getEstado());
        boolean unidadOperativa = unidad != null && "Operativo".equalsIgnoreCase(unidad.getDisponible());
        boolean disponible = programado && unidadOperativa && libres > 0 && !falloLectura;

        Color colorEstado;
        if (falloLectura || !unidadOperativa)
            colorEstado = ROJO;
        else if (!programado)
            colorEstado = TEXTO_SUAVE;
        else if (libres == 0)
            colorEstado = NARANJA;
        else
            colorEstado = VERDE;

        Tarjeta tarjeta = new Tarjeta(colorEstado);
        tarjeta.setLayout(new BorderLayout(20, 0));
        tarjeta.setBorder(new EmptyBorder(16, 22, 16, 22));
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel icono = new JPanel(new GridBagLayout());
        icono.setOpaque(false);
        icono.add(new IconoCircular("reservar.png"));

        JPanel informacion = new JPanel();
        informacion.setOpaque(false);
        informacion.setLayout(new BoxLayout(informacion, BoxLayout.Y_AXIS));
        JLabel ruta = new JLabel(itinerario.getRuta());
        ruta.setFont(new Font("SansSerif", Font.BOLD, 19));
        ruta.setForeground(TEXTO);
        informacion.add(ruta);
        informacion.add(Box.createVerticalStrut(9));

        JPanel datos = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datos.setOpaque(false);
        datos.add(dato("Salida", itinerario.getHoraSalida(), 24));
        datos.add(dato("Unidad", itinerario.getPlaca(), 24));
        datos.add(dato("Conductor", itinerario.getConductor(), 0));
        informacion.add(datos);

        JPanel estado = new JPanel();
        estado.setOpaque(false);
        estado.setLayout(new BoxLayout(estado, BoxLayout.Y_AXIS));

        String textoDisponibilidad;
        if (falloLectura)
            textoDisponibilidad = "Disponibilidad no cargada";
        else
            textoDisponibilidad = libres + " de " + capacidad + " asientos libres";

        JLabel textoCupos = new JLabel(textoDisponibilidad);
        textoCupos.setFont(new Font("SansSerif", Font.BOLD, 14));
        textoCupos.setForeground(TEXTO);
        textoCupos.setAlignmentX(Component.LEFT_ALIGNMENT);

        double fraccionOcupada = 0;
        if (capacidad > 0)
            fraccionOcupada = (double) ocupados.size() / capacidad;

        BarraOcupacion barra = new BarraOcupacion(fraccionOcupada, colorEstado);
        barra.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel insignia = new JLabel(textoEstado(itinerario, unidad, libres, falloLectura), SwingConstants.CENTER);
        insignia.setOpaque(true);
        insignia.setBackground(colorEstado);
        insignia.setForeground(Color.WHITE);
        insignia.setFont(new Font("SansSerif", Font.BOLD, 11));
        insignia.setBorder(new EmptyBorder(5, 10, 5, 10));
        insignia.setAlignmentX(Component.LEFT_ALIGNMENT);
        estado.add(textoCupos);
        estado.add(Box.createVerticalStrut(7));
        estado.add(barra);
        estado.add(Box.createVerticalStrut(8));
        estado.add(insignia);

        tarjeta.add(icono, BorderLayout.WEST);
        tarjeta.add(informacion, BorderLayout.CENTER);
        tarjeta.add(estado, BorderLayout.EAST);
        tarjeta.setPreferredSize(new Dimension(900, 118));

        MouseAdapter accion = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (disponible) {
                    mostrarReserva(itinerario);
                } else {
                    String mensaje;
                    if (falloLectura)
                        mensaje = "No se pudo consultar la disponibilidad de los asientos.";
                    else
                        mensaje = "Este itinerario no está disponible para reservar.";

                    mostrarMensaje(mensaje);
                }
            }
        };
        agregarAccion(tarjeta, accion);
        return tarjeta;
    }

    private JPanel dato(String etiqueta, String valor, int espacioDerecho) {
        JPanel dato = new JPanel();
        dato.setOpaque(false);
        dato.setLayout(new BoxLayout(dato, BoxLayout.Y_AXIS));
        dato.setBorder(new EmptyBorder(0, 0, 0, espacioDerecho));
        JLabel titulo = new JLabel(etiqueta);
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titulo.setForeground(TEXTO_SUAVE);
        JLabel texto = new JLabel(valor);
        texto.setFont(new Font("SansSerif", Font.BOLD, 14));
        texto.setForeground(TEXTO);
        dato.add(titulo);
        dato.add(texto);
        return dato;
    }

    private String textoEstado(Itinerario itinerario, Unidad unidad, int libres, boolean falloLectura) {
        if (falloLectura)
            return "ERROR DE LECTURA";

        if (unidad == null || !"Operativo".equalsIgnoreCase(unidad.getDisponible()))
            return "UNIDAD NO DISPONIBLE";

        if (!"Programado".equalsIgnoreCase(itinerario.getEstado()))
            return itinerario.getEstado().toUpperCase(Locale.ROOT);

        if (libres == 0)
            return "LLENO";
        else
            return "PROGRAMADO";
    }

    private void agregarAccion(JComponent componente, MouseAdapter accion) {
        componente.addMouseListener(accion);
        for (Component hijo : componente.getComponents()) {
            if (hijo instanceof JComponent)
                agregarAccion((JComponent) hijo, accion);
        }
    }
 
    private void mostrarAsientos(Itinerario itinerario, int capacidad, List<Integer> ocupados) {
        JDialog dialogo = new JDialog(this, "Seleccionar asiento", true);
        dialogo.setLayout(new BorderLayout(12, 12));
        dialogo.getContentPane().setBackground(FONDO);
 
        JLabel titulo = new JLabel(itinerario.getRuta() + "  |  " + itinerario.getHoraSalida());
        titulo.setFont(new Font("SansSerif", Font.BOLD, 17));
        titulo.setForeground(TEXTO);
        titulo.setBorder(new EmptyBorder(16, 18, 0, 18));
 
        JPanel asientos = new JPanel(new GridLayout(0, 5, 9, 9));
        asientos.setBackground(FONDO);
        asientos.setBorder(new EmptyBorder(12, 18, 12, 18));
        for (int numero = 1; numero <= capacidad; numero++) {
            int asiento = numero;
            boolean ocupado = ocupados.contains(asiento);
 
            Color colorBoton = VERDE;
            String tooltip = "Reservar asiento " + asiento;
            if (ocupado) {
                colorBoton = TEXTO_SUAVE;
                tooltip = "Asiento ocupado";
            }
 
            JButton boton = new HomeUsuario.BotonRedondeado(String.valueOf(asiento), colorBoton);
            boton.setPreferredSize(new Dimension(58, 42));
            boton.setEnabled(!ocupado);
            boton.setToolTipText(tooltip);
            boton.addActionListener(event -> {
                dialogo.dispose();
                confirmarReserva(itinerario, asiento);
            });
            asientos.add(boton);
        }
 
        JScrollPane scroll = new JScrollPane(asientos);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(FONDO);
        scroll.setPreferredSize(new Dimension(390, Math.min(340, 90 + ((capacidad + 4) / 5) * 54)));
        JLabel leyenda = new JLabel("Verde: disponible     Gris: ocupado");
        leyenda.setForeground(TEXTO_SUAVE);
        leyenda.setBorder(new EmptyBorder(0, 18, 14, 18));
 
        dialogo.add(titulo, BorderLayout.NORTH);
        dialogo.add(scroll, BorderLayout.CENTER);
        dialogo.add(leyenda, BorderLayout.SOUTH);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
 
    private void confirmarReserva(Itinerario itinerario, int numeroAsiento) {
        String respuesta = reservaController.procesarReserva(usuario, itinerario, numeroAsiento);
        boolean confirmada = !respuesta.startsWith("*");
 
        String mensaje = respuesta.substring(1);
        String tituloMensaje = "No se pudo reservar";
        int tipoMensaje = JOptionPane.WARNING_MESSAGE;
        if (confirmada) {
            mensaje = respuesta;
            tituloMensaje = "Reserva confirmada";
            tipoMensaje = JOptionPane.INFORMATION_MESSAGE;
        }
 
        JOptionPane.showMessageDialog(this, mensaje, tituloMensaje, tipoMensaje);
        refrescar();
    }
 
    private void volverAlInicio() {
        dispose();
        if (home != null)
            home.setVisible(true);
    }

    private void cerrarSesion() {
        if (controlador != null)
            controlador.cerrarSesion(this);
        else
            dispose();
        if (!isDisplayable() && home != null) // cerramo home si la sesión sí se cerró
            home.dispose();
    }

    private static class DisponibilidadItinerario {
        private final Itinerario itinerario;
        private List<Integer> asientosOcupados = new ArrayList<>();
        private boolean errorLectura;

        DisponibilidadItinerario(Itinerario itinerario) {
            this.itinerario = itinerario;
        }
    }

    private static class IconoCircular extends JComponent {
        private Image imagen;

        IconoCircular(String archivo) {
            setPreferredSize(new Dimension(48, 48));
            File imagenArchivo = new File("res/images/" + archivo);
            if (imagenArchivo.exists()) {
                imagen = new ImageIcon(imagenArchivo.getAbsolutePath()).getImage()
                        .getScaledInstance(26, 26, Image.SCALE_SMOOTH);
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(PANEL);
            g.fillOval(0, 0, getWidth(), getHeight());
            int centroX = getWidth() / 2;
            int centroY = getHeight() / 2;
            if (imagen != null)
                g.drawImage(imagen, centroX - 13, centroY - 13, null);
            else {
                g.setColor(Color.WHITE);
                g.fillOval(centroX - 7, centroY - 7, 14, 14);
            }
            g.dispose();
        }
    }

    private static class BarraOcupacion extends JComponent {
        private final double fraccion;
        private final Color color;

        BarraOcupacion(double fraccion, Color color) {
            this.fraccion = Math.max(0, Math.min(1, fraccion));
            this.color = color;
            setPreferredSize(new Dimension(220, 8));
            setMaximumSize(new Dimension(220, 8));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(45, 60, 105));
            g.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g.setColor(color);
            g.fillRoundRect(0, 0, (int) (getWidth() * fraccion), getHeight(), getHeight(), getHeight());
            g.dispose();
        }
    }

    private void mostrarMensaje(String mensaje) { //para mostrar en pantalla
        lblMensaje.setText(mensaje);
    }

    private void mostrarReserva(Itinerario itinerario) {
        lblMensaje.setText(" ");

        if (vistaReserva != null)
            panelVistas.remove(vistaReserva);

        vistaReserva = new UsuarioReservarCupo(usuario, itinerario, reservaController,
                home,
                controlador,
                () -> {
                    refrescar();
                    cardLayout.show(panelVistas, "rutas");
                });

        panelVistas.add(vistaReserva, "reserva");
        cardLayout.show(panelVistas, "reserva");
        panelVistas.revalidate();
        panelVistas.repaint();
    }
}
