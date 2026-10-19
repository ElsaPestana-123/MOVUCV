package src._helpers;

import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;


import src.controller.HomeAdminController;


public class ComponentUtils {

   public static String mensajeMultilinea(String mensaje, int caracteresPorLinea) {
        return formatearMensaje(mensaje, caracteresPorLinea, "center");
    }

   public static String mensajeJustificado(String mensaje, int caracteresPorLinea) {
        return formatearMensaje(mensaje, caracteresPorLinea, "justify");
    }

   private static String formatearMensaje(String mensaje, int caracteresPorLinea, String alineacion) {
        if (mensaje == null || mensaje.isEmpty()) {
            return " ";
        }

        mensaje = mensaje.replaceAll("<[^>]*>", "").trim();
        StringBuilder resultado = new StringBuilder("<html><div style='text-align:" + alineacion + ";'>");
        int caracteresEnLinea = 0;
        for (String palabra : mensaje.split("\\s+")) {
            int longitud = palabra.length();
            if (caracteresEnLinea > 0 && caracteresEnLinea + longitud + 1 > caracteresPorLinea) {
                resultado.append("<br>");
                caracteresEnLinea = 0;
            } else if (caracteresEnLinea > 0) {
                resultado.append(' ');
                caracteresEnLinea++;
            }

            resultado.append(palabra);
            caracteresEnLinea += longitud;
        }
        return resultado.append("</div></html>").toString();
    }

   public static JLabel textoPresionable(String texto,Color COLOR_TEXTO,Font FUENTE_TEXTO, Runnable accion) { 
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
        return text; //en caso de acción null no hace nada
    }

    public static class Tarjeta extends JPanel {// clase tarjeta para dashboard de admin y home de usuario

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

    public static JPanel menuIzqu(JFrame ventana,HomeAdminController controlador){ //para el menu lateral izquierdo del administrador
        Color COLOR_TEXTO = new Color(245, 245, 245);    // Blanco humo (Textos principales)
        Color COLOR_AZUL_TARJETA = new Color(58, 99, 168);  // Azul de cabezal, barra y tarjetas
        Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
   
        JPanel p = new JPanel();
        p.setBackground(COLOR_AZUL_TARJETA);
        p.setPreferredSize(new Dimension(200, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel titulo = new JLabel("Menu"); //
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(titulo);
        p.add(Box.createRigidArea(new Dimension(0, 20)));

        p.add(botonMenu("Dashboard", true, () -> controlador.regresarDashboard(ventana))); 
        p.add(Box.createRigidArea(new Dimension(0, 10))); // separa botones

        p.add(botonMenu("Gestionar Flota", false, ()-> controlador.llamarGestionarF(ventana))); //llamada que te lleva a gestionar flota
        p.add(Box.createRigidArea(new Dimension(0, 10)));

        p.add(botonMenu("Gestionar Itinerarios", false, () -> controlador.llamarGestionarI(ventana)));
        p.add(Box.createRigidArea(new Dimension(0, 10)));

        p.add(botonMenu("Gestionar Reportes", false, () -> controlador.llamarReportes(ventana)));
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(botonMenu("Gestionar Rutas", false, () -> controlador.llamarRutas(ventana)));

        p.add(Box.createVerticalGlue()); 
        return p;
    }

    public static JButton botonMenu(String texto, boolean activo, Runnable accion){ //botones del menu 
        Color COLOR_FONDO = new Color(11, 11, 35);     
        Color COLOR_TEXTO = new Color(245, 245, 245);    
        Font FUENTE_PEQUENA_NEGRITA = new Font("SansSerif", Font.BOLD, 13);

        JButton b = new JButton(texto);
        b.setFont(FUENTE_PEQUENA_NEGRITA);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setPreferredSize(new Dimension(170, 36));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36)); 

        if (activo) {
            b.setBackground(COLOR_FONDO); // para que al tocarlo se aclare en blanco
            b.setForeground(COLOR_TEXTO);

        } else {
            b.setBackground(COLOR_FONDO);
            b.setForeground(COLOR_TEXTO);
        }

        b.addActionListener(e -> accion.run()); // ejecute la accion que le pasan

        return b;
    }  

    public static void aplicarIcono(Window ventana) {
        try {
            var imagen = ImageIO.read(new File("res/images/logo sin fondo.png"));
            if (imagen != null) {
                ventana.setIconImage(imagen.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
            }
        } catch (IOException e) {
            System.err.println("No se pudo cargar res/images/logo sin fondo.png " + e.getMessage());
        }
    }
}
