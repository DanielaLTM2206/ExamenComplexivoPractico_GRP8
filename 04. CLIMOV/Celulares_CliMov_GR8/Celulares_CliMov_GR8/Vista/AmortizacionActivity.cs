using AndroidX.Core.Content;
using AndroidX.RecyclerView.Widget;
using ec.edu.monster.Controlador;
using ec.edu.monster.Util;

namespace ec.edu.monster.Vista;

// CU-08: tabla de amortización del crédito de una factura
[Activity(Label = "Amortización")]
public class AmortizacionActivity : ActividadBase
{
    public const string ExtraNumero = "numFactura";

    private readonly AmortizacionControlador controlador = new();
    private EditText txtNumero = null!;

    protected override async void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);
        ConfigurarPantalla(Resource.Layout.activity_amortizacion, "Tabla de amortización");

        txtNumero = Vista<EditText>(Resource.Id.txtNumFactura);
        Vista<View>(Resource.Id.btnBuscar).Click += async (_, _) => await Consultar();
        txtNumero.EditorAction += async (_, _) => await Consultar();
        Vista<RecyclerView>(Resource.Id.listaCuotas).SetLayoutManager(new LinearLayoutManager(this));

        var titulos = new CuotaAdapter.Fila(Vista<View>(Resource.Id.filaTitulos));
        titulos.Llenar("N.º", "Cuota", "Interés", "Capital", "Saldo");
        titulos.Estilo(ContextCompat.GetColor(this, Resource.Color.blanco), true);
        titulos.ItemView.SetBackgroundColor(new Android.Graphics.Color(ContextCompat.GetColor(this, Resource.Color.azul)));

        int numero = Intent?.GetIntExtra(ExtraNumero, 0) ?? 0;
        if (numero > 0)
        {
            txtNumero.Text = numero.ToString();
            await Consultar();
        }
    }

    private Task Consultar() => Ejecutar(async () =>
    {
        Vista<View>(Resource.Id.panelTabla).Visibility = ViewStates.Gone;
        TablaAmortizacion tabla = await controlador.ConsultarAsync(txtNumero.Text);

        Vista<TextView>(Resource.Id.txtCabecera).Text =
            $"Factura N.º {tabla.Factura.numFactura} · Crédito BanQuito N.º {tabla.Factura.codCredito}\n" +
            $"Cliente: {tabla.Factura.nombre}\n" +
            $"Monto financiado: {Formato.Moneda(tabla.Factura.total)} · {tabla.Cuotas.Length} meses al 16,5 % anual";

        Vista<RecyclerView>(Resource.Id.listaCuotas).SetAdapter(new CuotaAdapter(tabla.Cuotas));

        var totales = new CuotaAdapter.Fila(Vista<View>(Resource.Id.filaTotales));
        totales.Llenar("Total", Formato.Moneda(tabla.TotalPagado), Formato.Moneda(tabla.TotalInteres), Formato.Moneda(tabla.TotalCapital), "");
        totales.Estilo(ContextCompat.GetColor(this, Resource.Color.azul), true);
        totales.ItemView.SetBackgroundColor(new Android.Graphics.Color(ContextCompat.GetColor(this, Resource.Color.azul_claro)));

        Vista<View>(Resource.Id.panelTabla).Visibility = ViewStates.Visible;
    });
}
