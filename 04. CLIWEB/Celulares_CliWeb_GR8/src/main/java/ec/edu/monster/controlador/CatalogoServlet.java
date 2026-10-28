package ec.edu.monster.controlador;

import ec.edu.monster.servicio.ExcepcionCliente;
import ec.edu.monster.util.ValidadorEntrada;
import ec.edu.monster.ws.Celular;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

// CU-05: mantenimiento del catálogo de celulares (listar, nuevo, editar, eliminar)
@WebServlet(name = "CatalogoServlet", urlPatterns = {"/catalogo"})
public class CatalogoServlet extends ControladorBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        try {
            if ("nuevo".equals(accion)) {
                mostrar(req, resp, "celularForm");
                return;
            }
            if ("editar".equals(accion)) {
                int cod = ValidadorEntrada.entero(req.getParameter("cod"), "código", 1, Integer.MAX_VALUE);
                req.setAttribute("celular", servicio.obtenerCelular(cod));
                mostrar(req, resp, "celularForm");
                return;
            }
        } catch (ExcepcionCliente e) {
            error(req, e);
        }
        listar(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("eliminar".equals(req.getParameter("accion"))) {
            eliminar(req, resp);
        } else {
            guardar(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean todos = "1".equals(req.getParameter("todos"));
        try {
            req.setAttribute("celulares", servicio.listarCelulares(!todos));
        } catch (ExcepcionCliente e) {
            req.setAttribute("celulares", Collections.emptyList());
            error(req, e);
        }
        req.setAttribute("todos", todos);
        mostrar(req, resp, "catalogo");
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codTexto = req.getParameter("cod");
        boolean nuevo = codTexto == null || codTexto.isBlank();
        try {
            String marca = ValidadorEntrada.texto(req.getParameter("marca"), "marca", 40);
            String modelo = ValidadorEntrada.texto(req.getParameter("modelo"), "modelo", 60);
            BigDecimal precio = ValidadorEntrada.decimalPositivo(req.getParameter("precio"), "precio");
            Celular guardado;
            if (nuevo) {
                guardado = servicio.crearCelular(marca, modelo, precio);
            } else {
                int cod = ValidadorEntrada.entero(codTexto, "código", 1, Integer.MAX_VALUE);
                guardado = servicio.actualizarCelular(cod, marca, modelo, precio, req.getParameter("activo") != null);
            }
            avisar(req, (nuevo ? "Se registró el celular " : "Se actualizó el celular ")
                + guardado.getMarca() + " " + guardado.getModelo() + ".");
            redirigir(req, resp, "/catalogo?todos=1");
        } catch (ExcepcionCliente e) {
            // Se vuelve a mostrar el formulario con lo que escribió el usuario
            Celular celular = new Celular();
            celular.setCodCelular(nuevo || !codTexto.trim().matches("\\d{1,9}") ? 0 : Integer.parseInt(codTexto.trim()));
            celular.setMarca(req.getParameter("marca"));
            celular.setModelo(req.getParameter("modelo"));
            celular.setActivo(nuevo || req.getParameter("activo") != null);
            req.setAttribute("celular", celular);
            req.setAttribute("precioTexto", req.getParameter("precio"));
            error(req, e);
            mostrar(req, resp, "celularForm");
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int cod = ValidadorEntrada.entero(req.getParameter("cod"), "código", 1, Integer.MAX_VALUE);
            servicio.eliminarCelular(cod);
            avisar(req, "El celular se eliminó del catálogo. Si ya tenía ventas, quedó desactivado para conservar las facturas.");
            redirigir(req, resp, "/catalogo?todos=1");
        } catch (ExcepcionCliente e) {
            error(req, e);
            listar(req, resp);
        }
    }
}
