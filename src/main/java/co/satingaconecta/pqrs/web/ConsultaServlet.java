package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.modelo.Pqrs;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Consulta pública del estado de una PQRS por número de radicado (no requiere iniciar sesión).
 * Formulario GET: GET /consulta?radicado=PQ-2026-0001. Solo muestra tipo, fecha y estado, nunca datos personales.
 */
@WebServlet("/consulta")
public class ConsultaServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String radicado = req.getParameter("radicado");
        if (radicado != null && !radicado.isBlank()) {
            Pqrs p = pqrsDao().buscarPorRadicado(radicado).orElse(null);
            req.setAttribute("buscado", radicado.trim());
            req.setAttribute("pqrs", p);
        }
        vista(req, resp, "consulta.jsp", "Consultar PQRS");
    }
}
