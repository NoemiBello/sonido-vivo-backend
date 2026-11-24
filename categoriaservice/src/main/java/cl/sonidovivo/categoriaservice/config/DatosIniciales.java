package cl.sonidovivo.categoriaservice.config;

import cl.sonidovivo.categoriaservice.model.Categoria;
import cl.sonidovivo.categoriaservice.repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatosIniciales {

  @Bean
  CommandLineRunner categoriasIniciales(CategoriaRepository repo) {
    return args -> {
      if (repo.count() > 0) {
        return;
      }
      crear(repo, "Guitarras electricas", "Encuentra tu proximo sonido.", "/img/guitarra-schecter.jpg");
      crear(repo, "Microfonos", "Dale protagonismo a tu voz.", "/img/microfono.jpg");
      crear(repo, "Amplificadores", "Potencia para practicar y tocar.", "/img/amplificador-behringe.jpg");
      crear(repo, "Guitarras acusticas", "Sonidos naturales para cada cancion.", "/img/guitarra-acustica.jpg");
      crear(repo, "Bajos electricos", "El ritmo y la profundidad de tu musica.", "/img/bajo-electrico.jpg");
      crear(repo, "Baterias", "Marca el ritmo de tu musica.", "/img/bateria.jpg");
      crear(repo, "Teclados y pianos", "Explora nuevas melodias.", "/img/teclado.jpg");
      crear(repo, "Pedales de efectos", "Personaliza el sonido de tu instrumento.", "/img/pedal.jpg");
      crear(repo, "Accesorios", "Complementos para tus instrumentos.", "/img/cuerdas.jpg");
      crear(repo, "Estudio y grabacion", "Lleva tus grabaciones a otro nivel.", "/img/interfaz.jpg");
    };
  }

  private void crear(CategoriaRepository repo, String nombre, String descripcion, String imagen) {
    Categoria c = new Categoria();
    c.setNombre(nombre);
    c.setDescripcion(descripcion);
    c.setImagen(imagen);
    repo.save(c);
  }
}
