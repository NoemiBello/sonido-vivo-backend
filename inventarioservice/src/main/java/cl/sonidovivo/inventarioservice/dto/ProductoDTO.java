package cl.sonidovivo.inventarioservice.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductoDTO(Long id, String nombre, java.math.BigDecimal precio, boolean activo) {}
