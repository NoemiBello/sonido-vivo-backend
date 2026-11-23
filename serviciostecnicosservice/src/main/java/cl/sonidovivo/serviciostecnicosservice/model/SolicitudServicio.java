package cl.sonidovivo.serviciostecnicosservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Solicitud de reparación de un instrumento de cuerda (página Servicios).
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitudes_servicio")
public class SolicitudServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank @Email
    @Column(nullable = false)
    private String correo;

    private String telefono;

    // guitarra-electrica, guitarra-acustica, bajo u otro (mismos valores del formulario React)
    @NotBlank
    @Column(nullable = false)
    private String instrumento;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String descripcion;

    // PENDIENTE, EN_REVISION, EN_REPARACION, LISTO o ENTREGADO. Lo asigna el servidor.
    @Column(nullable = false)
    private String estado;

    // Fecha en que se recibió la solicitud. La asigna el servidor.
    @Column(nullable = false)
    private LocalDate fechaSolicitud;
}
