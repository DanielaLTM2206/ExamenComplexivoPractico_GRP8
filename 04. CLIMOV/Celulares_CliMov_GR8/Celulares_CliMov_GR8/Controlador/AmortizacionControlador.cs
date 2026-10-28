using ec.edu.monster.Modelo;
using ec.edu.monster.Servicio;
using ec.edu.monster.Util;

namespace ec.edu.monster.Controlador;

public record TablaAmortizacion(factura Factura, cuotaAmortizacion[] Cuotas, decimal TotalPagado, decimal TotalInteres, decimal TotalCapital);

// CU-08: tabla de amortización del crédito asociado a una factura
public class AmortizacionControlador
{
    private readonly ServicioComercializadora servicio = new();

    public async Task<TablaAmortizacion> ConsultarAsync(string? numFactura)
    {
        int numero = ValidadorEntrada.Entero(numFactura, "número de factura", 1, int.MaxValue);
        // Primero la tabla: si la factura no existe o fue en efectivo, el servidor responde VAL-001
        cuotaAmortizacion[] cuotas = await servicio.ConsultarAmortizacionAsync(numero);
        factura factura = await servicio.ObtenerFacturaAsync(numero);
        return new TablaAmortizacion(factura, cuotas,
            cuotas.Sum(c => c.valorCuota), cuotas.Sum(c => c.interes), cuotas.Sum(c => c.capital));
    }
}
