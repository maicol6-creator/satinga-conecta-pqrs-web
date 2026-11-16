<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<section class="tarjeta estrecha" aria-labelledby="t">
  <h1 id="t">Iniciar sesión</h1>
  <c:if test="${not empty error}"><div class="alerta alerta-error" role="alert"><c:out value="${error}"/></div></c:if>
  <form method="post" action="<c:url value='/login'/>" novalidate>
    <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
    <div class="campo">
      <label for="usuario">Usuario</label>
      <input id="usuario" name="usuario" required autocomplete="username" value="<c:out value='${usuarioIngresado}'/>">
    </div>
    <div class="campo">
      <label for="clave">Contraseña</label>
      <input id="clave" name="clave" type="password" required autocomplete="current-password">
    </div>
    <button class="btn" type="submit">Entrar</button>
  </form>
  <p class="nota">¿No tienes cuenta? <a href="<c:url value='/registro'/>">Regístrate</a>.</p>
  <p class="nota">Demostración: <code>maicol / Satinga2026</code> (ciudadano) · <code>laura / Funcionario2026</code> (funcionario).</p>
</section>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Acceso"/></jsp:include>
