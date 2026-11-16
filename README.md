# Satinga Conecta – Módulo PQRS (Servlets + JSP)

**Evidencia:** GA7-220501096-AA2-EV02 · *Módulos de software codificados y probados*
**Programa:** Análisis y Desarrollo de Software (ADSI) – SENA · **Aprendiz:** Maicol Caicedo
**Proyecto:** Satinga Conecta, plataforma ciudadana de Bocas de Satinga (Olaya Herrera, Nariño)

Módulo web de **Peticiones, Quejas, Reclamos y Sugerencias (PQRS)** codificado con **Servlets** y **JSP** (Jakarta EE 10, Tomcat 10.1, Java 17+), siguiendo el patrón MVC y los artefactos previos del proyecto (casos de uso "radicar PQRS", "consultar estado" y "responder PQRS"; interfaz diseñada en la AA3-EV03).

## Lista de chequeo de la guía

| Criterio | Dónde se cumple |
|---|---|
| Código con **formularios HTML con servlets** | `login.jsp`, `registro.jsp`, `formulario.jsp`, `detalle.jsp`, `lista.jsp`, `consulta.jsp` → servlets `LoginServlet`, `RegistroServlet`, `NuevaPqrsServlet`, `ResponderServlet`, `ListaPqrsServlet`, `ConsultaServlet` |
| Uso de métodos **GET y POST** | Ver tabla siguiente |
| Uso de **elementos JSP** | Ver tabla de elementos JSP |
| **Herramienta de versionamiento** | Repositorio **Git** con commits por etapa (`git log --oneline`); enlace en `REPOSITORIO.txt` |

### GET y POST

| Método | URL | Servlet | Para qué |
|---|---|---|---|
| GET | `/login`, `/registro`, `/pqrs/nueva` | Login / Registro / NuevaPqrs | Mostrar formularios |
| **POST** | `/login` | `LoginServlet` | Autenticar (credenciales no viajan en la URL) |
| **POST** | `/registro` | `RegistroServlet` | Crear cuenta |
| **POST** | `/pqrs/nueva` | `NuevaPqrsServlet` | Radicar PQRS (responde con redirección *Post/Redirect/Get*) |
| **POST** | `/gestion/responder` | `ResponderServlet` | Funcionario responde y cambia estado |
| **POST** | `/salir` | `SalirServlet` | Cerrar sesión |
| **GET** + parámetros | `/pqrs?estado=RADICADA` | `ListaPqrsServlet` | Filtrar listado (consulta, no modifica datos) |
| **GET** + parámetros | `/pqrs/detalle?radicado=PQ-2026-0001` | `DetallePqrsServlet` | Ver una PQRS |
| **GET** + parámetros | `/consulta?radicado=PQ-2026-0001` | `ConsultaServlet` | Consulta pública del estado, sin sesión |

Regla aplicada: **GET** para leer/consultar (idempotente, URL compartible) y **POST** para crear o modificar datos y enviar contraseñas.

### Elementos JSP utilizados

| Elemento | Ejemplo | Archivo |
|---|---|---|
| Directiva `page` | `<%@ page contentType="text/html;charset=UTF-8" %>` | todas las vistas |
| Directiva `include` | `<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>` | todas las vistas |
| Directiva `taglib` | `<%@ taglib prefix="c" uri="jakarta.tags.core" %>` | `cabecera.jspf`, `pie.jsp`, `error.jsp` |
| Declaración | `<%! private static final String ENTIDAD = ...; %>` | `pie.jsp` |
| Scriptlet | `<% String estadoSesion = ...; %>` / suma de conteos | `pie.jsp`, `inicio.jsp` |
| Expresión | `<%= ENTIDAD %>`, `<%= java.time.Year.now() %>` | `pie.jsp` |
| Comentario JSP | `<%-- ... --%>` | varias |
| Acción `jsp:include` + `jsp:param` | pie de página con parámetro | todas las vistas |
| Acción `jsp:forward` | `index.jsp` → `/inicio` | `index.jsp` |
| Acciones `jsp:useBean` / `jsp:getProperty` | JavaBean `Reloj` | `inicio.jsp` |
| EL y JSTL (`c:out`, `c:if`, `c:choose`, `c:forEach`, `c:url`, `c:param`, `fn:length`) | `${usuarioActual.nombre}` | todas |

## Arquitectura (MVC)

```
Navegador ──HTTP──► Filtros (UTF-8, autenticación)
                     │
                     ▼
             Servlets (controlador) ──► DAO (modelo) ──► Memoria  |  MySQL (JDBC)
                     │
                     ▼  forward
             JSP en /WEB-INF/vistas (vista, no accesibles directamente)
```

* `modelo/` Pqrs, Usuario, Tipo, Estado, Rol · `dao/` interfaz + implementación en memoria + JDBC · `web/` servlets y filtros · `util/` seguridad y validación.
* Seguridad: contraseñas con PBKDF2, token **CSRF** en cada formulario POST, salida escapada con `c:out` (anti-XSS), sentencias preparadas (anti inyección SQL), cambio de sesión al iniciar (anti fijación), roles ciudadano/funcionario, cabeceras de seguridad.
* Validación en el servidor (los atributos HTML5 son solo ayuda) y mensajes junto a cada campo.

## Cómo ejecutarlo

Requisitos: **JDK 17+**, **Maven 3.9+**.

```bash
mvn test                 # 31 pruebas: DAO, seguridad y flujo HTTP completo con Tomcat embebido
mvn package              # genera target/satinga-pqrs.war
mvn cargo:run            # Tomcat 10 embebido -> http://localhost:8080/satinga-pqrs/
```

También se puede copiar `target/satinga-pqrs.war` a la carpeta `webapps` de un **Tomcat 10.1** (no sirve Tomcat 9: usa `jakarta.*`).

**Usuarios de demostración** (modo memoria, ya sembrados): `maicol / Satinga2026` (ciudadano) · `laura / Funcionario2026` (funcionario).

### Con MySQL (XAMPP / Workbench)

1. Inicia MySQL y (opcional) ejecuta `database/satinga_pqrs.sql`; la app crea las tablas sola si no existen.
2. Arranca con: `mvn cargo:run -Dsatinga.modo=jdbc -Dsatinga.jdbc.url="jdbc:mysql://localhost:3306/satinga_conecta" -Dsatinga.jdbc.usuario=root -Dsatinga.jdbc.clave=`
   (o define esos valores como `context-param` en `web.xml`).

## Pruebas realizadas

* `PqrsDaoMemoriaTest` y `PqrsDaoJdbcTest` (H2 en modo MySQL) comparten el mismo contrato: radicado único, búsqueda, filtros, respuesta, conteos y texto con comillas/`DROP TABLE` guardado como texto.
* `FlujoWebTest` levanta Tomcat con los servlets y JSP reales y prueba por HTTP: login correcto/incorrecto, POST sin CSRF (403), rutas protegidas (302), radicar, validación (400), XSS escapado, permisos por rol (403/404), respuesta del funcionario, consulta pública GET, cierre de sesión por POST y `jsp:forward`.

## Versionamiento

```bash
git log --oneline          # historial por etapas
git remote add origin https://github.com/<usuario>/satinga-conecta-pqrs.git
git push -u origin main
```
