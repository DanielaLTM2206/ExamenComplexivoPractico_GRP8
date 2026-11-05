<c:set var="titulo" value="Servidor" />
<c:set var="seccion" value="servidor" />
<%@ include file="plantilla/cabecera.jspf" %>

<div class="encabezado">
    <div>
        <h1>Servidor</h1>
        <p class="subtitulo">Dirección del servicio SOAP de la comercializadora que usa este cliente web.</p>
    </div>
</div>

<div class="dos-columnas">
    <form class="tarjeta formulario" method="post" action="${pageContext.request.contextPath}/servidor" novalidate>
        <div class="servidor-actual">
            <span>Servidor actual</span>
            <strong><c:out value="${urlActual}" /></strong>
            <span class="etiqueta ${porDefecto ? 'etiqueta-gris' : 'etiqueta-azul'}">${porDefecto ? 'Local (por defecto)' : 'Personalizado'}</span>
        </div>

        <div class="campo">
            <label for="url">IP o dirección del servidor</label>
            <input type="text" id="url" name="url" maxlength="200" required data-nombre="dirección del servidor"
                   value="${fn:escapeXml(urlEscrita)}" placeholder="Ej.: 192.168.1.25" autocomplete="off" spellcheck="false">
            <small class="nota">Con solo la IP se completa el puerto 8080 y la ruta /WS_Comercializadora_SOAP/WSComercializadora.</small>
        </div>

        <c:if test="${not empty resultado}">
            <p class="resultado-conexion ${resultado.exitosa() ? 'ok' : 'falla'}">
                ${resultado.exitosa() ? '&#10003;&nbsp;' : '&#10007;&nbsp;'}<c:out value="${resultado.mensaje()}" />
            </p>
        </c:if>
        <c:if test="${ofrecerForzar}">
            <label class="interruptor">
                <input type="checkbox" name="forzar"> Guardar esta dirección aunque ahora no responda
            </label>
        </c:if>

        <div class="botones">
            <button type="submit" name="accion" value="probar" class="boton boton-secundario">Probar conexión</button>
            <button type="submit" name="accion" value="guardar" class="boton">Guardar</button>
        </div>
    </form>

    <div class="tarjeta">
        <h2>¿Cuándo cambiarlo?</h2>
        <p class="nota">Por defecto el cliente web usa <strong><c:out value="${urlLocal}" /></strong>, porque se despliega
            en el mismo Payara que los servidores SOAP.</p>
        <p class="nota">Si este cliente web se despliega en otra PC, escriba la IP de la PC servidor que muestra
            <strong>PREPARAR_SERVIDOR_SOAP_8080.bat</strong>. Ambas PC deben estar en la misma red.</p>
        <form method="post" action="${pageContext.request.contextPath}/servidor"
              data-confirmar="Se volverá a usar el servidor local (localhost). ¿Continuar?">
            <input type="hidden" name="accion" value="restablecer">
            <button type="submit" class="boton boton-peligro boton-bloque" ${porDefecto ? 'disabled' : ''}>Restablecer servidor local</button>
        </form>
    </div>
</div>

<%@ include file="plantilla/pie.jspf" %>
