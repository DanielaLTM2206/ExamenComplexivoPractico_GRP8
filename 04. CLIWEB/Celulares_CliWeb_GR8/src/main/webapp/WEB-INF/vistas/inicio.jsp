<c:set var="titulo" value="Inicio" />
<c:set var="seccion" value="inicio" />
<%@ include file="plantilla/cabecera.jspf" %>

<section class="portada">
    <img src="${pageContext.request.contextPath}/img/portada.jpg" alt="Atención al cliente en una agencia bancaria">
    <div class="portada-texto">
        <h1>Bienvenido a la Comercializadora de Celulares</h1>
        <p>Vende teléfonos en efectivo con el <strong>42 % de descuento</strong> o con <strong>crédito directo</strong>
            de BanQuito, de 3 a 18 meses al 16,5 % anual.</p>
    </div>
</section>

<section class="accesos">
    <a class="acceso" href="${pageContext.request.contextPath}/catalogo">
        <span class="acceso-titulo">Catálogo</span>
        <span>Registra, edita o desactiva los celulares disponibles.</span>
    </a>
    <a class="acceso" href="${pageContext.request.contextPath}/facturar">
        <span class="acceso-titulo">Facturar</span>
        <span>Emite una factura en efectivo o con crédito directo.</span>
    </a>
    <a class="acceso" href="${pageContext.request.contextPath}/amortizacion">
        <span class="acceso-titulo">Tabla de amortización</span>
        <span>Consulta las cuotas del crédito de una factura.</span>
    </a>
</section>

<%@ include file="plantilla/pie.jspf" %>
