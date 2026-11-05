package ec.edu.monster.modelo;

import java.io.Serializable;

// Un renglón de la venta que envía el cliente: qué celular y cuántas unidades
public class ItemVenta implements Serializable {
    private int codCelular;
    private int cantidad;

    public ItemVenta() {}

    public ItemVenta(int codCelular, int cantidad) {
        this.codCelular = codCelular;
        this.cantidad = cantidad;
    }

    public int getCodCelular() { return codCelular; }
    public void setCodCelular(int codCelular) { this.codCelular = codCelular; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
