package ec.edu.monster.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class RespuestaCredito implements Serializable {
    private boolean exito;
    private int codCredito;
    private BigDecimal valorCuota;
    private String mensaje;
    private List<CuotaAmortizacion> amortizacion;

    public RespuestaCredito() {}

    public RespuestaCredito(boolean exito, int codCredito, BigDecimal valorCuota, String mensaje, List<CuotaAmortizacion> amortizacion) {
        this.exito = exito;
        this.codCredito = codCredito;
        this.valorCuota = valorCuota;
        this.mensaje = mensaje;
        this.amortizacion = amortizacion;
    }

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public int getCodCredito() { return codCredito; }
    public void setCodCredito(int codCredito) { this.codCredito = codCredito; }

    public BigDecimal getValorCuota() { return valorCuota; }
    public void setValorCuota(BigDecimal valorCuota) { this.valorCuota = valorCuota; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public List<CuotaAmortizacion> getAmortizacion() { return amortizacion; }
    public void setAmortizacion(List<CuotaAmortizacion> amortizacion) { this.amortizacion = amortizacion; }
}
