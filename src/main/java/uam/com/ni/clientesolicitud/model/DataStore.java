package uam.com.ni.clientesolicitud.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataStore {
    private static final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private static int secuenciaId = 1001;

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
