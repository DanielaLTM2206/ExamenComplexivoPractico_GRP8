using AndroidX.AppCompat.App;
using ec.edu.monster.Servicio;

namespace ec.edu.monster.Vista;

// Comportamiento común de las pantallas: indicador de carga, manejo de errores y botón "atrás" de la barra
public abstract class ActividadBase : AppCompatActivity
{
    protected void ConfigurarPantalla(int layout, string titulo)
    {
        SetContentView(layout);
        Title = titulo;
        SupportActionBar?.SetDisplayHomeAsUpEnabled(true);
    }

    public override bool OnSupportNavigateUp()
    {
        Finish();
        return true;
    }

    // Ejecuta una operación contra el servidor mostrando "Consultando…" y los errores en el diálogo propio
    protected async Task Ejecutar(Func<Task> operacion)
    {
        var capa = FindViewById(Resource.Id.capaCargando);
        if (capa != null) capa.Visibility = ViewStates.Visible;
        try
        {
            await operacion();
        }
        catch (ExcepcionCliente e)
        {
            await Dialogos.Error(this, e.Titulo, e.Message);
        }
        catch (Exception e)
        {
            Android.Util.Log.Error("CelularesGR8", e.ToString());
            await Dialogos.Error(this, "Error inesperado", e.Message);
        }
        finally
        {
            if (capa != null) capa.Visibility = ViewStates.Gone;
        }
    }

    protected T Vista<T>(int id) where T : View => FindViewById<T>(id)!;
}
