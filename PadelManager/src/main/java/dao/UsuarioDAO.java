package dao;

import database.DatabaseConnection;
import models.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class UsuarioDAO {

    public boolean inicioSesion(String cedula, String contrasenia) {
        String consulta = "SELECT * FROM Usuario u INNER JOIN Administrador a ON u.cedula = a.cedula WHERE u.cedula = ? AND u.contraseniaCuenta = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedula);
            ps.setString(2, contrasenia);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    System.out.println("Inicio de sesión exitoso");
                    return true;
                } else {
                    System.out.println("Cédula o contraseña incorrecta");
                    return false;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar sesión", e);
        }
    }

    public boolean cambiarContrasenia(String cedula, String contraseniaActual, String nuevaContrasenia) {

        if (!validarContrasenia(nuevaContrasenia)) {
            System.out.println("La nueva contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return false;
        }

        String consultaVerificacion = "SELECT * FROM Usuario WHERE cedula = ? AND contraseniaCuenta = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaVerificacion)) {

            ps.setString(1, cedula);
            ps.setString(2, contraseniaActual);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("La contraseña actual es incorrecta.");
                    return false;
                }
            }

            String consultaActualizar = "UPDATE Usuario SET contraseniaCuenta = ? WHERE cedula = ?";
            try (PreparedStatement psUpdate = DatabaseConnection.getInstancia()
                    .getConnection()
                    .prepareStatement(consultaActualizar)) {

                psUpdate.setString(1, nuevaContrasenia);
                psUpdate.setString(2, cedula);

                int filas = psUpdate.executeUpdate();
                if (filas > 0) {
                    System.out.println("Contraseña actualizada correctamente.");
                    return true;
                } else {
                    System.out.println("No se pudo actualizar la contraseña.");
                    return false;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar contraseña", e);
        }
    }

    private boolean validarContrasenia(String contrasenia) {
        return contrasenia.length() >= 8 && contrasenia.matches(".*[A-Z].*");
    }

    public Vector<Usuario> listarUsuarios(String criterio) {
        Vector<Usuario> usuarios = new Vector<>();
        String consulta = "SELECT cedula, nombre, apellido, telefono, correo, fechaIngreso, esAdministrador, contraseniaCuenta FROM Usuario WHERE cedula LIKE ? OR nombre LIKE ? OR apellido LIKE ?";


        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {

            String busqueda = "%" + criterio + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            ps.setString(3, busqueda);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setCedula(rs.getString("cedula"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setFechaIngreso(rs.getDate("fechaIngreso"));
                    usuario.setEsAdministrador(rs.getBoolean("esAdministrador"));
                    usuario.setContraseniaCuenta(rs.getString("contraseniaCuenta"));

                    usuarios.add(usuario);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }

        return usuarios;
    }

    public Vector<Usuario> listarTodosLosUsuarios() {
        Vector<Usuario> usuarios = new Vector<>();
        String consulta = "SELECT cedula, nombre, apellido, telefono, correo, fechaIngreso FROM Usuario";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setCedula(rs.getString("cedula"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setFechaIngreso(rs.getDate("fechaIngreso"));

                    usuarios.add(usuario);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }

        return usuarios;
    }

    public void desactivarUsuarioPorCedula(String cedula) {
        String eliminarJugador = "DELETE FROM Jugador WHERE cedula = ?";
        String eliminarUsuario = "DELETE FROM Usuario WHERE cedula = ?";

        try (PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(eliminarJugador);
             PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(eliminarUsuario)) {

            // Primero eliminamos al jugador
            psJugador.setString(1, cedula);
            psJugador.executeUpdate();

            // Luego eliminamos al usuario
            psUsuario.setString(1, cedula);
            psUsuario.executeUpdate();

            System.out.println("Usuario y jugador eliminados correctamente.");


        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar usuario y jugador", e);
        }
    }

    public int contarUsuariosPorMes(int mes, int anio) {
        int cantidad = 0;
        String consulta = "SELECT COUNT(*) AS total FROM Usuario WHERE MONTH(fechaIngreso) = ? AND YEAR(fechaIngreso) = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, mes);
            ps.setInt(2, anio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cantidad = rs.getInt("total");
                }
            } catch (SQLException e) { throw new RuntimeException(e);}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cantidad;
    }

    public boolean existeUsuario(String cedula) {
        String consulta = "SELECT * FROM Usuario WHERE cedula = ?";
        try  {
            PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error ", e);
        }
    }

    // Total de usuarios registrados
public int totalUsuarios() {
    String consulta = "SELECT COUNT(*) AS total FROM Usuario";
    try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt("total");
    } catch (SQLException e) { throw new RuntimeException(e); }
    return 0;
}






}
