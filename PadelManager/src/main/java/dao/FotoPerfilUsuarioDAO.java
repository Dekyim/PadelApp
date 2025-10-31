package dao;
import database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FotoPerfilUsuarioDAO {
    public void guardarFoto(String cedula, String urlFoto) {
        String sql = "INSERT INTO FotoPerfilUsuario (cedulaUsuario, urlFoto) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE urlFoto = VALUES(urlFoto)";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.setString(2, urlFoto);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar o actualizar foto de perfil", e);
        }
    }


    public String obtenerFotoPorCedula(String cedula) {
        String sql = "SELECT urlFoto FROM FotoPerfilUsuario WHERE cedulaUsuario = ? ORDER BY id DESC LIMIT 1";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("urlFoto");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener foto de perfil", e);
        }

        return null;
    }
}