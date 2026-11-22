
package com.sonidovivo.pedidosservice.dto;

import java.math.BigDecimal;

public record ProductoResponse(
    Long id,
    String codigo,
    BigDecimal precio,
    BigDecimal descuento,
    Long categoriaId,
    Boolean activo
) {
}
