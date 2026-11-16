<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<section class="tarjeta" aria-labelledby="t">
  <h1 id="t">Radicar una PQRS</h1>
  <%-- Formulario POST: envía datos que crean un registro; el servlet responde con redirección (PRG) --%>
  <form method="post" action="<c:url value='/pqrs/nueva'/>" novalidate>
    <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
    <div class="campo">
      <label for="tipo">Tipo de solicitud <span class="obl">*</span></label>
      <select id="tipo" name="tipo" required>
        <option value="">Selecciona…</option>
        <c:forEach var="t" items="${tipos}">
          <option value="${t}" ${t == tipoSel ? 'selected' : ''}><c:out value="${t.etiqueta}"/></option>
        </c:forEach>
      </select>
      <c:if test="${not empty errores.tipo}"><span class="error" role="alert"><c:out value="${errores.tipo}"/></span></c:if>
    </div>
    <div class="campo">
      <label for="asunto">Asunto <span class="obl">*</span></label>
      <input id="asunto" name="asunto" required maxlength="120" value="<c:out value='${asunto}'/>">
      <c:if test="${not empty errores.asunto}"><span class="error" role="alert"><c:out value="${errores.asunto}"/></span></c:if>
    </div>
    <div class="campo">
      <label for="barrio">Barrio, vereda o sector</label>
      <input id="barrio" name="barrio" maxlength="80" value="<c:out value='${barrio}'/>">
      <c:if test="${not empty errores.barrio}"><span class="error" role="alert"><c:out value="${errores.barrio}"/></span></c:if>
    </div>
    <div class="campo">
      <label for="descripcion">Descripción <span class="obl">*</span></label>
      <textarea id="descripcion" name="descripcion" rows="6" required maxlength="1500"><c:out value="${descripcion}"/></textarea>
      <c:if test="${not empty errores.descripcion}"><span class="error" role="alert"><c:out value="${errores.descripcion}"/></span></c:if>
    </div>
    <button class="btn" type="submit">Radicar</button>
    <a class="btn btn-sec" href="<c:url value='/pqrs'/>">Cancelar</a>
  </form>
</section>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Radicación"/></jsp:include>
