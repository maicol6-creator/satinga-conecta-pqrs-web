<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%-- ACCIONES estándar: crear un JavaBean y leer/escribir sus propiedades --%>
<jsp:useBean id="reloj" class="co.satingaconecta.pqrs.util.Reloj" scope="request"/>
<%
    long total = 0;
    for (Long n : ((java.util.Map<?, Long>) request.getAttribute("conteo")).values()) total += n;
    request.setAttribute("total", total);   // SCRIPTLET: suma los conteos y lo deja disponible para EL
%>
<div class="encabezado">
  <h1>Hola, <c:out value="${usuarioActual.nombre}"/></h1>
  <p>Hoy es <jsp:getProperty name="reloj" property="hoy"/>.
     <c:choose>
       <c:when test="${usuarioActual.funcionario}">Gestiona las solicitudes de la comunidad.</c:when>
       <c:otherwise>Radica y sigue tus peticiones, quejas, reclamos y sugerencias.</c:otherwise>
     </c:choose></p>
</div>
<div class="rejilla">
  <div class="tarjeta kpi"><div class="num">${total}</div><div class="etq">PQRS en la plataforma</div></div>
  <c:forEach var="e" items="${conteo}">
    <div class="tarjeta kpi"><div class="num">${e.value}</div><div class="etq"><c:out value="${e.key.etiqueta}"/></div></div>
  </c:forEach>
</div>
<p class="acciones">
  <c:if test="${not usuarioActual.funcionario}"><a class="btn" href="<c:url value='/pqrs/nueva'/>">Radicar una PQRS</a></c:if>
  <a class="btn btn-sec" href="<c:url value='/pqrs'/>">${usuarioActual.funcionario ? 'Gestionar PQRS' : 'Ver mis PQRS'}</a>
</p>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Inicio"/></jsp:include>
