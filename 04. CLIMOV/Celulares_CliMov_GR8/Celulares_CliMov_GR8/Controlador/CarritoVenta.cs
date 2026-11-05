using ec.edu.monster.Modelo;
using ec.edu.monster.Servicio;

namespace ec.edu.monster.Controlador;

public record ItemCarrito(celular Celular, int Cantidad)
{
    public decimal Subtotal => Celular.precio * Cantidad;
}

// Celulares elegidos para la venta, con las mismas reglas del servidor: el mismo celular se suma,
// máximo 10 celulares distintos y de 1 a 99 unidades de cada uno
public class CarritoVenta
{
    public const int MaxCelulares = 10;
    public const int MaxCantidad = 99;

    private readonly List<ItemCarrito> items = new();

    public IReadOnlyList<ItemCarrito> Items => items;
    public bool EstaVacio => items.Count == 0;
    public int Unidades => items.Sum(i => i.Cantidad);
    public decimal Subtotal => items.Sum(i => i.Subtotal);

    public void Agregar(celular cel, int cantidad)
    {
        int posicion = items.FindIndex(i => i.Celular.codCelular == cel.codCelular);
        if (posicion >= 0)
        {
            int total = items[posicion].Cantidad + cantidad;
            if (total > MaxCantidad)
                throw Invalido($"La cantidad de cada celular no puede superar {MaxCantidad}.");
            items[posicion] = items[posicion] with { Cantidad = total };
            return;
        }
        if (items.Count >= MaxCelulares)
            throw Invalido($"Una factura admite máximo {MaxCelulares} celulares distintos.");
        items.Add(new ItemCarrito(cel, cantidad));
    }

    public void Quitar(int posicion) => items.RemoveAt(posicion);

    public itemVenta[] ComoItemsVenta() =>
        items.Select(i => new itemVenta { codCelular = i.Celular.codCelular, cantidad = i.Cantidad }).ToArray();

    private static ExcepcionCliente Invalido(string detalle) =>
        new("VAL-001", "VAL-001: Los datos ingresados no son válidos. " + detalle);
}
