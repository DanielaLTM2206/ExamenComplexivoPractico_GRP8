package ec.edu.monster.proxy;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para otorgarCredito complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="otorgarCredito">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="codCliente" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="precio" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         <element name="numeroCuotas" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "otorgarCredito", propOrder = {
    "codCliente",
    "precio",
    "numeroCuotas"
})
public class OtorgarCredito {

    protected int codCliente;
    protected BigDecimal precio;
    protected int numeroCuotas;

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

    /**
     * Obtiene el valor de la propiedad precio.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrecio() {
        return precio;
    }

    /**
     * Define el valor de la propiedad precio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrecio(BigDecimal value) {
        this.precio = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroCuotas.
     * 
     */
    public int getNumeroCuotas() {
        return numeroCuotas;
    }

    /**
     * Define el valor de la propiedad numeroCuotas.
     * 
     */
    public void setNumeroCuotas(int value) {
        this.numeroCuotas = value;
    }

}
