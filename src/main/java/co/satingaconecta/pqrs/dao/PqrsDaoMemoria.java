package co.satingaconecta.pqrs.dao;

import co.satingaconecta.pqrs.modelo.Estado;
import co.satingaconecta.pqrs.modelo.Pqrs;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Implementación en memoria (por defecto): no requiere base de datos. */
public class PqrsDaoMemoria implements PqrsDao {
    private final Map<String, Pqrs> porRadicado = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    @Override
    public Pqrs crear(Pqrs pqrs) {
        long id = secuencia.incrementAndGet();
        pqrs.setId(id);
        pqrs.setRadicado(Radicados.de(pqrs.getFecha(), id));
        porRadicado.put(pqrs.getRadicado(), pqrs);
        return pqrs;
    }

    @Override
    public Optional<Pqrs> buscarPorRadicado(String radicado) {
        return Optional.ofNullable(radicado == null ? null : porRadicado.get(radicado.trim().toUpperCase()));
    }

    @Override
    public List<Pqrs> listarPorCiudadano(String ciudadano) {
        return porRadicado.values().stream().filter(p -> p.getCiudadano().equals(ciudadano))
                .sorted(Comparator.comparing(Pqrs::getId).reversed()).toList();
    }

    @Override
    public List<Pqrs> listarTodas(Estado estado) {
        return new ArrayList<>(porRadicado.values().stream()
                .filter(p -> estado == null || p.getEstado() == estado)
                .sorted(Comparator.comparing(Pqrs::getId).reversed()).toList());
    }

    @Override
    public synchronized boolean responder(String radicado, String respuesta, Estado nuevoEstado, String funcionario) {
        Pqrs p = porRadicado.get(radicado == null ? "" : radicado.trim().toUpperCase());
        if (p == null) return false;
        p.setRespuesta(respuesta);
        p.setEstado(nuevoEstado);
        p.setRespondidoPor(funcionario);
        p.setFechaRespuesta(LocalDateTime.now());
        return true;
    }

    @Override
    public Map<Estado, Long> contarPorEstado() {
        Map<Estado, Long> m = new EnumMap<>(Estado.class);
        for (Estado e : Estado.values()) m.put(e, 0L);
        porRadicado.values().forEach(p -> m.merge(p.getEstado(), 1L, Long::sum));
        return m;
    }
}
