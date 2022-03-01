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
import modelo.Automobil;
import modelo.DAOmysql;
import static vista.inicioController.hora;
import static vista.inicioController.minutos;
import static vista.inicioController.segundos;

/**
 * FXML Controller class
 *
 * @author a
 */
public class AutosEliminadosController implements Initializable {

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
    LocalDateTime now;
    @FXML
    private Label labelHora;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // TODO
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
        new Thread(task).start();
    }

    @FXML
    private void handleButtonVolver(ActionEvent event) throws IOException {
        coordinador.setEscenaInicio();
    }

    @FXML
    private void onActionButtonInfo(ActionEvent event) {
        try {
            new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Informacion", null, new flujosDatos().lee("/info/infoAutosEliminados.txt"));
        } catch (Exception a) {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "No se ha encontrado el archivo", null, "no se ha encontrado el archivo de texto con las instrucciones de uso");
        }
    }

    private void recargaInfo() {
        ObservableList = FXCollections.observableList(new DAOmysql().obtenerTodosCoches());
        ObservableList = new logica().borraCochesNoEliminados(ObservableList);
        ObservableList = new logica().ponNombreCombustible(ObservableList);
        tableViewCoches.setItems(ObservableList);
    }

}
