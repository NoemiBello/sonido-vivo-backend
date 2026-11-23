package cl.sonidovivo.serviciostecnicosservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CambioEstadoRequest(
  @NotBlank String estado
) {}
