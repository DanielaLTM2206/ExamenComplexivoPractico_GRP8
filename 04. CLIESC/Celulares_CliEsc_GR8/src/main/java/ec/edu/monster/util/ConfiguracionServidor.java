package ec.edu.monster.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

// Dirección de WSComercializadora que usa el cliente de escritorio. La IP de la PC servidor cambia según la red,
// por eso no está fija en el código. Se toma, en este orden:
//   1. la variable de entorno CELULARES_SERVIDOR (por ejemplo 192.168.1.25),
//   2. el archivo .celulares_gr8\clientes-servidor.properties de la carpeta del usuario (lo escribe "Configurar servidor"),
//   3. localhost, cuando Payara corre en la misma PC.
public final class ConfiguracionServidor {

    public static final String VARIABLE_ENTORNO = "CELULARES_SERVIDOR";
    private static final Path ARCHIVO = Paths.get(System.getProperty("user.home"), ".celulares_gr8", "clientes-servidor.properties");
    private static final String CLAVE = "url.servicio";

    private ConfiguracionServidor() {
    }

    public static String getUrlActual() {
        String variable = System.getenv(VARIABLE_ENTORNO);
        if (variable != null && !variable.isBlank()) {
            try {
                return DireccionServidor.completar(variable);
            } catch (IllegalArgumentException e) {
                // Variable mal escrita: se sigue con el archivo o localhost
            }
        }
        if (Files.isRegularFile(ARCHIVO)) {
            Properties p = new Properties();
            try (InputStream entrada = Files.newInputStream(ARCHIVO)) {
                p.load(entrada);
                return DireccionServidor.completar(p.getProperty(CLAVE));
            } catch (IOException | IllegalArgumentException e) {
                // Archivo dañado: se usa localhost
            }
        }
        return DireccionServidor.URL_LOCAL;
    }

    /** Indica de dónde salió la dirección actual, para mostrarlo en la barra de estado. */
    public static String getOrigen() {
        String variable = System.getenv(VARIABLE_ENTORNO);
        if (variable != null && !variable.isBlank()) {
            return "variable " + VARIABLE_ENTORNO;
        }
        return Files.isRegularFile(ARCHIVO) ? "configurado" : "local por defecto";
    }

    public static boolean usaVariableEntorno() {
        String variable = System.getenv(VARIABLE_ENTORNO);
        return variable != null && !variable.isBlank();
    }

    public static void guardar(String urlCompleta) throws IOException {
        Properties p = new Properties();
        p.setProperty(CLAVE, urlCompleta);
        Files.createDirectories(ARCHIVO.getParent());
        try (OutputStream salida = Files.newOutputStream(ARCHIVO)) {
            p.store(salida, "Servidor SOAP de los clientes consola y escritorio (Grupo 08)");
        }
    }

    public static void restablecer() throws IOException {
        Files.deleteIfExists(ARCHIVO);
    }
}
