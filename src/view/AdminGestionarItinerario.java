package src.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.controller.HomeAdminController;
import src.model.*;
import src._helpers.ComponentUtils;

public class AdminGestionarItinerario extends JFrame {

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
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);

    // variables
    private JComboBox<String> desplegableRuta;
    private JComboBox<String> desplegableConductor;
    private JComboBox<String> desplegableUnidad;
    private JTextField hora;
    private JButton btnProgramar;
    private JButton Limpiar;
    private JTable tablaUnidades;

    private javax.swing.table.DefaultTableModel modeloTabla; // tabla
    private JLabel TextoError;
    private HomeAdminController controlador;
    private Usuario adminLogeado;

    // configuración de la ventana
    public AdminGestionarItinerario(HomeAdminController controlador, Usuario adminLogeado) {
        this.controlador = controlador;
        this.adminLogeado = adminLogeado; // necesario para el menu

        setTitle("MOVUCV - Panel de Administrador - Gestionar Itinerarios");
        setSize(1366, 768); // Tamaño de la ventn
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO); // Fondo
        setLayout(new BorderLayout());

        // Agregar el cabezal y el pie de pagina y el cuerpo principal

        add(Cabezal(), BorderLayout.NORTH);
        add(ComponentUtils.menuIzqu(this,controlador), BorderLayout.WEST);
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

        JLabel texto1 = new JLabel("Panel de Administrador");
        texto1.setFont(FUENTE_TEXTO);
        texto1.setForeground(COLOR_TEXTO);

        izq.add(titulo); // agregar el titulo a la izquierda
        izq.add(sep); // agregar el separador del titulo a la izquiersa
        izq.add(texto1); // agregamos el texto de registro de usuario a la izquierda

