<c:set var="titulo" value="Tabla de amortización" />
<c:set var="seccion" value="amortizacion" />
<%@ include file="plantilla/cabecera.jspf" %>

<div class="encabezado">
    <div>
        <h1>Tabla de amortización</h1>
        <p class="subtitulo">Cuotas del crédito directo asociado a una factura (consultadas al Core BanQuito).</p>
    </div>
    <form class="buscador" method="get" action="${pageContext.request.contextPath}/amortizacion" novalidate>
        <input type="text" name="numFactura" inputmode="numeric" maxlength="9" placeholder="N.º de factura" required
               data-validar="entero" data-min="1" data-nombre="número de factura" value="${fn:escapeXml(param.numFactura)}">
        <button type="submit" class="boton">Consultar</button>
    </form>
</div>

<c:if test="${not empty cuotas}">
    <div class="tarjeta">
        <div class="datos-cliente">
            <div><span>Factura</span><strong>N.º ${factura.numFactura}</strong></div>
            <div><span>Cliente</span><strong><c:out value="${factura.nombre}" /></strong></div>
            <div><span>Crédito BanQuito</span><strong>N.º ${factura.codCredito}</strong></div>
            <div><span>Monto financiado</span><strong><fmt:formatNumber value="${factura.total}" type="currency" currencySymbol="$" /></strong></div>
            <div><span>Plazo</span><strong>${fn:length(cuotas)} meses · 16,5 % anual</strong></div>
        </div>

        <table class="tabla">
            <thead>
            <tr><th>N.º cuota</th><th class="num">Valor cuota</th><th class="num">Interés pagado</th><th class="num">Capital pagado</th><th class="num">Saldo</th></tr>
            </thead>
            <tbody>
            <c:forEach var="cuota" items="${cuotas}">
                <tr>
                    <td>${cuota.numCuota}</td>
                    <td class="num"><fmt:formatNumber value="${cuota.valorCuota}" type="currency" currencySymbol="$" /></td>
                    <td class="num"><fmt:formatNumber value="${cuota.interes}" type="currency" currencySymbol="$" /></td>
                    <td class="num"><fmt:formatNumber value="${cuota.capital}" type="currency" currencySymbol="$" /></td>
                    <td class="num"><fmt:formatNumber value="${cuota.saldo}" type="currency" currencySymbol="$" /></td>
                </tr>
            </c:forEach>
            </tbody>
            <tfoot>
            <tr>
                <th>Totales</th>
                <th class="num"><fmt:formatNumber value="${totalPagado}" type="currency" currencySymbol="$" /></th>
                <th class="num"><fmt:formatNumber value="${totalInteres}" type="currency" currencySymbol="$" /></th>
                <th class="num"><fmt:formatNumber value="${totalCapital}" type="currency" currencySymbol="$" /></th>
                <th></th>
            </tr>
            </tfoot>
        </table>
    </div>
</c:if>

<%@ include file="plantilla/pie.jspf" %>
