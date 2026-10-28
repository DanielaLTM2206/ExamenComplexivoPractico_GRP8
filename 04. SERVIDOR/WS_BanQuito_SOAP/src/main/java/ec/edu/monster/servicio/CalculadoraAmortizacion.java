package ec.edu.monster.servicio;

import ec.edu.monster.modelo.CuotaAmortizacion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraAmortizacion {

    public static final BigDecimal TASA_ANUAL_DEFAULT = new BigDecimal("16.50");

    public static List<CuotaAmortizacion> generarTabla(BigDecimal monto, int cuotas, BigDecimal tasaAnual) {
        if (tasaAnual == null) {
            tasaAnual = TASA_ANUAL_DEFAULT;
        }

        List<CuotaAmortizacion> tabla = new ArrayList<>();
        BigDecimal tasaMensual = tasaAnual.divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)
                                          .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP);
        double iDbl = tasaMensual.doubleValue();
        double factor = iDbl / (1.0 - Math.pow(1.0 + iDbl, -cuotas));
        BigDecimal cuotaFija = monto.multiply(BigDecimal.valueOf(factor)).setScale(2, RoundingMode.HALF_UP);

        BigDecimal saldo = monto.setScale(2, RoundingMode.HALF_UP);

        for (int k = 1; k <= cuotas; k++) {
            BigDecimal interes = saldo.multiply(tasaMensual).setScale(2, RoundingMode.HALF_UP);
            BigDecimal capital;
            BigDecimal cuotaReal;

            if (k == cuotas) {
                // Última cuota: ajuste exacto a centavos para cerrar saldo en cero
                capital = saldo;
                cuotaReal = capital.add(interes);
                saldo = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            } else {
                capital = cuotaFija.subtract(interes).setScale(2, RoundingMode.HALF_UP);
                cuotaReal = cuotaFija;
                saldo = saldo.subtract(capital).setScale(2, RoundingMode.HALF_UP);
            }

            tabla.add(new CuotaAmortizacion(k, cuotaReal, interes, capital, saldo));
        }

        return tabla;
    }
}
