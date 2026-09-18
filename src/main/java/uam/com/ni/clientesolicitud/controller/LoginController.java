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

    private static final String USUARIO_VALIDO = "admin";
    private static final String PASSWORD_VALIDO = "admin ";

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
        txtUsuario.setOnKeyPressed(this::manejarTeclaUsuario);
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
        if (navegando) {return;}

        String usuario = obtenerTexto(txtUsuario);
        String pass = obtenerTexto(txtPassword);

        if (!validarFormulario(usuario, pass)) {
            return;
        }

        navegando = true;
        ocultarError();

        var loader = NavigationUtil.cambiarEscena(
                txtUsuario,
                "menu-principal-view.fxml",
                "Sistema de Registro y Solicitudes - Menú Principal",
                850,
                600
        );

        if (loader == null) {
            navegando = false;
        }
    }

    private boolean validarFormulario(String usuario, String pass) {
        if (usuario.isEmpty()) {
            mostrarError("Por favor, ingrese su nombre de usuario.");
            txtUsuario.requestFocus();
            return false;
        }

        if (pass.isEmpty()) {
            mostrarError("Por favor, ingrese su contraseña.");
            txtPassword.requestFocus();
            return false;
        }

        if (!usuario.equalsIgnoreCase(USUARIO_VALIDO) || !pass.equals(PASSWORD_VALIDO)) {
            mostrarError("Credenciales incorrectas. Verifique usuario y contraseña.");
            txtPassword.clear();
            txtPassword.requestFocus();
            return false;
        }

        return true;
    }

    private String obtenerTexto(TextField campo) {
        return campo.getText() != null ? campo.getText().trim() : "";
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
