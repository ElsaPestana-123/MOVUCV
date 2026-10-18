package src.model;

public class Pasaje {
    private String ruta;
    private char rol;
    private double montoPagado;

    public Pasaje(String ruta, char rol, double montoPagado) {
        this.ruta = ruta;
        this.rol = rol;
        this.montoPagado = montoPagado;
    }

    //Getters

    public String getRuta() {
        return ruta;
    }

    public char getRol() {
        return rol;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    //Setters

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setRol(char rol) {
        this.rol = rol;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }
    
}
