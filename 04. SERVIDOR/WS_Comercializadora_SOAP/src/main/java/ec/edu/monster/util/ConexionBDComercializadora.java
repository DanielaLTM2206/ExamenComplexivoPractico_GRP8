package ec.edu.monster.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBDComercializadora {
    private static final String URL = "jdbc:mysql://localhost:3306/comercializadora?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Guayaquil";
    private static final String USER = "Monster";
    private static final String PASS = "Monster9";

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
        try {
            return DriverManager.getConnection(URL, "Monster", "Monster9");
        } catch (SQLException e) {
            return DriverManager.getConnection(URL, "root", "root");
        }
    }
}
