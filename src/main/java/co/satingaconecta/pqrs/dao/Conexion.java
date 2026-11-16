package co.satingaconecta.pqrs.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Fábrica de conexiones JDBC (MySQL en producción, H2 modo MySQL en pruebas). */
public class Conexion {
    private final String url;
    private final String usuario;
    private final String clave;

    public Conexion(String url, String usuario, String clave) {
        this.url = url; this.usuario = usuario; this.clave = clave;
    }

    public Connection abrir() throws SQLException {
        return DriverManager.getConnection(url, usuario, clave);
    }

    /** Ejecuta /db/esquema.sql (CREATE TABLE IF NOT EXISTS) para tener las tablas listas. */
    public void crearEsquema() {
        try (InputStream in = Conexion.class.getResourceAsStream("/db/esquema.sql")) {
            if (in == null) throw new IllegalStateException("No se encontró /db/esquema.sql");
            String script = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Connection c = abrir(); Statement st = c.createStatement()) {
                for (String sentencia : script.split(";")) {
                    if (!sentencia.isBlank()) st.execute(sentencia);
                }
            }
        } catch (IOException | SQLException e) {
            throw new IllegalStateException("No se pudo crear el esquema: " + e.getMessage(), e);
        }
    }
}
