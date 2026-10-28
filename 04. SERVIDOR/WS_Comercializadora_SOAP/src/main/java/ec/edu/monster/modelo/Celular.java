package ec.edu.monster.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class Celular implements Serializable {
    private int codCelular;
    private String marca;
    private String modelo;
    private BigDecimal precio;
    private int activo; // 1 = activo, 0 = inactivo (borrado lógico)

    public Celular() {}

    public Celular(int codCelular, String marca, String modelo, BigDecimal precio, int activo) {
        this.codCelular = codCelular;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.activo = activo;
    }

    public int getCodCelular() { return codCelular; }
    public void setCodCelular(int codCelular) { this.codCelular = codCelular; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getActivo() { return activo; }
    public void setActivo(int activo) { this.activo = activo; }
}