// parte derecha 
    JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
    dere.setOpaque(false);
    dere.add(ComponentUtils.textoPresionable(adminLogeado.getNombre(), COLOR_TEXTO, FUENTE_TEXTO, null));
    dere.add(ComponentUtils.textoPresionable("|", COLOR_TEXTO, FUENTE_TEXTO, null)); 
    dere.add(ComponentUtils.textoPresionable("Cerrar Sesion", COLOR_TEXTO, FUENTE_TEXTO, () -> {controlador.cerrarSesion(this);})); 
 
    p.add(izq, BorderLayout.WEST);
    p.add(dere, BorderLayout.EAST);
 
    return p;

    }

    private JPanel Cuerpo() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(50, 0, 20, 0));

        p.add(cuadroIzq());
        p.add(cuadroDere());

        return p;
    }

    private JPanel cuadroIzq() {

        JPanel p = new JPanel();

        p.setBackground(COLOR_PANEL);
        p.setPreferredSize(new Dimension(550, 500));
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titulo = new JLabel("Rutas");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        p.add(titulo, BorderLayout.NORTH);

        // creamos el modelo de la tabla
        String[] columnas = { "Ruta", "Conductor", "Unidad", "Hora de Salida", "Estado" };
        modeloTabla = new javax.swing.table.DefaultTableModel(null, columnas) {
            // hacemos override para que no se puede editar la tgabla si le hacemos click
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // ponemos nuestro modelo de tabla en la tabla ocmo tal
        tablaUnidades = new JTable(modeloTabla);
        tablaUnidades.setFont(FUENTE_TEXTO);
        tablaUnidades.setRowHeight(30);
        tablaUnidades.getTableHeader().setFont(FUENTE_SUBTITULO);
        // ponemos la tabla dentro de un scroll para poder bajar por si hay muchas
        // unidades registradas
        JScrollPane scroll = new JScrollPane(tablaUnidades);
        p.add(scroll, BorderLayout.CENTER);

        return p;

    }

    private JPanel cuadroDere() {

        JPanel p = new JPanel();
        p.setBackground(COLOR_PANEL);
        p.setPreferredSize(new Dimension(430, 500)); // Tamaño fijo
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(30, 30, 30, 50));

        // titulo del form
        JLabel texto1 = new JLabel("Datos de la Ruta");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(COLOR_TEXTO);
        texto1.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(texto1);
        p.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel casillas = new JPanel(new GridBagLayout());
        casillas.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 15, 0);

        c.gridx = 0;
        c.gridy = 0;
        JLabel texto2 = new JLabel("Ruta:");
        texto2.setFont(FUENTE_SUBTITULO);
        texto2.setForeground(COLOR_TEXTO);
        casillas.add(texto2, c);

        c.gridx = 1;
        String[] rutas = { "Plaza Venezuela - UCV", "La Bandera - UCV", "Silencio - UCV" };
        desplegableRuta = new JComboBox<>(rutas);
        desplegableRuta.setFont(FUENTE_TEXTO);
        desplegableRuta.setBackground(COLOR_INPUT);
        casillas.add(desplegableRuta, c);

        c.gridx = 0;
        c.gridy = 1;
        JLabel texto3 = new JLabel("Conductor");
        texto3.setFont(FUENTE_SUBTITULO);
        texto3.setForeground(COLOR_TEXTO);
        casillas.add(texto3, c);

        c.gridx = 1;
        desplegableConductor = new JComboBox<>();
        desplegableConductor.setFont(FUENTE_TEXTO);
        desplegableConductor.setBackground(COLOR_INPUT);
        casillas.add(desplegableConductor, c);

        c.gridx = 0;
        c.gridy = 2;
        JLabel texto4 = new JLabel("Unidad:");
        texto4.setFont(FUENTE_SUBTITULO);
        texto4.setForeground(COLOR_TEXTO);
        casillas.add(texto4, c);

        c.gridx = 1;
        desplegableUnidad = new JComboBox<>();
        desplegableUnidad.setFont(FUENTE_TEXTO);
        desplegableUnidad.setBackground(COLOR_INPUT);
        casillas.add(desplegableUnidad, c);

        hora = new JTextField();
        crearCasillas(casillas, c, 3, "Hora salida (HH:MM):", hora);

        p.add(casillas);
        p.add(Box.createRigidArea(new Dimension(0, 30)));

        btnProgramar = new JButton("Programar");
        btnProgramar.setFont(FUENTE_SUBTITULO);
        btnProgramar.setBackground(COLOR_BOTONES);
        btnProgramar.setForeground(Color.WHITE);
        btnProgramar.setFocusPainted(false);
        btnProgramar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProgramar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnProgramar.setMaximumSize(new Dimension(200, 45));
        p.add(btnProgramar);
        p.add(Box.createRigidArea(new Dimension(0, 15)));

        // boton para limpiar el form
        Limpiar = new JButton("Limpiar");
        Limpiar.setFont(FUENTE_SUBTITULO);
        Limpiar.setBackground(Color.GRAY);
        Limpiar.setForeground(Color.WHITE);
        Limpiar.setFocusPainted(false);
        Limpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Limpiar.setAlignmentX(Component.CENTER_ALIGNMENT);
        Limpiar.setMaximumSize(new Dimension(100, 10));
        p.add(Limpiar);
        p.add(Box.createRigidArea(new Dimension(0, 15)));

        // Etiqueta de error
        TextoError = new JLabel(" "); // Empieza vacía
        TextoError.setFont(FUENTE_TEXTO);
        TextoError.setForeground(Color.RED);
        TextoError.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(TextoError);

        return p;
    }

    private void crearCasillas(JPanel casillas, GridBagConstraints c, int n, String texto, JTextField entrada) {

        c.gridx = 0;
        c.gridy = n;
        JLabel texto1 = new JLabel(texto);
        texto1.setFont(FUENTE_SUBTITULO);
        texto1.setForeground(COLOR_TEXTO);
        casillas.add(texto1, c);

        c.gridx = 1;
        c.gridwidth = 1;
        entrada.setFont(FUENTE_TEXTO);
        entrada.setBackground(COLOR_INPUT);
        entrada.setBorder(new EmptyBorder(8, 10, 8, 10));
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

    // metodo para mostrar mensaje de error
    public void mostrarError(String mensaje) {

        TextoError.setText(mensaje);
    }

    // limpiar el mensaje de error
    public void limpiarError() {
        TextoError.setText(" ");
    }

    // getters para obtener los valores de los campos de texto y botones

    public JButton getBtnProgramar() {
        return btnProgramar;
    }

    public JButton getBtnLimpiarButton() {
        return Limpiar;
    }

    public JTable getTablaUnidades() {
        return tablaUnidades;
    }

    public javax.swing.table.DefaultTableModel getTabla() {
        return modeloTabla;
    }

    public String getRuta() {
        return (String) desplegableRuta.getSelectedItem();
    }

    public String getConductor() {
        return (String) desplegableConductor.getSelectedItem();
    }

    public String getUnidad() {
        return (String) desplegableUnidad.getSelectedItem();
    }

    public String getHora() {
        return hora.getText();
    }

    // setters
    public void setRuta(String ruta) {
        desplegableRuta.setSelectedItem(ruta);
    }

    public void setConductor(String conductor) {
        desplegableConductor.setSelectedItem(conductor);
    }

    public void setUnidad(String unidad) {
        desplegableUnidad.setSelectedItem(unidad);
    }

    public void setHora(String textoHora) {
        hora.setText(textoHora);
    }

    public void cargarConductores(String[] conductores) {
        desplegableConductor.removeAllItems();
        for (String c : conductores) {
            desplegableConductor.addItem(c);
        }
    }

    public void cargarUnidadesEnCombo(String[] unidades) {

        desplegableUnidad.removeAllItems();
        for (String c : unidades) {
            desplegableUnidad.addItem(c);
        }
    }

    // metodo para limpiar el form despues de que se edite algo correctamente o se
    // registre una unidad correctamente
    public void limpiarFormulario() {
        if (desplegableRuta.getItemCount() > 0)
            desplegableRuta.setSelectedIndex(0);
        if (desplegableConductor.getItemCount() > 0)
            desplegableConductor.setSelectedIndex(0);
        if (desplegableUnidad.getItemCount() > 0)
            desplegableUnidad.setSelectedIndex(0);

        hora.setText("");
        limpiarError();
    }

    // cambio de boton dependiendo si se selecciona una unidad en lkla tabla para
    // editar
    public void cambiarModoBoton(boolean actualizar) {
        if (actualizar) {
            btnProgramar.setText("Guardar Cambios");
        } else {
            btnProgramar.setText("Programar");
        }
    }

    // metodo para cargar la tabla desde el controlador
    public void cargarDatosEnTabla(Object[][] datos) {
        modeloTabla.setRowCount(0); // Limpia datos viejos
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    // MAIN
    /*
     * public static void main(String[] args) {
     * 
     * AdminGestionarItinerario vista = new AdminGestionarItinerario();
     * 
     * new ItinerarioController(vista);
     * 
     * vista.setVisible(true);
     * }
     */
}
