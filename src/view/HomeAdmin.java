package src.view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.model.Usuario;
import src.model.UsuarioDAO;
import src._helpers.ComponentUtils;
import java.awt.geom.RoundRectangle2D;

public class HomeAdmin extends JFrame {
    private Usuario adminLogeado;
 
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

    public HomeAdmin(String correo){
        try{

        this.adminLogeado = UsuarioDAO.busquedaPorCorreo(correo);

        } catch (IOException e){
            e.printStackTrace(); 
            System.err.println("*Error al cargar los datos del usuario.");
        }

        setTitle("MOVUCV - Inicio");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
 
        /*add(Cabezal(), BorderLayout.NORTH);
        add(Cuerpo(), BorderLayout.CENTER);
        add(PieDePagina(), BorderLayout.SOUTH);*/

    }
    
}
