using System.Globalization;
using System.Text.RegularExpressions;
using ec.edu.monster.Servicio;

namespace ec.edu.monster.Util;

// Validación previa en el cliente: evita llamar al servicio con datos que ya se sabe que son inválidos
public static class ValidadorEntrada
{
    public static string Texto(string? valor, string campo, int maximo)
    {
        string limpio = valor?.Trim() ?? "";
        if (limpio.Length == 0)
            throw Invalido($"El campo {campo} es obligatorio.");
        if (limpio.Length > maximo)
            throw Invalido($"El campo {campo} admite máximo {maximo} caracteres.");
        return limpio;
    }

    public static int Entero(string? valor, string campo, int minimo, int maximo)
    {
        if (!int.TryParse(valor?.Trim(), NumberStyles.None, CultureInfo.InvariantCulture, out int numero))
            throw Invalido($"El campo {campo} debe ser un número entero.");
        if (numero < minimo || numero > maximo)
            throw Invalido($"El campo {campo} debe estar entre {minimo} y {maximo}.");
        return numero;
    }

    public static decimal DecimalPositivo(string? valor, string campo)
    {
        string texto = (valor ?? "").Trim().Replace(',', '.');
        if (!Regex.IsMatch(texto, @"^\d+(\.\d{1,2})?$")
            || !decimal.TryParse(texto, NumberStyles.AllowDecimalPoint, CultureInfo.InvariantCulture, out decimal numero)
            || numero <= 0)
            throw Invalido($"El campo {campo} debe ser un número mayor a cero con máximo dos decimales (ej.: 299.99).");
        return numero;
    }

    // Cédula ecuatoriana: 10 dígitos, provincia 01-24 o 30, tercer dígito menor a 6 y dígito verificador módulo 10
    public static string Cedula(string? valor)
    {
        string ced = valor?.Trim() ?? "";
        if (!Regex.IsMatch(ced, @"^\d{10}$"))
            throw Invalido("La cédula debe tener exactamente 10 dígitos.");
        int provincia = int.Parse(ced[..2]);
        bool valida = ((provincia >= 1 && provincia <= 24) || provincia == 30) && ced[2] - '0' < 6;
        int suma = 0;
        for (int i = 0; i < 9; i++)
        {
            int producto = (ced[i] - '0') * (i % 2 == 0 ? 2 : 1);
            suma += producto > 9 ? producto - 9 : producto;
        }
        if (!valida || (10 - suma % 10) % 10 != ced[9] - '0')
            throw Invalido($"La cédula {ced} no es válida (dígito verificador incorrecto).");
        return ced;
    }

    private static ExcepcionCliente Invalido(string detalle) =>
        new("VAL-001", "VAL-001: Los datos ingresados no son válidos. " + detalle);
}
