package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.modelo.Estado;
import co.satingaconecta.pqrs.modelo.Pqrs;
import co.satingaconecta.pqrs.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Panel de inicio: el ciudadano ve el resumen de sus PQRS; el funcionario, el de toda la plataforma. */
@WebServlet("/inicio")
public class InicioServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = usuarioActual(req);
        List<Pqrs> propias = u.isFuncionario() ? pqrsDao().listarTodas(null) : pqrsDao().listarPorCiudadano(u.getUsuario());
        Map<Estado, Long> conteo = new EnumMap<>(Estado.class);
        for (Estado e : Estado.values()) conteo.put(e, 0L);
        propias.forEach(p -> conteo.merge(p.getEstado(), 1L, Long::sum));
        req.setAttribute("conteo", conteo);
        req.setAttribute("ultimas", propias.stream().limit(5).toList());
        vista(req, resp, "inicio.jsp", "Mi panel");
    }
}
