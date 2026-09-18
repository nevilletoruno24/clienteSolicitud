package uam.com.ni.clientesolicitud.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Cliente {
    private String id;
    private String nombres;
    private String apellidos;
    private String tipoCliente;
    private String ciudad;
    private LocalDate fechaNacimiento;
    private String tipoSolicitud;
    private List<String> serviciosInteres;
    private String rutaFotografia;
    private String observaciones;
    private LocalDate fechaRegistro;

    public Cliente() {
        this.fechaRegistro = LocalDate.now();
    }

    public Cliente(String id, String nombres, String apellidos, String tipoCliente, String ciudad,
                   LocalDate fechaNacimiento, String tipoSolicitud, List<String> serviciosInteres,
                   String rutaFotografia, String observaciones) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipoCliente = tipoCliente;
        this.ciudad = ciudad;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoSolicitud = tipoSolicitud;
        this.serviciosInteres = serviciosInteres;
        this.rutaFotografia = rutaFotografia;
        this.observaciones = observaciones;
        this.fechaRegistro = LocalDate.now();
    }

    public String getNombreCompleto() {
        return (nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : "");
    }

    public String getFechaNacimientoFormateada() {
        if (fechaNacimiento == null) return "";
        return fechaNacimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getServiciosInteresTexto() {
        if (serviciosInteres == null || serviciosInteres.isEmpty()) {
            return "Ninguno";
        }
        return String.join(", ", serviciosInteres);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public List<String> getServiciosInteres() {
        return serviciosInteres;
    }

    public void setServiciosInteres(List<String> serviciosInteres) {
        this.serviciosInteres = serviciosInteres;
    }

    public String getRutaFotografia() {
        return rutaFotografia;
    }

    public void setRutaFotografia(String rutaFotografia) {
        this.rutaFotografia = rutaFotografia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + tipoCliente + ") - Solicitud: " + tipoSolicitud;
    }
}
