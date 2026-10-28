package ec.edu.monster.proxy;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para respuestaSujetoCredito complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="respuestaSujetoCredito">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="codCliente" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         <element name="esSujeto" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="motivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaSujetoCredito", propOrder = {
    "codCliente",
    "esSujeto",
    "motivo"
})
public class RespuestaSujetoCredito {

    protected Integer codCliente;
    protected boolean esSujeto;
    protected String motivo;

    /**
     * Obtiene el valor de la propiedad codCliente.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodCliente() {
        return codCliente;
    }

    /**
     * Define el valor de la propiedad codCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodCliente(Integer value) {
        this.codCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad esSujeto.
     * 
     */
    public boolean isEsSujeto() {
        return esSujeto;
    }

    /**
     * Define el valor de la propiedad esSujeto.
     * 
     */
    public void setEsSujeto(boolean value) {
        this.esSujeto = value;
    }

    /**
     * Obtiene el valor de la propiedad motivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Define el valor de la propiedad motivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivo(String value) {
        this.motivo = value;
    }

}
