package cl.sonidovivo.productosservice.dto;

import jakarta.validation.constraints.*;

public record ProductoRequest(
    @NotBlank @Size(min = 3, max = 40) String codigo,
    @NotBlank @Size(max = 100) String nombre,
    @Size(max = 500) String descripcion,
    @Size(max = 500) @Pattern(regexp = "^$|^/[^\\s]*$|^https://[^\\s]+$") String imagen,
    @NotNull @Positive Long categoriaId,
    @NotNull @DecimalMin("0.01") @Digits(integer = 12, fraction = 2) java.math.BigDecimal precio,
    @NotNull @Min(0) @Max(100) Integer descuento) {}
