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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import modelo.Automobil;
import modelo.DAOmysql;

/**
 * FXML Controller class
 *
 * @author a
 */
public class EliminarVehiculoController implements Initializable {

    @FXML
    private Button buttonEliminar;
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
    private TableColumn<Automobil, java.lang.String> TableColumMatricula;
    @FXML
    private TableColumn<Automobil, java.lang.String> TableColumColor;
    @FXML
    private Button buttonVolver;
    private coordinador coordinador;
    @FXML
    private Button buttonInfo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        coordinador = new coordinador();
        //rellenar la tableView
        TableColumnMarca.setCellValueFactory(new PropertyValueFactory<Automobil, String>("Marca"));
        TableColumnModelo.setCellValueFactory(new PropertyValueFactory<Automobil, String>("Modelo"));
        TableColumnCombustible.setCellValueFactory(new PropertyValueFactory<Automobil, java.lang.Character>("combustibleString"));
        TableColumPlazas.setCellValueFactory(new PropertyValueFactory<Automobil, java.lang.Integer>("Plazas"));
        TableColumMatricula.setCellValueFactory(new PropertyValueFactory<Automobil, java.lang.String>("matricula"));
        TableColumColor.setCellValueFactory(new PropertyValueFactory<Automobil, java.lang.String>("color"));
        recargaInfo();
        //crear ImageView para ponerlo en el boton
        ImageView imageView = new ImageView("/imagenes/iconoInfo.png");
        //poner el imageView en el boton
        buttonInfo.setGraphic(imageView);
    }

    @FXML
    private void handleButtonEliminar(ActionEvent event) {
        //Obtener auto
        Automobil auto = tableViewCoches.getSelectionModel().getSelectedItem();
        if(new DAOmysql().eliminarAutomobil(auto)){
            new logica().mostrarDialeg(Alert.AlertType.CONFIRMATION, "Vehiculo eliminado", null, null);
        }else{
             new logica().mostrarDialeg(Alert.AlertType.ERROR, "Error", null, "No se ha podido eliminar el vehiculo");
        }
        recargaInfo();

    }

    @FXML
    private void handleButtonVolver(ActionEvent event) throws IOException {
        coordinador.setEscenaInicio();
    }

    private void recargaInfo() {
        ObservableList = FXCollections.observableList(new DAOmysql().obtenerTodosCochesDisponibles());
        ObservableList = new logica().eliminarVehiculosRetirados(ObservableList);
        ObservableList = new logica().ponNombreCombustible(ObservableList);
        tableViewCoches.setItems(ObservableList);
    }

    @FXML
    private void onActionButtonInfo(ActionEvent event) {
        try {
            new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Informacion", null, new flujosDatos().lee("/info/infoEliminarAutomobil.txt"));
        } catch (Exception a) {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "No se ha encontrado el archivo", null, "no se ha encontrado el archivo de texto con las instrucciones de uso");
        }
    }

}
