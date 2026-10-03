package src.view;

import src.controller.HomeUsuarioController;
import src.controller.HomeAdminController;
import src.view.HomeUsuario;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src._helpers.ComponentUtils;
import src.controller.UsuarioController;

public class MOVUCVInicioApp extends JFrame {
    // colores
    private static final Color COLOR_FONDO = new Color(11, 11, 35); // Azul muy oscuro (Fondo general y Cabecera)
    private static final Color COLOR_PANEL = new Color(19, 29, 61); // Azul oscuro (Para ambas tarjetas)
    private static final Color COLOR_BOTONES = new Color(125, 182, 245); // Azul claro (Botones y enlaces)
    private static final Color COLOR_TEXTO = new Color(245, 245, 245); // Blanco humo (Textos principales)
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150); // Gris (Textos secundarios, separadores)
    private static final Color COLOR_INPUT = new Color(250, 250, 250); // Blanco (Fondo de casillas de texto)

    // fuentes
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 19);
    // variables

    private JTextField Correo;
    private JPasswordField Contrasena;
    private JButton btnRegistrar;
    private JLabel TextoError;

    // configuración de la ventana
    public MOVUCVInicioApp() {

        setTitle("MOVUCV - Inicio de Sesión");
        setSize(1366, 768); // Tamaño de la ventn
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO); // Fondo
        setLayout(new BorderLayout());

        // Agregar el cabezal y el pie de pagina y el cuerpo principal

        add(Cabezal(), BorderLayout.NORTH);
        add(Cuerpo(), BorderLayout.CENTER);
        add(PieDePagina(), BorderLayout.SOUTH);
    }

    // CABEZALLL

    private JPanel Cabezal() {

        JPanel p = new JPanel(new BorderLayout());

        p.setBackground(COLOR_PANEL); // color d el fondo
        p.setBorder(new EmptyBorder(15, 50, 15, 50)); // Márgenes

        // parte izquierda del cabezal
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        izq.setOpaque(false);

        JLabel titulo = new JLabel("MOVUCV");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);

        // CARFGA DEL LOGO

        String ruta = "res/images/logo sin fondo.png";
        java.io.File Logo = new java.io.File(ruta);

        if (Logo.exists()) {

            ImageIcon Imglogo = new ImageIcon(Logo.getAbsolutePath());
            Image LogoMejorado = Imglogo.getImage().getScaledInstance(100, 40, Image.SCALE_SMOOTH);
            titulo.setIcon(new ImageIcon(LogoMejorado));
            titulo.setIconTextGap(12);

        } else {

            System.err.println("Error: No se encontró el logo en " + ruta);
            titulo.setText("MOVUCV"); // Texto de respaldo si la imagen falla
        }

        JLabel sep = new JLabel("|");
        sep.setFont(FUENTE_TITULO);
        sep.setForeground(COLOR_SECUNDARIO);

        JLabel texto1 = new JLabel("Inicio de Sesión");
        texto1.setFont(FUENTE_TEXTO);
        texto1.setForeground(COLOR_TEXTO);

        izq.add(titulo); // agregar el titulo a la izquierda
        izq.add(sep); // agregar el separador del titulo a la izquiersa
        izq.add(texto1); // agregamos el texto de registro de usuario a la izquierda

        // parte derecha

        JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 10));
        dere.setOpaque(false);

        UsuarioController controladorLogin = new UsuarioController(); // instanciarlo para acceder a llamarRegistro

        dere.add(ComponentUtils.textoPresionable("Registro", COLOR_TEXTO, FUENTE_TEXTO, () -> {controladorLogin.llamarRegistro(this);}));
        dere.add(Textoscabezal("Rutas"));
        dere.add(Textoscabezal("Horarios"));
        dere.add(Textoscabezal("Contacto"));

        p.add(izq, BorderLayout.WEST);
        p.add(dere, BorderLayout.EAST);

        return p;
    }

    private JLabel Textoscabezal(String texto) {
        JLabel texto1 = new JLabel(texto);
        texto1.setFont(FUENTE_TEXTO);
        texto1.setForeground(COLOR_TEXTO);
        texto1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return texto1;
    }

    private JPanel Cuerpo() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 30));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(70, 0, 20, 0));

        JPanel cDere = cuadroDere(); // agregamos el cuadro derecho
        p.add(cDere);

        return p;
    }

    private JPanel cuadroDere() {

        JPanel p = new JPanel();
        p.setBackground(COLOR_PANEL);
        p.setPreferredSize(new Dimension(720, 480)); // Tamaño fijo
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(50, 60, 50, 60));

        JLabel texto1 = new JLabel("Iniciar Sesión");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(COLOR_TEXTO);
        texto1.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(texto1);
        p.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel texto2 = new JLabel("Ingresa tus datos para iniciar sesión");
        texto2.setFont(FUENTE_TEXTO);
        texto2.setForeground(COLOR_INPUT);
        texto2.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(texto2);
        p.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel casillas = new JPanel(new GridBagLayout());
        casillas.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 50, 15, 5); // Espacio entre campos

        Correo = new JTextField();

        crearCasillas(casillas, c, 2, " Correo:", Correo);

        Contrasena = new JPasswordField();

        crearCasillas(casillas, c, 4, "Contraseña:", Contrasena);

        p.add(casillas);

        JPanel terminos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        terminos.setOpaque(false);
        terminos.setBorder(new EmptyBorder(0, 0, 30, 0)); // Espacio antes del checkbox
        JLabel texto3 = new JLabel(
                "<html><body>Aceptas los <span style='color:#7DB6F5;'>Terminos y Condiciones</span> y la <span style='color:#7DB6F5;'>Politica de Privacidad</span></body></html>");
        texto3.setFont(FUENTE_TEXTO);
        texto3.setForeground(COLOR_TEXTO);
        terminos.add(texto3);
        p.add(Box.createRigidArea(new Dimension(0, 20))); // espacio
        p.add(terminos);

        btnRegistrar = new JButton("Iniciar Sesión");
        btnRegistrar.setFont(FUENTE_SUBTITULO);
        btnRegistrar.setBackground(COLOR_BOTONES);
        btnRegistrar.setForeground(COLOR_TEXTO);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.setMaximumSize(new Dimension(200, 45)); // Tamaño fijo
        btnRegistrar.setBorder(new EmptyBorder(5, 5, 5, 5));
        p.add(btnRegistrar);

        btnRegistrar.addActionListener(e -> {

            src.controller.UsuarioController controlador = new src.controller.UsuarioController();
            String resultadoString = controlador.iniciarSesion(
                    getCorreo(),
                    getContra());
            if (resultadoString.length() == 1) {
                inicioExitoso(resultadoString.charAt(0));

                resultadoString = resultadoString.toUpperCase();

                /*
                 * if (resultadoString.equals("C"))
                 * new HomeConductor(getCorreo()).setVisible(true);
                 */ // mouseherramienta para más tarde

                if (resultadoString.equals("E") || resultadoString.equals("T") || resultadoString.equals("P")) {
                    HomeUsuarioController homecontrol = new HomeUsuarioController();
                    homecontrol.iniciarHome(getCorreo());
                }

                else if (resultadoString.equals("A")) {
                    HomeAdminController homeadmincontrol = new HomeAdminController();
                    homeadmincontrol.iniciarHome(getCorreo());
                }

                else {
                    mostrarError("*Rol no reconocido en el sistema.");
                    TextoError.setForeground(Color.RED);
                }

                dispose();
            } else {
                mostrarError(resultadoString);
                TextoError.setForeground(Color.RED); // volvemos a ponerlo en rojo
            }

        });

        // Etiqueta de error
        TextoError = new JLabel(" "); // Empieza vacía
        TextoError.setFont(FUENTE_TEXTO);
        TextoError.setForeground(Color.RED);
        TextoError.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(TextoError);

        return p;
    }

    // metodo para crear las casillas de texto y sus identificadores
    private void crearCasillas(JPanel casillas, GridBagConstraints c, int fila, String Texto, JTextField entrada) {

        c.gridy = fila;
        c.gridx = 0; // Identificador
        c.gridwidth = 1;
        JLabel Identificador = new JLabel(Texto);
        Identificador.setFont(FUENTE_SUBTITULO);
        Identificador.setForeground(COLOR_TEXTO);
        casillas.add(Identificador, c);

        c.gridx = 1; // Entrada
        c.gridwidth = 1;
        entrada.setFont(FUENTE_SUBTITULO);
        entrada.setBackground(COLOR_INPUT);
        entrada.setBorder(new EmptyBorder(10, 15, 10, 15));
        // Agregar un FocusListener para limpiar el mensaje de error al enfocar el campo
        entrada.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {

                limpiarError(); // Limpiar el mensaje de error al enfocar el campo
            }

        });

        casillas.add(entrada, c);

    }

    // pie de pagina de la ventana
    private JPanel PieDePagina() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(COLOR_FONDO);
        p.setBorder(new EmptyBorder(1, 0, 10, 0));
        JLabel texto = new JLabel("MOVUCV | Inicio Seguro | Datos Protegidos");
        texto.setFont(FUENTE_TEXTO);
        texto.setForeground(COLOR_SECUNDARIO);
        p.add(texto);
        return p;
    }

    // getters para obtener los valores de los campos de texto y botones
    public String getCorreo() {
        return Correo.getText();
    }

    public String getContra() {
        return new String(Contrasena.getPassword());
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    // metodo para mostrar mensaje de error
    public void mostrarError(String mensaje) {

        TextoError.setText(mensaje);
    }

    // limpiar el mensaje de error
    public void limpiarError() {
        TextoError.setText(" ");
    }

    public void inicioExitoso(char c) {
        TextoError.setForeground(new Color(0, 128, 0)); // Verde
        TextoError.setText("Inicio de Sesión exitoso!");

    }

    // MAIN
    public static void main(String[] args) {

        new MOVUCVInicioApp().setVisible(true);

    }
}