using AndroidX.Core.Content;
using ec.edu.monster.Controlador;
using ec.edu.monster.Servicio;

namespace ec.edu.monster.Vista;

// "Cambiar servidor": la IP de la PC con Payara cambia según la red, por eso se escribe aquí y se guarda en el teléfono
[Activity(Label = "Cambiar servidor")]
public class ServidorActivity : ActividadBase
{
    private readonly ServidorControlador controlador = new();
    private EditText txtServidor = null!;
    private TextView txtResultado = null!;

    protected override void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);
        ConfigurarPantalla(Resource.Layout.activity_servidor, "Cambiar servidor");

        txtServidor = Vista<EditText>(Resource.Id.txtServidor);
        txtResultado = Vista<TextView>(Resource.Id.txtResultado);
        txtServidor.Text = controlador.UrlActual ?? "";
        txtServidor.TextChanged += (_, _) => txtResultado.Visibility = ViewStates.Gone;
        MostrarActual();

        Vista<View>(Resource.Id.btnProbar).Click += async (_, _) => await Probar();
        Vista<View>(Resource.Id.btnGuardar).Click += async (_, _) => await Guardar();
        Vista<View>(Resource.Id.btnRestablecer).Click += async (_, _) => await Restablecer();
    }

    private void MostrarActual()
    {
        Vista<TextView>(Resource.Id.txtUrlActual).Text = controlador.UrlActual is { } url
            ? "Servidor actual:\n" + url
            : "Servidor actual: sin configurar";
    }

    private Task Probar() => Ejecutar(async () =>
    {
        string url = controlador.CompletarUrl(txtServidor.Text);
        txtServidor.Text = url;
        MostrarResultado(await controlador.ProbarAsync(url));
    });

    // Se prueba antes de guardar; si falla, se pregunta si se guarda igual (el servidor puede estar apagándose)
    private Task Guardar() => Ejecutar(async () =>
    {
        string url = controlador.CompletarUrl(txtServidor.Text);
        txtServidor.Text = url;
        ResultadoConexion resultado = await controlador.ProbarAsync(url);
        MostrarResultado(resultado);
        if (!resultado.Exitosa
                && !await Dialogos.Confirmar(this, resultado.Mensaje + "\n\n¿Desea guardar esta dirección de todas formas?"))
        {
            return;
        }
        controlador.Guardar(url);
        MostrarActual();
        await Dialogos.Mostrar(this, "Servidor guardado", "La aplicación usará el servidor:\n" + url);
        Finish();
    });

    private async Task Restablecer()
    {
        string destino = controlador.UrlPorDefecto is { } porDefecto
            ? "la dirección del emulador (" + porDefecto + ")"
            : "sin servidor configurado";
        if (!await Dialogos.Confirmar(this, "Se borrará la dirección guardada y la aplicación quedará con " + destino + ". ¿Continuar?"))
        {
            return;
        }
        controlador.Restablecer();
        txtServidor.Text = controlador.UrlActual ?? "";
        txtResultado.Visibility = ViewStates.Gone;
        MostrarActual();
    }

    private void MostrarResultado(ResultadoConexion resultado)
    {
        int texto = resultado.Exitosa ? Resource.Color.exito : Resource.Color.error;
        int fondo = resultado.Exitosa ? Resource.Color.exito_claro : Resource.Color.error_claro;
        txtResultado.Text = (resultado.Exitosa ? "✓ " : "✗ ") + resultado.Mensaje;
        txtResultado.SetTextColor(new Android.Graphics.Color(ContextCompat.GetColor(this, texto)));
        txtResultado.SetBackgroundColor(new Android.Graphics.Color(ContextCompat.GetColor(this, fondo)));
        txtResultado.Visibility = ViewStates.Visible;
    }
}
