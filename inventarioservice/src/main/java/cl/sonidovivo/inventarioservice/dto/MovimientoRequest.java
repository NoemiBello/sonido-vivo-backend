package cl.sonidovivo.inventarioservice.dto;

import jakarta.validation.constraints.*;

public record MovimientoRequest(@NotNull Integer cantidad) {}
