package ec.edu.monster.proxy;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultarTablaAmortizacion complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="consultarTablaAmortizacion">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="codCredito" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarTablaAmortizacion", propOrder = {
    "codCredito"
})
public class ConsultarTablaAmortizacion {

    protected int codCredito;

    /**
     * Obtiene el valor de la propiedad codCredito.
     * 
     */
    public int getCodCredito() {
        return codCredito;
    }

    /**
     * Define el valor de la propiedad codCredito.
     * 
     */
    public void setCodCredito(int value) {
        this.codCredito = value;
    }

}
