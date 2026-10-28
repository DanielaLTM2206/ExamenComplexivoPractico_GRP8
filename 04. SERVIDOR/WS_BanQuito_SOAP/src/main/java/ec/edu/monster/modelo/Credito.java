package ec.edu.monster.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Credito implements Serializable {
    private int codCredito;
    private int codCliente;
    private BigDecimal monto;
    private int numCuotas;
    private BigDecimal tasaAnual;
    private BigDecimal valorCuota;
    private LocalDate fecha;
    private String estado; // ACTIVO o CANCELADO

    public Credito() {}

    public Credito(int codCredito, int codCliente, BigDecimal monto, int numCuotas,
                   BigDecimal tasaAnual, BigDecimal valorCuota, LocalDate fecha, String estado) {
        this.codCredito = codCredito;
        this.codCliente = codCliente;
        this.monto = monto;
        this.numCuotas = numCuotas;
        this.tasaAnual = tasaAnual;
        this.valorCuota = valorCuota;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getCodCredito() { return codCredito; }
    public void setCodCredito(int codCredito) { this.codCredito = codCredito; }

    public int getCodCliente() { return codCliente; }
    public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public int getNumCuotas() { return numCuotas; }
    public void setNumCuotas(int numCuotas) { this.numCuotas = numCuotas; }

    public BigDecimal getTasaAnual() { return tasaAnual; }
    public void setTasaAnual(BigDecimal tasaAnual) { this.tasaAnual = tasaAnual; }

    public BigDecimal getValorCuota() { return valorCuota; }
    public void setValorCuota(BigDecimal valorCuota) { this.valorCuota = valorCuota; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
