<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%-- ACCIONES estándar: crear un JavaBean y leer sus propiedades --%>
<jsp:useBean id="reloj" class="co.satingaconecta.pqrs.util.Reloj" scope="request"/>
<%
    long total = 0;
    for (Long n : ((java.util.Map<?, Long>) request.getAttribute("conteo")).values()) total += n;
    request.setAttribute("total", total);   // SCRIPTLET: suma los conteos y lo deja disponible para EL
%>
<div class="banner">
  <div>
    <h2>Hola, <c:out value="${usuarioActual.nombre}"/></h2>
    <p>Hoy es <jsp:getProperty name="reloj" property="hoy"/>.
      <c:choose>
        <c:when test="${usuarioActual.funcionario}">Gestiona las solicitudes de la comunidad.</c:when>
        <c:otherwise>Radica y sigue tus peticiones, quejas, reclamos y sugerencias.</c:otherwise>
      </c:choose></p>
  </div>
  <c:choose>
    <c:when test="${usuarioActual.funcionario}"><a class="btn amber" href="<c:url value='/pqrs'/>">Gestionar PQRS</a></c:when>
    <c:otherwise><a class="btn amber" href="<c:url value='/pqrs/nueva'/>">Radicar una PQRS</a></c:otherwise>
  </c:choose>
</div>

<div class="grid5">
  <div class="card kpi"><b>${total}</b><span>${usuarioActual.funcionario ? 'PQRS en la plataforma' : 'Mis PQRS radicadas'}</span></div>
  <c:forEach var="e" items="${conteo}">
    <div class="card kpi"><b>${e.value}</b><span><c:out value="${e.key.etiqueta}"/></span></div>
  </c:forEach>
</div>

<section class="card" style="margin-top:18px" aria-labelledby="t-ult">
  <h2 id="t-ult" style="font-size:17px;margin-bottom:12px;color:var(--navy)">Últimas solicitudes</h2>
  <c:choose>
    <c:when test="${empty ultimas}"><p class="empty">Aún no hay solicitudes. ¡Radica la primera!</p></c:when>
    <c:otherwise>
      <div class="tw">
        <table>
          <caption class="solo-lectores">Últimas PQRS</caption>
          <thead><tr><th scope="col">Radicado</th><th scope="col">Tipo</th><th scope="col">Asunto</th><th scope="col">Fecha</th><th scope="col">Estado</th></tr></thead>
          <tbody>
          <c:forEach var="p" items="${ultimas}">
            <tr>
              <td><a class="link" href="<c:url value='/pqrs/detalle'><c:param name='radicado' value='${p.radicado}'/></c:url>"><c:out value="${p.radicado}"/></a></td>
              <td><c:out value="${p.tipo.etiqueta}"/></td>
              <td><c:out value="${p.asunto}"/></td>
              <td><c:out value="${p.fechaTexto}"/></td>
              <td><span class="tag ${p.estado.clase}"><c:out value="${p.estado.etiqueta}"/></span></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </c:otherwise>
  </c:choose>
</section>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Inicio"/></jsp:include>
