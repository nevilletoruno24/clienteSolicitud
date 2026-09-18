package uam.com.ni.clientesolicitud.util;

import uam.com.ni.clientesolicitud.model.Cliente;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class CsvExportUtil {

    private static final String BOM = "\uFEFF";
    private static final String DELIMITADOR = ";";

    private CsvExportUtil() {
    }

    public static void exportarClientesACsv(File archivoDestino, List<Cliente> clientes) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivoDestino), StandardCharsets.UTF_8))) {

            writer.write(BOM);

            String[] cabeceras = {
                    "ID",
                    "Nombres",
                    "Apellidos",
                    "Tipo de Cliente",
                    "Ciudad",
                    "Fecha de Nacimiento",
                    "Tipo de Solicitud",
                    "Servicios de Interés",
                    "Observaciones"
            };
            writer.write(formatearLinea(cabeceras));
            writer.newLine();

            for (Cliente c : clientes) {
                String[] fila = {
                        escaparCampo(c.getId()),
                        escaparCampo(c.getNombres()),
                        escaparCampo(c.getApellidos()),
                        escaparCampo(c.getTipoCliente()),
                        escaparCampo(c.getCiudad()),
                        escaparCampo(c.getFechaNacimientoFormateada()),
                        escaparCampo(c.getTipoSolicitud()),
                        escaparCampo(c.getServiciosInteresTexto()),
                        escaparCampo(c.getObservaciones())
                };
                writer.write(formatearLinea(fila));
                writer.newLine();
            }
        }
    }

    private static String formatearLinea(String[] campos) {
        return String.join(DELIMITADOR, campos);
    }

    private static String escaparCampo(String valor) {
        if (valor == null) {
            return "\"\"";
        }
        String saneado = valor.replace("\"", "\"\"");
        return "\"" + saneado + "\"";
    }
}
