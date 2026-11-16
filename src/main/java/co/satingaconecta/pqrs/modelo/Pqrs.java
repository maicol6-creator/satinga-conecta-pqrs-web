package co.satingaconecta.pqrs.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Petición, queja, reclamo o sugerencia radicada por un ciudadano. */
public class Pqrs {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private long id;
    private String radicado;
    private Tipo tipo;
    private String asunto;
    private String descripcion;
    private String barrio;
    private String ciudadano;
    private LocalDateTime fecha;
    private Estado estado = Estado.RADICADA;
    private String respuesta;
    private LocalDateTime fechaRespuesta;
    private String respondidoPor;

    public Pqrs() { }

    public Pqrs(Tipo tipo, String asunto, String descripcion, String barrio, String ciudadano) {
        this.tipo = tipo; this.asunto = asunto; this.descripcion = descripcion;
        this.barrio = barrio; this.ciudadano = ciudadano; this.fecha = LocalDateTime.now();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getRadicado() { return radicado; }
    public void setRadicado(String radicado) { this.radicado = radicado; }
    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public String getCiudadano() { return ciudadano; }
    public void setCiudadano(String ciudadano) { this.ciudadano = ciudadano; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
    public LocalDateTime getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(LocalDateTime fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
    public String getRespondidoPor() { return respondidoPor; }
    public void setRespondidoPor(String respondidoPor) { this.respondidoPor = respondidoPor; }

    /** Fecha lista para mostrar en JSP con EL: {@code ${p.fechaTexto}}. */
    public String getFechaTexto() { return fecha == null ? "" : fecha.format(FORMATO); }
    public String getFechaRespuestaTexto() { return fechaRespuesta == null ? "" : fechaRespuesta.format(FORMATO); }
    public boolean isRespondida() { return respuesta != null && !respuesta.isBlank(); }
}
