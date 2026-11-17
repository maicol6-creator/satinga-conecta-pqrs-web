<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="auth">
  <section class="auth-side" aria-label="Presentación">
    <h2>Tu voz llega a la Alcaldía</h2>
    <p>Radica peticiones, quejas, reclamos y sugerencias desde Bocas de Satinga y haz seguimiento con tu número de radicado.</p>
    <ul>
      <li>Radicación en pocos pasos</li>
      <li>Consulta pública del estado por radicado</li>
      <li>Respuesta de los funcionarios en la plataforma</li>
    </ul>
  </section>
  <div class="auth-box">
    <form class="form" method="post" action="<c:url value='/login'/>" novalidate>
      <h1 style="font-size:28px;color:var(--navy);margin-bottom:6px">Iniciar sesión</h1>
      <p class="lead">Ingresa con tu usuario y contraseña.</p>
      <c:if test="${not empty flash}"><div class="aviso ${flashTipo == 'error' ? 'error' : 'ok'}" role="status"><c:out value="${flash}"/></div></c:if>
      <c:if test="${not empty error}"><div class="aviso error" role="alert"><c:out value="${error}"/></div></c:if>
      <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
      <div class="field">
        <label for="usuario">Usuario</label>
        <input id="usuario" name="usuario" required autocomplete="username" value="<c:out value='${usuarioIngresado}'/>">
      </div>
      <div class="field">
        <label for="clave">Contraseña</label>
        <input id="clave" name="clave" type="password" required autocomplete="current-password">
      </div>
      <button class="btn block" type="submit">Entrar</button>
      <p class="form-note" style="margin-top:14px">¿No tienes cuenta? <a class="link" href="<c:url value='/registro'/>">Regístrate</a></p>
      <div class="credenciales">Demostración: <code>maicol</code> / <code>Satinga2026</code> (ciudadano) · <code>laura</code> / <code>Funcionario2026</code> (funcionario)</div>
    </form>
  </div>
</div>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Acceso"/></jsp:include>
