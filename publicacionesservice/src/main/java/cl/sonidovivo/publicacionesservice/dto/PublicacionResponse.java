
package cl.sonidovivo.publicacionesservice.dto;

import java.time.LocalDate;

public record PublicacionResponse(
    Long id,
    String titulo,
    String resumen,
    String contenido,
    LocalDate fecha,
    String autor
) {}
