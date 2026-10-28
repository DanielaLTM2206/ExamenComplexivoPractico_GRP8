using AndroidX.Core.Content;
using AndroidX.RecyclerView.Widget;
using ec.edu.monster.Modelo;
using ec.edu.monster.Util;

namespace ec.edu.monster.Vista;

// Tarjetas del catálogo con acciones Editar y Eliminar
public class CelularAdapter : RecyclerView.Adapter
{
    private readonly List<celular> celulares = new();
    private readonly Action<celular> alEditar;
    private readonly Action<celular> alEliminar;

    public CelularAdapter(Action<celular> alEditar, Action<celular> alEliminar)
    {
        this.alEditar = alEditar;
        this.alEliminar = alEliminar;
    }

    public void Actualizar(IEnumerable<celular> lista)
    {
        celulares.Clear();
        celulares.AddRange(lista);
        NotifyDataSetChanged();
    }

    public override int ItemCount => celulares.Count;

    public override RecyclerView.ViewHolder OnCreateViewHolder(ViewGroup parent, int viewType)
    {
        var vista = LayoutInflater.From(parent.Context)!.Inflate(Resource.Layout.item_celular, parent, false)!;
        var fila = new Fila(vista);
        fila.Editar.Click += (_, _) => alEditar(celulares[fila.BindingAdapterPosition]);
        fila.Eliminar.Click += (_, _) => alEliminar(celulares[fila.BindingAdapterPosition]);
        return fila;
    }

    public override void OnBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        var fila = (Fila)holder;
        var cel = celulares[position];
        var contexto = fila.ItemView.Context!;
        fila.Modelo.Text = cel.modelo;
        fila.Marca.Text = $"{cel.marca} · código {cel.codCelular}";
        fila.Precio.Text = Formato.Moneda(cel.precio);
        fila.Estado.Text = cel.activo ? "Activo" : "Desactivado";
        fila.Estado.SetTextColor(new Android.Graphics.Color(ContextCompat.GetColor(contexto, cel.activo ? Resource.Color.exito : Resource.Color.texto_suave)));
        fila.Estado.Background?.Mutate()?.SetTint(ContextCompat.GetColor(contexto, cel.activo ? Resource.Color.exito_claro : Resource.Color.borde));
        fila.ItemView.Alpha = cel.activo ? 1f : 0.7f;
    }

    private class Fila : RecyclerView.ViewHolder
    {
        public TextView Modelo { get; }
        public TextView Marca { get; }
        public TextView Precio { get; }
        public TextView Estado { get; }
        public View Editar { get; }
        public View Eliminar { get; }

        public Fila(View vista) : base(vista)
        {
            Modelo = vista.FindViewById<TextView>(Resource.Id.txtModelo)!;
            Marca = vista.FindViewById<TextView>(Resource.Id.txtMarca)!;
            Precio = vista.FindViewById<TextView>(Resource.Id.txtPrecio)!;
            Estado = vista.FindViewById<TextView>(Resource.Id.txtEstado)!;
            Editar = vista.FindViewById(Resource.Id.btnEditar)!;
            Eliminar = vista.FindViewById(Resource.Id.btnEliminar)!;
        }
    }
}
