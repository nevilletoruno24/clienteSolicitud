package uam.com.ni.clientesolicitud.controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import uam.com.ni.clientesolicitud.model.Cliente;
import uam.com.ni.clientesolicitud.model.DataStore;
import uam.com.ni.clientesolicitud.util.AlertUtil;
import uam.com.ni.clientesolicitud.util.NavigationUtil;

import java.io.IOException;

public class ConsultaClientesController {

    @FXML
    private TableView<Cliente> tblClientes;

    @FXML
    private TableColumn<Cliente, String> colId;

    @FXML
    private TableColumn<Cliente, String> colNombreCompleto;

    @FXML
    private TableColumn<Cliente, String> colTipoCliente;

    @FXML
    private TableColumn<Cliente, String> colCiudad;

    @FXML
    private TableColumn<Cliente, String> colFechaNacimiento;

    @FXML
    private TableColumn<Cliente, String> colTipoSolicitud;

    @FXML
    private TableColumn<Cliente, String> colServicios;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblContador;

    private FilteredList<Cliente> clientesFiltrados;

    @FXML
    public void initialize() {
        // Configurar columnas con PropertyValueFactory
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreCompleto.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colTipoCliente.setCellValueFactory(new PropertyValueFactory<>("tipoCliente"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimientoFormateada"));
        colTipoSolicitud.setCellValueFactory(new PropertyValueFactory<>("tipoSolicitud"));
        colServicios.setCellValueFactory(new PropertyValueFactory<>("serviciosInteresTexto"));

        // Enlazar datos con FilteredList para búsqueda dinámica
        clientesFiltrados = new FilteredList<>(DataStore.getClientes(), b -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            clientesFiltrados.setPredicate(cliente -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String busqueda = newValue.toLowerCase().trim();
                return cliente.getNombreCompleto().toLowerCase().contains(busqueda)
                        || cliente.getTipoCliente().toLowerCase().contains(busqueda)
                        || cliente.getCiudad().toLowerCase().contains(busqueda)
                        || cliente.getTipoSolicitud().toLowerCase().contains(busqueda)
                        || cliente.getId().toLowerCase().contains(busqueda);
            });
            actualizarContador();
        });

        SortedList<Cliente> clientesOrdenados = new SortedList<>(clientesFiltrados);
        clientesOrdenados.comparatorProperty().bind(tblClientes.comparatorProperty());
        tblClientes.setItems(clientesOrdenados);

        actualizarContador();

        // Evento MouseEvent: Doble clic en una fila del TableView para ver detalle
        tblClientes.setOnMouseClicked(this::manejarDobleClicTabla);

        // Evento KeyEvent: ENTER para ver detalle, DELETE para eliminar fila seleccionada, ESC volver
        tblClientes.setOnKeyPressed(this::manejarTecladoTabla);
    }

    private void actualizarContador() {
        lblContador.setText("Mostrando " + clientesFiltrados.size() + " de " + DataStore.getClientes().size() + " clientes");
    }

    /**
     * MouseEvent requerido: Al hacer doble clic sobre un cliente, se abre el detalle.
     */
    private void manejarDobleClicTabla(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                abrirDetalleCliente(seleccionado);
            }
        }
    }

    /**
     * KeyEvent requerido: Enter abre detalle, Delete elimina registro, Esc vuelve al menú.
     */
    private void manejarTecladoTabla(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                abrirDetalleCliente(seleccionado);
            }
        } else if (event.getCode() == KeyCode.DELETE) {
            onEliminarClienteAction(null);
        } else if (event.getCode() == KeyCode.ESCAPE) {
            onVolverMenuAction(null);
        }
    }

    @FXML
    private void onVerDetalleAction(ActionEvent event) {
        Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.mostrarAdvertencia(
                    "Selección Requerida",
                    "Ningún cliente seleccionado",
                    "Por favor seleccione un cliente de la tabla o haga doble clic sobre él para ver el detalle."
            );
            return;
        }
        abrirDetalleCliente(seleccionado);
    }

    /**
     * Demostración requerida del paso de datos entre ventanas.
     */
    private void abrirDetalleCliente(Cliente cliente) {
        try {
            Stage stage = (Stage) tblClientes.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uam/com/ni/clientesolicitud/detalle-view.fxml"));
            Parent root = loader.load();

            // Transferencia de datos al controlador de detalle
            DetalleClienteController controller = loader.getController();
            controller.setCliente(cliente);

            Scene scene = new Scene(root, 800, 600);
            stage.setTitle("Detalle de Cliente y Solicitud - " + cliente.getNombreCompleto());
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.mostrarError("Error al Abrir Detalle", "No se pudo cargar la vista de detalle", e.getMessage());
        }
    }

    @FXML
    private void onNuevoClienteAction(ActionEvent event) {
        NavigationUtil.cambiarEscena(
                (Stage) tblClientes.getScene().getWindow(),
                "/uam/com/ni/clientesolicitud/registro-view.fxml",
                "Registro de Clientes y Solicitudes",
                750,
                650
        );
    }

    @FXML
    private void onEliminarClienteAction(ActionEvent event) {
        Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.mostrarAdvertencia(
                    "Selección Requerida",
                    "Ningún cliente seleccionado",
                    "Por favor seleccione un cliente de la tabla para eliminar."
            );
            return;
        }

        boolean confirmar = AlertUtil.mostrarConfirmacion(
                "Confirmar Eliminación",
                "¿Eliminar cliente " + seleccionado.getNombreCompleto() + "?",
                "Esta acción eliminará el registro y su solicitud asociada del sistema."
        );

        if (confirmar) {
            DataStore.eliminarCliente(seleccionado);
            actualizarContador();
            AlertUtil.mostrarInfo("Registro Eliminado", "Cliente eliminado con éxito", "El cliente ha sido retirado del sistema.");
        }
    }

    @FXML
    private void onVolverMenuAction(ActionEvent event) {
        NavigationUtil.cambiarEscena(
                (Stage) tblClientes.getScene().getWindow(),
                "/uam/com/ni/clientesolicitud/menu-principal-view.fxml",
                "Sistema de Registro y Solicitudes - Menú Principal",
                800,
                600
        );
    }
}
