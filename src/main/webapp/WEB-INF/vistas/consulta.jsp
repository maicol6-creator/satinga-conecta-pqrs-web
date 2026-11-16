<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<section class="tarjeta estrecha" aria-labelledby="t">
  <h1 id="t">Consultar el estado de una PQRS</h1>
  <p>Escribe el número de radicado que recibiste (por ejemplo <code>PQ-2026-0001</code>). No necesitas iniciar sesión.</p>
  <form method="get" action="<c:url value='/consulta'/>">
    <div class="campo">
      <label for="radicado">Número de radicado</label>
      <input id="radicado" name="radicado" required maxlength="30" value="<c:out value='${buscado}'/>">
    </div>
    <button class="btn" type="submit">Consultar</button>
  </form>
  <c:if test="${not empty buscado}">
    <c:choose>
      <c:when test="${not empty pqrs}">
        <div class="alerta alerta-info" role="status">
          <strong><c:out value="${pqrs.radicado}"/></strong> · <c:out value="${pqrs.tipo.etiqueta}"/> ·
          <span class="insignia ${pqrs.estado.clase}"><c:out value="${pqrs.estado.etiqueta}"/></span><br>
          Radicada el <c:out value="${pqrs.fechaTexto}"/>.
        </div>
      </c:when>
      <c:otherwise><div class="alerta alerta-error" role="alert">No encontramos el radicado «<c:out value="${buscado}"/>».</div></c:otherwise>
    </c:choose>
  </c:if>
</section>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Consulta pública"/></jsp:include>
