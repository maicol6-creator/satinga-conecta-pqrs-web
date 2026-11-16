package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.modelo.Estado;
import co.satingaconecta.pqrs.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * GET /pqrs?estado=RADICADA — el ciudadano ve sus solicitudes; el funcionario ve todas y puede filtrar.
 * El filtro viaja por GET (parámetros en la URL) porque no modifica datos y se puede marcar como favorito.
 */
@WebServlet("/pqrs")
public class ListaPqrsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = usuarioActual(req);
        Estado filtro = Estado.desde(req.getParameter("estado"));
        req.setAttribute("filtro", filtro);
        req.setAttribute("estados", Estado.values());
        req.setAttribute("lista", u.isFuncionario() ? pqrsDao().listarTodas(filtro) : pqrsDao().listarPorCiudadano(u.getUsuario()));
        vista(req, resp, "lista.jsp", u.isFuncionario() ? "Todas las PQRS" : "Mis PQRS");
    }
}
