package cl.sonidovivo.inventarioservice.config;

import cl.sonidovivo.inventarioservice.model.Inventario;
import cl.sonidovivo.inventarioservice.repository.InventarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatosIniciales {

  @Bean
  CommandLineRunner inventarioInicial(InventarioRepository repo) {
    return args -> {
      if (repo.count() > 0) {
        return;
      }
      crear(repo, 1L, 5, 2);
      crear(repo, 2L, 8, 2);
      crear(repo, 3L, 5, 2);
      crear(repo, 4L, 8, 2);
      crear(repo, 5L, 5, 2);
      crear(repo, 6L, 2, 2);
      crear(repo, 7L, 4, 2);
      crear(repo, 8L, 7, 2);
      crear(repo, 9L, 25, 2);
      crear(repo, 10L, 4, 2);
    };
  }

  private void crear(InventarioRepository repo, Long productoId, int stock, int stockCritico) {
    Inventario inv = new Inventario();
    inv.setProductoId(productoId);
    inv.setStock(stock);
    inv.setStockCritico(stockCritico);
    repo.save(inv);
  }
}
