package ec.edu.monster.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/banquito?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Guayaquil";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
