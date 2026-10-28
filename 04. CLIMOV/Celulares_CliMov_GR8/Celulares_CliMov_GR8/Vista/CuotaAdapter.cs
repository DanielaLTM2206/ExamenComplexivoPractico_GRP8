using AndroidX.Core.Content;
using AndroidX.RecyclerView.Widget;
using ec.edu.monster.Modelo;
using ec.edu.monster.Util;

namespace ec.edu.monster.Vista;

// Filas de la tabla de amortización
public class CuotaAdapter : RecyclerView.Adapter
{
    private readonly cuotaAmortizacion[] cuotas;

    public CuotaAdapter(cuotaAmortizacion[] cuotas) => this.cuotas = cuotas;

    public override int ItemCount => cuotas.Length;

    public override RecyclerView.ViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) =>
        new Fila(LayoutInflater.From(parent.Context)!.Inflate(Resource.Layout.item_cuota, parent, false)!);

    public override void OnBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        var c = cuotas[position];
        var fila = (Fila)holder;
        fila.Llenar(c.numCuota.ToString(), Formato.Moneda(c.valorCuota), Formato.Moneda(c.interes),
            Formato.Moneda(c.capital), Formato.Moneda(c.saldo));
        fila.ItemView.SetBackgroundColor(new Android.Graphics.Color(ContextCompat.GetColor(fila.ItemView.Context!,
            position % 2 == 0 ? Resource.Color.blanco : Resource.Color.fondo)));
    }

    public class Fila : RecyclerView.ViewHolder
    {
        private readonly TextView[] columnas;

        public Fila(View vista) : base(vista)
        {
            columnas = new[] { Resource.Id.colNumero, Resource.Id.colCuota, Resource.Id.colInteres, Resource.Id.colCapital, Resource.Id.colSaldo }
                .Select(id => vista.FindViewById<TextView>(id)!).ToArray();
        }

        public void Llenar(params string[] textos)
        {
            for (int i = 0; i < columnas.Length; i++) columnas[i].Text = textos[i];
        }

        public void Estilo(int colorTexto, bool negrita)
        {
            foreach (var col in columnas)
            {
                col.SetTextColor(new Android.Graphics.Color(colorTexto));
                col.SetTypeface(null, negrita ? Android.Graphics.TypefaceStyle.Bold : Android.Graphics.TypefaceStyle.Normal);
            }
        }
    }
}
