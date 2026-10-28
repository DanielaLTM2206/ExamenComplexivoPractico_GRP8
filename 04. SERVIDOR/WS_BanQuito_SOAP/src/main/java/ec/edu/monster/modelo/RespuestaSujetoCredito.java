package ec.edu.monster.modelo;

import java.io.Serializable;

public class RespuestaSujetoCredito implements Serializable {
    private boolean esSujeto;
    private Integer codCliente;
    private String motivo;

    public RespuestaSujetoCredito() {}

    public RespuestaSujetoCredito(boolean esSujeto, Integer codCliente, String motivo) {
        this.esSujeto = esSujeto;
        this.codCliente = codCliente;
        this.motivo = motivo;
    }

    public boolean isEsSujeto() { return esSujeto; }
    public void setEsSujeto(boolean esSujeto) { this.esSujeto = esSujeto; }

    public Integer getCodCliente() { return codCliente; }
    public void setCodCliente(Integer codCliente) { this.codCliente = codCliente; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
