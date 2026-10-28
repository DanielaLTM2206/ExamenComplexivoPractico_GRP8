using Android.Graphics.Drawables;
using AndroidX.Core.Content;
using Google.Android.Material.Button;

namespace ec.edu.monster.Vista;

// Diálogo propio (reemplaza al AlertDialog estándar) para errores, avisos y confirmaciones
public static class Dialogos
{
    public static Task Mostrar(Activity actividad, string titulo, string mensaje) =>
        Abrir(actividad, titulo, mensaje, "✓", false, false);

    public static Task Error(Activity actividad, string titulo, string mensaje) =>
        Abrir(actividad, titulo, mensaje, "!", true, false);

    public static Task<bool> Confirmar(Activity actividad, string mensaje) =>
        Abrir(actividad, "Confirmar", mensaje, "?", false, true);

    private static Task<bool> Abrir(Activity actividad, string titulo, string mensaje, string icono, bool esError, bool conCancelar)
    {
        var resultado = new TaskCompletionSource<bool>();
        var vista = actividad.LayoutInflater.Inflate(Resource.Layout.dialogo, null)!;

        vista.FindViewById<TextView>(Resource.Id.dialogoTitulo)!.Text = titulo;
        vista.FindViewById<TextView>(Resource.Id.dialogoMensaje)!.Text = mensaje;
        var txtIcono = vista.FindViewById<TextView>(Resource.Id.dialogoIcono)!;
        txtIcono.Text = icono;
        if (esError)
        {
            vista.SetBackgroundResource(Resource.Drawable.fondo_dialogo_error);
            txtIcono.Background?.Mutate()?.SetTint(ContextCompat.GetColor(actividad, Resource.Color.error));
            txtIcono.SetTextColor(Android.Graphics.Color.White);
        }

        var dialogo = new AndroidX.AppCompat.App.AlertDialog.Builder(actividad).SetView(vista)!.SetCancelable(false)!.Create();
        dialogo.Window?.SetBackgroundDrawable(new ColorDrawable(Android.Graphics.Color.Transparent));

        var aceptar = vista.FindViewById<MaterialButton>(Resource.Id.dialogoAceptar)!;
        var cancelar = vista.FindViewById<MaterialButton>(Resource.Id.dialogoCancelar)!;
        cancelar.Visibility = conCancelar ? ViewStates.Visible : ViewStates.Gone;
        aceptar.Click += (_, _) => { dialogo.Dismiss(); resultado.TrySetResult(true); };
        cancelar.Click += (_, _) => { dialogo.Dismiss(); resultado.TrySetResult(false); };

        dialogo.Show();
        return resultado.Task;
    }
}
