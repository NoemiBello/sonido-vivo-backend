package cl.sonidovivo.inventarioservice.controller;

import cl.sonidovivo.inventarioservice.dto.*;
import cl.sonidovivo.inventarioservice.model.*;
import cl.sonidovivo.inventarioservice.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
  private final InventarioService service;

  public InventarioController(InventarioService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(summary = "Listar inventario")
  public java.util.List<Inventario> listar(@RequestParam(defaultValue = "false") boolean criticos) {
    return service.listar(criticos);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Consultar por identificador")
  public Inventario obtener(@PathVariable Long id) {
    return service.obtener(id);
  }

  @PostMapping
  @Operation(summary = "Crear registro", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<Inventario> crear(@Valid @RequestBody InventarioRequest d) {
    var result = service.guardar(null, d);
    return ResponseEntity.created(URI.create("/api/inventario/" + result.getId())).body(result);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar registro", security = @SecurityRequirement(name = "bearerAuth"))
  public Inventario actualizar(@PathVariable Long id, @Valid @RequestBody InventarioRequest d) {
    return service.guardar(id, d);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar registro", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/producto/{productoId}")
  public Inventario porProducto(@PathVariable Long productoId) {
    return service.porProducto(productoId);
  }

  @PostMapping("/producto/{productoId}/movimientos")
  @Operation(
      summary = "Entrada positiva o salida negativa de stock",
      security = @SecurityRequirement(name = "bearerAuth"))
  public Inventario mover(@PathVariable Long productoId, @Valid @RequestBody MovimientoRequest d) {
    return service.mover(productoId, d);
  }
}
