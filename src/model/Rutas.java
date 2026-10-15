package src.model;

public class Rutas {

    public enum TipoRuta {
        URBANA,
        EXTRAURBANA
    }

    private String nombreRuta;
    private TipoRuta tipoRuta;
    private double tarifa;

    public Rutas(String nombreRuta, TipoRuta tipoRuta, double tarifa) {
        this.nombreRuta = nombreRuta;
        this.tipoRuta = tipoRuta;
        this.tarifa = tarifa;

    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public TipoRuta getTipoRuta() {
        return tipoRuta;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public void setTipoRuta(TipoRuta tipoRuta) {
        this.tipoRuta = tipoRuta;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }
    
}
