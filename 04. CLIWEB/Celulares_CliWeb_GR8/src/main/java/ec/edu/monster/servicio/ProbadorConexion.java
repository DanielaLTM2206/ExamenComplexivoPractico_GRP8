package ec.edu.monster.servicio;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

// Comprueba que en una dirección responda WSComercializadora pidiendo su WSDL antes de guardarla.
// Distingue un servidor apagado o bloqueado por el firewall de una ruta equivocada.
public class ProbadorConexion {

    private static final int TIEMPO_ESPERA_MS = 6000;

    public record Resultado(boolean exitosa, String mensaje) {
    }

    public Resultado probar(String urlServicio) {
        try {
            HttpURLConnection conexion = (HttpURLConnection) URI.create(urlServicio + "?wsdl").toURL().openConnection();
            conexion.setConnectTimeout(TIEMPO_ESPERA_MS);
            conexion.setReadTimeout(TIEMPO_ESPERA_MS);
            int codigo = conexion.getResponseCode();
            if (codigo != HttpURLConnection.HTTP_OK) {
                return new Resultado(false, "El equipo en " + urlServicio + " respondió con el código " + codigo
                    + ". Revise que la dirección termine en /WS_Comercializadora_SOAP/WSComercializadora y que el servicio esté desplegado en Payara.");
            }
            String contenido;
            try (InputStream entrada = conexion.getInputStream()) {
                contenido = new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
            }
            if (!contenido.contains("WSComercializadora")) {
                return new Resultado(false, "La dirección " + urlServicio + " responde, pero no es el servicio SOAP WSComercializadora.");
            }
            return new Resultado(true, "Conexión correcta con la comercializadora en " + urlServicio + ".");
        } catch (SocketTimeoutException e) {
            return new Resultado(false, "El servidor en " + urlServicio + " no respondió en " + TIEMPO_ESPERA_MS / 1000
                + " segundos. Verifique la IP, que ambas computadoras estén en la misma red y que el firewall permita el puerto 8080"
                + " (ejecute PREPARAR_SERVIDOR_SOAP_8080.bat en la PC servidor).");
        } catch (IOException | IllegalArgumentException e) {
            return new Resultado(false, "No fue posible conectar con " + urlServicio
                + ". Verifique que Payara esté encendido, la IP y el puerto, y el firewall de la PC servidor.");
        }
    }
}
