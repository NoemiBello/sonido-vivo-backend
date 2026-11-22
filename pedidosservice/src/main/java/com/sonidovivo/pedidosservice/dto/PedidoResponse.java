
package com.sonidovivo.pedidosservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(

    Long id,

    Long usuarioId,

    String nombreCliente,

    String direccionEntrega,

    BigDecimal total,

    String estado,

    LocalDateTime fechaCreacion,

    List<DetallePedidoResponse> detalles

) {
}
