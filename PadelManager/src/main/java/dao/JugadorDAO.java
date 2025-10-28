package dao;

import database.DatabaseConnection;
import models.Administrador;
import models.Jugador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class JugadorDAO {
    public void altaJugador(Jugador nuevoJugador) {
        if (!validarContrasenia(nuevoJugador.getContraseniaCuenta())) {
            System.out.println("La contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return;
        }

        String consultaUsuario = "INSERT INTO Usuario (cedula, nombre, apellido, correo, telefono, contraseniaCuenta) VALUES (?, ?, ?, ?, ?, ?)";
        String consultaJugador = "INSERT INTO Jugador (cedula, fechaNacimiento, categoria, genero, incumplePago, estaBaneado) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
                PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaJugador);
        ) {

            psUsuario.setString(1, nuevoJugador.getCedula());
            psUsuario.setString(2, nuevoJugador.getNombre());
            psUsuario.setString(3, nuevoJugador.getApellido());
            psUsuario.setString(4, nuevoJugador.getCorreo());
            psUsuario.setString(5, nuevoJugador.getTelefono());
            psUsuario.setString(6, nuevoJugador.getContraseniaCuenta());
            psUsuario.executeUpdate();


            psJugador.setString(1, nuevoJugador.getCedula());
            psJugador.setDate(2, new java.sql.Date(nuevoJugador.getFechaNacimiento().getTime()));
            psJugador.setString(3, nuevoJugador.getCategoria());
            psJugador.setString(4, nuevoJugador.getGenero());
            psJugador.setInt(5, nuevoJugador.isIncumplePago());
            psJugador.setBoolean(6, nuevoJugador.isEstaBaneado());
            psJugador.executeUpdate();

            System.out.println("Jugador creado correctamente");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void actualizarJugador(Jugador jugador) {
        if (!validarContrasenia(jugador.getContraseniaCuenta())) {
            System.out.println("La contraseña debe tener mínimo 8 caracteres y al menos una mayúscula.");
            return;
        }
        String consultaJugador = "UPDATE Jugador SET fechaNacimiento = ?, categoria = ?, genero = ?, incumplePago = ?, estaBaneado = ? WHERE cedula = ?";
        String consultaUsuario = "UPDATE Usuario SET nombre = ?, apellido = ?, correo = ?, telefono = ?, contraseniaCuenta = ? WHERE cedula = ?";
        try (
                PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaJugador);
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
        ) {
            psJugador.setDate(1, new java.sql.Date(jugador.getFechaNacimiento().getTime()));
            psJugador.setString(2, jugador.getCategoria());
            psJugador.setString(3, jugador.getGenero());
            psJugador.setInt(4, jugador.isIncumplePago());
            psJugador.setBoolean(5, jugador.isEstaBaneado());
            psJugador.setString(6, jugador.getCedula());
            psJugador.executeUpdate();

            psUsuario.setString(1, jugador.getNombre());
            psUsuario.setString(2, jugador.getApellido());
            psUsuario.setString(3, jugador.getCorreo());
            psUsuario.setString(4, jugador.getTelefono());
            psUsuario.setString(5, jugador.getContraseniaCuenta());
            psUsuario.setString(6, jugador.getCedula());
            psUsuario.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminarJugador(String cedula){
        String consultaJugador = "DELETE FROM Jugador WHERE cedula = ?";
        String consultaUsuario = "DELETE FROM Usuario WHERE cedula = ?";
        try (
                PreparedStatement psJugador = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaJugador);
                PreparedStatement psUsuario = DatabaseConnection.getInstancia().getConnection().prepareStatement(consultaUsuario);
        ) {
            psJugador.setString(1, cedula);
            psJugador.executeUpdate();

            psUsuario.setString(1, cedula);
            psUsuario.executeUpdate();
            System.out.println("Jugador eliminado correctamente");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validarContrasenia(String contrasenia) {
        return contrasenia.length() >= 8 && contrasenia.matches(".*[A-Z].*");
    }

    public Vector<Jugador> listarJugadores() {
        Vector<Jugador> jugadores = new Vector<>();
        String consulta = "SELECT u.cedula, u.nombre, u.apellido, u.correo, u.telefono, u.contraseniaCuenta, j.fechaNacimiento, j.categoria, j.genero, j.incumplePago, j.estaBaneado FROM Usuario u JOIN Jugador j ON u.cedula = j.cedula";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                String telefono = rs.getString("telefono");
                String contraseniaCuenta = rs.getString("contraseniaCuenta");
                Date fechaNacimiento = rs.getDate("fechaNacimiento");
                String categoria = rs.getString("categoria");
                String genero = rs.getString("genero");
                Boolean estaBaneado = rs.getBoolean("estaBaneado");
                int incumplePago = rs.getInt("incumplePago");
                Jugador jugador = new Jugador(cedula, nombre, apellido, correo, telefono, contraseniaCuenta, fechaNacimiento, categoria, genero, incumplePago, estaBaneado);
                jugadores.add(jugador);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar jugadores", e);
        } return jugadores;
    }

    public void analizarBaneos() {
        String consultaSelect = "SELECT cedula, incumplePago FROM Jugador";
        String consultaUpdate = "UPDATE Jugador SET estaBaneado = ? WHERE cedula = ?";
        int totalBaneados = 0;
        List<String> cedulasBaneados = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstancia().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (
                PreparedStatement psSelect = conn.prepareStatement(consultaSelect);
                ResultSet rs = psSelect.executeQuery()
        ) {
            while (rs.next()) {
                String cedula = rs.getString("cedula");
                int incumplePago = rs.getInt("incumplePago");
                boolean debeEstarBaneado = incumplePago >= 2;
                try (PreparedStatement psUpdate = conn.prepareStatement(consultaUpdate)) {
                    psUpdate.setBoolean(1, debeEstarBaneado);
                    psUpdate.setString(2, cedula);
                    psUpdate.executeUpdate();
                }
                if (debeEstarBaneado) {
                    totalBaneados++;
                    cedulasBaneados.add(cedula);
                }
            }
            System.out.println("Total de usuarios baneados: " + totalBaneados);
            if (!cedulasBaneados.isEmpty()) {
                System.out.println("Cédulas de usuarios baneados:");
                for (String ced : cedulasBaneados) {
                    System.out.println("- " + ced);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al analizar baneos", e);
        }
    }

    public Vector<Jugador> listarJugadoresBaneados() {
        Vector<Jugador> jugadores = new Vector<>();
        String consulta = "SELECT u.cedula, u.nombre, u.apellido, u.correo, u.telefono, u.contraseniaCuenta, j.fechaNacimiento, j.categoria, j.genero, j.incumplePago, j.estaBaneado FROM Usuario u JOIN Jugador j ON u.cedula = j.cedula WHERE j.estaBaneado = TRUE";
        try  {
            PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                String telefono = rs.getString("telefono");
                String contraseniaCuenta = rs.getString("contraseniaCuenta");
                Date fechaNacimiento = rs.getDate("fechaNacimiento");
                String categoria = rs.getString("categoria");
                String genero = rs.getString("genero");
                Boolean estaBaneado = rs.getBoolean("estaBaneado");
                int incumplePago = rs.getInt("incumplePago");
                Jugador jugador = new Jugador(cedula, nombre, apellido, correo, telefono, contraseniaCuenta, fechaNacimiento, categoria, genero, incumplePago, estaBaneado);
                jugadores.add(jugador);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // para ver la causa real en consola
            throw new RuntimeException("Error al listar jugadores baneados", e);
        }

        return jugadores;
    }

    public void desbanearJugador(String cedula) {
        String consulta = "UPDATE Jugador SET estaBaneado = FALSE, incumplePago = 0 WHERE cedula = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedula);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Jugador con cédula " + cedula + " ha sido desbaneado correctamente.");
            } else {
                System.out.println("No se encontró un jugador con la cédula " + cedula);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al desbanear jugador", e);
        }
    }

    public boolean jugadorBaneado(String cedula) {
        String consulta = "SELECT estaBaneado FROM Jugador WHERE cedula = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getBoolean("estaBaneado");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar estado del jugador", e);
        }
    }
    public Jugador obtenerJugadorPorCedula(String cedula) {
        String consulta = "SELECT u.cedula, u.nombre, u.apellido, u.correo, u.telefono, u.contraseniaCuenta, " +
                "j.fechaNacimiento, j.categoria, j.genero, j.incumplePago, j.estaBaneado " +
                "FROM Usuario u JOIN Jugador j ON u.cedula = j.cedula WHERE u.cedula = ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String correo = rs.getString("correo");
                    String telefono = rs.getString("telefono");
                    String contraseniaCuenta = rs.getString("contraseniaCuenta");
                    Date fechaNacimiento = rs.getDate("fechaNacimiento");
                    String categoria = rs.getString("categoria");
                    String genero = rs.getString("genero");
                    int incumplePago = rs.getInt("incumplePago");
                    boolean estaBaneado = rs.getBoolean("estaBaneado");

                    return new Jugador(cedula, nombre, apellido, correo, telefono, contraseniaCuenta,
                            fechaNacimiento, categoria, genero, incumplePago, estaBaneado);
                } else {
                    return null; // No se encontró el jugador
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener jugador por cédula", e);
        }
    }
    public Vector<Jugador> buscarJugadores(String termino) {
        Vector<Jugador> jugadores = new Vector<>();
        String consulta = "SELECT u.cedula, u.nombre, u.apellido, u.correo, u.telefono, u.contraseniaCuenta, " +
                "j.fechaNacimiento, j.categoria, j.genero, j.incumplePago, j.estaBaneado " +
                "FROM Usuario u JOIN Jugador j ON u.cedula = j.cedula " +
                "WHERE u.nombre LIKE ? OR u.apellido LIKE ? OR u.cedula LIKE ?";

        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta)) {
            String filtro = "%" + termino + "%";
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cedula = rs.getString("cedula");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String correo = rs.getString("correo");
                    String telefono = rs.getString("telefono");
                    String contraseniaCuenta = rs.getString("contraseniaCuenta");
                    Date fechaNacimiento = rs.getDate("fechaNacimiento");
                    String categoria = rs.getString("categoria");
                    String genero = rs.getString("genero");
                    int incumplePago = rs.getInt("incumplePago");
                    boolean estaBaneado = rs.getBoolean("estaBaneado");

                    Jugador jugador = new Jugador(cedula, nombre, apellido, correo, telefono,
                            contraseniaCuenta, fechaNacimiento, categoria, genero, incumplePago, estaBaneado);
                    jugadores.add(jugador);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar jugadores", e);
        }

        return jugadores;
    }
    public void banearJugador(String cedula) {
        String consulta = "UPDATE Jugador SET estaBaneado = TRUE WHERE cedula = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstancia()
                .getConnection().prepareStatement(consulta)) {
            ps.setString(1, cedula);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Jugador con cédula " + cedula + " ha sido baneado correctamente.");
            } else {
                System.out.println("No se encontró un jugador con la cédula " + cedula);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al banear jugador", e);
        }
    }

    public int cantidadJugadoresBaneados() {
        String consulta = "SELECT COUNT(*) AS total FROM Usuario u JOIN Jugador j ON u.cedula = j.cedula WHERE j.estaBaneado = TRUE";
        try (PreparedStatement ps = DatabaseConnection.getInstancia().getConnection().prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // para ver la causa real en consola
            throw new RuntimeException("Error al contar jugadores baneados", e);
        }
        return 0; // si no hay resultados
    }

}

