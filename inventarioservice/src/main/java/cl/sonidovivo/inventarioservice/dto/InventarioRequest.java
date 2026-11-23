package cl.sonidovivo.inventarioservice.dto;

import jakarta.validation.constraints.*;

public record InventarioRequest(
    @NotNull @Positive Long productoId,
    @NotNull @Min(0) Integer stock,
    @NotNull @Min(0) Integer stockCritico) {}
