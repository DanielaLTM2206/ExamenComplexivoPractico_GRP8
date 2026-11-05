using Android.OS;

namespace ec.edu.monster.Util;

/// <summary>
/// Dirección de WSComercializadora guardada en las preferencias del teléfono, para que el usuario pueda
/// cambiarla cuando la PC servidor cambia de red (la IP de la PC no es fija).
/// </summary>
public static class ConfiguracionServidor
{
    private const string ArchivoPreferencias = "configuracion_servidor";
    private const string ClaveUrl = "UrlServidorSoap";

    // En el emulador de Android, 10.0.2.2 apunta al localhost de la PC donde corre Payara
    private const string IpEmulador = "10.0.2.2";

    private static ISharedPreferences Preferencias =>
        Application.Context.GetSharedPreferences(ArchivoPreferencias, FileCreationMode.Private)!;

    /// <summary>En un celular real no hay dirección por defecto: la IP de la PC se escribe en "Cambiar servidor".</summary>
    public static string? UrlPorDefecto => EsEmulador ? DireccionServidor.Completar(IpEmulador) : null;

    public static string? UrlActual
    {
        get => Preferencias.GetString(ClaveUrl, null) ?? UrlPorDefecto;
        set
        {
            var editor = Preferencias.Edit()!;
            if (string.IsNullOrWhiteSpace(value))
            {
                editor.Remove(ClaveUrl);
            }
            else
            {
                editor.PutString(ClaveUrl, value.Trim().TrimEnd('/'));
            }
            editor.Apply();
        }
    }

    public static bool EstaConfigurado => !string.IsNullOrEmpty(UrlActual);

    // Huellas del emulador oficial de Android Studio (goldfish/ranchu, imágenes sdk_gphone)
    public static bool EsEmulador =>
            (Build.Fingerprint ?? "").StartsWith("generic", StringComparison.OrdinalIgnoreCase)
            || (Build.Hardware ?? "").Contains("goldfish", StringComparison.OrdinalIgnoreCase)
            || (Build.Hardware ?? "").Contains("ranchu", StringComparison.OrdinalIgnoreCase)
            || (Build.Model ?? "").Contains("sdk_gphone", StringComparison.OrdinalIgnoreCase)
            || (Build.Model ?? "").Contains("Emulator", StringComparison.OrdinalIgnoreCase)
            || (Build.Product ?? "").Contains("sdk", StringComparison.OrdinalIgnoreCase);
}
