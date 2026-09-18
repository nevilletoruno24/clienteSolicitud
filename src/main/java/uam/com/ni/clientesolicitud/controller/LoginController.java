package uam.com.ni.clientesolicitud.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import uam.com.ni.clientesolicitud.components.CustomInput;
import uam.com.ni.clientesolicitud.util.AppRoutes;
import uam.com.ni.clientesolicitud.util.SceneNavigator;

public class LoginController {

    private static final String USUARIO_VALIDO = "admin";
    private static final String PASSWORD_VALIDO = "admin";

    @FXML
    private CustomInput inputUsuario;

    @FXML
    private CustomInput inputPassword;

    @FXML
    private HBox boxError;

    @FXML
    private Label lblError;

    private boolean navegando = false;

    @FXML
    public void initialize() {
        inputUsuario.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                inputPassword.requestFocusInput();
            }
        });

        inputUsuario.textProperty().addListener((obs, oldV, newV) -> ocultarError());
        inputPassword.textProperty().addListener((obs, oldV, newV) -> ocultarError());
    }

    @FXML
    private void onIniciarSesionAction() {
        if (navegando) {
            return;
        }

        String usuario = inputUsuario.getText().trim();
        String pass = inputPassword.getText().trim();

        if (!validarFormulario(usuario, pass)) {
            return;
        }

        navegando = true;
        ocultarError();

        var loader = SceneNavigator.cambiarPantalla(
                inputUsuario,
                AppRoutes.MENU_PRINCIPAL,
                "Sistema de Registro y Solicitudes - Menú Principal"
        );

        if (loader == null) {
            navegando = false;
        }
    }

    private boolean validarFormulario(String usuario, String pass) {
        if (usuario.isEmpty()) {
            mostrarError("Por favor, ingrese su nombre de usuario.");
            inputUsuario.requestFocusInput();
            return false;
        }

        if (pass.isEmpty()) {
            mostrarError("Por favor, ingrese su contraseña.");
            inputPassword.requestFocusInput();
            return false;
        }

        if (!usuario.equalsIgnoreCase(USUARIO_VALIDO) || !pass.equals(PASSWORD_VALIDO)) {
            mostrarError("Credenciales incorrectas. Verifique usuario y contraseña.");
            inputPassword.selectAll();
            inputPassword.requestFocusInput();
            return false;
        }

        return true;
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
