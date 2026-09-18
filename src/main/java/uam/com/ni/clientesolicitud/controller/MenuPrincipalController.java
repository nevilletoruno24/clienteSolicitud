package uam.com.ni.clientesolicitud.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import uam.com.ni.clientesolicitud.components.StatCardComponent;
import uam.com.ni.clientesolicitud.model.DataStore;
import uam.com.ni.clientesolicitud.util.AlertUtil;
import uam.com.ni.clientesolicitud.util.AppRoutes;
import uam.com.ni.clientesolicitud.util.CsvExportUtil;
import uam.com.ni.clientesolicitud.util.SceneNavigator;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MenuPrincipalController {

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private StatCardComponent cardTotal;

    @FXML
    private StatCardComponent cardCreditos;

    @FXML
    private StatCardComponent cardCuentas;

    @FXML
    private StatCardComponent cardReclamos;

    @FXML
    public void initialize() {
        actualizarEstadisticas();
    }

    @FXML
    private void onContextMenuRequested(ContextMenuEvent event) {
        if (contextMenu != null && cardTotal != null && cardTotal.getScene() != null) {
            contextMenu.show(cardTotal.getScene().getWindow(), event.getScreenX(), event.getScreenY());
        }
    }

    public void actualizarEstadisticas() {
        if (cardTotal == null) {
            return;
        }
        int total = DataStore.getClientes().size();
        cardTotal.setValorMetrica(String.valueOf(total));

        long creditos = DataStore.getClientes().stream()
                .filter(c -> c.getTipoSolicitud() != null && c.getTipoSolicitud().toLowerCase().contains("crédito"))
                .count();
        long cuentas = DataStore.getClientes().stream()
                .filter(c -> c.getTipoSolicitud() != null && c.getTipoSolicitud().toLowerCase().contains("cuenta"))
                .count();
        long reclamos = DataStore.getClientes().stream()
                .filter(c -> c.getTipoSolicitud() != null && (c.getTipoSolicitud().toLowerCase().contains("reclamo") || c.getTipoSolicitud().toLowerCase().contains("soporte")))
                .count();

        if (cardCreditos != null) {
            cardCreditos.setValorMetrica(String.valueOf(creditos));
        }
        if (cardCuentas != null) {
            cardCuentas.setValorMetrica(String.valueOf(cuentas));
        }
        if (cardReclamos != null) {
            cardReclamos.setValorMetrica(String.valueOf(reclamos));
        }
    }

    @FXML
    private void onActualizarMetricasAction() {
        actualizarEstadisticas();
    }

    @FXML
    private void onRegistrarClienteAction() {
        SceneNavigator.cambiarPantalla(
                cardTotal,
                AppRoutes.REGISTRO,
                "Registro de Clientes y Solicitudes"
        );
    }

    @FXML
    private void onConsultarClientesAction() {
        SceneNavigator.cambiarPantalla(
                cardTotal,
                AppRoutes.CONSULTA,
                "Consulta y Administración de Clientes"
        );
    }

    @FXML
    private void onExportarDirectorioAction() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar Carpeta para Exportar Reporte de Clientes");

        Stage stage = (Stage) cardTotal.getScene().getWindow();
        File carpetaSeleccionada = directoryChooser.showDialog(stage);

        if (carpetaSeleccionada != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File archivoReporte = new File(carpetaSeleccionada, "Reporte_Clientes_" + timestamp + ".csv");

            try {
                CsvExportUtil.exportarClientesACsv(archivoReporte, DataStore.getClientes());

                AlertUtil.mostrarInfo(
                        "Exportación Exitosa",
                        "Reporte generado para Microsoft Excel",
                        "El archivo fue exportado en:\n" + archivoReporte.getAbsolutePath() +
                        "\n\nConfigurado con codificación UTF-8 BOM y separación automática por columnas."
                );
            } catch (IOException e) {
                AlertUtil.mostrarError("Error al Exportar", "No se pudo escribir el archivo", e.getMessage());
            }
        }
    }

    @FXML
    private void onCerrarSesionAction() {
        boolean confirmar = AlertUtil.mostrarConfirmacion(
                "Cerrar Sesión",
                "¿Está seguro de cerrar sesión?",
                "Regresará a la pantalla de inicio de sesión."
        );

        if (confirmar) {
            SceneNavigator.cambiarPantalla(
                    cardTotal,
                    AppRoutes.LOGIN,
                    "Sistema de Clientes - Inicio de Sesión"
            );
        }
    }

    @FXML
    private void onSalirAction() {
        boolean confirmar = AlertUtil.mostrarConfirmacion(
                "Salir del Sistema",
                "¿Desea cerrar la aplicación?",
                "Se guardarán los cambios en memoria antes de salir."
        );

        if (confirmar) {
            Platform.exit();
        }
    }

    @FXML
    private void onAcercaDeAction() {
        AlertUtil.mostrarInfo(
                "Acerca de la Aplicación",
                "Sistema de Registro y Solicitudes de Clientes v1.0",
                "Desarrollado con JavaFX.\n" +
                "Arquitectura MVC, eventos ActionEvent, MouseEvent, KeyEvent y diálogos integrados."
        );
    }
}
