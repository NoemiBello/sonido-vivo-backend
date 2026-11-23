package cl.sonidovivo.productosservice.config;
import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
  @Bean
  public WebClient webClient(@Value("${categorias.url}") String url) {
    var httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
    return WebClient.builder().baseUrl(url)
        .clientConnector(new JdkClientHttpConnector(httpClient)).build();
  }
}
