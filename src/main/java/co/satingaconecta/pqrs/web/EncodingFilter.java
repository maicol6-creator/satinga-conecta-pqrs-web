package co.satingaconecta.pqrs.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Fuerza UTF-8 (tildes y ñ en formularios POST) y cabeceras de seguridad básicas. */
@WebFilter("/*")
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        if (resp instanceof HttpServletResponse h) {
            h.setHeader("X-Content-Type-Options", "nosniff");
            h.setHeader("X-Frame-Options", "DENY");
            h.setHeader("Referrer-Policy", "same-origin");
        }
        chain.doFilter(req, resp);
    }
}
