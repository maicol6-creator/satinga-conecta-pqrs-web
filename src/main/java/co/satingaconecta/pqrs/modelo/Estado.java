package co.satingaconecta.pqrs.modelo;

/** Ciclo de vida de una PQRS. */
public enum Estado {
    RADICADA("Radicada", "i-ambar"), EN_TRAMITE("En trámite", "i-azul"),
    RESPONDIDA("Respondida", "i-verde"), CERRADA("Cerrada", "i-gris");

    private final String etiqueta;
    private final String clase;

    Estado(String etiqueta, String clase) { this.etiqueta = etiqueta; this.clase = clase; }

    public String getEtiqueta() { return etiqueta; }
    public String getClase() { return clase; }

    public static Estado desde(String texto) {
        if (texto == null) return null;
        try { return valueOf(texto.trim().toUpperCase()); } catch (IllegalArgumentException e) { return null; }
    }
}
