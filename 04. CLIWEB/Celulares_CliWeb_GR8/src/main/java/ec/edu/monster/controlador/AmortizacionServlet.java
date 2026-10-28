package ec.edu.monster.controlador;

import ec.edu.monster.servicio.ExcepcionCliente;
import ec.edu.monster.util.ValidadorEntrada;
import ec.edu.monster.ws.CuotaAmortizacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

// CU-08: tabla de amortización del crédito asociado a una factura
@WebServlet(name = "AmortizacionServlet", urlPatterns = {"/amortizacion"})
public class AmortizacionServlet extends ControladorBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String num = req.getParameter("numFactura");
        if (num != null && !num.isBlank()) {
            try {
                int numFactura = ValidadorEntrada.entero(num, "número de factura", 1, Integer.MAX_VALUE);
                List<CuotaAmortizacion> cuotas = servicio.consultarAmortizacion(numFactura);
                req.setAttribute("factura", servicio.obtenerFactura(numFactura));
                req.setAttribute("cuotas", cuotas);
                req.setAttribute("totalPagado", cuotas.stream().map(CuotaAmortizacion::getValorCuota).reduce(BigDecimal.ZERO, BigDecimal::add));
                req.setAttribute("totalInteres", cuotas.stream().map(CuotaAmortizacion::getInteres).reduce(BigDecimal.ZERO, BigDecimal::add));
                req.setAttribute("totalCapital", cuotas.stream().map(CuotaAmortizacion::getCapital).reduce(BigDecimal.ZERO, BigDecimal::add));
            } catch (ExcepcionCliente e) {
                error(req, e);
            }
        }
        mostrar(req, resp, "amortizacion");
    }
}
