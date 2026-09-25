package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MOVUCVRegistroApp extends JFrame {

    private static final Color COLOR_FONDO = new Color(11, 11, 35);       // Azul muy oscuro (Fondo general y Cabecera)
    private static final Color COLOR_PANEL = new Color(19, 29, 61);       // Azul oscuro (Para ambas tarjetas)
    private static final Color COLOR_BOTONES = new Color(125, 182, 245);   // Azul claro (Botones y enlaces)
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);    // Blanco humo (Textos principales)
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150); // Gris (Textos secundarios, separadores)
    private static final Color COLOR_INPUT = new Color(250, 250, 250);    // Blanco (Fondo de casillas de texto)

    // --- SISTEMA DE FUENTES REDUCIDO ---
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 13);
//variables
private JTextField Nombre;
private JTextField Apellido;
private JTextField Correo;
private JTextField Telefono;
private JTextField Cedula;
private JPasswordField Contrasena;
private JPasswordField ConfirmarContra;
private JButton btnRegistrar;
private JCheckBox chkTerminos;
private JLabel lblError;


//configuración de la ventana
public MOVUCVRegistroApp() {

setTitle("MOVUCV - Registro de Usuario");
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

String ruta = "src/images/logo.png"; 
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

JLabel texto1 = new JLabel("Registro de Usuario");
texto1.setFont(FUENTE_TEXTO);
texto1.setForeground(COLOR_TEXTO);

izq.add(titulo); //agregar el titulo a la izquierda
izq.add(sep); // agregar el separador del titulo a la izquiersa
izq.add(texto1); // agregamos el texto de registro de usuario a la izquierda

// parte derecha 

JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 10));
dere.setOpaque(false);
dere.add(Textoscabezal("Inicio"));
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
p.setBorder(new EmptyBorder(20, 0, 20, 0));

JPanel cIzq = cuadroIzq(); // agregamos el cuadro izquierdo
p.add(cIzq);

JPanel cDere = cuadroDere(); // agregamos el cuadro derecho
p.add(cDere);

        return p;
    }



private JPanel cuadroIzq() { // CUADRO IZQ

JPanel p = new JPanel();
p.setBackground(COLOR_PANEL);
p.setPreferredSize(new Dimension(420, 580)); // Tamaño fijo
p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
p.setBorder(new EmptyBorder(50, 40, 50, 40));


JLabel texto1 = new JLabel("<html><body style='text-align:center;'>Bienvenido al<br>Sistema de<br>Transporte UCV</body></html>");

texto1.setFont(FUENTE_TITULO);
texto1.setForeground(COLOR_TEXTO);
texto1.setAlignmentX(Component.CENTER_ALIGNMENT);
texto1.setBorder(new EmptyBorder(0, 50, 0, 0)); 
p.add(texto1);
p.add(Box.createRigidArea(new Dimension(0, 30))); // Espacio

JLabel texto2 = new JLabel("<html><body style='text-align:center;'>Regístrate para acceder al monitoreo<br>en tiempo real de todas las rutas.</body></html>");

texto2.setFont(FUENTE_TEXTO);
texto2.setForeground(COLOR_TEXTO);
texto2.setAlignmentX(Component.CENTER_ALIGNMENT);
texto2.setBorder(new EmptyBorder(0, 50, 0, 0));
p.add(texto2);
p.add(Box.createRigidArea(new Dimension(0, 40)));

        // Pastillas de Información (Pills)
p.add(textosCuadroIzq("18", "Rutas Disponibles"));
p.add(Box.createRigidArea(new Dimension(0, 10)));
p.add(textosCuadroIzq("31", "Unidades Activas"));
p.add(Box.createRigidArea(new Dimension(0, 10)));
p.add(textosCuadroIzq( "24/7", "Monitoreo en Vivo"));

        return p;
    }


private JPanel textosCuadroIzq(String texto1, String texto2) { // cuadritos de informacion del cuadro izquierdo

JPanel cuadritos = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
cuadritos.setBackground(COLOR_TEXTO); // Fondo azul oscuro
cuadritos.setMaximumSize(new Dimension(300, 70)); // Ancho fijo
cuadritos.setAlignmentX(Component.CENTER_ALIGNMENT);

JPanel texto = new JPanel(new GridLayout(2, 1));
texto.setOpaque(false);
JLabel value = new JLabel(texto1);
value.setFont(FUENTE_TITULO);
value.setForeground(COLOR_FONDO);
JLabel label = new JLabel(texto2);
label.setFont(FUENTE_TEXTO);
label.setForeground(COLOR_SECUNDARIO);
texto.add(value);
texto.add(label);
cuadritos.add(texto);

        return cuadritos;
    }


