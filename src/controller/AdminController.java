package src.controller;
import src.view.AdminGestionarFlota;
import src.model.UnidadDAO;
import src.model.Unidad;
import java.util.List;
public class AdminController {

private AdminGestionarFlota vista;
private UnidadDAO unidadDAO;


public AdminController(AdminGestionarFlota vista){
this.vista = vista;
this.unidadDAO = new UnidadDAO();
cargarTabla();
}

private void cargarTabla(){

    List<Unidad> listaUnidades = unidadDAO.listaUnidades();

    Object[][] listaCompleta = new Object[listaUnidades.size()][4];

    for(int i = 0; i < listaUnidades.size(); i++){
Unidad u = listaUnidades.get(i);
String estadoS = u.isDisponible() ? "Operativo" : "Inactivo";

listaCompleta[i][0] = u.getPlaca();
listaCompleta[i][1] = u.getModelo();
listaCompleta[i][2] = String.valueOf(u.getCapacidad());
listaCompleta[i][3] = estadoS;
    }
vista.cargarDatosEnTabla(listaCompleta);
}

}
