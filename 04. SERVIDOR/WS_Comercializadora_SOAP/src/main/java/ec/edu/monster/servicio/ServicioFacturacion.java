package ec.edu.monster.servicio;

import ec.edu.monster.dao.CelularDAO;
import ec.edu.monster.dao.FacturaDAO;
import ec.edu.monster.modelo.Celular;
import ec.edu.monster.modelo.DetalleFactura;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.proxy.CuotaAmortizacion;
import ec.edu.monster.proxy.RespuestaCredito;
import ec.edu.monster.proxy.RespuestaSujetoCredito;
import ec.edu.monster.proxy.WSBanQuito;
import ec.edu.monster.proxy.WSBanQuito_Service;
import ec.edu.monster.util.ValidadorCedula;
import jakarta.xml.ws.WebServiceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicioFacturacion {

    private static final Logger LOG = Logger.getLogger(ServicioFacturacion.class.getName());
    private static final BigDecimal DESCUENTO_EFECTIVO = new BigDecimal("0.42");

    private final CelularDAO celularDAO = new CelularDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();

    // El proxy toma el WSDL de http://localhost:8080/WS_BanQuito_SOAP/WSBanQuito?wsdl
    private WSBanQuito obtenerBanQuitoProxy() {
        return new WSBanQuito_Service().getWSBanQuitoPort();
    }

    public Factura facturarEfectivo(String cedula, String nombre, int codCelular, int cantidad) throws ExcepcionComercializadora {
        validarComprador(cedula, nombre);
        if (cantidad < 1) {
            throw new ExcepcionComercializadora(CodigoError.VAL_001, "La cantidad debe ser al menos 1.");
        }
        Celular cel = celularDisponible(codCelular);

        BigDecimal cantBd = BigDecimal.valueOf(cantidad);
        BigDecimal subtotal = cel.getPrecio().multiply(cantBd).setScale(2, RoundingMode.HALF_UP);

        // Descuento automático del 42 % para pago en efectivo
        BigDecimal descuento = subtotal.multiply(DESCUENTO_EFECTIVO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.subtract(descuento).setScale(2, RoundingMode.HALF_UP);

        Factura factura = new Factura(0, cedula.trim(), nombre.trim(), null, "EFECTIVO", subtotal, descuento, total, null);
        DetalleFactura detalle = new DetalleFactura(0, cel.getCodCelular(), cel.getMarca(), cel.getModelo(), cantidad, cel.getPrecio(), subtotal);

        try {
            return facturaDAO.registrarFactura(factura, detalle);
        } catch (SQLException e) {
            throw ExcepcionComercializadora.infraestructura(e);
        }
    }

    public Factura facturarCredito(String cedula, String nombre, int codCelular, int numeroCuotas) throws ExcepcionComercializadora {
        validarComprador(cedula, nombre);
        if (numeroCuotas < 3 || numeroCuotas > 18) {
            throw new ExcepcionComercializadora(CodigoError.CRE_003);
        }
        Celular cel = celularDisponible(codCelular);

        RespuestaCredito respCred;
        try {
            WSBanQuito banquito = obtenerBanQuitoProxy();

            // 1. Verificar si el comprador es sujeto de crédito en BanQuito (RN-01 a RN-04)
            RespuestaSujetoCredito respSujeto = banquito.verificarSujetoCredito(cedula.trim());
            if (!respSujeto.isEsSujeto()) {
                throw ExcepcionComercializadora.desdeMensajeCore(respSujeto.getMotivo());
            }

            // 2. Verificar monto máximo permitido (RN-05 y RN-06)
            BigDecimal montoMaximo = banquito.obtenerMontoMaximo(respSujeto.getCodCliente());
            if (cel.getPrecio().compareTo(montoMaximo) > 0) {
                throw new ExcepcionComercializadora(CodigoError.CRE_002, "Monto máximo aprobado: $" + montoMaximo + ".");
            }

            // 3. Otorgar crédito en el banco (RN-07 y RN-08)
            respCred = banquito.otorgarCredito(respSujeto.getCodCliente(), cel.getPrecio(), numeroCuotas);
        } catch (WebServiceException e) {
            throw ExcepcionComercializadora.desdeFallaCore(e);
        }
        if (!respCred.isExito()) {
            throw ExcepcionComercializadora.desdeMensajeCore(respCred.getMensaje());
        }

        // 4. Registrar la factura a CREDITO vinculada al cod_credito generado
        BigDecimal subtotal = cel.getPrecio().setScale(2, RoundingMode.HALF_UP);
        BigDecimal descuento = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal;

        Factura factura = new Factura(0, cedula.trim(), nombre.trim(), null, "CREDITO", subtotal, descuento, total, respCred.getCodCredito());
        DetalleFactura detalle = new DetalleFactura(0, cel.getCodCelular(), cel.getMarca(), cel.getModelo(), 1, cel.getPrecio(), subtotal);

        try {
            return facturaDAO.registrarFactura(factura, detalle);
        } catch (SQLException e) {
            // El crédito ya quedó registrado en el Core y no existe una operación para anularlo
            LOG.log(Level.SEVERE, "El crédito " + respCred.getCodCredito() + " se otorgó, pero la factura no se registró", e);
            throw new ExcepcionComercializadora(CodigoError.BDD_001, "El crédito " + respCred.getCodCredito()
                + " ya fue otorgado en BanQuito; no vuelva a facturar y reporte este número al administrador.");
        }
    }

    // CU-08: la tabla se consulta con el número de factura; el código del crédito se obtiene de la factura
    public List<CuotaAmortizacion> consultarAmortizacion(int numFactura) throws ExcepcionComercializadora {
        Factura factura = obtenerFactura(numFactura);
        if (factura.getCodCredito() == null) {
            throw new ExcepcionComercializadora(CodigoError.VAL_001,
                "La factura " + numFactura + " se pagó en efectivo y no tiene tabla de amortización.");
        }
        try {
            return obtenerBanQuitoProxy().consultarTablaAmortizacion(factura.getCodCredito());
        } catch (WebServiceException e) {
            throw ExcepcionComercializadora.desdeFallaCore(e);
        }
    }

    public Factura obtenerFactura(int numFactura) throws ExcepcionComercializadora {
        if (numFactura <= 0) {
            throw new ExcepcionComercializadora(CodigoError.VAL_001, "El número de factura debe ser mayor a cero.");
        }
        try {
            Factura factura = facturaDAO.obtenerFactura(numFactura);
            if (factura == null) {
                throw new ExcepcionComercializadora(CodigoError.VAL_001, "La factura " + numFactura + " no existe.");
            }
            return factura;
        } catch (SQLException e) {
            throw ExcepcionComercializadora.infraestructura(e);
        }
    }

    private void validarComprador(String cedula, String nombre) throws ExcepcionComercializadora {
        if (!ValidadorCedula.esValida(cedula == null ? null : cedula.trim())) {
            throw new ExcepcionComercializadora(CodigoError.VAL_001, "La cédula debe tener 10 dígitos y un dígito verificador correcto.");
        }
        if (nombre == null || nombre.isBlank() || nombre.trim().length() > 100) {
            throw new ExcepcionComercializadora(CodigoError.VAL_001, "El nombre del cliente es obligatorio (máximo 100 caracteres).");
        }
    }

    private Celular celularDisponible(int codCelular) throws ExcepcionComercializadora {
        try {
            Celular cel = celularDAO.buscarPorId(codCelular);
            if (cel == null || !cel.isActivo()) {
                throw new ExcepcionComercializadora(CodigoError.VAL_001,
                    "El celular " + codCelular + " no existe o no está disponible en el catálogo.");
            }
            return cel;
        } catch (SQLException e) {
            throw ExcepcionComercializadora.infraestructura(e);
        }
    }
}
