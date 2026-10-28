package ec.edu.monster.proxy;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para detalleFactura complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="detalleFactura">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="cantidad" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="codCelular" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="marcaCelular" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="modeloCelular" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="numFactura" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="precioUnitario" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         <element name="subtotal" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleFactura", propOrder = {
    "cantidad",
    "codCelular",
    "marcaCelular",
    "modeloCelular",
    "numFactura",
    "precioUnitario",
    "subtotal"
})
public class DetalleFactura {

    protected int cantidad;
    protected int codCelular;
    protected String marcaCelular;
    protected String modeloCelular;
    protected int numFactura;
    protected BigDecimal precioUnitario;
    protected BigDecimal subtotal;

    /**
     * Obtiene el valor de la propiedad cantidad.
     * 
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Define el valor de la propiedad cantidad.
     * 
     */
    public void setCantidad(int value) {
        this.cantidad = value;
    }

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

    /**
     * Obtiene el valor de la propiedad marcaCelular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarcaCelular() {
        return marcaCelular;
    }

    /**
     * Define el valor de la propiedad marcaCelular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarcaCelular(String value) {
        this.marcaCelular = value;
    }

    /**
     * Obtiene el valor de la propiedad modeloCelular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModeloCelular() {
        return modeloCelular;
    }

    /**
     * Define el valor de la propiedad modeloCelular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModeloCelular(String value) {
        this.modeloCelular = value;
    }

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

    /**
     * Obtiene el valor de la propiedad precioUnitario.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Define el valor de la propiedad precioUnitario.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrecioUnitario(BigDecimal value) {
        this.precioUnitario = value;
    }

    /**
     * Obtiene el valor de la propiedad subtotal.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    /**
     * Define el valor de la propiedad subtotal.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSubtotal(BigDecimal value) {
        this.subtotal = value;
    }

}
