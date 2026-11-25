
package com.sonidovivo.pedidosservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    // Dueño del pedido: correo del JWT de quien compró
    @Column(name = "correo_cliente", updatable = false)
    private String correoCliente;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 255)
    @Column(name = "direccion_entrega", nullable = false)
    private String direccionEntrega;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.01", message = "El total debe ser mayor que cero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, length = 30)
    private String estado = "PENDIENTE";

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(
        mappedBy = "pedido",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<DetallePedido> detalles = new ArrayList<>();

    @PrePersist
    public void antesDeGuardar() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }

        if (estado == null || estado.isBlank()) {
            estado = "PENDIENTE";
        }
    }
}
