<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="ph">
  <div><h1>Radicar PQRS</h1><p>Cuéntanos tu petición, queja, reclamo o sugerencia.</p></div>
  <a class="btn sec" href="<c:url value='/pqrs'/>">Ver mis solicitudes</a>
</div>
<div class="grid2">
  <%-- Formulario POST: envía datos que crean un registro; el servlet responde con redirección (PRG) --%>
  <form method="post" action="<c:url value='/pqrs/nueva'/>" class="card" novalidate>
    <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
    <div class="field">
      <label for="tipo">Tipo de solicitud *</label>
      <select id="tipo" name="tipo" required ${not empty errores.tipo ? 'aria-invalid="true"' : ''}>
        <option value="">Selecciona…</option>
        <c:forEach var="t" items="${tipos}">
          <option value="${t}" ${t == tipoSel ? 'selected' : ''}><c:out value="${t.etiqueta}"/></option>
        </c:forEach>
      </select>
      <c:if test="${not empty errores.tipo}"><span class="err" role="alert"><c:out value="${errores.tipo}"/></span></c:if>
    </div>
    <div class="field">
      <label for="asunto">Asunto *</label>
      <input id="asunto" name="asunto" required maxlength="120" placeholder="Resume tu solicitud en una frase" value="<c:out value='${asunto}'/>" ${not empty errores.asunto ? 'aria-invalid="true"' : ''}>
      <c:if test="${not empty errores.asunto}"><span class="err" role="alert"><c:out value="${errores.asunto}"/></span></c:if>
    </div>
    <div class="field">
      <label for="barrio">Barrio, vereda o sector</label>
      <input id="barrio" name="barrio" maxlength="80" value="<c:out value='${barrio}'/>" ${not empty errores.barrio ? 'aria-invalid="true"' : ''}>
      <c:if test="${not empty errores.barrio}"><span class="err" role="alert"><c:out value="${errores.barrio}"/></span></c:if>
    </div>
    <div class="field">
      <label for="descripcion">Descripción *</label>
      <textarea id="descripcion" name="descripcion" rows="6" required maxlength="1500" placeholder="Describe lo que ocurre, dónde y desde cuándo." ${not empty errores.descripcion ? 'aria-invalid="true"' : ''}><c:out value="${descripcion}"/></textarea>
      <c:if test="${not empty errores.descripcion}"><span class="err" role="alert"><c:out value="${errores.descripcion}"/></span></c:if>
    </div>
    <div class="btn-row">
      <button class="btn" type="submit">Radicar solicitud</button>
      <a class="btn sec" href="<c:url value='/pqrs'/>">Cancelar</a>
    </div>
  </form>
  <div class="card"><h2 style="font-size:17px;margin-bottom:12px;color:var(--navy)">Ayuda</h2>
    <ul class="list">
      <li><span><b>Petición</b><br><small style="color:var(--mute)">Solicita información o un servicio.</small></span></li>
      <li><span><b>Queja</b><br><small style="color:var(--mute)">Inconformidad con un funcionario.</small></span></li>
      <li><span><b>Reclamo</b><br><small style="color:var(--mute)">Servicio mal prestado.</small></span></li>
      <li><span><b>Sugerencia</b><br><small style="color:var(--mute)">Ideas para mejorar.</small></span></li>
    </ul>
    <p style="margin-top:12px;color:var(--mute);font-size:14px">Recibirás un número de radicado para el seguimiento.</p>
  </div>
</div>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Radicación"/></jsp:include>
