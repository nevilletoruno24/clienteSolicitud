package uam.com.ni.clientesolicitud.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Componente reutilizable para encabezados de ventana con título, subtítulo y separador.
 * Implementa el patrón fx:root sobre VBox.
 */
public class HeaderComponent extends VBox {

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblSubtitulo;

    private final StringProperty titulo = new SimpleStringProperty(this, "titulo", "");
    private final StringProperty subtitulo = new SimpleStringProperty(this, "subtitulo", "");

    public HeaderComponent() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/uam/com/ni/clientesolicitud/view/components/header-component.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar header-component.fxml", e);
        }

        lblTitulo.textProperty().bindBidirectional(titulo);
        lblSubtitulo.textProperty().bindBidirectional(subtitulo);
    }

    public HeaderComponent(String titulo, String subtitulo) {
        this();
        setTitulo(titulo);
        setSubtitulo(subtitulo);
    }

    public String getTitulo() {
        return titulo.get();
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public String getSubtitulo() {
        return subtitulo.get();
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo.set(subtitulo);
    }

    public StringProperty subtituloProperty() {
        return subtitulo;
    }
}
