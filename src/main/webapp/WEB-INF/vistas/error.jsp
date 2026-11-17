<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Error · Satinga Conecta</title><link rel="stylesheet" href="<c:url value='/css/estilos.css'/>"></head>
<body>
<header class="pub-head"><a class="brand" href="<c:url value='/inicio'/>"><span class="logo" aria-hidden="true"></span>Satinga Conecta</a></header>
<main class="content pub" id="main"><section class="card" style="max-width:560px;margin:40px auto;text-align:center">
  <h1 style="color:var(--navy)">No pudimos completar la acción</h1>
  <p style="margin:10px 0 18px;color:var(--mute)">Código <b><c:out value="${pageContext.errorData.statusCode}"/></b>.
     <c:if test="${pageContext.errorData.statusCode == 404}">La página no existe.</c:if>
     <c:if test="${pageContext.errorData.statusCode == 403}">No tienes permiso para esta acción.</c:if></p>
  <a class="btn" href="<c:url value='/inicio'/>">Volver al inicio</a>
</section></main>
</body></html>
