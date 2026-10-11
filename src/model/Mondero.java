package src.model;

public class Mondero {
    private String bancoEmisor;
    private String numeroTelefono;
    private String cedula;
    private String numeroReferencia;
    private float monto;

    public Mondero(String bancoEmisor, String numeroTelefono, String cedula, String numeroReferencia, float monto) {
        this.bancoEmisor = bancoEmisor;
        this.numeroTelefono = numeroTelefono;
        this.cedula = cedula;
        this.numeroReferencia = numeroReferencia;
        this.monto = monto;
    }

    public String getBancoEmisor() {
        return bancoEmisor;
    }

    public void setBancoEmisor(String bancoEmisor) {
        this.bancoEmisor = bancoEmisor;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }
    
}
