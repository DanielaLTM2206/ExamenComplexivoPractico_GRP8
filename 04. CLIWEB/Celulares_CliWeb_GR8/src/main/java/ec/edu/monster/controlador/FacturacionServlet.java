package ec.edu.monster.controlador;

import ec.edu.monster.servicio.ExcepcionCliente;
import ec.edu.monster.util.ValidadorEntrada;
import ec.edu.monster.ws.Factura;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

// CU-06 y CU-07: facturación en efectivo (42 % de descuento) o con crédito directo de BanQuito
@WebServlet(name = "FacturacionServlet", urlPatterns = {"/facturar"})
public class FacturacionServlet extends ControladorBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mostrarFormulario(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String cedula = ValidadorEntrada.cedula(req.getParameter("cedula"));
            String nombre = ValidadorEntrada.texto(req.getParameter("nombre"), "nombre", 100);
            int codCelular = ValidadorEntrada.entero(req.getParameter("codCelular"), "celular", 1, Integer.MAX_VALUE);

            Factura factura;
            if ("CREDITO".equals(req.getParameter("formaPago"))) {
                int cuotas = ValidadorEntrada.entero(req.getParameter("numeroCuotas"), "número de cuotas", 3, 18);
                factura = servicio.facturarCredito(cedula, nombre, codCelular, cuotas);
                avisar(req, "Crédito aprobado por BanQuito (crédito N.º " + factura.getCodCredito()
                    + "). Se emitió la factura N.º " + factura.getNumFactura() + ".");
            } else {
                int cantidad = ValidadorEntrada.entero(req.getParameter("cantidad"), "cantidad", 1, 99);
                factura = servicio.facturarEfectivo(cedula, nombre, codCelular, cantidad);
                avisar(req, "Se emitió la factura N.º " + factura.getNumFactura() + " con el 42 % de descuento por pago en efectivo.");
            }
            redirigir(req, resp, "/factura?num=" + factura.getNumFactura());
        } catch (ExcepcionCliente e) {
            // El formulario conserva lo escrito (param.*) para corregir y volver a intentar
            error(req, e);
            mostrarFormulario(req, resp);
        }
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("celulares", servicio.listarCelulares(true));
        } catch (ExcepcionCliente e) {
            req.setAttribute("celulares", Collections.emptyList());
            if (req.getAttribute("error") == null) {
                error(req, e);
            }
        }
        mostrar(req, resp, "facturar");
    }
}
