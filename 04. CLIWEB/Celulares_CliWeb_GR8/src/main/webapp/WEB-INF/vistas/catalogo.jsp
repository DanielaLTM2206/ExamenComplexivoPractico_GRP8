<c:set var="titulo" value="Catálogo" />
<c:set var="seccion" value="catalogo" />
<%@ include file="plantilla/cabecera.jspf" %>

<div class="encabezado">
    <div>
        <h1>Catálogo de celulares</h1>
        <p class="subtitulo">Teléfonos disponibles para la venta (sin control de inventario).</p>
    </div>
    <a class="boton" href="${pageContext.request.contextPath}/catalogo?accion=nuevo">+ Nuevo celular</a>
</div>

<form class="filtro" method="get" action="${pageContext.request.contextPath}/catalogo">
    <label class="interruptor">
        <input type="checkbox" name="todos" value="1" ${todos ? 'checked' : ''} onchange="this.form.submit()">
        Mostrar también los desactivados
    </label>
</form>

<div class="tarjeta">
    <c:choose>
        <c:when test="${empty celulares}">
            <p class="vacio">No hay celulares para mostrar.</p>
        </c:when>
        <c:otherwise>
            <table class="tabla">
                <thead>
                <tr><th>Código</th><th>Marca</th><th>Modelo</th><th class="num">Precio</th><th>Estado</th><th class="acciones">Acciones</th></tr>
                </thead>
                <tbody>
                <c:forEach var="cel" items="${celulares}">
                    <tr class="${cel.activo ? '' : 'inactivo'}">
                        <td>${cel.codCelular}</td>
                        <td><c:out value="${cel.marca}" /></td>
                        <td><c:out value="${cel.modelo}" /></td>
                        <td class="num"><fmt:formatNumber value="${cel.precio}" type="currency" currencySymbol="$" /></td>
                        <td><span class="etiqueta ${cel.activo ? 'etiqueta-ok' : 'etiqueta-gris'}">${cel.activo ? 'Activo' : 'Desactivado'}</span></td>
                        <td class="acciones">
                            <a class="boton boton-secundario boton-chico" href="${pageContext.request.contextPath}/catalogo?accion=editar&amp;cod=${cel.codCelular}">Editar</a>
                            <form method="post" action="${pageContext.request.contextPath}/catalogo" class="en-linea"
                                  data-confirmar="¿Eliminar el celular ${fn:escapeXml(cel.marca)} ${fn:escapeXml(cel.modelo)}? Si ya tiene ventas, solo se desactivará.">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="cod" value="${cel.codCelular}">
                                <button type="submit" class="boton boton-peligro boton-chico">Eliminar</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="plantilla/pie.jspf" %>
