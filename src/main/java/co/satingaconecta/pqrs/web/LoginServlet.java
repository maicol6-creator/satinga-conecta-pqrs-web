package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.modelo.Usuario;
import co.satingaconecta.pqrs.util.Seguridad;
import co.satingaconecta.pqrs.util.Validacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** GET /login muestra el formulario; POST /login valida credenciales e inicia sesión. */
@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (usuarioActual(req) != null) { redirigir(req, resp, "/inicio"); return; }
        req.setAttribute("layout", "auth");
        vista(req, resp, "login.jsp", "Iniciar sesión");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!csrfValido(req, resp)) return;
        String usuario = Validacion.limpiar(req.getParameter("usuario")).toLowerCase();
        String clave = req.getParameter("clave");
        Usuario u = usuarioDao().buscar(usuario).orElse(null);
        // Mismo mensaje para "usuario inexistente" y "clave incorrecta": no revela qué usuarios existen.
        if (u == null || !Seguridad.verificar(clave, u.getClaveHash())) {
            req.setAttribute("error", "Usuario o contraseña incorrectos.");
            req.setAttribute("usuarioIngresado", usuario);
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            req.setAttribute("layout", "auth");
            vista(req, resp, "login.jsp", "Iniciar sesión");
            return;
        }
        req.getSession().invalidate();                   // evita fijación de sesión
        req.getSession(true).setAttribute(SESION_USUARIO, u);
        redirigir(req, resp, "/inicio");
    }
}
