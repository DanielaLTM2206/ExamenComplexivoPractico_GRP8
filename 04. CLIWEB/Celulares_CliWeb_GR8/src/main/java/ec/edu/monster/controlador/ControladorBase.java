package ec.edu.monster.controlador;

import ec.edu.monster.servicio.ExcepcionCliente;
import ec.edu.monster.servicio.ServicioComercializadora;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Comportamiento común de los controladores: vistas en /WEB-INF/vistas, errores y avisos para el diálogo
public abstract class ControladorBase extends HttpServlet {

    protected final ServicioComercializadora servicio = new ServicioComercializadora();

    protected void mostrar(HttpServletRequest req, HttpServletResponse resp, String vista) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/vistas/" + vista + ".jsp").forward(req, resp);
    }

    // El diálogo de error de la plantilla lee estos atributos
    protected void error(HttpServletRequest req, ExcepcionCliente e) {
        req.setAttribute("tituloError", e.getTitulo());
        req.setAttribute("error", e.getMessage());
    }

    // Aviso que sobrevive a la redirección (patrón Post/Redirect/Get); la plantilla lo muestra una vez
    protected void avisar(HttpServletRequest req, String mensaje) {
        req.getSession().setAttribute("aviso", mensaje);
    }

    protected void redirigir(HttpServletRequest req, HttpServletResponse resp, String ruta) throws IOException {
        resp.sendRedirect(req.getContextPath() + ruta);
    }
}
