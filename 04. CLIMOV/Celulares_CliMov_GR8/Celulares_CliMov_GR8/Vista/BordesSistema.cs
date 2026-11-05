using AndroidX.Core.Content;

namespace ec.edu.monster.Vista;

// Desde Android 15 las apps se dibujan de borde a borde y Android 16 ya no permite desactivarlo:
// el contenido quedaba debajo de la barra de título y de la barra de navegación.
// Se reserva solo el espacio que realmente tapan las barras y la franja de la barra de estado se pinta de azul.
// Se usan las API propias de Android (no las de AndroidX.Core.View): sus clases Java generadas tienen nombres
// tan largos que, dentro de la carpeta de OneDrive, superaban el límite de 260 caracteres de Windows (XAJCW7023).
public static class BordesSistema
{
    public static void Ajustar(Activity actividad)
    {
        // Antes de Android 15 el sistema ya coloca el contenido debajo de las barras
        if (!OperatingSystem.IsAndroidVersionAtLeast(35)) return;
        var contenido = actividad.FindViewById(Android.Resource.Id.Content);
        if (contenido == null) return;

        // Detrás de la barra de estado se ve el fondo de la ventana: azul institucional con iconos claros
        var ventana = actividad.Window!;
        ventana.DecorView.SetBackgroundColor(new Android.Graphics.Color(ContextCompat.GetColor(actividad, Resource.Color.azul)));
        ventana.InsetsController?.SetSystemBarsAppearance(0, (int)WindowInsetsControllerAppearance.LightStatusBars);

        contenido.SetOnApplyWindowInsetsListener(new Oyente(actividad.FindViewById(Resource.Id.action_bar_container)));
        contenido.RequestApplyInsets();
    }

    private sealed class Oyente(View? barraTitulo) : Java.Lang.Object, View.IOnApplyWindowInsetsListener
    {
        public WindowInsets OnApplyWindowInsets(View vista, WindowInsets insets)
        {
            if (!OperatingSystem.IsAndroidVersionAtLeast(35)) return insets;
            var barras = insets.GetInsets(WindowInsets.Type.SystemBars() | WindowInsets.Type.Ime());
            // Las posiciones reales se conocen después del primer dibujo: solo se rellena lo que queda tapado
            vista.Post(() =>
            {
                int[] contenido = new int[2];
                vista.GetLocationOnScreen(contenido);
                int bordeSuperior = barras.Top;
                if (barraTitulo is { Height: > 0 })
                {
                    int[] titulo = new int[2];
                    barraTitulo.GetLocationOnScreen(titulo);
                    bordeSuperior = titulo[1] + barraTitulo.Height;
                }
                int tapadoArriba = Math.Max(0, bordeSuperior - contenido[1]);
                int tapadoAbajo = Math.Max(0, contenido[1] + vista.Height - (vista.RootView!.Height - barras.Bottom));
                vista.SetPadding(barras.Left, tapadoArriba, barras.Right, tapadoAbajo);
            });
            return insets;
        }
    }
}
