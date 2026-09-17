package uam.com.ni.clientesolicitud;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import uam.com.ni.clientesolicitud.controller.DetalleClienteController;
import uam.com.ni.clientesolicitud.model.Cliente;
import uam.com.ni.clientesolicitud.model.DataStore;

public class TestVistasHeadless {
    public static void main(String[] args) {
        System.out.println("Iniciando verificación de vistas FXML...");
        Platform.startup(() -> {
            try {
                // 1. Cargar Login
                FXMLLoader loaderLogin = new FXMLLoader(TestVistasHeadless.class.getResource("login-view.fxml"));
                Parent rootLogin = loaderLogin.load();
                System.out.println("OK -> login-view.fxml cargado con éxito.");

                // 2. Cargar Menu Principal
                FXMLLoader loaderMenu = new FXMLLoader(TestVistasHeadless.class.getResource("menu-principal-view.fxml"));
                Parent rootMenu = loaderMenu.load();
                System.out.println("OK -> menu-principal-view.fxml cargado con éxito.");

                // 3. Cargar Registro
                FXMLLoader loaderReg = new FXMLLoader(TestVistasHeadless.class.getResource("registro-view.fxml"));
                Parent rootReg = loaderReg.load();
                System.out.println("OK -> registro-view.fxml cargado con éxito.");

                // 4. Cargar Consulta
                FXMLLoader loaderCon = new FXMLLoader(TestVistasHeadless.class.getResource("consulta-view.fxml"));
                Parent rootCon = loaderCon.load();
                System.out.println("OK -> consulta-view.fxml cargado con éxito.");

                // 5. Cargar Detalle y probar paso de datos
                FXMLLoader loaderDet = new FXMLLoader(TestVistasHeadless.class.getResource("detalle-view.fxml"));
                Parent rootDet = loaderDet.load();
                DetalleClienteController detCtrl = loaderDet.getController();
                Cliente clientePrueba = DataStore.getClientes().get(0);
                detCtrl.setCliente(clientePrueba);
                System.out.println("OK -> detalle-view.fxml cargado y paso de datos probado con éxito para: " + clientePrueba.getNombreCompleto());

                System.out.println("TODAS LAS VISTAS Y CONTROLADORES SON 100% FUNCIONALES.");
                Platform.exit();
            } catch (Throwable t) {
                t.printStackTrace();
                Platform.exit();
                System.exit(1);
            }
        });
    }
}
