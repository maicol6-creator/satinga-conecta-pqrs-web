package co.satingaconecta.pqrs.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/** Reglas de validación del lado servidor (el HTML5 solo es una ayuda para el usuario). */
public final class Validacion {
    private static final Pattern USUARIO = Pattern.compile("^[a-z0-9._]{3,30}$");

    private Validacion() { }

    public static String limpiar(String s) { return s == null ? "" : s.trim(); }

    public static boolean usuarioValido(String u) { return USUARIO.matcher(u).matches(); }

    public static boolean longitud(String s, int min, int max) { return s.length() >= min && s.length() <= max; }

    /** Mapa campo → mensaje, para pintar el error junto a cada campo. */
    public static Map<String, String> errores() { return new LinkedHashMap<>(); }
}
