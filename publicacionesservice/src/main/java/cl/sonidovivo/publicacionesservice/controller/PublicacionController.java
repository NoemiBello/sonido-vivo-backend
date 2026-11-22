
package cl.sonidovivo.publicacionesservice.controller;

import cl.sonidovivo.publicacionesservice.dto.PublicacionRequest;
import cl.sonidovivo.publicacionesservice.dto.PublicacionResponse;
import cl.sonidovivo.publicacionesservice.service.PublicacionService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    // LISTAR PUBLICACIONES
    @GetMapping
    public ResponseEntity<List<PublicacionResponse>> listar() {
        return ResponseEntity.ok(publicacionService.listar());
    }

    // BUSCAR PUBLICACION POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PublicacionResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(publicacionService.buscarPorId(id));
    }

    // CREAR PUBLICACION
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<PublicacionResponse> crear(
            @Valid @RequestBody PublicacionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(publicacionService.crear(request));
    }

    // ACTUALIZAR PUBLICACION
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PublicacionRequest request) {

        return ResponseEntity.ok(publicacionService.actualizar(id, request));
    }

    // ELIMINAR PUBLICACION
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        publicacionService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
