package ec.edu.monster.ws;

import ec.edu.monster.modelo.CuotaAmortizacion;
import ec.edu.monster.modelo.RespuestaCredito;
import ec.edu.monster.modelo.RespuestaSujetoCredito;
import ec.edu.monster.servicio.ServicioCredito;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.math.BigDecimal;
import java.util.List;

@WebService(serviceName = "WSBanQuito")
public class WSBanQuito {

    private final ServicioCredito servicio = new ServicioCredito();

    @WebMethod(operationName = "verificarSujetoCredito")
    public RespuestaSujetoCredito verificarSujetoCredito(@WebParam(name = "cedula") String cedula) {
        return servicio.verificarSujeto(cedula);
    }

    @WebMethod(operationName = "obtenerMontoMaximo")
    public BigDecimal obtenerMontoMaximo(@WebParam(name = "codCliente") int codCliente) {
        return servicio.calcularMontoMaximo(codCliente);
    }

    @WebMethod(operationName = "otorgarCredito")
    public RespuestaCredito otorgarCredito(
            @WebParam(name = "codCliente") int codCliente,
            @WebParam(name = "precio") BigDecimal precio,
            @WebParam(name = "numeroCuotas") int numeroCuotas) {
        return servicio.otorgarCredito(codCliente, precio, numeroCuotas);
    }

    @WebMethod(operationName = "consultarTablaAmortizacion")
    public List<CuotaAmortizacion> consultarTablaAmortizacion(
            @WebParam(name = "codCredito") int codCredito) {
        return servicio.consultarTabla(codCredito);
    }
}
