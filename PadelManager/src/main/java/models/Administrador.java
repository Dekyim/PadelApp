package models;

public class Administrador extends Usuario {

    public Administrador() {
    }

    public Administrador(String cedula, String nombre, String apellido, String correo, String telefono, String contraseniaCuenta) {
        super(cedula, nombre, apellido, correo, telefono, contraseniaCuenta);
    }
}
