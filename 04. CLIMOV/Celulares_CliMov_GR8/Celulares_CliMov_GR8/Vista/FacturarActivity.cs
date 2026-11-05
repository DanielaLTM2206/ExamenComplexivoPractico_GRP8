using ec.edu.monster.Controlador;
using ec.edu.monster.Modelo;
using ec.edu.monster.Servicio;
using ec.edu.monster.Util;

namespace ec.edu.monster.Vista;

// CU-06 y CU-07: factura de uno o varios celulares en efectivo o con crédito directo de BanQuito
[Activity(Label = "Facturar", WindowSoftInputMode = SoftInput.AdjustResize)]
public class FacturarActivity : ActividadBase
{
    private readonly FacturacionControlador controlador = new();
    private readonly CarritoVenta carrito = new();
    private celular[] celulares = Array.Empty<celular>();
    private EditText txtCedula = null!, txtNombre = null!, txtCantidad = null!;
    private Spinner spCelular = null!, spCuotas = null!;
    private RadioButton rbCredito = null!;
    private LinearLayout contenedorCarrito = null!;

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
        contenedorCarrito = Vista<LinearLayout>(Resource.Id.contenedorCarrito);

        spCuotas.Adapter = Opciones(Enumerable.Range(3, 16).Select(n => $"{n} cuotas mensuales"));
        spCuotas.SetSelection(12 - 3);

        Vista<RadioGroup>(Resource.Id.rgFormaPago).CheckedChange += (_, _) => ActualizarResumen();
        spCuotas.ItemSelected += (_, _) => ActualizarResumen();
        Vista<View>(Resource.Id.btnAgregar).Click += async (_, _) => await Agregar();
        Vista<View>(Resource.Id.btnEmitir).Click += async (_, _) => await Emitir();

        await Ejecutar(async () =>
        {
            celulares = await controlador.CelularesDisponiblesAsync();
            spCelular.Adapter = Opciones(new[] { "Seleccione un celular…" }
                .Concat(celulares.Select(c => $"{c.marca} {c.modelo} — {Formato.Moneda(c.precio)}")));
        });
        PintarCarrito();
    }

    private ArrayAdapter<string> Opciones(IEnumerable<string> textos)
    {
        var adaptador = new ArrayAdapter<string>(this, Android.Resource.Layout.SimpleSpinnerItem, textos.ToList());
        adaptador.SetDropDownViewResource(Android.Resource.Layout.SimpleSpinnerDropDownItem);
        return adaptador;
    }

    // Agrega el celular elegido; si ya estaba en la venta se suma la cantidad
    private Task Agregar() => Ejecutar(() =>
    {
        controlador.AgregarAlCarrito(carrito, Seleccionado, txtCantidad.Text);
        spCelular.SetSelection(0);
        txtCantidad.Text = "1";
        PintarCarrito();
        return Task.CompletedTask;
    });

    private void PintarCarrito()
    {
        contenedorCarrito.RemoveAllViews();
        for (int i = 0; i < carrito.Items.Count; i++)
        {
            ItemCarrito item = carrito.Items[i];
            View fila = LayoutInflater.Inflate(Resource.Layout.item_carrito, contenedorCarrito, false)!;
            fila.FindViewById<TextView>(Resource.Id.txtItemCelular)!.Text = $"{item.Celular.marca} {item.Celular.modelo}";
            fila.FindViewById<TextView>(Resource.Id.txtItemDetalle)!.Text = $"{item.Cantidad} × {Formato.Moneda(item.Celular.precio)}";
            fila.FindViewById<TextView>(Resource.Id.txtItemSubtotal)!.Text = Formato.Moneda(item.Subtotal);
            int posicion = i;
            fila.FindViewById<View>(Resource.Id.btnQuitar)!.Click += (_, _) =>
            {
                carrito.Quitar(posicion);
                PintarCarrito();
            };
            contenedorCarrito.AddView(fila);
        }
        Vista<View>(Resource.Id.txtCarritoVacio).Visibility = carrito.EstaVacio ? ViewStates.Visible : ViewStates.Gone;
        ActualizarResumen();
    }

    // Vista previa: el 42 % en efectivo o la cuota estimada del crédito sobre el total de la venta
    private void ActualizarResumen()
    {
        Vista<View>(Resource.Id.campoCuotas).Visibility = Credito ? ViewStates.Visible : ViewStates.Gone;
        Vista<View>(Resource.Id.txtNota).Visibility = Credito ? ViewStates.Visible : ViewStates.Gone;
        Vista<TextView>(Resource.Id.txtEtiquetaTotal).Text = Credito ? "Total financiado" : "Total a pagar";

        var resumenTexto = Vista<TextView>(Resource.Id.txtResumen);
        var totalTexto = Vista<TextView>(Resource.Id.txtTotal);
        if (carrito.EstaVacio)
        {
            resumenTexto.Text = "Agregue celulares para ver el resumen.";
            totalTexto.Text = "—";
            return;
        }
        ResumenFactura r = controlador.CalcularResumen(carrito, Credito, Cuotas);
        resumenTexto.Text = Credito
            ? $"Unidades: {r.Unidades}\nMonto a financiar: {Formato.Moneda(r.Subtotal)}\nCuota estimada: {Cuotas} × {Formato.Moneda(r.CuotaEstimada ?? 0)}"
            : $"Unidades: {r.Unidades}\nSubtotal: {Formato.Moneda(r.Subtotal)}\nDescuento (42 %): − {Formato.Moneda(r.Descuento)}";
        totalTexto.Text = Formato.Moneda(r.Total);
    }

    private Task Emitir() => Ejecutar(async () =>
    {
        factura f = await controlador.FacturarAsync(txtCedula.Text, txtNombre.Text, carrito, Credito, Cuotas);
        await Dialogos.Mostrar(this, "Factura emitida", f.codCreditoSpecified
            ? $"Crédito aprobado por BanQuito (crédito N.º {f.codCredito}). Se emitió la factura N.º {f.numFactura}."
            : $"Se emitió la factura N.º {f.numFactura} con el 42 % de descuento por pago en efectivo.");
        var intent = new Intent(this, typeof(FacturaActivity));
        intent.PutExtra(FacturaActivity.ExtraNumero, f.numFactura);
        StartActivity(intent);
        Finish();
    });
}
