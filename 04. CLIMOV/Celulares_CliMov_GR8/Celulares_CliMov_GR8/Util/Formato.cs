using System.Globalization;

namespace ec.edu.monster.Util;

public static class Formato
{
    private static readonly CultureInfo Ecuador = CultureInfo.GetCultureInfo("es-EC");

    // Dólares con separadores de Ecuador: $1.049,00
    public static string Moneda(decimal valor) => "$" + valor.ToString("N2", Ecuador);
}
