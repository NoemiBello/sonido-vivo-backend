package cl.sonidovivo.authservice.dto;

import jakarta.validation.constraints.NotBlank;

// Token vigente que React envía para obtener uno nuevo antes de que expire.
public record RefreshRequest(
  @NotBlank String token
) {}
