package dao;
import models.MetodoPago;
import models.Reserva;

import database.DatabaseConnection;

import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

public class ReservaDAO {

    // Crear reserva
    public void crearReserva(Reserva nuevaReserva) {

        long inicioMillis = nuevaReserva.getHorarioInicio().getTime();
        long finMillis = inicioMillis + (90 * 60 * 1000); // 1h30m
        java.sql.Time horarioFinalCalculado = new java.sql.Time(finMillis);

        String validar = "SELECT COUNT(*) FROM Reserva r JOIN Cancha c ON r.idCancha = c.id " +
                "WHERE c.numero = ? AND fecha = ? AND (horarioInicio < ? AND horarioFinal > ?)";
        String consulta = "INSERT INTO Reserva (cedulaUsuario, idCancha, fecha, horarioInicio, horarioFinal, horaCancelacion, metodoPago, estaPagada, estaActiva) " +
                "VALUES (?, (SELECT id FROM Cancha WHERE numero = ?), ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement psValidar = DatabaseConnection.getInstancia().getConnection().prepareStatement(validar)) {

            psValidar.setInt(1, nuevaReserva.getNumeroCancha());
            psValidar.setDate(2, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            psValidar.setTime(3, horarioFinalCalculado);
            psValidar.setTime(4, nuevaReserva.getHorarioInicio());

            try (ResultSet rs = psValidar.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Error: ya existe una reserva en ese horario.");
                    return;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar reserva", e);
        }

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection()
                .prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nuevaReserva.getCedulaUsuario());
            ps.setInt(2, nuevaReserva.getNumeroCancha());
            ps.setDate(3, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            ps.setTime(4, nuevaReserva.getHorarioInicio());
            ps.setTime(5, horarioFinalCalculado);
            ps.setTime(6, nuevaReserva.getHoraCancelacion());
            ps.setString(7, nuevaReserva.getMetodoPago().getValue());
            ps.setBoolean(8, nuevaReserva.isEstaPagada());
            ps.setBoolean(9, nuevaReserva.isEstaActiva());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    nuevaReserva.setId(idGenerado);
                    System.out.println("Reserva creada correctamente. ID: " + idGenerado);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la reserva", e);
        }
    }

    // Actualizar reserva
    public void actualizarReserva(Reserva nuevaReserva) {

        long inicioMillis = nuevaReserva.getHorarioInicio().getTime();
        long finMillis = inicioMillis + (90 * 60 * 1000);
        java.sql.Time horarioFinalCalculado = new java.sql.Time(finMillis);

        String validar = "SELECT COUNT(*) FROM Reserva r JOIN Cancha c ON r.idCancha = c.id " +
                "WHERE c.numero = ? AND fecha = ? AND (horarioInicio < ? AND horarioFinal > ?) AND r.id <> ?";

        String consulta = "UPDATE Reserva SET cedulaUsuario = ?, idCancha = (SELECT id FROM Cancha WHERE numero = ?), fecha = ?, " +
                "horarioInicio = ?, horarioFinal = ?, horaCancelacion = ?, metodoPago = ?, estaPagada = ?, estaActiva = ? WHERE id = ?";

        try (PreparedStatement psValidar = DatabaseConnection.getInstancia().getConnection().prepareStatement(validar)) {

            psValidar.setInt(1, nuevaReserva.getNumeroCancha());
            psValidar.setDate(2, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            psValidar.setTime(3, horarioFinalCalculado);
            psValidar.setTime(4, nuevaReserva.getHorarioInicio());
            psValidar.setInt(5, nuevaReserva.getId());

            try (ResultSet rs = psValidar.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Ya existe una reserva en ese horario.");
                    return;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al validar reserva", e);
        }

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {

            ps.setString(1, nuevaReserva.getCedulaUsuario());
            ps.setInt(2, nuevaReserva.getNumeroCancha());
            ps.setDate(3, new java.sql.Date(nuevaReserva.getFecha().getTime()));
            ps.setTime(4, nuevaReserva.getHorarioInicio());
            ps.setTime(5, horarioFinalCalculado);
            ps.setTime(6, nuevaReserva.getHoraCancelacion());
            ps.setString(7, nuevaReserva.getMetodoPago().getValue());
            ps.setBoolean(8, nuevaReserva.isEstaPagada());
            ps.setBoolean(9, nuevaReserva.isEstaActiva());
            ps.setInt(10, nuevaReserva.getId());

            ps.executeUpdate();
            System.out.println("Reserva modificada correctamente");

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la reserva", e);
        }
    }


    // Cancelar reserva
    public void cancelarReservasPorCedula(String cedula) {
        String sql = "UPDATE Reserva SET estaActiva = false, horaCancelacion = CURRENT_TIME WHERE cedulaUsuario = ? AND estaActiva = true";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(sql)) {
            ps.setString(1, cedula);
            int filas = ps.executeUpdate();
            System.out.println("Reservas canceladas para usuario " + cedula + ": " + filas + " afectadas.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al cancelar las reservas del usuario: " + cedula, e);
        }
    }

    public void cancelarReserva(int id) {
        String consulta = "UPDATE Reserva SET estaActiva = false, horaCancelacion = CURRENT_TIME WHERE id = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Reserva cancelada correctamente");
            } else {
                System.out.println("No se encontró la reserva con el ID proporcionado");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Listar reservas por usuario
    public Vector<Reserva> listarReservasPorUsuario(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ? ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    //Listar reservas por número de cancha
    public Vector<Reserva> listarReservasPorCancha(int numeroCancha) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT r.* FROM Reserva r JOIN Cancha c ON r.idCancha = c.id " +
                "WHERE c.numero = ? AND r.estaActiva = true " +
                "ORDER BY r.fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, numeroCancha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    // Listar reservas por fecha
    public Vector<Reserva> listarReservasPorFecha(Date fecha) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE fecha = ? AND estaActiva = true ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    // Listar reservas por fecha y jugador
    public Vector<Reserva> listarReservasPorFechaJugador(Date fecha, String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT r.* FROM Reserva r JOIN Usuario u ON r.cedulaUsuario = u.cedula " +
                "WHERE r.fecha = ? AND r.cedulaUsuario = ?" +
                "ORDER BY r.fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            ps.setString(2, cedulaUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarTodasLasReservas() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public boolean obtenerEstadoPago(int id) {
        String consulta = "SELECT estaPagada FROM Reserva WHERE id = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean pagada = rs.getBoolean("estaPagada");
                    System.out.println(pagada ? "La reserva ha sido pagada" : "La reserva aún no ha sido pagada");
                    return pagada;
                } else {
                    System.out.println("No se encontró la reserva con el ID proporcionado");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el estado de pago", e);
        }
    }

    public void pagarReserva(int id) {
        if (obtenerEstadoPago(id)) return;

        String consulta = "UPDATE Reserva SET estaPagada = TRUE WHERE id = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "La reserva ha sido pagada exitosamente." : "No se encontró la reserva para pagar.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al pagar la reserva", e);
        }
    }

