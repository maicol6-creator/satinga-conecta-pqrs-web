package co.satingaconecta.pqrs.modelo;

/** Usuario de la plataforma (ciudadano o funcionario de la Alcaldía). */
public class Usuario {
    private final String usuario;
    private final String nombre;
    private final Rol rol;
    private final String claveHash;

    public Usuario(String usuario, String nombre, Rol rol, String claveHash) {
        this.usuario = usuario; this.nombre = nombre; this.rol = rol; this.claveHash = claveHash;
    }

    public String getUsuario() { return usuario; }
    public String getNombre() { return nombre; }
    public Rol getRol() { return rol; }
    public String getClaveHash() { return claveHash; }
    public boolean isFuncionario() { return rol == Rol.FUNCIONARIO; }
}
