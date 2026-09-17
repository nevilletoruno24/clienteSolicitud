package uam.com.ni.clientesolicitud.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Almacén central de datos en memoria para la aplicación.
 * Permite compartir datos de clientes entre todas las ventanas.
 */
public class DataStore {
    private static final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private static int secuenciaId = 1000;

    static {
        // Datos de prueba iniciales para que la aplicación muestre registros inmediatamente
        clientes.add(new Cliente(
                "CLI-1001",
                "Carlos Alberto",
                "Mendoza Solís",
                "Individual",
                "Managua",
                LocalDate.of(1992, 4, 15),
                "Crédito Personal",
                new ArrayList<>(Arrays.asList("Banca en Línea", "Tarjeta de Crédito")),
                "",
                "Cliente con excelente historial crediticio."
        ));

        clientes.add(new Cliente(
                "CLI-1002",
                "Elena Beatriz",
                "Torres Rivas",
                "Corporativo",
                "León",
                LocalDate.of(1988, 9, 23),
                "Apertura de Cuenta",
                new ArrayList<>(Arrays.asList("Banca en Línea", "Asesoría Financiera")),
                "",
                "Representante legal de Distribuidora del Norte S.A."
        ));

        clientes.add(new Cliente(
                "CLI-1003",
                "Mario José",
                "García Peralta",
                "VIP",
                "Granada",
                LocalDate.of(1995, 11, 30),
                "Reclamo de Servicio",
                new ArrayList<>(Arrays.asList("Seguro de Vida")),
                "",
                "Solicita revisión de cobro por seguro no autorizado."
        ));

        secuenciaId = 1004;
    }

    public static ObservableList<Cliente> getClientes() {
        return clientes;
    }

    public static void agregarCliente(Cliente cliente) {
        if (cliente.getId() == null || cliente.getId().trim().isEmpty()) {
            cliente.setId("CLI-" + secuenciaId++);
        }
        clientes.add(cliente);
    }

    public static void eliminarCliente(Cliente cliente) {
        clientes.remove(cliente);
    }

    public static void actualizarCliente(Cliente clienteActualizado) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId().equals(clienteActualizado.getId())) {
                clientes.set(i, clienteActualizado);
                break;
            }
        }
    }
}
