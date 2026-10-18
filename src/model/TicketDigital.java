package src.model;

public class TicketDigital {
    private String idReserva;
    private String nombrePasajero;
    private String cedula;
    private String ruta;
    private String horaSalida;
    private double tarifaBase;
    private double montoPagado;
    private String rolPasajero;

    public TicketDigital(String idReserva, String nombrePasajero, String cedula, String ruta, String horaSalida, double tarifaBase, double montoPagado, String rolPasajero) {
        this.idReserva = idReserva;
        this.nombrePasajero = nombrePasajero;
        this.cedula = cedula;
        this.ruta = ruta;
        this.horaSalida = horaSalida;
        this.tarifaBase = tarifaBase;
        this.montoPagado = montoPagado;
        this.rolPasajero = rolPasajero;
    }

    // Getters
    public String getIdReserva() { 
        return idReserva; 
    
    }
    public String getNombrePasajero() { 
        return nombrePasajero; 
    }

    public String getCedula() { 
        return cedula; 
    }

    public String getRuta() { 
        return ruta; 
    }

    public String getHoraSalida() { 
        return horaSalida; 
    }

    public double getTarifaBase() { 
        return tarifaBase; 
    }

    public double getMontoPagado() { 
        return montoPagado; 
    }

    public String getRolPasajero() { 
        return rolPasajero; 
    }
}
