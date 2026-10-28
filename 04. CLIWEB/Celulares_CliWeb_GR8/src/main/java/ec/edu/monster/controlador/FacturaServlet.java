package ec.edu.monster.controlador;

import ec.edu.monster.servicio.ExcepcionCliente;
import ec.edu.monster.util.ValidadorEntrada;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Consulta de una factura emitida por su número
@WebServlet(name = "FacturaServlet", urlPatterns = {"/factura"})
public class FacturaServlet extends ControladorBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String num = req.getParameter("num");
        if (num != null && !num.isBlank()) {
            try {
                int numFactura = ValidadorEntrada.entero(num, "número de factura", 1, Integer.MAX_VALUE);
                req.setAttribute("factura", servicio.obtenerFactura(numFactura));
            } catch (ExcepcionCliente e) {
                error(req, e);
            }
        }
        mostrar(req, resp, "factura");
    }
}
