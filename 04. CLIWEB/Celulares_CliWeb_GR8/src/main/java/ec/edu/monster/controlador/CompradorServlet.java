package ec.edu.monster.controlador;

import ec.edu.monster.servicio.ExcepcionCliente;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Autocompletar el nombre en "Facturar": el formulario lo pide al escribir una cédula válida.
// Responde JSON {"encontrado": true|false, "nombre": "...", "mensaje": "..."}; el nombre lo da BanQuito vía la comercializadora.
@WebServlet(name = "CompradorServlet", urlPatterns = {"/comprador"})
public class CompradorServlet extends ControladorBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-store");
        String json;
        try {
            String nombre = servicio.buscarComprador(req.getParameter("cedula"));
            json = nombre == null || nombre.isBlank()
                ? "{\"encontrado\":false,\"mensaje\":\"No es cliente de BanQuito: escriba el nombre (el crédito directo será rechazado).\"}"
                : "{\"encontrado\":true,\"nombre\":\"" + escapar(nombre) + "\"}";
        } catch (ExcepcionCliente e) {
            json = "{\"encontrado\":false,\"mensaje\":\"" + escapar(e.getMessage()) + "\"}";
        }
        resp.getWriter().write(json);
    }

    private static String escapar(String texto) {
        StringBuilder sb = new StringBuilder();
        for (char c : texto.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '<' -> sb.append("\\u003c");
                default -> sb.append(c < 0x20 ? " " : String.valueOf(c));
            }
        }
        return sb.toString();
    }
}
