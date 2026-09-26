package src.model;

public class Usuario {
    private String cedula;
    private char rol; // E -> Estudiante, T -> Trabajador, P -> Público general, C -> Conductor y A -> Administrador
    private String nombre;
    private String correo;
    private String claveAcceso;
    private double saldo;

    public Usuario(String cedula, char rol, String nombre, String correo, String claveAcceso, double saldo){
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
        this.claveAcceso = claveAcceso;
        this.rol = rol;
        this.saldo = saldo;
    }

    // getters y setters
    public String getNombre(){ 
        return nombre;
    }

    public String getCedula(){ 
        return cedula;
    }

    public String getCorreo(){ 
        return correo;
    }

    public String getClaveAcceso(){ 
        return claveAcceso;
    }
    public char getRol(){ 
        return rol;
    }

    public double getSaldo(){ 
        return saldo;
    }

    public void setSaldo(double saldo){
        this.saldo = saldo;
    }

    public void setCorreo(String correo){
        this.correo = correo;    
    }

    public void setClaveAcceso(String claveAcceso){
        this.claveAcceso = claveAcceso;    
    }
    
}