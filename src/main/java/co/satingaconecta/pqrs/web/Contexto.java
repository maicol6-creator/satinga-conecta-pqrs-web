package co.satingaconecta.pqrs.web;

import co.satingaconecta.pqrs.dao.Conexion;
import co.satingaconecta.pqrs.dao.PqrsDao;
import co.satingaconecta.pqrs.dao.PqrsDaoJdbc;
import co.satingaconecta.pqrs.dao.PqrsDaoMemoria;
import co.satingaconecta.pqrs.dao.UsuarioDao;
import co.satingaconecta.pqrs.dao.UsuarioDaoJdbc;
import co.satingaconecta.pqrs.dao.UsuarioDaoMemoria;
import co.satingaconecta.pqrs.modelo.Pqrs;
import co.satingaconecta.pqrs.modelo.Rol;
import co.satingaconecta.pqrs.modelo.Tipo;
import co.satingaconecta.pqrs.modelo.Usuario;
import co.satingaconecta.pqrs.util.Seguridad;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Se ejecuta al arrancar la aplicación: elige la implementación de DAO y siembra datos de demostración.
 * Modo "memoria" (por defecto) o "jdbc" con -Dsatinga.modo=jdbc -Dsatinga.jdbc.url=... (ver README).
 */
@WebListener
public class Contexto implements ServletContextListener {
    public static final String PQRS_DAO = "pqrsDao";
    public static final String USUARIO_DAO = "usuarioDao";

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        ServletContext ctx = evento.getServletContext();
        PqrsDao pqrs;
        UsuarioDao usuarios;
        if ("jdbc".equalsIgnoreCase(propiedad(ctx, "satinga.modo", "memoria"))) {
            Conexion c = new Conexion(
                    propiedad(ctx, "satinga.jdbc.url", "jdbc:mysql://localhost:3306/satinga_conecta?useUnicode=true&characterEncoding=UTF-8"),
                    propiedad(ctx, "satinga.jdbc.usuario", "root"),
                    propiedad(ctx, "satinga.jdbc.clave", ""));
            c.crearEsquema();
            pqrs = new PqrsDaoJdbc(c);
            usuarios = new UsuarioDaoJdbc(c);
        } else {
            pqrs = new PqrsDaoMemoria();
            usuarios = new UsuarioDaoMemoria();
        }
        ctx.setAttribute(PQRS_DAO, pqrs);
        ctx.setAttribute(USUARIO_DAO, usuarios);
        sembrar(pqrs, usuarios);
    }

    private static String propiedad(ServletContext ctx, String nombre, String defecto) {
        String v = System.getProperty(nombre);
        if (v == null) v = ctx.getInitParameter(nombre);
        return v == null ? defecto : v;
    }

    private static void sembrar(PqrsDao pqrs, UsuarioDao usuarios) {
        if (usuarios.buscar("maicol").isPresent()) return;   // ya sembrado (modo JDBC)
        usuarios.registrar(new Usuario("maicol", "Maicol Caicedo", Rol.CIUDADANO, Seguridad.hashear("Satinga2026")));
        usuarios.registrar(new Usuario("laura", "Laura Quiñones (Alcaldía)", Rol.FUNCIONARIO, Seguridad.hashear("Funcionario2026")));
        pqrs.crear(new Pqrs(Tipo.PETICION, "Alumbrado público en el muelle",
                "Solicito la revisión de las lámparas del muelle principal; llevan varias noches apagadas y es riesgoso abordar las lanchas.",
                "Muelle principal", "maicol"));
        pqrs.crear(new Pqrs(Tipo.QUEJA, "Demora en la recolección de basuras",
                "En el barrio Pueblo Nuevo la recolección no pasa desde hace dos semanas y se acumulan residuos cerca de las viviendas.",
                "Pueblo Nuevo", "maicol"));
    }
}
