package uam.com.ni.clientesolicitud.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Utilería para la navegación y transición entre ventanas en JavaFX.
 */
public class NavigationUtil {

    /**
     * Obtiene el Stage activo a partir de un evento o de las ventanas abiertas.
     */
    public static Stage obtenerStageActivo(Event event) {
        if (event != null) {
            Object source = event.getSource();
            if (source instanceof Node) {
                Scene scene = ((Node) source).getScene();
                if (scene != null && scene.getWindow() instanceof Stage) {
                    return (Stage) scene.getWindow();
                }
            }
        }
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage && window.isShowing()) {
                return (Stage) window;
            }
        }
        return null;
    }

    /**
     * Carga y muestra una nueva vista reemplazando la escena del Stage actual a partir de un evento.
     */
    public static FXMLLoader cambiarEscena(Event event, String rutaFxml, String titulo, int ancho, int alto) {
        Stage stage = obtenerStageActivo(event);
        return cambiarEscena(stage, rutaFxml, titulo, ancho, alto);
    }

    /**
     * Carga y muestra una vista en un Stage específico.
     */
    public static FXMLLoader cambiarEscena(Stage stage, String rutaFxml, String titulo, int ancho, int alto) {
        try {
            if (stage == null) {
                stage = obtenerStageActivo(null);
                if (stage == null) {
                    stage = new Stage();
                }
            }

            System.out.println("Cargando FXML: " + rutaFxml);
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(rutaFxml));
            Parent root = loader.load();
            System.out.println("FXML cargado correctamente: " + rutaFxml);

            Scene scene = new Scene(root, ancho, alto);
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            System.out.println("Escena mostrada en Stage.");
            return loader;
        } catch (Throwable e) {
            System.err.println("=== ERROR EN NAVIGATION UTIL ===");
            System.err.println("Fallo al cargar: " + rutaFxml);
            e.printStackTrace();
            AlertUtil.mostrarError("Error de Navegación", "No se pudo cargar la vista: " + rutaFxml, e.getMessage() != null ? e.getMessage() : e.toString());
            return null;
        }
    }
}
