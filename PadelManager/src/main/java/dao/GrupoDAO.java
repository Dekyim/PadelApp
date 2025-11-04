package dao;
import database.DatabaseConnection;
import models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrupoDAO {
    public void crearGrupo(Grupo grupo) {
        String consulta = "INSERT INTO Grupo (idCreador, horaDesde, horaHasta, categoria, cupos, descripcion, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, grupo.getIdCreador());
            ps.setTime(2, grupo.getHoraDesde());
            ps.setTime(3, grupo.getHoraHasta());
            ps.setString(4, grupo.getCategoria());
            ps.setInt(5, grupo.getCupos());
            ps.setString(6, grupo.getDescripcion());
            ps.setString(7, grupo.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    grupo.setIdGrupo(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Grupo obtenerGrupoPorId(int id) throws SQLException {
        String consulta = "SELECT * FROM Grupo WHERE idGrupo = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Grupo grupo = new Grupo();
                    grupo.setIdGrupo(rs.getInt("idGrupo"));
                    grupo.setIdCreador(rs.getString("idCreador"));
                    grupo.setHoraDesde(rs.getTime("horaDesde"));
                    grupo.setHoraHasta(rs.getTime("horaHasta"));
                    grupo.setCategoria(rs.getString("categoria"));
                    grupo.setCupos(rs.getInt("cupos"));
                    grupo.setDescripcion(rs.getString("descripcion"));
                    grupo.setEstado(rs.getString("estado"));
                    return grupo;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener grupo por ID", e);
        }
    }

    public List<Grupo> listarGruposAbiertos() {
        List<Grupo> grupos = new ArrayList<>();
        String consulta = "SELECT * FROM Grupo WHERE estado = 'abierto'";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Grupo grupo = new Grupo();
                    grupo.setIdGrupo(rs.getInt("idGrupo"));
                    grupo.setIdCreador(rs.getString("idCreador"));
                    grupo.setHoraDesde(rs.getTime("horaDesde"));
                    grupo.setHoraHasta(rs.getTime("horaHasta"));
                    grupo.setCategoria(rs.getString("categoria"));
                    grupo.setCupos(rs.getInt("cupos"));
                    grupo.setDescripcion(rs.getString("descripcion"));
                    grupo.setEstado(rs.getString("estado"));
                    grupos.add(grupo);
                }

            }
            return grupos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Grupo> listarGrupos() {
        List<Grupo> grupos = new ArrayList<>();
        String consulta = "SELECT * FROM Grupo";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Grupo grupo = new Grupo();
                    grupo.setIdGrupo(rs.getInt("idGrupo"));
                    grupo.setIdCreador(rs.getString("idCreador"));
                    grupo.setHoraDesde(rs.getTime("horaDesde"));
                    grupo.setHoraHasta(rs.getTime("horaHasta"));
                    grupo.setCategoria(rs.getString("categoria"));
                    grupo.setCupos(rs.getInt("cupos"));
                    grupo.setDescripcion(rs.getString("descripcion"));
                    grupo.setEstado(rs.getString("estado"));
                    grupos.add(grupo);
                }

            }
            return grupos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Grupo> obtenerGruposPorJugador(String cedulaJugador) {
        List<Grupo> grupos = new ArrayList<>();
        String consulta = "SELECT g.* FROM Grupo g JOIN ParticipantesGrupo p ON g.idGrupo = p.idGrupo WHERE p.idJugador = ? AND g.estado = 'abierto'";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaJugador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Grupo grupo = new Grupo();
                    grupo.setIdGrupo(rs.getInt("idGrupo"));
                    grupo.setIdCreador(rs.getString("idCreador"));
                    grupo.setHoraDesde(rs.getTime("horaDesde"));
                    grupo.setHoraHasta(rs.getTime("horaHasta"));
                    grupo.setCategoria(rs.getString("categoria"));
                    grupo.setCupos(rs.getInt("cupos"));
                    grupo.setDescripcion(rs.getString("descripcion"));
                    grupo.setEstado(rs.getString("estado"));
                    grupos.add(grupo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return grupos;
    }


    public void cerrarGrupo(int id) {
        String consulta = "UPDATE Grupo SET estado = 'cerrado' WHERE idGrupo = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarGrupo(int id) {
        String consulta = "DELETE FROM Grupo WHERE idGrupo = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarGrupo(Grupo grupo) {
        String consulta = "UPDATE Grupo SET horaDesde = ?, horaHasta = ?, categoria = ?, cupos = ?, descripcion = ?, estado = ? WHERE idGrupo = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setTime(1, grupo.getHoraDesde());
            ps.setTime(2, grupo.getHoraHasta());
            ps.setString(3, grupo.getCategoria());
            ps.setInt(4, grupo.getCupos());
            ps.setString(5, grupo.getDescripcion());
            ps.setString(6, grupo.getEstado());
            ps.setInt(7, grupo.getIdGrupo());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el grupo", e);
        }
    }

}