package es.aritzherrero.ejercicioi.Controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import es.aritzherrero.ejercicioi.DAO.PersonaDAO;
import es.aritzherrero.ejercicioi.HelloApplication;
import es.aritzherrero.ejercicioi.Modelo.Persona;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class EjercicioI_Principal_Control implements Initializable{

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private ContextMenu cmTabla;

    @FXML
    private ImageView imgImagen;

    @FXML
    private MenuItem miModificar;

    @FXML
    private MenuItem miEliminar;

    @FXML
    private TableView<Persona> tblvTabla;

    @FXML
    private TableColumn<Persona, String> tblcApellidos;

    @FXML
    private TableColumn<Persona, Integer> tblcEdad;

    @FXML
    private TableColumn<Persona, String> tblcNombre;

    @FXML
    private TextField txtFiltrar;

    // Variables de clase
    public static PersonaDAO pDao = new PersonaDAO();
    static ObservableList<Persona> listaPersonas;
    static ObservableList<Persona> listaFiltrada;
    static Persona p=new Persona("", "", 0);

    /*
     * Procedimiento de inicialización
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        listaPersonas = pDao.cargarPersonas();
        listaFiltrada = pDao.cargarPersonas();
        tblcNombre.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombre"));
        tblcApellidos.setCellValueFactory(new PropertyValueFactory<Persona, String>("apellidos"));
        tblcEdad.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("edad"));

        tblvTabla.setItems(listaFiltrada);

        cmTabla = new ContextMenu();
        miModificar = new MenuItem("Modificar");

        cmTabla.getItems().addAll(miModificar);

    }
    @FXML
    void agregarPersona(ActionEvent event) {
        // Limpiar los datos de la persona antes de abrir la ventana
        p.setNombre("");
        p.setApellidos("");
        p.setEdad(0);
        crearVentanaAux(); // Crear la ventana para añadir una nueva persona
        actualizarTablaCompleta(); // Actualizar la tabla con la nueva persona
    }


    /**
     * Procedimiento para eliminar una persona seleccionada de la tabla.
     *
     * Si no se selecciona ninguna persona, se muestra una alerta.
     *
     * @param event El evento de clic del botón "Eliminar"
     */
    @FXML
    void eliminarPersona(ActionEvent event) {
        try {
            // Obtener la persona seleccionada de la tabla
            Persona p = tblvTabla.getSelectionModel().getSelectedItem();
            listaPersonas.remove(p);   // Eliminar la persona de la lista principal
            listaFiltrada.remove(p);   // Eliminar la persona de la lista filtrada
            pDao.eliminarPersona(p);   // Eliminar la persona de la base de datos
            ventanaAlerta("I", "Persona eliminada correctamente"); // Mostrar confirmación
            actualizarTablaCompleta(); // Actualizar la tabla
        } catch (NullPointerException e) {
            // Mostrar alerta si no hay ninguna persona seleccionada
            ventanaAlerta("E", "Seleccione un registro de la tabla. Si no lo hay, añada uno.");
        }
    }

    /**
     * Procedimiento para modificar una persona seleccionada de la tabla.
     *
     * Si no se selecciona ninguna persona, se muestra una alerta.
     *
     * @param event El evento de clic del botón "Modificar"
     */
    @FXML
    void modificarPersona(ActionEvent event) {
        try {
            // Cargar los datos de la persona seleccionada para modificarlos
            p.setNombre(tblvTabla.getSelectionModel().getSelectedItem().getNombre());
            p.setApellidos(tblvTabla.getSelectionModel().getSelectedItem().getApellidos());
            p.setEdad(tblvTabla.getSelectionModel().getSelectedItem().getEdad());
            p.setId(tblvTabla.getSelectionModel().getSelectedItem().getId());
            crearVentanaAux(); // Abrir la ventana para modificar los datos
            actualizarTablaCompleta(); // Actualizar la tabla con los nuevos datos
        } catch (NullPointerException e) {
            // Mostrar alerta si no hay ninguna persona seleccionada
            ventanaAlerta("E", "Seleccione un registro de la tabla. Si no lo hay, añada uno.");
        }
    }

    /*
     * Procedimiento para filtrar por nombre la tabla.
     *
     * Cada vez que se inserte/elimine un caracter de txtFiltrar se actualiza.
     */
    @FXML
    void filtrarTabla(KeyEvent event) {
        // Obtener el texto del filtro y convertirlo a minúsculas
        String sFiltro = txtFiltrar.getText().toLowerCase();

        // Limpiar la lista filtrada antes de aplicar el filtro
        listaFiltrada.clear();
        for (Persona p : listaPersonas) {
            // Si el nombre de la persona contiene el texto del filtro, agregarla a la lista filtrada
            if (p.getNombre().toLowerCase().contains(sFiltro)) {
                listaFiltrada.add(p);
            }
        }

        // Actualizar la tabla con la lista filtrada
        tblvTabla.setItems(listaFiltrada);
    }


    /*
     * Metodos auxiliares
     */

    // para mostrar alertas de tipo error o confirmación
    static void ventanaAlerta(String tipoAlerta, String mensaje) {
        Alert alert = null;
        switch (tipoAlerta) {
            case ("E"):
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            case ("I"):
                alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    void crearVentanaAux() {
        Stage escena = new Stage(); // Crear una nueva ventana
        escena.setTitle("NUEVA PERSONA"); // Establecer el título de la ventana
        FlowPane flwPanel;

        try {
            // Cargar el archivo FXML para la nueva persona
            flwPanel = FXMLLoader.load(HelloApplication.class.getResource("/fxml/ejercicioi_nuevapersona.fxml"));
            Scene scene = new Scene(flwPanel, 600, 300);
            escena.setScene(scene);

            // Configurar las dimensiones mínimas de la ventana
            escena.setMinHeight(300);
            escena.setMinWidth(600);
            escena.show();
        } catch (IOException e) {
            // Si hay un error al cargar la vista, mostrar el error en la consola
            System.out.println("No ha sido posible abrir la ventana");
            e.printStackTrace();
        }
    }

    boolean comprobarPersona(Persona p) {
        boolean correcto = true;

        if (p.getNombre()=="")correcto=false;
        if (p.getApellidos()=="")correcto=false;
        if (p.getEdad()==0)correcto=false;

        return correcto;
    }

    void actualizarTablaCompleta() {
        tblvTabla.setItems(listaPersonas); // Establecer la lista completa de personas en la tabla
    }
}