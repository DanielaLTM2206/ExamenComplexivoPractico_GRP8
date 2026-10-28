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

    private static final QName _ExcepcionComercializadora_QNAME = new QName("http://ws.monster.edu.ec/", "ExcepcionComercializadora");
    private static final QName _ActualizarCelular_QNAME = new QName("http://ws.monster.edu.ec/", "actualizarCelular");
    private static final QName _ActualizarCelularResponse_QNAME = new QName("http://ws.monster.edu.ec/", "actualizarCelularResponse");
    private static final QName _ConsultarAmortizacion_QNAME = new QName("http://ws.monster.edu.ec/", "consultarAmortizacion");
    private static final QName _ConsultarAmortizacionResponse_QNAME = new QName("http://ws.monster.edu.ec/", "consultarAmortizacionResponse");
    private static final QName _CrearCelular_QNAME = new QName("http://ws.monster.edu.ec/", "crearCelular");
    private static final QName _CrearCelularResponse_QNAME = new QName("http://ws.monster.edu.ec/", "crearCelularResponse");
    private static final QName _EliminarCelular_QNAME = new QName("http://ws.monster.edu.ec/", "eliminarCelular");
    private static final QName _EliminarCelularResponse_QNAME = new QName("http://ws.monster.edu.ec/", "eliminarCelularResponse");
    private static final QName _FacturarCredito_QNAME = new QName("http://ws.monster.edu.ec/", "facturarCredito");
    private static final QName _FacturarCreditoResponse_QNAME = new QName("http://ws.monster.edu.ec/", "facturarCreditoResponse");
    private static final QName _FacturarEfectivo_QNAME = new QName("http://ws.monster.edu.ec/", "facturarEfectivo");
    private static final QName _FacturarEfectivoResponse_QNAME = new QName("http://ws.monster.edu.ec/", "facturarEfectivoResponse");
    private static final QName _ListarCelulares_QNAME = new QName("http://ws.monster.edu.ec/", "listarCelulares");
    private static final QName _ListarCelularesResponse_QNAME = new QName("http://ws.monster.edu.ec/", "listarCelularesResponse");
    private static final QName _ObtenerCelular_QNAME = new QName("http://ws.monster.edu.ec/", "obtenerCelular");
    private static final QName _ObtenerCelularResponse_QNAME = new QName("http://ws.monster.edu.ec/", "obtenerCelularResponse");
    private static final QName _ObtenerFactura_QNAME = new QName("http://ws.monster.edu.ec/", "obtenerFactura");
    private static final QName _ObtenerFacturaResponse_QNAME = new QName("http://ws.monster.edu.ec/", "obtenerFacturaResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ec.edu.monster.proxy
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExcepcionComercializadora }
     * 
     * @return
     *     the new instance of {@link ExcepcionComercializadora }
     */
    public ExcepcionComercializadora createExcepcionComercializadora() {
        return new ExcepcionComercializadora();
    }

    /**
     * Create an instance of {@link ActualizarCelular }
     * 
     * @return
     *     the new instance of {@link ActualizarCelular }
     */
    public ActualizarCelular createActualizarCelular() {
        return new ActualizarCelular();
    }

    /**
     * Create an instance of {@link ActualizarCelularResponse }
     * 
     * @return
     *     the new instance of {@link ActualizarCelularResponse }
     */
    public ActualizarCelularResponse createActualizarCelularResponse() {
        return new ActualizarCelularResponse();
    }

    /**
     * Create an instance of {@link ConsultarAmortizacion }
     * 
     * @return
     *     the new instance of {@link ConsultarAmortizacion }
     */
    public ConsultarAmortizacion createConsultarAmortizacion() {
        return new ConsultarAmortizacion();
    }

    /**
     * Create an instance of {@link ConsultarAmortizacionResponse }
     * 
     * @return
     *     the new instance of {@link ConsultarAmortizacionResponse }
     */
    public ConsultarAmortizacionResponse createConsultarAmortizacionResponse() {
        return new ConsultarAmortizacionResponse();
    }

    /**
     * Create an instance of {@link CrearCelular }
     * 
     * @return
     *     the new instance of {@link CrearCelular }
     */
    public CrearCelular createCrearCelular() {
        return new CrearCelular();
    }

    /**
     * Create an instance of {@link CrearCelularResponse }
     * 
     * @return
     *     the new instance of {@link CrearCelularResponse }
     */
    public CrearCelularResponse createCrearCelularResponse() {
        return new CrearCelularResponse();
    }

    /**
     * Create an instance of {@link EliminarCelular }
     * 
     * @return
     *     the new instance of {@link EliminarCelular }
     */
    public EliminarCelular createEliminarCelular() {
        return new EliminarCelular();
    }

    /**
     * Create an instance of {@link EliminarCelularResponse }
     * 
     * @return
     *     the new instance of {@link EliminarCelularResponse }
     */
    public EliminarCelularResponse createEliminarCelularResponse() {
        return new EliminarCelularResponse();
    }

    /**
     * Create an instance of {@link FacturarCredito }
     * 
     * @return
     *     the new instance of {@link FacturarCredito }
     */
    public FacturarCredito createFacturarCredito() {
        return new FacturarCredito();
    }

    /**
     * Create an instance of {@link FacturarCreditoResponse }
     * 
     * @return
     *     the new instance of {@link FacturarCreditoResponse }
     */
    public FacturarCreditoResponse createFacturarCreditoResponse() {
        return new FacturarCreditoResponse();
    }

    /**
     * Create an instance of {@link FacturarEfectivo }
     * 
     * @return
     *     the new instance of {@link FacturarEfectivo }
     */
    public FacturarEfectivo createFacturarEfectivo() {
        return new FacturarEfectivo();
    }

    /**
     * Create an instance of {@link FacturarEfectivoResponse }
     * 
     * @return
     *     the new instance of {@link FacturarEfectivoResponse }
     */
    public FacturarEfectivoResponse createFacturarEfectivoResponse() {
        return new FacturarEfectivoResponse();
    }

    /**
     * Create an instance of {@link ListarCelulares }
     * 
     * @return
     *     the new instance of {@link ListarCelulares }
     */
    public ListarCelulares createListarCelulares() {
        return new ListarCelulares();
    }

    /**
     * Create an instance of {@link ListarCelularesResponse }
     * 
     * @return
     *     the new instance of {@link ListarCelularesResponse }
     */
    public ListarCelularesResponse createListarCelularesResponse() {
        return new ListarCelularesResponse();
    }

    /**
     * Create an instance of {@link ObtenerCelular }
     * 
     * @return
     *     the new instance of {@link ObtenerCelular }
     */
    public ObtenerCelular createObtenerCelular() {
        return new ObtenerCelular();
    }

    /**
     * Create an instance of {@link ObtenerCelularResponse }
     * 
     * @return
     *     the new instance of {@link ObtenerCelularResponse }
     */
    public ObtenerCelularResponse createObtenerCelularResponse() {
        return new ObtenerCelularResponse();
    }

    /**
     * Create an instance of {@link ObtenerFactura }
     * 
     * @return
     *     the new instance of {@link ObtenerFactura }
     */
    public ObtenerFactura createObtenerFactura() {
        return new ObtenerFactura();
    }

    /**
     * Create an instance of {@link ObtenerFacturaResponse }
     * 
     * @return
     *     the new instance of {@link ObtenerFacturaResponse }
     */
    public ObtenerFacturaResponse createObtenerFacturaResponse() {
        return new ObtenerFacturaResponse();
    }

    /**
     * Create an instance of {@link Celular }
     * 
     * @return
     *     the new instance of {@link Celular }
     */
    public Celular createCelular() {
        return new Celular();
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
     * Create an instance of {@link Factura }
     * 
     * @return
     *     the new instance of {@link Factura }
     */
    public Factura createFactura() {
        return new Factura();
    }

    /**
     * Create an instance of {@link DetalleFactura }
     * 
     * @return
     *     the new instance of {@link DetalleFactura }
     */
    public DetalleFactura createDetalleFactura() {
        return new DetalleFactura();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExcepcionComercializadora }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExcepcionComercializadora }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "ExcepcionComercializadora")
    public JAXBElement<ExcepcionComercializadora> createExcepcionComercializadora(ExcepcionComercializadora value) {
        return new JAXBElement<>(_ExcepcionComercializadora_QNAME, ExcepcionComercializadora.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActualizarCelular }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ActualizarCelular }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "actualizarCelular")
    public JAXBElement<ActualizarCelular> createActualizarCelular(ActualizarCelular value) {
        return new JAXBElement<>(_ActualizarCelular_QNAME, ActualizarCelular.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActualizarCelularResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ActualizarCelularResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "actualizarCelularResponse")
    public JAXBElement<ActualizarCelularResponse> createActualizarCelularResponse(ActualizarCelularResponse value) {
        return new JAXBElement<>(_ActualizarCelularResponse_QNAME, ActualizarCelularResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarAmortizacion }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarAmortizacion }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarAmortizacion")
    public JAXBElement<ConsultarAmortizacion> createConsultarAmortizacion(ConsultarAmortizacion value) {
        return new JAXBElement<>(_ConsultarAmortizacion_QNAME, ConsultarAmortizacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarAmortizacionResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultarAmortizacionResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "consultarAmortizacionResponse")
    public JAXBElement<ConsultarAmortizacionResponse> createConsultarAmortizacionResponse(ConsultarAmortizacionResponse value) {
        return new JAXBElement<>(_ConsultarAmortizacionResponse_QNAME, ConsultarAmortizacionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearCelular }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CrearCelular }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "crearCelular")
    public JAXBElement<CrearCelular> createCrearCelular(CrearCelular value) {
        return new JAXBElement<>(_CrearCelular_QNAME, CrearCelular.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearCelularResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CrearCelularResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "crearCelularResponse")
    public JAXBElement<CrearCelularResponse> createCrearCelularResponse(CrearCelularResponse value) {
        return new JAXBElement<>(_CrearCelularResponse_QNAME, CrearCelularResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarCelular }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EliminarCelular }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "eliminarCelular")
    public JAXBElement<EliminarCelular> createEliminarCelular(EliminarCelular value) {
        return new JAXBElement<>(_EliminarCelular_QNAME, EliminarCelular.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarCelularResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EliminarCelularResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "eliminarCelularResponse")
    public JAXBElement<EliminarCelularResponse> createEliminarCelularResponse(EliminarCelularResponse value) {
        return new JAXBElement<>(_EliminarCelularResponse_QNAME, EliminarCelularResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FacturarCredito }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FacturarCredito }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "facturarCredito")
    public JAXBElement<FacturarCredito> createFacturarCredito(FacturarCredito value) {
        return new JAXBElement<>(_FacturarCredito_QNAME, FacturarCredito.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FacturarCreditoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FacturarCreditoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "facturarCreditoResponse")
    public JAXBElement<FacturarCreditoResponse> createFacturarCreditoResponse(FacturarCreditoResponse value) {
        return new JAXBElement<>(_FacturarCreditoResponse_QNAME, FacturarCreditoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FacturarEfectivo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FacturarEfectivo }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "facturarEfectivo")
    public JAXBElement<FacturarEfectivo> createFacturarEfectivo(FacturarEfectivo value) {
        return new JAXBElement<>(_FacturarEfectivo_QNAME, FacturarEfectivo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FacturarEfectivoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FacturarEfectivoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "facturarEfectivoResponse")
    public JAXBElement<FacturarEfectivoResponse> createFacturarEfectivoResponse(FacturarEfectivoResponse value) {
        return new JAXBElement<>(_FacturarEfectivoResponse_QNAME, FacturarEfectivoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarCelulares }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListarCelulares }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "listarCelulares")
    public JAXBElement<ListarCelulares> createListarCelulares(ListarCelulares value) {
        return new JAXBElement<>(_ListarCelulares_QNAME, ListarCelulares.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarCelularesResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListarCelularesResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "listarCelularesResponse")
    public JAXBElement<ListarCelularesResponse> createListarCelularesResponse(ListarCelularesResponse value) {
        return new JAXBElement<>(_ListarCelularesResponse_QNAME, ListarCelularesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerCelular }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ObtenerCelular }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "obtenerCelular")
    public JAXBElement<ObtenerCelular> createObtenerCelular(ObtenerCelular value) {
        return new JAXBElement<>(_ObtenerCelular_QNAME, ObtenerCelular.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerCelularResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ObtenerCelularResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "obtenerCelularResponse")
    public JAXBElement<ObtenerCelularResponse> createObtenerCelularResponse(ObtenerCelularResponse value) {
        return new JAXBElement<>(_ObtenerCelularResponse_QNAME, ObtenerCelularResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerFactura }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ObtenerFactura }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "obtenerFactura")
    public JAXBElement<ObtenerFactura> createObtenerFactura(ObtenerFactura value) {
        return new JAXBElement<>(_ObtenerFactura_QNAME, ObtenerFactura.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerFacturaResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ObtenerFacturaResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "obtenerFacturaResponse")
    public JAXBElement<ObtenerFacturaResponse> createObtenerFacturaResponse(ObtenerFacturaResponse value) {
        return new JAXBElement<>(_ObtenerFacturaResponse_QNAME, ObtenerFacturaResponse.class, null, value);
    }

}
