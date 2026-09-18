package uam.com.ni.clientesolicitud.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Componente reutilizable para pie de página / barra de estado.
 * Implementa el patrón fx:root sobre VBox.
 */
public class FooterComponent extends VBox {

    @FXML
    private Label lblMensaje;

    @FXML
    private HBox boxContenedor;

    private final StringProperty mensaje = new SimpleStringProperty(this, "mensaje", "");

    public FooterComponent() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/uam/com/ni/clientesolicitud/view/components/footer-component.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar footer-component.fxml", e);
        }

        lblMensaje.textProperty().bindBidirectional(mensaje);
    }

    public FooterComponent(String mensaje) {
        this();
        setMensaje(mensaje);
    }

    public String getMensaje() {
        return mensaje.get();
    }

    public void setMensaje(String mensaje) {
        this.mensaje.set(mensaje);
    }

    public StringProperty mensajeProperty() {
        return mensaje;
    }

    public void setAlineacion(Pos pos) {
        if (boxContenedor != null) {
            boxContenedor.setAlignment(pos);
        }
    }
}
