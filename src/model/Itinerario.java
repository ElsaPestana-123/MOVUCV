package src.model;

import src._helpers.Validaciones;

public class Itinerario {

    private String ruta;
    private String horaSalida;
    private String placa;
    private String conductor;
    private String estado;
    private int reservas;

    public boolean validarDatos(){
        return Validaciones.validarFormatoItinerario(this.ruta, this.horaSalida, this.estado);
    }

    public Itinerario(String ruta, String horaSalida, String placa, String conductor, String estado, int reservas) {

        this.ruta = ruta;
        this.horaSalida = horaSalida;
        this.placa = placa;
        this.conductor = conductor;
        this.estado = estado;
        this.reservas = reservas;

    }

    //Get and set

    public String getRuta() {

        return this.ruta;

    }

    public String getHoraSalida() {

        return this.horaSalida;

    }

    public String getPlaca() {

        return this.placa;

    }

    public String getConductor() {

        return this.conductor;

    }

    public String getEstado() {

        return this.estado;

    }

    public int getReservas() {

        return this.reservas;

    }

    public void setRuta(String ruta) {

        this.ruta = ruta;

    }

    public void setHoraSalida(String horaSalida) {

        this.horaSalida = horaSalida;

    }

    public void setPlaca(String placa) {

        this.placa = placa;

    }

    public void setConductor(String conductor) {

        this.conductor = conductor;

    }

    public void setEstado(String estado) {

        this.estado = estado;

    }

    public void setReservas(int reservas) {

        this.reservas = reservas;

    }
    
}
