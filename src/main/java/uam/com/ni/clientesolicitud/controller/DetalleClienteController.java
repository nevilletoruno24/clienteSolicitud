package uam.com.ni.clientesolicitud.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uam.com.ni.clientesolicitud.model.Cliente;
import uam.com.ni.clientesolicitud.model.DataStore;
import uam.com.ni.clientesolicitud.util.AlertUtil;
import uam.com.ni.clientesolicitud.util.AppRoutes;
import uam.com.ni.clientesolicitud.util.SceneNavigator;

import java.util.Optional;

public class DetalleClienteController {

    @FXML
    private Label lblCodigo;

    @FXML
    private Label lblNombreCompleto;

    @FXML
    private Label lblTipoCliente;

    @FXML
    private Label lblCiudad;

    @FXML
    private Label lblFechaNacimiento;

    @FXML
    private Label lblTipoSolicitud;

    @FXML
    private ListView<String> lstServicios;

    @FXML
    private Label lblFechaRegistro;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private ImageView imgFoto;

    @FXML
    private Button btnVolverConsulta;

    private Cliente clienteActual;

    public void setCliente(Cliente cliente) {
        this.clienteActual = cliente;
        if (cliente == null) return;

        lblCodigo.setText(cliente.getId());
        lblNombreCompleto.setText(cliente.getNombreCompleto());
        lblTipoCliente.setText(cliente.getTipoCliente());
        lblCiudad.setText(cliente.getCiudad());
        lblFechaNacimiento.setText(cliente.getFechaNacimientoFormateada());
        lblTipoSolicitud.setText(cliente.getTipoSolicitud());
        if (cliente.getServiciosInteres() != null && !cliente.getServiciosInteres().isEmpty()) {
            lstServicios.setItems(FXCollections.observableArrayList(cliente.getServiciosInteres()));
        } else {
            lstServicios.setItems(FXCollections.observableArrayList("Sin servicios seleccionados"));
        }
        lblFechaRegistro.setText(cliente.getFechaRegistro() != null ? cliente.getFechaRegistro().toString() : "Hoy");
        txtObservaciones.setText(cliente.getObservaciones() != null ? cliente.getObservaciones() : "");

        if (cliente.getRutaFotografia() != null && !cliente.getRutaFotografia().isEmpty()) {
            try {
                imgFoto.setImage(new Image(cliente.getRutaFotografia()));
            } catch (Exception e) {
                imgFoto.setImage(null);
            }
        }
    }

    @FXML
    private void onEditarConDialogAction() {
        Optional<String> nuevaNota = AlertUtil.mostrarDialogoTexto(
                "Añadir Nota a la Solicitud",
                "Actualizar notas de expediente para " + clienteActual.getNombreCompleto(),
                "Ingrese el nuevo comentario u observación:",
                txtObservaciones.getText()
        );

        nuevaNota.ifPresent(nota -> {
            txtObservaciones.setText(nota);
            if (clienteActual != null) {
                clienteActual.setObservaciones(nota);
                DataStore.actualizarCliente(clienteActual);
                AlertUtil.mostrarInfo("Actualización", "Nota Guardada", "La observación del cliente ha sido actualizada.");
            }
        });
    }

    @FXML
    private void onGuardarCambiosAction() {
        if (clienteActual != null) {
            clienteActual.setObservaciones(txtObservaciones.getText());
            DataStore.actualizarCliente(clienteActual);
            AlertUtil.mostrarInfo("Cambios Guardados", "Expediente Actualizado", "Los cambios en las observaciones del cliente fueron guardados exitosamente.");
        }
    }

    @FXML
    private void onVolverConsultaAction() {
        SceneNavigator.cambiarPantalla(
                btnVolverConsulta,
                AppRoutes.CONSULTA,
                "Consulta y Administración de Clientes"
        );
    }
}
