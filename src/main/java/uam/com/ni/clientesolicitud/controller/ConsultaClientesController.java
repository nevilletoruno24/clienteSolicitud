package uam.com.ni.clientesolicitud.controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import uam.com.ni.clientesolicitud.model.Cliente;
import uam.com.ni.clientesolicitud.model.DataStore;
import uam.com.ni.clientesolicitud.util.AlertUtil;
import uam.com.ni.clientesolicitud.util.AppRoutes;
import uam.com.ni.clientesolicitud.util.SceneNavigator;

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
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreCompleto.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colTipoCliente.setCellValueFactory(new PropertyValueFactory<>("tipoCliente"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimientoFormateada"));
        colTipoSolicitud.setCellValueFactory(new PropertyValueFactory<>("tipoSolicitud"));
        colServicios.setCellValueFactory(new PropertyValueFactory<>("serviciosInteresTexto"));

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

        tblClientes.setOnMouseClicked(this::manejarDobleClicTabla);
        tblClientes.setOnKeyPressed(this::manejarTecladoTabla);
    }

    private void actualizarContador() {
        lblContador.setText("Mostrando " + clientesFiltrados.size() + " de " + DataStore.getClientes().size() + " clientes");
    }

    private void manejarDobleClicTabla(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                abrirDetalleCliente(seleccionado);
            }
        }
    }

    private void manejarTecladoTabla(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                abrirDetalleCliente(seleccionado);
            }
        } else if (event.getCode() == KeyCode.DELETE) {
            onEliminarClienteAction();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            onVolverMenuAction();
        }
    }

    @FXML
    private void onVerDetalleAction() {
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

    private void abrirDetalleCliente(Cliente cliente) {
        FXMLLoader loader = SceneNavigator.cambiarPantalla(
                tblClientes,
                AppRoutes.DETALLE,
                "Detalle de Cliente y Solicitud - " + cliente.getNombreCompleto()
        );
        if (loader != null) {
            DetalleClienteController controller = loader.getController();
            controller.setCliente(cliente);
        }
    }

    @FXML
    private void onNuevoClienteAction() {
        SceneNavigator.cambiarPantalla(
                tblClientes,
                AppRoutes.REGISTRO,
                "Registro de Clientes y Solicitudes"
        );
    }

    @FXML
    private void onEliminarClienteAction() {
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
    private void onVolverMenuAction() {
        SceneNavigator.cambiarPantalla(
                tblClientes,
                AppRoutes.MENU_PRINCIPAL,
                "Sistema de Registro y Solicitudes - Menú Principal"
        );
    }
}
