namespace ec.edu.monster.Servicio;

// Error que se muestra al usuario: conserva el código del ERS (VAL-001, CRE-001, ...) que envía el servidor
public class ExcepcionCliente : Exception
{
    public string Codigo { get; }

    public ExcepcionCliente(string codigo, string mensaje) : base(mensaje)
    {
        Codigo = codigo;
    }

    // Título del diálogo según la familia del código
    public string Titulo => Codigo switch
    {
        _ when Codigo.StartsWith("VAL") => "Datos no válidos",
        _ when Codigo.StartsWith("CLI") => "Cliente no encontrado",
        _ when Codigo.StartsWith("CRE") => "Crédito no aprobado",
        _ => "Error del sistema"
    };
}
