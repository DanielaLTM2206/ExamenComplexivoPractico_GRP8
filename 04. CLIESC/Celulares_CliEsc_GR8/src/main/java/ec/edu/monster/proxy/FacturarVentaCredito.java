package ec.edu.monster.proxy;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para facturarVentaCredito complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="facturarVentaCredito">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="cedula" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="items" type="{http://ws.monster.edu.ec/}itemVenta" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "facturarVentaCredito", propOrder = {
    "cedula",
    "nombre",
    "items",
    "numeroCuotas"
})
public class FacturarVentaCredito {

    protected String cedula;
    protected String nombre;
    protected List<ItemVenta> items;
    protected int numeroCuotas;

    /**
     * Obtiene el valor de la propiedad cedula.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * Define el valor de la propiedad cedula.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCedula(String value) {
        this.cedula = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore, any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a {@code set} method for the items property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItemVenta }
     * </p>
     * 
     * 
     * @return
     *     The value of the items property.
     */
    public List<ItemVenta> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return this.items;
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
