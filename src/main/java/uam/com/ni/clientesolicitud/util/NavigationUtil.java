package uam.com.ni.clientesolicitud.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class NavigationUtil {

    private static final String RUTA_BASE = "/uam/com/ni/clientesolicitud/view/";

    public static Stage obtenerStageActivo(Event event) {
        if (event != null && event.getSource() instanceof Node node && node.getScene() != null) {
            return (Stage) node.getScene().getWindow();
        }
        for (Window w : Window.getWindows()) {
            if (w instanceof Stage s && s.isShowing()) {
                return s;
            }
        }
        return null;
    }

    public static FXMLLoader cambiarEscena(Object origen, String vista, String titulo, int ancho, int alto) {
        Node node = null;
        if (origen instanceof Node n) {
            node = n;
        } else if (origen instanceof Event event && event.getSource() instanceof Node n) {
            node = n;
        }

        String ruta = vista.startsWith("/") ? vista : RUTA_BASE + vista;

        if (node != null && node.getScene() != null) {
            Stage stage = (Stage) node.getScene().getWindow();
            if (stage != null) {
                stage.setWidth(ancho);
                stage.setHeight(alto);
                stage.centerOnScreen();
            }
            return SceneNavigator.cambiarPantalla(node, ruta, titulo);
        }

        Stage stage = (origen instanceof Stage s) ? s : obtenerStageActivo(null);
        if (stage == null) {
            stage = new Stage();
        }
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(ruta));
            Parent root = loader.load();
            stage.setTitle(titulo);
            Scene scene = stage.getScene();
            if (scene == null) {
                stage.setScene(new Scene(root, ancho, alto));
            } else {
                scene.setRoot(root);
                stage.setWidth(ancho);
                stage.setHeight(alto);
            }
            stage.centerOnScreen();
            if (!stage.isShowing()) {
                stage.show();
            }
            return loader;
        } catch (Exception e) {
            AlertUtil.mostrarError("Error de Navegación", "No se pudo cargar la vista solicitada.", e.getMessage());
            return null;
        }
    }
}
