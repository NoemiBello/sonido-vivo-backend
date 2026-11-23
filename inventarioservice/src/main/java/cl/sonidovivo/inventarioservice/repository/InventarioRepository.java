package cl.sonidovivo.inventarioservice.repository;

import cl.sonidovivo.inventarioservice.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
  java.util.Optional<Inventario> findByProductoId(Long productoId);

  boolean existsByProductoId(Long productoId);

  // FOR UPDATE sin cláusula OF es compatible con MySQL y MariaDB de XAMPP.
  // El método se ejecuta dentro de la transacción del servicio.
  @org.springframework.data.jpa.repository.Query(
      value = "select * from inventario where producto_id = :productoId for update",
      nativeQuery = true)
  java.util.Optional<Inventario> bloquearPorProducto(Long productoId);
}
