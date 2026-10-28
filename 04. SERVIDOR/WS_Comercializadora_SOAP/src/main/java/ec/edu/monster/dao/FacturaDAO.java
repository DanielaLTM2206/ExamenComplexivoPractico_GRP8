package ec.edu.monster.dao;

import ec.edu.monster.modelo.DetalleFactura;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.util.ConexionBDComercializadora;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FacturaDAO {

    // Mismo formato con el que MySQL devuelve un DATETIME, para que la fecha se vea igual al emitir y al consultar
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Factura registrarFactura(Factura factura, DetalleFactura detalle) throws SQLException {
        String sqlFactura = "INSERT INTO FACTURA (cedula, nombre, fecha, forma_pago, subtotal, descuento, total, cod_credito) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime fecha = LocalDateTime.now().withNano(0);
        String sqlDetalle = "INSERT INTO DETALLE_FACTURA (num_factura, cod_celular, cantidad, precio_unitario, subtotal) " +
                            "VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = ConexionBDComercializadora.obtenerConexion()) {
            cn.setAutoCommit(false);
            try {
                int numFacturaGenerado;
                try (PreparedStatement psFact = cn.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS)) {
                    psFact.setString(1, factura.getCedula());
                    psFact.setString(2, factura.getNombre());
                    psFact.setTimestamp(3, Timestamp.valueOf(fecha));
                    psFact.setString(4, factura.getFormaPago());
                    psFact.setBigDecimal(5, factura.getSubtotal());
                    psFact.setBigDecimal(6, factura.getDescuento());
                    psFact.setBigDecimal(7, factura.getTotal());
                    if (factura.getCodCredito() != null) {
                        psFact.setInt(8, factura.getCodCredito());
                    } else {
                        psFact.setNull(8, Types.INTEGER);
                    }
                    psFact.executeUpdate();

                    try (ResultSet rs = psFact.getGeneratedKeys()) {
                        if (rs.next()) {
                            numFacturaGenerado = rs.getInt(1);
                        } else {
                            throw new SQLException("No se pudo obtener el número de factura generado.");
                        }
                    }
                }

                try (PreparedStatement psDet = cn.prepareStatement(sqlDetalle)) {
                    psDet.setInt(1, numFacturaGenerado);
                    psDet.setInt(2, detalle.getCodCelular());
                    psDet.setInt(3, detalle.getCantidad());
                    psDet.setBigDecimal(4, detalle.getPrecioUnitario());
                    psDet.setBigDecimal(5, detalle.getSubtotal());
                    psDet.executeUpdate();
                }

                cn.commit();
                factura.setNumFactura(numFacturaGenerado);
                factura.setFecha(fecha.format(FORMATO_FECHA));
                detalle.setNumFactura(numFacturaGenerado);
                factura.getDetalles().clear();
                factura.getDetalles().add(detalle);
                return factura;

            } catch (SQLException e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    public Factura obtenerFactura(int numFactura) throws SQLException {
        String sqlFact = "SELECT num_factura, cedula, nombre, fecha, forma_pago, subtotal, descuento, total, cod_credito " +
                         "FROM FACTURA WHERE num_factura = ?";
        String sqlDet = "SELECT d.num_factura, d.cod_celular, c.marca, c.modelo, d.cantidad, d.precio_unitario, d.subtotal " +
                        "FROM DETALLE_FACTURA d " +
                        "JOIN CELULAR c ON d.cod_celular = c.cod_celular " +
                        "WHERE d.num_factura = ?";

        try (Connection cn = ConexionBDComercializadora.obtenerConexion();
             PreparedStatement psFact = cn.prepareStatement(sqlFact)) {
            psFact.setInt(1, numFactura);
            try (ResultSet rsFact = psFact.executeQuery()) {
                if (rsFact.next()) {
                    Factura f = new Factura(
                        rsFact.getInt("num_factura"),
                        rsFact.getString("cedula"),
                        rsFact.getString("nombre"),
                        rsFact.getString("fecha"),
                        rsFact.getString("forma_pago"),
                        rsFact.getBigDecimal("subtotal"),
                        rsFact.getBigDecimal("descuento"),
                        rsFact.getBigDecimal("total"),
                        rsFact.getObject("cod_credito") != null ? rsFact.getInt("cod_credito") : null
                    );

                    try (PreparedStatement psDet = cn.prepareStatement(sqlDet)) {
                        psDet.setInt(1, numFactura);
                        try (ResultSet rsDet = psDet.executeQuery()) {
                            while (rsDet.next()) {
                                f.getDetalles().add(new DetalleFactura(
                                    rsDet.getInt("num_factura"),
                                    rsDet.getInt("cod_celular"),
                                    rsDet.getString("marca"),
                                    rsDet.getString("modelo"),
                                    rsDet.getInt("cantidad"),
                                    rsDet.getBigDecimal("precio_unitario"),
                                    rsDet.getBigDecimal("subtotal")
                                ));
                            }
                        }
                    }
                    return f;
                }
            }
        }
        return null;
    }
}
