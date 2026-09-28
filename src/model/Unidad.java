package src.model;

import src._helpers.Validaciones;

public class Unidad {

    private String placa;
    private String modelo;
    private int capacidad;
    private boolean disponible;

    public Unidad(String placa, String modelo, int capacidad, boolean disponible) {
        this.placa = placa;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.disponible = disponible;

    }

    public boolean validarDatos(){
        return Validaciones.validarFormatoDatosUnidad(this.placa,this.modelo,this.capacidad);
    }

    // Getters and Setters
    public String getPlaca() {
        return this.placa;
    }

    public String getModelo() {
        return this.modelo;
    }

    public int getCapacidad() {
        return this.capacidad;
    }

    public boolean isDisponible() {
        return this.disponible;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
