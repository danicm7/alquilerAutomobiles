package vista;

import static app.MainApp.stage;
import app.flujosDatos;
import controlador.coordinador;
import controlador.logica;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class inicioController implements Initializable {

    @FXML
    private Button buttonNuevaReserva;
    @FXML
    private Button ButtonHistorial;
    private coordinador coordinador;
    @FXML
    private MenuButton OpcionesMenuButton;
    @FXML
    private MenuItem ItemOpciones;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private MenuItem itemEliminarVehiculo;
    @FXML
    private Label labelHora;
    LocalDateTime localDateTime;
    public static int hora = 0, minutos = 0, segundos = 0;
    LocalDateTime now;
    @FXML
    private Button ButtonSalir;
    @FXML
    private Button buttonAnularReserva;
    @FXML
    private Button buttonInfo;
    @FXML
    private ImageView ImageView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        coordinador = new coordinador();

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
        //crear ImageView para ponerlo en el boton
        ImageView imageView = new ImageView("/imagenes/iconoInfo.png");
        //poner el imageView en el boton
        buttonInfo.setGraphic(imageView);
    }

    @FXML
    private void handlebuttonNuevaReserva(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/nuevaReserva.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Nueva reserva");
        stage.show();
    }

    @FXML
    private void handleButtonHistorial(ActionEvent event) throws IOException {
        coordinador.setEscenaHistorial();
    }

    @FXML
    private void handleItemOpciones(ActionEvent event) throws IOException {
        coordinador.setEscenaNuevoAutomobil();

    }

    @FXML
    private void handleItemEliminarVehiculo(ActionEvent event) throws IOException {
        coordinador.setEscenaEliminarVehiculo();
    }

    @FXML
    private void handleItemDevolucion(ActionEvent event) throws IOException {
        coordinador.setEscenaDevolucion();
    }

    @FXML
    private void handleButtonSalir(ActionEvent event) {
        coordinador.salir();
    }

    @FXML
    private void handleButtonAnularReserva(ActionEvent event) throws IOException {
        coordinador.setEscenaAnularReserva();
    }

    @FXML
    private void handleItemRegistrarUsuario(ActionEvent event) throws IOException {
        coordinador.setEscenaRegistrarUsuario();
    }

    @FXML
    private void onActionButtonInfo(ActionEvent event) {
        new logica().mostrarDialeg(Alert.AlertType.INFORMATION, "Informacion", null, new flujosDatos().lee("/info/infoInicio.txt"));
    }

    @FXML
    private void handleItemCambiarUsuario(ActionEvent event) throws IOException {
        coordinador.setEscenaLogin();
    }

    @FXML
    private void handleItemVerVehiculosEliminados(ActionEvent event) throws IOException {
        coordinador.setEscenaVerVehiculosEliminados();
    }

}
