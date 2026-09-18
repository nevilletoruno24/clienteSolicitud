package uam.com.ni.clientesolicitud.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Utilería centralizada para la navegación y transición entre ventanas en JavaFX.
 * Facilita el cambio de vistas y la transferencia de datos entre controladores sin código duplicado.
 */
public class NavigationUtil {

    public static final String VIEW_BASE = "/uam/com/ni/clientesolicitud/view/";

    /**
     * Resuelve la ruta completa del FXML dentro de la carpeta view/.
     */
    private static String resolverRuta(String vista) {
        if (vista == null || vista.trim().isEmpty()) {
            return VIEW_BASE;
        }
        if (vista.startsWith("/")) {
            return vista;
        }
        return VIEW_BASE + vista;
    }

    /**
     * Obtiene el Stage activo a partir de un Event o de las ventanas visibles.
     */
    public static Stage obtenerStageActivo(Event event) {
        if (event != null && event.getSource() instanceof Node) {
            Scene scene = ((Node) event.getSource()).getScene();
            if (scene != null && scene.getWindow() instanceof Stage) {
                return (Stage) scene.getWindow();
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
     * Obtiene el Stage a partir de un nodo de la interfaz.
     */
    public static Stage obtenerStageDeNodo(Node nodo) {
        if (nodo != null && nodo.getScene() != null && nodo.getScene().getWindow() instanceof Stage) {
            return (Stage) nodo.getScene().getWindow();
        }
        return obtenerStageActivo(null);
    }

    /**
     * Carga y cambia la escena a partir de un nodo visual de origen.
     */
    public static FXMLLoader cambiarEscena(Node nodoOrigen, String fxmlName, String titulo, int ancho, int alto) {
        Stage stage = obtenerStageDeNodo(nodoOrigen);
        return cambiarEscena(stage, fxmlName, titulo, ancho, alto);
    }

    /**
     * Carga y cambia la escena a partir de un evento.
     */
    public static FXMLLoader cambiarEscena(Event event, String fxmlName, String titulo, int ancho, int alto) {
        Stage stage = obtenerStageActivo(event);
        return cambiarEscena(stage, fxmlName, titulo, ancho, alto);
    }

    /**
     * Carga una vista en el Stage indicado, actualiza escena y título, y retorna el FXMLLoader
     * para permitir el paso de datos al controlador de destino.
     */
    public static FXMLLoader cambiarEscena(Stage stage, String fxmlName, String titulo, int ancho, int alto) {
        String rutaCompleta = resolverRuta(fxmlName);
        try {
            if (stage == null) {
                stage = obtenerStageActivo(null);
                if (stage == null) {
                    stage = new Stage();
                }
            }

            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(rutaCompleta));
            Parent root = loader.load();

            Scene scene = new Scene(root, ancho, alto);
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            return loader;
        } catch (Throwable e) {
            System.err.println("=== ERROR EN NAVIGATION UTIL ===");
            System.err.println("Fallo al cargar la vista: " + rutaCompleta);
            e.printStackTrace();
            AlertUtil.mostrarError("Error de Navegación", "No se pudo cargar la vista: " + rutaCompleta, e.getMessage() != null ? e.getMessage() : e.toString());
            return null;
        }
    }
}
