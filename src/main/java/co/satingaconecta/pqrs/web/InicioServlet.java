package co.satingaconecta.pqrs.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Panel de inicio: resumen de PQRS por estado. */
@WebServlet("/inicio")
public class InicioServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("conteo", pqrsDao().contarPorEstado());
        vista(req, resp, "inicio.jsp", "Inicio");
    }
}
