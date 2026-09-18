package uam.com.ni.clientesolicitud.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import uam.com.ni.clientesolicitud.model.Cliente;
import uam.com.ni.clientesolicitud.model.DataStore;
import uam.com.ni.clientesolicitud.util.AlertUtil;
import uam.com.ni.clientesolicitud.util.AppRoutes;
import uam.com.ni.clientesolicitud.util.SceneNavigator;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class RegistroClienteController {

    @FXML
    private Label lblTituloPantalla;

    @FXML
    private Label lblSubtituloPantalla;

    @FXML
    private TextField txtNombres;

    @FXML
    private TextField txtApellidos;

    @FXML
    private ComboBox<String> cmbTipoCliente;

    @FXML
    private ComboBox<String> cmbCiudad;

    @FXML
    private DatePicker dpFechaNacimiento;

    @FXML
    private ToggleGroup tgTipoSolicitud;

    @FXML
    private RadioButton rbCredito;

    @FXML
    private CheckBox chkBancaLinea;

    @FXML
    private CheckBox chkTarjetaCredito;

    @FXML
    private CheckBox chkSeguroVida;

    @FXML
    private CheckBox chkAsesoria;

    @FXML
    private ImageView imgFoto;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private Button btnSeleccionarFoto;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    private String rutaFotoSeleccionada = "";
    private Cliente clienteAEditar;

    public void setClienteAEditar(Cliente cliente) {
        this.clienteAEditar = cliente;
        if (cliente == null) return;

        if (lblTituloPantalla != null) {
            lblTituloPantalla.setText("Edición de Cliente: " + cliente.getId());
        }
        if (lblSubtituloPantalla != null) {
            lblSubtituloPantalla.setText("Modifique los campos necesarios y guarde los cambios en el sistema.");
        }
        btnGuardar.setText("Guardar Cambios");

        txtNombres.setText(cliente.getNombres());
        txtApellidos.setText(cliente.getApellidos());
        cmbTipoCliente.setValue(cliente.getTipoCliente());
        cmbCiudad.setValue(cliente.getCiudad());
        dpFechaNacimiento.setValue(cliente.getFechaNacimiento());

        for (Toggle toggle : tgTipoSolicitud.getToggles()) {
            if (toggle instanceof RadioButton rb && rb.getText().equalsIgnoreCase(cliente.getTipoSolicitud())) {
                toggle.setSelected(true);
                break;
            }
        }

        List<String> servicios = cliente.getServiciosInteres() != null ? cliente.getServiciosInteres() : new ArrayList<>();
        chkBancaLinea.setSelected(servicios.contains(chkBancaLinea.getText()));
        chkTarjetaCredito.setSelected(servicios.contains(chkTarjetaCredito.getText()));
        chkSeguroVida.setSelected(servicios.contains(chkSeguroVida.getText()));
        chkAsesoria.setSelected(servicios.contains(chkAsesoria.getText()));

        rutaFotoSeleccionada = cliente.getRutaFotografia() != null ? cliente.getRutaFotografia() : "";
        if (!rutaFotoSeleccionada.isEmpty()) {
            try {
                imgFoto.setImage(new Image(rutaFotoSeleccionada));
            } catch (Exception e) {
                imgFoto.setImage(null);
            }
        }

        txtObservaciones.setText(cliente.getObservaciones() != null ? cliente.getObservaciones() : "");
    }

    @FXML
    public void initialize() {
        cmbTipoCliente.setItems(FXCollections.observableArrayList(
                "Individual", "Corporativo", "VIP", "Gubernamental"
        ));

        cmbCiudad.setItems(FXCollections.observableArrayList(
                "Managua", "León", "Granada", "Matagalpa", "Estelí", "Chinandega", "Masaya", "Rivas"
        ));

        txtNombres.addEventFilter(KeyEvent.KEY_TYPED, this::filtrarSoloLetras);
        txtApellidos.addEventFilter(KeyEvent.KEY_TYPED, this::filtrarSoloLetras);

        configurarDatePicker();
    }

    private void configurarDatePicker() {
        DateTimeFormatter dtfSlash = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtfHyphen = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        dpFechaNacimiento.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dtfSlash.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.trim().isEmpty()) {
                    try {
                        return LocalDate.parse(string.trim(), dtfSlash);
                    } catch (DateTimeParseException e) {
                        try {
                            return LocalDate.parse(string.trim(), dtfHyphen);
                        } catch (DateTimeParseException ex) {
                            return null;
                        }
                    }
                }
                return null;
            }
        });

        dpFechaNacimiento.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                String texto = dpFechaNacimiento.getEditor().getText();
                LocalDate parsed = dpFechaNacimiento.getConverter().fromString(texto);
                if (parsed == null && texto != null && !texto.trim().isEmpty()) {
                    dpFechaNacimiento.getEditor().clear();
                    dpFechaNacimiento.setValue(null);
                }
            }
        });
    }

    private void filtrarSoloLetras(KeyEvent event) {
        char c = event.getCharacter().isEmpty() ? 0 : event.getCharacter().charAt(0);
        if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != '\b' && c != '\r') {
            event.consume();
        }
    }

    @FXML
    private void onSeleccionarFotoAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Fotografía del Cliente");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        Stage stage = (Stage) btnSeleccionarFoto.getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            try {
                Image imagen = new Image(archivo.toURI().toString());
                imgFoto.setImage(imagen);
                rutaFotoSeleccionada = archivo.toURI().toString();
            } catch (Exception e) {
                AlertUtil.mostrarError("Error de Imagen", "No se pudo cargar la fotografía", e.getMessage());
            }
        }
    }

    @FXML
    private void onGuardarAction() {
        String nombres = txtNombres.getText() != null ? txtNombres.getText().trim() : "";
        String apellidos = txtApellidos.getText() != null ? txtApellidos.getText().trim() : "";
        String tipoCliente = cmbTipoCliente.getValue();
        String ciudad = cmbCiudad.getValue();
        try {
            dpFechaNacimiento.commitValue();
        } catch (Exception ignored) {
            dpFechaNacimiento.setValue(null);
        }
        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();

        if (nombres.isEmpty() || apellidos.isEmpty()) {
            AlertUtil.mostrarAdvertencia("Campos Incompletos", "Datos Personales Requeridos", "Por favor, complete los nombres y apellidos del cliente.");
            txtNombres.requestFocus();
            return;
        }

        if (tipoCliente == null || tipoCliente.trim().isEmpty()) {
            AlertUtil.mostrarAdvertencia("Campo Incompleto", "Tipo de Cliente Requerido", "Por favor, seleccione el tipo de cliente en la lista.");
            cmbTipoCliente.requestFocus();
            return;
        }

        if (ciudad == null || ciudad.trim().isEmpty()) {
            AlertUtil.mostrarAdvertencia("Campo Incompleto", "Ciudad Requerida", "Por favor, seleccione la ciudad del cliente.");
            cmbCiudad.requestFocus();
            return;
        }

        if (fechaNacimiento == null) {
            AlertUtil.mostrarAdvertencia("Campo Incompleto", "Fecha de Nacimiento Inválida", "Por favor, seleccione o ingrese una fecha de nacimiento válida (dd/MM/yyyy).");
            dpFechaNacimiento.requestFocus();
            return;
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            AlertUtil.mostrarAdvertencia("Fecha Inválida", "Fecha en el futuro", "La fecha de nacimiento no puede ser posterior a la fecha actual.");
            dpFechaNacimiento.requestFocus();
            return;
        }

        if (fechaNacimiento.isAfter(LocalDate.now().minusYears(18))) {
            AlertUtil.mostrarAdvertencia("Validación de Edad", "Cliente menor de edad", "El cliente debe ser mayor de 18 años para realizar una solicitud.");
            dpFechaNacimiento.requestFocus();
            return;
        }

        RadioButton seleccionado = (RadioButton) tgTipoSolicitud.getSelectedToggle();
        if (seleccionado == null) {
            AlertUtil.mostrarAdvertencia("Campo Incompleto", "Tipo de Solicitud Requerido", "Por favor, seleccione el tipo de solicitud.");
            return;
        }
        String tipoSolicitud = seleccionado.getText();

        List<String> servicios = new ArrayList<>();
        if (chkBancaLinea.isSelected()) servicios.add(chkBancaLinea.getText());
        if (chkTarjetaCredito.isSelected()) servicios.add(chkTarjetaCredito.getText());
        if (chkSeguroVida.isSelected()) servicios.add(chkSeguroVida.getText());
        if (chkAsesoria.isSelected()) servicios.add(chkAsesoria.getText());

        String observaciones = txtObservaciones.getText() != null ? txtObservaciones.getText().trim() : "";

        if (clienteAEditar != null) {
            clienteAEditar.setNombres(nombres);
            clienteAEditar.setApellidos(apellidos);
            clienteAEditar.setTipoCliente(tipoCliente);
            clienteAEditar.setCiudad(ciudad);
            clienteAEditar.setFechaNacimiento(fechaNacimiento);
            clienteAEditar.setTipoSolicitud(tipoSolicitud);
            clienteAEditar.setServiciosInteres(servicios);
            clienteAEditar.setRutaFotografia(rutaFotoSeleccionada);
            clienteAEditar.setObservaciones(observaciones);

            DataStore.actualizarCliente(clienteAEditar);

            AlertUtil.mostrarInfo(
                    "Actualización Exitosa",
                    "Cliente Actualizado",
                    "Se han actualizado correctamente todos los datos del cliente:\n" + clienteAEditar.getNombreCompleto() +
                    "\nCódigo: " + clienteAEditar.getId()
            );

            SceneNavigator.cambiarPantalla(
                    btnGuardar,
                    AppRoutes.CONSULTA,
                    "Consulta y Administración de Clientes"
            );
            return;
        }

        Cliente nuevoCliente = new Cliente(
                null,
                nombres,
                apellidos,
                tipoCliente,
                ciudad,
                fechaNacimiento,
                tipoSolicitud,
                servicios,
                rutaFotoSeleccionada,
                observaciones
        );

        DataStore.agregarCliente(nuevoCliente);

        AlertUtil.mostrarInfo(
                "Registro Exitoso",
                "Cliente Registrado",
                "Se ha registrado satisfactoriamente el cliente:\n" + nuevoCliente.getNombreCompleto() +
                "\nCódigo generado: " + nuevoCliente.getId() +
                "\n\nLos datos han sido transferidos al catálogo de clientes."
        );

        boolean irAConsulta = AlertUtil.mostrarConfirmacion(
                "Navegación",
                "¿Desea ver el catálogo de clientes ahora?",
                "Presione Aceptar para ir a la consulta o Cancelar para seguir registrando."
        );

        if (irAConsulta) {
            SceneNavigator.cambiarPantalla(
                    btnGuardar,
                    AppRoutes.CONSULTA,
                    "Consulta y Administración de Clientes"
            );
        } else {
            limpiarFormulario();
        }
    }

    @FXML
    private void onLimpiarAction() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtNombres.clear();
        txtApellidos.clear();
        cmbTipoCliente.setValue(null);
        cmbCiudad.setValue(null);
        dpFechaNacimiento.setValue(null);
        rbCredito.setSelected(true);
        chkBancaLinea.setSelected(false);
        chkTarjetaCredito.setSelected(false);
        chkSeguroVida.setSelected(false);
        chkAsesoria.setSelected(false);
        imgFoto.setImage(null);
        rutaFotoSeleccionada = "";
        txtObservaciones.clear();
        txtNombres.requestFocus();
    }

    @FXML
    private void onCancelarAction() {
        if (clienteAEditar != null) {
            SceneNavigator.cambiarPantalla(
                    btnCancelar,
                    AppRoutes.CONSULTA,
                    "Consulta y Administración de Clientes"
            );
        } else {
            SceneNavigator.cambiarPantalla(
                    btnCancelar,
                    AppRoutes.MENU_PRINCIPAL,
                    "Sistema de Registro y Solicitudes - Menú Principal"
            );
        }
    }
}
