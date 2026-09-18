package uam.com.ni.clientesolicitud.controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
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
    private TableColumn<Cliente, Void> colAcciones;

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

        configurarColumnaAcciones();

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

    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button();
            private final Button btnEliminar = new Button();
            private final HBox container = new HBox(6, btnEditar, btnEliminar);

            {
                container.setAlignment(Pos.CENTER);

                FontIcon iconEdit = new FontIcon("fas-edit");
                iconEdit.setIconSize(11);
                iconEdit.setIconColor(Color.web("#2563EB"));
                btnEditar.setGraphic(iconEdit);
                btnEditar.setStyle("-fx-background-color: #EFF6FF; -fx-border-color: #DBEAFE; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-padding: 4px 8px; -fx-cursor: hand;");
                btnEditar.setTooltip(new Tooltip("Editar Cliente"));

                FontIcon iconDelete = new FontIcon("fas-trash-alt");
                iconDelete.setIconSize(11);
                iconDelete.setIconColor(Color.web("#DC2626"));
                btnEliminar.setGraphic(iconDelete);
                btnEliminar.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #FECACA; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-padding: 4px 8px; -fx-cursor: hand;");
                btnEliminar.setTooltip(new Tooltip("Eliminar Cliente"));

                btnEditar.setOnAction(event -> {
                    Cliente c = getTableView().getItems().get(getIndex());
                    if (c != null) {
                        abrirEdicionCliente(c);
                    }
                });

                btnEliminar.setOnAction(event -> {
                    Cliente c = getTableView().getItems().get(getIndex());
                    if (c != null) {
                        confirmarYEliminarCliente(c);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
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

    private void abrirEdicionCliente(Cliente cliente) {
        FXMLLoader loader = SceneNavigator.cambiarPantalla(
                tblClientes,
                AppRoutes.REGISTRO,
                "Edición de Cliente - " + cliente.getNombreCompleto()
        );
        if (loader != null) {
            RegistroClienteController controller = loader.getController();
            controller.setClienteAEditar(cliente);
        }
    }

    @FXML
    private void onEditarClienteSeleccionadoAction() {
        Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.mostrarAdvertencia(
                    "Selección Requerida",
                    "Ningún cliente seleccionado",
                    "Por favor seleccione un cliente de la tabla para editar."
            );
            return;
        }
        abrirEdicionCliente(seleccionado);
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
        confirmarYEliminarCliente(seleccionado);
    }

    private void confirmarYEliminarCliente(Cliente cliente) {
        boolean confirmar = AlertUtil.mostrarConfirmacion(
                "Confirmar Eliminación",
                "¿Eliminar cliente " + cliente.getNombreCompleto() + "?",
                "Esta acción eliminará el registro y su solicitud asociada del sistema."
        );

        if (confirmar) {
            DataStore.eliminarCliente(cliente);
            actualizarContador();
            AlertUtil.mostrarInfo("Registro Eliminado", "Cliente eliminado con éxito", "El cliente ha sido retirado del sistema.");
        }
    }

    @FXML
    private void onBuscarPorCodigoDialogAction() {
        java.util.Optional<String> codigoOpt = AlertUtil.mostrarDialogoTexto(
                "Búsqueda de Expediente por Código",
                "Localizar Cliente por Identificador",
                "Ingrese el código del cliente (ej. CLI-1001):",
                ""
        );

        codigoOpt.ifPresent(codigo -> {
            if (codigo.trim().isEmpty()) return;
            java.util.Optional<Cliente> encontrado = DataStore.getClientes().stream()
                    .filter(c -> c.getId() != null && c.getId().equalsIgnoreCase(codigo.trim()))
                    .findFirst();

            if (encontrado.isPresent()) {
                tblClientes.getSelectionModel().select(encontrado.get());
                tblClientes.scrollTo(encontrado.get());
                abrirDetalleCliente(encontrado.get());
            } else {
                AlertUtil.mostrarAdvertencia(
                        "Cliente No Encontrado",
                        "Código no registrado",
                        "No se encontró ningún cliente registrado con el código: " + codigo.trim()
                );
            }
        });
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
