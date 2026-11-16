package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.modelo.Rol;
import co.satingaconecta.pqrs.modelo.Usuario;
import co.satingaconecta.pqrs.util.Seguridad;
import co.satingaconecta.pqrs.util.Validacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/** Registro de ciudadanos: GET muestra el formulario, POST crea la cuenta. */
@WebServlet("/registro")
public class RegistroServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        vista(req, resp, "registro.jsp", "Crear cuenta");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!csrfValido(req, resp)) return;
        String nombre = Validacion.limpiar(req.getParameter("nombre"));
        String usuario = Validacion.limpiar(req.getParameter("usuario")).toLowerCase();
        String clave = req.getParameter("clave") == null ? "" : req.getParameter("clave");
        String confirmar = req.getParameter("confirmar") == null ? "" : req.getParameter("confirmar");

        Map<String, String> errores = Validacion.errores();
        if (!Validacion.longitud(nombre, 3, 80)) errores.put("nombre", "Escribe tu nombre completo (3 a 80 caracteres).");
        if (!Validacion.usuarioValido(usuario)) errores.put("usuario", "Usa de 3 a 30 caracteres: minúsculas, números, punto o guion bajo.");
        if (clave.length() < 8) errores.put("clave", "La contraseña debe tener al menos 8 caracteres.");
        else if (!clave.equals(confirmar)) errores.put("confirmar", "Las contraseñas no coinciden.");

        if (errores.isEmpty()
                && !usuarioDao().registrar(new Usuario(usuario, nombre, Rol.CIUDADANO, Seguridad.hashear(clave)))) {
            errores.put("usuario", "Ese nombre de usuario ya está en uso.");
        }
        if (!errores.isEmpty()) {
            req.setAttribute("errores", errores);
            req.setAttribute("nombre", nombre);
            req.setAttribute("usuarioIngresado", usuario);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            vista(req, resp, "registro.jsp", "Crear cuenta");
            return;
        }
        flash(req, "Cuenta creada. Ya puedes iniciar sesión.", "ok");
        redirigir(req, resp, "/login");
    }
}
