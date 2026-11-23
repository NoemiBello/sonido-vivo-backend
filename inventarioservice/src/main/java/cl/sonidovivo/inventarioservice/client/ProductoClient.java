package cl.sonidovivo.inventarioservice.client;
import cl.sonidovivo.inventarioservice.dto.ProductoDTO;
import cl.sonidovivo.inventarioservice.exception.ApiException;
import java.time.Duration;
import org.springframework.core.codec.CodecException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;

@Component
public class ProductoClient {
  private final WebClient webClient;
  public ProductoClient(WebClient webClient) { this.webClient = webClient; }

  public ProductoDTO obtenerPorId(Long id) {
    try {
      ProductoDTO datos = webClient.get().uri("/api/productos/{id}", id)
          .retrieve().bodyToMono(ProductoDTO.class).block(Duration.ofSeconds(5));
      if (datos == null || datos.id() == null) throw new IllegalStateException("Respuesta vacía");
      return datos;
    } catch (WebClientResponseException.NotFound ex) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "El producto no existe.");
    } catch (WebClientException | CodecException | IllegalStateException ex) {
      throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
          "No se pudo verificar el producto. Intenta nuevamente.");
    }
  }

}
