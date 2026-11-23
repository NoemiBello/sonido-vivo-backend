package cl.sonidovivo.authservice.dto;

// Lo que se muestra al administrador: sin el hash de la contraseña.
public record CredencialResponse(
    Long id,
    String correo,
    String rol,
    boolean activo)
{}
