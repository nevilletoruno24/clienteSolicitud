package uam.com.ni.clientesolicitud.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import uam.com.ni.clientesolicitud.util.AlertUtil;
import uam.com.ni.clientesolicitud.util.NavigationUtil;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnSalir;

    @FXML
    public void initialize() {
        txtPassword.setOnKeyPressed(this::manejarTeclaPassword);
        txtUsuario.setOnKeyPressed(this::manejarTeclaUsuario);
    }

    private void manejarTeclaPassword(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnIniciarSesion.fire();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            btnSalir.fire();
        }
    }

    private void manejarTeclaUsuario(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            txtPassword.requestFocus();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            btnSalir.fire();
        }
    }

    @FXML
    private void onIniciarSesionAction(ActionEvent event) {
        String usuario = txtUsuario.getText() != null ? txtUsuario.getText().trim() : "";
        String pass = txtPassword.getText() != null ? txtPassword.getText().trim() : "";

        // Validación de campos vacíos requerida
        if (usuario.isEmpty() || pass.isEmpty()) {
            AlertUtil.mostrarAdvertencia(
                    "Campos Incompletos",
                    "No se puede iniciar sesión",
                    "Por favor, complete tanto el usuario como la contraseña para ingresar al sistema."
            );
            return;
        }

        // Validación de credenciales: admin/admin123 o cualquier usuario con clave correspondiente
        if (usuario.equalsIgnoreCase("admin") && !pass.equals("admin123")) {
            AlertUtil.mostrarError(
                    "Acceso Denegado",
                    "Contraseña incorrecta",
                    "Para el usuario 'admin' la contraseña es: admin123"
            );
            return;
        }

        // Abrir la ventana principal pasando el evento para resolver el Stage
        System.out.println("Validación exitosa. Intentando cargar menú principal...");
        Stage stage = null;
        try {
            stage = (Stage) txtUsuario.getScene().getWindow();
            System.out.println("Stage obtenido correctamente.");
        } catch (Exception e) {
            System.err.println("Error al obtener Stage desde txtUsuario: " + e.getMessage());
        }

        try {
            NavigationUtil.cambiarEscena(
                    stage != null ? stage : NavigationUtil.obtenerStageActivo(event),
                    "/uam/com/ni/clientesolicitud/menu-principal-view.fxml",
                    "Sistema de Registro y Solicitudes - Menú Principal",
                    850,
                    600
            );
            System.out.println("Llamada a NavigationUtil.cambiarEscena completada.");
        } catch (Exception e) {
            System.err.println("Error crítico al intentar cambiar de escena: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onSalirAction(ActionEvent event) {
        boolean confirmar = AlertUtil.mostrarConfirmacion(
                "Confirmación de Salida",
                "¿Desea salir de la aplicación?",
                "Se cerrará el sistema de registro y solicitudes."
        );

        if (confirmar) {
            Platform.exit();
        }
    }
}
