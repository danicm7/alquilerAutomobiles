/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import app.flujosDatos;
import controlador.coordinador;
import controlador.logica;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import modelo.Automobil;
import modelo.Cliente;
import modelo.DAOmysql;

/**
 * FXML Controller class
 *
 * @author a
 */
public class NuevaReservaController implements Initializable {

    private ObservableList ObservableList;
    @FXML
    private TableView<Automobil> tableViewCoches;
    @FXML
    private TableColumn<Automobil, String> TableColumnMarca;
    @FXML
    private TableColumn<Automobil, String> TableColumnModelo;
    @FXML
    private TableColumn<Automobil, java.lang.Character> TableColumnCombustible;
    @FXML
    private TableColumn<Automobil, java.lang.Integer> TableColumPlazas;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldDNI;
    private Automobil automobil;
    @FXML
    private TextArea TextAreaObservaciones;
    @FXML
    private DatePicker DatePickerFechaFin;
    private String fecha;
    @FXML
    private Button buttonInfo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //rellenar la tableView
        TableColumnMarca.setCellValueFactory(new PropertyValueFactory<Automobil, String>("Marca"));
        TableColumnModelo.setCellValueFactory(new PropertyValueFactory<Automobil, String>("Modelo"));
        TableColumnCombustible.setCellValueFactory(new PropertyValueFactory<Automobil, java.lang.Character>("Combustible"));
        TableColumPlazas.setCellValueFactory(new PropertyValueFactory<Automobil, java.lang.Integer>("Plazas"));
        recargaInfo();
        //poner el calendario a aditable
        DatePickerFechaFin.setEditable(true);
        //crear ImageView para ponerlo en el boton
        ImageView imageView = new ImageView("/imagenes/iconoInfo.png");
        //poner el imageView en el boton
        buttonInfo.setGraphic(imageView);
    }

    @FXML
    private void handleButtonHecho(ActionEvent event) throws IOException, InterruptedException {
        //aqui se valida que ningun dato esta vacio, i que el del dni/telefono es correcto
        //se validan coche y fecha con booleanos y try-catch, porque al acceder con getValue()/equals,
        //lanza excepcion
        boolean hayAuto = true;
        boolean hayFecha = true;
        automobil = tableViewCoches.getSelectionModel().getSelectedItem();
        //comprueva automobil
        if (automobil == null) {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "Seleccion un automobil!", null, "no has seleccionado ningun automobil");
            hayAuto = false;
        }
        //comprueba fecha
        try {
            fecha = DatePickerFechaFin.getValue().toString();
            //comprueba que la fecha esta dentro del limite
            if (new logica().validaFecha(fecha)) {
                hayFecha = true;
            } else {
                hayFecha = false;
                return;
            }
        } catch (java.lang.NullPointerException asfsdgdasfg) {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "Seleccion una fecha!", null, "no has seleccionado ninguna fecha!");
            hayFecha = false;
            return;
        }
        //comprueba campos de texto
        if (!textFieldDNI.getText().equals("") && !textFieldNombre.getText().equals("") && !textFieldTelefono.getText().equals("")) {
            // if (hayAuto && hayFecha) {
            try {
                //crea cliente
                Cliente cliente = new Cliente(Integer.parseInt(textFieldDNI.getText()), textFieldNombre.getText(), Integer.parseInt(textFieldTelefono.getText()));
                new logica().validaReserva(cliente, automobil, fecha, TextAreaObservaciones.getText());
                return;
            } catch (IOException | InterruptedException | NumberFormatException adfdty) {
                new logica().mostrarDialeg(Alert.AlertType.ERROR, "Error de formato", null, "Error en el formato de los datos!\nusa la cantidad de digitos correcta");
                return;
            }
        } else {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "Campos Vacios", null, "Rellena todos los campos!");
        }

    }

    @FXML
    private void handleButtonVolver(ActionEvent event) throws IOException {
        new coordinador().setEscenaInicio();
    }

    @FXML
    private void onActionButtonInfo(ActionEvent event) {
        try {
            new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Informacion", null, new flujosDatos().lee("/info/infoNuevaReserva.txt"));
        } catch (Exception a) {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "No se ha encontrado el archivo", null, "no se ha encontrado el archivo de texto con las instrucciones de uso");
        }
    }

    private void recargaInfo() {
        ObservableList = FXCollections.observableList(new DAOmysql().obtenerTodosCochesDisponibles());
        ObservableList = new logica().eliminarVehiculosRetirados(ObservableList);
        tableViewCoches.setItems(ObservableList);
    }
}
