package cl.sonidovivo.authservice.dto;

import jakarta.validation.constraints.*;

// El administrador crea una cuenta eligiendo el rol (ADMIN, VENDEDOR o USER).
public record CredencialRequest(
  @NotBlank @Email String correo,
  @NotBlank String password,
  @NotBlank String rol
) {}
