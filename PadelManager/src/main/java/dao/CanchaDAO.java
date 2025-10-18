package dao;
import database.DatabaseConnection;
import models.CanchaHorario;
import models.Cancha;

import java.sql.*;
import java.util.Vector;

public class CanchaDAO {
    public void altaCancha(Cancha nuevaCancha) {
        String consultaCancha = "INSERT INTO Cancha (numero, precio, esTechada, estaDisponible) VALUES (?, ?, ?, ?)";
        String consultaHorario = "INSERT INTO CanchaHorario (idCancha, horario) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement psCancha = null;
        PreparedStatement psHorario = null;
        ResultSet rsKeys = null;

        try {
            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false);

            // Insertar cancha sin id (autoincremental)
            psCancha = conn.prepareStatement(consultaCancha, Statement.RETURN_GENERATED_KEYS);
            psCancha.setInt(1, nuevaCancha.getNumero());
            psCancha.setDouble(2, nuevaCancha.getPrecio());
            psCancha.setBoolean(3, nuevaCancha.isEsTechada());
            psCancha.setBoolean(4, nuevaCancha.isEstaDisponible());
            psCancha.executeUpdate();

            // Obtener id generado por la BD
            rsKeys = psCancha.getGeneratedKeys();
            if (rsKeys.next()) {
                int idGenerado = rsKeys.getInt(1);
                nuevaCancha.setId(idGenerado);

                // Insertar horarios con id generado
                psHorario = conn.prepareStatement(consultaHorario);
                Vector<Time> listaHorarios = nuevaCancha.getHorario().getHorarios();
                for (Time horario : listaHorarios) {
                    psHorario.setInt(1, idGenerado);
                    psHorario.setTime(2, horario);
                    psHorario.addBatch();
                }
                psHorario.executeBatch();
            } else {
                throw new SQLException("No se pudo obtener el ID generado para la cancha.");
            }

            conn.commit();
            System.out.println("Cancha y horarios creados correctamente");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Error rollback: " + ex.getMessage()); }
            }
            throw new RuntimeException("No se pudo registrar la cancha: " + e.getMessage(), e);

        } finally {
            try {
                if (rsKeys != null) rsKeys.close();
                if (psCancha != null) psCancha.close();
                if (psHorario != null) psHorario.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    // ACTUALIZAR CANCHA
    public void actualizarCancha(Cancha canchaActualizada) {
        String consultaId = "SELECT id FROM Cancha WHERE numero = ?";
        String consultaCancha = "UPDATE Cancha SET precio = ?, esTechada = ?, estaDisponible = ? WHERE id = ?";
        String eliminarHorarios = "DELETE FROM CanchaHorario WHERE idCancha = ?";
        String insertarHorario = "INSERT INTO CanchaHorario (idCancha, horario) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false);

            // Obtener id de la cancha a partir del numero
            PreparedStatement psId = conn.prepareStatement(consultaId);
            psId.setInt(1, canchaActualizada.getNumero());
            ResultSet rs = psId.executeQuery();
            if (rs.next()) {
                int idCancha = rs.getInt("id");
                canchaActualizada.setId(idCancha);
            } else {
                throw new SQLException("No se encontró cancha con número: " + canchaActualizada.getNumero());
            }
            rs.close();
            psId.close();

            // Actualizar tabla Cancha
            PreparedStatement psCancha = conn.prepareStatement(consultaCancha);
            psCancha.setDouble(1, canchaActualizada.getPrecio());
            psCancha.setBoolean(2, canchaActualizada.isEsTechada());
            psCancha.setBoolean(3, canchaActualizada.isEstaDisponible());
            psCancha.setInt(4, canchaActualizada.getId());
            psCancha.executeUpdate();
            psCancha.close();

            // Actualizar horarios
            PreparedStatement psEliminarHorarios = conn.prepareStatement(eliminarHorarios);
            psEliminarHorarios.setInt(1, canchaActualizada.getId());
            psEliminarHorarios.executeUpdate();
            psEliminarHorarios.close();

            PreparedStatement psInsertarHorario = conn.prepareStatement(insertarHorario);
            Vector<Time> listaHorarios = canchaActualizada.getHorario().getHorarios();
            for (Time horario : listaHorarios) {
                psInsertarHorario.setInt(1, canchaActualizada.getId());
                psInsertarHorario.setTime(2, horario);
                psInsertarHorario.addBatch();
            }
            psInsertarHorario.executeBatch();
            psInsertarHorario.close();

            conn.commit();
            System.out.println("Cancha actualizada correctamente");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Error rollback: " + ex.getMessage()); }
            }
            throw new RuntimeException("No se pudo actualizar la cancha: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    public Vector<Cancha> listarCancha() throws SQLException {
        String consultaCanchas = "SELECT * FROM Cancha";
        String consultaHorarios = "SELECT horario FROM CanchaHorario WHERE idCancha = ?";
        Vector<Cancha> listaCanchas = new Vector<>();
        Connection conn = DatabaseConnection.getInstancia().getConnection();
        try (
             PreparedStatement psCanchas = conn.prepareStatement(consultaCanchas);
             ResultSet rsCanchas = psCanchas.executeQuery()) {

            while (rsCanchas.next()) {
                int id = rsCanchas.getInt("id");
                int numero = rsCanchas.getInt("numero"); // agregado
                double precio = rsCanchas.getDouble("precio");
                boolean esTechada = rsCanchas.getBoolean("esTechada");
                boolean estaDisponible = rsCanchas.getBoolean("estaDisponible");

                PreparedStatement psHorarios = conn.prepareStatement(consultaHorarios);
                psHorarios.setInt(1, id);
                ResultSet rsHorarios = psHorarios.executeQuery();

                Vector<Time> horarios = new Vector<>();
                while (rsHorarios.next()) {
                    horarios.add(rsHorarios.getTime("horario"));
                }
                rsHorarios.close();
                psHorarios.close();

                CanchaHorario canchaHorario = new CanchaHorario(id, horarios);
                Cancha cancha = new Cancha(id, esTechada, precio, estaDisponible, numero, canchaHorario);
                listaCanchas.add(cancha);

                System.out.printf("-ID: %d | Nº: %d | Precio: %.2f | Techada: %s | Disponible: %s%n",
                        id, numero, precio, esTechada ? "Sí" : "No", estaDisponible ? "Sí" : "No");
                System.out.println("  Horarios:");
                for (Time h : horarios) System.out.println("    " + h);
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudieron obtener las canchas: " + e.getMessage(), e);
        }

        return listaCanchas;
    }


    public boolean desactivarCancha(Cancha canchaDesactivar) {
        String consultaId = "SELECT id FROM Cancha WHERE numero = ?";
        String consultaReservas = "SELECT COUNT(*) FROM Reserva WHERE idCancha = ? AND estaActiva = TRUE";
        String eliminarHorarios = "DELETE FROM CanchaHorario WHERE idCancha = ?";
        String eliminarCancha = "DELETE FROM Cancha WHERE id = ?";

        Connection conn = null;
        PreparedStatement psId = null;
        PreparedStatement psReservas = null;
        PreparedStatement psEliminarHorarios = null;
        PreparedStatement psEliminarCancha = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstancia().getConnection();
            conn.setAutoCommit(false);

            psId = conn.prepareStatement(consultaId);
            psId.setInt(1, canchaDesactivar.getNumero());
            rs = psId.executeQuery();
            if (rs.next()) {
                int idCancha = rs.getInt("id");
                canchaDesactivar.setId(idCancha);
            } else {
                throw new SQLException("No se encontró cancha con número: " + canchaDesactivar.getNumero());
            }
            rs.close();
            psId.close();

            psReservas = conn.prepareStatement(consultaReservas);
            psReservas.setInt(1, canchaDesactivar.getId());
            rs = psReservas.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("No se puede eliminar la cancha. Tiene reservas activas.");
                conn.rollback();
                return false;
            }
            rs.close();
            psReservas.close();

            psEliminarHorarios = conn.prepareStatement(eliminarHorarios);
            psEliminarHorarios.setInt(1, canchaDesactivar.getId());
            psEliminarHorarios.executeUpdate();
            psEliminarHorarios.close();

            psEliminarCancha = conn.prepareStatement(eliminarCancha);
            psEliminarCancha.setInt(1, canchaDesactivar.getId());
            psEliminarCancha.executeUpdate();
            psEliminarCancha.close();

            conn.commit();
            System.out.println("Cancha y sus horarios eliminados correctamente (ID: " + canchaDesactivar.getId() +
                    " | Nº: " + canchaDesactivar.getNumero() + ")");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Error rollback: " + ex.getMessage()); }
            }
            throw new RuntimeException("No se pudo eliminar la cancha: " + e.getMessage(), e);

        } finally {
            try {
                if (rs != null) rs.close();
                if (psId != null) psId.close();
                if (psReservas != null) psReservas.close();
                if (psEliminarHorarios != null) psEliminarHorarios.close();
                if (psEliminarCancha != null) psEliminarCancha.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }
        return false;
    }



    public Vector<Cancha> busquedaAvanzada(Double minPrecio, Double maxPrecio,
                                           Boolean esTechada, Boolean disponible,
                                           Date fecha, Time hora) throws SQLException {

        Vector<Cancha> listaCanchas = new Vector<>();
        StringBuilder consulta = new StringBuilder(
                "SELECT c.id, c.numero, c.precio, c.esTechada, c.estaDisponible, " +
                        "GROUP_CONCAT(ch.horario SEPARATOR ',') AS horarios " +
                        "FROM Cancha c " +
                        "JOIN CanchaHorario ch ON c.id = ch.idCancha WHERE 1=1 "
        );

        if (minPrecio != null) consulta.append("AND c.precio >= ? ");
        if (maxPrecio != null) consulta.append("AND c.precio <= ? ");
        if (esTechada != null) consulta.append("AND c.esTechada = ? ");
        if (disponible != null) consulta.append("AND c.estaDisponible = ? ");
        if (hora != null) consulta.append("AND ch.horario = ? ");
        consulta.append("GROUP BY c.id");
        Connection conn = DatabaseConnection.getInstancia().getConnection();
        try (
             PreparedStatement ps = conn.prepareStatement(consulta.toString())) {

            int index = 1;
            if (minPrecio != null) ps.setDouble(index++, minPrecio);
            if (maxPrecio != null) ps.setDouble(index++, maxPrecio);
            if (esTechada != null) ps.setBoolean(index++, esTechada);
            if (disponible != null) ps.setBoolean(index++, disponible);
            if (hora != null) ps.setTime(index++, hora);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int numero = rs.getInt("numero"); // agregado
                    double precio = rs.getDouble("precio");
                    boolean techada = rs.getBoolean("esTechada");
                    boolean estaDisponible = rs.getBoolean("estaDisponible");

                    String horariosStr = rs.getString("horarios");
                    Vector<Time> horarios = new Vector<>();
                    if (horariosStr != null && !horariosStr.isEmpty()) {
                        for (String parte : horariosStr.split(",")) {
                            horarios.add(Time.valueOf(parte.trim()));
                        }
                    }

                    CanchaHorario canchaHorario = new CanchaHorario(id, horarios);
                    Cancha cancha = new Cancha(id, techada, precio, estaDisponible, numero, canchaHorario);
                    listaCanchas.add(cancha);

                    System.out.printf("-ID: %d | Nº: %d | Precio: %.2f | Techada: %s | Disponible: %s%n",
                            id, numero, precio, techada ? "Sí" : "No", estaDisponible ? "Sí" : "No");
                    System.out.println("  Horarios:");
                    for (Time h : horarios) System.out.println("    " + h);
                }
            }

            if (listaCanchas.isEmpty()) {
                System.out.println("No se encontraron canchas con esos criterios.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo realizar la búsqueda avanzada: " + e.getMessage(), e);
        }

        return listaCanchas;
    }


    public void bloquearCanchaPorMantenimiento(Cancha canchaBloquear) throws SQLException {
        String consultaId = "SELECT id FROM Cancha WHERE numero = ?";
        String consulta = "UPDATE Cancha SET estaDisponible = ? WHERE id = ?";
        PreparedStatement psId = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DatabaseConnection.getInstancia().getConnection();
        try {

            psId = conn.prepareStatement(consultaId);
            psId.setInt(1, canchaBloquear.getNumero());
            rs = psId.executeQuery();
            if (rs.next()) {
                canchaBloquear.setId(rs.getInt("id"));
            } else {
                throw new SQLException("No se encontró cancha con número: " + canchaBloquear.getNumero());
            }
            rs.close();
            psId.close();

            ps = conn.prepareStatement(consulta);
            ps.setBoolean(1, false);
            ps.setInt(2, canchaBloquear.getId());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cancha bloqueada por mantenimiento (ID: " + canchaBloquear.getId() + ")");
            } else {
                System.out.println("No se encontró ninguna cancha con ese ID.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al bloquear la cancha: " + e.getMessage(), e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (psId != null) psId.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }



    public void desbloquearCancha(Cancha canchaDesbloquear) throws SQLException {
        String consultaId = "SELECT id FROM Cancha WHERE numero = ?";
        String consulta = "UPDATE Cancha SET estaDisponible = ? WHERE id = ?";
        PreparedStatement psId = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DatabaseConnection.getInstancia().getConnection();
        try  {

            psId = conn.prepareStatement(consultaId);
            psId.setInt(1, canchaDesbloquear.getNumero());
            rs = psId.executeQuery();
            if (rs.next()) {
                canchaDesbloquear.setId(rs.getInt("id"));
            } else {
                throw new SQLException("No se encontró cancha con número: " + canchaDesbloquear.getNumero());
            }
            rs.close();
            psId.close();

            ps = conn.prepareStatement(consulta);
            ps.setBoolean(1, true);
            ps.setInt(2, canchaDesbloquear.getId());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cancha desbloqueada (ID: " + canchaDesbloquear.getId() + ")");
            } else {
                System.out.println("No se encontró ninguna cancha con ese ID.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al desbloquear la cancha: " + e.getMessage(), e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (psId != null) psId.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    public boolean canchaDisponible(int numeroCancha) {
        String consulta = "SELECT estaDisponible FROM Cancha WHERE numero = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
            ps.setInt(1, numeroCancha);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getBoolean("estaDisponible");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar disponibilidad de cancha", e);
        }
    }

    public boolean existeNumeroCancha(int numero) {
        String consulta = "SELECT 1 FROM Cancha WHERE numero = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de número de cancha", e);
        }
    }

    // Total de canchas
public int totalCanchas() {
    String consulta = "SELECT COUNT(*) AS total FROM Cancha";
    try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt("total");
    } catch (SQLException e) { throw new RuntimeException(e); }
    return 0;
}

// Total de canchas techadasasd
public int totalCanchasTechadas() {
    String consulta = "SELECT COUNT(*) AS total FROM Cancha WHERE esTechada = TRUE";
    try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt("total");
    } catch (SQLException e) { throw new RuntimeException(e); }
    return 0;
}

// Total de canchas disponibles
public int totalCanchasDisponibles() {
    String consulta = "SELECT COUNT(*) AS total FROM Cancha WHERE estaDisponible = TRUE";
    try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt("total");
    } catch (SQLException e) { throw new RuntimeException(e); }
    return 0;
}



}
