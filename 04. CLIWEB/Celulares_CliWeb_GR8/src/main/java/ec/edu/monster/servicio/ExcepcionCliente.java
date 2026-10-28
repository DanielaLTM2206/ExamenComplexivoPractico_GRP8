package ec.edu.monster.servicio;

// Error que se muestra al usuario: conserva el código del ERS (VAL-001, CRE-001, ...) que envía el servidor
public class ExcepcionCliente extends Exception {

    private final String codigo;

    public ExcepcionCliente(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public String getCodigo() { return codigo; }

    // Título del diálogo según la familia del código
    public String getTitulo() {
        if (codigo == null) {
            return "Error";
        }
        if (codigo.startsWith("VAL")) return "Datos no válidos";
        if (codigo.startsWith("CLI")) return "Cliente no encontrado";
        if (codigo.startsWith("CRE")) return "Crédito no aprobado";
        return "Error del sistema";
    }
}
