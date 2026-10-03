package src;

import javax.swing.SwingUtilities;
import src.view.MOVUCVInicioApp;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MOVUCVInicioApp app = new MOVUCVInicioApp();
            app.setVisible(true);
        });
    }
}
