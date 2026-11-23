package cl.sonidovivo.productosservice.client;
import cl.sonidovivo.productosservice.dto.CategoriaDTO;
import cl.sonidovivo.productosservice.exception.ApiException;
import java.time.Duration;
import org.springframework.core.codec.CodecException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;

@Component
public class CategoriaClient {
  private final WebClient webClient;
  public CategoriaClient(WebClient webClient) { this.webClient = webClient; }

  public CategoriaDTO obtenerPorId(Long id) {
    try {
      CategoriaDTO datos = webClient.get().uri("/api/categorias/{id}", id)
          .retrieve().bodyToMono(CategoriaDTO.class).block(Duration.ofSeconds(5));
      if (datos == null || datos.id() == null) throw new IllegalStateException("Respuesta vacía");
      return datos;
    } catch (WebClientResponseException.NotFound ex) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "La categoría no existe.");
    } catch (WebClientException | CodecException | IllegalStateException ex) {
      throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
          "No se pudo verificar la categoría. Intenta nuevamente.");
    }
  }

}
