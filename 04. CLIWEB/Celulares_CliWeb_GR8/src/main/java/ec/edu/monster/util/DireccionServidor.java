package ec.edu.monster.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Convierte lo que escribe el usuario en la dirección completa de WSComercializadora:
// 192.168.1.25 → http://192.168.1.25:8080/WS_Comercializadora_SOAP/WSComercializadora
public final class DireccionServidor {

    public static final int PUERTO_POR_DEFECTO = 8080;
    public static final String RUTA_SERVICIO = "/WS_Comercializadora_SOAP/WSComercializadora";
    public static final String URL_LOCAL = "http://localhost:" + PUERTO_POR_DEFECTO + RUTA_SERVICIO;

    private static final String MENSAJE_FORMATO =
        "Use la IP de la computadora servidor (por ejemplo 192.168.1.25) "
        + "o la dirección completa (http://192.168.1.25:8080/WS_Comercializadora_SOAP/WSComercializadora).";

    private static final Pattern NUMERICO = Pattern.compile("^[0-9.]+$");
    private static final Pattern IPV4 = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
    private static final Pattern HOST = Pattern.compile("^[A-Za-z0-9]([A-Za-z0-9.-]*[A-Za-z0-9])?$");

    private DireccionServidor() {
    }

    /**
     * Con solo la IP o el nombre del equipo se completan el puerto 8080 y la ruta del servicio.
     * Una dirección que ya trae ruta se respeta; se quita el "?wsdl" si la copiaron del navegador.
     *
     * @throws IllegalArgumentException si la dirección no es válida (mensaje listo para el usuario)
     */
    public static String completar(String entrada) {
        String texto = entrada == null ? "" : entrada.trim();
        if (texto.toLowerCase().endsWith("?wsdl")) {
            texto = texto.substring(0, texto.length() - 5);
        }
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Escriba la dirección del servidor. " + MENSAJE_FORMATO);
        }
        if (!texto.contains("://")) {
            texto = "http://" + texto;
        }
        try {
            URI uri = new URI(texto);
            String esquema = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase();
            String host = uri.getHost();
            if (!(esquema.equals("http") || esquema.equals("https")) || host == null || !hostValido(host)
                    || uri.getQuery() != null || uri.getFragment() != null) {
                throw new IllegalArgumentException("La dirección del servidor no es válida. " + MENSAJE_FORMATO);
            }
            String ruta = uri.getPath() == null || uri.getPath().isEmpty() || uri.getPath().equals("/")
                ? RUTA_SERVICIO : uri.getPath();
            int puerto = uri.getPort() == -1 && ruta.equals(RUTA_SERVICIO) && uri.getPath().length() <= 1
                ? PUERTO_POR_DEFECTO : uri.getPort();
            String resultado = new URI(esquema, null, host, puerto, ruta, null, null).toString();
            return resultado.endsWith("/") ? resultado.substring(0, resultado.length() - 1) : resultado;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("La dirección del servidor no es válida. " + MENSAJE_FORMATO);
        }
    }

    // Se exigen los cuatro números de 0 a 255 cuando el host es una IP
    private static boolean hostValido(String host) {
        if (!NUMERICO.matcher(host).matches()) {
            return HOST.matcher(host).matches();
        }
        Matcher m = IPV4.matcher(host);
        if (!m.matches()) {
            return false;
        }
        for (int i = 1; i <= 4; i++) {
            if (Integer.parseInt(m.group(i)) > 255) {
                return false;
            }
        }
        return true;
    }
}
