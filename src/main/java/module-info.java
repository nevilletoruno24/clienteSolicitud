module uam.com.ni.clientesolicitud {
    requires javafx.controls;
    requires javafx.fxml;


    opens uam.com.ni.clientesolicitud to javafx.fxml;
    exports uam.com.ni.clientesolicitud;
}