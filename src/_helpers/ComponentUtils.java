package src._helpers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;

import javax.swing.JLabel;

public class ComponentUtils {


   public static JLabel textoPresionable(String texto,Color COLOR_TEXTO,Font FUENTE_TEXTO,  Runnable accion) {
        JLabel texto1 = new JLabel(texto);
        texto1.setFont(FUENTE_TEXTO);
        texto1.setForeground(COLOR_TEXTO);
 
        if (accion != null) {

            texto1.setCursor(new Cursor(Cursor.HAND_CURSOR));
            texto1.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override

            public void mouseClicked(java.awt.event.MouseEvent e) {
                accion.run();
            }
            });
        }
        return texto1;
    }


    
}
