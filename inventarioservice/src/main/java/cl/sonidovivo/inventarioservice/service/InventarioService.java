package cl.sonidovivo.inventarioservice.service;

import cl.sonidovivo.inventarioservice.client.ProductoClient;
import cl.sonidovivo.inventarioservice.dto.*;
import cl.sonidovivo.inventarioservice.exception.ApiException;
import cl.sonidovivo.inventarioservice.model.*;
import cl.sonidovivo.inventarioservice.repository.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioService {
  private final InventarioRepository repo;
  private final ProductoClient cliente;

  public InventarioService(InventarioRepository repo, ProductoClient cliente) {
    this.repo = repo;
    this.cliente = cliente;
  }

  public Inventario obtener(Long id) {
    return repo.findById(id)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Registro no encontrado."));
  }

  public List<Inventario> listar(boolean criticos) {
    return repo.findAll(org.springframework.data.domain.Sort.by("id")).stream()
        .filter(i -> !criticos || i.getStock() <= i.getStockCritico())
        .toList();
  }

  public Inventario porProducto(Long id) {
    return repo.findByProductoId(id)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inventario no encontrado."));
  }

  @Transactional
  public Inventario guardar(Long id, InventarioRequest d) {
    cliente.obtenerPorId(d.productoId());
    Inventario i;
    if (id == null) {
      if (repo.existsByProductoId(d.productoId()))
        throw new ApiException(HttpStatus.CONFLICT, "El producto ya tiene inventario.");
      i = new Inventario();
      i.setProductoId(d.productoId());
    } else {
      var actual = obtener(id);
      if (!actual.getProductoId().equals(d.productoId()))
        throw new ApiException(
            HttpStatus.CONFLICT, "No se puede cambiar el producto del inventario.");
      i = repo.bloquearPorProducto(d.productoId()).orElseThrow();
    }
    i.setStock(d.stock());
    i.setStockCritico(d.stockCritico());
    return repo.saveAndFlush(i);
  }

  @Transactional
  public void eliminar(Long id) {
    var i = obtener(id);
    i = repo.bloquearPorProducto(i.getProductoId()).orElseThrow();
    if (i.getStock() != 0)
      throw new ApiException(
          HttpStatus.CONFLICT, "El inventario debe estar en cero para eliminarlo.");
    repo.delete(i);
  }

  @Transactional
  public Inventario mover(Long productoId, MovimientoRequest d) {
    cliente.obtenerPorId(productoId);
    var i =
        repo.bloquearPorProducto(productoId)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Inventario no encontrado."));
    long stock = (long) i.getStock() + d.cantidad();
    if (stock < 0) throw new ApiException(HttpStatus.CONFLICT, "Stock insuficiente.");
    if (stock > Integer.MAX_VALUE)
      throw new ApiException(HttpStatus.BAD_REQUEST, "Cantidad demasiado grande.");
    i.setStock((int) stock);
    return repo.saveAndFlush(i);
  }
}
