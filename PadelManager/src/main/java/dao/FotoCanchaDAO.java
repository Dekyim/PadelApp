package dao;

import database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FotoCanchaDAO {

    public void guardarFoto(int idCancha, String urlFoto) {
        String sql = "INSERT INTO foto_cancha (id, url) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE url = VALUES(url)";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idCancha);
            ps.setString(2, urlFoto);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar o actualizar foto de cancha", e);
        }
    }

    public String obtenerFotoPorId(int idCancha) {
        String sql = "SELECT url FROM foto_cancha WHERE id = ? ORDER BY id DESC LIMIT 1";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idCancha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("url");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener foto de cancha", e);
        }

        return null;
    }
}

