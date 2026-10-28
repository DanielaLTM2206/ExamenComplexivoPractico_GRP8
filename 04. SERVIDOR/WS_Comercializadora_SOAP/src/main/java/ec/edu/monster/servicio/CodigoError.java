package ec.edu.monster.servicio;

// Catálogo de códigos de error definido en el ERS
public enum CodigoError {
    VAL_001("VAL-001", "Los datos ingresados no son válidos."),
    CLI_001("CLI-001", "La persona no es cliente del banco."),
    CRE_001("CRE-001", "El cliente no es sujeto de crédito:"),
    CRE_002("CRE-002", "El valor del teléfono supera el monto máximo de crédito."),
    CRE_003("CRE-003", "El plazo debe estar entre 3 y 18 meses."),
    BDD_001("BDD-001", "No fue posible completar la operación. Intente nuevamente.");

    private final String codigo;
    private final String texto;

    CodigoError(String codigo, String texto) {
        this.codigo = codigo;
        this.texto = texto;
    }

    public String getCodigo() { return codigo; }

    public String mensaje() { return codigo + ": " + texto; }

    public String mensaje(String detalle) { return mensaje() + " " + detalle; }
}
