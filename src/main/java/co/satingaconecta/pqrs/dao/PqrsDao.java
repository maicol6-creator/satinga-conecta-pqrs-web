package co.satingaconecta.pqrs.dao;

import co.satingaconecta.pqrs.modelo.Estado;
import co.satingaconecta.pqrs.modelo.Pqrs;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Acceso a datos de PQRS. Dos implementaciones: en memoria y JDBC (MySQL). */
public interface PqrsDao {
    /** Guarda la PQRS, le asigna id y número de radicado y la devuelve. */
    Pqrs crear(Pqrs pqrs);

    Optional<Pqrs> buscarPorRadicado(String radicado);

    List<Pqrs> listarPorCiudadano(String ciudadano);

    /** @param estado filtro opcional; {@code null} devuelve todas. */
    List<Pqrs> listarTodas(Estado estado);

    /** Registra la respuesta del funcionario y cambia el estado. @return false si no existe el radicado. */
    boolean responder(String radicado, String respuesta, Estado nuevoEstado, String funcionario);

    /** Conteo de PQRS por estado (para el panel de inicio). */
    Map<Estado, Long> contarPorEstado();
}
