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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import modelo.Automobil;
import modelo.DAOmysql;
import org.hibernate.HibernateException;

/**
 * FXML Controller class
 *
 * @author a
 */
public class NuevoAutomobilController implements Initializable {

    @FXML
    private Button buttonGuardar;
    @FXML
    private TextField editTextMarca;
    @FXML
    private TextField editTextModelo;
    @FXML
    private TextField editTextMatricula;
    @FXML
    private TextField editTextNumRuedas;
    @FXML
    private TextField editTextAutonomia;
    @FXML
    private TextField editTextColor;
    @FXML
    private TextField editTextKm;
    @FXML
    private TextField editTextCombustible;
    @FXML
    private TextField editTextNumPlazas;
    @FXML
    private TextField editTextTipo;
    @FXML
    private TextField editTextEnUso;
    private String marca, modelo, matricula, color, tipo, enUsoString;
    private char combustible;
    private int numRuedas, Autonomia, Km, numPlazas;
    //es static para poder hacer las validaciones desde otra clase
    public static boolean enUso;
    private logica logica;
    @FXML
    private Button buttonVolver;
    @FXML
    private Button buttonInfo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logica = new logica();
        //crear ImageView para ponerlo en el boton
        ImageView imageView = new ImageView("/imagenes/iconoInfo.png");
        //poner el imageView en el boton
        buttonInfo.setGraphic(imageView);
    }

    @FXML
    private void handleButtonGuardar(ActionEvent event) throws IOException {
        marca = editTextMarca.getText();
        modelo = editTextModelo.getText();
        matricula = editTextMatricula.getText().toUpperCase();
        color = editTextColor.getText();
        tipo = editTextTipo.getText();
        try {
            combustible = editTextCombustible.getText().toUpperCase().charAt(0);
        } catch (StringIndexOutOfBoundsException asfgsd) {
            logica.mostrarDialeg(Alert.AlertType.ERROR, "Error", null, "Has olvidado rellenar el apartado de combustible");
            return;
        }
        try {
            numRuedas = Integer.parseInt(editTextNumRuedas.getText());
        } catch (java.lang.NumberFormatException frgnhdjn) {
            logica.mostrarDialeg(Alert.AlertType.ERROR, "Error", null, "Debes introducir SOLAMENTE digitos para indicar las ruedas del vehiculo");
            return;
        }
        try {
            Autonomia = Integer.parseInt(editTextAutonomia.getText());
        } catch (NumberFormatException sdfhjutri67kgh) {
            logica.mostrarDialeg(Alert.AlertType.ERROR, "Error", null, "Debes introducir SOLAMENTE digitos para indicar la autonomia del vehiculo");
            return;
        }
        try {
            Km = Integer.parseInt(editTextKm.getText());
        } catch (java.lang.NumberFormatException sghrfyngbv) {
            logica.mostrarDialeg(Alert.AlertType.ERROR, "Error", null, "Debes introducir SOLAMENTE digitos para indicar los kilometros de vehiculo");
            return;
        }
        try {
            numPlazas = Integer.parseInt(editTextNumPlazas.getText());
        } catch (NumberFormatException sdgsdthjdfj) {
            logica.mostrarDialeg(Alert.AlertType.ERROR, "Error", null, "Debes introducir SOLAMENTE digitos para indicar el numero de plazas del vehiculo");
            return;
        }
        try {
            //se usa este string para hacer la validacion en la clase logica
            //como la varibale es static desde la clase logica se le pone valor cuando se hace la validacion del
            //de los datos del coche, sino se pasa la validacion, no se guardan los datos con lo cual no hay problema
            enUsoString = editTextEnUso.getText().toLowerCase();
        } catch (StringIndexOutOfBoundsException adsgfhj) {
            logica.mostrarDialeg(Alert.AlertType.ERROR, "Error", null, "Debes introducir SOLAMENTE Y o N para indicar si el vehiculo esta disponible");
            return;
        } catch (Exception generica) {
            logica.mostrarDialeg(Alert.AlertType.ERROR, "Error", null, "Debes introducir SOLAMENTE Y o N para indicar si el vehiculo esta disponible");
            return;
        }
        if (logica.validaCoche(marca, modelo, matricula, color, tipo, combustible, numRuedas, Autonomia, Km, numPlazas, enUsoString)) {
            try {
                new DAOmysql().guardaAutomobil(new Automobil(marca, modelo, matricula, numRuedas, Autonomia, color, Km, combustible, numPlazas, tipo, enUso));
                new logica().mostrarDialeg(Alert.AlertType.CONFIRMATION, "Nuevo vehiculo Guardado", null, "Nuevo vehiculo guardado correctamente");
                new coordinador().setEscenaInicio();
            } catch (HibernateException a) {
                logica.mostrarDialeg(Alert.AlertType.ERROR, "Error", null, "Error al guardar el coche en la BBDD");
                return;
            }
        }
    }

    @FXML
    private void handleButtonVolver(ActionEvent event) throws IOException {
        new coordinador().setEscenaInicio();
    }

    @FXML
    private void onActionButtonInfo(ActionEvent event) {
        try {
            new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Informacion", null, new flujosDatos().lee("/info/infoNuevoAutomobil.txt"));
        } catch (Exception a) {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "No se ha encontrado el archivo", null, "no se ha encontrado el archivo de texto con las instrucciones de uso");
        }
    }
}
