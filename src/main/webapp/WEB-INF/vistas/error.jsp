<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Error – Satinga Conecta</title><link rel="stylesheet" href="<c:url value='/css/estilos.css'/>"></head>
<body><main class="contenido" id="principal"><section class="tarjeta estrecha">
  <h1>No pudimos completar la acción</h1>
  <p>Código <strong><c:out value="${pageContext.errorData.statusCode}"/></strong>.
     <c:if test="${pageContext.errorData.statusCode == 404}">La página no existe.</c:if>
     <c:if test="${pageContext.errorData.statusCode == 403}">No tienes permiso para esta acción.</c:if></p>
  <p><a class="btn" href="<c:url value='/inicio'/>">Volver al inicio</a></p>
</section></main></body></html>
