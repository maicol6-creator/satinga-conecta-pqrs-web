package co.satingaconecta.pqrs.dao;

import co.satingaconecta.pqrs.modelo.Estado;
import co.satingaconecta.pqrs.modelo.Pqrs;
import co.satingaconecta.pqrs.modelo.Tipo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Implementación JDBC con sentencias preparadas (evita inyección SQL). */
public class PqrsDaoJdbc implements PqrsDao {
    private static final String COLUMNAS =
            "id, radicado, tipo, asunto, descripcion, barrio, ciudadano, fecha, estado, respuesta, fecha_respuesta, respondido_por";

    private final Conexion conexion;

    public PqrsDaoJdbc(Conexion conexion) { this.conexion = conexion; }

    @Override
    public Pqrs crear(Pqrs p) {
        String sql = "INSERT INTO pqrs (radicado, tipo, asunto, descripcion, barrio, ciudadano, fecha, estado) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = conexion.abrir()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "TMP-" + System.nanoTime());   // se reemplaza con el radicado definitivo
                ps.setString(2, p.getTipo().name());
                ps.setString(3, p.getAsunto());
                ps.setString(4, p.getDescripcion());
                ps.setString(5, p.getBarrio());
                ps.setString(6, p.getCiudadano());
                ps.setTimestamp(7, Timestamp.valueOf(p.getFecha()));
                ps.setString(8, p.getEstado().name());
                ps.executeUpdate();
                try (ResultSet k = ps.getGeneratedKeys()) {
                    k.next();
                    p.setId(k.getLong(1));
                }
            }
            p.setRadicado(Radicados.de(p.getFecha(), p.getId()));
            try (PreparedStatement ps = c.prepareStatement("UPDATE pqrs SET radicado = ? WHERE id = ?")) {
                ps.setString(1, p.getRadicado());
                ps.setLong(2, p.getId());
                ps.executeUpdate();
            }
            c.commit();
            return p;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo radicar la PQRS: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Pqrs> buscarPorRadicado(String radicado) {
        List<Pqrs> r = consultar("SELECT " + COLUMNAS + " FROM pqrs WHERE radicado = ?",
                radicado == null ? "" : radicado.trim().toUpperCase());
        return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
    }

    @Override
    public List<Pqrs> listarPorCiudadano(String ciudadano) {
        return consultar("SELECT " + COLUMNAS + " FROM pqrs WHERE ciudadano = ? ORDER BY id DESC", ciudadano);
    }

    @Override
    public List<Pqrs> listarTodas(Estado estado) {
        if (estado == null) return consultar("SELECT " + COLUMNAS + " FROM pqrs ORDER BY id DESC");
        return consultar("SELECT " + COLUMNAS + " FROM pqrs WHERE estado = ? ORDER BY id DESC", estado.name());
    }

    @Override
    public boolean responder(String radicado, String respuesta, Estado nuevoEstado, String funcionario) {
        String sql = "UPDATE pqrs SET respuesta = ?, estado = ?, respondido_por = ?, fecha_respuesta = ? WHERE radicado = ?";
        try (Connection c = conexion.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, respuesta);
            ps.setString(2, nuevoEstado.name());
            ps.setString(3, funcionario);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(5, radicado == null ? "" : radicado.trim().toUpperCase());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo responder la PQRS: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<Estado, Long> contarPorEstado() {
        Map<Estado, Long> m = new EnumMap<>(Estado.class);
        for (Estado e : Estado.values()) m.put(e, 0L);
        try (Connection c = conexion.abrir(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT estado, COUNT(*) FROM pqrs GROUP BY estado")) {
            while (rs.next()) {
                Estado e = Estado.desde(rs.getString(1));
                if (e != null) m.put(e, rs.getLong(2));
            }
            return m;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo contar: " + e.getMessage(), e);
        }
    }

    private List<Pqrs> consultar(String sql, String... parametros) {
        try (Connection c = conexion.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) ps.setString(i + 1, parametros[i]);
            try (ResultSet rs = ps.executeQuery()) {
                List<Pqrs> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error de consulta: " + e.getMessage(), e);
        }
    }

    private static Pqrs mapear(ResultSet rs) throws SQLException {
        Pqrs p = new Pqrs();
        p.setId(rs.getLong("id"));
        p.setRadicado(rs.getString("radicado"));
        p.setTipo(Tipo.desde(rs.getString("tipo")));
        p.setAsunto(rs.getString("asunto"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setBarrio(rs.getString("barrio"));
        p.setCiudadano(rs.getString("ciudadano"));
        p.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        p.setEstado(Estado.desde(rs.getString("estado")));
        p.setRespuesta(rs.getString("respuesta"));
        Timestamp fr = rs.getTimestamp("fecha_respuesta");
        p.setFechaRespuesta(fr == null ? null : fr.toLocalDateTime());
        p.setRespondidoPor(rs.getString("respondido_por"));
        return p;
    }
}
