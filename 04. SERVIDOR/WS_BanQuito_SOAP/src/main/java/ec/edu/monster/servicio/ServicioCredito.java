package ec.edu.monster.servicio;

import ec.edu.monster.dao.ClienteDAO;
import ec.edu.monster.dao.CreditoDAO;
import ec.edu.monster.dao.MovimientoDAO;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.Credito;
import ec.edu.monster.modelo.CuotaAmortizacion;
import ec.edu.monster.modelo.RespuestaCredito;
import ec.edu.monster.modelo.RespuestaSujetoCredito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;

public class ServicioCredito {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final MovimientoDAO movimientoDAO = new MovimientoDAO();
    private final CreditoDAO creditoDAO = new CreditoDAO();

    public RespuestaSujetoCredito verificarSujeto(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return new RespuestaSujetoCredito(false, null, "La cédula proporcionada es inválida o vacía.");
        }

        try {
            // RN-01: Ser cliente del banco
            Cliente cliente = clienteDAO.buscarPorCedula(cedula.trim());
            if (cliente == null) {
                return new RespuestaSujetoCredito(false, null, "CLI-001: El cliente no se encuentra registrado en BanQuito.");
            }

            int codCliente = cliente.getCodCliente();

            // RN-02: Tener al menos un depósito en el último mes
            int depositosMes = movimientoDAO.contarDepositosUltimoMes(codCliente);
            if (depositosMes <= 0) {
                return new RespuestaSujetoCredito(false, codCliente, "RN-02: El cliente no registra depósitos en los últimos 30 días.");
            }

            // RN-03: Si es hombre, edad >= 25 años
            if ("M".equalsIgnoreCase(cliente.getGenero()) && cliente.getFechaNacimiento() != null) {
                int edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears();
                if (edad < 25) {
                    return new RespuestaSujetoCredito(false, codCliente, "RN-03: Los clientes de género masculino deben tener al menos 25 años.");
                }
            }

            // RN-04: No tener crédito actualmente activo
            boolean tieneActivo = creditoDAO.tieneCreditoActivo(codCliente);
            if (tieneActivo) {
                return new RespuestaSujetoCredito(false, codCliente, "RN-04: El cliente ya mantiene un crédito activo en BanQuito.");
            }

            // Cumple todas las condiciones
            return new RespuestaSujetoCredito(true, codCliente, "El cliente es sujeto de crédito calificado.");

        } catch (SQLException e) {
            return new RespuestaSujetoCredito(false, null, "Error en base de datos: " + e.getMessage());
        }
    }

    public BigDecimal calcularMontoMaximo(int codCliente) {
        try {
            BigDecimal promDep = movimientoDAO.calcularPromedioUltimos3Meses(codCliente, "DEP");
            BigDecimal promRet = movimientoDAO.calcularPromedioUltimos3Meses(codCliente, "RET");

            BigDecimal diferencia = promDep.subtract(promRet);
            if (diferencia.compareTo(BigDecimal.ZERO) <= 0) {
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }

            // Fórmula: ((promDep - promRet) * 0.30) * 6
            BigDecimal porcentaje = new BigDecimal("0.30");
            BigDecimal multiplicador = new BigDecimal("6");
            return diferencia.multiply(porcentaje).multiply(multiplicador).setScale(2, RoundingMode.HALF_UP);

        } catch (SQLException e) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public RespuestaCredito otorgarCredito(int codCliente, BigDecimal monto, int cuotas) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return new RespuestaCredito(false, 0, BigDecimal.ZERO, "El monto del crédito debe ser mayor a cero.", Collections.emptyList());
        }
        if (cuotas < 3 || cuotas > 18) {
            return new RespuestaCredito(false, 0, BigDecimal.ZERO, "El plazo del crédito debe estar entre 3 y 18 meses.", Collections.emptyList());
        }

        try {
            Cliente cliente = clienteDAO.buscarPorId(codCliente);
            if (cliente == null) {
                return new RespuestaCredito(false, 0, BigDecimal.ZERO, "Cliente no encontrado.", Collections.emptyList());
            }

            BigDecimal montoMaximo = calcularMontoMaximo(codCliente);
            if (monto.compareTo(montoMaximo) > 0) {
                return new RespuestaCredito(false, 0, BigDecimal.ZERO,
                    "El monto solicitado ($" + monto + ") supera el monto máximo permitido ($" + montoMaximo + ").",
                    Collections.emptyList());
            }

            // Generar tabla de amortización
            List<CuotaAmortizacion> amortizacion = CalculadoraAmortizacion.generarTabla(monto, cuotas, CalculadoraAmortizacion.TASA_ANUAL_DEFAULT);
            BigDecimal cuotaFija = amortizacion.get(0).getValorCuota();

            Credito credito = new Credito(
                0,
                codCliente,
                monto,
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
                "Crédito otorgado exitosamente con código " + codCreditoGenerado,
                amortizacion
            );

        } catch (SQLException e) {
            return new RespuestaCredito(false, 0, BigDecimal.ZERO, "Error al registrar crédito: " + e.getMessage(), Collections.emptyList());
        }
    }

    public List<CuotaAmortizacion> consultarTabla(int codCredito) {
        try {
            return creditoDAO.consultarTablaAmortizacion(codCredito);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
