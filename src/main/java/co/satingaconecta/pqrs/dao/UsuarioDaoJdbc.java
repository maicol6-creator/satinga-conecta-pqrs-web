package co.satingaconecta.pqrs.dao;

import co.satingaconecta.pqrs.modelo.Rol;
import co.satingaconecta.pqrs.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsuarioDaoJdbc implements UsuarioDao {
    private final Conexion conexion;

    public UsuarioDaoJdbc(Conexion conexion) { this.conexion = conexion; }

    @Override
    public Optional<Usuario> buscar(String usuario) {
        String sql = "SELECT usuario, nombre, rol, clave_hash FROM usuarios WHERE usuario = ?";
        try (Connection c = conexion.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuario == null ? "" : usuario.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Usuario(rs.getString(1), rs.getString(2),
                        Rol.valueOf(rs.getString(3)), rs.getString(4)));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean registrar(Usuario u) {
        if (buscar(u.getUsuario()).isPresent()) return false;
        String sql = "INSERT INTO usuarios (usuario, nombre, rol, clave_hash) VALUES (?,?,?,?)";
        try (Connection c = conexion.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario().toLowerCase());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getRol().name());
            ps.setString(4, u.getClaveHash());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (buscar(u.getUsuario()).isPresent()) return false; // carrera: otro hilo lo creó
            throw new IllegalStateException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }
}
