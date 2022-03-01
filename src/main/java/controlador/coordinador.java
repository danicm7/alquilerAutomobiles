/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static app.MainApp.stage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author a
 */
public class coordinador {
//esta clase es para navegar entre vistas

    public coordinador() {
    }

    public void setEscenaInicio() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inicio.fxml"));
        Scene escena = new Scene((Parent) loader.load());
        stage.setTitle("BRUUUUM!");
        stage.setScene(escena);
        //el show no hace falta, se cambia automaticamente
        //stage.show();
    }

    public void setEscenaHistorial() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/historial.fxml"));
        Scene escena = new Scene((Parent) loader.load());
        stage.setTitle("Historial");
        stage.setScene(escena);
    }

    public void setEscenaNuevoAutomobil() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/nuevoAutomobil.fxml"));
        Scene escena = new Scene((Parent) loader.load());
        stage.setTitle("Nuevo automobil");
        stage.setScene(escena);
    }

    public void setEscenaEliminarVehiculo() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eliminarAutomobil.fxml"));
        Scene escena = new Scene((Parent) loader.load());
        stage.setTitle("Eliminar vehiculo");
        stage.setScene(escena);
    }

    public void setEscenaDevolucion() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/finReservaAutomobil.fxml"));
        Scene escena = new Scene((Parent) loader.load());
        stage.setTitle("Finalizar Reserva");
        stage.setScene(escena);
    }

    public void setEscenaAnularReserva() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/verAlquileres.fxml"));
        Scene escena = new Scene((Parent) loader.load());
        stage.setTitle("Ver alquileres en curso");
        stage.setScene(escena);
    }

    public void setEscenaRegistrarUsuario() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegistrarUsuario.fxml"));
        Scene escena = new Scene((Parent) loader.load());
        stage.setTitle("Registrar Usuario");
        stage.setScene(escena);
    }

    public void setEscenaLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene escena = new Scene((Parent) loader.load());
        stage.setTitle("Login");
        stage.setScene(escena);
    }

    public void setEscenaVerVehiculosEliminados() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/autosEliminados.fxml"));
        Scene escena = new Scene((Parent) loader.load());
        stage.setTitle("Ver vehiculos eliminados");
        stage.setScene(escena);
    }

    public void salir() {
        Platform.exit();
        System.exit(0);
    }
}
