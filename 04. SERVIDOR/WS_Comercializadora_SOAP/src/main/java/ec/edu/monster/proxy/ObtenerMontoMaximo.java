package ec.edu.monster.proxy;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para obtenerMontoMaximo complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="obtenerMontoMaximo">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="codCliente" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerMontoMaximo", propOrder = {
    "codCliente"
})
public class ObtenerMontoMaximo {

    protected int codCliente;

    /**
     * Obtiene el valor de la propiedad codCliente.
     * 
     */
    public int getCodCliente() {
        return codCliente;
    }

    /**
     * Define el valor de la propiedad codCliente.
     * 
     */
    public void setCodCliente(int value) {
        this.codCliente = value;
    }

}