    public void despagarReserva(int id) {
        String sql = "UPDATE Reserva SET estaPagada = false WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Reserva despagada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void activarReserva(int id) {
        String sql = "UPDATE Reserva SET estaActiva = true, horaCancelacion = NULL WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Reserva activada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean obtenerEstadoActiva(int id) {
        String consulta = "SELECT estaActiva FROM Reserva WHERE id = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean activa = rs.getBoolean("estaActiva");
                    System.out.println(activa ? "La reserva está activa" : "La reserva no está activa");
                    return activa;
                } else {
                    System.out.println("No se encontró la reserva con el ID proporcionado");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el estado de la reserva", e);
        }
    }


    public int totalReservas() {
        String consulta = "SELECT COUNT(*) AS total FROM Reserva";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    public double totalIngresosMes(java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        String consulta = "SELECT COALESCE(SUM(c.precio), 0) AS total " +
                "FROM Reserva r JOIN Cancha c ON r.idCancha = c.id " +
                "WHERE r.fecha BETWEEN ? AND ? AND r.estaPagada = true";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble("total");
                    System.out.println("Total de ingresos pagados del mes: " + total);
                    return total;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el total de ingresos", e);
        }
        return 0;
    }

    public double totalIngresos(java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        String consulta = "SELECT COALESCE(SUM(c.precio), 0) AS total FROM Reserva r JOIN Cancha c ON r.idCancha = c.id " +
                "WHERE r.fecha BETWEEN ? AND ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Total de ingresos: " + rs.getDouble("total"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el total de ingresos", e);
        }
        return 0;
    }

    private Reserva mapearReservaDesdeResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String cedula = rs.getString("cedulaUsuario");
        int idCancha = rs.getInt("idCancha");

        // Obtener el número de cancha desde la tabla Cancha
        int numeroCancha = new CanchaDAO().obtenerNumeroPorId(idCancha);

        Date fecha = rs.getDate("fecha");
        Time horarioInicio = rs.getTime("horarioInicio");
        Time horarioFinal = rs.getTime("horarioFinal");
        Time horaCancelacion = rs.getTime("horaCancelacion");
        MetodoPago metodoPagoEnum = MetodoPago.fromString(rs.getString("metodoPago"));
        boolean estaPagada = rs.getBoolean("estaPagada");
        boolean estaActiva = rs.getBoolean("estaActiva");

        return new Reserva(id, cedula, idCancha, numeroCancha, fecha, horarioInicio, horarioFinal,
                horaCancelacion, metodoPagoEnum, estaPagada, estaActiva);
    }



    public int totalReservasActivas() {
    String consulta = "SELECT COUNT(*) AS total FROM Reserva WHERE estaActiva = TRUE";
    try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt("total");
    } catch (SQLException e) { throw new RuntimeException(e); }
    return 0;
}

    public int totalReservasPorUsuario(String cedulaUsuario) {
        String consulta = "SELECT COUNT(*) AS total FROM Reserva WHERE cedulaUsuario = ? AND estaActiva = TRUE";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    public int totalReservasPorCancha(int numeroCancha) {
        String consulta = "SELECT COUNT(*) AS total FROM Reserva r JOIN Cancha c ON r.idCancha = c.id WHERE c.numero = ? AND r.estaActiva = TRUE";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, numeroCancha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    public int totalReservasPagadas() {
        String consulta = "SELECT COUNT(*) AS total FROM Reserva WHERE estaPagada = TRUE";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    public int totalReservasNoPagadas() {
        String consulta = "SELECT COUNT(*) AS total FROM Reserva WHERE estaPagada = FALSE";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    public Vector<Reserva> listarTodasLasReservasAsc() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservas.add(mapearReservaDesdeResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar reservas en orden ascendente", e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorUsuarioAsc(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ? ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorCanchaAsc(int numeroCancha) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT r.* FROM Reserva r JOIN Cancha c ON r.idCancha = c.id " +
                "WHERE c.numero = ? AND r.estaActiva = true " +
                "ORDER BY r.fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, numeroCancha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorFechaAsc(Date fecha) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE fecha = ? AND estaActiva = true ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorFechaJugadorAsc(Date fecha, String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT r.* FROM Reserva r JOIN Usuario u ON r.cedulaUsuario = u.cedula " +
                "WHERE r.fecha = ? AND r.cedulaUsuario = ?" +
                "ORDER BY r.fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            ps.setString(2, cedulaUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public int totalReservasPorMetodoPago(String metodoPago) {
        String consulta = "SELECT COUNT(*) AS total FROM Reserva WHERE metodoPago = ? AND estaActiva = TRUE";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, metodoPago);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    public Vector<Reserva> listarReservasPorMetodoPago(String metodoPago) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE metodoPago = ? AND estaActiva = true ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, metodoPago);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorMetodoPagoAsc(String metodoPago) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE metodoPago = ? AND estaActiva = true ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, metodoPago);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPagadas() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE estaPagada = true ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPagadasAsc() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE estaPagada = true ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasNoPagadas() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE estaPagada = false ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasNoPagadasAsc() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE estaPagada = false ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasActivas() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE estaActiva = true ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasActivasAsc() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE estaActiva = true ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasNoActivas() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE estaActiva = false ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasNoActivasAsc() {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE estaActiva = false ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorCanchaJugador(int numeroCancha, String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT r.* FROM Reserva r " +
                "WHERE r.idCancha = (SELECT id FROM Cancha WHERE numero = ?) " +
                "AND r.cedulaUsuario = ?" +
                "ORDER BY r.fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, numeroCancha);
            ps.setString(2, cedulaUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar reservas por cancha y jugador", e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorCanchaJugadorAsc(int numeroCancha, String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT r.* FROM Reserva r " +
                "WHERE r.idCancha = (SELECT id FROM Cancha WHERE numero = ?) " +
                "AND r.cedulaUsuario = ?" +
                "ORDER BY r.fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, numeroCancha);
            ps.setString(2, cedulaUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar reservas por cancha y jugador", e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorJugadorNoActiva(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ? AND estaActiva = false ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorJugadorNoActivaAsc(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ? AND estaActiva = false ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorMetodoPagoJugador(String metodoPago, String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE metodoPago = ? AND cedulaUsuario = ?  ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, metodoPago);
            ps.setString(2, cedulaUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar reservas por metodo de pago y jugador", e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorMetodoPagoJugadorAsc(String metodoPago, String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE metodoPago = ? AND cedulaUsuario = ?  ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, metodoPago);
            ps.setString(2, cedulaUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar reservas por metodo de pago y jugador", e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorJugadorPagada(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ? AND estaPagada = true ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorJugadorPagadaAsc(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ? AND estaPagada = true ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorJugadorNoPagada(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ? AND estaPagada = false ORDER BY fecha DESC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Reserva> listarReservasPorJugadorNoPagadaAsc(String cedulaUsuario) {
        Vector<Reserva> reservas = new Vector<>();
        String consulta = "SELECT * FROM Reserva WHERE cedulaUsuario = ? AND estaPagada = false ORDER BY fecha ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedulaUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReservaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservas;
    }

    public Vector<Time> obtenerHorariosInicioUnicos() {
        Vector<Time> horarios = new Vector<>();
        String consulta = "SELECT DISTINCT horarioInicio FROM Reserva ORDER BY horarioInicio ASC";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                horarios.add(rs.getTime("horarioInicio"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener horarios únicos de inicio", e);
        }

        return horarios;
    }


}


