namespace ec.edu.monster.Servicio;

public static class Configuracion
{
    // IP de la computadora donde corre Payara, en la misma red Wi-Fi que el celular (ver con ipconfig).
    // En el emulador de Android usar 10.0.2.2 en lugar de la IP.
    public const string Servidor = "192.168.100.222";

    public const string UrlServicio = "http://" + Servidor + ":8080/WS_Comercializadora_SOAP/WSComercializadora";

    public static readonly TimeSpan TiempoEspera = TimeSpan.FromSeconds(30);
}
