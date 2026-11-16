package co.satingaconecta.pqrs.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SeguridadTest {
    @Test void hashVerificaClaveCorrecta() {
        String h = Seguridad.hashear("Satinga2026");
        assertTrue(Seguridad.verificar("Satinga2026", h));
        assertFalse(Seguridad.verificar("otra", h));
        assertFalse(h.contains("Satinga2026"));
    }
    @Test void mismoTextoGeneraHashesDistintos() {
        assertNotEquals(Seguridad.hashear("abc12345"), Seguridad.hashear("abc12345"));
    }
    @Test void validaUsuarios() {
        assertTrue(Validacion.usuarioValido("maicol.c_1"));
        assertFalse(Validacion.usuarioValido("Ma icol"));
        assertFalse(Validacion.usuarioValido("ab"));
    }
}
