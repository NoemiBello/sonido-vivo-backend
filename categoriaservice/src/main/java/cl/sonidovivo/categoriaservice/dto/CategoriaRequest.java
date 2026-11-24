package cl.sonidovivo.categoriaservice.dto;

import jakarta.validation.constraints.*;

public record CategoriaRequest(
    @NotBlank @Size(max = 100) String nombre,
    @Size(max = 500) String descripcion,
    @Size(max = 500) @Pattern(regexp = "^$|^/[^\\s]*$|^https://[^\\s]+$") String imagen) {}
