package uam.com.ni.clientesolicitud.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {

    public static FXMLLoader cambiarPantalla(Node nodoActual, String rutaFxml) {
        return cambiarPantalla(nodoActual, rutaFxml, null);
    }

    public static FXMLLoader cambiarPantalla(Node nodoActual, String rutaFxml, String titulo) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneNavigator.class.getResource(rutaFxml));
            Parent nuevaRaiz = fxmlLoader.load();

            Scene escenaActual = nodoActual.getScene();
            escenaActual.setRoot(nuevaRaiz);

            if (titulo != null && escenaActual.getWindow() instanceof Stage stage) {
                stage.setTitle(titulo);
            }

            return fxmlLoader;
        } catch (IOException exception) {
            AlertUtil.mostrarError("Error de navegación", "No se pudo cargar la pantalla " + rutaFxml, exception.getMessage());
            return null;
        }
    }
}
