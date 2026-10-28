using ec.edu.monster.Controlador;
using ec.edu.monster.Modelo;
using ec.edu.monster.Util;

namespace ec.edu.monster.Vista;

// CU-06 y CU-07: factura en efectivo o con crédito directo de BanQuito
[Activity(Label = "Facturar", WindowSoftInputMode = SoftInput.AdjustResize)]
public class FacturarActivity : ActividadBase
{
    private readonly FacturacionControlador controlador = new();
    private celular[] celulares = Array.Empty<celular>();
    private EditText txtCedula = null!, txtNombre = null!, txtCantidad = null!;
    private Spinner spCelular = null!, spCuotas = null!;
    private RadioButton rbCredito = null!;

    private bool Credito => rbCredito.Checked;
    private int Cuotas => spCuotas.SelectedItemPosition + 3;
    private celular? Seleccionado => spCelular.SelectedItemPosition > 0 ? celulares[spCelular.SelectedItemPosition - 1] : null;

    protected override async void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);
        ConfigurarPantalla(Resource.Layout.activity_facturar, "Facturar venta");

        txtCedula = Vista<EditText>(Resource.Id.txtCedula);
        txtNombre = Vista<EditText>(Resource.Id.txtNombre);
        txtCantidad = Vista<EditText>(Resource.Id.txtCantidad);
        spCelular = Vista<Spinner>(Resource.Id.spCelular);
        spCuotas = Vista<Spinner>(Resource.Id.spCuotas);
        rbCredito = Vista<RadioButton>(Resource.Id.rbCredito);

        spCuotas.Adapter = Opciones(Enumerable.Range(3, 16).Select(n => $"{n} cuotas mensuales"));
        spCuotas.SetSelection(12 - 3);

        Vista<RadioGroup>(Resource.Id.rgFormaPago).CheckedChange += (_, _) => ActualizarResumen();
        spCelular.ItemSelected += (_, _) => ActualizarResumen();
        spCuotas.ItemSelected += (_, _) => ActualizarResumen();
        txtCantidad.TextChanged += (_, _) => ActualizarResumen();
        Vista<View>(Resource.Id.btnEmitir).Click += async (_, _) => await Emitir();

        await Ejecutar(async () =>
        {
            celulares = await controlador.CelularesDisponiblesAsync();
            spCelular.Adapter = Opciones(new[] { "Seleccione un celular…" }
                .Concat(celulares.Select(c => $"{c.marca} {c.modelo} — {Formato.Moneda(c.precio)}")));
        });
        ActualizarResumen();
    }

    private ArrayAdapter<string> Opciones(IEnumerable<string> textos)
    {
        var adaptador = new ArrayAdapter<string>(this, Android.Resource.Layout.SimpleSpinnerItem, textos.ToList());
        adaptador.SetDropDownViewResource(Android.Resource.Layout.SimpleSpinnerDropDownItem);
        return adaptador;
    }

    // Vista previa: el 42 % en efectivo o la cuota estimada del crédito
    private void ActualizarResumen()
    {
        Vista<View>(Resource.Id.campoCantidad).Visibility = Credito ? ViewStates.Gone : ViewStates.Visible;
        Vista<View>(Resource.Id.campoCuotas).Visibility = Credito ? ViewStates.Visible : ViewStates.Gone;
        Vista<View>(Resource.Id.txtNota).Visibility = Credito ? ViewStates.Visible : ViewStates.Gone;
        Vista<TextView>(Resource.Id.txtEtiquetaTotal).Text = Credito ? "Total financiado" : "Total a pagar";

        var resumenTexto = Vista<TextView>(Resource.Id.txtResumen);
        var totalTexto = Vista<TextView>(Resource.Id.txtTotal);
        celular? cel = Seleccionado;
        if (cel == null)
        {
            resumenTexto.Text = "Seleccione un celular para ver el resumen.";
            totalTexto.Text = "—";
            return;
        }
        int.TryParse(txtCantidad.Text, out int cantidad);
        ResumenFactura r = controlador.CalcularResumen(cel, Credito, cantidad, Cuotas);
        resumenTexto.Text = Credito
            ? $"Precio del celular: {Formato.Moneda(r.PrecioUnitario)}\nMonto a financiar: {Formato.Moneda(r.Subtotal)}\nCuota estimada: {Cuotas} × {Formato.Moneda(r.CuotaEstimada ?? 0)}"
            : $"Precio unitario: {Formato.Moneda(r.PrecioUnitario)}\nSubtotal: {Formato.Moneda(r.Subtotal)}\nDescuento (42 %): − {Formato.Moneda(r.Descuento)}";
        totalTexto.Text = Formato.Moneda(r.Total);
    }

    private Task Emitir() => Ejecutar(async () =>
    {
        factura f = await controlador.FacturarAsync(txtCedula.Text, txtNombre.Text, Seleccionado, Credito, txtCantidad.Text, Cuotas);
        await Dialogos.Mostrar(this, "Factura emitida", f.codCreditoSpecified
            ? $"Crédito aprobado por BanQuito (crédito N.º {f.codCredito}). Se emitió la factura N.º {f.numFactura}."
            : $"Se emitió la factura N.º {f.numFactura} con el 42 % de descuento por pago en efectivo.");
        var intent = new Intent(this, typeof(FacturaActivity));
        intent.PutExtra(FacturaActivity.ExtraNumero, f.numFactura);
        StartActivity(intent);
        Finish();
    });
}
