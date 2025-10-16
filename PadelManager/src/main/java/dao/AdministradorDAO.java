package dao;
import database.DatabaseConnection;
import models.Administrador;
import models.Usuario;

import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Vector;

public class AdministradorDAO {

    public void crearAdministrador(Administrador nuevoAdministrador) {
        if (!validarContrasenia(nuevoAdministrador.getContraseniaCuenta())) {
            System.out.println("La contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return;
        }

        String consultaUsuario = "INSERT INTO Usuario (cedula, nombre, apellido, correo, telefono, contraseniaCuenta) VALUES (?, ?, ?, ?, ?, ?)";
        String consultaAdmin = "INSERT INTO Administrador (cedula) VALUES (?)";

        try (
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
                PreparedStatement psAdmin = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaAdmin)
        ) {
            psUsuario.setString(1, nuevoAdministrador.getCedula());
            psUsuario.setString(2, nuevoAdministrador.getNombre());
            psUsuario.setString(3, nuevoAdministrador.getApellido());
            psUsuario.setString(4, nuevoAdministrador.getCorreo());
            psUsuario.setString(5, nuevoAdministrador.getTelefono());
            psUsuario.setString(6, nuevoAdministrador.getContraseniaCuenta());
            psUsuario.executeUpdate();

            psAdmin.setString(1, nuevoAdministrador.getCedula());
            psAdmin.executeUpdate();

            System.out.println("Administrador creado correctamente");

        } catch (Exception e) {
            throw new RuntimeException("Error al crear administrador", e);
        }
    }

    public void modificarAdministrador(Administrador nuevoAdministrador) {
        if (!validarContrasenia(nuevoAdministrador.getContraseniaCuenta())) {
            System.out.println("La contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return;
        }

        String consulta = "UPDATE Usuario SET nombre = ?, apellido = ?, correo = ?, telefono = ?, contraseniaCuenta = ? WHERE cedula = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, nuevoAdministrador.getNombre());
            ps.setString(2, nuevoAdministrador.getApellido());
            ps.setString(3, nuevoAdministrador.getCorreo());
            ps.setString(4, nuevoAdministrador.getTelefono());
            ps.setString(5, nuevoAdministrador.getContraseniaCuenta());
            ps.setString(6, nuevoAdministrador.getCedula());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Administrador modificado correctamente");
            } else {
                System.out.println("No se encontró un administrador con esa cédula");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar administrador", e);
        }
    }


    public void eliminarAdministrador(Administrador administrador) {
        String consultaAdministrador = "DELETE FROM Administrador WHERE cedula = ?";
        String consultaUsuario = "DELETE FROM Usuario WHERE cedula = ?";

        try (
                PreparedStatement psAdministrador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaAdministrador);
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario)
        ) {
            psAdministrador.setString(1, administrador.getCedula());
            psAdministrador.executeUpdate();

            psUsuario.setString(1, administrador.getCedula());
            psUsuario.executeUpdate();

            System.out.println("Administrador eliminado correctamente");

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar administrador", e);
        }
    }

    public Vector<Administrador> listarAdministradores() {
        Vector<Administrador> administradores = new Vector<>();

        String consulta = "SELECT u.cedula, u.nombre, u.apellido, u.correo, u.telefono, u.contraseniaCuenta FROM Usuario u JOIN Administrador a ON u.cedula = a.cedula";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                String telefono = rs.getString("telefono");
                String contraseniaCuenta = rs.getString("contraseniaCuenta");

                Administrador admin = new Administrador(cedula, nombre, apellido, correo, telefono, contraseniaCuenta);
                administradores.add(admin);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar administradores", e);
        }

        return administradores;
    }

    private boolean validarContrasenia(String contrasenia) {
        return contrasenia.length() >= 8 && contrasenia.matches(".*[A-Z].*");
    }
}
