package co.satingaconecta.pqrs.dao;

import static org.junit.jupiter.api.Assertions.*;

import co.satingaconecta.pqrs.modelo.Rol;
import co.satingaconecta.pqrs.modelo.Usuario;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Prueba el DAO JDBC contra H2 en modo MySQL (mismas sentencias que en XAMPP/MySQL). */
class PqrsDaoJdbcTest extends PqrsDaoContrato {
    private PqrsDao dao;
    private Conexion conexion;

    @BeforeEach void preparar() {
        conexion = new Conexion("jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");
        conexion.crearEsquema();
        dao = new PqrsDaoJdbc(conexion);
    }

    @Override PqrsDao dao() { return dao; }

    @Test void usuariosJdbcRegistraYNoDuplica() {
        UsuarioDao u = new UsuarioDaoJdbc(conexion);
        assertTrue(u.registrar(new Usuario("Rosa", "Rosa Mina", Rol.CIUDADANO, "hash")));
        assertFalse(u.registrar(new Usuario("rosa", "Otra", Rol.CIUDADANO, "hash")));
        assertEquals(Rol.CIUDADANO, u.buscar("ROSA").orElseThrow().getRol());
        assertTrue(u.buscar("nadie").isEmpty());
    }
}
