package co.satingaconecta.pqrs.dao;

import java.time.LocalDateTime;

/** Formato del número de radicado: PQ-AAAA-0001. */
final class Radicados {
    private Radicados() { }

    static String de(LocalDateTime fecha, long id) {
        return String.format("PQ-%d-%04d", fecha.getYear(), id);
    }
}
