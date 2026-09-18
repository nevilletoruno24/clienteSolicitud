package uam.com.ni.clientesolicitud.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Componente reutilizable para tarjetas de métricas/estadísticas en el dashboard.
 * Implementa el patrón fx:root sobre VBox.
 */
@SuppressWarnings("unused")
public class StatCardComponent extends VBox {

    @FXML
    private Label lblTituloMetrica;

    @FXML
    private Label lblValorMetrica;

    private final StringProperty tituloMetrica = new SimpleStringProperty(this, "tituloMetrica", "Métrica");
    private final StringProperty valorMetrica = new SimpleStringProperty(this, "valorMetrica", "0");

    public StatCardComponent() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/uam/com/ni/clientesolicitud/view/components/stat-card-component.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar stat-card-component.fxml", e);
        }

        lblTituloMetrica.textProperty().bindBidirectional(tituloMetrica);
        lblValorMetrica.textProperty().bindBidirectional(valorMetrica);
    }

    public StatCardComponent(String titulo, String valorInicial) {
        this();
        setTituloMetrica(titulo);
        setValorMetrica(valorInicial);
    }

    public String getTituloMetrica() {
        return tituloMetrica.get();
    }

    public void setTituloMetrica(String titulo) {
        this.tituloMetrica.set(titulo);
    }

    public StringProperty tituloMetricaProperty() {
        return tituloMetrica;
    }

    public String getValorMetrica() {
        return valorMetrica.get();
    }

    public void setValorMetrica(String valor) {
        this.valorMetrica.set(valor);
    }

    public StringProperty valorMetricaProperty() {
        return valorMetrica;
    }
}
