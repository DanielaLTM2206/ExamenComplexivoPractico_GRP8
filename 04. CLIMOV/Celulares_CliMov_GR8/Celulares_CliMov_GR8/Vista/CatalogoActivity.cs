using AndroidX.RecyclerView.Widget;
using ec.edu.monster.Controlador;
using ec.edu.monster.Modelo;
using Google.Android.Material.SwitchMaterial;

namespace ec.edu.monster.Vista;

// CU-05: lista del catálogo con alta, edición y eliminación
[Activity(Label = "Catálogo")]
public class CatalogoActivity : ActividadBase
{
    private readonly CatalogoControlador controlador = new();
    private CelularAdapter adaptador = null!;
    private SwitchMaterial swTodos = null!;

    protected override void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);
        ConfigurarPantalla(Resource.Layout.activity_catalogo, "Catálogo de celulares");

        adaptador = new CelularAdapter(Editar, Eliminar);
        var lista = Vista<RecyclerView>(Resource.Id.lista);
        lista.SetLayoutManager(new LinearLayoutManager(this));
        lista.SetAdapter(adaptador);

        swTodos = Vista<SwitchMaterial>(Resource.Id.swTodos);
        swTodos.CheckedChange += async (_, _) => await Cargar();
        Vista<View>(Resource.Id.btnNuevo).Click += (_, _) => StartActivity(typeof(CelularFormActivity));
    }

    // Se recarga al volver del formulario
    protected override async void OnResume()
    {
        base.OnResume();
        await Cargar();
    }

    private Task Cargar() => Ejecutar(async () =>
    {
        celular[] celulares = await controlador.ListarAsync(swTodos.Checked);
        adaptador.Actualizar(celulares);
        Vista<View>(Resource.Id.txtVacio).Visibility = celulares.Length == 0 ? ViewStates.Visible : ViewStates.Gone;
    });

    private void Editar(celular cel)
    {
        var intent = new Intent(this, typeof(CelularFormActivity));
        intent.PutExtra(CelularFormActivity.ExtraCodigo, cel.codCelular);
        StartActivity(intent);
    }

    private async void Eliminar(celular cel)
    {
        bool confirmar = await Dialogos.Confirmar(this,
            $"¿Eliminar el celular {cel.marca} {cel.modelo}? Si ya tiene ventas, solo se desactivará.");
        if (!confirmar) return;
        await Ejecutar(async () =>
        {
            await controlador.EliminarAsync(cel.codCelular);
            await Dialogos.Mostrar(this, "Operación exitosa",
                "El celular se eliminó del catálogo. Si ya tenía ventas, quedó desactivado para conservar las facturas.");
            // Se muestran también los desactivados para que se vea si el celular quedó desactivado
            if (swTodos.Checked)
                adaptador.Actualizar(await controlador.ListarAsync(true));
            else
                swTodos.Checked = true;
        });
    }
}
