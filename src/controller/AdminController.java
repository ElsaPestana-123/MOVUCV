package src.controller;
import src.view.AdminGestionarFlota;
import src.model.UnidadDAO;
import src.model.Unidad;
import java.util.List;
public class AdminController {

private AdminGestionarFlota vista;
private UnidadDAO unidadDAO;
private boolean Editar = false;

public AdminController(AdminGestionarFlota vista){
this.vista = vista;
this.unidadDAO = new UnidadDAO();
cargarTabla();
inicioEventos();
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


//metodos para los eventos (cuando se selecciona una unidad de la tabla, registrar, guardar cambios)
private void inicioEventos(){

//agregamos un listener para el boton de limpiar
vista.getBtnLimpiar().addActionListener(e -> {
//limpiamos form
vista.limpiarFormulario();
//ponemos el boton en registrar unidad
vista.cambiarModoBoton(false);
//dejamos de seleccionar una unidad en la tabla
vista.getTablaUnidades().clearSelection();
Editar = false;
});
//agregamos un listener para la tabla
vista.getTablaUnidades().getSelectionModel().addListSelectionListener(e -> {
//evitamos que se buguee si le damos dos veces o sostenemos el click
if (!e.getValueIsAdjusting()) {

int fila = vista.getTablaUnidades().getSelectedRow();
// seleccion valida
if (fila >= 0) {
//usamos los getters para obtener los datos de la tabla
String placa = vista.getModeloTabla().getValueAt(fila, 0).toString();
String modelo = vista.getModeloTabla().getValueAt(fila,1).toString();
String capacidad = vista.getModeloTabla().getValueAt(fila,2).toString();
String estado = vista.getModeloTabla().getValueAt(fila,3).toString();
//usamos los setters para poner los datos en el form 
vista.setPlaca(placa);
vista.setModelo(modelo);
vista.setCapacidad(capacidad);
vista.setEstado(estado);
//actualizamos el boton a true para que salga la opcion de guardar cambios
vista.cambiarModoBoton(true );
Editar = true;
}
}

});

}

}
