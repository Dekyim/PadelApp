package models;
import java.sql.Date;
import java.util.ArrayList;

public class Usuario {
    private String cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String contraseniaCuenta;
    private Date fechaIngreso;
    private boolean esAdministrador;


    public Usuario() {
    }
    public Usuario(String cedula, String nombre, String apellido, String correo, String telefono, String contraseniaCuenta) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.contraseniaCuenta = contraseniaCuenta;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContraseniaCuenta() {
        return contraseniaCuenta;
    }

    public void setContraseniaCuenta(String contraseniaCuenta) {
        this.contraseniaCuenta = contraseniaCuenta;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean esAdministrador() {
        return esAdministrador;
    }
    
    public void setEsAdministrador(boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
    }


    @Override
    public String toString() {
        return "Cédula: " + cedula + ", Nombre: " + nombre + ", Apellido: " + apellido + ", Correo: " + correo + ", Teléfono: " + telefono + ", Contraseña: " + contraseniaCuenta;
    }
}
