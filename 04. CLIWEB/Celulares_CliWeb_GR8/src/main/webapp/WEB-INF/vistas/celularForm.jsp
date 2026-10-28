<c:set var="nuevo" value="${empty celular or celular.codCelular == 0}" />
<c:set var="titulo" value="${nuevo ? 'Nuevo celular' : 'Editar celular'}" />
<c:set var="seccion" value="catalogo" />
<%@ include file="plantilla/cabecera.jspf" %>

<div class="encabezado">
    <div>
        <h1>${nuevo ? 'Nuevo celular' : 'Editar celular'}</h1>
        <p class="subtitulo">${nuevo ? 'Registra un teléfono en el catálogo.' : 'Modifica los datos o el estado del teléfono.'}</p>
    </div>
</div>

<form class="tarjeta formulario" method="post" action="${pageContext.request.contextPath}/catalogo" novalidate>
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="cod" value="${nuevo ? '' : celular.codCelular}">

    <c:if test="${not nuevo}">
        <div class="campo">
            <label>Código</label>
            <input type="text" value="${celular.codCelular}" disabled>
        </div>
    </c:if>
    <div class="campo">
        <label for="marca">Marca</label>
        <input type="text" id="marca" name="marca" maxlength="40" required data-nombre="marca"
               value="${fn:escapeXml(celular.marca)}" placeholder="Ej.: Samsung">
    </div>
    <div class="campo">
        <label for="modelo">Modelo</label>
        <input type="text" id="modelo" name="modelo" maxlength="60" required data-nombre="modelo"
               value="${fn:escapeXml(celular.modelo)}" placeholder="Ej.: Galaxy A35 256GB">
    </div>
    <div class="campo">
        <label for="precio">Precio (USD)</label>
        <input type="text" id="precio" name="precio" inputmode="decimal" required data-validar="decimal" data-nombre="precio"
               value="${fn:escapeXml(not empty precioTexto ? precioTexto : celular.precio)}" placeholder="Ej.: 449.99">
    </div>
    <c:if test="${not nuevo}">
        <label class="interruptor">
            <input type="checkbox" name="activo" ${celular.activo ? 'checked' : ''}>
            Activo (visible para la venta)
        </label>
    </c:if>

    <div class="botones">
        <a class="boton boton-secundario" href="${pageContext.request.contextPath}/catalogo?todos=1">Cancelar</a>
        <button type="submit" class="boton">Guardar</button>
    </div>
</form>

<%@ include file="plantilla/pie.jspf" %>
