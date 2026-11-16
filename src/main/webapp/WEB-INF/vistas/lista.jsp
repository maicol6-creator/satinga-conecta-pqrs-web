<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="encabezado"><h1><c:out value="${titulo}"/></h1></div>
<%-- Formulario GET: los parámetros van en la URL (?estado=...), es una consulta que no modifica datos --%>
<form class="filtro" method="get" action="<c:url value='/pqrs'/>">
  <label for="estado">Filtrar por estado</label>
  <select id="estado" name="estado">
    <option value="">Todos</option>
    <c:forEach var="e" items="${estados}">
      <option value="${e}" ${e == filtro ? 'selected' : ''}><c:out value="${e.etiqueta}"/></option>
    </c:forEach>
  </select>
  <button class="btn btn-sec btn-peq" type="submit">Filtrar</button>
</form>
<c:choose>
  <c:when test="${empty lista}"><div class="alerta alerta-info">No hay PQRS para mostrar.</div></c:when>
  <c:otherwise>
    <div class="tabla-envoltura">
      <table>
        <caption class="solo-lectores">Listado de PQRS</caption>
        <thead><tr><th scope="col">Radicado</th><th scope="col">Tipo</th><th scope="col">Asunto</th>
          <c:if test="${usuarioActual.funcionario}"><th scope="col">Ciudadano</th></c:if>
          <th scope="col">Fecha</th><th scope="col">Estado</th></tr></thead>
        <tbody>
        <c:forEach var="p" items="${lista}">
          <tr>
            <td><a href="<c:url value='/pqrs/detalle'><c:param name='radicado' value='${p.radicado}'/></c:url>"><c:out value="${p.radicado}"/></a></td>
            <td><c:out value="${p.tipo.etiqueta}"/></td>
            <td><c:out value="${p.asunto}"/></td>
            <c:if test="${usuarioActual.funcionario}"><td><c:out value="${p.ciudadano}"/></td></c:if>
            <td><c:out value="${p.fechaTexto}"/></td>
            <td><span class="insignia ${p.estado.clase}"><c:out value="${p.estado.etiqueta}"/></span></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
    <p class="nota">${fn:length(lista)} resultado(s).</p>
  </c:otherwise>
</c:choose>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Listado"/></jsp:include>
