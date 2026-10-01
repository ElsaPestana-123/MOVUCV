package src._helpers;

import java.awt.*;

import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.geom.RoundRectangle2D;

public class ComponentUtils {


   public static JLabel textoPresionable(String texto,Color COLOR_TEXTO,Font FUENTE_TEXTO,  Runnable accion) { 
        JLabel text = new JLabel(texto);
        text.setFont(FUENTE_TEXTO);
        text.setForeground(COLOR_TEXTO);
 
        if (accion != null){

            text.setCursor(new Cursor(Cursor.HAND_CURSOR));
            text.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override

            public void mouseClicked(java.awt.event.MouseEvent e) {
                accion.run();
            }
            });
        }
        return text; //
    }

    public static class Tarjeta extends JPanel {// clase tarjeta 

        private static final Color COLOR_AZUL_TARJETA = new Color(58, 99, 168);
        private Color linea;
        private Color fondo;
 
        public Tarjeta(Color linea) {
            this(COLOR_AZUL_TARJETA, linea);
        }

        public Tarjeta(Color fondo, Color linea) {
        this.fondo = fondo;
        this.linea = linea;
        setOpaque(false);
        }
 
        @Override
        protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        RoundRectangle2D forma = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30);
 
        g2.setColor(fondo);
        g2.fill(forma);
 

        if (linea != null) {
            g2.setClip(forma);
            g2.setColor(linea);
            g2.fillRect(0, 0, getWidth(), 4);
        }
 
        g2.dispose();
        
       }
    }

    
}
