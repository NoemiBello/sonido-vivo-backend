
package cl.sonidovivo.publicacionesservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PublicacionRequest(

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 5, max = 150,
          message = "El titulo debe tener entre 5 y 150 caracteres")
    String titulo,

    @NotBlank(message = "El resumen es obligatorio")
    @Size(min = 10, max = 500,
          message = "El resumen debe tener entre 10 y 500 caracteres")
    String resumen,

    @NotBlank(message = "El contenido es obligatorio")
    @Size(min = 20, max = 20000,
          message = "El contenido debe tener entre 20 y 20000 caracteres")
    String contenido,

    @NotNull(message = "La fecha es obligatoria")
    LocalDate fecha,

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 3, max = 100,
          message = "El autor debe tener entre 3 y 100 caracteres")
    String autor

) {}
