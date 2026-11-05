using ec.edu.monster.Servicio;
using ec.edu.monster.Util;

namespace ec.edu.monster.Controlador;

/// <summary>
/// "Cambiar servidor": completa la dirección escrita, la prueba y la guarda en las preferencias del teléfono.
/// </summary>
public class ServidorControlador
{
    private readonly ProbadorConexion probador = new();

    public string? UrlActual => ConfiguracionServidor.UrlActual;

    public string? UrlPorDefecto => ConfiguracionServidor.UrlPorDefecto;

    /// <returns>La dirección completa (192.168.1.25 → http://192.168.1.25:8080/WS_Comercializadora_SOAP/WSComercializadora).</returns>
    /// <exception cref="ExcepcionCliente">VAL-001 si la dirección no es válida.</exception>
    public string CompletarUrl(string? entrada)
    {
        try
        {
            return DireccionServidor.Completar(entrada);
        }
        catch (ArgumentException e)
        {
            throw new ExcepcionCliente("VAL-001", e.Message);
        }
    }

    public Task<ResultadoConexion> ProbarAsync(string urlCompleta) => probador.ProbarAsync(urlCompleta);

    public void Guardar(string urlCompleta) => ConfiguracionServidor.UrlActual = urlCompleta;

    /// <summary>Borra la dirección guardada: vuelve a 10.0.2.2 en el emulador o queda sin configurar en un celular.</summary>
    public void Restablecer() => ConfiguracionServidor.UrlActual = null;
}
