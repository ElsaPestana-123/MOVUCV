package src.controller;

import src.model.Reporte;
import src.model.ReporteDAO;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ReporteController {

    public Reporte procesarReporte() {
        
        return new ReporteDAO().generarReporte();
    }

    public boolean manejarExportacion(Reporte reporte) {
       
        if (reporte == null) {
            return false; 
        }

        JFileChooser exploradorArchivos = new JFileChooser();
        exploradorArchivos.setDialogTitle("Exportar Data Consolidada");
        
        String nombreSugerido = "MOVUCV_Reporte_" + reporte.getIdReporte() + ".csv";
        exploradorArchivos.setSelectedFile(new File(nombreSugerido));

        FileNameExtensionFilter filtroCSV = new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv");
        exploradorArchivos.setFileFilter(filtroCSV);

        int seleccion = exploradorArchivos.showSaveDialog(null);
        
        if (seleccion == JFileChooser.APPROVE_OPTION) {

            String rutaDestino = exploradorArchivos.getSelectedFile().getAbsolutePath();
            
            if (!rutaDestino.toLowerCase().endsWith(".csv")) {
                rutaDestino += ".csv";
            }
            
            return ReporteDAO.exportarCSV(reporte, rutaDestino);
        }
        
        return false; 
    }
}
