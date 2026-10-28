using ec.edu.monster.Modelo;
using ec.edu.monster.Servicio;
using ec.edu.monster.Util;

namespace ec.edu.monster.Controlador;

// CU-05: mantenimiento del catálogo de celulares
public class CatalogoControlador
{
    private readonly ServicioComercializadora servicio = new();

    public Task<celular[]> ListarAsync(bool incluirDesactivados) =>
        servicio.ListarCelularesAsync(!incluirDesactivados);

    public Task<celular> ObtenerAsync(int codCelular) =>
        servicio.ObtenerCelularAsync(codCelular);

    // codCelular = 0 registra un celular nuevo; en otro caso actualiza el existente
    public Task<celular> GuardarAsync(int codCelular, string? marca, string? modelo, string? precio, bool activo)
    {
        string marcaValida = ValidadorEntrada.Texto(marca, "marca", 40);
        string modeloValido = ValidadorEntrada.Texto(modelo, "modelo", 60);
        decimal precioValido = ValidadorEntrada.DecimalPositivo(precio, "precio");
        return codCelular == 0
            ? servicio.CrearCelularAsync(marcaValida, modeloValido, precioValido)
            : servicio.ActualizarCelularAsync(codCelular, marcaValida, modeloValido, precioValido, activo);
    }

    // Borrado físico si no tiene ventas; si las tiene, el servidor lo desactiva
    public Task<bool> EliminarAsync(int codCelular) =>
        servicio.EliminarCelularAsync(codCelular);
}
