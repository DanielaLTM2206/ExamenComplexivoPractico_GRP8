package ec.edu.monster.ws;

import ec.edu.monster.dao.CelularDAO;
import ec.edu.monster.dao.FacturaDAO;
import ec.edu.monster.modelo.Celular;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.proxy.CuotaAmortizacion;
import ec.edu.monster.servicio.ServicioFacturacion;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebService(serviceName = "WSComercializadora")
public class WSComercializadora {

    private final CelularDAO celularDAO = new CelularDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final ServicioFacturacion servicioFacturacion = new ServicioFacturacion();

    // -------------------------------------------------------------------------
    // Mantenimiento CRUD del Catálogo de Celulares
    // -------------------------------------------------------------------------

    @WebMethod(operationName = "crearCelular")
    public Celular crearCelular(
            @WebParam(name = "marca") String marca,
            @WebParam(name = "modelo") String modelo,
            @WebParam(name = "precio") BigDecimal precio) throws Exception {
        try {
            return celularDAO.crearCelular(marca, modelo, precio);
        } catch (SQLException e) {
            throw new Exception("Error al crear celular: " + e.getMessage());
        }
    }

    @WebMethod(operationName = "listarCelulares")
    public List<Celular> listarCelulares(@WebParam(name = "soloActivos") boolean soloActivos) {
        try {
            return celularDAO.listarCelulares(soloActivos);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @WebMethod(operationName = "obtenerCelular")
    public Celular obtenerCelular(@WebParam(name = "codCelular") int codCelular) {
        try {
            return celularDAO.buscarPorId(codCelular);
        } catch (SQLException e) {
            return null;
        }
    }

    @WebMethod(operationName = "actualizarCelular")
    public boolean actualizarCelular(
            @WebParam(name = "codCelular") int codCelular,
            @WebParam(name = "marca") String marca,
            @WebParam(name = "modelo") String modelo,
            @WebParam(name = "precio") BigDecimal precio,
            @WebParam(name = "activo") int activo) throws Exception {
        try {
            return celularDAO.actualizarCelular(codCelular, marca, modelo, precio, activo);
        } catch (SQLException e) {
            throw new Exception("Error al actualizar celular: " + e.getMessage());
        }
    }

    @WebMethod(operationName = "eliminarCelular")
    public boolean eliminarCelular(@WebParam(name = "codCelular") int codCelular) throws Exception {
        try {
            return celularDAO.eliminarCelular(codCelular);
        } catch (SQLException e) {
            throw new Exception("Error al eliminar celular: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Facturación y Crédito
    // -------------------------------------------------------------------------

    @WebMethod(operationName = "facturarEfectivo")
    public Factura facturarEfectivo(
            @WebParam(name = "cedula") String cedula,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "codCelular") int codCelular,
            @WebParam(name = "cantidad") int cantidad) throws Exception {
        return servicioFacturacion.facturarEfectivo(cedula, nombre, codCelular, cantidad);
    }

    @WebMethod(operationName = "facturarCredito")
    public Factura facturarCredito(
            @WebParam(name = "cedula") String cedula,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "codCelular") int codCelular,
            @WebParam(name = "cuotas") int cuotas) throws Exception {
        return servicioFacturacion.facturarCredito(cedula, nombre, codCelular, cuotas);
    }

    @WebMethod(operationName = "consultarAmortizacion")
    public List<CuotaAmortizacion> consultarAmortizacion(@WebParam(name = "codCredito") int codCredito) {
        return servicioFacturacion.consultarAmortizacion(codCredito);
    }

    @WebMethod(operationName = "obtenerFactura")
    public Factura obtenerFactura(@WebParam(name = "numFactura") int numFactura) {
        try {
            return facturaDAO.obtenerFactura(numFactura);
        } catch (SQLException e) {
            return null;
        }
    }
}
