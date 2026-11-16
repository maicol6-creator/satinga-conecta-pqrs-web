<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<section class="tarjeta estrecha" aria-labelledby="t">
  <h1 id="t">Crear cuenta de ciudadano</h1>
  <form method="post" action="<c:url value='/registro'/>" novalidate>
    <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
    <div class="campo">
      <label for="nombre">Nombre completo</label>
      <input id="nombre" name="nombre" required maxlength="80" value="<c:out value='${nombre}'/>">
      <c:if test="${not empty errores.nombre}"><span class="error" role="alert"><c:out value="${errores.nombre}"/></span></c:if>
    </div>
    <div class="campo">
      <label for="usuario">Usuario</label>
      <input id="usuario" name="usuario" required maxlength="30" autocomplete="username" value="<c:out value='${usuarioIngresado}'/>">
      <c:if test="${not empty errores.usuario}"><span class="error" role="alert"><c:out value="${errores.usuario}"/></span></c:if>
    </div>
    <div class="campo">
      <label for="clave">Contraseña (mínimo 8 caracteres)</label>
      <input id="clave" name="clave" type="password" required autocomplete="new-password">
      <c:if test="${not empty errores.clave}"><span class="error" role="alert"><c:out value="${errores.clave}"/></span></c:if>
    </div>
    <div class="campo">
      <label for="confirmar">Confirmar contraseña</label>
      <input id="confirmar" name="confirmar" type="password" required autocomplete="new-password">
      <c:if test="${not empty errores.confirmar}"><span class="error" role="alert"><c:out value="${errores.confirmar}"/></span></c:if>
    </div>
    <button class="btn" type="submit">Crear cuenta</button>
  </form>
  <p class="nota">¿Ya tienes cuenta? <a href="<c:url value='/login'/>">Inicia sesión</a>.</p>
</section>
<jsp:include page="/WEB-INF/vistas/pie.jsp"><jsp:param name="modulo" value="Registro"/></jsp:include>
