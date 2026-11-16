<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<p class="migas"><a href="<c:url value='/pqrs'/>">PQRS</a> › <c:out value="${pqrs.radicado}"/></p>
<section class="tarjeta" aria-labelledby="t">
  <h1 id="t"><c:out value="${pqrs.asunto}"/></h1>
  <p><span class="insignia ${pqrs.estado.clase}"><c:out value="${pqrs.estado.etiqueta}"/></span>
     <c:out value="${pqrs.tipo.etiqueta}"/> · Radicado <strong><c:out value="${pqrs.radicado}"/></strong> · <c:out value="${pqrs.fechaTexto}"/></p>
  <dl>
    <dt>Ciudadano</dt><dd><c:out value="${pqrs.ciudadano}"/></dd>
    <c:if test="${not empty pqrs.barrio}"><dt>Barrio / sector</dt><dd><c:out value="${pqrs.barrio}"/></dd></c:if>
    <dt>Descripción</dt><dd class="texto"><c:out value="${pqrs.descripcion}"/></dd>
  </dl>
</section>

<section class="tarjeta" aria-labelledby="t2">
  <h2 id="t2">Respuesta de la Alcaldía</h2>
  <c:choose>
    <c:when test="${pqrs.respondida}">
      <p class="texto"><c:out value="${pqrs.respuesta}"/></p>
      <p class="nota">Respondida por <c:out value="${pqrs.respondidoPor}"/> el <c:out value="${pqrs.fechaRespuestaTexto}"/>.</p>
    </c:when>
    <c:otherwise><p class="nota">Aún no hay respuesta. Te avisaremos aquí cuando un funcionario la registre.</p></c:otherwise>
  </c:choose>
</section>

<c:if test="${usuarioActual.funcionario}">
  <section class="tarjeta" aria-labelledby="t3">
    <h2 id="t3">Gestionar</h2>
    <form method="post" action="<c:url value='/gestion/responder'/>">
      <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
      <input type="hidden" name="radicado" value="<c:out value='${pqrs.radicado}'/>">
      <div class="campo">
        <label for="estado">Nuevo estado</label>
        <select id="estado" name="estado" required>
          <c:forEach var="e" items="${estados}">
            <c:if test="${e != 'RADICADA'}"><option value="${e}" ${e == pqrs.estado ? 'selected' : ''}><c:out value="${e.etiqueta}"/></option></c:if>
          </c:forEach>
        </select>
      </div>
      <div class="campo">
        <label for="respuesta">Respuesta</label>
        <textarea id="respuesta" name="respuesta" rows="5" required minlength="10" maxlength="1500"><c:out value="${pqrs.respuesta}"/></textarea>
      </div>
      <button class="btn" type="submit">Guardar respuesta</button>
    </form>
  </section>
</c:if>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Detalle"/></jsp:include>
