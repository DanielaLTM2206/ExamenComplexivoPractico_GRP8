<c:set var="titulo" value="Facturar" />
<c:set var="seccion" value="facturar" />
<c:set var="formaPago" value="${param.formaPago == 'CREDITO' ? 'CREDITO' : 'EFECTIVO'}" />
<%@ include file="plantilla/cabecera.jspf" %>

<div class="encabezado">
    <div>
        <h1>Facturar venta</h1>
        <p class="subtitulo">El pago en efectivo tiene 42 % de descuento. El crédito directo lo aprueba BanQuito según la cédula del comprador.</p>
    </div>
</div>

<form id="form-factura" class="dos-columnas" method="post" action="${pageContext.request.contextPath}/facturar" novalidate>
    <div class="tarjeta formulario">
        <h2>Datos de la venta</h2>
        <div class="campo">
            <label for="cedula">Cédula del comprador</label>
            <input type="text" id="cedula" name="cedula" maxlength="10" inputmode="numeric" required
                   data-validar="cedula" data-nombre="cédula" value="${fn:escapeXml(param.cedula)}" placeholder="10 dígitos">
        </div>
        <div class="campo">
            <label for="nombre">Nombre del comprador</label>
            <input type="text" id="nombre" name="nombre" maxlength="100" required data-nombre="nombre"
                   value="${fn:escapeXml(param.nombre)}" placeholder="Nombres y apellidos">
        </div>
        <div class="campo">
            <label for="codCelular">Celular</label>
            <select id="codCelular" name="codCelular" required data-nombre="celular">
                <option value="">Seleccione un celular…</option>
                <c:forEach var="cel" items="${celulares}">
                    <option value="${cel.codCelular}" data-precio="${cel.precio}" ${param.codCelular == cel.codCelular ? 'selected' : ''}>
                        <c:out value="${cel.marca} ${cel.modelo}" /> — <fmt:formatNumber value="${cel.precio}" type="currency" currencySymbol="$" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <fieldset class="campo">
            <legend>Forma de pago</legend>
            <div class="opciones-pago">
                <label class="opcion">
                    <input type="radio" name="formaPago" value="EFECTIVO" ${formaPago == 'EFECTIVO' ? 'checked' : ''}>
                    <span><strong>Efectivo</strong><small>42 % de descuento</small></span>
                </label>
                <label class="opcion">
                    <input type="radio" name="formaPago" value="CREDITO" ${formaPago == 'CREDITO' ? 'checked' : ''}>
                    <span><strong>Crédito directo</strong><small>BanQuito · 16,5 % anual</small></span>
                </label>
            </div>
        </fieldset>

        <div class="campo" id="campo-cantidad">
            <label for="cantidad">Cantidad</label>
            <input type="text" id="cantidad" name="cantidad" inputmode="numeric" maxlength="2"
                   data-validar="entero" data-min="1" data-max="99" data-nombre="cantidad"
                   value="${empty param.cantidad ? '1' : fn:escapeXml(param.cantidad)}">
        </div>
        <div class="campo" id="campo-cuotas">
            <label for="numeroCuotas">Plazo (meses)</label>
            <select id="numeroCuotas" name="numeroCuotas">
                <c:forEach var="n" begin="3" end="18">
                    <option value="${n}" ${param.numeroCuotas == n or (empty param.numeroCuotas and n == 12) ? 'selected' : ''}>${n} cuotas mensuales</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <aside class="tarjeta resumen">
        <h2>Resumen</h2>
        <dl>
            <dt>Precio unitario</dt><dd id="r-precio">—</dd>
            <dt id="r-etq-subtotal">Subtotal</dt><dd id="r-subtotal">—</dd>
            <dt id="r-etq-descuento">Descuento (42 %)</dt><dd id="r-descuento">—</dd>
            <dt id="r-etq-cuota" hidden>Cuota estimada</dt><dd id="r-cuota" hidden>—</dd>
        </dl>
        <div class="total"><span id="r-etq-total">Total a pagar</span><strong id="r-total">—</strong></div>
        <p class="nota" id="r-nota">El valor final de la cuota lo calcula BanQuito al aprobar el crédito.</p>
        <button type="submit" class="boton boton-bloque">Emitir factura</button>
    </aside>
</form>

<%@ include file="plantilla/pie.jspf" %>
