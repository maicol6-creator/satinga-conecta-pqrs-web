package co.satingaconecta.pqrs.web;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Solo usuarios autenticados entran a /inicio, /pqrs/* y /gestion/*. */
@WebFilter(urlPatterns = {"/inicio", "/pqrs", "/pqrs/*", "/gestion/*", "/salir"},
           dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})   // FORWARD: también protege index.jsp -> /inicio
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute(BaseServlet.SESION_USUARIO) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        resp.setHeader("Cache-Control", "no-store");
        chain.doFilter(request, response);
    }
}
