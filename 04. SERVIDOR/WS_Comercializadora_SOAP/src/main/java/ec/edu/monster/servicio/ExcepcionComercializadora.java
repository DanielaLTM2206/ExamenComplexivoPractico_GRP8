package ec.edu.monster.servicio;

import jakarta.xml.ws.WebServiceException;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

// Se declara en el WSDL como SOAP Fault: los clientes reciben el código del ERS y el mensaje para el usuario
public class ExcepcionComercializadora extends Exception {

    private static final Logger LOG = Logger.getLogger(ExcepcionComercializadora.class.getName());
    private static final Pattern MENSAJE_CON_CODIGO = Pattern.compile("(?s)^[A-Z]{3}-\\d{3}: .+");

    private final String codigo;

    public ExcepcionComercializadora(CodigoError error) {
        this(error.getCodigo(), error.mensaje());
    }

    public ExcepcionComercializadora(CodigoError error, String detalle) {
        this(error.getCodigo(), error.mensaje(detalle));
    }

    private ExcepcionComercializadora(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    // El detalle técnico se registra en el log del servidor y no se envía al cliente
    public static ExcepcionComercializadora infraestructura(Exception causa) {
        LOG.log(Level.SEVERE, "Error de infraestructura en la Comercializadora", causa);
        return new ExcepcionComercializadora(CodigoError.BDD_001);
    }

    // Conserva el código que envía el Core BanQuito ("CRE-001: ..."); si no lo trae, es una falla de infraestructura
    public static ExcepcionComercializadora desdeMensajeCore(String mensaje) {
        return tieneCodigo(mensaje)
            ? new ExcepcionComercializadora(mensaje.substring(0, 7), mensaje)
            : infraestructura(new IllegalStateException("Respuesta del Core sin código: " + mensaje));
    }

    // Un SOAP Fault del Core trae el código en su mensaje; un error de conexión no lo trae
    public static ExcepcionComercializadora desdeFallaCore(WebServiceException e) {
        return tieneCodigo(e.getMessage())
            ? new ExcepcionComercializadora(e.getMessage().substring(0, 7), e.getMessage())
            : infraestructura(e);
    }

    private static boolean tieneCodigo(String mensaje) {
        return mensaje != null && MENSAJE_CON_CODIGO.matcher(mensaje).matches();
    }

    public String getCodigo() { return codigo; }
}
