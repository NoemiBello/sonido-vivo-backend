
package cl.sonidovivo.contactoservice.dto;

import java.time.LocalDateTime;

public record ContactoResponse(
        Long id,
        String nombre,
        String correo,
        String asunto,
        String mensaje,
        LocalDateTime fechaCreacion
) {}
