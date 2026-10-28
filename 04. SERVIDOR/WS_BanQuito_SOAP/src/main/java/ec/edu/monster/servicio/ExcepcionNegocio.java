package ec.edu.monster.servicio;

import java.util.logging.Level;
import java.util.logging.Logger;

// Se propaga como SOAP Fault cuyo mensaje inicia con el código del ERS (por ejemplo "VAL-001: ...").
// Es no comprobada para no alterar el WSDL que ya consume la comercializadora.
public class ExcepcionNegocio extends RuntimeException {

    private static final Logger LOG = Logger.getLogger(ExcepcionNegocio.class.getName());

    private final String codigo;

    public ExcepcionNegocio(CodigoError error) {
        super(error.mensaje());
        this.codigo = error.getCodigo();
    }

    public ExcepcionNegocio(CodigoError error, String detalle) {
        super(error.mensaje(detalle));
        this.codigo = error.getCodigo();
    }

    // El detalle técnico se registra en el log del servidor y no se envía al cliente
    public static ExcepcionNegocio baseDatos(Exception causa) {
        LOG.log(Level.SEVERE, "Error de base de datos en el Core BanQuito", causa);
        return new ExcepcionNegocio(CodigoError.BDD_001);
    }

    public String getCodigo() { return codigo; }
}
