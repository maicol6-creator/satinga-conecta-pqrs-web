package co.satingaconecta.pqrs.dao;

import co.satingaconecta.pqrs.modelo.Usuario;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UsuarioDaoMemoria implements UsuarioDao {
    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    @Override
    public Optional<Usuario> buscar(String usuario) {
        return Optional.ofNullable(usuario == null ? null : usuarios.get(usuario.toLowerCase()));
    }

    @Override
    public boolean registrar(Usuario u) {
        return usuarios.putIfAbsent(u.getUsuario().toLowerCase(), u) == null;
    }
}
