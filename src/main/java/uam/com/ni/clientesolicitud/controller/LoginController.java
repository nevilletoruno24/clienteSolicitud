package uam.com.ni.clientesolicitud.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import uam.com.ni.clientesolicitud.util.NavigationUtil;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private HBox boxError;

    @FXML
    private Label lblError;

    private boolean navegando = false;

    @FXML
    public void initialize() {
        // Navegación por teclado: Enter en usuario pasa el foco a contraseña
        txtUsuario.setOnKeyPressed(this::manejarTeclaUsuario);

        // Ocultar mensaje de error al teclear
        txtUsuario.textProperty().addListener((obs, oldV, newV) -> ocultarError());
        txtPassword.textProperty().addListener((obs, oldV, newV) -> ocultarError());
    }

    private void manejarTeclaUsuario(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            txtPassword.requestFocus();
        }
    }

    @FXML
    private void onIniciarSesionAction() {
        if (navegando) {
            return;
        }

        String usuario = txtUsuario.getText() != null ? txtUsuario.getText().trim() : "";
        String pass = txtPassword.getText() != null ? txtPassword.getText().trim() : "";

        // Validación de campos obligatorios
        if (usuario.isEmpty()) {
            mostrarError("Por favor, ingrese su nombre de usuario para continuar.");
            txtUsuario.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            mostrarError("Por favor, ingrese su contraseña.");
            txtPassword.requestFocus();
            return;
        }

        // Validación de credenciales
        if (usuario.equalsIgnoreCase("admin") && !pass.equals("admin123")) {
            mostrarError("Contraseña incorrecta. Verifique sus credenciales.");
            txtPassword.requestFocus();
            return;
        }

        navegando = true;
        ocultarError();
        NavigationUtil.cambiarEscena(
                txtUsuario,
                "menu-principal-view.fxml",
                "Sistema de Registro y Solicitudes - Menú Principal",
                850,
                600
        );
    }

    private void mostrarError(String mensaje) {
        if (lblError != null && boxError != null) {
            lblError.setText(mensaje);
            boxError.setVisible(true);
            boxError.setManaged(true);
        }
    }

    private void ocultarError() {
        if (boxError != null && boxError.isVisible()) {
            boxError.setVisible(false);
            boxError.setManaged(false);
        }
    }
}
