package cl.sonidovivo.productosservice.config;

import cl.sonidovivo.productosservice.model.Producto;
import cl.sonidovivo.productosservice.repository.ProductoRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatosIniciales {

  @Bean
  CommandLineRunner productosIniciales(ProductoRepository repo) {
    return args -> {
      if (repo.count() > 0) {
        return;
      }
      crear(repo, "GE001", "Guitarra electrica SGR by Schecter C-1 Gloss Black",
          "Guitarra electrica de seis cuerdas para practica, ensayo y presentaciones.",
          "/img/guitarra-schecter.jpg", 1L, "279900.00", 0);
      crear(repo, "MI001", "Microfono dinamico vocal Shure SM58",
          "Microfono dinamico cardioide para voces en vivo, ensayos y grabaciones.",
          "/img/microfono.jpg", 2L, "129900.00", 10);
      crear(repo, "AM001", "Amplificador Behringer HA-20R",
          "Amplificador para guitarra de 20 W con reverberacion.",
          "/img/amplificador-behringe.jpg", 3L, "145990.00", 0);
      crear(repo, "GA001", "Guitarra Acustica Yamaha F310",
          "Guitarra acustica con tapa de abeto, aros y fondo de meranti. Ideal para principiantes.",
          "/img/guitarra-acustica.jpg", 4L, "129990.00", 0);
      crear(repo, "BA001", "Bajo Electrico Squier Affinity PJ",
          "Bajo electrico de cuatro cuerdas con pickup PJ, cuerpo de alamo y mastil de arce.",
          "/img/bajo-electrico.jpg", 5L, "299990.00", 0);
      crear(repo, "BT001", "Bateria Acustica Pearl Roadshow",
          "Bateria acustica de cinco piezas. Incluye stands, platillos y pedal de bombo.",
          "/img/bateria.jpg", 6L, "599990.00", 0);
      crear(repo, "TC001", "Teclado Digital Yamaha PSR-E373",
          "Teclado digital de 61 teclas sensibles al tacto y 622 voces.",
          "/img/teclado.jpg", 7L, "249990.00", 0);
      crear(repo, "PE001", "Pedal Distorsion Boss DS-1",
          "Clasico pedal de distorsion para guitarra electrica con tres controles.",
          "/img/pedal.jpg", 8L, "79990.00", 0);
      crear(repo, "AC001", "Cuerdas Ernie Ball Super Slinky 09-42",
          "Juego de seis cuerdas para guitarra electrica de calibre ligero.",
          "/img/cuerdas.jpg", 9L, "8990.00", 0);
      crear(repo, "ES001", "Interfaz de Audio Focusrite Scarlett Solo",
          "Interfaz de audio USB con entrada XLR e instrumento, dos salidas y resolucion de 24bit/192kHz.",
          "/img/interfaz.jpg", 10L, "149990.00", 0);
    };
  }

  private void crear(
      ProductoRepository repo,
      String codigo,
      String nombre,
      String descripcion,
      String imagen,
      Long categoriaId,
      String precio,
      int descuento) {
    Producto p = new Producto();
    p.setCodigo(codigo);
    p.setNombre(nombre);
    p.setDescripcion(descripcion);
    p.setImagen(imagen);
    p.setCategoriaId(categoriaId);
    p.setPrecio(new BigDecimal(precio));
    p.setDescuento(descuento);
    p.setActivo(true);
    repo.save(p);
  }
}
