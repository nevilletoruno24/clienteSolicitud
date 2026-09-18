package uam.com.ni.clientesolicitud.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;


public class AlertUtil {

    public static void mostrarInfo(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public static void mostrarAdvertencia(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public static void mostrarError(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public static boolean mostrarConfirmacion(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    public static Optional<String> mostrarDialogoTexto(String titulo, String encabezado, String mensaje, String valorPorDefecto) {
        TextInputDialog dialog = new TextInputDialog(valorPorDefecto);
        dialog.setTitle(titulo);
        dialog.setHeaderText(encabezado);
        dialog.setContentText(mensaje);
        return dialog.showAndWait();
    }
}
