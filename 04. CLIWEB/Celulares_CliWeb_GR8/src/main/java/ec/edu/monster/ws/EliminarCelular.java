package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para eliminarCelular complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="eliminarCelular">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="codCelular" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eliminarCelular", propOrder = {
    "codCelular"
})
public class EliminarCelular {

    protected int codCelular;

    /**
     * Obtiene el valor de la propiedad codCelular.
     * 
     */
    public int getCodCelular() {
        return codCelular;
    }

    /**
     * Define el valor de la propiedad codCelular.
     * 
     */
    public void setCodCelular(int value) {
        this.codCelular = value;
    }

}
