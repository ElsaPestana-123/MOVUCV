package src.model;

public class Rutas {

    public enum TipoRuta {
        URBANA,
        EXTRAURBANA
    }

    private String nombreRuta;
    private TipoRuta tipoRuta;

    public Rutas(String nombreRuta, TipoRuta tipoRuta) {
        this.nombreRuta = nombreRuta;
        this.tipoRuta = tipoRuta;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public TipoRuta getTipoRuta() {
        return tipoRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public void setTipoRuta(TipoRuta tipoRuta) {
        this.tipoRuta = tipoRuta;
    }
    
}
