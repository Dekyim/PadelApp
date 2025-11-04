package dao;
import database.DatabaseConnection;

import models.ParticipantesGrupo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantesGrupoDAO {

    public void agregarParticipante(ParticipantesGrupo participante) {
        String consulta = "INSERT INTO ParticipantesGrupo (idGrupo, idJugador, esCreador) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, participante.getIdGrupo());
            ps.setString(2, participante.getIdJugador());
            ps.setBoolean(3, participante.isEsCreador());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ParticipantesGrupo> obtenerParticipantesPorGrupo(int idGrupo) {
        List<ParticipantesGrupo> participantes = new ArrayList<>();
        String consulta = "SELECT * FROM ParticipantesGrupo WHERE idGrupo = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ParticipantesGrupo p = new ParticipantesGrupo();
                    p.setIdGrupo(rs.getInt("idGrupo"));
                    p.setIdJugador(rs.getString("idJugador"));
                    p.setEsCreador(rs.getBoolean("esCreador"));
                    participantes.add(p);
                }
            }
            return participantes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminarParticipante(int idGrupo, String idJugador) {
        String consulta = "DELETE FROM ParticipantesGrupo WHERE idGrupo = ? AND idJugador = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, idGrupo);
            ps.setString(2, idJugador);
            System.out.println("Intentando eliminar participante: grupo=" + idGrupo + ", jugador=" + idJugador);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeParticipante(int idGrupo, String idJugador) {
        String consulta = "SELECT COUNT(*) FROM ParticipantesGrupo WHERE idGrupo = ? AND idJugador = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, idGrupo);
            ps.setString(2, idJugador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<String> obtenerCedulasPorGrupo(int idGrupo) {
        List<String> cedulas = new ArrayList<>();
        String consulta = "SELECT idJugador FROM ParticipantesGrupo WHERE idGrupo = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cedulas.add(rs.getString("idJugador"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cedulas;
    }



}
