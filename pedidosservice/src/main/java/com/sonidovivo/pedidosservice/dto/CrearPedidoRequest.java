
package com.sonidovivo.pedidosservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CrearPedidoRequest(

    @NotNull(message = "El usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    Long usuarioId,

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    String nombreCliente,

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
    String direccionEntrega,

    @NotEmpty(message = "El pedido debe contener al menos un producto")
    List<@NotNull @Valid DetallePedidoRequest> detalles

) {
}
