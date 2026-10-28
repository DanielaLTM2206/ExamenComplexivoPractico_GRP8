package ec.edu.monster.dao;

import ec.edu.monster.modelo.Celular;
import ec.edu.monster.util.ConexionBDComercializadora;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CelularDAO {

    public Celular crearCelular(String marca, String modelo, BigDecimal precio) throws SQLException {
        String sql = "INSERT INTO CELULAR (marca, modelo, precio, activo) VALUES (?, ?, ?, 1)";
        try (Connection cn = ConexionBDComercializadora.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, marca);
            ps.setString(2, modelo);
            ps.setBigDecimal(3, precio);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Celular(rs.getInt(1), marca, modelo, precio, true);
                }
            }
        }
        return null;
    }

    public List<Celular> listarCelulares(boolean soloActivos) throws SQLException {
        List<Celular> lista = new ArrayList<>();
        String sql = soloActivos 
            ? "SELECT cod_celular, marca, modelo, precio, activo FROM CELULAR WHERE activo = 1 ORDER BY marca, modelo"
            : "SELECT cod_celular, marca, modelo, precio, activo FROM CELULAR ORDER BY cod_celular ASC";
        try (Connection cn = ConexionBDComercializadora.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Celular(
                    rs.getInt("cod_celular"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getBigDecimal("precio"),
                    rs.getBoolean("activo")
                ));
            }
        }
        return lista;
    }

    public Celular buscarPorId(int codCelular) throws SQLException {
        String sql = "SELECT cod_celular, marca, modelo, precio, activo FROM CELULAR WHERE cod_celular = ?";
        try (Connection cn = ConexionBDComercializadora.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codCelular);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Celular(
                        rs.getInt("cod_celular"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getBigDecimal("precio"),
                        rs.getBoolean("activo")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizarCelular(int codCelular, String marca, String modelo, BigDecimal precio, boolean activo) throws SQLException {
        String sql = "UPDATE CELULAR SET marca = ?, modelo = ?, precio = ?, activo = ? WHERE cod_celular = ?";
        try (Connection cn = ConexionBDComercializadora.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, marca);
            ps.setString(2, modelo);
            ps.setBigDecimal(3, precio);
            ps.setBoolean(4, activo);
            ps.setInt(5, codCelular);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarCelular(int codCelular) throws SQLException {
        // 1. Verificar si el celular ya ha sido facturado
        String sqlCheck = "SELECT COUNT(*) FROM DETALLE_FACTURA WHERE cod_celular = ?";
        try (Connection cn = ConexionBDComercializadora.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sqlCheck)) {
            ps.setInt(1, codCelular);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Borrado Lógico: Preserva integridad referencial histórica
                    String sqlLogic = "UPDATE CELULAR SET activo = 0 WHERE cod_celular = ?";
                    try (PreparedStatement ps2 = cn.prepareStatement(sqlLogic)) {
                        ps2.setInt(1, codCelular);
                        return ps2.executeUpdate() > 0;
                    }
                }
            }
        }

        // 2. Si no tiene facturas asociadas, se realiza borrado físico
        String sqlDelete = "DELETE FROM CELULAR WHERE cod_celular = ?";
        try (Connection cn = ConexionBDComercializadora.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sqlDelete)) {
            ps.setInt(1, codCelular);
            return ps.executeUpdate() > 0;
        }
    }
}
