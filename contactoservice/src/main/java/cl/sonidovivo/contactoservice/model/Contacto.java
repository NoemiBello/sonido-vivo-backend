package cl.sonidovivo.contactoservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contactos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100,
          message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Ingresa un correo válido")
    @Size(max = 254)
    @Column(nullable = false, length = 254)
    private String correo;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(min = 4, max = 150,
          message = "El asunto debe tener entre 4 y 150 caracteres")
    @Column(nullable = false, length = 150)
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 10, max = 2000,
          message = "El mensaje debe tener entre 10 y 2000 caracteres")
    @Column(nullable = false, length = 2000)
    private String mensaje;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void registrarFechaCreacion() {
        fechaCreacion = LocalDateTime.now();
    }
}