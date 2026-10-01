package src.view;


import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.model.Usuario;
import src.model.UsuarioDAO;

public class HomeUsuario extends JFrame {
    private Usuario usuarioLogeado;

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

    public HomeUsuario(String correo){
        try{
        this.usuarioLogeado = UsuarioDAO.busquedaPorCorreo(correo);





        } catch (IOException e){
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(this, "*Error al cargar los datos del usuario."); 
        }


    }
    
}
