package ec.edu.monster.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class Cuenta implements Serializable {
    private String numCuenta;
    private int codCliente;
    private BigDecimal saldo;

    public Cuenta() {}

    public Cuenta(String numCuenta, int codCliente, BigDecimal saldo) {
        this.numCuenta = numCuenta;
        this.codCliente = codCliente;
        this.saldo = saldo;
    }

    public String getNumCuenta() { return numCuenta; }
    public void setNumCuenta(String numCuenta) { this.numCuenta = numCuenta; }

    public int getCodCliente() { return codCliente; }
    public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}
