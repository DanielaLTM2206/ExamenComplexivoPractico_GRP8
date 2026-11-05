using System.ServiceModel;
using ec.edu.monster.Modelo;
using ec.edu.monster.Util;

namespace ec.edu.monster.Servicio;

// Único punto de la app móvil que invoca a WSComercializadora (proxy WCF generado en Modelo/)
public class ServicioComercializadora
{
    private const string SinServidor =
        "Aún no se ha configurado el servidor. En la portada toque \"Cambiar servidor\" y escriba la IP de la PC donde corre Payara.";

    private static WSComercializadoraClient CrearCliente()
    {
        string url = ConfiguracionServidor.UrlActual ?? throw new ExcepcionCliente("SRV-000", SinServidor);
        var binding = new BasicHttpBinding
        {
            MaxReceivedMessageSize = int.MaxValue,
            OpenTimeout = Configuracion.TiempoEspera,
            SendTimeout = Configuracion.TiempoEspera,
            ReceiveTimeout = Configuracion.TiempoEspera
        };
        return new WSComercializadoraClient(binding, new EndpointAddress(url));
    }

    // Abre un cliente por llamada y traduce las fallas del servicio a ExcepcionCliente
    private static async Task<T> Invocar<T>(Func<WSComercializadoraClient, Task<T>> llamada)
    {
        var cliente = CrearCliente();
        try
        {
            T resultado = await llamada(cliente);
            await cliente.CloseAsync();
            return resultado;
        }
        catch (FaultException<ExcepcionComercializadora> e)
        {
            cliente.Abort();
            throw new ExcepcionCliente(e.Detail.codigo, e.Detail.message);
        }
        catch (Exception e) when (e is CommunicationException or TimeoutException)
        {
            cliente.Abort();
            Android.Util.Log.Error("ServicioComercializadora", e.ToString());
            throw new ExcepcionCliente("BDD-001",
                "BDD-001: No fue posible conectar con la comercializadora en " + ConfiguracionServidor.UrlActual +
                ". Verifique que el servidor esté encendido o corrija la dirección con \"Cambiar servidor\" en la portada.");
        }
    }

    public async Task<celular[]> ListarCelularesAsync(bool soloActivos) =>
        (await Invocar(c => c.listarCelularesAsync(soloActivos))).@return ?? Array.Empty<celular>();

    public async Task<celular> ObtenerCelularAsync(int codCelular) =>
        (await Invocar(c => c.obtenerCelularAsync(codCelular))).@return;

    public async Task<celular> CrearCelularAsync(string marca, string modelo, decimal precio) =>
        (await Invocar(c => c.crearCelularAsync(marca, modelo, precio))).@return;

    public async Task<celular> ActualizarCelularAsync(int codCelular, string marca, string modelo, decimal precio, bool activo) =>
        (await Invocar(c => c.actualizarCelularAsync(codCelular, marca, modelo, precio, activo))).@return;

    public async Task<bool> EliminarCelularAsync(int codCelular) =>
        (await Invocar(c => c.eliminarCelularAsync(codCelular))).@return;

    public async Task<factura> FacturarEfectivoAsync(string cedula, string nombre, int codCelular, int cantidad) =>
        (await Invocar(c => c.facturarEfectivoAsync(cedula, nombre, codCelular, cantidad))).@return;

    public async Task<factura> FacturarCreditoAsync(string cedula, string nombre, int codCelular, int numeroCuotas) =>
        (await Invocar(c => c.facturarCreditoAsync(cedula, nombre, codCelular, numeroCuotas))).@return;

    public async Task<factura> ObtenerFacturaAsync(int numFactura) =>
        (await Invocar(c => c.obtenerFacturaAsync(numFactura))).@return;

    public async Task<cuotaAmortizacion[]> ConsultarAmortizacionAsync(int numFactura) =>
        (await Invocar(c => c.consultarAmortizacionAsync(numFactura))).@return ?? Array.Empty<cuotaAmortizacion>();
}
