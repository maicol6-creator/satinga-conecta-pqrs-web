package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.dao.PqrsDao;
import co.satingaconecta.pqrs.dao.UsuarioDao;
import co.satingaconecta.pqrs.modelo.Usuario;
import co.satingaconecta.pqrs.util.Seguridad;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Utilidades comunes: acceso a DAO, sesión, token CSRF, mensajes flash y despacho a las vistas JSP. */
public abstract class BaseServlet extends HttpServlet {
    public static final String SESION_USUARIO = "usuario";
    public static final String SESION_CSRF = "csrf";
    public static final String FLASH = "flash";
    public static final String FLASH_TIPO = "flashTipo";

    protected PqrsDao pqrsDao() { return (PqrsDao) getServletContext().getAttribute(Contexto.PQRS_DAO); }

    protected UsuarioDao usuarioDao() { return (UsuarioDao) getServletContext().getAttribute(Contexto.USUARIO_DAO); }

    protected Usuario usuarioActual(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (Usuario) s.getAttribute(SESION_USUARIO);
    }

    /** Token CSRF de la sesión (se crea la primera vez). Los formularios POST lo envían como campo oculto. */
    protected String tokenCsrf(HttpServletRequest req) {
        HttpSession s = req.getSession(true);
        String t = (String) s.getAttribute(SESION_CSRF);
        if (t == null) { t = Seguridad.tokenAleatorio(); s.setAttribute(SESION_CSRF, t); }
        return t;
    }

    /** @return true si el POST trae un token válido; si no, responde 403 y devuelve false. */
    protected boolean csrfValido(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        String esperado = s == null ? null : (String) s.getAttribute(SESION_CSRF);
        if (!Seguridad.tokensIguales(esperado, req.getParameter("csrf"))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solicitud no válida (token de seguridad ausente o vencido).");
            return false;
        }
        return true;
    }

    protected void flash(HttpServletRequest req, String mensaje, String tipo) {
        HttpSession s = req.getSession(true);
        s.setAttribute(FLASH, mensaje);
        s.setAttribute(FLASH_TIPO, tipo);
    }

    /** Redirección relativa a la aplicación (patrón Post/Redirect/Get). */
    protected void redirigir(HttpServletRequest req, HttpServletResponse resp, String ruta) throws IOException {
        resp.sendRedirect(req.getContextPath() + ruta);
    }

    /** Hace forward a una vista JSP protegida en /WEB-INF/vistas, con el mensaje flash y el token CSRF listos. */
    protected void vista(HttpServletRequest req, HttpServletResponse resp, String jsp, String titulo)
            throws ServletException, IOException {
        HttpSession s = req.getSession(true);
        Object msg = s.getAttribute(FLASH);
        if (msg != null) {
            req.setAttribute(FLASH, msg);
            req.setAttribute(FLASH_TIPO, s.getAttribute(FLASH_TIPO));
            s.removeAttribute(FLASH);
            s.removeAttribute(FLASH_TIPO);
        }
        req.setAttribute("csrf", tokenCsrf(req));
        req.setAttribute("titulo", titulo);
        req.setAttribute("usuarioActual", usuarioActual(req));
        req.getRequestDispatcher("/WEB-INF/vistas/" + jsp).forward(req, resp);
    }
}
