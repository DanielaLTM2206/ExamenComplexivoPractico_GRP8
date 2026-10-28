package ec.edu.monster.util;

import ec.edu.monster.servicio.ExcepcionCliente;

import java.math.BigDecimal;

// Validación previa en el cliente: evita llamar al servicio con datos que ya se sabe que son inválidos
public final class ValidadorEntrada {

    private ValidadorEntrada() {}

    public static String texto(String valor, String campo, int maximo) throws ExcepcionCliente {
        if (valor == null || valor.isBlank()) {
            throw invalido("El campo " + campo + " es obligatorio.");
        }
        String limpio = valor.trim();
        if (limpio.length() > maximo) {
            throw invalido("El campo " + campo + " admite máximo " + maximo + " caracteres.");
        }
        return limpio;
    }

    public static int entero(String valor, String campo, int minimo, int maximo) throws ExcepcionCliente {
        try {
            int numero = Integer.parseInt(valor == null ? "" : valor.trim());
            if (numero < minimo || numero > maximo) {
                throw invalido("El campo " + campo + " debe estar entre " + minimo + " y " + maximo + ".");
            }
            return numero;
        } catch (NumberFormatException e) {
            throw invalido("El campo " + campo + " debe ser un número entero.");
        }
    }

    public static BigDecimal decimalPositivo(String valor, String campo) throws ExcepcionCliente {
        try {
            BigDecimal numero = new BigDecimal(valor == null ? "" : valor.trim().replace(',', '.'));
            if (numero.signum() <= 0 || numero.scale() > 2) {
                throw invalido("El campo " + campo + " debe ser mayor a cero y tener máximo dos decimales.");
            }
            return numero;
        } catch (NumberFormatException e) {
            throw invalido("El campo " + campo + " debe ser un número, por ejemplo 299.99.");
        }
    }

    // Cédula ecuatoriana: 10 dígitos, provincia 01-24 o 30, tercer dígito menor a 6 y dígito verificador módulo 10
    public static String cedula(String valor) throws ExcepcionCliente {
        String ced = valor == null ? "" : valor.trim();
        if (!ced.matches("\\d{10}")) {
            throw invalido("La cédula debe tener exactamente 10 dígitos.");
        }
        int provincia = Integer.parseInt(ced.substring(0, 2));
        boolean valida = ((provincia >= 1 && provincia <= 24) || provincia == 30) && ced.charAt(2) - '0' < 6;
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int producto = (ced.charAt(i) - '0') * (i % 2 == 0 ? 2 : 1);
            suma += producto > 9 ? producto - 9 : producto;
        }
        if (!valida || (10 - suma % 10) % 10 != ced.charAt(9) - '0') {
            throw invalido("La cédula " + ced + " no es válida (dígito verificador incorrecto).");
        }
        return ced;
    }

    private static ExcepcionCliente invalido(String detalle) {
        return new ExcepcionCliente("VAL-001", "VAL-001: Los datos ingresados no son válidos. " + detalle);
    }
}
