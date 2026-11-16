package co.satingaconecta.pqrs.modelo;

/** Clasificación de la solicitud ciudadana: Petición, Queja, Reclamo o Sugerencia. */
public enum Tipo {
    PETICION("Petición"), QUEJA("Queja"), RECLAMO("Reclamo"), SUGERENCIA("Sugerencia");

    private final String etiqueta;

    Tipo(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }

    /** @return el tipo o {@code null} si el texto no corresponde a ninguno. */
    public static Tipo desde(String texto) {
        if (texto == null) return null;
        try { return valueOf(texto.trim().toUpperCase()); } catch (IllegalArgumentException e) { return null; }
    }
}
