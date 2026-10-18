package src.model;

public class Reserva {
    private String idReserva; 
    private String cedulaPasajero; 
    private int idItinerario; 
    private int numeroAsiento;
    private String fecha; 

    public Reserva(String idReserva, String cedulaPasajero, int idItinerario, int numeroAsiento, String fecha) {
        this.idReserva = idReserva;
        this.cedulaPasajero = cedulaPasajero;
        this.idItinerario = idItinerario;
        this.numeroAsiento = numeroAsiento;
        this.fecha = fecha;
    }

    // getters and setters
    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getCedulaPasajero() {
        return cedulaPasajero;
    }

    public void setCedulaPasajero(String cedulaPasajero) {
        this.cedulaPasajero = cedulaPasajero;
    }

    public int getIdItinerario() {
        return idItinerario;
    }

    public void setIdItinerario(int idItinerario) {
        this.idItinerario = idItinerario;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(int numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
