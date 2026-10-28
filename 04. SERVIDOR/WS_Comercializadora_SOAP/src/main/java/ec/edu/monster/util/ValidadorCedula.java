package ec.edu.monster.util;

public final class ValidadorCedula {

    private ValidadorCedula() {}

    // Cédula ecuatoriana: 10 dígitos, provincia 01-24 o 30, tercer dígito menor a 6 y dígito verificador módulo 10
    public static boolean esValida(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) {
            return false;
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if ((provincia < 1 || provincia > 24) && provincia != 30) {
            return false;
        }
        if (cedula.charAt(2) - '0' >= 6) {
            return false;
        }
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int producto = (cedula.charAt(i) - '0') * (i % 2 == 0 ? 2 : 1);
            suma += producto > 9 ? producto - 9 : producto;
        }
        int verificador = (10 - suma % 10) % 10;
        return verificador == cedula.charAt(9) - '0';
    }
}
