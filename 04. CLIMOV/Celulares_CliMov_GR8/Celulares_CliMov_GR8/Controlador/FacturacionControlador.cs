using ec.edu.monster.Modelo;
using ec.edu.monster.Servicio;
using ec.edu.monster.Util;

namespace ec.edu.monster.Controlador;

// Vista previa de la factura: los valores definitivos los calcula el servidor (y la cuota, BanQuito)
public record ResumenFactura(int Unidades, decimal Subtotal, decimal Descuento, decimal Total, decimal? CuotaEstimada);

// CU-06 y CU-07: facturación de uno o varios celulares en efectivo (42 % de descuento) o con crédito directo de BanQuito
public class FacturacionControlador
{
    private const decimal DescuentoEfectivo = 0.42m;
    private const double TasaMensual = 0.165 / 12;

    private readonly ServicioComercializadora servicio = new();

    public Task<celular[]> CelularesDisponiblesAsync() =>
        servicio.ListarCelularesAsync(true);

    /// <summary>Agrega el celular elegido al carrito; valida que haya uno seleccionado y la cantidad (1 a 99).</summary>
    public void AgregarAlCarrito(CarritoVenta carrito, celular? cel, string? cantidad)
    {
        if (cel == null)
            throw new ExcepcionCliente("VAL-001", "VAL-001: Los datos ingresados no son válidos. Seleccione el celular que desea agregar.");
        carrito.Agregar(cel, ValidadorEntrada.Entero(cantidad, "cantidad", 1, CarritoVenta.MaxCantidad));
    }

    public ResumenFactura CalcularResumen(CarritoVenta carrito, bool credito, int cuotas)
    {
        decimal subtotal = carrito.Subtotal;
        if (credito)
        {
            double monto = (double)subtotal;
            decimal? cuota = subtotal > 0
                ? Math.Round((decimal)(monto * TasaMensual / (1 - Math.Pow(1 + TasaMensual, -cuotas))), 2)
                : null;
            return new ResumenFactura(carrito.Unidades, subtotal, 0m, subtotal, cuota);
        }
        decimal descuento = Math.Round(subtotal * DescuentoEfectivo, 2, MidpointRounding.AwayFromZero);
        return new ResumenFactura(carrito.Unidades, subtotal, descuento, subtotal - descuento, null);
    }

    public Task<factura> FacturarAsync(string? cedula, string? nombre, CarritoVenta carrito, bool credito, int cuotas)
    {
        string cedulaValida = ValidadorEntrada.Cedula(cedula);
        string nombreValido = ValidadorEntrada.Texto(nombre, "nombre", 100);
        if (carrito.EstaVacio)
            throw new ExcepcionCliente("VAL-001", "VAL-001: Los datos ingresados no son válidos. Agregue al menos un celular a la venta.");

        if (credito)
        {
            int plazo = ValidadorEntrada.Entero(cuotas.ToString(), "número de cuotas", 3, 18);
            return servicio.FacturarVentaCreditoAsync(cedulaValida, nombreValido, carrito.ComoItemsVenta(), plazo);
        }
        return servicio.FacturarVentaEfectivoAsync(cedulaValida, nombreValido, carrito.ComoItemsVenta());
    }

    public Task<factura> ObtenerFacturaAsync(string? numFactura) =>
        servicio.ObtenerFacturaAsync(ValidadorEntrada.Entero(numFactura, "número de factura", 1, int.MaxValue));
}
