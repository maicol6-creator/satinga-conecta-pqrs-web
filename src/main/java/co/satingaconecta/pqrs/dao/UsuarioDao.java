package co.satingaconecta.pqrs.dao;

import co.satingaconecta.pqrs.modelo.Usuario;
import java.util.Optional;

public interface UsuarioDao {
    Optional<Usuario> buscar(String usuario);

    /** @return false si el nombre de usuario ya existe. */
    boolean registrar(Usuario usuario);
}
