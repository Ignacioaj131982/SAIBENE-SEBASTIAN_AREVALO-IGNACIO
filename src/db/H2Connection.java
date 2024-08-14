package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Connection {

    private static final String URL = "jdbc:h2:./clinica_odontologica";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";

    static {
        try {

            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se pudo cargar el driver de H2", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {

            throw new SQLException("Error al obtener la conexión a la base de datos", e);
        }
    }
}

