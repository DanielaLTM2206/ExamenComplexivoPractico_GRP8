package ec.edu.monster.proxy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para respuestaCredito complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="respuestaCredito">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="amortizacion" type="{http://ws.monster.edu.ec/}cuotaAmortizacion" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="codCredito" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="exito" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="mensaje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="valorCuota" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaCredito", propOrder = {
    "amortizacion",
    "codCredito",
    "exito",
    "mensaje",
    "valorCuota"
})
public class RespuestaCredito {

    @XmlElement(nillable = true)
    protected List<CuotaAmortizacion> amortizacion;
    protected int codCredito;
    protected boolean exito;
    protected String mensaje;
    protected BigDecimal valorCuota;

    /**
     * Gets the value of the amortizacion property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore, any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a {@code set} method for the amortizacion property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getAmortizacion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CuotaAmortizacion }
     * </p>
     * 
     * 
     * @return
     *     The value of the amortizacion property.
     */
    public List<CuotaAmortizacion> getAmortizacion() {
        if (amortizacion == null) {
            amortizacion = new ArrayList<>();
        }
        return this.amortizacion;
    }

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

    /**
     * Obtiene el valor de la propiedad exito.
     * 
     */
    public boolean isExito() {
        return exito;
    }

    /**
     * Define el valor de la propiedad exito.
     * 
     */
    public void setExito(boolean value) {
        this.exito = value;
    }

    /**
     * Obtiene el valor de la propiedad mensaje.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Define el valor de la propiedad mensaje.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensaje(String value) {
        this.mensaje = value;
    }

    /**
     * Obtiene el valor de la propiedad valorCuota.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorCuota() {
        return valorCuota;
    }

    /**
     * Define el valor de la propiedad valorCuota.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorCuota(BigDecimal value) {
        this.valorCuota = value;
    }

}
