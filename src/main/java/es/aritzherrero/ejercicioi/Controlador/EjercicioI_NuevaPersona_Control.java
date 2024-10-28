package es.aritzherrero.ejercicioi.Controlador;


import java.net.URL;
import java.util.ResourceBundle;

import es.aritzherrero.ejercicioi.Modelo.Persona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EjercicioI_NuevaPersona_Control implements Initializable{

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtNombre;

    //variables de clase
    String camposNulos;

    /*
     * Método de inicialización.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (!EjercicioI_Principal_Control.p.getApellidos().isEmpty()) {
            txtNombre.setText(EjercicioI_Principal_Control.p.getNombre());
            txtApellidos.setText(EjercicioI_Principal_Control.p.getApellidos());
            txtEdad.setText(EjercicioI_Principal_Control.p.getEdad()+"");
        }
    }

    /*
     * Metodo para cerrar la ventana auxiliar
     */
    @FXML
    void cancelarVentana(ActionEvent event) {
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    /*
     * Método para agregar personas a la tabla.
     * Se controla que los campos no pueden ser nulos y que el campo edad sea un número mayor que 1.
     */
    @FXML
    void guardarPersona(ActionEvent event) {
        if (EjercicioI_Principal_Control.p.getNombre().equals("")) {
            aniadir();
        }else {
            modificar();
        }
        cancelarVentana(event);
    }

    /*
     * Métodos auxiliares
     */

    private void aniadir() {
        String camposNulos = "";
        try {
            // Controlar que los parametros se insertan correctamente
            camposNulos = comprobarCampos();
            if (camposNulos!="") {throw new NullPointerException();}
            if (Integer.parseInt(txtEdad.getText().toString()) < 1) {throw new NumberFormatException();}

            // Crear persona
            String nombre= txtNombre.getText();
            String apellidos= txtApellidos.getText();
            Integer edad= Integer.parseInt(txtEdad.getText());
            Persona p = new Persona(nombre, apellidos, edad);
            // Insertar persona, controlando que no exista
            if (EjercicioI_Principal_Control.listaPersonas.contains(p)== false) {
                p.setId(EjercicioI_Principal_Control.pDao.insertarPersona(p));
                EjercicioI_Principal_Control.listaPersonas.add(p);
                EjercicioI_Principal_Control.listaFiltrada.add(p);

                EjercicioI_Principal_Control.ventanaAlerta("I", "Persona añadida correctamente");
                eliminarValores();
            }else{
                EjercicioI_Principal_Control.ventanaAlerta("E", "La persona ya existe");
            }
        }catch(NullPointerException e){
            EjercicioI_Principal_Control.ventanaAlerta("E", camposNulos);
        }catch(NumberFormatException e) {
            EjercicioI_Principal_Control.ventanaAlerta("E", "El valor de edad debe ser un número mayor que cero");
        }
    }

    private void modificar() {
        camposNulos="";
        try {
            // Controlar que los parametros se insertan correctamente
            camposNulos = comprobarCampos();
            if (!camposNulos.equals("")) {
                throw new NullPointerException();
            }
            if (Integer.parseInt(txtEdad.getText().toString()) < 1) {throw new NumberFormatException();}

            // Crear persona para comprobar que no existe
            Persona pAux = new Persona(txtNombre.getText(), txtApellidos.getText(), Integer.parseInt(txtEdad.getText()), EjercicioI_Principal_Control.p.getId());
            if (!EjercicioI_Principal_Control.listaPersonas.contains(pAux)) {
                // Modificar persona
                EjercicioI_Principal_Control.pDao.modificarPersona(EjercicioI_Principal_Control.p,pAux);
                EjercicioI_Principal_Control.listaPersonas.remove(EjercicioI_Principal_Control.p);
                EjercicioI_Principal_Control.listaFiltrada.remove(EjercicioI_Principal_Control.p);
                EjercicioI_Principal_Control.listaPersonas.add(pAux);
                EjercicioI_Principal_Control.listaFiltrada.add(pAux);
                EjercicioI_Principal_Control.ventanaAlerta("I", "Persona modificada correctamente");
                eliminarValores();
            }else {
                EjercicioI_Principal_Control.ventanaAlerta("E", "Persona existente");
            }

        }catch(NullPointerException e){
            EjercicioI_Principal_Control.ventanaAlerta("E", camposNulos);
        }
    }
    // Vacia los editText  
    private void eliminarValores() {
        txtNombre.clear();
        txtApellidos.clear();
        txtEdad.clear();
    }

    private String comprobarCampos() {
        // Controlar que los parametros se insertan correctamente
        String sCamposNulos="";
        if (txtNombre.getText().equals("")) {sCamposNulos = "El campo nombre es obligatorio\n";}
        if (txtApellidos.getText().equals("")) {sCamposNulos += "El campo apellidos es obligatorio\n";}
        if (txtEdad.getText().isEmpty()) {sCamposNulos += "El campo edad es obligatorio";}
        return sCamposNulos;
    }
}
