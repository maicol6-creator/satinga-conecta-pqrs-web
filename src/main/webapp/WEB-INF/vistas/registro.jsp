<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="auth">
  <section class="auth-side" aria-label="Presentación">
    <h2>Crea tu cuenta</h2>
    <p>Con tu cuenta de ciudadano puedes radicar solicitudes y ver las respuestas de la Alcaldía.</p>
    <ul>
      <li>Gratis y sin filas</li>
      <li>Tu contraseña se guarda cifrada</li>
      <li>Solo tú ves tus solicitudes</li>
    </ul>
  </section>
  <div class="auth-box">
    <form class="form" method="post" action="<c:url value='/registro'/>" novalidate>
      <h1 style="font-size:28px;color:var(--navy);margin-bottom:6px">Registro de ciudadano</h1>
      <p class="lead">Los campos con * son obligatorios.</p>
      <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
      <div class="field">
        <label for="nombre">Nombre completo *</label>
        <input id="nombre" name="nombre" required maxlength="80" value="<c:out value='${nombre}'/>" ${not empty errores.nombre ? 'aria-invalid="true"' : ''}>
        <c:if test="${not empty errores.nombre}"><span class="err" role="alert"><c:out value="${errores.nombre}"/></span></c:if>
      </div>
      <div class="field">
        <label for="usuario">Usuario *</label>
        <input id="usuario" name="usuario" required maxlength="30" autocomplete="username" value="<c:out value='${usuarioIngresado}'/>" ${not empty errores.usuario ? 'aria-invalid="true"' : ''}>
        <small>Minúsculas, números, punto o guion bajo (3 a 30).</small>
        <c:if test="${not empty errores.usuario}"><span class="err" role="alert"><c:out value="${errores.usuario}"/></span></c:if>
      </div>
      <div class="row2">
        <div class="field">
          <label for="clave">Contraseña *</label>
          <input id="clave" name="clave" type="password" required autocomplete="new-password" ${not empty errores.clave ? 'aria-invalid="true"' : ''}>
          <small>Mínimo 8 caracteres.</small>
          <c:if test="${not empty errores.clave}"><span class="err" role="alert"><c:out value="${errores.clave}"/></span></c:if>
        </div>
        <div class="field">
          <label for="confirmar">Confirmar *</label>
          <input id="confirmar" name="confirmar" type="password" required autocomplete="new-password" ${not empty errores.confirmar ? 'aria-invalid="true"' : ''}>
          <c:if test="${not empty errores.confirmar}"><span class="err" role="alert"><c:out value="${errores.confirmar}"/></span></c:if>
        </div>
      </div>
      <button class="btn block" type="submit">Crear cuenta</button>
      <p class="form-note" style="margin-top:14px">¿Ya tienes cuenta? <a class="link" href="<c:url value='/login'/>">Inicia sesión</a></p>
    </form>
  </div>
</div>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Registro"/></jsp:include>
