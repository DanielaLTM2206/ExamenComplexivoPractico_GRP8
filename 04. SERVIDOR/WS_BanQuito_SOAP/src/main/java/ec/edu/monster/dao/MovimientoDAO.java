package ec.edu.monster.dao;

import ec.edu.monster.util.ConexionBD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovimientoDAO {

    public int contarDepositosUltimoMes(int codCliente) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MOVIMIENTO m " +
                     "JOIN CUENTA c ON m.num_cuenta = c.num_cuenta " +
                     "WHERE c.cod_cliente = ? AND m.tipo = 'DEP' " +
                     "AND m.fecha >= CURDATE() - INTERVAL 30 DAY";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public BigDecimal calcularPromedioUltimos3Meses(int codCliente, String tipo) throws SQLException {
        String sql = "SELECT COALESCE(AVG(m.valor), 0) FROM MOVIMIENTO m " +
                     "JOIN CUENTA c ON m.num_cuenta = c.num_cuenta " +
                     "WHERE c.cod_cliente = ? AND m.tipo = ? " +
                     "AND m.fecha >= CURDATE() - INTERVAL 90 DAY";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            ps.setString(2, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal promedio = rs.getBigDecimal(1);
                    return promedio != null ? promedio.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2);
                }
            }
        }
        return BigDecimal.ZERO.setScale(2);
    }
}
