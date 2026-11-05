package ec.edu.monster.controlador;

import ec.edu.monster.servicio.ExcepcionCliente;
import ec.edu.monster.util.ValidadorEntrada;
import ec.edu.monster.ws.Factura;
import ec.edu.monster.ws.ItemVenta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// CU-06 y CU-07: facturación de uno o varios celulares en efectivo (42 % de descuento) o con crédito directo de BanQuito
@WebServlet(name = "FacturacionServlet", urlPatterns = {"/facturar"})
public class FacturacionServlet extends ControladorBase {

    private static final int MAX_CELULARES = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mostrarFormulario(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String cedula = ValidadorEntrada.cedula(req.getParameter("cedula"));
            String nombre = ValidadorEntrada.texto(req.getParameter("nombre"), "nombre", 100);
            List<ItemVenta> items = leerCarrito(req);

            Factura factura;
            if ("CREDITO".equals(req.getParameter("formaPago"))) {
                int cuotas = ValidadorEntrada.entero(req.getParameter("numeroCuotas"), "número de cuotas", 3, 18);
                factura = servicio.facturarVentaCredito(cedula, nombre, items, cuotas);
                avisar(req, "Crédito aprobado por BanQuito (crédito N.º " + factura.getCodCredito()
                    + "). Se emitió la factura N.º " + factura.getNumFactura() + ".");
            } else {
                factura = servicio.facturarVentaEfectivo(cedula, nombre, items);
                avisar(req, "Se emitió la factura N.º " + factura.getNumFactura() + " con el 42 % de descuento por pago en efectivo.");
            }
            redirigir(req, resp, "/factura?num=" + factura.getNumFactura());
        } catch (ExcepcionCliente e) {
            // El formulario conserva lo escrito (param.* y el carrito) para corregir y volver a intentar
            error(req, e);
            mostrarFormulario(req, resp);
        }
    }

    // El carrito llega como pares itemCelular / itemCantidad (una fila por celular)
    private List<ItemVenta> leerCarrito(HttpServletRequest req) throws ExcepcionCliente {
        String[] celulares = req.getParameterValues("itemCelular");
        String[] cantidades = req.getParameterValues("itemCantidad");
        if (celulares == null || celulares.length == 0) {
            throw new ExcepcionCliente("VAL-001", "Agregue al menos un celular a la venta.");
        }
        if (cantidades == null || cantidades.length != celulares.length) {
            throw new ExcepcionCliente("VAL-001", "El detalle de la venta está incompleto. Vuelva a agregar los celulares.");
        }
        if (celulares.length > MAX_CELULARES) {
            throw new ExcepcionCliente("VAL-001", "Una factura admite máximo " + MAX_CELULARES + " celulares distintos.");
        }
        List<ItemVenta> items = new ArrayList<>();
        for (int i = 0; i < celulares.length; i++) {
            ItemVenta item = new ItemVenta();
            item.setCodCelular(ValidadorEntrada.entero(celulares[i], "celular", 1, Integer.MAX_VALUE));
            item.setCantidad(ValidadorEntrada.entero(cantidades[i], "cantidad", 1, 99));
            items.add(item);
        }
        return items;
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
        // Carrito enviado ("cod:cantidad,cod:cantidad") para reconstruirlo si hubo un error
        String[] celulares = req.getParameterValues("itemCelular");
        String[] cantidades = req.getParameterValues("itemCantidad");
        if (celulares != null && cantidades != null && celulares.length == cantidades.length) {
            StringBuilder carrito = new StringBuilder();
            for (int i = 0; i < celulares.length; i++) {
                if (celulares[i].matches("\\d{1,9}") && cantidades[i].matches("\\d{1,2}")) {
                    carrito.append(carrito.length() > 0 ? "," : "").append(celulares[i]).append(':').append(cantidades[i]);
                }
            }
            req.setAttribute("carrito", carrito.toString());
        }
        mostrar(req, resp, "facturar");
    }
}
