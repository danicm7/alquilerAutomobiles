package app;

import controlador.coordinador;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {
//este objeto sirve para cambiar de ventana, es public/static para poder acceder a el en cualquier momento
    //y poder cambiar de ventana

    public static Stage stage;

    @Override
    public void start(Stage a) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.HAND);
        //scene.getStylesheets().add("/fxml/inicio.css");
        stage = a;
        //poner el icono de la aplicacion
        stage.getIcons().add(new Image("/imagenes/iconoAplicacion.png"));
        stage.centerOnScreen();
        //para que no se pueda cambiar el tamaño de la pestaña porque el fondo pantalla se queda clavado
        stage.setResizable(false);
        stage.setTitle("BRRUUUUUM!");
        stage.setScene(scene);
        stage.show();
        //al cargar la escena dos veces se recoloca la imagen de fondo bien
        new coordinador().setEscenaLogin();
        /*esto es un listener para cuando se cierra la app,
        lo que hace es parar todos los hilos, porque antes, se quedaba el hilo del reloj dando vueltas
         */
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                new coordinador().salir();
            }
        });
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("No se puede abrir la aplicacion javaFX");
    }

}
