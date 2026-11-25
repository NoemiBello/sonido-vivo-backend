package cl.sonidovivo.productosservice.controller;

import cl.sonidovivo.productosservice.dto.*;
import cl.sonidovivo.productosservice.model.*;
import cl.sonidovivo.productosservice.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {
  private final ProductoService service;

  public ProductoController(ProductoService service) {
    this.service = service;
  }

  @GetMapping("/acceso")
  @Operation(summary = "Validar acceso administrativo", security = @SecurityRequirement(name = "bearerAuth"))
  public java.util.Map<String, String> acceso(org.springframework.security.core.Authentication authentication) {
    return java.util.Map.of("correo", authentication.getName(), "rol", "ADMIN");
  }

  @GetMapping
  @Operation(summary = "Listar productos")
  public java.util.List<Producto> listar(@RequestParam(required = false) Long categoriaId) {
    return service.listar(categoriaId);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Consultar por identificador")
  public Producto obtener(@PathVariable Long id) {
    return service.obtener(id);
  }

  @PostMapping
  @Operation(summary = "Crear registro", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoRequest d) {
    var result = service.guardar(null, d);
    return ResponseEntity.created(URI.create("/api/v1/productos/" + result.getId())).body(result);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar registro", security = @SecurityRequirement(name = "bearerAuth"))
  public Producto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest d) {
    return service.guardar(id, d);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar registro", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/existe")
  public boolean existe(@RequestParam Long categoriaId) {
    return service.existe(categoriaId);
  }
}
