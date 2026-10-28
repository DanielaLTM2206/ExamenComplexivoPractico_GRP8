package ec.edu.monster.servicio;

import ec.edu.monster.dao.ClienteDAO;
import ec.edu.monster.dao.CreditoDAO;
import ec.edu.monster.dao.MovimientoDAO;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.Credito;
import ec.edu.monster.modelo.CuotaAmortizacion;
import ec.edu.monster.modelo.RespuestaCredito;
import ec.edu.monster.modelo.RespuestaSujetoCredito;
import ec.edu.monster.util.ValidadorCedula;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;

// Las reglas incumplidas se informan en el objeto de respuesta; los datos inválidos de las operaciones
// sin objeto de respuesta y las fallas de base de datos (BDD-001) se lanzan como ExcepcionNegocio.
public class ServicioCredito {

    private static final BigDecimal PORCENTAJE = new BigDecimal("0.30");
    private static final BigDecimal MESES = new BigDecimal("6");

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final MovimientoDAO movimientoDAO = new MovimientoDAO();
    private final CreditoDAO creditoDAO = new CreditoDAO();

    public RespuestaSujetoCredito verificarSujeto(String cedula) {
        String ced = cedula == null ? "" : cedula.trim();
        if (!ValidadorCedula.esValida(ced)) {
            return new RespuestaSujetoCredito(false, null,
                CodigoError.VAL_001.mensaje("La cédula debe tener 10 dígitos y un dígito verificador correcto."));
        }

        try {
            // RN-01: Ser cliente del banco
            Cliente cliente = clienteDAO.buscarPorCedula(ced);
            if (cliente == null) {
                return new RespuestaSujetoCredito(false, null, CodigoError.CLI_001.mensaje());
            }

            String motivo = motivoRechazo(cliente);
            if (motivo != null) {
                return new RespuestaSujetoCredito(false, cliente.getCodCliente(), CodigoError.CRE_001.mensaje(motivo));
            }
            return new RespuestaSujetoCredito(true, cliente.getCodCliente(), "El cliente es sujeto de crédito.");

        } catch (SQLException e) {
            throw ExcepcionNegocio.baseDatos(e);
        }
    }

    public BigDecimal calcularMontoMaximo(int codCliente) {
        if (codCliente <= 0) {
            throw new ExcepcionNegocio(CodigoError.VAL_001, "El código de cliente debe ser mayor a cero.");
        }
        try {
            if (clienteDAO.buscarPorId(codCliente) == null) {
                throw new ExcepcionNegocio(CodigoError.CLI_001);
            }
            return montoMaximo(codCliente);
        } catch (SQLException e) {
            throw ExcepcionNegocio.baseDatos(e);
        }
    }

    public RespuestaCredito otorgarCredito(int codCliente, BigDecimal monto, int cuotas) {
        if (codCliente <= 0 || monto == null || monto.signum() <= 0) {
            return rechazo(CodigoError.VAL_001.mensaje("El código de cliente y el monto deben ser mayores a cero."));
        }
        if (cuotas < 3 || cuotas > 18) {
            return rechazo(CodigoError.CRE_003.mensaje());
        }

        try {
            Cliente cliente = clienteDAO.buscarPorId(codCliente);
            if (cliente == null) {
                return rechazo(CodigoError.CLI_001.mensaje());
            }

            // Se vuelven a aplicar las reglas porque el servicio puede invocarse sin verificar antes al cliente
            String motivo = motivoRechazo(cliente);
            if (motivo != null) {
                return rechazo(CodigoError.CRE_001.mensaje(motivo));
            }

            BigDecimal montoCredito = monto.setScale(2, RoundingMode.HALF_UP);
            BigDecimal montoMaximo = montoMaximo(codCliente);
            if (montoCredito.compareTo(montoMaximo) > 0) {
                return rechazo(CodigoError.CRE_002.mensaje("Monto máximo aprobado: $" + montoMaximo + "."));
            }

            // Generar tabla de amortización
            List<CuotaAmortizacion> amortizacion = CalculadoraAmortizacion.generarTabla(montoCredito, cuotas, CalculadoraAmortizacion.TASA_ANUAL_DEFAULT);
            BigDecimal cuotaFija = amortizacion.get(0).getValorCuota();

            Credito credito = new Credito(
                0,
                codCliente,
                montoCredito,
                cuotas,
                CalculadoraAmortizacion.TASA_ANUAL_DEFAULT,
                cuotaFija,
                LocalDate.now(),
                "ACTIVO"
            );

            int codCreditoGenerado = creditoDAO.registrarCreditoConAmortizacion(credito, amortizacion);

            return new RespuestaCredito(
                true,
                codCreditoGenerado,
                cuotaFija,
                "Crédito " + codCreditoGenerado + " otorgado: " + cuotas + " cuotas de $" + cuotaFija + ".",
                amortizacion
            );

        } catch (SQLException e) {
            throw ExcepcionNegocio.baseDatos(e);
        }
    }

    public List<CuotaAmortizacion> consultarTabla(int codCredito) {
        if (codCredito <= 0) {
            throw new ExcepcionNegocio(CodigoError.VAL_001, "El código de crédito debe ser mayor a cero.");
        }
        try {
            List<CuotaAmortizacion> tabla = creditoDAO.consultarTablaAmortizacion(codCredito);
            if (tabla.isEmpty()) {
                throw new ExcepcionNegocio(CodigoError.VAL_001, "El crédito " + codCredito + " no existe.");
            }
            return tabla;
        } catch (SQLException e) {
            throw ExcepcionNegocio.baseDatos(e);
        }
    }

    // RN-02 a RN-04: devuelve el motivo del rechazo, o null si el cliente es sujeto de crédito
    private String motivoRechazo(Cliente cliente) throws SQLException {
        int codCliente = cliente.getCodCliente();

        // RN-02: Tener al menos un depósito en el último mes
        if (movimientoDAO.contarDepositosUltimoMes(codCliente) <= 0) {
            return "no registra depósitos en los últimos 30 días.";
        }

        // RN-03: Si es hombre, edad >= 25 años
        if ("M".equalsIgnoreCase(cliente.getGenero())
                && Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears() < 25) {
            return "los clientes de género masculino deben tener al menos 25 años.";
        }

        // RN-04: No tener crédito actualmente activo
        if (creditoDAO.tieneCreditoActivo(codCliente)) {
            return "ya mantiene un crédito activo en BanQuito.";
        }
        return null;
    }

    // RN-05: ((promDep - promRet de los últimos 3 meses) * 0.30) * 6; si el resultado es negativo, el monto es 0
    private BigDecimal montoMaximo(int codCliente) throws SQLException {
        BigDecimal promDep = movimientoDAO.calcularPromedioUltimos3Meses(codCliente, "DEP");
        BigDecimal promRet = movimientoDAO.calcularPromedioUltimos3Meses(codCliente, "RET");

        BigDecimal diferencia = promDep.subtract(promRet);
        if (diferencia.signum() <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return diferencia.multiply(PORCENTAJE).multiply(MESES).setScale(2, RoundingMode.HALF_UP);
    }

    private RespuestaCredito rechazo(String mensaje) {
        return new RespuestaCredito(false, 0, BigDecimal.ZERO, mensaje, Collections.emptyList());
    }
}
