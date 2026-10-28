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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class ServicioFacturacion {

    private final CelularDAO celularDAO = new CelularDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();

    private WSBanQuito obtenerBanQuitoProxy() {
        try {
            URL wsdlUrl = new URL("http://localhost:8080/WS_BanQuito_SOAP/WSBanQuito?wsdl");
            WSBanQuito_Service service = new WSBanQuito_Service(wsdlUrl);
            return service.getWSBanQuitoPort();
        } catch (Exception e) {
            // Fallback al constructor por defecto
            return new WSBanQuito_Service().getWSBanQuitoPort();
        }
    }

    public Factura facturarEfectivo(String cedula, String nombre, int codCelular, int cantidad) throws Exception {
        if (cantidad <= 0) cantidad = 1;

        Celular cel = celularDAO.buscarPorId(codCelular);
        if (cel == null || cel.getActivo() != 1) {
            throw new Exception("El celular seleccionado no existe o no está activo en el catálogo.");
        }

        BigDecimal cantBd = BigDecimal.valueOf(cantidad);
        BigDecimal subtotal = cel.getPrecio().multiply(cantBd).setScale(2, RoundingMode.HALF_UP);

        // Descuento automático del 42 % para pago en efectivo
        BigDecimal porcentajeDescuento = new BigDecimal("0.42");
        BigDecimal descuento = subtotal.multiply(porcentajeDescuento).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.subtract(descuento).setScale(2, RoundingMode.HALF_UP);

        Factura factura = new Factura(0, cedula, nombre, null, "EFECTIVO", subtotal, descuento, total, null);
        DetalleFactura detalle = new DetalleFactura(0, cel.getCodCelular(), cel.getMarca(), cel.getModelo(), cantidad, cel.getPrecio(), subtotal);

        return facturaDAO.registrarFactura(factura, detalle);
    }

    public Factura facturarCredito(String cedula, String nombre, int codCelular, int cuotas) throws Exception {
        Celular cel = celularDAO.buscarPorId(codCelular);
        if (cel == null || cel.getActivo() != 1) {
            throw new Exception("El celular seleccionado no existe o no está activo en el catálogo.");
        }

        if (cuotas < 3 || cuotas > 18) {
            throw new Exception("El plazo de cuotas debe estar entre 3 y 18 meses.");
        }

        WSBanQuito banquito = obtenerBanQuitoProxy();

        // 1. Verificar si el comprador es sujeto de crédito en BanQuito (RN-01 a RN-04)
        RespuestaSujetoCredito respSujeto = banquito.verificarSujetoCredito(cedula);
        if (!respSujeto.isEsSujeto()) {
            throw new Exception("Crédito rechazado por BanQuito: " + respSujeto.getMotivo());
        }

        // 2. Verificar monto máximo permitido (RN-05)
        BigDecimal montoMaximo = banquito.obtenerMontoMaximo(respSujeto.getCodCliente());
        if (cel.getPrecio().compareTo(montoMaximo) > 0) {
            throw new Exception("El precio del celular ($" + cel.getPrecio() + ") supera su cupo máximo de crédito aprobado ($" + montoMaximo + ").");
        }

        // 3. Otorgar crédito en el banco (RN-06 a RN-08)
        RespuestaCredito respCred = banquito.otorgarCredito(respSujeto.getCodCliente(), cel.getPrecio(), cuotas);
        if (!respCred.isExito()) {
            throw new Exception("No se pudo otorgar el crédito: " + respCred.getMensaje());
        }

        // 4. Registrar la factura a CREDITO vinculada al cod_credito generado
        BigDecimal subtotal = cel.getPrecio().setScale(2, RoundingMode.HALF_UP);
        BigDecimal descuento = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal;

        Factura factura = new Factura(0, cedula, nombre, null, "CREDITO", subtotal, descuento, total, respCred.getCodCredito());
        DetalleFactura detalle = new DetalleFactura(0, cel.getCodCelular(), cel.getMarca(), cel.getModelo(), 1, cel.getPrecio(), subtotal);

        return facturaDAO.registrarFactura(factura, detalle);
    }

    public List<CuotaAmortizacion> consultarAmortizacion(int codCredito) {
        try {
            WSBanQuito banquito = obtenerBanQuitoProxy();
            return banquito.consultarTablaAmortizacion(codCredito);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
