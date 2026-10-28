package ec.edu.monster.servicio;

import ec.edu.monster.ws.Celular;
import ec.edu.monster.ws.CuotaAmortizacion;
import ec.edu.monster.ws.ExcepcionComercializadora;
import ec.edu.monster.ws.ExcepcionComercializadora_Exception;
import ec.edu.monster.ws.Factura;
import ec.edu.monster.ws.WSComercializadora;
import ec.edu.monster.ws.WSComercializadora_Service;
import jakarta.xml.ws.WebServiceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Único punto del cliente web que invoca a WSComercializadora (WSDL en
// http://localhost:8080/WS_Comercializadora_SOAP/WSComercializadora?wsdl)
public class ServicioComercializadora {

    private static final Logger LOG = Logger.getLogger(ServicioComercializadora.class.getName());

    // Crear el Service descarga el WSDL, por eso se crea una sola vez y solo cuando el servidor responde
    private static WSComercializadora_Service servicio;

    @FunctionalInterface
    private interface Llamada<T> {
        T ejecutar(WSComercializadora puerto) throws ExcepcionComercializadora_Exception;
    }

    private static synchronized WSComercializadora puerto() {
        if (servicio == null) {
            servicio = new WSComercializadora_Service();
        }
        return servicio.getWSComercializadoraPort();
    }

    private static synchronized void reiniciar() {
        servicio = null;
    }

    private <T> T invocar(Llamada<T> llamada) throws ExcepcionCliente {
        try {
            return llamada.ejecutar(puerto());
        } catch (ExcepcionComercializadora_Exception e) {
            ExcepcionComercializadora falla = e.getFaultInfo();
            throw new ExcepcionCliente(falla.getCodigo(), falla.getMessage());
        } catch (WebServiceException e) {
            LOG.log(Level.SEVERE, "No se pudo comunicar con WSComercializadora", e);
            reiniciar();
            throw new ExcepcionCliente("BDD-001",
                "BDD-001: No fue posible conectar con la comercializadora. Verifique que el servidor esté encendido e intente nuevamente.");
        }
    }

    public List<Celular> listarCelulares(boolean soloActivos) throws ExcepcionCliente {
        return invocar(p -> p.listarCelulares(soloActivos));
    }

    public Celular obtenerCelular(int codCelular) throws ExcepcionCliente {
        return invocar(p -> p.obtenerCelular(codCelular));
    }

    public Celular crearCelular(String marca, String modelo, BigDecimal precio) throws ExcepcionCliente {
        return invocar(p -> p.crearCelular(marca, modelo, precio));
    }

    public Celular actualizarCelular(int codCelular, String marca, String modelo, BigDecimal precio, boolean activo) throws ExcepcionCliente {
        return invocar(p -> p.actualizarCelular(codCelular, marca, modelo, precio, activo));
    }

    public boolean eliminarCelular(int codCelular) throws ExcepcionCliente {
        return invocar(p -> p.eliminarCelular(codCelular));
    }

    public Factura facturarEfectivo(String cedula, String nombre, int codCelular, int cantidad) throws ExcepcionCliente {
        return invocar(p -> p.facturarEfectivo(cedula, nombre, codCelular, cantidad));
    }

    public Factura facturarCredito(String cedula, String nombre, int codCelular, int numeroCuotas) throws ExcepcionCliente {
        return invocar(p -> p.facturarCredito(cedula, nombre, codCelular, numeroCuotas));
    }

    public Factura obtenerFactura(int numFactura) throws ExcepcionCliente {
        return invocar(p -> p.obtenerFactura(numFactura));
    }

    public List<CuotaAmortizacion> consultarAmortizacion(int numFactura) throws ExcepcionCliente {
        return invocar(p -> p.consultarAmortizacion(numFactura));
    }
}
