package cl.sonidovivo.categoriaservice.repository;

import cl.sonidovivo.categoriaservice.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
  boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
