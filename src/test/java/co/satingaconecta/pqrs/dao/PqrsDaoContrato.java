package co.satingaconecta.pqrs.dao;

import static org.junit.jupiter.api.Assertions.*;

import co.satingaconecta.pqrs.modelo.Estado;
import co.satingaconecta.pqrs.modelo.Pqrs;
import co.satingaconecta.pqrs.modelo.Tipo;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Las dos implementaciones (memoria y JDBC) deben comportarse igual. */
abstract class PqrsDaoContrato {
    abstract PqrsDao dao();

    private Pqrs nueva(String ciudadano, String asunto) {
        return dao().crear(new Pqrs(Tipo.QUEJA, asunto, "Descripción suficientemente larga para la prueba.", "Centro", ciudadano));
    }

    @Test void creaConRadicadoYEstadoInicial() {
        Pqrs p = nueva("ana", "Falla en el agua");
        assertTrue(p.getId() > 0);
        assertTrue(p.getRadicado().matches("PQ-\\d{4}-\\d{4}"), p.getRadicado());
        Pqrs leida = dao().buscarPorRadicado(p.getRadicado()).orElseThrow();
        assertEquals("Falla en el agua", leida.getAsunto());
        assertEquals(Estado.RADICADA, leida.getEstado());
        assertEquals(Tipo.QUEJA, leida.getTipo());
    }

    @Test void radicadosSonUnicos() {
        assertNotEquals(nueva("ana", "Uno uno").getRadicado(), nueva("ana", "Dos dos").getRadicado());
    }

    @Test void buscaSinImportarMayusculasYNoExistente() {
        Pqrs p = nueva("ana", "Consulta");
        assertTrue(dao().buscarPorRadicado(" " + p.getRadicado().toLowerCase() + " ").isPresent());
        assertTrue(dao().buscarPorRadicado("PQ-0000-9999").isEmpty());
        assertTrue(dao().buscarPorRadicado(null).isEmpty());
    }

    @Test void listaPorCiudadanoSoloLasSuyas() {
        nueva("luis", "De luis uno"); nueva("luis", "De luis dos"); nueva("rosa", "De rosa");
        assertEquals(2, dao().listarPorCiudadano("luis").size());
        assertEquals("De luis dos", dao().listarPorCiudadano("luis").get(0).getAsunto()); // más reciente primero
    }

    @Test void respondeYFiltraPorEstado() {
        Pqrs p = nueva("ana", "Para responder");
        assertTrue(dao().responder(p.getRadicado(), "Ya enviamos una cuadrilla.", Estado.RESPONDIDA, "laura"));
        Pqrs r = dao().buscarPorRadicado(p.getRadicado()).orElseThrow();
        assertEquals(Estado.RESPONDIDA, r.getEstado());
        assertEquals("laura", r.getRespondidoPor());
        assertTrue(r.isRespondida());
        assertNotNull(r.getFechaRespuesta());
        assertTrue(dao().listarTodas(Estado.RESPONDIDA).stream().anyMatch(x -> x.getRadicado().equals(p.getRadicado())));
        assertTrue(dao().listarTodas(Estado.CERRADA).stream().noneMatch(x -> x.getRadicado().equals(p.getRadicado())));
        assertFalse(dao().responder("PQ-0000-9999", "x", Estado.CERRADA, "laura"));
    }

    @Test void cuentaPorEstado() {
        Pqrs p = nueva("ana", "Contar uno"); nueva("ana", "Contar dos");
        dao().responder(p.getRadicado(), "Respuesta de prueba", Estado.EN_TRAMITE, "laura");
        Map<Estado, Long> c = dao().contarPorEstado();
        assertEquals(1L, c.get(Estado.EN_TRAMITE));
        assertEquals(1L, c.get(Estado.RADICADA));
        assertEquals(0L, c.get(Estado.CERRADA));
    }

    @Test void guardaTextoConInyeccionSqlComoTextoLiteral() {
        Pqrs p = dao().crear(new Pqrs(Tipo.PETICION, "x'); DROP TABLE pqrs;--", "Descripción suficientemente larga para la prueba.", "ñandú", "ana"));
        Pqrs r = dao().buscarPorRadicado(p.getRadicado()).orElseThrow();
        assertEquals("x'); DROP TABLE pqrs;--", r.getAsunto());
        assertEquals("ñandú", r.getBarrio());
    }
}
