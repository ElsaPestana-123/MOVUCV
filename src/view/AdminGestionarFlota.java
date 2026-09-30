package src.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src._helpers.Validaciones;

public class AdminGestionarFlota extends JFrame {

    //colores
private static final Color COLOR_FONDO = new Color(11, 11, 35);       // Azul muy oscuro (Fondo general y Cabecera)
private static final Color COLOR_PANEL = new Color(19, 29, 61);       // Azul oscuro (Para ambas tarjetas)
private static final Color COLOR_BOTONES = new Color(125, 182, 245);   // Azul claro (Botones y enlaces)
private static final Color COLOR_TEXTO = new Color(245, 245, 245);    // Blanco humo (Textos principales)
private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150); // Gris (Textos secundarios, separadores)
private static final Color COLOR_INPUT = new Color(250, 250, 250);    // Blanco (Fondo de casillas de texto)

    //  fuentes
private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
private static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);


//variables
private JTextField Placa;
private JTextField Modelo;
private JTextField Capacidad;
private JButton btnRegistrar;
private JButton Limpiar;
private JComboBox<String> comboEstado; // selector de estado inactivo,e tc
private JTable tablaUnidades;

private javax.swing.table.DefaultTableModel modeloTabla; //tabla
private JLabel TextoError;


//configuración de la ventana
public AdminGestionarFlota() {

setTitle("MOVUCV - Panel de Administrador - Gestionar Flota");
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

JLabel texto1 = new JLabel("Panel de Administrador");
texto1.setFont(FUENTE_TEXTO);
texto1.setForeground(COLOR_TEXTO);

izq.add(titulo); //agregar el titulo a la izquierda
izq.add(sep); // agregar el separador del titulo a la izquiersa
izq.add(texto1); // agregamos el texto de registro de usuario a la izquierda

// parte derecha 

JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 10));
dere.setOpaque(false);
dere.add(Textoscabezal("Gestionar Flota"));

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

p.add(cuadroIzq());
p.add(cuadroDere());

        return p;
    }




