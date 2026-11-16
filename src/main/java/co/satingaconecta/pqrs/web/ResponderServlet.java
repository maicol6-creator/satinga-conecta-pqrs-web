package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.modelo.Estado;
import co.satingaconecta.pqrs.modelo.Pqrs;
import co.satingaconecta.pqrs.modelo.Usuario;
import co.satingaconecta.pqrs.util.Validacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** POST /gestion/responder — solo funcionarios: registra la respuesta y actualiza el estado. */
@WebServlet("/gestion/responder")
public class ResponderServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = usuarioActual(req);
        if (!u.isFuncionario()) { resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los funcionarios gestionan PQRS."); return; }
        if (!csrfValido(req, resp)) return;

        String radicado = Validacion.limpiar(req.getParameter("radicado"));
        String respuesta = Validacion.limpiar(req.getParameter("respuesta"));
        Estado estado = Estado.desde(req.getParameter("estado"));
        Pqrs p = pqrsDao().buscarPorRadicado(radicado).orElse(null);
        if (p == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No existe esa PQRS."); return; }

        if (estado == null || estado == Estado.RADICADA || !Validacion.longitud(respuesta, 10, 1500)) {
            flash(req, "Escribe una respuesta de 10 a 1500 caracteres y elige un estado de gestión.", "error");
        } else {
            pqrsDao().responder(radicado, respuesta, estado, u.getUsuario());
            flash(req, "Respuesta registrada para " + radicado + ".", "ok");
        }
        redirigir(req, resp, "/pqrs/detalle?radicado=" + p.getRadicado());
    }
}
