module uam.com.ni.clientesolicitud {
    requires javafx.controls;
    requires javafx.fxml;

    opens uam.com.ni.clientesolicitud to javafx.fxml;
    opens uam.com.ni.clientesolicitud.controller to javafx.fxml;
    opens uam.com.ni.clientesolicitud.components to javafx.fxml;
    opens uam.com.ni.clientesolicitud.model to javafx.base;

    exports uam.com.ni.clientesolicitud;
    exports uam.com.ni.clientesolicitud.controller;
    exports uam.com.ni.clientesolicitud.components;
    exports uam.com.ni.clientesolicitud.model;
    exports uam.com.ni.clientesolicitud.util;
}
