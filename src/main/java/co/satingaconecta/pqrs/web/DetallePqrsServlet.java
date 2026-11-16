package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.modelo.Estado;
import co.satingaconecta.pqrs.modelo.Pqrs;
import co.satingaconecta.pqrs.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** GET /pqrs/detalle?radicado=PQ-2026-0001 — el ciudadano solo ve las suyas; el funcionario, todas. */
@WebServlet("/pqrs/detalle")
public class DetallePqrsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = usuarioActual(req);
        Pqrs p = pqrsDao().buscarPorRadicado(req.getParameter("radicado")).orElse(null);
        if (p == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No existe una PQRS con ese radicado."); return; }
        if (!u.isFuncionario() && !p.getCiudadano().equals(u.getUsuario())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Esta PQRS pertenece a otro ciudadano.");
            return;
        }
        req.setAttribute("pqrs", p);
        req.setAttribute("estados", Estado.values());
        vista(req, resp, "detalle.jsp", "PQRS " + p.getRadicado());
    }
}
