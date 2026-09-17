package uam.com.ni.clientesolicitud.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uam.com.ni.clientesolicitud.model.Cliente;
import uam.com.ni.clientesolicitud.model.DataStore;
import uam.com.ni.clientesolicitud.util.AlertUtil;
import uam.com.ni.clientesolicitud.util.NavigationUtil;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistroClienteController {

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
    private RadioButton rbCuenta;

    @FXML
    private RadioButton rbReclamo;

    @FXML
    private RadioButton rbSoporte;

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
    private Button btnLimpiar;

    @FXML
    private Button btnCancelar;

    private String rutaFotoSeleccionada = "";

    @FXML
    public void initialize() {
        // Inicializar ComboBox de Tipo de Cliente
        cmbTipoCliente.setItems(FXCollections.observableArrayList(
                "Individual", "Corporativo", "VIP", "Gubernamental"
        ));

        // Inicializar ComboBox de Ciudad
        cmbCiudad.setItems(FXCollections.observableArrayList(
                "Managua", "León", "Granada", "Matagalpa", "Estelí", "Chinandega", "Masaya", "Rivas"
        ));

        // Evento de Teclado KeyEvent: Restringir que sólo se ingresen letras y espacios en nombres y apellidos
        txtNombres.addEventFilter(KeyEvent.KEY_TYPED, this::filtrarSoloLetras);
        txtApellidos.addEventFilter(KeyEvent.KEY_TYPED, this::filtrarSoloLetras);
    }

    private void filtrarSoloLetras(KeyEvent event) {
        char c = event.getCharacter().isEmpty() ? 0 : event.getCharacter().charAt(0);
        // Permitir letras, espacios y teclas de control
        if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != '\b' && c != '\r') {
            event.consume(); // Cancela el evento impidiendo ingresar números o símbolos especiales
        }
    }

    @FXML
    private void onSeleccionarFotoAction(ActionEvent event) {
        // FileChooser para seleccionar una fotografía
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
    private void onGuardarAction(ActionEvent event) {
        String nombres = txtNombres.getText() != null ? txtNombres.getText().trim() : "";
        String apellidos = txtApellidos.getText() != null ? txtApellidos.getText().trim() : "";
        String tipoCliente = cmbTipoCliente.getValue();
        String ciudad = cmbCiudad.getValue();
        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();

        // Validaciones requeridas
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
            AlertUtil.mostrarAdvertencia("Campo Incompleto", "Fecha de Nacimiento Requerida", "Por favor, seleccione la fecha de nacimiento.");
            dpFechaNacimiento.requestFocus();
            return;
        }

        if (fechaNacimiento.isAfter(LocalDate.now().minusYears(18))) {
            AlertUtil.mostrarAdvertencia("Validación de Edad", "Cliente menor de edad", "El cliente debe ser mayor de 18 años para realizar una solicitud.");
            return;
        }

        RadioButton seleccionado = (RadioButton) tgTipoSolicitud.getSelectedToggle();
        if (seleccionado == null) {
            AlertUtil.mostrarAdvertencia("Campo Incompleto", "Tipo de Solicitud Requerido", "Por favor, seleccione el tipo de solicitud.");
            return;
        }
        String tipoSolicitud = seleccionado.getText();

        // Servicios de interés (CheckBoxes)
        List<String> servicios = new ArrayList<>();
        if (chkBancaLinea.isSelected()) servicios.add(chkBancaLinea.getText());
        if (chkTarjetaCredito.isSelected()) servicios.add(chkTarjetaCredito.getText());
        if (chkSeguroVida.isSelected()) servicios.add(chkSeguroVida.getText());
        if (chkAsesoria.isSelected()) servicios.add(chkAsesoria.getText());

        String observaciones = txtObservaciones.getText() != null ? txtObservaciones.getText().trim() : "";

        // Crear y guardar el cliente en el almacén de datos
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

        // Alert de información de guardado exitoso
        AlertUtil.mostrarInfo(
                "Registro Exitoso",
                "Cliente Registrado",
                "Se ha registrado satisfactoriamente el cliente:\n" + nuevoCliente.getNombreCompleto() +
                "\nCódigo generado: " + nuevoCliente.getId() +
                "\n\nLos datos han sido transferidos al catálogo de clientes."
        );

        // Preguntar si desea ir a la consulta o registrar otro
        boolean irAConsulta = AlertUtil.mostrarConfirmacion(
                "Navegación",
                "¿Desea ver el catálogo de clientes ahora?",
                "Presione Aceptar para ir a la consulta o Cancelar para seguir registrando."
        );

        if (irAConsulta) {
            NavigationUtil.cambiarEscena(
                    (Stage) btnGuardar.getScene().getWindow(),
                    "/uam/com/ni/clientesolicitud/consulta-view.fxml",
                    "Consulta y Administración de Clientes",
                    950,
                    650
            );
        } else {
            limpiarFormulario();
        }
    }

    @FXML
    private void onLimpiarAction(ActionEvent event) {
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
    private void onCancelarAction(ActionEvent event) {
        NavigationUtil.cambiarEscena(
                (Stage) btnCancelar.getScene().getWindow(),
                "/uam/com/ni/clientesolicitud/menu-principal-view.fxml",
                "Sistema de Registro y Solicitudes - Menú Principal",
                900,
                650
        );
    }
}
