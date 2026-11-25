package cl.sonidovivo.categoriaservice.controller;

import cl.sonidovivo.categoriaservice.dto.*;
import cl.sonidovivo.categoriaservice.model.*;
import cl.sonidovivo.categoriaservice.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {
  private final CategoriaService service;

  public CategoriaController(CategoriaService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(summary = "Listar categorias")
  public java.util.List<Categoria> listar() {
    return service.listar();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Consultar por identificador")
  public Categoria obtener(@PathVariable Long id) {
    return service.obtener(id);
  }

  @PostMapping
  @Operation(summary = "Crear registro", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaRequest d) {
    var result = service.guardar(null, d);
    return ResponseEntity.created(URI.create("/api/v1/categorias/" + result.getId())).body(result);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar registro", security = @SecurityRequirement(name = "bearerAuth"))
  public Categoria actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest d) {
    return service.guardar(id, d);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar registro", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}
