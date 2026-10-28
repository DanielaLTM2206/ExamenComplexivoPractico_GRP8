package ec.edu.monster.proxy;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ec.edu.monster.proxy package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _ConsultarTablaAmortizacion_QNAME = new QName("http://ws.monster.edu.ec/", "consultarTablaAmortizacion");
    private static final QName _ConsultarTablaAmortizacionResponse_QNAME = new QName("http://ws.monster.edu.ec/", "consultarTablaAmortizacionResponse");
    private static final QName _ObtenerMontoMaximo_QNAME = new QName("http://ws.monster.edu.ec/", "obtenerMontoMaximo");
    private static final QName _ObtenerMontoMaximoResponse_QNAME = new QName("http://ws.monster.edu.ec/", "obtenerMontoMaximoResponse");
    private static final QName _OtorgarCredito_QNAME = new QName("http://ws.monster.edu.ec/", "otorgarCredito");
    private static final QName _OtorgarCreditoResponse_QNAME = new QName("http://ws.monster.edu.ec/", "otorgarCreditoResponse");
    private static final QName _VerificarSujetoCredito_QNAME = new QName("http://ws.monster.edu.ec/", "verificarSujetoCredito");
    private static final QName _VerificarSujetoCreditoResponse_QNAME = new QName("http://ws.monster.edu.ec/", "verificarSujetoCreditoResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ec.edu.monster.proxy
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConsultarTablaAmortizacion }
     * 
     * @return
     *     the new instance of {@link ConsultarTablaAmortizacion }
     */
    public ConsultarTablaAmortizacion createConsultarTablaAmortizacion() {
        return new ConsultarTablaAmortizacion();
    }

    /**
     * Create an instance of {@link ConsultarTablaAmortizacionResponse }
     * 
     * @return
     *     the new instance of {@link ConsultarTablaAmortizacionResponse }
     */
    public ConsultarTablaAmortizacionResponse createConsultarTablaAmortizacionResponse() {
        return new ConsultarTablaAmortizacionResponse();
    }

    /**
     * Create an instance of {@link ObtenerMontoMaximo }
     * 
     * @return
     *     the new instance of {@link ObtenerMontoMaximo }
     */
    public ObtenerMontoMaximo createObtenerMontoMaximo() {
        return new ObtenerMontoMaximo();
    }

    /**
     * Create an instance of {@link ObtenerMontoMaximoResponse }
     * 
     * @return
     *     the new instance of {@link ObtenerMontoMaximoResponse }
     */
    public ObtenerMontoMaximoResponse createObtenerMontoMaximoResponse() {
        return new ObtenerMontoMaximoResponse();
    }

    /**
     * Create an instance of {@link OtorgarCredito }
     * 
     * @return
     *     the new instance of {@link OtorgarCredito }
     */
    public OtorgarCredito createOtorgarCredito() {
        return new OtorgarCredito();
    }

    /**
     * Create an instance of {@link OtorgarCreditoResponse }
     * 
     * @return
     *     the new instance of {@link OtorgarCreditoResponse }
     */
    public OtorgarCreditoResponse createOtorgarCreditoResponse() {
        return new OtorgarCreditoResponse();
    }

    /**
     * Create an instance of {@link VerificarSujetoCredito }
     * 
     * @return
     *     the new instance of {@link VerificarSujetoCredito }
     */
    public VerificarSujetoCredito createVerificarSujetoCredito() {
        return new VerificarSujetoCredito();
    }

    /**
     * Create an instance of {@link VerificarSujetoCreditoResponse }
     * 
     * @return
     *     the new instance of {@link VerificarSujetoCreditoResponse }
     */
    public VerificarSujetoCreditoResponse createVerificarSujetoCreditoResponse() {
        return new VerificarSujetoCreditoResponse();
    }

    /**
     * Create an instance of {@link CuotaAmortizacion }
     * 
     * @return
     *     the new instance of {@link CuotaAmortizacion }
     */
    public CuotaAmortizacion createCuotaAmortizacion() {
        return new CuotaAmortizacion();
    }

    /**
     * Create an instance of {@link RespuestaSujetoCredito }
     * 
     * @return
     *     the new instance of {@link RespuestaSujetoCredito }
     */
    public RespuestaSujetoCredito createRespuestaSujetoCredito() {
        return new RespuestaSujetoCredito();
    }

    /**
     * Create an instance of {@link RespuestaCredito }
     * 
     * @return
     *     the new instance of {@link RespuestaCredito }
     */
    public RespuestaCredito createRespuestaCredito() {
        return new RespuestaCredito();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarTablaAmortizacion }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarTablaAmortizacion }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarTablaAmortizacion")
    public JAXBElement<ConsultarTablaAmortizacion> createConsultarTablaAmortizacion(ConsultarTablaAmortizacion value) {
        return new JAXBElement<>(_ConsultarTablaAmortizacion_QNAME, ConsultarTablaAmortizacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarTablaAmortizacionResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarTablaAmortizacionResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarTablaAmortizacionResponse")
    public JAXBElement<ConsultarTablaAmortizacionResponse> createConsultarTablaAmortizacionResponse(ConsultarTablaAmortizacionResponse value) {
        return new JAXBElement<>(_ConsultarTablaAmortizacionResponse_QNAME, ConsultarTablaAmortizacionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerMontoMaximo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ObtenerMontoMaximo }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "obtenerMontoMaximo")
    public JAXBElement<ObtenerMontoMaximo> createObtenerMontoMaximo(ObtenerMontoMaximo value) {
        return new JAXBElement<>(_ObtenerMontoMaximo_QNAME, ObtenerMontoMaximo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerMontoMaximoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ObtenerMontoMaximoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "obtenerMontoMaximoResponse")
    public JAXBElement<ObtenerMontoMaximoResponse> createObtenerMontoMaximoResponse(ObtenerMontoMaximoResponse value) {
        return new JAXBElement<>(_ObtenerMontoMaximoResponse_QNAME, ObtenerMontoMaximoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OtorgarCredito }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OtorgarCredito }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "otorgarCredito")
    public JAXBElement<OtorgarCredito> createOtorgarCredito(OtorgarCredito value) {
        return new JAXBElement<>(_OtorgarCredito_QNAME, OtorgarCredito.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OtorgarCreditoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OtorgarCreditoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "otorgarCreditoResponse")
    public JAXBElement<OtorgarCreditoResponse> createOtorgarCreditoResponse(OtorgarCreditoResponse value) {
        return new JAXBElement<>(_OtorgarCreditoResponse_QNAME, OtorgarCreditoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerificarSujetoCredito }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link VerificarSujetoCredito }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "verificarSujetoCredito")
    public JAXBElement<VerificarSujetoCredito> createVerificarSujetoCredito(VerificarSujetoCredito value) {
        return new JAXBElement<>(_VerificarSujetoCredito_QNAME, VerificarSujetoCredito.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerificarSujetoCreditoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link VerificarSujetoCreditoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "verificarSujetoCreditoResponse")
    public JAXBElement<VerificarSujetoCreditoResponse> createVerificarSujetoCreditoResponse(VerificarSujetoCreditoResponse value) {
        return new JAXBElement<>(_VerificarSujetoCreditoResponse_QNAME, VerificarSujetoCreditoResponse.class, null, value);
    }

}
