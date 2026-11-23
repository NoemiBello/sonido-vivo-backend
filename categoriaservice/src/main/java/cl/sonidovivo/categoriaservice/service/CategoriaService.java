package cl.sonidovivo.categoriaservice.service;

import cl.sonidovivo.categoriaservice.client.ProductoClient;
import cl.sonidovivo.categoriaservice.dto.*;
import cl.sonidovivo.categoriaservice.exception.ApiException;
import cl.sonidovivo.categoriaservice.model.*;
import cl.sonidovivo.categoriaservice.repository.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {
  private final CategoriaRepository repo;
  private final ProductoClient cliente;

  public CategoriaService(CategoriaRepository repo, ProductoClient cliente) {
    this.repo = repo;
    this.cliente = cliente;
  }

  public Categoria obtener(Long id) {
    return repo.findById(id)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Registro no encontrado."));
  }

  public List<Categoria> listar() {
    return repo.findAll(org.springframework.data.domain.Sort.by("id"));
  }

  @Transactional
  public Categoria guardar(Long id, CategoriaRequest d) {
    var c = id == null ? new Categoria() : obtener(id);
    if (repo.existsByNombreIgnoreCaseAndIdNot(d.nombre().trim(), id == null ? -1L : id))
      throw new ApiException(HttpStatus.CONFLICT, "Ya existe esa categoría.");
    c.setNombre(d.nombre().trim());
    c.setDescripcion(d.descripcion());
    c.setImagen(d.imagen());
    return repo.saveAndFlush(c);
  }

  @Transactional
  public void eliminar(Long id) {
    var c = obtener(id);
    if (cliente.tieneProductosPorCategoria(id))
      throw new ApiException(
          HttpStatus.CONFLICT,
          "Reasigna o elimina los productos de esta categoría antes de eliminarla.");
    repo.delete(c);
  }
}
