package cl.sonidovivo.categoriaservice.client;

import cl.sonidovivo.categoriaservice.exception.ApiException;
import java.time.Duration;
import org.springframework.core.codec.CodecException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;

@Component
public class ProductoClient {
  private final WebClient webClient;
  public ProductoClient(WebClient webClient) { this.webClient = webClient; }

  public boolean tieneProductosPorCategoria(Long categoriaId) {
    try {
      Boolean existe = webClient.get()
          .uri(builder -> builder.path("/api/productos/existe").queryParam("categoriaId", categoriaId).build())
          .retrieve().bodyToMono(Boolean.class).block(Duration.ofSeconds(5));
      if (existe == null) throw new IllegalStateException("Respuesta vacía de Productos");
      return existe;
    } catch (WebClientException | CodecException | IllegalStateException ex) {
      throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
          "No se pudieron verificar los productos asociados.");
    }
  }

}
