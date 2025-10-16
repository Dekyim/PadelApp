package database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instancia;
    private Connection connection;
    private static Properties reader = new Properties();

    static {
        try {
            InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.config");

            reader.load(in);
        } catch (Exception e) {
            System.out.println("Error al cargar configuración de base de datos: " + e.getMessage());
        }
    }

    private DatabaseConnection() throws SQLException {
        try {
            // Cargar el driver JDBC de MySQL explícitamente
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión usando propiedades
            this.connection = DriverManager.getConnection(
                    reader.getProperty("db.url"),
                    reader.getProperty("db.user"),
                    reader.getProperty("db.password")
            );

        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC de MySQL - " + e.getMessage());
        } catch (SQLException e) {
            throw new SQLException("Error al conectarnos a la base de datos - " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstancia() throws SQLException {
        if (instancia != null) {
            return instancia;
        } else {
            instancia = new DatabaseConnection();
            return instancia;
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
