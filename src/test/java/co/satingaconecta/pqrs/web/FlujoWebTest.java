package co.satingaconecta.pqrs.web;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Prueba de integración: arranca Tomcat embebido con los servlets y JSP reales y navega por HTTP (GET y POST). */
class FlujoWebTest {
    static Tomcat tomcat;
    static String base;

    @BeforeAll static void arrancar() throws Exception {
        System.setProperty("satinga.modo", "memoria");
        File tmp = Files.createTempDirectory("tomcat-test").toFile();
        tomcat = new Tomcat();
        tomcat.setBaseDir(tmp.getAbsolutePath());
        tomcat.setPort(0);
        tomcat.getConnector();
        Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        ctx.setParentClassLoader(FlujoWebTest.class.getClassLoader());
        // Equivale a WEB-INF/classes del WAR: Tomcat escanea aquí las anotaciones @WebServlet, @WebFilter y @WebListener
        File clases = new File(Contexto.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        WebResourceRoot recursos = new StandardRoot(ctx);
        recursos.addPreResources(new DirResourceSet(recursos, "/WEB-INF/classes", clases.getAbsolutePath(), "/"));
        ctx.setResources(recursos);
        tomcat.start();
        base = "http://localhost:" + tomcat.getConnector().getLocalPort();
    }

    @AfterAll static void detener() throws Exception { tomcat.stop(); tomcat.destroy(); }

    /** Cliente con cookies (sesión) y sin seguir redirecciones, para poder verificar los 302. */
    static class Cliente {
        final HttpClient http = HttpClient.newBuilder().cookieHandler(new CookieManager())
                .followRedirects(HttpClient.Redirect.NEVER).build();

        HttpResponse<String> get(String ruta) throws Exception {
            return http.send(HttpRequest.newBuilder(URI.create(base + ruta)).GET().build(),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        }

        HttpResponse<String> post(String ruta, Map<String, String> campos) throws Exception {
            StringBuilder sb = new StringBuilder();
            campos.forEach((k, v) -> sb.append(sb.length() > 0 ? "&" : "")
                    .append(URLEncoder.encode(k, StandardCharsets.UTF_8)).append('=').append(URLEncoder.encode(v, StandardCharsets.UTF_8)));
            return http.send(HttpRequest.newBuilder(URI.create(base + ruta))
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(sb.toString())).build(),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        }

        /** Obtiene la página y extrae el token CSRF del formulario. */
        String csrf(String ruta) throws Exception {
            Matcher m = Pattern.compile("name=\"csrf\" value=\"([^\"]+)\"").matcher(get(ruta).body());
            assertTrue(m.find(), "la página " + ruta + " debe traer el token csrf");
            return m.group(1);
        }

        void ingresar(String usuario, String clave) throws Exception {
            Map<String, String> f = new LinkedHashMap<>();
            f.put("csrf", csrf("/login")); f.put("usuario", usuario); f.put("clave", clave);
            HttpResponse<String> r = post("/login", f);
            assertEquals(302, r.statusCode(), r.body());
        }
    }

    static String ubicacion(HttpResponse<?> r) { return r.headers().firstValue("Location").orElse(""); }

    @Test void paginaLoginUsaJspYFormularioPost() throws Exception {
        HttpResponse<String> r = new Cliente().get("/login");
        assertEquals(200, r.statusCode());
        assertTrue(r.headers().firstValue("Content-Type").orElse("").contains("UTF-8"));
        assertTrue(r.body().contains("method=\"post\" action=\"/login\""));
        assertTrue(r.body().contains("Alcaldía de Olaya Herrera"), "el pie JSP (declaración + expresión) debe renderizar");
        assertTrue(r.body().contains("Eres visitante"), "scriptlet y método declarado en el pie");
        assertTrue(r.body().contains("Acceso"), "jsp:param llega al pie");
        assertFalse(r.body().contains("Ã"), "los fragmentos .jspf también deben leerse en UTF-8");
        assertFalse(r.body().contains("jsessionid"), "no se reescriben las URL con jsessionid");
    }

    @Test void rutasProtegidasRedirigenALogin() throws Exception {
        Cliente c = new Cliente();
        for (String ruta : new String[]{"/inicio", "/pqrs", "/pqrs/nueva", "/pqrs/detalle?radicado=PQ-2026-0001"}) {
            HttpResponse<String> r = c.get(ruta);
            assertEquals(302, r.statusCode(), ruta);
            assertTrue(ubicacion(r).endsWith("/login"), ruta);
        }
    }

    @Test void vistasEnWebInfNoSonAccesiblesDirectamente() throws Exception {
        assertEquals(404, new Cliente().get("/WEB-INF/vistas/login.jsp").statusCode());
    }

    @Test void loginIncorrectoYSinCsrf() throws Exception {
        Cliente c = new Cliente();
        Map<String, String> f = new LinkedHashMap<>();
        f.put("usuario", "maicol"); f.put("clave", "Satinga2026");
        assertEquals(403, c.post("/login", f).statusCode(), "POST sin token CSRF debe rechazarse");

        f.put("csrf", c.csrf("/login")); f.put("clave", "mala");
        HttpResponse<String> r = c.post("/login", f);
        assertEquals(401, r.statusCode());
        assertTrue(r.body().contains("Usuario o contraseña incorrectos."));
    }

    @Test void ciudadanoRadicaYConsultaSuPqrs() throws Exception {
        Cliente c = new Cliente();
        c.ingresar("maicol", "Satinga2026");
        HttpResponse<String> inicio = c.get("/inicio");
        assertEquals(200, inicio.statusCode());
        assertTrue(inicio.body().contains("Hola, Maicol Caicedo"));
        assertTrue(inicio.body().contains("Mis PQRS radicadas"), "scriptlet + EL");
        assertTrue(inicio.body().contains("Hoy es"), "jsp:useBean / getProperty");

        assertTrue(c.get("/pqrs/nueva").body().contains("<form method=\"post\" action=\"/pqrs/nueva\""));

        Map<String, String> f = new LinkedHashMap<>();
        f.put("csrf", c.csrf("/pqrs/nueva")); f.put("tipo", "SUGERENCIA");
        f.put("asunto", "Más puntos de WiFi gratis"); f.put("barrio", "Ñapi"); 
        f.put("descripcion", "Sugiero instalar zonas WiFi en la plaza y en el colegio para estudiar.");
        HttpResponse<String> creada = c.post("/pqrs/nueva", f);
        assertEquals(302, creada.statusCode(), creada.body());
        String destino = ubicacion(creada);
        assertTrue(destino.contains("/pqrs/detalle?radicado=PQ-"), destino);

        HttpResponse<String> detalle = c.get(destino.replace(base, ""));
        assertEquals(200, detalle.statusCode());
        assertTrue(detalle.body().contains("Más puntos de WiFi gratis"));
        assertTrue(detalle.body().contains("Ñapi"), "UTF-8 ida y vuelta");
        assertTrue(detalle.body().contains("Tu solicitud fue radicada"), "mensaje flash tras redirección");
        assertFalse(c.get(destino.replace(base, "")).body().contains("Tu solicitud fue radicada"), "el flash se muestra una sola vez");

        assertTrue(c.get("/pqrs").body().contains("Más puntos de WiFi gratis"));
    }

    @Test void validacionDevuelve400ConErroresYConservaDatos() throws Exception {
        Cliente c = new Cliente();
        c.ingresar("maicol", "Satinga2026");
        Map<String, String> f = new LinkedHashMap<>();
        f.put("csrf", c.csrf("/pqrs/nueva")); f.put("tipo", ""); f.put("asunto", "abc"); f.put("descripcion", "corta");
        HttpResponse<String> r = c.post("/pqrs/nueva", f);
        assertEquals(400, r.statusCode());
        assertTrue(r.body().contains("Selecciona el tipo de solicitud."));
        assertTrue(r.body().contains("El asunto debe tener entre 5 y 120 caracteres."));
        assertTrue(r.body().contains("value=\"abc\""));
    }

    @Test void escapaHtmlParaEvitarXss() throws Exception {
        Cliente c = new Cliente();
        c.ingresar("maicol", "Satinga2026");
        Map<String, String> f = new LinkedHashMap<>();
        f.put("csrf", c.csrf("/pqrs/nueva")); f.put("tipo", "QUEJA");
        f.put("asunto", "<script>alert(1)</script> prueba"); f.put("descripcion", "Descripción con <img src=x onerror=alert(2)> suficiente largo.");
        HttpResponse<String> creada = c.post("/pqrs/nueva", f);
        String cuerpo = c.get(ubicacion(creada).replace(base, "")).body();
        assertFalse(cuerpo.contains("<script>alert(1)</script>"));
        assertFalse(cuerpo.contains("<img src=x"));
        assertTrue(cuerpo.contains("&lt;script&gt;"));
    }

    @Test void ciudadanoNoVePqrsAjenaNiGestiona() throws Exception {
        Cliente rosa = new Cliente();
        Map<String, String> reg = new LinkedHashMap<>();
        reg.put("csrf", rosa.csrf("/registro")); reg.put("nombre", "Rosa Mina"); reg.put("usuario", "rosa");
        reg.put("clave", "ClaveSegura1"); reg.put("confirmar", "ClaveSegura1");
        assertEquals(302, rosa.post("/registro", reg).statusCode());
        rosa.ingresar("rosa", "ClaveSegura1");

        assertEquals(403, rosa.get("/pqrs/detalle?radicado=PQ-" + java.time.Year.now() + "-0001").statusCode());
        assertEquals(404, rosa.get("/pqrs/detalle?radicado=PQ-0000-0000").statusCode());
        Map<String, String> r = new LinkedHashMap<>();
        r.put("csrf", rosa.csrf("/inicio")); r.put("radicado", "PQ-" + java.time.Year.now() + "-0001");
        r.put("estado", "CERRADA"); r.put("respuesta", "Intento no autorizado de respuesta.");
        assertEquals(403, rosa.post("/gestion/responder", r).statusCode());
        assertEquals(200, rosa.get("/pqrs/nueva").statusCode(), "el ciudadano sí puede radicar");
    }

    @Test void registroRechazaUsuarioDuplicado() throws Exception {
        Cliente c = new Cliente();
        Map<String, String> reg = new LinkedHashMap<>();
        reg.put("csrf", c.csrf("/registro")); reg.put("nombre", "Otro Maicol"); reg.put("usuario", "maicol");
        reg.put("clave", "ClaveSegura1"); reg.put("confirmar", "ClaveSegura1");
        HttpResponse<String> r = c.post("/registro", reg);
        assertEquals(400, r.statusCode());
        assertTrue(r.body().contains("Ese nombre de usuario ya está en uso."));
    }

    @Test void funcionarioResponde_yElCiudadanoLoVe() throws Exception {
        Cliente ciu = new Cliente();
        ciu.ingresar("maicol", "Satinga2026");
        Map<String, String> f = new LinkedHashMap<>();
        f.put("csrf", ciu.csrf("/pqrs/nueva")); f.put("tipo", "PETICION");
        f.put("asunto", "Arreglo del puente peatonal"); f.put("descripcion", "El puente de madera tiene tablas rotas y es peligroso para los niños.");
        String detalle = ubicacion(ciu.post("/pqrs/nueva", f)).replace(base, "");
        String radicado = detalle.substring(detalle.indexOf("PQ-"));

        Cliente fun = new Cliente();
        fun.ingresar("laura", "Funcionario2026");
        assertTrue(fun.get("/pqrs").body().contains("Arreglo del puente peatonal"), "el funcionario ve todas");
        assertTrue(fun.get("/pqrs?estado=RADICADA").body().contains(radicado), "filtro GET por estado");
        assertFalse(fun.get("/pqrs?estado=CERRADA").body().contains(radicado));
        assertTrue(fun.get(detalle).body().contains("Guardar respuesta"));
        assertEquals(403, fun.get("/pqrs/nueva").statusCode());

        Map<String, String> r = new LinkedHashMap<>();
        r.put("csrf", fun.csrf(detalle)); r.put("radicado", radicado); r.put("estado", "RESPONDIDA");
        r.put("respuesta", "Cuadrilla programada para el jueves.");
        assertEquals(302, fun.post("/gestion/responder", r).statusCode());

        String visto = ciu.get(detalle).body();
        assertTrue(visto.contains("Cuadrilla programada para el jueves."));
        assertTrue(visto.contains("Respondida"));
    }

    @Test void consultaPublicaPorGetSinSesion() throws Exception {
        Cliente c = new Cliente();
        HttpResponse<String> ok = c.get("/consulta?radicado=pq-" + java.time.Year.now() + "-0001");
        assertEquals(200, ok.statusCode());
        assertTrue(ok.body().contains("Petición") || ok.body().contains("Queja"));
        assertFalse(ok.body().contains("alumbrado"), "la consulta pública no muestra el contenido");
        assertTrue(c.get("/consulta?radicado=PQ-0000-0000").body().contains("No encontramos el radicado"));
        assertTrue(c.get("/consulta").body().contains("<form method=\"get\""));
    }

    @Test void cerrarSesionPorPostInvalidaLaSesion() throws Exception {
        Cliente c = new Cliente();
        c.ingresar("maicol", "Satinga2026");
        assertEquals(405, c.get("/salir").statusCode(), "GET /salir no cierra sesión");
        Map<String, String> f = new LinkedHashMap<>();
        f.put("csrf", c.csrf("/inicio"));
        assertEquals(302, c.post("/salir", f).statusCode());
        assertEquals(302, c.get("/pqrs").statusCode());
        assertTrue(ubicacion(c.get("/pqrs")).endsWith("/login"));
    }

    @Test void indexJspHaceForwardAInicio() throws Exception {
        HttpResponse<String> r = new Cliente().get("/");
        assertEquals(302, r.statusCode());
        assertTrue(ubicacion(r).endsWith("/login"));
    }
}
