package ec.edu.monster.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetalleFactura implements Serializable {
    private int numFactura;
    private int codCelular;
    private String marcaCelular;
    private String modeloCelular;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetalleFactura() {}

    public DetalleFactura(int numFactura, int codCelular, String marcaCelular, String modeloCelular,
                          int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.numFactura = numFactura;
        this.codCelular = codCelular;
        this.marcaCelular = marcaCelular;
        this.modeloCelular = modeloCelular;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public int getNumFactura() { return numFactura; }
    public void setNumFactura(int numFactura) { this.numFactura = numFactura; }

    public int getCodCelular() { return codCelular; }
    public void setCodCelular(int codCelular) { this.codCelular = codCelular; }

    public String getMarcaCelular() { return marcaCelular; }
    public void setMarcaCelular(String marcaCelular) { this.marcaCelular = marcaCelular; }

    public String getModeloCelular() { return modeloCelular; }
    public void setModeloCelular(String modeloCelular) { this.modeloCelular = modeloCelular; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
