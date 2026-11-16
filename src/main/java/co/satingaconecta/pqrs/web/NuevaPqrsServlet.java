package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.modelo.Pqrs;
import co.satingaconecta.pqrs.modelo.Tipo;
import co.satingaconecta.pqrs.util.Validacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/** GET muestra el formulario de radicación; POST valida, guarda y redirige al detalle (Post/Redirect/Get). */
@WebServlet("/pqrs/nueva")
public class NuevaPqrsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (usuarioActual(req).isFuncionario()) { resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los ciudadanos radican PQRS."); return; }
        req.setAttribute("tipos", Tipo.values());
        vista(req, resp, "formulario.jsp", "Radicar PQRS");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!csrfValido(req, resp)) return;
        var usuario = usuarioActual(req);
        if (usuario.isFuncionario()) { resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los ciudadanos radican PQRS."); return; }

        Tipo tipo = Tipo.desde(req.getParameter("tipo"));
        String asunto = Validacion.limpiar(req.getParameter("asunto"));
        String descripcion = Validacion.limpiar(req.getParameter("descripcion"));
        String barrio = Validacion.limpiar(req.getParameter("barrio"));

        Map<String, String> errores = Validacion.errores();
        if (tipo == null) errores.put("tipo", "Selecciona el tipo de solicitud.");
        if (!Validacion.longitud(asunto, 5, 120)) errores.put("asunto", "El asunto debe tener entre 5 y 120 caracteres.");
        if (!Validacion.longitud(descripcion, 20, 1500)) errores.put("descripcion", "Describe tu solicitud (20 a 1500 caracteres).");
        if (barrio.length() > 80) errores.put("barrio", "El barrio o vereda admite máximo 80 caracteres.");

        if (!errores.isEmpty()) {
            req.setAttribute("errores", errores);
            req.setAttribute("tipos", Tipo.values());
            req.setAttribute("tipoSel", tipo);
            req.setAttribute("asunto", asunto);
            req.setAttribute("descripcion", descripcion);
            req.setAttribute("barrio", barrio);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            vista(req, resp, "formulario.jsp", "Radicar PQRS");
            return;
        }
        Pqrs p = pqrsDao().crear(new Pqrs(tipo, asunto, descripcion, barrio, usuario.getUsuario()));
        flash(req, "Tu solicitud fue radicada con el número " + p.getRadicado() + ".", "ok");
        redirigir(req, resp, "/pqrs/detalle?radicado=" + p.getRadicado());
    }
}
