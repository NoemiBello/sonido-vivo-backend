package cl.sonidovivo.productosservice.service;

import cl.sonidovivo.productosservice.client.CategoriaClient;
import cl.sonidovivo.productosservice.dto.*;
import cl.sonidovivo.productosservice.exception.ApiException;
import cl.sonidovivo.productosservice.model.*;
import cl.sonidovivo.productosservice.repository.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {
  private final ProductoRepository repo;
  private final CategoriaClient cliente;

  public ProductoService(ProductoRepository repo, CategoriaClient cliente) {
    this.repo = repo;
    this.cliente = cliente;
  }

  public Producto obtener(Long id) {
    return repo.findById(id)
        .filter(Producto::isActivo)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Registro no encontrado."));
  }

  public List<Producto> listar(Long categoriaId) {
    return categoriaId == null
        ? repo.findByActivoTrueOrderByIdAsc()
        : repo.findByCategoriaIdAndActivoTrueOrderByIdAsc(categoriaId);
  }

  public boolean existe(Long categoriaId) {
    return repo.existsByCategoriaIdAndActivoTrue(categoriaId);
  }

  @Transactional
  public Producto guardar(Long id, ProductoRequest d) {
    var p = id == null ? new Producto() : obtener(id);
    if (repo.existsByCodigoIgnoreCaseAndActivoTrueAndIdNot(
        d.codigo().trim(), id == null ? -1L : id))
      throw new ApiException(HttpStatus.CONFLICT, "Ya existe un producto con ese código.");
    cliente.obtenerPorId(d.categoriaId());
    p.setCodigo(d.codigo().trim().toUpperCase(java.util.Locale.ROOT));
    p.setNombre(d.nombre().trim());
    p.setDescripcion(d.descripcion());
    p.setImagen(d.imagen());
    p.setCategoriaId(d.categoriaId());
    p.setPrecio(d.precio());
    p.setDescuento(d.descuento());
    return repo.saveAndFlush(p);
  }

  @Transactional
  public void eliminar(Long id) {
    var p = obtener(id);
    p.setActivo(false);
    repo.save(p);
  }
}
