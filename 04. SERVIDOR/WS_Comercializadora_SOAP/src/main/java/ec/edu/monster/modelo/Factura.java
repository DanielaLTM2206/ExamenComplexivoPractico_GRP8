package ec.edu.monster.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Factura implements Serializable {
    private int numFactura;
    private String cedula;
    private String nombre;
    private String fecha; // Formato String para transporte limpio en SOAP
    private String formaPago; // EFECTIVO o CREDITO
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private Integer codCredito; // Opcional (solo CREDITO)
    private List<DetalleFactura> detalles = new ArrayList<>();

    public Factura() {}

    public Factura(int numFactura, String cedula, String nombre, String fecha, String formaPago,
                   BigDecimal subtotal, BigDecimal descuento, BigDecimal total, Integer codCredito) {
        this.numFactura = numFactura;
        this.cedula = cedula;
        this.nombre = nombre;
        this.fecha = fecha;
        this.formaPago = formaPago;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.codCredito = codCredito;
    }

    public int getNumFactura() { return numFactura; }
    public void setNumFactura(int numFactura) { this.numFactura = numFactura; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Integer getCodCredito() { return codCredito; }
    public void setCodCredito(Integer codCredito) { this.codCredito = codCredito; }

    public List<DetalleFactura> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFactura> detalles) { this.detalles = detalles; }
}
