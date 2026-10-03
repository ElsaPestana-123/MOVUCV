package src;

import javax.swing.SwingUtilities;
import src.view.MOVUCVInicioApp;

public class main {
    public static void main() {
        SwingUtilities.invokeLater(() -> {
            MOVUCVInicioApp app = new MOVUCVInicioApp();
            app.setVisible(true);
        });
    }
}
