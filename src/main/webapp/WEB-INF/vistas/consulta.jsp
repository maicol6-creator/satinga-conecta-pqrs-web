<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="ph"><div><h1>Consultar el estado de una PQRS</h1>
  <p>Escribe el número de radicado que recibiste (por ejemplo <b>PQ-2026-0001</b>). No necesitas iniciar sesión.</p></div></div>
<section class="card" style="max-width:640px" aria-labelledby="t">
  <h2 id="t" class="solo-lectores">Formulario de consulta</h2>
  <form method="get" action="<c:url value='/consulta'/>">
    <div class="field">
      <label for="radicado">Número de radicado</label>
      <input id="radicado" name="radicado" required maxlength="30" value="<c:out value='${buscado}'/>">
    </div>
    <button class="btn" type="submit">Consultar</button>
  </form>
  <c:if test="${not empty buscado}">
    <c:choose>
      <c:when test="${not empty pqrs}">
        <div class="aviso info" style="margin-top:18px" role="status">
          <b><c:out value="${pqrs.radicado}"/></b> · <c:out value="${pqrs.tipo.etiqueta}"/> ·
          <span class="tag ${pqrs.estado.clase}"><c:out value="${pqrs.estado.etiqueta}"/></span><br>
          Radicada el <c:out value="${pqrs.fechaTexto}"/>.
        </div>
      </c:when>
      <c:otherwise><div class="aviso error" style="margin-top:18px" role="alert">No encontramos el radicado «<c:out value="${buscado}"/>».</div></c:otherwise>
    </c:choose>
  </c:if>
</section>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Consulta pública"/></jsp:include>
