package uam.com.ni.clientesolicitud.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import uam.com.ni.clientesolicitud.components.StatCardComponent;
import uam.com.ni.clientesolicitud.model.Cliente;
import uam.com.ni.clientesolicitud.model.DataStore;
import uam.com.ni.clientesolicitud.util.AlertUtil;
import uam.com.ni.clientesolicitud.util.NavigationUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MenuPrincipalController {

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
        configurarMenuContextual();
    }

    private void configurarMenuContextual() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem miNuevo = new MenuItem("Registrar Nuevo Cliente");
        miNuevo.setOnAction(this::onRegistrarClienteAction);
        MenuItem miConsultar = new MenuItem("Consultar Catálogo de Clientes");
        miConsultar.setOnAction(this::onConsultarClientesAction);
        MenuItem miExportar = new MenuItem("Exportar Catálogo a Carpeta...");
        miExportar.setOnAction(this::onExportarDirectorioAction);
        MenuItem miNota = new MenuItem("Registrar Nota Rápida (Dialog)...");
        miNota.setOnAction(this::onNotaRapidaDialogAction);
        contextMenu.getItems().addAll(miNuevo, miConsultar, miExportar, miNota);

        Platform.runLater(() -> {
            if (cardTotal != null && cardTotal.getScene() != null) {
                cardTotal.getScene().setOnContextMenuRequested(e -> contextMenu.show(cardTotal.getScene().getWindow(), e.getScreenX(), e.getScreenY()));
            }
        });
    }

    public void actualizarEstadisticas() {
        if (cardTotal == null) return;
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

        if (cardCreditos != null) cardCreditos.setValorMetrica(String.valueOf(creditos));
        if (cardCuentas != null) cardCuentas.setValorMetrica(String.valueOf(cuentas));
        if (cardReclamos != null) cardReclamos.setValorMetrica(String.valueOf(reclamos));
    }

    @FXML
    private void onRegistrarClienteAction(ActionEvent event) {
        NavigationUtil.cambiarEscena(
                event,
                "registro-view.fxml",
                "Registro de Clientes y Solicitudes",
                820,
                680
        );
    }

    @FXML
    private void onConsultarClientesAction(ActionEvent event) {
        NavigationUtil.cambiarEscena(
                event,
                "consulta-view.fxml",
                "Consulta y Administración de Clientes",
                900,
                600
        );
    }

    @FXML
    private void onExportarDirectorioAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar Carpeta para Exportar Reporte de Clientes");

        Stage stage = NavigationUtil.obtenerStageActivo(event);
        File carpetaSeleccionada = directoryChooser.showDialog(stage);

        if (carpetaSeleccionada != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File archivoReporte = new File(carpetaSeleccionada, "Reporte_Clientes_" + timestamp + ".csv");

            try (FileWriter writer = new FileWriter(archivoReporte)) {
                writer.write("ID,Nombres,Apellidos,TipoCliente,Ciudad,FechaNacimiento,TipoSolicitud,Servicios,Observaciones\n");
                for (Cliente c : DataStore.getClientes()) {
                    writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                            c.getId(),
                            c.getNombres(),
                            c.getApellidos(),
                            c.getTipoCliente(),
                            c.getCiudad(),
                            c.getFechaNacimientoFormateada(),
                            c.getTipoSolicitud(),
                            c.getServiciosInteresTexto(),
                            c.getObservaciones() != null ? c.getObservaciones().replace("\"", "'") : ""
                    ));
                }

                AlertUtil.mostrarInfo(
                        "Exportación Exitosa",
                        "Reporte generado satisfactoriamente",
                        "El archivo fue exportado en:\n" + archivoReporte.getAbsolutePath()
                );
            } catch (IOException e) {
                AlertUtil.mostrarError("Error al Exportar", "No se pudo escribir el archivo", e.getMessage());
            }
        }
    }

    @FXML
    private void onNotaRapidaDialogAction(ActionEvent event) {
        Optional<String> respuesta = AlertUtil.mostrarDialogoTexto(
                "Nota Rápida del Sistema",
                "Registrar mensaje de recordatorio del día",
                "Ingrese el recordatorio para la sucursal:",
                "Atención prioritaria a solicitudes de crédito hoy."
        );

        respuesta.ifPresent(nota -> AlertUtil.mostrarInfo("Recordatorio Guardado", "Nota registrada", "Mensaje: \"" + nota + "\""));
    }

    @FXML
    private void onCerrarSesionAction(ActionEvent event) {
        boolean confirmar = AlertUtil.mostrarConfirmacion(
                "Cerrar Sesión",
                "¿Está seguro de cerrar sesión?",
                "Regresará a la pantalla de inicio de sesión."
        );

        if (confirmar) {
            NavigationUtil.cambiarEscena(
                    event,
                    "login-view.fxml",
                    "Sistema de Clientes - Inicio de Sesión",
                    580,
                    560
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
                "Desarrollado con JavaFX y Scene Builder.\n" +
                "Arquitectura MVC, eventos ActionEvent, MouseEvent, KeyEvent y diálogos integrados."
        );
    }
}
