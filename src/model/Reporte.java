package src.model;

import java.util.Map;

public class Reporte {

    private int idReporte; 
    private String tipoReporte;
    private double totalEstudiantes;
    private double totalTrabajadores;
    private double totalPublico;
    private Map<String, Integer> concurrenciaRutas;

    public Reporte(int idReporte, String tipoReporte, double totalEstudiantes, double totalTrabajadores, double totalPublico, Map<String, Integer> concurrenciaRutas) {
        this.idReporte = idReporte;
        this.tipoReporte = tipoReporte;
        this.totalEstudiantes = totalEstudiantes;
        this.totalTrabajadores = totalTrabajadores;
        this.totalPublico = totalPublico;
        this.concurrenciaRutas = concurrenciaRutas;
    }

    public int getIdReporte() { return idReporte; } 
    public String getTipoReporte() { return tipoReporte; }
    public double getTotalEstudiantes() { return totalEstudiantes; }
    public double getTotalTrabajadores() { return totalTrabajadores; }
    public double getTotalPublico() { return totalPublico; }
    public Map<String, Integer> getConcurrenciaRutas() { return concurrenciaRutas; }

    public double getTotal() {
        return totalEstudiantes + totalTrabajadores + totalPublico;
    }
    
}
