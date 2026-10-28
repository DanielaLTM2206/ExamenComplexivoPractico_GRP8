using System.Globalization;
using ec.edu.monster.Controlador;
using ec.edu.monster.Modelo;
using Google.Android.Material.SwitchMaterial;

namespace ec.edu.monster.Vista;

// Formulario para registrar o editar un celular del catálogo
[Activity(Label = "Celular")]
public class CelularFormActivity : ActividadBase
{
    public const string ExtraCodigo = "codCelular";

    private readonly CatalogoControlador controlador = new();
    private int codCelular;
    private EditText txtMarca = null!, txtModelo = null!, txtPrecio = null!;
    private SwitchMaterial swActivo = null!;

    protected override async void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);
        codCelular = Intent?.GetIntExtra(ExtraCodigo, 0) ?? 0;
        bool nuevo = codCelular == 0;
        ConfigurarPantalla(Resource.Layout.activity_celular_form, nuevo ? "Nuevo celular" : "Editar celular");

        txtMarca = Vista<EditText>(Resource.Id.txtMarca);
        txtModelo = Vista<EditText>(Resource.Id.txtModelo);
        txtPrecio = Vista<EditText>(Resource.Id.txtPrecio);
        swActivo = Vista<SwitchMaterial>(Resource.Id.swActivo);
        swActivo.Visibility = nuevo ? ViewStates.Gone : ViewStates.Visible;
        Vista<TextView>(Resource.Id.txtSubtitulo).Text = nuevo
            ? "Registra un teléfono en el catálogo."
            : $"Modifica los datos o el estado del celular con código {codCelular}.";

        Vista<View>(Resource.Id.btnCancelar).Click += (_, _) => Finish();
        Vista<View>(Resource.Id.btnGuardar).Click += async (_, _) => await Guardar();

        if (!nuevo)
        {
            await Ejecutar(async () =>
            {
                celular cel = await controlador.ObtenerAsync(codCelular);
                txtMarca.Text = cel.marca;
                txtModelo.Text = cel.modelo;
                txtPrecio.Text = cel.precio.ToString("0.00", CultureInfo.InvariantCulture);
                swActivo.Checked = cel.activo;
            });
        }
    }

    private Task Guardar() => Ejecutar(async () =>
    {
        celular guardado = await controlador.GuardarAsync(codCelular, txtMarca.Text, txtModelo.Text, txtPrecio.Text, swActivo.Checked);
        await Dialogos.Mostrar(this, "Operación exitosa",
            $"{(codCelular == 0 ? "Se registró" : "Se actualizó")} el celular {guardado.marca} {guardado.modelo}.");
        Finish();
    });
}
