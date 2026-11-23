package cl.sonidovivo.productosservice.repository;

import cl.sonidovivo.productosservice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
  boolean existsByCodigoIgnoreCaseAndActivoTrueAndIdNot(String codigo, Long id);

  java.util.List<Producto> findByActivoTrueOrderByIdAsc();

  java.util.List<Producto> findByCategoriaIdAndActivoTrueOrderByIdAsc(Long categoriaId);

  boolean existsByCategoriaIdAndActivoTrue(Long categoriaId);
}
