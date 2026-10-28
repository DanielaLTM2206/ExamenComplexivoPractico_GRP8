using AndroidX.AppCompat.App;

namespace ec.edu.monster.Vista;

// Portada de la aplicación
[Activity(Label = "@string/app_name", MainLauncher = true)]
public class MainActivity : AppCompatActivity
{
    protected override void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);
        SetContentView(Resource.Layout.activity_main);
        Title = "Comercializadora de Celulares";

        Abrir<CatalogoActivity>(Resource.Id.btnCatalogo);
        Abrir<FacturarActivity>(Resource.Id.btnFacturar);
        Abrir<FacturaActivity>(Resource.Id.btnFacturas);
        Abrir<AmortizacionActivity>(Resource.Id.btnAmortizacion);
    }

    private void Abrir<T>(int idBoton) where T : Activity =>
        FindViewById(idBoton)!.Click += (_, _) => StartActivity(typeof(T));
}
