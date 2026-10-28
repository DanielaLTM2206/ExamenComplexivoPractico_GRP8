package ec.edu.monster.dao;

import ec.edu.monster.modelo.Credito;
import ec.edu.monster.modelo.CuotaAmortizacion;
import ec.edu.monster.util.ConexionBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreditoDAO {

    public boolean tieneCreditoActivo(int codCliente) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CREDITO WHERE cod_cliente = ? AND estado = 'ACTIVO'";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public int registrarCreditoConAmortizacion(Credito credito, List<CuotaAmortizacion> amortizacion) throws SQLException {
        String sqlCredito = "INSERT INTO CREDITO (cod_cliente, monto, num_cuotas, tasa_anual, valor_cuota, fecha, estado) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlAmortizacion = "INSERT INTO AMORTIZACION (cod_credito, num_cuota, valor_cuota, interes, capital, saldo) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection cn = ConexionBD.obtenerConexion()) {
            cn.setAutoCommit(false);
            try {
                int codCreditoGenerado;
                try (PreparedStatement psCred = cn.prepareStatement(sqlCredito, Statement.RETURN_GENERATED_KEYS)) {
                    psCred.setInt(1, credito.getCodCliente());
                    psCred.setBigDecimal(2, credito.getMonto());
                    psCred.setInt(3, credito.getNumCuotas());
                    psCred.setBigDecimal(4, credito.getTasaAnual());
                    psCred.setBigDecimal(5, credito.getValorCuota());
                    psCred.setDate(6, Date.valueOf(credito.getFecha()));
                    psCred.setString(7, credito.getEstado() != null ? credito.getEstado() : "ACTIVO");
                    psCred.executeUpdate();

                    try (ResultSet rsKeys = psCred.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            codCreditoGenerado = rsKeys.getInt(1);
                        } else {
                            throw new SQLException("No se pudo obtener el código del crédito generado.");
                        }
                    }
                }

                try (PreparedStatement psAmort = cn.prepareStatement(sqlAmortizacion)) {
                    for (CuotaAmortizacion cuota : amortizacion) {
                        psAmort.setInt(1, codCreditoGenerado);
                        psAmort.setInt(2, cuota.getNumCuota());
                        psAmort.setBigDecimal(3, cuota.getValorCuota());
                        psAmort.setBigDecimal(4, cuota.getInteres());
                        psAmort.setBigDecimal(5, cuota.getCapital());
                        psAmort.setBigDecimal(6, cuota.getSaldo());
                        psAmort.addBatch();
                    }
                    psAmort.executeBatch();
                }

                cn.commit();
                return codCreditoGenerado;
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    public List<CuotaAmortizacion> consultarTablaAmortizacion(int codCredito) throws SQLException {
        List<CuotaAmortizacion> lista = new ArrayList<>();
        String sql = "SELECT num_cuota, valor_cuota, interes, capital, saldo " +
                     "FROM AMORTIZACION WHERE cod_credito = ? ORDER BY num_cuota ASC";
        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codCredito);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new CuotaAmortizacion(
                        rs.getInt("num_cuota"),
                        rs.getBigDecimal("valor_cuota"),
                        rs.getBigDecimal("interes"),
                        rs.getBigDecimal("capital"),
                        rs.getBigDecimal("saldo")
                    ));
                }
            }
        }
        return lista;
    }
}
