package vista;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import app.flujosDatos;
import controlador.coordinador;
import controlador.logica;
import static controlador.logica.cifraPsswd;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import modelo.DAOmysql;
import usuarios.Usuario;
import static vista.inicioController.hora;
import static vista.inicioController.minutos;
import static vista.inicioController.segundos;

/**
 * FXML Controller class
 *
 * @author a
 */
public class LoginController implements Initializable {

    @FXML
    private Button buttonOk;
    @FXML
    private TextField textFieldUsuario;
    @FXML
    private TextField textFieldContraseña;
    LocalDateTime now;
    @FXML
    private Label labelHora;
    @FXML
    private Button buttonInfo;
    @FXML
    private Button buttonSalir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    private void handleButtonOk(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        if (validaUsuario(textFieldUsuario.getText(), textFieldContraseña.getText())) {
            new coordinador().setEscenaInicio();
        } else {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "Usuario/contraseña incorrecto", null, "No se ha encontrado el usuario o la contraseña");
        }

    }

    private boolean validaUsuario(String user, String psswd) throws NoSuchAlgorithmException {
        //cifra la contraseña
        psswd = cifraPsswd(psswd);
        //crea un usuario y lo pasa a la funcion login para hacer la consulta en la bbdd
        return new DAOmysql().login(new Usuario(user, psswd));
    }
//esta funcion cifra la contraseña con MD5, es una funcion que he encontrao en internet

    @FXML
    private void onActionButtonInfo(ActionEvent event) {
        try {
            new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Informacion", null, new flujosDatos().lee("/info/infoLogin.txt"));
        } catch (Exception a) {
            new logica().mostrarDialeg(Alert.AlertType.ERROR, "No se ha encontrado el archivo", null, "no se ha encontrado el archivo de texto con las instrucciones de uso");
        }
    }

    @FXML
    private void handleButtonSalir(ActionEvent event) {
    new coordinador().salir();
    }
}
