using ec.edu.monster.Modelo;
using ec.edu.monster.Servicio;
using ec.edu.monster.Util;

namespace ec.edu.monster.Controlador;

// Vista previa de la factura: los valores definitivos los calcula el servidor (y la cuota, BanQuito)
public record ResumenFactura(decimal PrecioUnitario, decimal Subtotal, decimal Descuento, decimal Total, decimal? CuotaEstimada);

// CU-06 y CU-07: facturación en efectivo (42 % de descuento) o con crédito directo de BanQuito
public class FacturacionControlador
{
    private const decimal DescuentoEfectivo = 0.42m;
    private const double TasaMensual = 0.165 / 12;

    private readonly ServicioComercializadora servicio = new();

    public Task<celular[]> CelularesDisponiblesAsync() =>
        servicio.ListarCelularesAsync(true);

    public ResumenFactura CalcularResumen(celular cel, bool credito, int cantidad, int cuotas)
    {
        if (credito)
        {
            double monto = (double)cel.precio;
            double cuota = monto * TasaMensual / (1 - Math.Pow(1 + TasaMensual, -cuotas));
            return new ResumenFactura(cel.precio, cel.precio, 0m, cel.precio, Math.Round((decimal)cuota, 2));
        }
        decimal subtotal = cel.precio * Math.Max(cantidad, 1);
        decimal descuento = Math.Round(subtotal * DescuentoEfectivo, 2, MidpointRounding.AwayFromZero);
        return new ResumenFactura(cel.precio, subtotal, descuento, subtotal - descuento, null);
    }

    public Task<factura> FacturarAsync(string? cedula, string? nombre, celular? cel, bool credito, string? cantidad, int cuotas)
    {
        string cedulaValida = ValidadorEntrada.Cedula(cedula);
        string nombreValido = ValidadorEntrada.Texto(nombre, "nombre", 100);
        if (cel == null)
            throw new ExcepcionCliente("VAL-001", "VAL-001: Los datos ingresados no son válidos. Seleccione un celular.");

        if (credito)
        {
            int plazo = ValidadorEntrada.Entero(cuotas.ToString(), "número de cuotas", 3, 18);
            return servicio.FacturarCreditoAsync(cedulaValida, nombreValido, cel.codCelular, plazo);
        }
        int cantidadValida = ValidadorEntrada.Entero(cantidad, "cantidad", 1, 99);
        return servicio.FacturarEfectivoAsync(cedulaValida, nombreValido, cel.codCelular, cantidadValida);
    }

    public Task<factura> ObtenerFacturaAsync(string? numFactura) =>
        servicio.ObtenerFacturaAsync(ValidadorEntrada.Entero(numFactura, "número de factura", 1, int.MaxValue));
}
