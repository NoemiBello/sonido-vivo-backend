
package com.sonidovivo.pedidosservice.dto;

public record InventarioResponse(
    Long id,
    Long productoId,
    Integer stock,
    Integer stockCritico
) {
}
