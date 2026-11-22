package cl.sonidovivo.publicacionesservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "publicaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 5, max = 150,
          message = "El titulo debe tener entre 5 y 150 caracteres")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "El resumen es obligatorio")
    @Size(min = 10, max = 500,
          message = "El resumen debe tener entre 10 y 500 caracteres")
    @Column(nullable = false, length = 500)
    private String resumen;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(min = 20, max = 20000,
          message = "El contenido debe tener entre 20 y 20000 caracteres")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 3, max = 100,
          message = "El autor debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String autor;
}