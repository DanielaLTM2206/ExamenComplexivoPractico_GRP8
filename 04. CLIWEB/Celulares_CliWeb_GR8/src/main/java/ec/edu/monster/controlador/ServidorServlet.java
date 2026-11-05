package ec.edu.monster.controlador;

import ec.edu.monster.servicio.ExcepcionCliente;
import ec.edu.monster.servicio.ProbadorConexion;
import ec.edu.monster.servicio.ServicioComercializadora;
import ec.edu.monster.util.ConfiguracionServidor;
import ec.edu.monster.util.DireccionServidor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Página "Servidor": cambia la dirección de WSComercializadora (por si el cliente web se despliega en otra PC)
@WebServlet(name = "ServidorServlet", urlPatterns = {"/servidor"})
public class ServidorServlet extends ControladorBase {

    private final ProbadorConexion probador = new ProbadorConexion();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mostrarPagina(req, resp, ConfiguracionServidor.getUrlActual());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("restablecer".equals(accion)) {
            ConfiguracionServidor.restablecer();
            ServicioComercializadora.reiniciar();
            avisar(req, "Se restableció el servidor local: " + DireccionServidor.URL_LOCAL);
            redirigir(req, resp, "/servidor");
            return;
        }

        String escrito = req.getParameter("url");
        String url;
        try {
            url = DireccionServidor.completar(escrito);
        } catch (IllegalArgumentException e) {
            error(req, new ExcepcionCliente("VAL-001", e.getMessage()));
            mostrarPagina(req, resp, escrito);
            return;
        }

        ProbadorConexion.Resultado resultado = probador.probar(url);
        // Se guarda aunque falle solo si el usuario lo confirmó (el servidor puede estar apagado por ahora)
        if ("guardar".equals(accion) && (resultado.exitosa() || req.getParameter("forzar") != null)) {
            ConfiguracionServidor.guardar(url);
            ServicioComercializadora.reiniciar();
            avisar(req, "El cliente web usará el servidor: " + url);
            redirigir(req, resp, "/servidor");
            return;
        }
        req.setAttribute("resultado", resultado);
        req.setAttribute("ofrecerForzar", "guardar".equals(accion) && !resultado.exitosa());
        mostrarPagina(req, resp, url);
    }

    private void mostrarPagina(HttpServletRequest req, HttpServletResponse resp, String urlEscrita) throws ServletException, IOException {
        req.setAttribute("urlActual", ConfiguracionServidor.getUrlActual());
        req.setAttribute("porDefecto", ConfiguracionServidor.esPorDefecto());
        req.setAttribute("urlLocal", DireccionServidor.URL_LOCAL);
        req.setAttribute("urlEscrita", urlEscrita);
        mostrar(req, resp, "servidor");
    }
}
