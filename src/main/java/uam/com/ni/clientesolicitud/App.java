package uam.com.ni.clientesolicitud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/uam/com/ni/clientesolicitud/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 580, 560);
        stage.setTitle("Sistema de Clientes y Solicitudes - Inicio de Sesión");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(460);
        stage.setMinHeight(500);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
