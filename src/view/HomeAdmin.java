package src.view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import src.model.Usuario;
import src.model.UsuarioDAO;
import src._helpers.ComponentUtils;
import src._helpers.ComponentUtils.Tarjeta;
import src.controller.HomeUsuarioController;
import src.controller.HomeAdminController;

import java.awt.geom.RoundRectangle2D;

public class HomeAdmin extends JFrame {
    private Usuario adminLogeado;
    private HomeAdminController controlador;
 
    //colores
    private static final Color COLOR_FONDO = new Color(11, 11, 35);       // Azul muy oscuro (Fondo general y Cabecera)
    private static final Color COLOR_PANEL = new Color(19, 29, 61);       // Azul oscuro (Para ambas tarjetas)
    private static final Color COLOR_BOTONES = new Color(125, 182, 245);   // Azul claro (Botones y enlaces)
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);    // Blanco humo (Textos principales)
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150); // Gris (Textos secundarios, separadores)
    private static final Color COLOR_AZUL_TARJETA = new Color(58, 99, 168);  // Azul de cabezal, barra y tarjetas
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

    public HomeAdmin(Usuario usuarioadminLogeado, HomeAdminController controlador){

        this.adminLogeado = usuarioadminLogeado;
        this.controlador = controlador;

        setTitle("MOVUCV - Panel de Administrador");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout()); 
 
        add(Cabezal(), BorderLayout.NORTH);
       add(Cuerpo(), BorderLayout.CENTER);
        add(PieDePagina(), BorderLayout.SOUTH);

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
 
        JLabel texto1 = new JLabel("Panel Administrador");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(COLOR_TEXTO);
 
        izq.add(titulo); // agregar el titulo a la izquierda
        izq.add(sep); // agregar el separador
        izq.add(texto1); // agregamos el texto de inicio
 
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
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
 
       /*p.add(menuLateral(), BorderLayout.WEST);
        p.add(dashboard(), BorderLayout.CENTER);*/ 
        return p;
    }

    private JPanel PieDePagina() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        p.setBackground(COLOR_PANEL);
        p.setBorder(new EmptyBorder(6, 0, 6, 0));
        JLabel texto = new JLabel("MOVUCV | Inicio | Datos Protegidos"); 

        texto.setFont(FUENTE_TEXTO);
        texto.setForeground(COLOR_TEXTO);
        p.add(texto);

        return p;
    }
}
