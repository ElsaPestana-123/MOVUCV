package src.view;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.model.Usuario;
import src.controller.HomeUsuarioController;
import src._helpers.ComponentUtils;
import src._helpers.ComponentUtils.Tarjeta;

public class HomeUsuario extends JFrame {
    private Usuario usuarioLogeado;
    private HomeUsuarioController controlador;

    // colores
    private static final Color COLOR_FONDO = new Color(11, 11, 35); // Azul muy oscuro (Fondo general y Cabecera)
    private static final Color COLOR_PANEL = new Color(19, 29, 61); // Azul oscuro (Para ambas tarjetas)
    private static final Color COLOR_BOTONES = new Color(125, 182, 245); // Azul claro (Botones y enlaces)
    private static final Color COLOR_TEXTO = new Color(245, 245, 245); // Blanco humo (Textos principales)
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150); // Gris (Textos secundarios, separadores)
    private static final Color COLOR_VERDE = new Color(30, 150, 60);
    private static final Color COLOR_NARANJA = new Color(235, 140, 0);
    private static final Color COLOR_ROJO = new Color(220, 30, 30);

    // fuentes
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_BIENVENIDA = new Font("SansSerif", Font.BOLD, 32);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);
    private static final Font FUENTE_TARJETA_TITULO = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FUENTE_PEQUENA = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FUENTE_PEQUENA_NEGRITA = new Font("SansSerif", Font.BOLD, 13);

    public HomeUsuario(Usuario usuarioLogeado, HomeUsuarioController controlador) {
        this.usuarioLogeado = usuarioLogeado;
        this.controlador = controlador;

        setTitle("MOVUCV - Inicio");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        add(Cabezal(), BorderLayout.NORTH);
        add(Cuerpo(), BorderLayout.CENTER);
        add(PieDePagina(), BorderLayout.SOUTH);

    }

    private static class Icono extends JComponent {

        private Image imagen;

        Icono(String archivo) {
            setPreferredSize(new Dimension(42, 42));
            File f = new File("res/images/" + archivo);

            if (f.exists()) { // como aún no es funcional solo se va a ver un puntico blanco
                ImageIcon original = new ImageIcon(f.getAbsolutePath());
                imagen = original.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            }

        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(COLOR_PANEL);
            g2.fillOval(0, 0, getWidth(), getHeight()); // circulito azul de fondo

            int centroX = getWidth() / 2;
            int centroY = getHeight() / 2;

            if (imagen != null)
                g2.drawImage(imagen, centroX - 12, centroY - 12, null); // ubicamos la imagen en todo el centro

            else {
                g2.setColor(Color.WHITE);
                g2.fillOval(centroX - 7, centroY - 7, 14, 14); // puntico blanco en caso de falla de archivo
            }

            g2.dispose();
        }
    }

    private static class BotonRedondeado extends JButton { // boton redondito de acceder

        BotonRedondeado(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setRolloverEnabled(true);

            setFont(FUENTE_PEQUENA_NEGRITA);
            setForeground(COLOR_FONDO);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(5, 20, 5, 20));

        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? COLOR_BOTONES.brighter() : COLOR_BOTONES);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private String darNombre() {
        if (usuarioLogeado != null)
            return usuarioLogeado.getNombre();

        return "Usuario";
    }

    private JPanel Cabezal() {
        JPanel p = new JPanel(new BorderLayout());

        p.setBackground(COLOR_PANEL);
        p.setBorder(new EmptyBorder(15, 20, 15, 20));

        // parte izquierda logo, MOVUCV, separador y tipo de panel
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izq.setOpaque(false);

        JLabel titulo = new JLabel("MOVUCV");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);

        // carga de logito
        String ruta = "res/images/logo sin fondo.png";
        File archivoLogo = new File(ruta);

        if (archivoLogo.exists()) {
            ImageIcon Imglogo = new ImageIcon(archivoLogo.getAbsolutePath());
            Image LogoMejorado = Imglogo.getImage().getScaledInstance(100, 40, Image.SCALE_SMOOTH);
            titulo.setIcon(new ImageIcon(LogoMejorado));
            titulo.setIconTextGap(12);

        } else
            System.err.println("Error: No se encontró el logo en " + ruta);

        JLabel sep = new JLabel("|");
        sep.setFont(FUENTE_TITULO);
        sep.setForeground(COLOR_SECUNDARIO);

        JLabel texto1 = new JLabel(" Inicio");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(COLOR_TEXTO);

        izq.add(titulo); // agregar el titulo a la izquierda
        izq.add(sep); // agregar el separador
        izq.add(texto1); // agregamos el texto de inicio

        // parte derecha
        JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        dere.setOpaque(false);
        dere.add(ComponentUtils.textoPresionable(darNombre(), COLOR_TEXTO, FUENTE_TEXTO, null));
        dere.add(ComponentUtils.textoPresionable("|", COLOR_TEXTO, FUENTE_TEXTO, null));
        dere.add(ComponentUtils.textoPresionable("Mi Cuenta", COLOR_TEXTO, FUENTE_TEXTO, () -> {
        })); // le pasamos una función lamda a fin de que sea presionable pero no rediriga
        dere.add(ComponentUtils.textoPresionable("|", COLOR_TEXTO, FUENTE_TEXTO, null));
        dere.add(ComponentUtils.textoPresionable("Cerrar Sesion", COLOR_TEXTO, FUENTE_TEXTO, () -> {
            controlador.cerrarSesion(this);
        }));

        p.add(izq, BorderLayout.WEST);
        p.add(dere, BorderLayout.EAST);

        return p;
    }

    private JPanel Cuerpo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(25, 50, 20, 50));

        // bienvenido y nombre
        JPanel arriba = new JPanel();
        arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));
        arriba.setOpaque(false);

        JLabel bienvenida = new JLabel("Bienvenido, " + darNombre());
        bienvenida.setFont(FUENTE_BIENVENIDA);
        bienvenida.setForeground(COLOR_TEXTO);
        bienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        arriba.add(bienvenida);
        arriba.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel subtitulo = new JLabel(
                "Selecciona una opción para comenzar. Tienes acceso a todas las funcionalidades del sistema.");
        subtitulo.setFont(FUENTE_PEQUENA);
        subtitulo.setForeground(COLOR_TEXTO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        arriba.add(subtitulo);

        p.add(arriba, BorderLayout.NORTH);
        p.add(cuadricularTarjetas(), BorderLayout.CENTER);
        return p;
    }

    // cuadrados de las 6 tarjetas
    private JPanel cuadricularTarjetas() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 30, 30));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(35, 0, 0, 0));

        // en cada tarjeta pasamos como acción lamda para que sea clickleable sin que
        // haga nada

        grid.add(crearTarjeta("rutas.png", "Consultar Rutas",
                "Explora todas las rutas urbanas y extraurbanas disponibles. Consulta horarios, paradas y disponibilidad en tiempo real.",
                "", COLOR_FONDO, COLOR_BOTONES, () -> {
                }));

        grid.add(crearTarjeta("reservar.png", "Reservar Puesto",
                "Selecciona tu ruta y reserva un asiento en la unidad de tu preferencia.",
                "", COLOR_VERDE, COLOR_VERDE, () -> {
                }));

        grid.add(crearTarjeta("reservas.png", "Mis Reservas",
                "Revisa el historial de tus reservas, cancela o modifica tus viajes programados.",
                "", COLOR_NARANJA, COLOR_NARANJA, () -> {
                }));

        grid.add(crearTarjeta("horarios.png", "Horarios", "Consulta los horarios de salida de cada ruta.",
                "", COLOR_FONDO, COLOR_BOTONES, () -> {
                }));

        grid.add(crearTarjeta("notificaciones.png", "Notificaciones",
                "Mantente informado sobre cambios de ruta, suspensiones, retrasos y novedades del sistema.",
                "", COLOR_ROJO, COLOR_ROJO, () -> {
                }));

        grid.add(crearTarjeta("perfil.png", "Mi Perfil", "Actualiza tus datos personales, preferencias.", "",
                COLOR_FONDO, COLOR_TEXTO, () -> {
                }));

        return grid;
    }

    // crear una tarjeta
    private JPanel crearTarjeta(String icono, String titulo, String descripcion, String infoBlanco, Color colorInfo,
            Color colorBorde, Runnable accion) {

        Tarjeta tarjeta = new Tarjeta(colorBorde);
        tarjeta.setLayout(new BorderLayout(0, 8));
        tarjeta.setBorder(new EmptyBorder(20, 20, 18, 20));

        // parte de arriba del circulitoo y botón acceder
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izq.setOpaque(false);
        izq.add(new Icono(icono)); // llamada a icono circular que recibe el nombre del png a buscar

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_TARJETA_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO);
        izq.add(lblTitulo);

        BotonRedondeado botonAcc = new BotonRedondeado("Acceder");
        botonAcc.addActionListener(e -> {
        }); // función lamda again

        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        der.setOpaque(false);
        der.add(botonAcc);

        encabezado.add(izq, BorderLayout.WEST);
        encabezado.add(der, BorderLayout.EAST);

        // descripción de abajito
        JTextArea desc = new JTextArea(descripcion);
        desc.setEditable(false);
        desc.setFocusable(false);
        desc.setOpaque(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setFont(FUENTE_PEQUENA);
        desc.setForeground(COLOR_TEXTO);
        desc.setBorder(new EmptyBorder(10, 0, 10, 0));

        // cuadrito inferior
        JLabel lblInfo = new JLabel(infoBlanco, SwingConstants.CENTER);
        lblInfo.setOpaque(true);
        lblInfo.setBackground(Color.WHITE);
        lblInfo.setForeground(colorInfo);
        lblInfo.setFont(FUENTE_PEQUENA_NEGRITA);
        lblInfo.setBorder(new EmptyBorder(7, 0, 7, 0));

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(desc, BorderLayout.CENTER);
        tarjeta.add(lblInfo, BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel PieDePagina() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(COLOR_PANEL);
        p.setBorder(new EmptyBorder(6, 0, 6, 0));
        JLabel texto = new JLabel("MOVUCV | Inicio | Datos Protegidos"); // pie de pagina por default de cada vetana

        texto.setFont(FUENTE_TEXTO);
        texto.setForeground(COLOR_TEXTO);
        p.add(texto);

        return p;
    }

}
