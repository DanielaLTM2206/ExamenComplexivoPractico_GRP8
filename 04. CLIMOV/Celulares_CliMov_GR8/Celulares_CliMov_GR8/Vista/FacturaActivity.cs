using AndroidX.Core.Content;
using ec.edu.monster.Controlador;
using ec.edu.monster.Modelo;
using ec.edu.monster.Util;

namespace ec.edu.monster.Vista;

// Consulta de una factura emitida por su número
[Activity(Label = "Factura")]
public class FacturaActivity : ActividadBase
{
    public const string ExtraNumero = "numFactura";

    private readonly FacturacionControlador controlador = new();
    private EditText txtNumero = null!;
    private int numeroMostrado;

    protected override async void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);
        ConfigurarPantalla(Resource.Layout.activity_factura, "Consultar factura");

        txtNumero = Vista<EditText>(Resource.Id.txtNumFactura);
        Vista<View>(Resource.Id.btnBuscar).Click += async (_, _) => await Buscar();
        txtNumero.EditorAction += async (_, _) => await Buscar();
        Vista<View>(Resource.Id.btnVerAmortizacion).Click += (_, _) => VerTabla();

        int numero = Intent?.GetIntExtra(ExtraNumero, 0) ?? 0;
        if (numero > 0)
        {
            txtNumero.Text = numero.ToString();
            await Buscar();
        }
    }

    private Task Buscar() => Ejecutar(async () =>
    {
        Vista<View>(Resource.Id.tarjetaFactura).Visibility = ViewStates.Gone;
        factura f = await controlador.ObtenerFacturaAsync(txtNumero.Text);
        Mostrar(f);
    });

    private void Mostrar(factura f)
    {
        bool credito = f.formaPago == "CREDITO";
        Vista<TextView>(Resource.Id.txtNumero).Text = $"Factura N.º {f.numFactura}";
        Vista<TextView>(Resource.Id.txtFecha).Text = f.fecha;

        var etiqueta = Vista<TextView>(Resource.Id.txtFormaPago);
        etiqueta.Text = credito ? "Crédito directo" : "Efectivo";
        etiqueta.SetTextColor(new Android.Graphics.Color(ContextCompat.GetColor(this, credito ? Resource.Color.amarillo : Resource.Color.exito)));
        etiqueta.Background?.Mutate()?.SetTint(ContextCompat.GetColor(this, credito ? Resource.Color.azul : Resource.Color.exito_claro));

        Vista<TextView>(Resource.Id.txtCliente).Text =
            $"Cliente: {f.nombre}\nCédula: {f.cedula}" + (f.codCreditoSpecified ? $"\nCrédito BanQuito: N.º {f.codCredito}" : "");

        var contenedor = Vista<LinearLayout>(Resource.Id.contenedorDetalles);
        contenedor.RemoveAllViews();
        foreach (detalleFactura d in f.detalles ?? Array.Empty<detalleFactura>())
        {
            var linea = new TextView(this)
            {
                Text = $"{d.marcaCelular} {d.modeloCelular}\n{d.cantidad} × {Formato.Moneda(d.precioUnitario)} = {Formato.Moneda(d.subtotal)}"
            };
            linea.SetTextColor(new Android.Graphics.Color(ContextCompat.GetColor(this, Resource.Color.texto)));
            linea.SetPadding(0, 12, 0, 12);
            contenedor.AddView(linea);
        }

        Vista<TextView>(Resource.Id.txtTotales).Text =
            $"Subtotal: {Formato.Moneda(f.subtotal)}\nDescuento: − {Formato.Moneda(f.descuento)}\nTotal: {Formato.Moneda(f.total)}";

        Vista<View>(Resource.Id.btnVerAmortizacion).Visibility = credito ? ViewStates.Visible : ViewStates.Gone;
        Vista<View>(Resource.Id.tarjetaFactura).Visibility = ViewStates.Visible;
        numeroMostrado = f.numFactura;
    }

    private void VerTabla()
    {
        var intent = new Intent(this, typeof(AmortizacionActivity));
        intent.PutExtra(AmortizacionActivity.ExtraNumero, numeroMostrado);
        StartActivity(intent);
    }
}
