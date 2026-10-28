package ec.edu.monster.dao;

import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.util.ConexionBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    public Cliente buscarPorCedula(String cedula) throws SQLException {
        String sql = "SELECT cod_cliente, cedula, nombre, genero, fecha_nacimiento FROM CLIENTE WHERE cedula = ?";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date fn = rs.getDate("fecha_nacimiento");
                    return new Cliente(
                        rs.getInt("cod_cliente"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("genero"),
                        fn != null ? fn.toLocalDate() : null
                    );
                }
            }
        }
        return null;
    }

    public Cliente buscarPorId(int codCliente) throws SQLException {
        String sql = "SELECT cod_cliente, cedula, nombre, genero, fecha_nacimiento FROM CLIENTE WHERE cod_cliente = ?";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date fn = rs.getDate("fecha_nacimiento");
                    return new Cliente(
                        rs.getInt("cod_cliente"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("genero"),
                        fn != null ? fn.toLocalDate() : null
                    );
                }
            }
        }
        return null;
    }
}
