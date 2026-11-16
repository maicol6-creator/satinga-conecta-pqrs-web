package co.satingaconecta.pqrs.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Contraseñas con PBKDF2 (solo JDK) y tokens aleatorios para CSRF. */
public final class Seguridad {
    private static final int ITERACIONES = 120_000;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private Seguridad() { }

    public static String hashear(String clave) {
        byte[] sal = new byte[16];
        ALEATORIO.nextBytes(sal);
        return ITERACIONES + ":" + Base64.getEncoder().encodeToString(sal) + ":"
                + Base64.getEncoder().encodeToString(derivar(clave, sal, ITERACIONES));
    }

    public static boolean verificar(String clave, String almacenado) {
        if (clave == null || almacenado == null) return false;
        String[] partes = almacenado.split(":");
        if (partes.length != 3) return false;
        byte[] esperado = Base64.getDecoder().decode(partes[2]);
        byte[] calculado = derivar(clave, Base64.getDecoder().decode(partes[1]), Integer.parseInt(partes[0]));
        return MessageDigest.isEqual(esperado, calculado);   // comparación en tiempo constante
    }

    public static String tokenAleatorio() {
        byte[] b = new byte[24];
        ALEATORIO.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    public static boolean tokensIguales(String a, String b) {
        return a != null && b != null && MessageDigest.isEqual(a.getBytes(), b.getBytes());
    }

    private static byte[] derivar(String clave, byte[] sal, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(clave.toCharArray(), sal, iteraciones, 256);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }
}
