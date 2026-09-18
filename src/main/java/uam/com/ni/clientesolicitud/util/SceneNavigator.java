package uam.com.ni.clientesolicitud.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {

    public static final int ANCHO_ESTANDAR = 920;
    public static final int ALTO_ESTANDAR = 640;

    public static FXMLLoader cambiarPantalla(Node nodoActual, String rutaFxml, String titulo) {
        return cambiarPantalla(nodoActual, rutaFxml, titulo, -1, -1);
    }

    public static FXMLLoader cambiarPantalla(Node nodoActual, String rutaFxml, String titulo, int ancho, int alto) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneNavigator.class.getResource(rutaFxml));
            Parent nuevaRaiz = fxmlLoader.load();

            Scene escenaActual = nodoActual.getScene();
            escenaActual.setRoot(nuevaRaiz);

            if (escenaActual.getWindow() instanceof Stage stage) {
                if (titulo != null) {
                    stage.setTitle(titulo);
                }
                if (ancho > 0 && alto > 0) {
                    stage.setWidth(ancho);
                    stage.setHeight(alto);
                    stage.centerOnScreen();
                } else if (stage.getWidth() < 800 && !rutaFxml.contains("login")) {
                    stage.setWidth(ANCHO_ESTANDAR);
                    stage.setHeight(ALTO_ESTANDAR);
                    stage.setMinWidth(850);
                    stage.setMinHeight(580);
                    stage.centerOnScreen();
                } else if (rutaFxml.contains("login")) {
                    stage.setWidth(580);
                    stage.setHeight(560);
                    stage.setMinWidth(460);
                    stage.setMinHeight(500);
                    stage.centerOnScreen();
                }
            }

            return fxmlLoader;
        } catch (IOException exception) {
            AlertUtil.mostrarError("Error de navegación", "No se pudo cargar la pantalla: " + rutaFxml, exception.getMessage());
            return null;
        }
    }
}
