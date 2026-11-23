package cl.sonidovivo.authservice.dto;

// Respuesta de login, registro y refresh: el token y datos mínimos, nunca el hash.
public record LoginResponse(
    String token,
    String correo,
    String rol)
{}
