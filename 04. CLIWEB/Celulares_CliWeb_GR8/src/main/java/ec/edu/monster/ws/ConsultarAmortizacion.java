package ec.edu.monster.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultarAmortizacion complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="consultarAmortizacion">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="numFactura" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarAmortizacion", propOrder = {
    "numFactura"
})
public class ConsultarAmortizacion {

    protected int numFactura;

    /**
     * Obtiene el valor de la propiedad numFactura.
     * 
     */
    public int getNumFactura() {
        return numFactura;
    }

    /**
     * Define el valor de la propiedad numFactura.
     * 
     */
    public void setNumFactura(int value) {
        this.numFactura = value;
    }

}
