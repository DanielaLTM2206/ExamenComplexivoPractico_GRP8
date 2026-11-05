package ec.edu.monster.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

// Dirección de WSComercializadora que usa el cliente web. Por defecto es localhost (Payara con los servidores
// en la misma PC); si la aplicación web se despliega en otra PC se cambia en la página "Servidor".
// Se guarda en la carpeta del usuario para que sobreviva a los redespliegues desde NetBeans.
public final class ConfiguracionServidor {

    private static final Logger LOG = Logger.getLogger(ConfiguracionServidor.class.getName());
    private static final Path ARCHIVO = Paths.get(System.getProperty("user.home"), ".celulares_gr8", "cliweb-servidor.properties");
    private static final String CLAVE = "url.servicio";

    private static String urlActual = leer();

    private ConfiguracionServidor() {
    }

    public static synchronized String getUrlActual() {
        return urlActual;
    }

    public static boolean esPorDefecto() {
        return DireccionServidor.URL_LOCAL.equals(getUrlActual());
    }

    public static String getUbicacionArchivo() {
        return ARCHIVO.toString();
    }

    /** Guarda la dirección (ya completada) para todas las sesiones del cliente web. */
    public static synchronized void guardar(String urlCompleta) throws IOException {
        Properties p = new Properties();
        p.setProperty(CLAVE, urlCompleta);
        Files.createDirectories(ARCHIVO.getParent());
        try (OutputStream salida = Files.newOutputStream(ARCHIVO)) {
            p.store(salida, "Servidor SOAP del cliente web Celulares_CliWeb_GR8");
        }
        urlActual = urlCompleta;
    }

    /** Vuelve a localhost borrando el archivo de configuración. */
    public static synchronized void restablecer() throws IOException {
        Files.deleteIfExists(ARCHIVO);
        urlActual = DireccionServidor.URL_LOCAL;
    }

    private static String leer() {
        if (Files.isRegularFile(ARCHIVO)) {
            Properties p = new Properties();
            try (InputStream entrada = Files.newInputStream(ARCHIVO)) {
                p.load(entrada);
                return DireccionServidor.completar(p.getProperty(CLAVE));
            } catch (IOException | IllegalArgumentException e) {
                LOG.log(Level.WARNING, "No se pudo leer " + ARCHIVO + "; se usa localhost", e);
            }
        }
        return DireccionServidor.URL_LOCAL;
    }
}
