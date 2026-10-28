package ec.edu.monster.servicio;

import ec.edu.monster.dao.CelularDAO;
import ec.edu.monster.modelo.Celular;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class ServicioCatalogo {

    // Límite de la columna DECIMAL(10,2)
    private static final BigDecimal PRECIO_MAXIMO = new BigDecimal("99999999.99");

    private final CelularDAO celularDAO = new CelularDAO();

    public List<Celular> listar(boolean soloActivos) throws ExcepcionComercializadora {
        try {
            return celularDAO.listarCelulares(soloActivos);
        } catch (SQLException e) {
            throw ExcepcionComercializadora.infraestructura(e);
        }
    }

    public Celular obtener(int codCelular) throws ExcepcionComercializadora {
        if (codCelular <= 0) {
            throw new ExcepcionComercializadora(CodigoError.VAL_001, "El código del celular debe ser mayor a cero.");
        }
        try {
            Celular celular = celularDAO.buscarPorId(codCelular);
            if (celular == null) {
                throw new ExcepcionComercializadora(CodigoError.VAL_001, "El celular " + codCelular + " no existe.");
            }
            return celular;
        } catch (SQLException e) {
            throw ExcepcionComercializadora.infraestructura(e);
        }
    }

    public Celular crear(String marca, String modelo, BigDecimal precio) throws ExcepcionComercializadora {
        validar(marca, modelo, precio);
        try {
            return celularDAO.crearCelular(marca.trim(), modelo.trim(), precio.setScale(2, RoundingMode.HALF_UP));
        } catch (SQLException e) {
            throw ExcepcionComercializadora.infraestructura(e);
        }
    }

    public Celular actualizar(int codCelular, String marca, String modelo, BigDecimal precio, boolean activo) throws ExcepcionComercializadora {
        validar(marca, modelo, precio);
        obtener(codCelular);
        try {
            celularDAO.actualizarCelular(codCelular, marca.trim(), modelo.trim(), precio.setScale(2, RoundingMode.HALF_UP), activo);
        } catch (SQLException e) {
            throw ExcepcionComercializadora.infraestructura(e);
        }
        return obtener(codCelular);
    }

    // Borrado físico si el celular no tiene ventas; si las tiene, borrado lógico (activo = 0)
    public boolean eliminar(int codCelular) throws ExcepcionComercializadora {
        obtener(codCelular);
        try {
            return celularDAO.eliminarCelular(codCelular);
        } catch (SQLException e) {
            throw ExcepcionComercializadora.infraestructura(e);
        }
    }

    private void validar(String marca, String modelo, BigDecimal precio) throws ExcepcionComercializadora {
        if (marca == null || marca.isBlank() || marca.trim().length() > 40) {
            throw new ExcepcionComercializadora(CodigoError.VAL_001, "La marca es obligatoria (máximo 40 caracteres).");
        }
        if (modelo == null || modelo.isBlank() || modelo.trim().length() > 60) {
            throw new ExcepcionComercializadora(CodigoError.VAL_001, "El modelo es obligatorio (máximo 60 caracteres).");
        }
        if (precio == null || precio.signum() <= 0 || precio.compareTo(PRECIO_MAXIMO) > 0) {
            throw new ExcepcionComercializadora(CodigoError.VAL_001, "El precio debe ser mayor a cero.");
        }
    }
}
