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
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import modelo.Alquiler;
import modelo.Automobil;
import modelo.DAOmysql;

/**
 * FXML Controller class
 *
 * @author a
 */
public class verAlquileresController implements Initializable {

    @FXML
    private TableView<Alquiler> tableViewAnular;
    @FXML
    private TableColumn<Alquiler, String> TableColumnMarca;
    @FXML
    private TableColumn<Alquiler, String> TableColumnNombreCliente;
    @FXML
    private TableColumn<Alquiler, Integer> TableColumnDni;
    @FXML
    private Button buttonSalir;
    private ObservableList ObservableList;
    @FXML
    private TableColumn<Alquiler, String> TableColumnFechaInicio;
    @FXML
    private TableColumn<Alquiler, String> TableColumnFechaFin;
    @FXML
    private Button buttonInfo;
    @FXML
    private TableColumn<Alquiler, String> TableColumnObservaciones;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //rellenar la tableView
        TableColumnMarca.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("nombreMarca"));
        TableColumnNombreCliente.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("nombreCliente"));
        TableColumnDni.setCellValueFactory(new PropertyValueFactory<Alquiler, Integer>("Dni"));
        TableColumnFechaInicio.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("fechaInicio"));
        TableColumnFechaFin.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("fechaFin"));
        TableColumnObservaciones.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("observaciones"));
        recargaInfo();
        //crear ImageView para ponerlo en el boton
        ImageView imageView = new ImageView("/imagenes/iconoInfo.png");
        //poner el imageView en el boton
        buttonInfo.setGraphic(imageView);
    }

    private void handleButtonAnular(ActionEvent event) {
        new DAOmysql().borrarEntradaHistorial(tableViewAnular.getSelectionModel().getSelectedItem());
        //cambia la variable 'enUso' a false pasandole como parametro el idCoche del coche seleccionado y 'false'
        new DAOmysql().marcaEnUsoPorIdAuto(tableViewAnular.getSelectionModel().getSelectedItem().getId_coche(), false);
        recargaInfo();

    }

    @FXML
    private void handleButtonSalir(ActionEvent event) throws IOException {
        new coordinador().setEscenaInicio();
    }

    @FXML
    private void onActionButtonInfo(ActionEvent event) {
        try {
            new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Informacion", null, new flujosDatos().lee("/info/infoVerAlquileres.txt"));
        } catch (Exception a) {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "No se ha encontrado el archivo", null, "no se ha encontrado el archivo de texto con las instrucciones de uso");
        }
    }

    private void recargaInfo() {
        ObservableList = FXCollections.observableList(new DAOmysql().ObtenerHistorialAnularReserva());
        ObservableList = new logica().borraAlquileresAcabadosAndAutosRetiradosAndAutosNoEnUso(ObservableList);
        //borra el simbolo % que indica que el auto esta retirado del catalogo de la empresa
        ObservableList = new logica().sustituyeSimboloporR(ObservableList);
        tableViewAnular.setItems(ObservableList);
    }

    @FXML
    private void onMouseClickedTableViewHistorial(MouseEvent event) {
        new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Observaciones", null, tableViewAnular.getSelectionModel().getSelectedItem().getObservaciones());
    }

}
