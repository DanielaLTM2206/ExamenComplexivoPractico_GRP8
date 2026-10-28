package ec.edu.monster.proxy;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para listarCelulares complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="listarCelulares">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="soloActivos" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listarCelulares", propOrder = {
    "soloActivos"
})
public class ListarCelulares {

    protected boolean soloActivos;

    /**
     * Obtiene el valor de la propiedad soloActivos.
     * 
     */
    public boolean isSoloActivos() {
        return soloActivos;
    }

    /**
     * Define el valor de la propiedad soloActivos.
     * 
     */
    public void setSoloActivos(boolean value) {
        this.soloActivos = value;
    }

}
