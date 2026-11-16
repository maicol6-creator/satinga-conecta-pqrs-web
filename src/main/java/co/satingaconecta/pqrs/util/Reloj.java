package co.satingaconecta.pqrs.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** JavaBean usado desde JSP con {@code <jsp:useBean>} y {@code <jsp:getProperty>}. */
public class Reloj implements java.io.Serializable {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-CO"));

    private LocalDate fecha = LocalDate.now();

    public String getHoy() { return fecha.format(FORMATO); }

    public int getAnio() { return fecha.getYear(); }

    /** Permite {@code <jsp:setProperty name="reloj" property="fecha" value="2026-11-15"/>} (útil en pruebas). */
    public void setFecha(String iso) { this.fecha = LocalDate.parse(iso); }
}