private JPanel cuadroIzq(){

JPanel p =  new JPanel();

p.setBackground(COLOR_PANEL);
p.setPreferredSize(new  Dimension(600,500));
p.setLayout(new BorderLayout());
p.setBorder(new EmptyBorder(30,30,30,30));

JLabel titulo = new JLabel("Unidades en Sistema");
titulo.setFont(FUENTE_TITULO);
titulo.setForeground(COLOR_TEXTO);
titulo.setHorizontalAlignment(SwingConstants.CENTER);
titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
p.add(titulo, BorderLayout.NORTH);

//creamos el modelo de la tabla
String[] columnas = {"Placa", "Modelo", "Capacidad", "Estado"};
modeloTabla = new javax.swing.table.DefaultTableModel(null, columnas) {
//hacemos override para que no se puede editar la tgabla si le hacemos click
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
// ponemos la tabla dentro de un scroll para poder bajar por si hay muchas unidades registradas
JScrollPane scroll = new JScrollPane(tablaUnidades);
p.add(scroll, BorderLayout.CENTER);

return p;

}

private JPanel cuadroDere() {

JPanel p = new JPanel();
p.setBackground(COLOR_PANEL);
p.setPreferredSize(new Dimension(500, 500)); // Tamaño fijo
p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
p.setBorder(new EmptyBorder(30, 50, 30, 50));

//titulo del form
JLabel texto1 = new JLabel("Datos de la Unidad");
texto1.setFont(FUENTE_TITULO);
texto1.setForeground(COLOR_TEXTO);
texto1.setAlignmentX(Component.CENTER_ALIGNMENT);
p.add(texto1);
p.add(Box.createRigidArea(new Dimension(0, 30)));

JPanel casillas= new JPanel( new GridBagLayout());
casillas.setOpaque(false);
GridBagConstraints c = new GridBagConstraints();
c.fill = GridBagConstraints.HORIZONTAL;
c.weightx = 1.0;
c.insets = new Insets(0, 0, 15, 0);


// creamos las casillas del form
Placa = new JTextField();
crearCasillas(casillas, c, 0, "Placa:", Placa);

Modelo = new JTextField();
crearCasillas(casillas, c, 1, "Modelo:", Modelo);

Capacidad = new JTextField();
crearCasillas(casillas, c, 2, "Capacidad (Pasajeros):", Capacidad);

//ubicamos el combobox o sea el selector de estado
c.gridx = 0;
c.gridy = 3;
JLabel estado = new JLabel("Estado Operativo:");
estado.setFont(FUENTE_SUBTITULO);
estado.setForeground(COLOR_TEXTO);
casillas.add(estado, c);

c.gridx = 1;
c.gridy = 3;
String[] opcionesEstado = {"Operativo", "En Mantenimiento", "Inactivo"};
comboEstado = new JComboBox<>(opcionesEstado);
comboEstado.setFont(FUENTE_TEXTO);
comboEstado.setBackground(COLOR_INPUT);

p.add(casillas);
p.add(Box.createRigidArea(new Dimension(0, 30)));
casillas.add(comboEstado, c);

//boton para registrar una unidad
btnRegistrar = new JButton("Registrar Unidad");
btnRegistrar.setFont(FUENTE_SUBTITULO);
btnRegistrar.setBackground(COLOR_BOTONES);
btnRegistrar.setForeground(Color.WHITE);
btnRegistrar.setFocusPainted(false);
btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
btnRegistrar.setMaximumSize(new Dimension(200, 45)); 
p.add(btnRegistrar);
p.add(Box.createRigidArea(new Dimension(0, 15)));

//boton para limpiar el form 
Limpiar = new JButton("Limpiar");
Limpiar.setFont(FUENTE_SUBTITULO);
Limpiar.setBackground(Color.GRAY);
Limpiar.setForeground(Color.WHITE);
Limpiar.setFocusPainted(false);
Limpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
Limpiar.setAlignmentX(Component.CENTER_ALIGNMENT);
Limpiar.setMaximumSize(new Dimension(100, 10)); 
p.add(Limpiar);
p.add(Box.createRigidArea(new Dimension(0,15)));

// Etiqueta de error 
TextoError = new JLabel(" "); // Empieza vacía
TextoError.setFont(FUENTE_TEXTO);
TextoError.setForeground(Color.RED);
TextoError.setAlignmentX(Component.CENTER_ALIGNMENT);

p.add(TextoError);

        return p;
    }

private void crearCasillas(JPanel casillas, GridBagConstraints c, int n , String texto, JTextField entrada ){

c.gridx = 0;
c.gridy = n;
JLabel texto1= new JLabel(texto);
texto1.setFont(FUENTE_SUBTITULO);
texto1.setForeground(COLOR_TEXTO);
casillas.add(texto1,c);

c.gridx = 1;
c.gridwidth = 1;
entrada.setFont(FUENTE_TEXTO);
entrada.setBackground(COLOR_INPUT);
entrada.setBorder(new EmptyBorder(8,10,8,10));
casillas.add(entrada,c);


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



//metodo para mostrar mensaje de error
public void mostrarError(String mensaje) {
    
    TextoError.setText(mensaje);
}


//limpiar el mensaje de error
public void limpiarError() {
    TextoError.setText(" ");
}

// getters para obtener los valores de los campos de texto y botones
public JButton getBtnRegistrar() { return btnRegistrar; }
public String getPlaca() { return Placa.getText(); }
public String getModelo() { return Modelo.getText(); }
public String getCapacidad() { return Capacidad.getText(); }
public String getEstado() { return (String) comboEstado.getSelectedItem(); }
public javax.swing.table.DefaultTableModel getModeloTabla() { return modeloTabla; }
public JTable getTablaUnidades() { return tablaUnidades; }
public JButton getBtnLimpiar(){return Limpiar;}

//setters
public void setPlaca(String texto) { Placa.setText(texto); }
public void setModelo(String texto) { Modelo.setText(texto); }
public void setCapacidad(String texto) { Capacidad.setText(texto); }
public void setEstado(String estado) { comboEstado.setSelectedItem(estado); }


//metodo para limpiar el form despues de que se edite algo correctamente o se registre una unidad correctamente
public void limpiarFormulario() {
Validaciones.limpiarCampos(Placa);
Validaciones.limpiarCampos(Modelo);
Validaciones.limpiarCampos(Capacidad);
comboEstado.setSelectedIndex(0);
    limpiarError();
}

//cambio de boton dependiendo si se selecciona una unidad en lkla tabla para editar
public void cambiarModoBoton(boolean actualizar) {
if (actualizar) {
btnRegistrar.setText("Guardar Cambios");
} else {
    btnRegistrar.setText("Registrar Unidad");
}
}
//metodo para cargar la tabla desde el controlador
public void cargarDatosEnTabla(Object[][] datos) {
modeloTabla.setRowCount(0); // Limpia datos viejos
for (Object[] fila : datos) {
        modeloTabla.addRow(fila);
    }
}

    // MAIN
    public static void main(String[] args) {
        AdminGestionarFlota vista = new AdminGestionarFlota();
        @SuppressWarnings("unused")
        src.controller.AdminController controlador = new src.controller.AdminController(vista);
        vista.setVisible(true);

    }
}
