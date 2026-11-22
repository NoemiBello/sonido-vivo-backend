package cl.sonidovivo.authservice.controller;

import cl.sonidovivo.authservice.dto.*;
import cl.sonidovivo.authservice.service.CredencialService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Protegido en SecurityConfig: /api/v1/auth/usuarios/** solo ADMIN.
@RestController
@RequestMapping("/api/v1/auth/usuarios")
public class CredencialController {
  private final CredencialService service;
  public CredencialController(CredencialService service) { this.service = service; }

  @GetMapping public List<CredencialResponse> listar() { return service.listar(); }

  @GetMapping("/{id}") public CredencialResponse buscar(@PathVariable Long id) { return service.buscar(id); }

  @PostMapping
  public ResponseEntity<CredencialResponse> crear(@Valid @RequestBody CredencialRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
  }

  @PutMapping("/{id}")
  public CredencialResponse actualizar(@PathVariable Long id,
      @Valid @RequestBody ActualizarCredencialRequest request) {
    return service.actualizar(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void desactivar(@PathVariable Long id) { service.desactivar(id); }
}
