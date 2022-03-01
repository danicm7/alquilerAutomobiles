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
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import modelo.Alquiler;
import modelo.DAOmysql;
import static vista.inicioController.hora;
import static vista.inicioController.minutos;
import static vista.inicioController.segundos;

/**
 * FXML Controller class
 *
 * @author a
 */
public class HistorialController implements Initializable {

    @FXML
    private TableView<Alquiler> TableViewHistorial;
    @FXML
    private TableColumn<Alquiler, String> columnCoche;
    @FXML
    private TableColumn<Alquiler, String> columnCliente;
    @FXML
    private TableColumn<Alquiler, String> columnFechaInicio;
    @FXML
    private TableColumn<Alquiler, String> columnFechaFin;
    private ObservableList ObservableList;
    @FXML
    private Button buttonVolver;
    @FXML
    private TableColumn<Alquiler, String> columnDni;
    @FXML
    private TableColumn<Alquiler, String> tableColumnObservaciones;
    @FXML
    private Label labelHora;
    private LocalDateTime now;
    @FXML
    private Button buttonInfo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        columnCoche.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("nombreMarca"));
        columnCliente.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("nombreCliente"));
        columnFechaInicio.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("fechaInicio"));
        columnFechaFin.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("fechaFin"));
        columnDni.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("dni"));
        tableColumnObservaciones.setCellValueFactory(new PropertyValueFactory<Alquiler, String>("observaciones"));
        recargaInfo();
        /* esto es para actualizar el reloj.
        Se hace con hilos pero de manera especial, con hilos solo peta porque javafx se ejecuta sobre 
        un hilo, y si se abre otro hilo, este se ejecuta dentro del anterior y se hace un bucle infinito.
        ademas, no se puede acceder a los elementos graficos de javaFX desde un hilo que no sea el principal.
        Para hacerlo mas complicado tambien hay que declarar los elementos de FXML como private, 
        asi que solo se pueden acceder desde el controlador.
        Tampoco podia usar expresiones lambda porque mi version de java era demasiado antigua
        Al final lo he hecho pasandole un objeto Task a un Thread, al objeto task le  sobreescibimos 
        el metodo call(), dentro del metodo call(), usamos la funcion Platform.runLater y le mandamos 
        un objeto Runable al que sobreescribimos la funcion run y escribimos aqui el codigo del hilo, 
        luego hacemos un sleep de 1 segundo para que el reloj se actualize cada segundo.
         */
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            now = LocalDateTime.now();
                            inicioController.hora = now.getHour();
                            inicioController.minutos = now.getMinute();
                            inicioController.segundos = now.getSecond();
                            labelHora.setText(String.valueOf(hora) + ":" + String.valueOf(minutos) + ":" + String.valueOf(segundos));
                        }
                    });
                    try {
                        Thread.sleep(1000); // Vamos a actualizar el reloj cada segundo
                    } catch (InterruptedException e) {
                    }
                }
            }
        ;
        };
        //inicia el hilo del reloj 
        new Thread(task).start();
        //crear ImageView para ponerlo en el boton
        ImageView imageView = new ImageView("/imagenes/iconoInfo.png");
        //poner el imageView en el boton
        buttonInfo.setGraphic(imageView);

    }

    @FXML
    private void handleButtonVolver(ActionEvent event) throws IOException {
        new coordinador().setEscenaInicio();
    }

    @FXML
    private void onMouseClickedTableViewHistorial(MouseEvent event) {
        new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Observaciones", null, TableViewHistorial.getSelectionModel().getSelectedItem().getObservaciones());
    }

    @FXML
    private void onActionButtonInfo(ActionEvent event) {
        try {
            new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Informacion", null, new flujosDatos().lee("/info/infoHistorial.txt"));
        } catch (Exception a) {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "No se ha encontrado el archivo", null, "no se ha encontrado el archivo de texto con las instrucciones de uso");
        }
    }

    private void recargaInfo() {
        ObservableList = FXCollections.observableList(new DAOmysql().ObtenerHistorial());
        /*//borramos las entradas del historial con fecha fin posterior a hoy.
        ObservableList = new logica().borraEntradasConFechaFinAnteriorHoy(ObservableList);*/
        //este metodo sustituyre % por R en los vehiculos que ya han sido retirados por la empresa
        ObservableList = new logica().sustituyeSimboloporR(ObservableList);
        TableViewHistorial.setItems(ObservableList);       
    }
}
