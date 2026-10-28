<c:set var="titulo" value="Facturas" />
<c:set var="seccion" value="factura" />
<%@ include file="plantilla/cabecera.jspf" %>

<div class="encabezado">
    <div>
        <h1>Consultar factura</h1>
        <p class="subtitulo">Busca una factura emitida por su número.</p>
    </div>
    <form class="buscador" method="get" action="${pageContext.request.contextPath}/factura" novalidate>
        <input type="text" name="num" inputmode="numeric" maxlength="9" placeholder="N.º de factura" required
               data-validar="entero" data-min="1" data-nombre="número de factura" value="${fn:escapeXml(param.num)}">
        <button type="submit" class="boton">Buscar</button>
    </form>
</div>

<c:if test="${not empty factura}">
    <article class="tarjeta factura">
        <header class="factura-cabecera">
            <div>
                <h2>Factura N.º ${factura.numFactura}</h2>
                <p class="subtitulo"><c:out value="${factura.fecha}" /></p>
            </div>
            <span class="etiqueta ${factura.formaPago == 'CREDITO' ? 'etiqueta-azul' : 'etiqueta-ok'}">
                ${factura.formaPago == 'CREDITO' ? 'Crédito directo' : 'Efectivo'}
            </span>
        </header>

        <div class="datos-cliente">
            <div><span>Cliente</span><strong><c:out value="${factura.nombre}" /></strong></div>
            <div><span>Cédula</span><strong><c:out value="${factura.cedula}" /></strong></div>
            <c:if test="${not empty factura.codCredito}">
                <div><span>Crédito BanQuito</span><strong>N.º ${factura.codCredito}</strong></div>
            </c:if>
        </div>

        <table class="tabla">
            <thead>
            <tr><th>Celular</th><th class="num">Cantidad</th><th class="num">Precio unitario</th><th class="num">Subtotal</th></tr>
            </thead>
            <tbody>
            <c:forEach var="det" items="${factura.detalles}">
                <tr>
                    <td><c:out value="${det.marcaCelular} ${det.modeloCelular}" /></td>
                    <td class="num">${det.cantidad}</td>
                    <td class="num"><fmt:formatNumber value="${det.precioUnitario}" type="currency" currencySymbol="$" /></td>
                    <td class="num"><fmt:formatNumber value="${det.subtotal}" type="currency" currencySymbol="$" /></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <dl class="totales">
            <dt>Subtotal</dt><dd><fmt:formatNumber value="${factura.subtotal}" type="currency" currencySymbol="$" /></dd>
            <dt>Descuento</dt><dd>− <fmt:formatNumber value="${factura.descuento}" type="currency" currencySymbol="$" /></dd>
            <dt class="total">Total</dt><dd class="total"><fmt:formatNumber value="${factura.total}" type="currency" currencySymbol="$" /></dd>
        </dl>

        <c:if test="${factura.formaPago == 'CREDITO'}">
            <div class="botones">
                <a class="boton" href="${pageContext.request.contextPath}/amortizacion?numFactura=${factura.numFactura}">Ver tabla de amortización</a>
            </div>
        </c:if>
    </article>
</c:if>

<%@ include file="plantilla/pie.jspf" %>
