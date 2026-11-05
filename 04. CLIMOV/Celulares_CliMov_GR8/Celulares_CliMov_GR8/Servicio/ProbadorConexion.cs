namespace ec.edu.monster.Servicio;

public record ResultadoConexion(bool Exitosa, string Mensaje);

/// <summary>
/// Comprueba que en una dirección responda WSComercializadora pidiendo su WSDL, antes de guardarla.
/// Distingue un servidor apagado o bloqueado por el firewall de una ruta equivocada.
/// </summary>
public class ProbadorConexion
{
    private static readonly TimeSpan TiempoEspera = TimeSpan.FromSeconds(6);

    public async Task<ResultadoConexion> ProbarAsync(string urlServicio)
    {
        string url = urlServicio.Trim().TrimEnd('/');
        using var clienteHttp = new HttpClient { Timeout = TiempoEspera };

        try
        {
            using HttpResponseMessage respuesta = await clienteHttp.GetAsync(url + "?wsdl");
            if (!respuesta.IsSuccessStatusCode)
            {
                return new ResultadoConexion(false,
                    $"El equipo en {url} respondió con el código {(int)respuesta.StatusCode}. " +
                    "Revise que la dirección termine en /WS_Comercializadora_SOAP/WSComercializadora " +
                    "y que el servicio esté desplegado en Payara.");
            }

            string contenido = await respuesta.Content.ReadAsStringAsync();
            if (!contenido.Contains("WSComercializadora", StringComparison.Ordinal))
            {
                return new ResultadoConexion(false,
                    $"La dirección {url} responde, pero no es el servicio SOAP WSComercializadora.");
            }
            return new ResultadoConexion(true, $"Conexión correcta con la comercializadora en {url}.");
        }
        catch (TaskCanceledException)
        {
            return new ResultadoConexion(false,
                $"El servidor en {url} no respondió en {TiempoEspera.TotalSeconds:0} segundos. " +
                "Verifique la IP, que el celular y la PC estén en la misma red Wi-Fi y que el firewall " +
                "de la PC permita el puerto 8080 (ejecute PREPARAR_SERVIDOR_SOAP_8080.bat).");
        }
        catch (HttpRequestException)
        {
            return new ResultadoConexion(false,
                $"No fue posible conectar con {url}. " +
                "Verifique que Payara esté encendido, la IP y el puerto, y el firewall de la PC servidor.");
        }
    }
}
