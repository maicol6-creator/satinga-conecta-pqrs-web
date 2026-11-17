<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="ph">
  <div><h1><c:out value="${pqrs.asunto}"/></h1>
    <p><span class="tag ${pqrs.estado.clase}"><c:out value="${pqrs.estado.etiqueta}"/></span>
       &nbsp;<c:out value="${pqrs.tipo.etiqueta}"/> · Radicado <b><c:out value="${pqrs.radicado}"/></b></p></div>
  <a class="btn sec" href="<c:url value='/pqrs'/>">← Volver al listado</a>
</div>
<div class="grid2">
  <div>
    <section class="card" aria-labelledby="t1">
      <h2 id="t1" style="font-size:17px;margin-bottom:12px;color:var(--navy)">Detalle de la solicitud</h2>
      <dl class="summary">
        <dt>Fecha</dt><dd><c:out value="${pqrs.fechaTexto}"/></dd>
        <dt>Ciudadano</dt><dd><c:out value="${pqrs.ciudadano}"/></dd>
        <c:if test="${not empty pqrs.barrio}"><dt>Barrio / sector</dt><dd><c:out value="${pqrs.barrio}"/></dd></c:if>
        <dt>Descripción</dt><dd class="texto"><c:out value="${pqrs.descripcion}"/></dd>
      </dl>
    </section>
    <section class="card" style="margin-top:18px" aria-labelledby="t2">
      <h2 id="t2" style="font-size:17px;margin-bottom:12px;color:var(--navy)">Respuesta de la Alcaldía</h2>
      <c:choose>
        <c:when test="${pqrs.respondida}">
          <p class="texto"><c:out value="${pqrs.respuesta}"/></p>
          <p class="form-note">Respondida por <c:out value="${pqrs.respondidoPor}"/> el <c:out value="${pqrs.fechaRespuestaTexto}"/>.</p>
        </c:when>
        <c:otherwise><p class="empty">Aún no hay respuesta. Un funcionario la registrará aquí.</p></c:otherwise>
      </c:choose>
    </section>
  </div>
  <c:if test="${usuarioActual.funcionario}">
    <section class="card" aria-labelledby="t3">
      <h2 id="t3" style="font-size:17px;margin-bottom:12px;color:var(--navy)">Gestionar</h2>
      <form method="post" action="<c:url value='/gestion/responder'/>">
        <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
        <input type="hidden" name="radicado" value="<c:out value='${pqrs.radicado}'/>">
        <div class="field">
          <label for="estado">Nuevo estado</label>
          <select id="estado" name="estado" required>
            <c:forEach var="e" items="${estados}">
              <c:if test="${e != 'RADICADA'}"><option value="${e}" ${e == pqrs.estado ? 'selected' : ''}><c:out value="${e.etiqueta}"/></option></c:if>
            </c:forEach>
          </select>
        </div>
        <div class="field">
          <label for="respuesta">Respuesta</label>
          <textarea id="respuesta" name="respuesta" rows="6" required minlength="10" maxlength="1500"><c:out value="${pqrs.respuesta}"/></textarea>
        </div>
        <button class="btn" type="submit">Guardar respuesta</button>
      </form>
    </section>
  </c:if>
</div>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Detalle"/></jsp:include>
