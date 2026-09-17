package uam.com.ni.clientesolicitud.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
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
    private Label lblTotalClientes;

    @FXML
    private Label lblCreditos;

    @FXML
    private Label lblCuentas;

    @FXML
    private Label lblReclamos;

    @FXML
    public void initialize() {
        actualizarEstadisticas();
    }

    public void actualizarEstadisticas() {
        if (lblTotalClientes == null) return;
        int total = DataStore.getClientes().size();
        lblTotalClientes.setText(String.valueOf(total));

        long creditos = DataStore.getClientes().stream()
                .filter(c -> c.getTipoSolicitud() != null && c.getTipoSolicitud().toLowerCase().contains("crédito"))
                .count();
        long cuentas = DataStore.getClientes().stream()
                .filter(c -> c.getTipoSolicitud() != null && c.getTipoSolicitud().toLowerCase().contains("cuenta"))
                .count();
        long reclamos = DataStore.getClientes().stream()
                .filter(c -> c.getTipoSolicitud() != null && (c.getTipoSolicitud().toLowerCase().contains("reclamo") || c.getTipoSolicitud().toLowerCase().contains("soporte")))
                .count();

        if (lblCreditos != null) lblCreditos.setText(String.valueOf(creditos));
        if (lblCuentas != null) lblCuentas.setText(String.valueOf(cuentas));
        if (lblReclamos != null) lblReclamos.setText(String.valueOf(reclamos));
    }

    @FXML
    private void onRegistrarClienteAction(ActionEvent event) {
        NavigationUtil.cambiarEscena(
                event,
                "/uam/com/ni/clientesolicitud/registro-view.fxml",
                "Registro de Clientes y Solicitudes",
                820,
                680
        );
    }

    @FXML
    private void onConsultarClientesAction(ActionEvent event) {
        NavigationUtil.cambiarEscena(
                event,
                "/uam/com/ni/clientesolicitud/consulta-view.fxml",
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

        respuesta.ifPresent(nota -> {
            AlertUtil.mostrarInfo("Recordatorio Guardado", "Nota registrada", "Mensaje: \"" + nota + "\"");
        });
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
                    "/uam/com/ni/clientesolicitud/login-view.fxml",
                    "Sistema de Clientes - Inicio de Sesión",
                    500,
                    360
            );
        }
    }

    @FXML
    private void onSalirAction(ActionEvent event) {
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
    private void onAcercaDeAction(ActionEvent event) {
        AlertUtil.mostrarInfo(
                "Acerca de la Aplicación",
                "Sistema de Registro y Solicitudes de Clientes v1.0",
                "Desarrollado con JavaFX y Scene Builder.\n" +
                "Arquitectura MVC, eventos ActionEvent, MouseEvent, KeyEvent y diálogos integrados."
        );
    }
}
