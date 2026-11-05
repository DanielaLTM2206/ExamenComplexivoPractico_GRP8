package ec.edu.monster.ws;

import ec.edu.monster.modelo.Celular;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.modelo.ItemVenta;
import ec.edu.monster.proxy.CuotaAmortizacion;
import ec.edu.monster.servicio.ExcepcionComercializadora;
import ec.edu.monster.servicio.ServicioCatalogo;
import ec.edu.monster.servicio.ServicioFacturacion;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.math.BigDecimal;
import java.util.List;

// Los errores llegan a los clientes como SOAP Fault ExcepcionComercializadora con el código del ERS (VAL-001, CRE-001, ...)
@WebService(serviceName = "WSComercializadora")
public class WSComercializadora {

    private final ServicioCatalogo servicioCatalogo = new ServicioCatalogo();
    private final ServicioFacturacion servicioFacturacion = new ServicioFacturacion();

    // -------------------------------------------------------------------------
    // Mantenimiento CRUD del Catálogo de Celulares
    // -------------------------------------------------------------------------

    @WebMethod(operationName = "crearCelular")
    public Celular crearCelular(
            @WebParam(name = "marca") String marca,
            @WebParam(name = "modelo") String modelo,
            @WebParam(name = "precio") BigDecimal precio) throws ExcepcionComercializadora {
        return servicioCatalogo.crear(marca, modelo, precio);
    }

    @WebMethod(operationName = "listarCelulares")
    public List<Celular> listarCelulares(@WebParam(name = "soloActivos") boolean soloActivos) throws ExcepcionComercializadora {
        return servicioCatalogo.listar(soloActivos);
    }

    @WebMethod(operationName = "obtenerCelular")
    public Celular obtenerCelular(@WebParam(name = "codCelular") int codCelular) throws ExcepcionComercializadora {
        return servicioCatalogo.obtener(codCelular);
    }

    @WebMethod(operationName = "actualizarCelular")
    public Celular actualizarCelular(
            @WebParam(name = "codCelular") int codCelular,
            @WebParam(name = "marca") String marca,
            @WebParam(name = "modelo") String modelo,
            @WebParam(name = "precio") BigDecimal precio,
            @WebParam(name = "activo") boolean activo) throws ExcepcionComercializadora {
        return servicioCatalogo.actualizar(codCelular, marca, modelo, precio, activo);
    }

    @WebMethod(operationName = "eliminarCelular")
    public boolean eliminarCelular(@WebParam(name = "codCelular") int codCelular) throws ExcepcionComercializadora {
        return servicioCatalogo.eliminar(codCelular);
    }

    // -------------------------------------------------------------------------
    // Facturación y Crédito
    // -------------------------------------------------------------------------

    // Autocompletar la factura: nombre del comprador según BanQuito; null si no es cliente del banco
    @WebMethod(operationName = "buscarComprador")
    public String buscarComprador(@WebParam(name = "cedula") String cedula) throws ExcepcionComercializadora {
        return servicioFacturacion.buscarComprador(cedula);
    }

    @WebMethod(operationName = "facturarEfectivo")
    public Factura facturarEfectivo(
            @WebParam(name = "cedula") String cedula,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "codCelular") int codCelular,
            @WebParam(name = "cantidad") int cantidad) throws ExcepcionComercializadora {
        return servicioFacturacion.facturarEfectivo(cedula, nombre, codCelular, cantidad);
    }

    @WebMethod(operationName = "facturarCredito")
    public Factura facturarCredito(
            @WebParam(name = "cedula") String cedula,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "codCelular") int codCelular,
            @WebParam(name = "numeroCuotas") int numeroCuotas) throws ExcepcionComercializadora {
        return servicioFacturacion.facturarCredito(cedula, nombre, codCelular, numeroCuotas);
    }

    // Venta de varios celulares: cada ítem es {codCelular, cantidad}; el mismo celular repetido se suma
    @WebMethod(operationName = "facturarVentaEfectivo")
    public Factura facturarVentaEfectivo(
            @WebParam(name = "cedula") String cedula,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "items") List<ItemVenta> items) throws ExcepcionComercializadora {
        return servicioFacturacion.facturarVentaEfectivo(cedula, nombre, items);
    }

    // Un solo crédito BanQuito por el total de la venta; el total debe ser menor o igual al monto máximo (CRE-002)
    @WebMethod(operationName = "facturarVentaCredito")
    public Factura facturarVentaCredito(
            @WebParam(name = "cedula") String cedula,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "items") List<ItemVenta> items,
            @WebParam(name = "numeroCuotas") int numeroCuotas) throws ExcepcionComercializadora {
        return servicioFacturacion.facturarVentaCredito(cedula, nombre, items, numeroCuotas);
    }

    @WebMethod(operationName = "consultarAmortizacion")
    public List<CuotaAmortizacion> consultarAmortizacion(@WebParam(name = "numFactura") int numFactura) throws ExcepcionComercializadora {
        return servicioFacturacion.consultarAmortizacion(numFactura);
    }

    @WebMethod(operationName = "obtenerFactura")
    public Factura obtenerFactura(@WebParam(name = "numFactura") int numFactura) throws ExcepcionComercializadora {
        return servicioFacturacion.obtenerFactura(numFactura);
    }
}
