package ec.edu.monster.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Movimiento implements Serializable {
    private int codMovimiento;
    private String numCuenta;
    private String tipo; // DEP o RET
    private BigDecimal valor;
    private LocalDate fecha;

    public Movimiento() {}

    public Movimiento(int codMovimiento, String numCuenta, String tipo, BigDecimal valor, LocalDate fecha) {
        this.codMovimiento = codMovimiento;
        this.numCuenta = numCuenta;
        this.tipo = tipo;
        this.valor = valor;
        this.fecha = fecha;
    }

    public int getCodMovimiento() { return codMovimiento; }
    public void setCodMovimiento(int codMovimiento) { this.codMovimiento = codMovimiento; }

    public String getNumCuenta() { return numCuenta; }
    public void setNumCuenta(String numCuenta) { this.numCuenta = numCuenta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
