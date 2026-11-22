package cl.sonidovivo.authservice.dto;

import jakarta.validation.constraints.NotBlank;

// El administrador cambia el rol y, si lo envía, el estado activo.
public record ActualizarCredencialRequest(
  @NotBlank String rol,
  Boolean activo
) {}
