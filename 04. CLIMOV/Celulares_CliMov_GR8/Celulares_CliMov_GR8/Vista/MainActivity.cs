using AndroidX.AppCompat.App;
using AndroidX.Core.Content;
using ec.edu.monster.Util;

namespace ec.edu.monster.Vista;

// Portada de la aplicación
[Activity(Label = "@string/app_name", MainLauncher = true)]
public class MainActivity : AppCompatActivity
{
    protected override void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);
        SetContentView(Resource.Layout.activity_main);
        BordesSistema.Ajustar(this);
        Title = "Comercializadora de Celulares";

        Abrir<CatalogoActivity>(Resource.Id.btnCatalogo);
        Abrir<FacturarActivity>(Resource.Id.btnFacturar);
        Abrir<FacturaActivity>(Resource.Id.btnFacturas);
        Abrir<AmortizacionActivity>(Resource.Id.btnAmortizacion);
        FindViewById(Resource.Id.btnServidor)!.Click += (_, _) => StartActivity(typeof(ServidorActivity));
    }

    // Al volver de "Cambiar servidor" se muestra la dirección recién guardada
    protected override void OnResume()
    {
        base.OnResume();
        var txtServidor = FindViewById<TextView>(Resource.Id.txtServidorPortada)!;
        bool configurado = ConfiguracionServidor.EstaConfigurado;
        txtServidor.Text = configurado
            ? ConfiguracionServidor.UrlActual
            : "Sin configurar: toque \"Cambiar servidor\" y escriba la IP de la PC con Payara.";
        txtServidor.SetTextColor(new Android.Graphics.Color(
            ContextCompat.GetColor(this, configurado ? Resource.Color.texto_suave : Resource.Color.error)));
    }

    // Sin servidor configurado (celular real recién instalado) se lleva primero a "Cambiar servidor"
    private void Abrir<T>(int idBoton) where T : Activity =>
        FindViewById(idBoton)!.Click += async (_, _) =>
        {
            if (ConfiguracionServidor.EstaConfigurado)
            {
                StartActivity(typeof(T));
                return;
            }
            await Dialogos.Error(this, "Servidor sin configurar",
                "Antes de continuar escriba la IP de la PC donde corre Payara (aparece al ejecutar PREPARAR_SERVIDOR_SOAP_8080.bat).");
            StartActivity(typeof(ServidorActivity));
        };
}
