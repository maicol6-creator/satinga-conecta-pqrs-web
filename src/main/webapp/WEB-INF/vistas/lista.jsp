<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="ph">
  <div><h1><c:out value="${titulo}"/></h1>
    <p>${usuarioActual.funcionario ? 'Solicitudes radicadas por la comunidad.' : 'Solicitudes que has radicado y su estado.'}</p></div>
  <c:if test="${not usuarioActual.funcionario}"><a class="btn" href="<c:url value='/pqrs/nueva'/>">Radicar PQRS</a></c:if>
</div>

<%-- Formulario GET: los parámetros van en la URL (?estado=...), es una consulta que no modifica datos --%>
<form class="card filtro" method="get" action="<c:url value='/pqrs'/>">
  <div class="field">
    <label for="estado">Filtrar por estado</label>
    <select id="estado" name="estado">
      <option value="">Todos</option>
      <c:forEach var="e" items="${estados}">
        <option value="${e}" ${e == filtro ? 'selected' : ''}><c:out value="${e.etiqueta}"/></option>
      </c:forEach>
    </select>
  </div>
  <button class="btn" type="submit">Filtrar</button>
  <c:if test="${not empty filtro}"><a class="btn sec" href="<c:url value='/pqrs'/>">Quitar filtro</a></c:if>
</form>

<section class="card" style="margin-top:18px" aria-label="Listado de PQRS">
<c:choose>
  <c:when test="${empty lista}"><p class="empty">No hay PQRS para mostrar.</p></c:when>
  <c:otherwise>
    <div class="tw">
      <table>
        <caption class="solo-lectores">Listado de PQRS</caption>
        <thead><tr><th scope="col">Radicado</th><th scope="col">Tipo</th><th scope="col">Asunto</th>
          <c:if test="${usuarioActual.funcionario}"><th scope="col">Ciudadano</th></c:if>
          <th scope="col">Fecha</th><th scope="col">Estado</th></tr></thead>
        <tbody>
        <c:forEach var="p" items="${lista}">
          <tr>
            <td><a class="link" href="<c:url value='/pqrs/detalle'><c:param name='radicado' value='${p.radicado}'/></c:url>"><c:out value="${p.radicado}"/></a></td>
            <td><c:out value="${p.tipo.etiqueta}"/></td>
            <td><c:out value="${p.asunto}"/></td>
            <c:if test="${usuarioActual.funcionario}"><td><c:out value="${p.ciudadano}"/></td></c:if>
            <td><c:out value="${p.fechaTexto}"/></td>
            <td><span class="tag ${p.estado.clase}"><c:out value="${p.estado.etiqueta}"/></span></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
    <p class="form-note">${fn:length(lista)} resultado(s).</p>
  </c:otherwise>
</c:choose>
</section>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Listado"/></jsp:include>
