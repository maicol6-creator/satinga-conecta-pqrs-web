<%-- Pie de página incluido con la ACCIÓN <jsp:include> (se ejecuta en cada petición, con su propio parámetro). --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%! // DECLARACIÓN: se compila como miembro de la clase del servlet generado
    private static final String ENTIDAD = "Alcaldía de Olaya Herrera, Nariño";
    private static String saludoDe(String sesion) { return "visitante".equals(sesion) ? "Eres visitante" : "Sesión iniciada"; }
%>
<%  // SCRIPTLET: código Java dentro de la petición
    String estadoSesion = (session.getAttribute("usuario") != null) ? "autenticado" : "visitante";
%>
</main>
<footer class="pie">
  <p>Satinga Conecta · Bocas de Satinga · <%= ENTIDAD %> · <%= java.time.Year.now() %></p>
  <p class="pie-peq"><c:out value="${param.modulo}"/> · <%= saludoDe(estadoSesion) %></p>
</footer>
</body>
</html>