private JPanel cuadroDere() {

JPanel p = new JPanel();
p.setBackground(COLOR_PANEL);
p.setPreferredSize(new Dimension(720, 580)); // Tamaño fijo
p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
p.setBorder(new EmptyBorder(15, 60, 50, 60));


JLabel texto1 = new JLabel("Crear Cuenta");
texto1.setFont(FUENTE_TITULO);
texto1.setForeground(COLOR_TEXTO);
texto1.setAlignmentX(Component.CENTER_ALIGNMENT);
p.add(texto1);
p.add(Box.createRigidArea(new Dimension(0, 10)));

JLabel texto2 = new JLabel("Completa el formulario para registrarte en el sistema");
texto2.setFont(FUENTE_TEXTO);
texto2.setForeground(COLOR_INPUT
);
texto2.setAlignmentX(Component.CENTER_ALIGNMENT);
p.add(texto2);
p.add(Box.createRigidArea(new Dimension(0, 40)));


JPanel casillas = new JPanel(new GridBagLayout());
casillas.setOpaque(false);
GridBagConstraints c = new GridBagConstraints();
c.fill = GridBagConstraints.HORIZONTAL;
c.weightx = 1.0;
c.insets = new Insets(0, 10, 15, 10); // Espacio entre campos


Nombre = new JTextField();

crearCasillas(casillas, c, 0, "Nombre:", Nombre);

Apellido = new JTextField();

crearCasillas(casillas, c, 1, "Apellido:", Apellido);

Correo = new JTextField();

crearCasillas(casillas, c, 2, "Correo Electrónico:", Correo);

Cedula = new JTextField();
crearCasillas(casillas, c, 3, "Cédula:", Cedula);

Contrasena = new JPasswordField();

crearCasillas(casillas, c, 4, "Contraseña:", Contrasena);
ConfirmarContra = new JPasswordField();

crearCasillas(casillas, c, 5, "Confirmar Contraseña:", ConfirmarContra);

p.add(casillas);


JPanel terminos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
terminos.setOpaque(false);
terminos.setBorder(new EmptyBorder(0, 0, 5, 0)); // Espacio antes del checkbox
chkTerminos = new JCheckBox();
chkTerminos.setOpaque(false);
JLabel texto3 = new JLabel("<html><body>Acepto los <span style='color:#7DB6F5;'>Terminos y Condiciones</span> y la <span style='color:#7DB6F5;'>Politica de Privacidad</span></body></html>");
texto3.setFont(FUENTE_TEXTO);
texto3.setForeground(COLOR_TEXTO);
terminos.add(chkTerminos);
terminos.add(texto3);
p.add(Box.createRigidArea(new Dimension(0, 20))); // espacio
p.add(terminos);

chkTerminos.addActionListener(e -> btnRegistrar.setEnabled(chkTerminos.isSelected()));

btnRegistrar = new JButton("Crear Cuenta");
btnRegistrar.setFont(FUENTE_SUBTITULO);
btnRegistrar.setBackground(COLOR_BOTONES);
btnRegistrar.setForeground(COLOR_TEXTO);
btnRegistrar.setFocusPainted(false);
btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
btnRegistrar.setMaximumSize(new Dimension(200, 45)); // Tamaño fijo
btnRegistrar.setEnabled(false);
btnRegistrar.setBorder(new EmptyBorder(5, 5, 5, 5));
p.add(btnRegistrar);

// Etiqueta de error en dessarolloooooooooooooooooooooooooooooo
lblError = new JLabel(" "); // Empieza vacía
lblError.setFont(FUENTE_TEXTO);
lblError.setForeground(Color.RED);
lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

p.add(lblError);

        return p;
    }

private void crearCasillas(JPanel casillas, GridBagConstraints c, int fila, String Texto, JTextField entrada) {

c.gridy = fila;
c.gridx = 0; // Columna 1: Label
c.gridwidth = 1;
JLabel lbl = new JLabel(Texto);
lbl.setFont(FUENTE_SUBTITULO);
lbl.setForeground(COLOR_TEXTO);
casillas.add(lbl, c);

c.gridx = 1; // Columna 2: Input
c.gridwidth = 1;
entrada.setFont(FUENTE_SUBTITULO);
entrada.setBackground(COLOR_INPUT);
entrada.setBorder(new EmptyBorder(10, 15, 10, 15));
        

casillas.add(entrada, c);

    }

private JPanel PieDePagina() {

JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
p.setBackground(COLOR_FONDO);
p.setBorder(new EmptyBorder(10, 0, 10, 0));
JLabel label = new JLabel("MOVUCV | Registro Seguro | Datos Protegidos");
label.setFont(FUENTE_TEXTO);
label.setForeground(COLOR_SECUNDARIO);
p.add(label);
return p;
    }


// getters para obtener los valores de los campos de texto y botones
public String getNombre() { return Nombre.getText(); }
public String getApellido() { return Apellido.getText(); }
public String getCorreo() { return Correo.getText(); }
public String getTelefono() { return Telefono.getText(); }
public String getCedula() { return Cedula.getText(); }
public String getPassword() { return new String(Contrasena.getPassword()); }
public String getConfirmarPassword() { return new String(ConfirmarContra.getPassword()); }
public JButton getBtnRegistrar() { return btnRegistrar; }

    
//MAIN
    public static void main(String[] args) {

            new MOVUCVRegistroApp().setVisible(true);

    }
}