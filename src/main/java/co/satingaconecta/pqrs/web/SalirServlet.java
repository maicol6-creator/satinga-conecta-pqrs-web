package co.satingaconecta.pqrs.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Cierra la sesión. Solo por POST (con CSRF) para que un enlace externo no pueda desconectar al usuario. */
@WebServlet("/salir")
public class SalirServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!csrfValido(req, resp)) return;
        req.getSession().invalidate();
        redirigir(req, resp, "/login");
    }
}
