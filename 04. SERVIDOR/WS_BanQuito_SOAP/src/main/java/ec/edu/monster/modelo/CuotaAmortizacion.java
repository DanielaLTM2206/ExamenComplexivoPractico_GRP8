package ec.edu.monster.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class CuotaAmortizacion implements Serializable {
    private int numCuota;
    private BigDecimal valorCuota;
    private BigDecimal interes;
    private BigDecimal capital;
    private BigDecimal saldo;

    public CuotaAmortizacion() {}

    public CuotaAmortizacion(int numCuota, BigDecimal valorCuota, BigDecimal interes, BigDecimal capital, BigDecimal saldo) {
        this.numCuota = numCuota;
        this.valorCuota = valorCuota;
        this.interes = interes;
        this.capital = capital;
        this.saldo = saldo;
    }

    public int getNumCuota() { return numCuota; }
    public void setNumCuota(int numCuota) { this.numCuota = numCuota; }

    public BigDecimal getValorCuota() { return valorCuota; }
    public void setValorCuota(BigDecimal valorCuota) { this.valorCuota = valorCuota; }

    public BigDecimal getInteres() { return interes; }
    public void setInteres(BigDecimal interes) { this.interes = interes; }

    public BigDecimal getCapital() { return capital; }
    public void setCapital(BigDecimal capital) { this.capital = capital; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}
